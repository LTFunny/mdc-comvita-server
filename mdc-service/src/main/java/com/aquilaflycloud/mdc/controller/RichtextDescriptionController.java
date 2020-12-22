package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.richtext.RichtextDescriptionInfo;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionEditParam;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionGetParam;
import com.aquilaflycloud.mdc.service.RichtextDescriptionService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * RichtextDescriptionController
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
@RestController
@Api(tags = "富文本描述信息")
public class RichtextDescriptionController {
    @Resource
    private RichtextDescriptionService richtextDescriptionService;

    @ApiOperation(value = "根据类型获取对应的描述信息", notes = "根据类型获取对应的描述信息")
    @PreAuthorize("hasAuthority('mdc:richtext:info:getByType')")
    @ApiMapping(value = "backend.mdc.richtext.info.getByType", method = RequestMethod.POST, permission = true)
    public RichtextDescriptionInfo getByType(RichtextDescriptionGetParam param) {
        return richtextDescriptionService.getByType(param);
    }

    @ApiOperation(value = "更新或添加描述信息", notes = "更新或添加描述信息")
    @PreAuthorize("hasAuthority('mdc:richtext:info:edit')")
    @ApiMapping(value = "backend.mdc.richtext.info.edit", method = RequestMethod.POST, permission = true)
    public RichtextDescriptionInfo edit(RichtextDescriptionEditParam param) {
        return richtextDescriptionService.edit(param);
    }
}
