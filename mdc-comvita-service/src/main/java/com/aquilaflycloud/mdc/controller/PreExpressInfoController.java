package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreExpressInfo;
import com.aquilaflycloud.mdc.param.pre.PreExpressInfoPageParam;
import com.aquilaflycloud.mdc.service.PreExpressInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
@RestController
@Api(tags = "快递编码相关")
public class PreExpressInfoController {
    @Resource
    private PreExpressInfoService preExpressInfoService;

    @ApiOperation(value = "获取快递编码信息分页", notes = "获取快递编码信息分页")
//    @PreAuthorize("hasAuthority('mdc:pre:express:info:page')")
    @ApiMapping(value = "backend.comvita.pre.express.info.page", method = RequestMethod.POST, permission = true)
    public IPage<PreExpressInfo> page(PreExpressInfoPageParam param) {
        return preExpressInfoService.page(param);
    }
}
