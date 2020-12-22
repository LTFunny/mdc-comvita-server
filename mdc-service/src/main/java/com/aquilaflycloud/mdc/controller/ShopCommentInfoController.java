package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopCommentStarRatingSortInfoResult;
import com.aquilaflycloud.mdc.service.ShopCommentInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemShopCommentInfoController
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@RestController
@Api(tags = "商户评论")
public class ShopCommentInfoController {

    @Resource
    private ShopCommentInfoService shopCommentInfoService;

    @ApiOperation(value = "查询商户评论信息(分页)", notes = "查询商户评论信息(分页)")
    @PreAuthorize("hasAuthority('mdc:shop:comment:info:page')")
    @ApiMapping(value = "backend.mdc.shop.comment.info.page", method = RequestMethod.POST, permission = true)
    public IPage<ShopCommentInfo> page(ShopCommentInfoListParam param) {
        return shopCommentInfoService.page(param);
    }

    @ApiOperation(value = "获取商户评论详情", notes = "获取商户评论详情")
    @PreAuthorize("hasAuthority('mdc:shop:comment:info:get')")
    @ApiMapping(value = "backend.mdc.shop.comment.info.get", method = RequestMethod.POST, permission = true)
    public ShopCommentInfo get(ShopCommentInfoGetParam param) {
        return shopCommentInfoService.get(param);
    }

    @ApiOperation(value = "审核商户评论信息", notes = "审核商户评论信息")
    @PreAuthorize("hasAuthority('mdc:shop:comment:info:audit')")
    @ApiMapping(value = "backend.mdc.shop.comment.info.audit", method = RequestMethod.POST, permission = true)
    public void audit(ShopCommentInfoAuditParam param) {
        shopCommentInfoService.audit(param);
    }

    @ApiOperation(value = "删除商户评论信息", notes = "删除商户评论信息")
    @PreAuthorize("hasAuthority('mdc:shop:comment:info:delete')")
    @ApiMapping(value = "backend.mdc.shop.comment.info.delete", method = RequestMethod.POST, permission = true)
    public void delete(ShopCommentInfoDeleteParam param) {
        shopCommentInfoService.delete(param);
    }

    @ApiOperation(value = "评分排名数据", notes = "评分排名数据")
    @PreAuthorize("hasAuthority('mdc:shop:comment:info:page')")
    @ApiMapping(value = "backend.mdc.shop.comment.info.getStarRatingSort", method = RequestMethod.POST, permission = true)
    public ShopCommentStarRatingSortInfoResult getStarRatingSort(ShopCommentStarRatingSortInfoParam param) {
        return shopCommentInfoService.getStarRatingSort(param);
    }
}
