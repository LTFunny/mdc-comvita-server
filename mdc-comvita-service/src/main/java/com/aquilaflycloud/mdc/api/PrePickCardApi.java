package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2021/1/9 15:12
 * @Version 1.0
 */
@RestController
@Api(tags = "提货卡接口")
public class PrePickCardApi {

    @Resource
    private PrePickingCardService prePickingCardService;

    @ApiOperation(value = "验证提货卡密码", notes = "验证提货卡密码")
    @ApiMapping(value = "comvita.card.validation.password", method = RequestMethod.POST)
    public PreGoodsInfo validationCardPassWord(PreReservationOrderGoodsParam param) {
        return prePickingCardService.validationCardPassWord(param);
    }

}
