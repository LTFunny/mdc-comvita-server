package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.ClientItemGetParam;
import com.aquilaflycloud.mdc.service.ClientConfigService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ConfigApi
 *
 * @author star
 * @date 2020-04-21
 */
@RestController
@Api(tags = "客户端配置接口")
public class ConfigApi {
    @Resource
    private ClientConfigService clientConfigService;

    @ApiOperation(value = "获取客户端配置值", notes = "获取客户端配置值")
    @ApiMapping(value = "comvita.client.configItem.get", method = RequestMethod.POST)
    public BaseResult getItem(ClientItemGetParam param) {
        return clientConfigService.getItem(param);
    }
}
