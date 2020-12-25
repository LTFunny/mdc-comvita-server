package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.member.MemberHealthRecord;
import com.aquilaflycloud.mdc.param.member.HealthAddParam;
import com.aquilaflycloud.mdc.param.member.HealthQuickAddParam;
import com.aquilaflycloud.mdc.service.MemberHealthService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberHealthApi
 *
 * @author star
 * @date 2020-04-19
 */
@RestController
@Api(tags = "会员防疫信息接口")
public class MemberHealthApi {

    @Resource
    private MemberHealthService memberHealthService;

    @ApiOperation(value = "获取会员防疫信息", notes = "获取会员防疫信息")
    @ApiMapping(value = "comvita.member.health.get", method = RequestMethod.POST)
    public MemberHealthRecord getHealth() {
        return memberHealthService.getHealth();
    }

    @ApiOperation(value = "记录会员防疫信息", notes = "记录会员防疫信息")
    @ApiMapping(value = "comvita.member.health.add", method = RequestMethod.POST)
    public void addHealth(HealthAddParam param) {
        memberHealthService.addHealth(param);
    }

    @ApiOperation(value = "快捷记录会员防疫信息", notes = "快捷记录会员防疫信息(使用最后信息记录)")
    @ApiMapping(value = "comvita.member.health.quickAdd", method = RequestMethod.POST)
    public void quickAddHealth(HealthQuickAddParam param) {
        memberHealthService.quickAddHealth(param);
    }
}
