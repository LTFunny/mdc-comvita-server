package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import com.aquilaflycloud.mdc.param.sign.SignGetParam;
import com.aquilaflycloud.mdc.param.sign.SignRecordPageParam;
import com.aquilaflycloud.mdc.result.sign.OfflineSignResult;
import com.aquilaflycloud.mdc.result.sign.OfflineSignRewardResult;
import com.aquilaflycloud.mdc.result.sign.RecordResult;
import com.aquilaflycloud.mdc.result.sign.SignRecordResult;
import com.aquilaflycloud.mdc.service.OfflineSignActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * OfflineSignActivityApi
 *
 * @author star
 * @date 2020-05-07
 */
@RestController
@Api(tags = "线下打卡活动接口")
public class OfflineSignActivityApi {

    @Resource
    private OfflineSignActivityService offlineSignActivityService;

    @ApiOperation(value = "打卡活动列表", notes = "打卡活动列表")
    @ApiMapping(value = "comvita.offlineSign.info.page", method = RequestMethod.POST)
    public IPage<OfflineSignActivity> pageOfflineSign(PageParam<OfflineSignActivity> param) {
        return offlineSignActivityService.pageOfflineSign(param);
    }

    @ApiOperation(value = "获取打卡活动详情", notes = "获取打卡活动详情")
    @ApiMapping(value = "comvita.offlineSign.info.get", method = RequestMethod.POST)
    public OfflineSignResult getOfflineSign(SignGetParam param) {
        return offlineSignActivityService.getOfflineSign(param);
    }

    @ApiOperation(value = "新增打卡记录", notes = "新增打卡记录,会员打卡并返回奖品信息")
    @ApiMapping(value = "comvita.offlineSign.record.add", method = RequestMethod.POST)
    public OfflineSignRewardResult addOfflineSignRecord(SignGetParam param) {
        return offlineSignActivityService.addOfflineSignRecord(param);
    }

    @ApiOperation(value="获取打卡记录列表", notes = "获取打卡记录列表")
    @ApiMapping(value = "comvita.offlineSign.record.page", method = RequestMethod.POST)
    public IPage<RecordResult> pageOfflineSignRecord(SignRecordPageParam param) {
        return offlineSignActivityService.pageOfflineSignRecord(param);
    }

    @ApiOperation(value="获取打卡活动记录列表", notes = "获取打卡活动记录列表")
    @ApiMapping(value = "comvita.offlineSign.memberRecord.page", method = RequestMethod.POST)
    public IPage<SignRecordResult> pageOfflineSignMemberRecord(PageParam<OfflineSignMemberRecord> param) {
        return offlineSignActivityService.pageOfflineSignMemberRecord(param);
    }
}
