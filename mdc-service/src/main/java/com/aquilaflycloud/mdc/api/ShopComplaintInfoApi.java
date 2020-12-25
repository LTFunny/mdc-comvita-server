package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.service.ShopComplaintInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ShopCommentInfoApi
 *
 * @author zengqingjie
 * @date 2020-04-16
 */
@RestController
@Api(tags = "商铺投诉信息查询")
public class ShopComplaintInfoApi {

    @Resource
    private ShopComplaintInfoService shopComplaintInfoService;

    @ApiOperation(value = "获取投诉信息(分页)", notes = "获取投诉信息(分页)")
    @ApiMapping(value = "comvita.shop.complaint.page", method = RequestMethod.POST)
    public IPage<ShopComplaintInfo> page(ShopComplaintInfoPageApiParam param) {
        return shopComplaintInfoService.getPageComplaintInfo(param);
    }

    @ApiOperation(value = "添加投诉信息", notes = "添加投诉信息")
    @ApiMapping(value = "comvita.shop.complaint.add", method = RequestMethod.POST)
    public void add(ShopComplaintInfoAddApiParam param) {
        shopComplaintInfoService.addComplaintInfo(param);
    }

    @ApiOperation(value = "获取投诉信息", notes = "获取投诉信息")
    @ApiMapping(value = "comvita.shop.complaint.get", method = RequestMethod.POST)
    public ShopComplaintInfo get(ShopComplaintInfoGetParam param) {
        return shopComplaintInfoService.get(param);
    }
}
