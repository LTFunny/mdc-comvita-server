package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.wechat.MaterialGetParam;
import com.aquilaflycloud.mdc.param.wechat.MaterialListParam;
import com.aquilaflycloud.mdc.service.WechatMaterialService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialVideoInfoResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * WechatMaterialController
 *
 * @author star
 * @date 2019-10-09
 */
@Api(tags = "微信公众号素材接口")
@RestController
public class WechatMaterialController {

    @Resource
    private WechatMaterialService wechatMaterialService;

    @ApiOperation(value = "微信公众号图文素材列表(分页)", notes = "微信公众号图文素材列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:materialList')")
    @ApiMapping(value = "backend.mdc.wechat.materialNews.page", method = RequestMethod.POST, permission = true)
    public IPage<WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> pageMaterialNews(MaterialListParam param) {
        return wechatMaterialService.pageMaterialNews(param);
    }

    @ApiOperation(value = "微信公众号其他素材列表(分页)", notes = "微信公众号其他素材列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:materialList')")
    @ApiMapping(value = "backend.mdc.wechat.material.page", method = RequestMethod.POST, permission = true)
    public IPage<WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> pageMaterial(MaterialListParam param) {
        return wechatMaterialService.pageMaterial(param);
    }

    @ApiOperation(value = "微信公众号图文素材", notes = "微信公众号图文素材")
    @PreAuthorize("hasAuthority('mdc:wechat:materialGet')")
    @ApiMapping(value = "backend.mdc.wechat.materialNews.get", method = RequestMethod.POST, permission = true)
    public WxMpMaterialNews getMaterialNews(MaterialGetParam param) {
        return wechatMaterialService.getMaterialNews(param);
    }

    @ApiOperation(value = "微信公众号视频素材", notes = "微信公众号视频素材")
    @PreAuthorize("hasAuthority('mdc:wechat:materialGet')")
    @ApiMapping(value = "backend.mdc.wechat.materialVideo.get", method = RequestMethod.POST, permission = true)
    public WxMpMaterialVideoInfoResult getMaterialVideo(MaterialGetParam param) {
        return wechatMaterialService.getMaterialVideo(param);
    }

    @ApiOperation(value = "微信公众号其他素材", notes = "微信公众号其他素材")
    @PreAuthorize("hasAuthority('mdc:wechat:materialGet')")
    @ApiMapping(value = "backend.mdc.wechat.material.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getMaterial(MaterialGetParam param) {
        return wechatMaterialService.getMaterial(param);
    }

}
