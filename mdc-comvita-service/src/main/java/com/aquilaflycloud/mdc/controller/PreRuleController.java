package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.PreRuleAddParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleIdParam;
import com.aquilaflycloud.mdc.param.pre.PreRulePageParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleUpdateParam;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.result.pre.PreRuleDetailResult;
import com.aquilaflycloud.mdc.service.PreRuleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * PreRuleController
 * @author linkq
 */
@RestController
@Api(tags = "销售规则")
public class PreRuleController {

    @Resource
    private PreRuleService preRuleService;

    @ApiOperation(value = "销售规则分页信息", notes = "销售规则分页信息")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:page')")
    @ApiMapping(value = "backend.comvita.pre.rule.page", method = RequestMethod.POST, permission = true)
    public IPage<PreRuleDetailResult> page(PreRulePageParam param) {
        return preRuleService.page(param);
    }

    @ApiOperation(value = "销售规则新增", notes = "销售规则新增")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:add')")
    @ApiMapping(value = "backend.comvita.pre.rule.add", method = RequestMethod.POST, permission = true)
    public void add(PreRuleAddParam param) {
        preRuleService.add(param);
    }

    @ApiOperation(value = "销售规则详情", notes = "销售规则详情")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:get')")
    @ApiMapping(value = "backend.comvita.pre.rule.get", method = RequestMethod.POST, permission = true)
    public PreRuleDetailResult get(PreRuleIdParam param) {
        return preRuleService.get(param);
    }

    @ApiOperation(value = "销售规则编辑", notes = "销售规则编辑")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:update')")
    @ApiMapping(value = "backend.comvita.pre.rule.update", method = RequestMethod.POST, permission = true)
    public void update(PreRuleUpdateParam param) {
        preRuleService.update(param);
    }

    @ApiOperation(value = "销售规则停用/启用", notes = "销售规则停用/启用")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:stop')")
    @ApiMapping(value = "backend.comvita.pre.rule.stop_start", method = RequestMethod.POST, permission = true)
    public void cancelStart(PreRuleIdParam param) {
        preRuleService.cancelStart(param);
    }

    @ApiOperation(value = "销售规则设为默认", notes = "销售规则设为默认")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:setDefault')")
    @ApiMapping(value = "backend.comvita.pre.rule.setDefault", method = RequestMethod.POST, permission = true)
    public void setDefault(PreRuleIdParam param) {
        preRuleService.setDefault(param);
    }


    @ApiOperation(value = "获取启用的销售规则", notes = "获取启用的销售规则")
//    @PreAuthorize("hasAuthority('mdc:pre:rule:enableRules')")
    @ApiMapping(value = "backend.comvita.pre.rule.enableRules", method = RequestMethod.POST, permission = true)
    public List<PreEnableRuleResult> getEnableRules() {
        return preRuleService.getEnableRules();
    }

}
