package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopComplaintStatisticsResult;
import com.aquilaflycloud.mdc.service.ShopComplaintInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemShopComplaintInfoController
 *
 * @author zengqingjie
 * @date 2020-04-22
 */
@RestController
@Api(tags = "客户投诉信息")
public class ShopComplaintInfoController {

    @Resource
    private ShopComplaintInfoService shopComplaintInfoService;

    @ApiOperation(value = "查询用户投诉信息(分页)", notes = "查询用户投诉信息(分页)")
    @PreAuthorize("hasAuthority('mdc:shop:complaint:info:page')")
    @ApiMapping(value = "backend.comvita.shop.complaint.info.page", method = RequestMethod.POST, permission = true)
    public IPage<ShopComplaintInfo> page(ShopComplaintInfoPageParam param) {
        return shopComplaintInfoService.page(param);
    }

    @ApiOperation(value = "查询用户投诉详情", notes = "查询用户投诉详情")
    @PreAuthorize("hasAuthority('mdc:shop:complaint:info:get')")
    @ApiMapping(value = "backend.comvita.shop.complaint.info.get", method = RequestMethod.POST, permission = true)
    public ShopComplaintInfo get(ShopComplaintInfoGetParam param) {
        return shopComplaintInfoService.get(param);
    }

    @ApiOperation(value = "更新用户投诉", notes = "更新用户投诉")
    @PreAuthorize("hasAuthority('mdc:shop:complaint:info:edit')")
    @ApiMapping(value = "backend.comvita.shop.complaint.info.edit", method = RequestMethod.POST, permission = true)
    public void edit(ShopComplaintInfoEditParam param) {
        shopComplaintInfoService.edit(param);
    }

    @ApiOperation(value = "删除用户投诉", notes = "删除用户投诉")
    @PreAuthorize("hasAuthority('mdc:shop:complaint:info:delete')")
    @ApiMapping(value = "backend.comvita.shop.complaint.info.delete", method = RequestMethod.POST, permission = true)
    public void delete(ShopComplaintInfoDeleteParam param) {
        shopComplaintInfoService.delete(param);
    }

    @ApiOperation(value = "投诉统计", notes = "投诉统计")
    @PreAuthorize("hasAuthority('mdc:shop:complaint:info:page')")
    @ApiMapping(value = "backend.comvita.shop.complaint.info.statistics", method = RequestMethod.POST, permission = true)
    public ShopComplaintStatisticsResult statistics(ShopComplaintStatisticsParam param) {
        return shopComplaintInfoService.statistics(param);
    }
}
