package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.richtext.RichtextDescriptionInfo;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionGetParam;
import com.aquilaflycloud.mdc.service.RichtextDescriptionService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "富文本描述")
public class RichtextDescriptionApi {
    @Resource
    private RichtextDescriptionService richtextDescriptionService;

    @ApiOperation(value = "获取富文本描述信息", notes = "获取富文本描述信息")
    @ApiMapping(value = "mdc.richtext.description.get", method = RequestMethod.POST)
    public RichtextDescriptionInfo get(RichtextDescriptionGetParam param) {
        return richtextDescriptionService.getByType(param);
    }
}
