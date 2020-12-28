package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.exchange.ExchangeSpecValueInfo;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueAddParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueGetParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValuePageParam;
import com.aquilaflycloud.mdc.param.exchange.GoodsPageParam;
import com.aquilaflycloud.mdc.result.exchange.GoodsResult;
import com.aquilaflycloud.mdc.service.ExchangeSpecValueInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ExchangeSpecValueInfoController
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@RestController
@Api(tags = "兑换商城规格和规格值")
public class ExchangeSpecValueInfoController {

    @Resource
    private ExchangeSpecValueInfoService exchangeSpecValueInfoService;

    @ApiOperation(value = "分页规格和规格值", notes = "分页规格和规格值")
    @PreAuthorize("hasAuthority('mdc:exchangeSpecValue:list')")
    @ApiMapping(value = "backend.comvita.exchange.specValue.page", method = RequestMethod.POST, permission = true)
    public IPage<ExchangeSpecValueInfo> page(ExchangeSpecValuePageParam param) {
        return exchangeSpecValueInfoService.page(param);
    }

    @ApiOperation(value = "规格和规格值添加", notes = "规格和规格值添加")
    @PreAuthorize("hasAuthority('mdc:exchangeSpecValue:add')")
    @ApiMapping(value = "backend.comvita.exchange.specValue.add", method = RequestMethod.POST, permission = true)
    public ExchangeSpecValueInfo add(ExchangeSpecValueAddParam param) {
        return exchangeSpecValueInfoService.add(param);
    }

    @ApiOperation(value = "获取规格和规格值信息", notes = "获取规格和规格值信息")
    @PreAuthorize("hasAuthority('mdc:exchangeSpecValue:get')")
    @ApiMapping(value = "backend.comvita.exchange.specValue.get", method = RequestMethod.POST, permission = true)
    public ExchangeSpecValueInfo get(ExchangeSpecValueGetParam param) {
        return exchangeSpecValueInfoService.get(param);
    }
}
