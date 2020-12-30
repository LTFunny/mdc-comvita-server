package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardValidationParam;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 14:34
 * @Version 1.0
 */
@RestController
@Api(tags = "提货卡接口")
public class PrePickingCardApi {

    @Resource
    private PrePickingCardService prePickingCardService;

    @ApiOperation(value = "验证提货卡", notes = "验证提货卡")
    @ApiMapping(value = "comvita.picking.card.validation", method = RequestMethod.POST)
    public boolean addStatConfirmOrder(PrePickingCardValidationParam param) {
        return prePickingCardService.validationPickingCard(param);
    }

}
