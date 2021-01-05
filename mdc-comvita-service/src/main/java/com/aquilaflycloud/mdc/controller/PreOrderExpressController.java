package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.PreOrderExpressInfoParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderExpressResult;
import com.aquilaflycloud.mdc.service.PreOrderExpressService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
@RestController
@Api(tags = "物流接口")
public class PreOrderExpressController {
    @Resource
    private PreOrderExpressService preOrderExpressService;

    @ApiOperation(value = "获取物流信息", notes = "获取物流信息")
    @PreAuthorize("hasAuthority('mdc:pre:order:express:get')")
    @ApiMapping(value = "backend.comvita.pre.order.express.get", method = RequestMethod.POST, permission = true)
    public List<PreOrderExpressResult> get(PreOrderExpressInfoParam param) {
        return preOrderExpressService.queryTrackInfo(param);
    }
}
