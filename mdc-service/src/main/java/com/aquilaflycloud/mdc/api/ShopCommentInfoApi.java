package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.param.shop.ShopCommentInfoAddApiParam;
import com.aquilaflycloud.mdc.param.shop.ShopCommentInfoPageApiParam;
import com.aquilaflycloud.mdc.service.ShopCommentInfoService;
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
@Api(tags = "商铺评论信息查询")
public class ShopCommentInfoApi {

    @Resource
    private ShopCommentInfoService shopCommentInfoService;

    @ApiOperation(value = "获取评论信息(分页)", notes = "获取评论信息(分页)")
    @ApiMapping(value = "mdc.shop.comment.page", method = RequestMethod.POST)
    public IPage<ShopCommentInfo> page(ShopCommentInfoPageApiParam param) {
        return shopCommentInfoService.getPageCommentInfo(param);
    }

    @ApiOperation(value = "添加评论信息", notes = "添加评论信息")
    @ApiMapping(value = "mdc.shop.comment.add", method = RequestMethod.POST)
    public void add(ShopCommentInfoAddApiParam param) {
        shopCommentInfoService.addCommentInfo(param);
    }
}
