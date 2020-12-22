package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.param.alipay.*;
import com.aquilaflycloud.mdc.service.AlipayAuthorSiteService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * AlipayAuthorSiteController
 *
 * @author star
 * @date 2019-12-25
 */
@Api(tags = "支付宝授权接口")
@RestController
public class AlipayAuthorSiteController {

    @Resource
    private AlipayAuthorSiteService alipayAuthorSiteService;

    @ApiOperation(value = "查询授权号列表(分页)", notes = "查询授权号列表(分页)")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteList')")
    @ApiMapping(value = "backend.mdc.alipay.author.page", method = RequestMethod.POST, permission = true)
    public IPage<AlipayAuthorSite> pageAuthor(AlipayAuthorSitePageParam param) {
        return alipayAuthorSiteService.pageAuthor(param);
    }

    @ApiOperation(value = "获取预授权链接", notes = "获取预授权链接,可供第三方授权")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.preAuthUrl.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getPreAuthUrl(AlipayPreAuthUrlGetParam param) {
        return alipayAuthorSiteService.getPreAuthUrl(param);
    }

    @ApiOperation(value = "新增授权应用", notes = "新增授权应用")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.author.add", method = RequestMethod.POST, permission = true)
    public void addAuthor(AlipayAuthorSiteAddParam param) {
        alipayAuthorSiteService.addAuthor(param);
    }

    @ApiOperation(value = "编辑授权应用", notes = "编辑授权应用")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.author.edit", method = RequestMethod.POST, permission = true)
    public void editAuthor(AlipayAuthorSiteEditParam param) {
        alipayAuthorSiteService.editAuthor(param);
    }

    @ApiOperation(value = "删除授权应用", notes = "删除授权应用")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.author.delete", method = RequestMethod.POST, permission = true)
    public void deleteAuthor(AlipayAuthorSiteGetParam param) {
        alipayAuthorSiteService.deleteAuthor(param);
    }

    @ApiOperation(value = "更新第三方授权生活号", notes = "更新第三方授权生活号")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.authorPublicInfo.update", method = RequestMethod.POST, permission = true)
    public void updatePublicInfo(AlipayAuthorSiteGetParam param) {
        alipayAuthorSiteService.updatePublicInfo(param);
    }

    @ApiOperation(value = "获取授权页面URL", notes = "获取授权页面URL,根据参数生成Oauth2链接,通过此链接可获取支付宝userId等参数")
    @PreAuthorize("hasAuthority('mdc:alipay:authorSiteSave')")
    @ApiMapping(value = "backend.mdc.alipay.oauth2Url.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getOauth2Url(AlipayOauth2UrlGetParam param) {
        return new BaseResult<String>().setResult(alipayAuthorSiteService.getOauth2Url(param));
    }
}
