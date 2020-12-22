package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.auth.bean.User;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintAnonymousEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintStateEnum;
import com.aquilaflycloud.mdc.mapper.ShopComplaintInfoMapper;
import com.aquilaflycloud.mdc.mapper.ShopInfoMapper;
import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.shop.ShopComplaintStatisticsResult;
import com.aquilaflycloud.mdc.result.shop.ShopComplaintTopInfoResult;
import com.aquilaflycloud.mdc.service.ShopComplaintInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ShopComplaintInfoServiceImpl implements ShopComplaintInfoService {

    @Resource
    private ShopComplaintInfoMapper shopComplaintInfoMapper;

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public IPage<ShopComplaintInfo> page(ShopComplaintInfoPageParam param) {
        return shopComplaintInfoMapper.selectPage(param.page(), Wrappers.<ShopComplaintInfo>lambdaQuery()
                .eq(ObjectUtil.isNotNull(param.getState()), ShopComplaintInfo::getState, param.getState())
                .ge(ObjectUtil.isNotNull(param.getStartTime()), ShopComplaintInfo::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotNull(param.getEndTime()), ShopComplaintInfo::getCreateTime, param.getEndTime())
                .orderByDesc(ShopComplaintInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public ShopComplaintInfo get(ShopComplaintInfoGetParam param) {
        return shopComplaintInfoMapper.selectById(param.getId());
    }

    @Override
    public void edit(ShopComplaintInfoEditParam param) {

        ShopComplaintInfo info = new ShopComplaintInfo();
        BeanUtil.copyProperties(param, info);

        //设置审核时间和申诉时间
        if (ObjectUtil.equal(ShopComplaintStateEnum.PASS, param.getState())
                || ObjectUtil.equal(ShopComplaintStateEnum.NOPASS, param.getState())) {
            info.setLastUpdateTime(new Date());
            info.setLastOperatorId(MdcUtil.getCurrentUserId());
            info.setLastOperatorName(MdcUtil.getCurrentUserName());
        } else if (ObjectUtil.equal(ShopComplaintStateEnum.APPEAL_SUCCESS, param.getState())) {
            info.setAppealTime(new Date());
            info.setAppealId(MdcUtil.getCurrentUserId());
            info.setAppealName(MdcUtil.getCurrentUserName());
        }

        int count = shopComplaintInfoMapper.updateById(info);

        if (count <=0){
            throw new ServiceException("编辑信息失败，请重试");
        }
    }

    @Override
    public ShopComplaintStatisticsResult statistics(ShopComplaintStatisticsParam param) {
        //统计top10
        String authSql = param.getDataAuthParam().getAliasCompleteSql("a");
        List<ShopComplaintTopInfoResult> topInfo = shopComplaintInfoMapper.statistics(authSql);

        //2统计各个状态数量
        //2.1待处理投诉数量
        Long acceptCount = new Long(shopComplaintInfoMapper.selectCount(Wrappers.<ShopComplaintInfo> lambdaQuery()
                .eq(ShopComplaintInfo::getState, ShopComplaintStateEnum.ACCEPT)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ));

        //2.2已通过投诉数量
        Long passCount = new Long(shopComplaintInfoMapper.selectCount(Wrappers.<ShopComplaintInfo> lambdaQuery()
                .eq(ShopComplaintInfo::getState, ShopComplaintStateEnum.PASS)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ));

        //2.3申诉成功投诉数量
        Long appealSuccessCount = new Long(shopComplaintInfoMapper.selectCount(Wrappers.<ShopComplaintInfo> lambdaQuery()
                .eq(ShopComplaintInfo::getState, ShopComplaintStateEnum.APPEAL_SUCCESS)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ));

        return new ShopComplaintStatisticsResult(topInfo, acceptCount, passCount, appealSuccessCount);
    }

    @Override
    public IPage<ShopComplaintInfo> getPageComplaintInfo(ShopComplaintInfoPageApiParam param) {
        //判断是否是登录用户
        MemberInfoResult member = MdcUtil.getCurrentMember();
        LambdaQueryWrapper<ShopComplaintInfo> queryWrapper = Wrappers.<ShopComplaintInfo>lambdaQuery()
                .eq(ShopComplaintInfo::getShopId, param.getShopId())
                .orderByDesc(ShopComplaintInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams());

        //有会员登录信息
        if (null != member && ObjectUtil.isNotNull(member.getId())) {
            queryWrapper.and(j->j.eq(ShopComplaintInfo::getState, ShopComplaintStateEnum.PASS).or(k->k.ne(ShopComplaintInfo::getState, ShopComplaintStateEnum.PASS)
                    .eq(ShopComplaintInfo::getMemberId, member.getId())));
        } else {
            queryWrapper.eq(ShopComplaintInfo::getState, ShopComplaintStateEnum.PASS);
        }

        return shopComplaintInfoMapper.selectPage(param.page(), queryWrapper).convert(this::handleComplaintInfo);

    }

    @Override
    public void addComplaintInfo(ShopComplaintInfoAddApiParam param) {
        MemberInfoResult member = MdcUtil.getRequireCurrentMember();

        //获取商户信息
        ShopInfo shopInfo = shopInfoMapper.selectById(param.getShopId());

        ShopComplaintInfo info = new ShopComplaintInfo();
        BeanUtil.copyProperties(param, info);

        MdcUtil.setMemberInfo(info, member);
        info.setTenantId(member.getTenantId());
        info.setState(ShopComplaintStateEnum.ACCEPT);

        if (null != shopInfo) {
            info.setShopFullName(shopInfo.getShopName());
            info.setSubTenantId(shopInfo.getRelationId());
        }

        int count = shopComplaintInfoMapper.normalInsert(info);

        if (count <= 0) {
            throw new ServiceException("添加投诉内容失败，请重试");
        }
    }

    @Override
    public void delete(ShopComplaintInfoDeleteParam param) {
        int count = shopComplaintInfoMapper.deleteById(param.getId());

        if (count <= 0) {
            throw new ServiceException("删除投诉内容失败，请重试");
        }
    }

    private ShopComplaintInfo handleComplaintInfo(ShopComplaintInfo info) {
        MemberInfoResult member = MdcUtil.getCurrentMember();
        //有会员登录信息且与当前信息的会员id相等，则设置为昵称为我
        if (null != member && ObjectUtil.isNotNull(member.getId()) && ObjectUtil.equal(member.getId(), info.getMemberId())) {
            info.setNickName("我");
        } else if (ObjectUtil.equal(info.getIsAnonymous(), ShopComplaintAnonymousEnum.ANONYMOUS) || StrUtil.isBlank(info.getNickName())) {
            //当前用户未登录设置匿名
            info.setNickName("匿名用户");
        }

        return info;
    }
}
