package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.mission.MissionInfo;
import com.aquilaflycloud.mdc.param.mission.MissionAddParam;
import com.aquilaflycloud.mdc.param.mission.MissionEditParam;
import com.aquilaflycloud.mdc.param.mission.MissionGetParam;
import com.aquilaflycloud.mdc.result.mission.MissionInfoResult;
import com.aquilaflycloud.mdc.service.MissionService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * MissionController
 *
 * @author star
 * @date 2020-05-08
 */
@RestController
@Api(tags = "会员任务管理")
public class MissionController {

    @Resource
    private MissionService missionService;

    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    @PreAuthorize("hasAuthority('mdc:mission:list')")
    @ApiMapping(value = "backend.comvita.mission.info.list", method = RequestMethod.POST, permission = true)
    public List<MissionInfo> listMission() {
        return missionService.listMission();
    }

    @ApiOperation(value = "获取任务详情", notes = "获取任务详情")
    @PreAuthorize("hasAuthority('mdc:mission:get')")
    @ApiMapping(value = "backend.comvita.mission.info.get", method = RequestMethod.POST, permission = true)
    public MissionInfoResult getMission(MissionGetParam param) {
        return missionService.getMission(param);
    }

    @ApiOperation(value = "新增任务", notes = "新增任务")
    @PreAuthorize("hasAuthority('mdc:mission:add')")
    @ApiMapping(value = "backend.comvita.mission.info.add", method = RequestMethod.POST, permission = true)
    public void addMission(MissionAddParam param) {
        missionService.addMission(param);
    }

    @ApiOperation(value = "编辑任务", notes = "编辑任务")
    @PreAuthorize("hasAuthority('mdc:mission:edit')")
    @ApiMapping(value = "backend.comvita.mission.info.edit", method = RequestMethod.POST, permission = true)
    public void editMission(MissionEditParam param) {
        missionService.editMission(param);
    }

    @ApiOperation(value = "启用/停用任务", notes = "启用/停用任务")
    @PreAuthorize("hasAuthority('mdc:mission:edit')")
    @ApiMapping(value = "backend.comvita.mission.info.toggle", method = RequestMethod.POST, permission = true)
    public void toggleMission(MissionGetParam param) {
        missionService.toggleMission(param);
    }
}
