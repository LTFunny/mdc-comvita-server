package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.param.member.MemberEventAddParam;
import com.aquilaflycloud.mdc.param.member.MemberEventParam;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberEventApi
 *
 * @author star
 * @date 2019-11-20
 */
@RestController
@Api(tags = "会员事件记录接口")
public class MemberEventApi {

    @Resource
    private MemberEventLogService memberEventLogService;

    private BaseResult<Long> returnResult(Long num) {
        return new BaseResult<Long>().setResult(num);
    }

    @ApiOperation(value = "记录广告分享", notes = "记录广告分享,返回分享数")
    @ApiMapping(value = "mdc.member.adShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addAdShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.AD);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录欢乐抽奖分享", notes = "记录欢乐抽奖分享,返回分享数")
    @ApiMapping(value = "mdc.member.lotteryShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addLotteryShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.LOTTERY);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    /*@ApiOperation(value="记录打卡有礼分享", notes = "记录打卡有礼分享,返回分享数")
    @ApiMapping(value = "mdc.member.offlineSignShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addOfflineSignShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.OFFLINESIGN);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }*/

    @ApiOperation(value = "记录活动报名分享", notes = "记录活动报名分享,返回分享数")
    @ApiMapping(value = "mdc.member.applyShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addApplyShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.APPLY);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录最新推荐分享", notes = "记录最新推荐分享,返回分享数")
    @ApiMapping(value = "mdc.member.recommendShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addRecommendShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.RECOMMEND);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录景区信息分享", notes = "记录景区信息分享,返回分享数")
    @ApiMapping(value = "mdc.member.scenicSpotsShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addScenicSpotsShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.SCENICSPOTS);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录商品分享", notes = "记录商品分享,返回分享数")
    @ApiMapping(value = "mdc.member.goodsShare.add", method = RequestMethod.POST)
    public BaseResult<Long> addGoodsShare(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.EXCHANGEGOODS);
        eventParam.setEventType(EventTypeEnum.SHARE);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录广告点击", notes = "记录广告点击,返回点击数")
    @ApiMapping(value = "mdc.member.adClick.add", method = RequestMethod.POST)
    public BaseResult<Long> addAdClick(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.AD);
        eventParam.setEventType(EventTypeEnum.CLICK);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

    @ApiOperation(value = "记录最新推荐点击", notes = "记录最新推荐点击,返回点击数")
    @ApiMapping(value = "mdc.member.recommendClick.add", method = RequestMethod.POST)
    public BaseResult<Long> addRecommendClick(MemberEventAddParam param) {
        MemberEventParam eventParam = new MemberEventParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(BusinessTypeEnum.RECOMMEND);
        eventParam.setEventType(EventTypeEnum.CLICK);
        return returnResult(memberEventLogService.increaseBusinessNum(eventParam));
    }

}
