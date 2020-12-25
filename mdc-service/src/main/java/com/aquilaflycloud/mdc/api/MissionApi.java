package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.mission.MissionMemberRecord;
import com.aquilaflycloud.mdc.result.mission.MissionRecordResult;
import com.aquilaflycloud.mdc.service.MissionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MissionApi
 *
 * @author star
 * @date 2020-05-08
 */
@RestController
@Api(tags = "会员任务接口")
public class MissionApi {
    @Resource
    private MissionService missionService;

    @ApiOperation(value = "获取会员任务记录列表(分页)", notes = "获取会员任务记录列表(分页)")
    @ApiMapping(value = "comvita.mission.record.page", method = RequestMethod.POST)
    public IPage<MissionRecordResult> pageRecord(PageParam<MissionMemberRecord> param) {
        return missionService.pageRecord(param);
    }
}
