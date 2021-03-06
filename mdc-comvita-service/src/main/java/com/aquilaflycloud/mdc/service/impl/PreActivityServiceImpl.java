package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.OSSObject;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.param.member.MemberInteractionParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.aquilaflycloud.mdc.service.MemberInteractionService;
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
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
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
    @Resource
    private MemberInteractionService memberInteractionService;
    @Resource
    private PreCommentInfoMapper preCommentInfoMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    private PreActivityInfo stateHandler(PreActivityInfo info) {
        if (info == null) {
            throw new ServiceException("???????????????");
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
                .orderByDesc(PreActivityInfo::getCreateTime)
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
        Long memberId = MdcUtil.getCurrentMemberId();
        PreActivityInfo info = preActivityInfoMapper.selectById(param.getId());
        info = stateHandler(info);
        PreActivityInfoApiResult result = BeanUtil.copyProperties(info, PreActivityInfoApiResult.class);
        if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
            result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardParam.class));
        }
        PreFlashOrderInfo orderInfo = null;
        if (memberId != null) {
            orderInfo = preFlashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                    .eq(PreFlashOrderInfo::getActivityInfoId, result.getId())
                    .eq(PreFlashOrderInfo::getMemberId, memberId)
            );
            if (orderInfo != null) {
                result.setShopId(orderInfo.getShopId());
                result.setShopName(orderInfo.getShopName());
            }
        }
        if (info.getActivityType() == ActivityTypeEnum.FLASH) {
            if (result.getActivityState() == ActivityStateEnum.IN_PROGRESS) {
                result.setButtonState(ButtonStateEnum.JOIN);
                if (memberId != null) {
                    if (orderInfo == null) {
                        int orderCount = preFlashOrderInfoMapper.selectCount(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                                .eq(PreFlashOrderInfo::getActivityInfoId, result.getId())
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
            } else if (result.getActivityState() == ActivityStateEnum.FINISHED || result.getActivityState() == ActivityStateEnum.CANCELED) {
                result.setButtonState(ButtonStateEnum.FINISHED);
                if (memberId != null) {
                    if (orderInfo != null) {
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
                    .isNotNull(PreActiveQrCodeInfo::getOrgId)
            ).stream().map(qrCodeInfo -> {
                PreActivityInfoApiResult.ShopInfo shopInfo = new PreActivityInfoApiResult.ShopInfo();
                shopInfo.setShopId(qrCodeInfo.getOrgId());
                shopInfo.setShopName(qrCodeInfo.getOrgName());
                shopInfo.setShopAddress(qrCodeInfo.getOrgAddress());
                return shopInfo;
            }).collect(Collectors.toList());
            result.setShopList(shopInfoList);
        }
        List<PreCommentInfo> commentInfoList = preCommentInfoMapper.selectList(Wrappers.<PreCommentInfo>query()
                .orderByAsc("parent_id")
                .orderByDesc(memberId != null, "commentator_id=" + memberId)
                .lambda()
                .eq(PreCommentInfo::getActivityId, info.getId())
                .nested(memberId == null, i -> i.eq(PreCommentInfo::getComState, ActivityCommentStateEnum.PASS)
                        .eq(PreCommentInfo::getComViewState, ActivityCommentViewStateEnum.OPEN))
                .nested(memberId != null, i -> i.eq(PreCommentInfo::getComState, ActivityCommentStateEnum.PASS)
                        .eq(PreCommentInfo::getComViewState, ActivityCommentViewStateEnum.OPEN)
                        .or()
                        .eq(PreCommentInfo::getCommentatorId, memberId)
                )
                .orderByDesc(PreCommentInfo::getCreateTime)
        );
        List<PreCommentResult> commentResultList = commentInfoList.stream().filter(commentInfo -> commentInfo.getParentId() == null)
                .map(commentInfo -> covert(commentInfo, commentInfoList))
                .sorted((o1, o2) -> {
                    if (memberId != null) {
                        if (memberId.equals(o1.getCommentatorId()) && !memberId.equals(o2.getCommentatorId())) {
                            return -1;
                        } else if (!memberId.equals(o1.getCommentatorId()) && memberId.equals(o2.getCommentatorId())) {
                            return 1;
                        }
                    }
                    return o2.getLikeNum().compareTo(o1.getLikeNum());
                }).collect(Collectors.toList());
        result.setCommentList(commentResultList);
        return result;
    }

    private PreCommentResult covert(PreCommentInfo commentInfo, List<PreCommentInfo> commentInfoList) {
        PreCommentResult result = BeanUtil.copyProperties(commentInfo, PreCommentResult.class);
        MemberInteractionParam interactionParam = new MemberInteractionParam().setBusinessId(commentInfo.getId())
                .setBusinessType(InteractionBusinessTypeEnum.COMMENT).setInteractionType(InteractionTypeEnum.LIKE);
        result.setLiked(memberInteractionService.getIsInteraction(interactionParam));
        result.setLikeNum(memberInteractionService.getInteractionNum(interactionParam));
        List<PreCommentResult> children = commentInfoList.stream()
                .filter(child -> commentInfo.getId().equals(child.getParentId()))
                .map(child -> covert(child, commentInfoList)).collect(Collectors.toList());
        result.setCommentReplyList(children);
        return result;
    }

    @Override
    public IPage<PreActivityPageResult> page(PreActivityPageParam param) {
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        //???????????? ???????????????????????????????????? ?????????
        if(!CollUtil.isEmpty(param.getFolksonomyIds()) && CollUtil.isEmpty(businessIds)){
            return null;
        }
        List<Long> activityIds = getActivityIdByShopName(param.getShopName());
        //???????????????????????? ???????????????????????????????????? ?????????
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
                .orderByDesc(PreActivityInfo::getCreateTime)
        ).convert(this::dataConvertResult);
    }

    /**
     * ???????????????????????????????????????id
     * @param shopName
     * @return
     */
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
            //???????????? ????????????
            if(ActivityTypeEnum.FLASH == info.getActivityType()){
                result.setRefShops(getRefShops(info.getId()));
                result.setParticipationCount(getParticipationCount(info.getId()));
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
     * ???????????????????????????
     * @param id
     * @return
     */
    private int getParticipationCount(Long id) {
        return preFlashOrderInfoMapper.selectCount(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId, id));
    }

    /**
     * ?????????????????? ????????????
     * @param activityId
     * @return
     */
    private List<RefShopInfoResult> getRefShops(Long activityId) {
        List<RefShopInfoResult> result = new ArrayList<>();
        QueryWrapper<PreActiveQrCodeInfo> qw = new QueryWrapper<>();
        qw.eq("activity_id", activityId);
        List<PreActiveQrCodeInfo> preActiveQrCodeInfos = preActivityQrCodeInfoMapper.selectList(qw);
        if(CollUtil.isNotEmpty(preActiveQrCodeInfos)){
            preActiveQrCodeInfos.forEach(f -> {
                RefShopInfoResult refShopInfo = new RefShopInfoResult();
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


    /**
     * ?????????????????????
     * @param folksonomyIds
     * @return
     */
    private List<Long> getFolksonomyBusinessRels(List<Long> folksonomyIds) {
        if(CollUtil.isEmpty(folksonomyIds)){
            return null;
        }
        Set<Long> businessIds = new HashSet<>();
        //??????????????????????????????id
        if(CollUtil.isNotEmpty(folksonomyIds)){
            QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
            qw.in("folksonomy_id", folksonomyIds);
            qw.eq("business_type",BusinessTypeEnum.PREACTIVITY.getType());
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
        checkNameParam(null,param.getActivityName(), param.getActivityType());
        checkTimeParam(param.getBeginTime(), param.getEndTime());

        PreActivityInfo activityInfo = new PreActivityInfo();
        BeanUtil.copyProperties(param, activityInfo, "refGoods");
        activityInfo.setId(MdcUtil.getSnowflakeId());
        activityInfo.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        activityInfo.setRefGoods(JSONUtil.toJsonStr(param.getRefGoods()));
        //???????????? ????????????
        DateTime now = DateTime.now();
        if (now.isAfterOrEquals(param.getBeginTime()) && now.isBeforeOrEquals(param.getEndTime())) {
            activityInfo.setActivityState(ActivityStateEnum.IN_PROGRESS);
        } else if (now.isBefore(param.getBeginTime())) {
            activityInfo.setActivityState(ActivityStateEnum.NOT_STARTED);
        } else if (now.isAfter(param.getEndTime())) {
            activityInfo.setActivityState(ActivityStateEnum.FINISHED);
        }
        //?????????????????? ??????????????????
        if (ActivityTypeEnum.PRE_SALES == param.getActivityType()) {
            if (null == param.getRefRule()) {
                QueryWrapper<PreRuleInfo> qw = new QueryWrapper<>();
                qw.eq("is_default", RuleDefaultEnum.DEFAULT.getType());
                PreRuleInfo info = preRuleInfoMapper.selectOne(qw);
                if (null != info) {
                    activityInfo.setRefRule(info.getId());
                } else {
                    log.info("????????????????????????,????????????????????????!");
                }
            }
        }
        int count = preActivityInfoMapper.insert(activityInfo);
        if (count == 1) {
            log.info("??????????????????");
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
            log.info("??????????????????");
            if (ActivityTypeEnum.FLASH == param.getActivityType()) {
                //???????????????????????????????????????
                PreActiveQrCodeInfo info = new PreActiveQrCodeInfo();
                info.setActivityId(activityInfo.getId());
                info.setOrgName("??????");
                count = preActivityQrCodeInfoMapper.insert(info);
                if (count <= 0) {
                    throw new ServiceException("??????????????????????????????");
                }
                createMiniQrcode(info);
            }
        } else {
            throw new ServiceException("??????????????????");
        }
    }

    /**
     * ?????????????????????
     * ???????????????????????????????????? ????????????????????????????????????
     * @param beginTime
     * @param endTime
     */
    private void checkTimeParam(Date beginTime, Date endTime) {
//        Date now = DateTime.now();
//        if(beginTime.getTime() < now.getTime()){
//            throw new ServiceException("??????????????????????????????????????????");
//        }
//        if(endTime.getTime() < now.getTime()){
//            throw new ServiceException("??????????????????????????????????????????");
//        }
        if(null != beginTime && null != endTime){
            if(endTime.getTime() < beginTime.getTime()){
                throw new ServiceException("????????????????????????????????????????????????");
            }
        }
    }

    /**
     * ????????????
     * ???????????????????????????
     *  ??????????????????????????????
     * @param id
     * @param activityName
     * @param activityType ????????????
     */
    private void checkNameParam(Long id, String activityName, ActivityTypeEnum activityType) {
        PreActivityInfo info =  preActivityInfoMapper.selectOne(Wrappers.<PreActivityInfo>lambdaQuery()
                .ne(null != id,PreActivityInfo::getId,id)
                .eq(PreActivityInfo::getActivityName,activityName)
                .eq(PreActivityInfo::getActivityType,activityType));
        if(null != info){
            throw new ServiceException("??????????????????????????????,?????????:" + info.getActivityName());
        }
    }

    @Transactional
    @Override
    public void update(PreActivityUpdateParam param) {
        if(param.getId()==null) {
            throw new ServiceException("?????????????????????id??????" );
        }
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        PreActivityInfo info = new PreActivityInfo();
        info.setId(activityInfo.getId());
        Date beginTime = param.getBeginTime();
        Date endTime = param.getEndTime();
        if(null == beginTime){
            beginTime = activityInfo.getBeginTime();
        }
        if(null == endTime){
            endTime = activityInfo.getEndTime();
        }
        checkTimeParam(beginTime,endTime);
        checkNameParam(param.getId(),param.getActivityName(), param.getActivityType());
        BeanUtil.copyProperties(param, info,"id","activityState","refGoods");
        //????????????????????? ?????????????????? ????????????????????????????????????
        if(null != activityInfo.getActivityState() && activityInfo.getActivityState() != ActivityStateEnum.CANCELED){
            DateTime now = DateTime.now();
            if (now.isAfterOrEquals(beginTime) && now.isBeforeOrEquals(endTime)) {
                info.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(beginTime)) {
                info.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(endTime)) {
                info.setActivityState(ActivityStateEnum.FINISHED);
            }
        }
        if (CollUtil.isNotEmpty(param.getRewardRuleList())) {
            info.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        }
        if (CollUtil.isNotEmpty(param.getRefGoods())) {
            info.setRefGoods(JSONUtil.toJsonStr(param.getRefGoods()));
        }
        int count = preActivityInfoMapper.updateById(info);
        if (count <= 0) {
            throw new ServiceException("??????????????????");
        }
        if (activityInfo.getActivityType() == ActivityTypeEnum.FLASH) {
            PreFlashOrderInfo update = new PreFlashOrderInfo();
            update.setBeginTime(activityInfo.getBeginTime());
            update.setEndTime(activityInfo.getEndTime());
            preFlashOrderInfoMapper.update(update, Wrappers.<PreFlashOrderInfo>lambdaUpdate()
                    .eq(PreFlashOrderInfo::getActivityInfoId, activityInfo.getId())
            );
        }
        log.info("????????????????????????");
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
        log.info("??????????????????");
    }

    /**
     * ??????????????????
     * @param param
     * @return
     */
    @Override
    public PreActivityDetailResult get(PreActivityGetParam param) {
        if(param.getId()==null) {
            throw new ServiceException("???????????????????????????id??????" );
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
        if(ActivityTypeEnum.FLASH == info.getActivityType()){
            preActivityDetailResult.setRefShops(getRefShops(info.getId()));
            preActivityDetailResult.setParticipationCount(getParticipationCount(info.getId()));
        }
        return preActivityDetailResult;
    }

    @Override
    public void changeState(PreActivityCancelParam param) {
        if(param.getId()==null) {
            throw new ServiceException("??????(??????)???????????????id??????" );
        }
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        PreActivityInfo update = new PreActivityInfo();
        update.setId(activityInfo.getId());
        if(activityInfo.getActivityState() == ActivityStateEnum.CANCELED){
            //???????????? ??????????????????
            DateTime now = DateTime.now();
            if (now.isAfterOrEquals(activityInfo.getBeginTime()) && now.isBeforeOrEquals(activityInfo.getEndTime())) {
                update.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(activityInfo.getBeginTime())) {
                update.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(activityInfo.getEndTime())) {
                update.setActivityState(ActivityStateEnum.FINISHED);
            }
        }else{
            //??????
            update.setActivityState(ActivityStateEnum.CANCELED);
        }
        preActivityInfoMapper.updateById(update);
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
            if (result.getEventType() == EventTypeEnum.SHARE) {
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
        if (statisticsResult.getClickUv() != 0) {
            statisticsResult.setConversionRate(NumberUtil.formatPercent(
                    NumberUtil.div(statisticsResult.getParticipantsCount(), statisticsResult.getClickUv()).doubleValue(), 2));
        }
        return statisticsResult;
    }

    @Override
    public BaseResult<String> addQrcode(PreQrcodeAddParam param) {
        PreActivityInfo activityInfo = preActivityInfoMapper.selectById(param.getId());
        if (activityInfo == null) {
            throw new ServiceException("???????????????");
        }
        List<Long> orgIdList = preActivityQrCodeInfoMapper.selectList(Wrappers.<PreActiveQrCodeInfo>lambdaQuery()
                .select(PreActiveQrCodeInfo::getOrgId)
                .eq(PreActiveQrCodeInfo::getActivityId, param.getId())
                .isNotNull(PreActiveQrCodeInfo::getOrgId)
        ).stream().map(PreActiveQrCodeInfo::getOrgId).collect(Collectors.toList());
        List<String> duplicateOrg = new ArrayList<>();
        List<PreActiveQrCodeInfo> infoList = new ArrayList<>();
        for (PreQrcodeAddParam.OrgInfo orgInfo : param.getOrgInfoList()) {
            if (orgIdList.contains(orgInfo.getOrgId())) {
                duplicateOrg.add(orgInfo.getOrgName());
                continue;
            }
            PreActiveQrCodeInfo info = new PreActiveQrCodeInfo();
            info.setActivityId(param.getId());
            info.setOrgId(orgInfo.getOrgId());
            info.setOrgName(orgInfo.getOrgName());
            info.setOrgAddress(orgInfo.getOrgAddress());
            infoList.add(info);
        }
        if (infoList.size() > 0) {
            int count = preActivityQrCodeInfoMapper.insertAllBatch(infoList);
            if (count <= 0) {
                throw new ServiceException("??????????????????????????????");
            }
        }
        createMiniQrcode(infoList.toArray(new PreActiveQrCodeInfo[]{}));
        if (!duplicateOrg.isEmpty()) {
            return BaseResult.buildResult(ArrayUtil.join(duplicateOrg.toArray(), ", "));
        } else {
            return new BaseResult<>();
        }
    }

    @Override
    public void deleteQrcode(PreQrcodeDeleteParam param) {
        if(param.getId()==null) {
            throw new ServiceException("????????????id??????!" );
        }
        if(param.getQrId()==null) {
            throw new ServiceException("?????????id??????!" );
        }
        preActivityQrCodeInfoMapper.delete(new QueryWrapper<PreActiveQrCodeInfo>()
                .eq("activity_id", param.getId())
                .eq("id", param.getQrId()));
    }

    private int incrementalCount = 0;

    @Override
    public BaseResult<String> downloadQrcode(PreQrcodeDownloadParam param) {
        if (CollUtil.isEmpty(param.getQrIdList())) {
            throw new ServiceException("?????????id????????????");
        }
        if(param.getActivityName()==null) {
            throw new ServiceException("??????????????????!" );
        }

        List<PreActiveQrCodeInfo> qrCodeList = preActivityQrCodeInfoMapper.selectList(Wrappers.<PreActiveQrCodeInfo>lambdaQuery()
                .eq(PreActiveQrCodeInfo::getActivityId, param.getActivityId())
                .in(PreActiveQrCodeInfo::getId, param.getQrIdList())
        );
        List<InputStream> isList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        for (PreActiveQrCodeInfo qrCode : qrCodeList) {
            OSSObject ossObject = AliOssUtil.getObject(qrCode.getQrCodeFileKey());
            isList.add(ossObject.getObjectContent());
            pathList.add(param.getActivityName() + qrCode.getOrgName() + (incrementalCount++) + ".png");
        }
        try {
            File tmpDirFile = Files.createTempDirectory("flash-codes-temp").toFile();
            File resultFile = File.createTempFile("codes", ".zip", tmpDirFile);
            File zipFile = ZipUtil.zip(resultFile, pathList.toArray(new String[0]), isList.toArray(new InputStream[0]));
            String url = AliOssUtil.uploadFile(param.getActivityName() + "?????????.zip", new FileInputStream(zipFile));
            return new BaseResult<String>().setResult(url);
        } catch (IOException e) {
            log.error("????????????oss??????", e);
            throw new ServiceException("??????????????????????????????");
        }
    }

    @Override
    public List<PreActivityQrCodeResult> getQrcode(PreQrcodeGetterParam param) {
        if(param.getActivityId()==null) {
            throw new ServiceException("????????????id??????,???????????????????????????!" );
        }
        List<PreActivityQrCodeResult> result = new ArrayList<>();
        QueryWrapper<PreActiveQrCodeInfo> qw = new QueryWrapper<>();
        qw.eq("activity_id", param.getActivityId());
        qw.like(param.getShopName() != null,"org_name",param.getShopName());
        List<PreActiveQrCodeInfo> preActiveQrCodeInfos = preActivityQrCodeInfoMapper.selectList(qw);
        if(CollUtil.isNotEmpty(preActiveQrCodeInfos)){
            preActiveQrCodeInfos.forEach(f -> {
                PreActivityQrCodeResult qrCodeResult = new PreActivityQrCodeResult();
                qrCodeResult.setActivityId(f.getActivityId());
                qrCodeResult.setOrgId(f.getOrgId());
                qrCodeResult.setOrgName(f.getOrgName());
                qrCodeResult.setQrId(f.getId());
                result.add(qrCodeResult);
            });
        }
        return result;
    }

    /**
     * ????????????
     * @param param
     * @return
     */
    @Override
    public IPage<PreFlashReportPageResult> pageExportFlashActivityPageResultList(FlashExportParam param) {
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        if(!CollUtil.isEmpty(param.getFolksonomyIds()) && CollUtil.isEmpty(businessIds)){
            return null;
        }
        List<Long> activityIds = getActivityIdByShopName(param.getShopName());
        if(StrUtil.isNotBlank(param.getShopName()) && CollUtil.isEmpty(activityIds) ){
            return null;
        }

        List<Long> ids = null;
        if(CollUtil.isNotEmpty(businessIds) && CollUtil.isNotEmpty(activityIds)){
            ids = (List<Long>)CollUtil.union(businessIds,activityIds);
        }else if(CollUtil.isNotEmpty(businessIds) && CollUtil.isEmpty(activityIds)){
            if(null == ids){
                ids = new ArrayList<>();
            }
            ids.addAll(businessIds);
        }else if(CollUtil.isEmpty(businessIds) && CollUtil.isNotEmpty(activityIds)){
            if(null == ids){
                ids = new ArrayList<>();
            }
            ids.addAll(activityIds);
        }

        ActivityStateEnum state = param.getActivityState();
        DateTime now = DateTime.now();
        Date start_ = param.getCreateTimeStart();
        Date end_ = param.getCreateTimeEnd();
        IPage<PreFlashReportPageResult> resultIPage =  preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .like( param.getActivityName()!= null,PreActivityInfo::getActivityName,param.getActivityName())
                .like( param.getCreatorName() != null,PreActivityInfo::getCreatorName,param.getCreatorName())
                .in(CollUtil.isNotEmpty(ids),PreActivityInfo::getId,ids)
                .eq(PreActivityInfo::getActivityType,ActivityTypeEnum.FLASH)
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
                .orderByDesc(PreActivityInfo::getCreateTime)
        ).convert(info -> {
            PreFlashReportPageResult preFlashReportPageResult = new PreFlashReportPageResult();
            preFlashReportPageResult.setActivityId(info.getId());
            preFlashReportPageResult.setActivityName(info.getActivityName());
            preFlashReportPageResult.setActivityTime(DateUtil.formatDateTime(info.getBeginTime()) + " ~ " + DateUtil.formatDateTime(info.getEndTime()));
            List<PreActivityFolksonomyResult> folksonomys = getFolksonomys(info.getId());
            StringBuffer sb = new StringBuffer();
            if(CollUtil.isNotEmpty(folksonomys)){
                for(int i = 0 ; i < folksonomys.size() ; i++){
                    sb.append(folksonomys.get(i).getFolksonomyName());
                    if(i != folksonomys.size() - 1 ){
                        sb.append(",");
                    }
                }
            }
            preFlashReportPageResult.setActivityFolksonomy(sb.toString());
            FlashStatisticsGetParam param_ = new FlashStatisticsGetParam();
            param_.setId(info.getId());
            FlashStatisticsGetResult statistics = getFlashStatistics(param_);
            if(null != statistics){
                preFlashReportPageResult.setSharePv(statistics.getSharePv());
                preFlashReportPageResult.setClickUv(statistics.getClickUv());
                preFlashReportPageResult.setClickPv(statistics.getClickPv());
                preFlashReportPageResult.setConversionRate(statistics.getConversionRate());
                preFlashReportPageResult.setParticipantsCount(statistics.getParticipantsCount());
            }
            return preFlashReportPageResult;
        });
        List<PreFlashReportPageResult> resultList = resultIPage.getRecords();
        if(CollUtil.isNotEmpty(resultList)){
            Map<String,PreFlashReportPageResult> activityId2Result = new HashMap<>();
            for (PreFlashReportPageResult result : resultList) {
                activityId2Result.put(Convert.toStr(result.getActivityId()), result);
            }
            //1?????????????????????????????????
            Map<String,String> map = getEverBought();
            //2????????????????????????????????????
            List<PreFlashReportPageResult> resultList3 = getShopAndMember(activityId2Result,map);
            if(CollUtil.isEmpty(resultList3)){
                resultList3 = resultList;
            }
            if(CollUtil.isNotEmpty(resultList3)){
                resultIPage.setRecords(resultList3);
            }
        }
        return resultIPage;
    }

    /**
     * ???????????????????????????
     * @param paramList
     * @param memberId2isEvenBought
     * @return
     */
    private List<PreFlashReportPageResult> fillMemberInfo(List<PreFlashReportPageResult> paramList, Map<String, String> memberId2isEvenBought) {
        Map<String,PreFlashReportPageResult> preMap = new HashMap<>();
        List<String> allKey = new ArrayList<>();
        for(PreFlashReportPageResult result : paramList){
            String key = Convert.toStr(result.getActivityId());
            if(null != result.getOrgId()){
                key = key + Convert.toStr(result.getOrgId());
            }
            if(StrUtil.isNotBlank(result.getOrgName())){
                key = key + result.getOrgName();
            }
            allKey.add(key);
            preMap.put(key,result);
        }
        Set<String> alreadyResult = new HashSet<>();
        List<PreFlashReportPageResult> resultList = new ArrayList<>();
        List<Map<String, Object>> list = preActivityInfoMapper.getMembers();
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l -> {
                Long activity_info_id = (Long) l.get("activity_info_id");
                Long shop_id = (Long) l.get("shop_id");
                String shop_name = (String) l.get("shop_name");
                String mapKey = Convert.toStr(activity_info_id);
                if(null != shop_id){
                    mapKey = mapKey + Convert.toStr(shop_id);
                }
                if(StrUtil.isNotBlank(shop_name)){
                    mapKey = mapKey + shop_name;
                }
                PreFlashReportPageResult result = preMap.get(mapKey);
                if(null != result){
                    alreadyResult.add(mapKey);
                    PreFlashReportPageResult newResult = new PreFlashReportPageResult();
                    BeanUtil.copyProperties(result, newResult);
                    resultList.add(newResult);
                    //??????????????????
                    Long member_id = (Long) l.get("member_id");
                    String isEvenBought = memberId2isEvenBought.get(Convert.toStr(member_id));
                    if(StrUtil.isNotBlank(isEvenBought)){
                        newResult.setIsEverBought(isEvenBought);
                    }
                    //????????????
                    String org_name = (String) l.get("shop_name");
                    newResult.setOrgName(org_name);
                    String org_address = (String) l.get("shop_address");
                    newResult.setOrgAddress(org_address);
                    //????????????
                    String real_name = (String) l.get("real_name");
                    String nick_name = (String) l.get("nick_name");
                    if(StrUtil.isNotBlank(real_name)){
                        newResult.setParticipantName(real_name);
                    }else{
                        newResult.setParticipantName(nick_name);
                    }
                    String phone_number = (String) l.get("phone_number");
                    newResult.setParticipantPhoneNum(phone_number);
                    if(null != l.get("sex")){
                        int sex = (Integer) l.get("sex");
                        if(0 == sex){
                            newResult.setParticipantSex("??????");
                        }else if(1 == sex){
                            newResult.setParticipantSex("???");
                        }else if(2 == sex){
                            newResult.setParticipantSex("???");
                        }
                    }
                    if(null != l.get("birthday")){
                        Date birthday = (Date) l.get("birthday");
                        newResult.setParticipantBirthdate(DateUtil.format(birthday,"yyyy-MM-dd"));
                    }
                    StringBuffer sb = new StringBuffer();
                    if(null !=  l.get("province")){
                        sb.append((String) l.get("province"));
                    }
                    if(null !=  l.get("city")){
                        sb.append((String) l.get("city"));
                    }
                    if(null !=  l.get("county")){
                        sb.append((String) l.get("county"));
                    }
                    if(null !=  l.get("address")){
                        sb.append((String) l.get("address"));
                    }
                    newResult.setParticipantAddress(sb.toString());
                    Date create_time = (Date) l.get("create_time");
                    //??????????????????????????????
                    boolean isSameDay = DateUtil.isSameDay(create_time, DateTime.now());
                    if(isSameDay){
                        newResult.setIsNewMember("???");
                    }else{
                        newResult.setIsNewMember("???");
                    }
                }
            });
        }

        List<String> disjunction = (List<String>) CollUtil.disjunction(allKey,alreadyResult);
        if(CollUtil.isNotEmpty(disjunction)){
            for(String str : disjunction){
                PreFlashReportPageResult re = preMap.get(str);
                if(null != re){
                    resultList.add(re);
                }
            }
        }

        return CollUtil.sortByProperty(resultList,"activityId");
    }

    /**
     * ??????????????????
     * @param resultList
     * @return
     */
    private List<PreFlashReportPageResult> fillShopInfo(List<PreFlashReportPageResult> resultList) {
        List<PreFlashReportPageResult> returnList = new ArrayList<>();
        if (resultList != null) {
            for(PreFlashReportPageResult result : resultList){
                List<PreActiveQrCodeInfo> qrCodeInfos = preActivityQrCodeInfoMapper.selectList(Wrappers.<PreActiveQrCodeInfo>lambdaQuery()
                        .eq(PreActiveQrCodeInfo::getActivityId, result.getActivityId()));
                if(CollUtil.isNotEmpty(qrCodeInfos)){
                    for(PreActiveQrCodeInfo qrCodeInfo : qrCodeInfos){
                        PreFlashReportPageResult preFlashReportPageResult = new PreFlashReportPageResult();
                        BeanUtil.copyProperties(result, preFlashReportPageResult);
                        returnList.add(preFlashReportPageResult);
                        preFlashReportPageResult.setOrgName(qrCodeInfo.getOrgName());
                        preFlashReportPageResult.setOrgAddress(qrCodeInfo.getOrgAddress());
                        preFlashReportPageResult.setOrgId(qrCodeInfo.getOrgId());
                    }
                }else{
                    returnList.add(result);
                }
            }
        }
        return returnList;
    }

    /**
     * ????????????
     * @param param
     * @return
     */
    @Override
    public IPage<PreActivityReportPageResult> pageExportPreActivityPageResultList(PreActivityExportParam param) {
        //1,????????????
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        if(!CollUtil.isEmpty(param.getFolksonomyIds()) && CollUtil.isEmpty(businessIds)){
            return null;
        }
        ActivityStateEnum state = param.getActivityState();
        DateTime now = DateTime.now();
        Date start_ = param.getCreateTimeStart();
        Date end_ = param.getCreateTimeEnd();
        IPage<PreActivityReportPageResult> resultIPage =  preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .like( param.getActivityName()!= null,PreActivityInfo::getActivityName,param.getActivityName())
                .like( param.getCreatorName() != null,PreActivityInfo::getCreatorName,param.getCreatorName())
                .in(CollUtil.isNotEmpty(businessIds),PreActivityInfo::getId,businessIds)
                .eq(PreActivityInfo::getActivityType,ActivityTypeEnum.PRE_SALES)
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
                .orderByDesc(PreActivityInfo::getCreateTime)
        ).convert(info -> {
            PreActivityReportPageResult pageResult = new PreActivityReportPageResult();
            pageResult.setActivityId(info.getId());
            pageResult.setActivityName(info.getActivityName());
            pageResult.setActivityTime(DateUtil.formatDateTime(info.getBeginTime()) + " ~ " + DateUtil.formatDateTime(info.getEndTime()));
            List<PreActivityFolksonomyResult> folksonomys = getFolksonomys(info.getId());
            StringBuffer sb = new StringBuffer();
            if(CollUtil.isNotEmpty(folksonomys)){
                for(int i = 0 ; i < folksonomys.size() ; i++){
                    sb.append(folksonomys.get(i).getFolksonomyName());
                    if(i != folksonomys.size() - 1 ){
                        sb.append(",");
                    }
                }
            }
            pageResult.setActivityFolksonomy(sb.toString());
            return pageResult;
        });
        List<PreActivityReportPageResult> resultList = resultIPage.getRecords();
        if(CollUtil.isNotEmpty(resultList)){
            Map<String,PreActivityReportPageResult> activityId2Result = new HashMap<>();
            for (PreActivityReportPageResult result : resultList) {
                activityId2Result.put(Convert.toStr(result.getActivityId()), result);
            }
            //2?????????????????????????????????Map
            Map<String,String> map = getParticipantsCountMap();
            //3????????????????????????????????????
            List<PreActivityReportPageResult> result1 = getPreActivityRefShopAndMember(resultList,activityId2Result,map);
            if(CollUtil.isNotEmpty(result1)){
                resultIPage.setRecords(result1);
            }
        }
        return resultIPage;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param results
     * @param activityId2Result
     * @param activityId2Count
     * @return
     */
    private List<PreActivityReportPageResult> getPreActivityRefShopAndMember(
            List<PreActivityReportPageResult> results,
            Map<String, PreActivityReportPageResult> activityId2Result,
            Map<String, String> activityId2Count) {
        //???????????????????????????id??????
        List<String> allKey = new ArrayList<>();
        results.forEach(r-> allKey.add(Convert.toStr(r.getActivityId())));
        //???????????????????????????????????????id ??????????????????????????????????????????
        Set<String> alreadyResult = new HashSet<>();
        List<PreActivityReportPageResult> resultList = new ArrayList<>();
        List<Map<String, Object>> list = preActivityInfoMapper.getPreActivityRefShopAndMember();
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l -> {
                Long activity_info_id = (Long) l.get("activity_info_id");
                PreActivityReportPageResult result = activityId2Result.get(Convert.toStr(activity_info_id));
                if(null != result){
                    alreadyResult.add(Convert.toStr(result.getActivityId()));
                    PreActivityReportPageResult newResult = new PreActivityReportPageResult();
                    BeanUtil.copyProperties(result, newResult);
                    resultList.add(newResult);
                    //????????????
                    String count = activityId2Count.get(Convert.toStr(activity_info_id));
                    if(StrUtil.isNotBlank(count)){
                        newResult.setParticipantsCount(Convert.toLong(count));
                    }
                    //????????????
                    String shop_name = (String) l.get("shop_name");
                    newResult.setOrgName(shop_name);
                    String shop_address = (String) l.get("shop_address");
                    newResult.setOrgAddress(shop_address);
                    //????????????
                    String real_name = (String) l.get("real_name");
                    String nick_name = (String) l.get("nick_name");
                    if (StrUtil.isNotBlank(real_name)){
                        newResult.setParticipantName(real_name);
                    }else{
                        newResult.setParticipantName(nick_name);
                    }
                    String phone_number = (String) l.get("phone_number");
                    newResult.setParticipantPhoneNum(phone_number);
                    if(null != l.get("sex")){
                        int sex = Convert.toInt(l.get("sex"));
                        if(0 == sex){
                            newResult.setParticipantSex("??????");
                        }else if(1 == sex){
                            newResult.setParticipantSex("???");
                        }else if(2 == sex){
                            newResult.setParticipantSex("???");
                        }
                    }
                    if(null != l.get("birthday")){
                        Date birthday = (Date) l.get("birthday");
                        newResult.setParticipantBirthdate(DateUtil.format(birthday,"yyyy-MM-dd"));
                    }
                    StringBuffer sb = new StringBuffer();
                    if(null !=  l.get("province")){
                        sb.append((String) l.get("province"));
                    }
                    if(null !=  l.get("city")){
                        sb.append((String) l.get("city"));
                    }
                    if(null !=  l.get("county")){
                        sb.append((String) l.get("county"));
                    }
                    if(null !=  l.get("address")){
                        sb.append((String) l.get("address"));
                    }
                    newResult.setParticipantAddress(sb.toString());
                }
            });
        }
        //????????????????????????????????? ?????????????????????
        List<String> disjunction = (List<String>) CollUtil.disjunction(allKey,alreadyResult);
        if(CollUtil.isNotEmpty(disjunction)){
            for(String str : disjunction){
                PreActivityReportPageResult re = activityId2Result.get(str);
                if(null != re){
                    //??????????????????
                    re.setParticipantsCount(0L);
                    resultList.add(re);
                }
            }
        }
        return CollUtil.sortByProperty(resultList,"activityId");
    }

    /**
     * ??????????????????????????????Map
     * @return
     */
    private Map<String, String> getParticipantsCountMap() {
        QueryWrapper<PreOrderInfo> qw = new QueryWrapper<>();
        qw.select("activity_info_id","count(distinct member_id) as count")
                .groupBy("activity_info_id");
        List<Map<String, Object>> maps = preOrderInfoMapper.selectMaps(qw);
        //key : activity id value:member count
        Map<String,String> activityId2Count = new HashMap<>();
        if(CollUtil.isNotEmpty(maps)){
            for(Map<String, Object> map_ : maps){
                Long activity_info_id = Convert.toLong(map_.get("activity_info_id"));
                Long count = Convert.toLong(map_.get("count"));
                activityId2Count.put(Convert.toStr(activity_info_id),Convert.toStr(count));
            }
        }
        return activityId2Count;
    }


    /**
     * ??????????????????????????????????????????
     * @param activityId2result
     * @param memberId2isEvenBought
     * @return
     */
    private List<PreFlashReportPageResult> getShopAndMember(Map<String, PreFlashReportPageResult> activityId2result,
                                                            Map<String, String> memberId2isEvenBought) {
        List<PreFlashReportPageResult> resultList = new ArrayList<>();
        List<Map<String, Object>> list = preActivityInfoMapper.getShopAndMember();
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l -> {
                Long activity_info_id = (Long) l.get("activity_info_id");
                PreFlashReportPageResult result = activityId2result.get(Convert.toStr(activity_info_id));
                if(null != result){
                    PreFlashReportPageResult newResult = new PreFlashReportPageResult();
                    BeanUtil.copyProperties(result, newResult);
                    resultList.add(newResult);
                    //??????????????????
                    Long member_id = (Long) l.get("member_id");
                    String isEvenBought = memberId2isEvenBought.get(Convert.toStr(member_id));
                    if(StrUtil.isNotBlank(isEvenBought)){
                        newResult.setIsEverBought(isEvenBought);
                    }
                    //????????????
                    String org_name = (String) l.get("shop_name");
                    newResult.setOrgName(org_name);
                    String org_address = (String) l.get("shop_address");
                    newResult.setOrgAddress(org_address);
                    //????????????
                    String real_name = (String) l.get("real_name");
                    String nick_name = (String) l.get("nick_name");
                    if(StrUtil.isNotBlank(real_name)){
                        newResult.setParticipantName(real_name);
                    }else{
                        newResult.setParticipantName(nick_name);
                    }
                    String phone_number = (String) l.get("phone_number");
                    newResult.setParticipantPhoneNum(phone_number);
                    if(null != l.get("sex")){
                        int sex = (Integer) l.get("sex");
                        if(0 == sex){
                            newResult.setParticipantSex("??????");
                        }else if(1 == sex){
                            newResult.setParticipantSex("???");
                        }else if(2 == sex){
                            newResult.setParticipantSex("???");
                        }
                    }
                    if(null != l.get("birthday")){
                        Date birthday = (Date) l.get("birthday");
                        newResult.setParticipantBirthdate(DateUtil.format(birthday,"yyyy-MM-dd"));
                    }
                    StringBuffer sb = new StringBuffer();
                    if(null !=  l.get("province")){
                        sb.append((String) l.get("province"));
                    }
                    if(null !=  l.get("city")){
                        sb.append((String) l.get("city"));
                    }
                    if(null !=  l.get("county")){
                        sb.append((String) l.get("county"));
                    }
                    if(null !=  l.get("address")){
                        sb.append((String) l.get("address"));
                    }
                    newResult.setParticipantAddress(sb.toString());
                    Date create_time = (Date) l.get("create_time");
                    //??????????????????????????????
                    if(null != create_time){
                        boolean isSameDay = DateUtil.isSameDay(create_time, DateTime.now());
                        if(isSameDay){
                            newResult.setIsNewMember("???");
                        }else{
                            newResult.setIsNewMember("???");
                        }
                    }
                }
            });
        }
        return resultList;
    }

    /**
     *  ??????????????????????????????
     * @return map: key->member_id value->??????????????????
     */
    private Map<String,String> getEverBought() {
        QueryWrapper<PreFlashOrderInfo> qw = new QueryWrapper<>();
        qw.select("member_id","count(member_id) as count")
                .groupBy("member_id");
        List<Map<String, Object>> maps = preFlashOrderInfoMapper.selectMaps(qw);
        //key : member id value:??????????????????
        Map<String,String> memberId2Count = new HashMap<>();
        if(CollUtil.isNotEmpty(maps)){
            for(Map<String, Object> map_ : maps){
                Long member_id = (Long) map_.get("member_id");
                Long count = (Long) map_.get("count");
                if(count > 1){
                    memberId2Count.put(Convert.toStr(member_id),"???");
                }else{
                    memberId2Count.put(Convert.toStr(member_id),"???");
                }
            }
        }
        return memberId2Count;
    }

    private void createMiniQrcode(PreActiveQrCodeInfo... qrCodeInfoList) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            for (PreActiveQrCodeInfo qrCode : qrCodeInfoList) {
                if (StrUtil.isBlank(qrCode.getQrCodeUrl())) {
                    try {
                        String appId = "wxadb960df280e9e79";
                        String pagePath = "pages/activity/quick/detail";
                        qrCode.setAppId(appId);
                        qrCode.setPagePath(pagePath);
                        File file = wechatMiniService.getWxMaServiceByAppId(qrCode.getAppId())
                                .getQrcodeService().createQrcode(pagePath + "?id=" + qrCode.getActivityId() +
                                        (qrCode.getOrgId() != null ? "&orgId=" + qrCode.getOrgId() : ""));
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
                        log.error("????????????????????????", e);
                    }
                }
            }
        });
    }
}
