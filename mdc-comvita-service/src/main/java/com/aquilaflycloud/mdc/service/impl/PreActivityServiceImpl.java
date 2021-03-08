package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.result.OssResult;
import com.aquilaflycloud.util.AliOssUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PreActivityServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreActivityServiceImpl implements PreActivityService {
    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;
    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;
    @Resource
    private PreGoodsInfoMapper preGoodsInfoMapper;
    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreActivityQrCodeInfoMapper preActivityQrCodeInfoMapper;
    @Resource
    private PreFlashOrderInfoMapper preFlashOrderInfoMapper;
    @Resource
    private FolksonomyBusinessRelMapper folksonomyBusinessRelMapper;
    @Resource
    private FolksonomyService folksonomyService;
    @Resource
    private WechatMiniService wechatMiniService;
    @Resource
    private MemberEventLogService memberEventLogService;

    private PreActivityInfo stateHandler(PreActivityInfo info) {
        if (info == null) {
            throw new ServiceException("活动不存在");
        }
        DateTime now = DateTime.now();
        if (info.getActivityState() != ActivityStateEnum.CANCELED) {
            if (now.isAfterOrEquals(info.getBeginTime()) && now.isBeforeOrEquals(info.getEndTime())) {
                info.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(info.getBeginTime())) {
                info.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(info.getEndTime())) {
                info.setActivityState(ActivityStateEnum.FINISHED);
            }
        }
        return info;
    }

    @Override
    public IPage<PreActivityPageApiResult> pagePreActivity(PreActivityPageParam param) {
        ActivityStateEnum state = param.getActivityState();
        DateTime now = DateTime.now();
        return preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .eq(param.getActivityType() != null, PreActivityInfo::getActivityType, param.getActivityType())
                .ne(PreActivityInfo::getActivityState, ActivityStateEnum.CANCELED)
                .and(state == ActivityStateEnum.NOT_STARTED,
                        j -> j.and(k -> k.ge(PreActivityInfo::getBeginTime, now)))
                .and(state == ActivityStateEnum.IN_PROGRESS,
                        j -> j.and(k -> k.le(PreActivityInfo::getBeginTime, now).ge(PreActivityInfo::getEndTime, now)))
                .and(state == ActivityStateEnum.FINISHED,
                        j -> j.and(k -> k.le(PreActivityInfo::getEndTime, now)))
        ).convert(info -> {
            info = stateHandler(info);
            PreActivityPageApiResult result = BeanUtil.copyProperties(info, PreActivityPageApiResult.class);
            if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
                result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardParam.class));
            }
            return result;
        });
    }

    @Override
    public PreActivityInfoApiResult getPreActivity(PreActivityGetParam param) {
        MemberInfo memberInfo = MdcUtil.getCurrentMember();
        PreActivityInfo info = preActivityInfoMapper.selectById(param.getId());
        info = stateHandler(info);
        PreActivityInfoApiResult result = BeanUtil.copyProperties(info, PreActivityInfoApiResult.class);
        if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
            result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardParam.class));
        }
        if (info.getActivityType() == ActivityTypeEnum.FLASH) {
            if (result.getActivityState() == ActivityStateEnum.IN_PROGRESS) {
                result.setButtonState(ButtonStateEnum.JOIN);
                if (memberInfo != null) {
                    PreFlashOrderInfo orderInfo = preFlashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                            .eq(PreFlashOrderInfo::getActivityInfoId, result.getId())
                            .eq(PreFlashOrderInfo::getMemberId, memberInfo.getId())
                    );
                    if (orderInfo == null) {
                        int orderCount = preOrderInfoMapper.selectCount(Wrappers.<PreOrderInfo>lambdaQuery()
                                .eq(PreOrderInfo::getActivityInfoId, result.getId())
                        );
                        if (orderCount >= result.getMaxParticipationCount()) {
                            result.setButtonState(ButtonStateEnum.FULL);
                        }
                    } else {
                        if (orderInfo.getFlashOrderState() == FlashOrderInfoStateEnum.WRITTENOFF) {
                            result.setButtonState(ButtonStateEnum.COMMENT);
                        } else {
                            if (info.getActivityGettingWay() == ActivityGettingWayEnum.OFF_LINE) {
                                result.setButtonState(ButtonStateEnum.SHOW);
                            } else {
                                result.setButtonState(ButtonStateEnum.JOINED);
                            }
                        }
                    }
                }
            }
            List<PreActivityInfoApiResult.ShopInfo> shopInfoList = preActivityQrCodeInfoMapper.selectList(Wrappers.<PreActiveQrCodeInfo>lambdaQuery()
                    .eq(PreActiveQrCodeInfo::getActivityId, result.getId())
            ).stream().map(qrCodeInfo -> {
                PreActivityInfoApiResult.ShopInfo shopInfo = new PreActivityInfoApiResult.ShopInfo();
                shopInfo.setShopId(qrCodeInfo.getOrgId());
                shopInfo.setShopName(qrCodeInfo.getOrgName());
                return shopInfo;
            }).collect(Collectors.toList());
            result.setShopList(shopInfoList);
        }
        return result;
    }

    @Override
    public IPage<PreActivityPageResult> page(PreActivityPageParam param) {
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        //选中标签 但是没有找到关联的活动时 返回空
        if(!CollUtil.isEmpty(param.getFolksonomyIds()) && CollUtil.isEmpty(businessIds)){
            return null;
        }
        List<Long> activityIds = getActivityIdByShopName(param.getShopName());
        //输入关联门店名称 但是没有找到关联的活动时 返回空
        if(StrUtil.isNotBlank(param.getShopName()) && CollUtil.isEmpty(activityIds) ){
            return null;
        }
        Set<Long> Ids_ = new HashSet<>();
        if(!CollUtil.isEmpty(businessIds)){
            businessIds.forEach( b -> {
                Ids_.add(b);
            });
        }
        if(!CollUtil.isEmpty(activityIds)){
            activityIds.forEach( b -> {
                Ids_.add(b);
            });
        }

        List<Long> ids = new ArrayList<>(Ids_);
        ActivityStateEnum state = param.getActivityState();
        DateTime now = DateTime.now();
        Date start_ = param.getCreateTimeStart();
        Date end_ = param.getCreateTimeEnd();
        return preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .like( param.getActivityName()!= null,PreActivityInfo::getActivityName,param.getActivityName())
                .like( param.getCreatorName() != null,PreActivityInfo::getCreatorName,param.getCreatorName())
                .in(CollUtil.isNotEmpty(ids),PreActivityInfo::getId,ids)
                .eq( param.getActivityType() != null,PreActivityInfo::getActivityType,param.getActivityType())
                .and(start_ != null,k -> k.ge(PreActivityInfo::getCreateTime, start_))
                .and(end_ != null,k -> k.le(PreActivityInfo::getCreateTime, end_))
                .eq(param.getActivityState()!=null && param.getActivityState() == ActivityStateEnum.CANCELED,
                        PreActivityInfo::getActivityState,param.getActivityState())
                .and(state != null && state == ActivityStateEnum.NOT_STARTED,
                        k -> k.ne(PreActivityInfo::getActivityState,ActivityStateEnum.CANCELED)
                                .ge(PreActivityInfo::getBeginTime, now))
                .and(state != null && state == ActivityStateEnum.IN_PROGRESS,
                        k -> k.ne(PreActivityInfo::getActivityState,ActivityStateEnum.CANCELED)
                                .le(PreActivityInfo::getBeginTime, now)
                                .ge(PreActivityInfo::getEndTime, now))
                .and(state != null && state == ActivityStateEnum.FINISHED,
                        k -> k.ne(PreActivityInfo::getActivityState,ActivityStateEnum.CANCELED)
                                .le(PreActivityInfo::getEndTime, now))
        ).convert(this::dataConvertResult);
    }

    private List<Long> getActivityIdByShopName(String shopName) {
        if(StrUtil.isBlank(shopName)){
            return null;
        }
        Set<Long> activityIds = new HashSet<>();
        if(StrUtil.isNotBlank(shopName)){
            QueryWrapper<PreActiveQrCodeInfo> qw = new QueryWrapper<>();
            qw.like("org_name", shopName);
            List<PreActiveQrCodeInfo> preActiveQrCodeInfos = preActivityQrCodeInfoMapper.selectList(qw);
            if(CollUtil.isNotEmpty(preActiveQrCodeInfos)){
                preActiveQrCodeInfos.forEach(f -> {
                    activityIds.add(f.getActivityId());
                });
            }
        }
        return new ArrayList<>(activityIds);
    }

    private PreActivityPageResult dataConvertResult(PreActivityInfo info) {
        if (null != info) {
            DateTime now = DateTime.now();
            PreActivityPageResult result = new PreActivityPageResult();
            BeanUtil.copyProperties(info, result);
            result.setRefGoodsCode(getGoodsCode(info.getRefGoods()));
            result.setFolksonomyIds(getFolksonomys(info.getId()));
            if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
                result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardParam.class));
            }
            if(ActivityTypeEnum.FLASH == info.getActivityType()){
                result.setRefShops(getRefShops(info.getId()));

            }

            if (info.getActivityState() != ActivityStateEnum.CANCELED) {
                if (now.isAfterOrEquals(info.getBeginTime()) && now.isBeforeOrEquals(info.getEndTime())) {
                    result.setActivityState(ActivityStateEnum.IN_PROGRESS);
                } else if (now.isBefore(info.getBeginTime())) {
                    result.setActivityState(ActivityStateEnum.NOT_STARTED);
                } else if (now.isAfter(info.getEndTime())) {
                    result.setActivityState(ActivityStateEnum.FINISHED);
                }
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 快闪活动详情 关联门店
     * @param activityId
     * @return
     */
    private List<PreActivityPageResult.RefShopInfo> getRefShops(Long activityId) {
        List<PreActivityPageResult.RefShopInfo> result = new ArrayList<>();
        QueryWrapper<PreActiveQrCodeInfo> qw = new QueryWrapper<>();
        qw.eq("activity_id", activityId);
        List<PreActiveQrCodeInfo> preActiveQrCodeInfos = preActivityQrCodeInfoMapper.selectList(qw);
        if(CollUtil.isNotEmpty(preActiveQrCodeInfos)){
            preActiveQrCodeInfos.forEach(f -> {
                PreActivityPageResult.RefShopInfo refShopInfo = new PreActivityPageResult.RefShopInfo();
                refShopInfo.setShopId(f.getOrgId());
                refShopInfo.setShopName(f.getOrgName());
                refShopInfo.setShopAddress(f.getOrgAddress());
                result.add(refShopInfo);
            });
        }
        return result;
    }

    private List<PreActivityFolksonomyResult> getFolksonomys(Long business_id) {
        List<PreActivityFolksonomyResult> results = new ArrayList<>();
        List<Map<String, Object>> list = preActivityInfoMapper.getFolksonomy(business_id);
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l -> {
                Long id = (Long) l.get("id");
                String name = (String) l.get("name");
                PreActivityFolksonomyResult p = new PreActivityFolksonomyResult();
                p.setFolksonomyId(id);
                p.setFolksonomyName(name);
                results.add(p);
            });
        }
        return results;
    }

    private List<PreActivityRefGoodsResult> getGoodsCode(String refGoods) {
        List<PreActivityRefGoodsResult> result = new ArrayList<>();
        JSONArray array_ = JSONUtil.parseArray(refGoods);
        array_.stream().forEach(i ->{
            Long idLong = Long.parseLong(i.toString());
            PreGoodsInfo goods = preGoodsInfoMapper.selectById(idLong);
            if(null != goods){
                PreActivityRefGoodsResult refGoodsResult = new PreActivityRefGoodsResult();
                refGoodsResult.setGoodsId(goods.getId());
                refGoodsResult.setGoodsCode(goods.getGoodsCode());
                refGoodsResult.setGoodsName(goods.getGoodsName());
                refGoodsResult.setGoodsPrice(goods.getGoodsPrice());
                refGoodsResult.setGoodsPicture(goods.getGoodsPicture());
                result.add(refGoodsResult);
            }
        });
        return result;
    }

    public static void main(String[] args) {
        List<String> aa = new ArrayList<>();
        aa.add("345356789543212345");
//        aa.add("145356789543212345");
//        aa.add("245356789543212345");

        String aa1 = JSONUtil.toJsonStr(aa);
        System.out.println("aa1:" + aa1);
//        String str = "[\"2314235432132367845\"]";
        JSONArray array_ = JSONUtil.parseArray(aa1);
        array_.stream().forEach(i ->{
            String str1 = i.toString();
//            str1 = str1.replaceAll("\"","");
            Long idLong = Long.parseLong(str1);
            System.out.println("i:" + i);
        });


    }


    /**
     * 获取关联的标签
     * @param folksonomyIds
     * @return
     */
    private List<Long> getFolksonomyBusinessRels(List<Long> folksonomyIds) {
        if(CollUtil.isEmpty(folksonomyIds)){
            return null;
        }
        Set<Long> businessIds = new HashSet<>();
        //先获取标签关联的业务id
        if(CollUtil.isNotEmpty(folksonomyIds)){
            QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
            qw.in("folksonomy_id", folksonomyIds);
            List<FolksonomyBusinessRel> folksonomyBusinessRels = folksonomyBusinessRelMapper.selectList(qw);
            if(CollUtil.isNotEmpty(folksonomyBusinessRels)){
                folksonomyBusinessRels.forEach(f -> {
                    businessIds.add(f.getBusinessId());
                });
            }
        }
        return new ArrayList<>(businessIds);
    }

    @Transactional
    @Override
    public void add(PreActivityAddParam param) {
        checkNameParam(param.getActivityName());
        checkTimeParam(param.getBeginTime(),param.getEndTime());

        PreActivityInfo activityInfo = new PreActivityInfo();
        BeanUtil.copyProperties(param, activityInfo,"refGoods");
        activityInfo.setId(MdcUtil.getSnowflakeId());
        activityInfo.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        activityInfo.setRefGoods(JSONUtil.toJsonStr(param.getRefGoods()));
        //根据时间 判断状态
        DateTime now = DateTime.now();
        if (now.isAfterOrEquals(param.getBeginTime()) && now.isBeforeOrEquals(param.getEndTime())) {
            activityInfo.setActivityState(ActivityStateEnum.IN_PROGRESS);
        } else if (now.isBefore(param.getBeginTime())) {
            activityInfo.setActivityState(ActivityStateEnum.NOT_STARTED);
        } else if (now.isAfter(param.getEndTime())) {
            activityInfo.setActivityState(ActivityStateEnum.FINISHED);
        }
        //设置默认规则 预售活动才有
        if(ActivityTypeEnum.PRE_SALES == param.getActivityType()){
            if(null == param.getRefRule()){
                QueryWrapper<PreRuleInfo> qw = new QueryWrapper<>();
                qw.eq("is_default", RuleDefaultEnum.DEFAULT.getType());
                PreRuleInfo info = preRuleInfoMapper.selectOne(qw);
                if(null != info){
                    activityInfo.setRefRule(info.getId());
                }else{
                    log.info("无法找到默认规则,活动设置规则失败!");
                }
            }
        }
        int count = preActivityInfoMapper.insert(activityInfo);
        if (count == 1) {
            log.info("新增活动成功");
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
            log.info("处理标签成功");
        } else {
            throw new ServiceException("新增活动失败");
        }
    }

    /**
     * 时间有效性校验
     * 活动时间不得早于当前时间 开始时间不得迟于结束时间
     * @param beginTime
     * @param endTime
     */
    private void checkTimeParam(Date beginTime, Date endTime) {
//        Date now = DateTime.now();
//        if(beginTime.getTime() < now.getTime()){
//            throw new ServiceException("活动开始时间不得早于当前时间");
//        }
//        if(endTime.getTime() < now.getTime()){
//            throw new ServiceException("活动结束时间不得早于当前时间");
//        }
        if(null != beginTime && null != endTime){
            if(endTime.getTime() < beginTime.getTime()){
                throw new ServiceException("活动结束时间不得早于活动开始时间");
            }
        }
    }

    /**
     * 重名校验
     * 活动名称不允许重复
     * @param activityName
     */
    private void checkNameParam(String activityName) {
        PreActivityInfo info =  preActivityInfoMapper.selectOne(Wrappers.<PreActivityInfo>lambdaQuery()
                .eq(PreActivityInfo::getActivityName,activityName));
        if(null != info){
            throw new ServiceException("已存在相同名称的活动,名称为:" + info.getActivityName());
        }
    }

    @Transactional
    @Override
    public void update(PreActivityUpdateParam param) {
        if(param.getId()==null) {
            throw new ServiceException("编辑的活动主键id为空" );
        }
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        Date beginTime = param.getBeginTime();
        Date endTime = param.getEndTime();
        if(null == beginTime){
            beginTime = activityInfo.getBeginTime();
        }
        if(null == endTime){
            endTime = activityInfo.getEndTime();
        }
        checkTimeParam(beginTime,endTime);
        BeanUtil.copyProperties(param, activityInfo,"id","activityState","refGoods");
        //时间有更新的话 同步更新状态 但是已下架状态的要先上架
        if(null != activityInfo.getActivityState() && activityInfo.getActivityState() != ActivityStateEnum.CANCELED){
            DateTime now = DateTime.now();
            if (now.isAfterOrEquals(beginTime) && now.isBeforeOrEquals(endTime)) {
                activityInfo.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(beginTime)) {
                activityInfo.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(endTime)) {
                activityInfo.setActivityState(ActivityStateEnum.FINISHED);
            }
        }
        if (CollUtil.isNotEmpty(param.getRewardRuleList())) {
            activityInfo.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        }
        if (CollUtil.isNotEmpty(param.getRefGoods())) {
            activityInfo.setRefGoods(JSONUtil.toJsonStr(param.getRefGoods()));
        }
        preActivityInfoMapper.updateById(activityInfo);
        log.info("编辑活动信息成功");
        Set<Long> oldIds = new HashSet<>();
        QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
        qw.in("business_id", param.getId());
        List<FolksonomyBusinessRel> folksonomyBusinessRels = folksonomyBusinessRelMapper.selectList(qw);
        if(CollUtil.isNotEmpty(folksonomyBusinessRels)){
            folksonomyBusinessRels.forEach(f -> oldIds.add(f.getFolksonomyId()));
        }
        Set<Long> newIds = new HashSet<>();
        if(CollUtil.isNotEmpty(param.getFolksonomyIds())){
            for(Long id : param.getFolksonomyIds()){
                newIds.add(id);
            }
        }
        List<Long> addIds = new ArrayList<>();
        List<Long> deleteIds = new ArrayList<>();
        for(Long n : newIds){
            if(!oldIds.contains(n)){
                addIds.add(n);
            }
        }
        for(Long o : oldIds){
            if(!newIds.contains(o)){
                deleteIds.add(o);
            }
        }
        if(CollUtil.isNotEmpty(addIds)){
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), addIds);
        }

        if(CollUtil.isNotEmpty(deleteIds)){
            deleteIds.forEach(i -> folksonomyBusinessRelMapper.delete(new QueryWrapper<FolksonomyBusinessRel>()
                    .eq("folksonomy_id", i)
                    .eq("business_id",param.getId())));
        }
        log.info("处理标签成功");
    }

    /**
     * 获取活动详情
     * @param param
     * @return
     */
    @Override
    public PreActivityDetailResult get(PreActivityGetParam param) {
        if(param.getId()==null) {
            throw new ServiceException("获取详情的活动主键id为空" );
        }
        PreActivityInfo info=  preActivityInfoMapper.selectById(param.getId());
        PreActivityDetailResult preActivityDetailResult = new PreActivityDetailResult();
        BeanUtil.copyProperties(info, preActivityDetailResult);
        //refGoodsCode
        if(StrUtil.isNotBlank(info.getRefGoods())){
            preActivityDetailResult.setRefGoodsCode(getGoodsCode(info.getRefGoods()));
        }
        //refRuleName
        PreRuleInfo rule = preRuleInfoMapper.selectById(info.getRefRule());
        if(null != rule){
            preActivityDetailResult.setRefRuleName(rule.getRuleName());
        }
        //rewardRuleList
        if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
            preActivityDetailResult.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardResult.class));
        }
        // ksonomyIds
        List<FolksonomyInfo> folksonomyInfos = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.PREACTIVITY, info.getId());
        if(CollUtil.isNotEmpty(folksonomyInfos)){
            List<String> names = new ArrayList<>();
            folksonomyInfos.forEach(s -> {
                names.add(s.getName());
            });
            preActivityDetailResult.setFolksonomyNames(names);
        }
        return preActivityDetailResult;
    }

    @Override
    public void changeState(PreActivityCancelParam param) {
        if(param.getId()==null) {
            throw new ServiceException("上架(下架)的活动主键id为空" );
        }
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        if(activityInfo.getActivityState() == ActivityStateEnum.CANCELED){
            //根据时间 判断上架状态
            DateTime now = DateTime.now();
            if (now.isAfterOrEquals(activityInfo.getBeginTime()) && now.isBeforeOrEquals(activityInfo.getEndTime())) {
                activityInfo.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(activityInfo.getBeginTime())) {
                activityInfo.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(activityInfo.getEndTime())) {
                activityInfo.setActivityState(ActivityStateEnum.FINISHED);
            }
        }else{
            //下架
            activityInfo.setActivityState(ActivityStateEnum.CANCELED);
        }
        preActivityInfoMapper.updateById(activityInfo);
    }

    @Override
    public PreActivityAnalysisResult analyse(PreActivityAnalysisParam param) {
        QueryWrapper<PreOrderInfo> qw = new QueryWrapper<>();
        qw.select("DISTINCT member_id","sum(total_price) as total")
                .eq("activity_info_id",param.getId())
                .groupBy("member_id");
        List<Map<String, Object>> maps = preOrderInfoMapper.selectMaps(qw);
        PreActivityAnalysisResult result = new  PreActivityAnalysisResult();
        if(CollUtil.isNotEmpty(maps)){
            result.setParticipantsCount(Convert.toLong(maps.size()));
            BigDecimal total = new BigDecimal(0.00);
            for(Map<String, Object> map : maps){
                BigDecimal start = new BigDecimal(0.00);
                BigDecimal bigDecimal = (BigDecimal) map.get("total");
                if(null != bigDecimal){
                    total = total.add(bigDecimal);
                }
            }
            result.setExchangePrice(total);
            if(maps.size() > 0){
                BigDecimal ppc = total.divide(new BigDecimal(maps.size()),2,BigDecimal.ROUND_HALF_UP);
                result.setPricePerCustomer(ppc);
            }else{
                result.setPricePerCustomer(new BigDecimal(0.00));
            }
        }else{
            result.setParticipantsCount(0L);
            result.setExchangePrice(new BigDecimal(0.00));
            result.setPricePerCustomer(new BigDecimal(0.00));
        }
        return result;
    }

    @Override
    public FlashStatisticsGetResult getFlashStatistics(FlashStatisticsGetParam param) {
        FlashStatisticsGetResult statisticsResult = new FlashStatisticsGetResult();
        Set<EventTypeEnum> eventTypeSet = new HashSet<>();
        eventTypeSet.add(EventTypeEnum.SHARE);
        eventTypeSet.add(EventTypeEnum.CLICK);
        List<MemberEventStatisticsResult> list = memberEventLogService.selectLogStatistics(new MemberEventStatisticsParam()
                .setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.PREACTIVITY).setEventTypes(eventTypeSet));
        for (MemberEventStatisticsResult result : list) {
            if (result.getEventType() == EventTypeEnum.SHOW) {
                statisticsResult.setSharePv(result.getPv());
                statisticsResult.setShareUv(result.getUv());
            } else if (result.getEventType() == EventTypeEnum.CLICK) {
                statisticsResult.setClickPv(result.getPv());
                statisticsResult.setClickUv(result.getUv());
            }
        }
        int participantsCount = preFlashOrderInfoMapper.selectCount(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId, param.getId())
        );
        statisticsResult.setParticipantsCount(Convert.toLong(participantsCount));
        if (participantsCount != 0) {
            statisticsResult.setConversionRate(NumberUtil.formatPercent(
                    NumberUtil.div(statisticsResult.getClickUv(), statisticsResult.getParticipantsCount()).doubleValue(), 2));
        }
        return statisticsResult;
    }

    @Override
    public void addQrcode(PreQrcodeAddParam param) {
        PreActivityInfo activityInfo = preActivityInfoMapper.selectById(param.getId());
        if (activityInfo == null) {
            throw new ServiceException("活动不存在");
        }
        List<PreActiveQrCodeInfo> infoList = new ArrayList<>();
        for (PreQrcodeAddParam.OrgInfo orgInfo : param.getOrgInfoList()) {
            PreActiveQrCodeInfo info = new PreActiveQrCodeInfo();
            info.setActivityId(param.getId());
            info.setOrgId(orgInfo.getOrgId());
            info.setOrgName(orgInfo.getOrgName());
            infoList.add(info);
        }
        int count = preActivityQrCodeInfoMapper.insertAllBatch(infoList);
        if (count <= 0) {
            throw new ServiceException("保存小程序二维码失败");
        }
        createMiniQrcode(infoList.toArray(new PreActiveQrCodeInfo[]{}));
    }

    private void createMiniQrcode(PreActiveQrCodeInfo... qrCodeInfoList) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            for (PreActiveQrCodeInfo qrCode : qrCodeInfoList) {
                if (StrUtil.isBlank(qrCode.getQrCodeUrl())) {
                    try {
                        String appId = "wxadb960df280e9e79";
                        String pagePath = "pages/activity/quick/detail";
                        File file = wechatMiniService.getWxMaServiceByAppId(qrCode.getAppId())
                                .getQrcodeService().createQrcode(pagePath + "?id=" + qrCode.getActivityId() + "&orgId=" + qrCode.getOrgId());
                        String path = qrCode.getAppId() + "/" + qrCode.getPagePath().replace("/", ".");
                        OssResult ossResult = AliOssUtil.uploadFileReturn(path, StrUtil.appendIfMissing(qrCode.getActivityId() + "_" + DateTime.now().getTime(),
                                ".png"), new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
                        PreActiveQrCodeInfo update = new PreActiveQrCodeInfo();
                        update.setAppId(appId);
                        update.setPagePath(pagePath);
                        update.setQrCodeFileKey(ossResult.getObjectKey());
                        update.setQrCodeUrl(ossResult.getUrl());
                        update.setId(qrCode.getId());
                        preActivityQrCodeInfoMapper.updateById(update);
                    } catch (WxErrorException | FileNotFoundException e) {
                        log.error("创建小程序码失败", e);
                    }
                }
            }
        });
    }
}
