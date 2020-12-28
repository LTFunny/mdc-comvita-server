package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aquilafly.easypay.req.QueryOtherOrderReq;
import com.aquilafly.easypay.resp.QueryOtherOrderResp;
import com.aquilafly.easypay.util.EasyPayUtil;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.member.ConsumptionTicketStateEnum;
import com.aquilaflycloud.mdc.enums.member.MemberScanTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopShowListPicEnum;
import com.aquilaflycloud.mdc.feign.consumer.org.ShopConsumer;
import com.aquilaflycloud.mdc.mapper.EasypayRecordMapper;
import com.aquilaflycloud.mdc.mapper.MemberInfoMapper;
import com.aquilaflycloud.mdc.mapper.MemberScanRecordMapper;
import com.aquilaflycloud.mdc.mapper.ShopInfoMapper;
import com.aquilaflycloud.mdc.message.RewardErrorEnum;
import com.aquilaflycloud.mdc.model.easypay.EasypayRecord;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.member.MemberScanResult;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.MemberScanService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.org.service.provider.entity.PShopInfo;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum.EASYPAYQUERY;

/**
 * MemberScanServiceImpl
 *
 * @author star
 * @date 2020-03-27
 */
@Service
public class MemberScanServiceImpl implements MemberScanService {
    @Resource
    private MemberScanRecordMapper memberScanRecordMapper;
    @Resource
    private EasypayRecordMapper easypayRecordMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private MemberService memberService;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private ShopInfoMapper shopInfoMapper;
    @Resource
    private ShopConsumer shopConsumer;

    private Long getFormatId(String merchantNo) {
        //获取商户业态id
        Long formatId = null;
        if (StrUtil.isNotBlank(merchantNo)) {
            PShopInfo pShopInfo = shopConsumer.getByPaymentSystemNumber(merchantNo);
            if (pShopInfo != null) {
                formatId = pShopInfo.getFormatId();
            }
        }
        return formatId;
    }

    @Transactional
    @Override
    public MemberScanResult addScanInfo(ScanInfoAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        RedisUtil.transactionalLock("memberScanLock" + memberInfo.getId());
        Integer scanCount = memberScanRecordMapper.selectCount(Wrappers.<MemberScanRecord>lambdaQuery()
                .eq(MemberScanRecord::getOrderNo, param.getOrderNo())
        );
        if (scanCount > 0) {
            throw RewardErrorEnum.REWARD_ERROR_10601.getErrorMeta().getException();
        }
        QueryOtherOrderResp resp = EasyPayUtil.queryOtherOrder(new QueryOtherOrderReq().setP1_OrderNo(param.getOrderNo()),
                MdcUtil.getCurrentTenantId().toString() + EASYPAYQUERY.getType());
        if (!StrUtil.equals(resp.getRa_Code(), "100")) {
            throw new ServiceException(resp.getRb_CodeMsg());
        }
        MemberScanRecord record = new MemberScanRecord();
        MdcUtil.setMemberInfo(record, memberInfo);
        record.setMerchantNo(resp.getR00_MerchantNo());
        record.setOrderNo(resp.getR01_OrderNo());
        record.setPayNo(resp.getR05_TrxNo());
        record.setCardNo(resp.getR07_CardNo());
        record.setProductName(resp.getR04_ProductName());
        record.setPayMoney(new BigDecimal(resp.getR03_Amount()));
        record.setPayTime(DateUtil.parse(resp.getR06_PaySuccessTime()));
        record.setPayType(EnumUtil.likeValueOf(PaymentTypeEnum.class, Convert.toInt(resp.getR02_PaymentType())));
        Map<RewardTypeEnum, MemberScanRewardResult> rewardResult = memberRewardService.addScanRewardRecord(memberInfo, getFormatId(record.getMerchantNo()), record.getPayMoney(), param.getIgnore());
        if (CollUtil.isEmpty(rewardResult)) {
            throw new ServiceException("消费奖励未启用");
        }
        List<MemberScanRewardResult> list = CollUtil.newArrayList(rewardResult.values());
        boolean success = false;
        for (MemberScanRewardResult result : list) {
            success = result.getOverLimit();
            if (success) {
                break;
            }
        }
        if (!success) {
            record.setRewardValueContent(JSONUtil.toJsonStr(list));
            record.setType(MemberScanTypeEnum.SCAN);
            int count = memberScanRecordMapper.insert(record);
            if (count <= 0) {
                throw new ServiceException("保存订单记录失败");
            }
        }
        MemberScanResult result = new MemberScanResult();
        result.setRewardList(list);
        return result;
    }

    @Transactional
    @Override
    public void addConsume(ConsumeAddParam param) {
        EasypayRecord easypayRecord = new EasypayRecord();
        BeanUtil.copyProperties(param, easypayRecord);
        int count = easypayRecordMapper.insertIgnoreAllBatch(CollUtil.toList(easypayRecord));
        if (count > 0) {
            if (StrUtil.isBlank(easypayRecord.getMemberCode()) || StrUtil.isBlank(easypayRecord.getOrderNo()) || easypayRecord.getPayMoney() == null) {
                return;
            }
            MemberInfo memberInfo = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(MemberInfo::getMemberCode, easypayRecord.getMemberCode())
            );
            if (memberInfo == null) {
                return;
            }
            int scanCount = memberScanRecordMapper.selectCount(Wrappers.<MemberScanRecord>lambdaQuery()
                    .eq(MemberScanRecord::getOrderNo, easypayRecord.getOrderNo())
            );
            if (scanCount > 0) {
                return;
            }
            MemberScanRecord record = new MemberScanRecord();
            MdcUtil.setMemberInfo(record, memberInfo);
            record.setMerchantNo(easypayRecord.getMerchantNo());
            record.setOrderNo(easypayRecord.getOrderNo());
            record.setPayNo(easypayRecord.getPayNo());
            record.setCardNo(easypayRecord.getCreditCard());
            record.setProductName(easypayRecord.getProductName());
            record.setPayMoney(easypayRecord.getPayMoney());
            record.setPayTime(easypayRecord.getPayTime());
            record.setPayType(EnumUtil.likeValueOf(PaymentTypeEnum.class, Convert.toInt(easypayRecord.getPayType())));
            Map<RewardTypeEnum, MemberScanRewardResult> rewardResult = memberRewardService.addScanRewardRecord(memberInfo, getFormatId(record.getMerchantNo()), record.getPayMoney(), true);
            if (CollUtil.isEmpty(rewardResult)) {
                return;
            }
            List<MemberScanRewardResult> list = CollUtil.newArrayList(rewardResult.values());
            record.setRewardValueContent(JSONUtil.toJsonStr(list));
            record.setType(MemberScanTypeEnum.SCAN);
            int insertCount = memberScanRecordMapper.insert(record);
            if (insertCount <= 0) {
                throw new ServiceException("保存消费奖励失败");
            }
        }
    }

    @Transactional
    @Override
    public MemberScanResult addQrcodeScanConsume(String orderNo, String openId, String miniAppId) {
        EasypayRecord easypayRecord = easypayRecordMapper.selectOne(Wrappers.<EasypayRecord>lambdaQuery()
                .eq(EasypayRecord::getOrderNo, orderNo)
        );
        if (easypayRecord == null) {
            throw new ServiceException("该订单不存在喔");
        }
        MemberInfo memberInfo = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getOpenId, openId)
        );
        if (memberInfo == null || StrUtil.isBlank(memberInfo.getUnionId())) {
            throw new ServiceException("找不到该会员喔");
        }
        memberInfo = memberService.getUnifiedMember(Wrappers.<MemberInfo>lambdaQuery()
                .eq(MemberInfo::getUnionId, memberInfo.getUnionId())
                .eq(MemberInfo::getWxAppId, miniAppId)
        );
        if (memberInfo == null) {
            throw new ServiceException("您还不是会员喔");
        }
        int count = memberScanRecordMapper.selectCount(Wrappers.<MemberScanRecord>lambdaQuery()
                .eq(MemberScanRecord::getOrderNo, easypayRecord.getOrderNo())
        );
        if (count > 0) {
            throw RewardErrorEnum.REWARD_ERROR_10601.getErrorMeta().getException();
        }
        MemberScanRecord record = new MemberScanRecord();
        MdcUtil.setMemberInfo(record, memberInfo);
        record.setMerchantNo(easypayRecord.getMerchantNo());
        record.setOrderNo(easypayRecord.getOrderNo());
        record.setPayNo(easypayRecord.getPayNo());
        record.setCardNo(easypayRecord.getCreditCard());
        record.setProductName(easypayRecord.getProductName());
        record.setPayMoney(easypayRecord.getPayMoney());
        record.setPayTime(easypayRecord.getPayTime());
        record.setPayType(EnumUtil.likeValueOf(PaymentTypeEnum.class, Convert.toInt(easypayRecord.getPayType())));
        Map<RewardTypeEnum, MemberScanRewardResult> rewardResult = memberRewardService.addScanRewardRecord(memberInfo, getFormatId(record.getMerchantNo()), record.getPayMoney(), true);
        if (CollUtil.isEmpty(rewardResult)) {
            throw new ServiceException("消费奖励未启用");
        }
        List<MemberScanRewardResult> list = CollUtil.newArrayList(rewardResult.values());
        record.setRewardValueContent(JSONUtil.toJsonStr(list));
        record.setType(MemberScanTypeEnum.SCAN);
        count = memberScanRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("保存订单记录失败");
        }
        MemberScanResult result = new MemberScanResult();
        result.setRewardList(list);
        return result;
    }

    @Override
    public IPage<MemberScanRecord> pageInfo(MemberConsumptionTicketPageInfoParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();

        return memberScanRecordMapper.selectPage(param.page(), Wrappers.<MemberScanRecord>lambdaQuery()
                .eq(MemberScanRecord::getMemberId, memberId)
                .eq(MemberScanRecord::getType, MemberScanTypeEnum.PHOTO)
                .orderByDesc(MemberScanRecord::getCreateTime)
        ).convert(item -> {
            String reward = item.getRewardValueContent();
            StringBuffer rewardResult = new StringBuffer();

            if (StrUtil.isNotBlank(reward)) {
                JSONArray rewardArr = JSONUtil.parseArray(reward);
                List<MemberScanRewardResult> memberScanRewardResults = rewardArr.toList(MemberScanRewardResult.class);
                for (MemberScanRewardResult result : memberScanRewardResults) {
                    rewardResult.append(result.getCanReward() > 0 ? "+" + result.getCanReward() : result.getCanReward()).append(result.getRewardType().getName()).append(",");
                }
            }

            String rewardStr = rewardResult.toString();
            item.setRewardValueContent(StrUtil.isBlank(rewardStr) ? rewardStr : rewardStr.substring(0, rewardStr.length() - 1));

            return item;
        });
    }

    @Override
    public void addConsumptionTicketnfo(MemberConsumptionTicketAddInfoParam param) {
        MemberInfoResult memberInfo = MdcUtil.getRequireCurrentMember();

        if (ObjectUtil.isNull(param.getShopId()) && ObjectUtil.isNull(param.getShopName())) {
            throw new ServiceException("店铺名称不能为空，请重试");
        }

        MemberScanRecord info = new MemberScanRecord();
        BeanUtil.copyProperties(param, info);
        MdcUtil.setMemberInfo(info, memberInfo);
        info.setState(ConsumptionTicketStateEnum.NO_AUDIT);
        info.setType(MemberScanTypeEnum.PHOTO);

        if (ObjectUtil.isNotNull(param.getShopId())) {
            //系统存在该店铺
            ShopInfo shopInfo = shopInfoMapper.selectById(param.getShopId());

            if (null == shopInfo) {
                throw new ServiceException("所选店铺不存在，请重试");
            }

            //店铺使用列表图和列表图url存在时
            if (ObjectUtil.equal(ShopShowListPicEnum.USE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getListPicUrl())) {
                info.setShopUrl(shopInfo.getListPicUrl());
            } else if (ObjectUtil.equal(ShopShowListPicEnum.NOUSE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getShopLogo())) {
                info.setShopUrl(shopInfo.getShopLogo());
            }

            info.setShopName(shopInfo.getShopName());
        }

        int count = memberScanRecordMapper.insert(info);

        if (count <= 0) {
            throw new ServiceException("保存失败，请重试");
        }
    }

    @Override
    public IPage<MemberScanRecord> pageConsumptionTicket(MemberConsumptionTicketPageParam param) {
        return memberScanRecordMapper.selectPage(param.page(), Wrappers.<MemberScanRecord>lambdaQuery()
                .ge(ObjectUtil.isNotNull(param.getCreateStartTime()), MemberScanRecord::getCreateTime, param.getCreateStartTime())
                .le(ObjectUtil.isNotNull(param.getCreateEndTime()), MemberScanRecord::getCreateTime, param.getCreateEndTime())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberScanRecord::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), MemberScanRecord::getPhoneNumber, param.getPhoneNumber())
                .ge(ObjectUtil.isNotNull(param.getStartAmount()), MemberScanRecord::getPayMoney, param.getStartAmount())
                .le(ObjectUtil.isNotNull(param.getEndAmount()), MemberScanRecord::getPayMoney, param.getEndAmount())
                .eq(ObjectUtil.isNotNull(param.getState()), MemberScanRecord::getState, param.getState())
                .like(StrUtil.isNotBlank(param.getShopName()), MemberScanRecord::getShopName, param.getShopName())
                .like(StrUtil.isNotBlank(param.getProductName()), MemberScanRecord::getProductName, param.getProductName())
                .like(StrUtil.isNotBlank(param.getAuditName()), MemberScanRecord::getAuditName, param.getAuditName())
                .eq(ObjectUtil.isNotNull(param.getType()), MemberScanRecord::getType, param.getType())
                .orderByDesc(MemberScanRecord::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(item -> {
            String reward = item.getRewardValueContent();
            StringBuilder rewardResult = new StringBuilder();
            if (StrUtil.isNotBlank(reward)) {
                JSONArray rewardArr = JSONUtil.parseArray(reward);
                List<MemberScanRewardResult> memberScanRewardResults = rewardArr.toList(MemberScanRewardResult.class);
                for (MemberScanRewardResult result : memberScanRewardResults) {
                    rewardResult.append(result.getCanReward() > 0 ? "+" + result.getCanReward() : result.getCanReward()).append(result.getRewardType().getName()).append(",");
                }
            }
            String rewardStr = rewardResult.toString();
            item.setRewardValueContent(StrUtil.isBlank(rewardStr) ? rewardStr : rewardStr.substring(0, rewardStr.length() - 1));
            return item;
        });
    }

    @Override
    public MemberScanRecord getConsumptionTicket(MemberConsumptionTicketGetParam param) {
        return memberScanRecordMapper.selectById(param.getId());
    }

    @Transactional
    @Override
    public void auditConsumptionTicket(MemberConsumptionTicketAuditParam param) {
        MemberScanRecord updateInfo = new MemberScanRecord();
        BeanUtil.copyProperties(param, updateInfo);
        updateInfo.setAuditId(MdcUtil.getCurrentUserId());
        updateInfo.setAuditName(MdcUtil.getCurrentUserName());

        //通过审核则生成奖励数据
        if (ObjectUtil.equal(ConsumptionTicketStateEnum.PASS, param.getState())) {
            //查询对应的消费小票记录
            MemberScanRecord info = memberScanRecordMapper.selectById(param.getId());
            MemberInfo memberInfo = memberService.getUnifiedMember(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(StrUtil.isNotBlank(info.getUserId()), MemberInfo::getAliAppId, info.getAppId())
                    .eq(StrUtil.isNotBlank(info.getOpenId()), MemberInfo::getWxAppId, info.getAppId())
                    .eq(StrUtil.isAllBlank(info.getUserId(), info.getOpenId()), MemberInfo::getId, info.getMemberId())
            );
            if (null == info || null == memberInfo) {
                throw new ServiceException("未找到对应的记录，请重试");
            }

            //获取奖励数据
            Map<RewardTypeEnum, MemberScanRewardResult> rewardMap = memberRewardService.addScanRewardRecord(memberInfo, null, info.getPayMoney(), true);

            if (null != rewardMap && rewardMap.size() > 0) {
                JSONArray rewardArray = JSONUtil.parseArray(rewardMap.values());
                updateInfo.setRewardValueContent(rewardArray.toString());
            }
        }

        if (ObjectUtil.isNotNull(param.getShopId())) {
            //系统存在该店铺
            ShopInfo shopInfo = shopInfoMapper.selectById(param.getShopId());

            if (null == shopInfo) {
                throw new ServiceException("所选店铺不存在，请重试");
            }

            //店铺使用列表图和列表图url存在时
            if (ObjectUtil.equal(ShopShowListPicEnum.USE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getListPicUrl())) {
                updateInfo.setShopUrl(shopInfo.getListPicUrl());
            } else if (ObjectUtil.equal(ShopShowListPicEnum.NOUSE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getShopLogo())) {
                updateInfo.setShopUrl(shopInfo.getShopLogo());
            }

            updateInfo.setShopName(shopInfo.getShopName());
        }


        int count = memberScanRecordMapper.updateById(updateInfo);

        if (count <= 0) {
            throw new ServiceException("更新失败，请重试");
        }
    }

    @Override
    public void editConsumptionTicket(MemberConsumptionTicketEditParam param) {
        if (ObjectUtil.isNull(param.getShopId()) && ObjectUtil.isNull(param.getShopName())) {
            throw new ServiceException("店铺名称不能为空，请重试");
        }

        MemberScanRecord info = new MemberScanRecord();
        BeanUtil.copyProperties(param, info);

        if (ObjectUtil.isNotNull(param.getShopId())) {
            //系统存在该店铺
            ShopInfo shopInfo = shopInfoMapper.selectById(param.getShopId());

            if (null == shopInfo) {
                throw new ServiceException("所选店铺不存在，请重试");
            }

            //店铺使用列表图和列表图url存在时
            if (ObjectUtil.equal(ShopShowListPicEnum.USE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getListPicUrl())) {
                info.setShopUrl(shopInfo.getListPicUrl());
            } else if (ObjectUtil.equal(ShopShowListPicEnum.NOUSE, shopInfo.getShowListPic()) && StrUtil.isNotBlank(shopInfo.getShopLogo())) {
                info.setShopUrl(shopInfo.getShopLogo());
            }

            info.setShopName(shopInfo.getShopName());
        }

        int count = memberScanRecordMapper.updateById(info);

        if (count <= 0) {
            throw new ServiceException("更新失败，请重试");
        }
    }

    @Override
    public MemberScanRecord preNextConsumptionTicketInfo(MemberConsumptionTicketPreNextInfoParam param) {
        //判断pageNum要>0
        if (!(param.getPageNum() > 0)) {
            throw new ServiceException("页数要大于0");
        }

        if (param.getPageSize() != 1) {
            throw new ServiceException("页大小不等于1");
        }

        IPage<MemberScanRecord> info = memberScanRecordMapper.selectPage(param.page(), Wrappers.<MemberScanRecord>lambdaQuery()
                .ge(ObjectUtil.isNotNull(param.getCreateStartTime()), MemberScanRecord::getCreateTime, param.getCreateStartTime())
                .le(ObjectUtil.isNotNull(param.getCreateEndTime()), MemberScanRecord::getCreateTime, param.getCreateEndTime())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberScanRecord::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), MemberScanRecord::getPhoneNumber, param.getPhoneNumber())
                .ge(ObjectUtil.isNotNull(param.getStartAmount()), MemberScanRecord::getPayMoney, param.getStartAmount())
                .le(ObjectUtil.isNotNull(param.getEndAmount()), MemberScanRecord::getPayMoney, param.getEndAmount())
                .eq(ObjectUtil.isNotNull(param.getState()), MemberScanRecord::getState, param.getState())
                .like(StrUtil.isNotBlank(param.getAuditName()), MemberScanRecord::getAuditName, param.getAuditName())
                .eq(ObjectUtil.isNotNull(param.getType()), MemberScanRecord::getType, param.getType())
                .orderByDesc(MemberScanRecord::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //获取该记录
        if (null != info && null != info.getRecords() && info.getRecords().size() == 1) {
            return info.getRecords().get(0);
        }

        return null;
    }
}
