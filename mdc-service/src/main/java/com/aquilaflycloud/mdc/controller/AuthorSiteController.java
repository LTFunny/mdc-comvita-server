package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.author.AuthorSiteListParam;
import com.aquilaflycloud.mdc.result.author.AuthorSite;
import com.aquilaflycloud.mdc.service.AuthorSiteService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * AuthorSiteController
 *
 * @author star
 * @date 2020-04-04
 */
@Api(tags = "授权号(微信或支付宝)接口")
@RestController
public class AuthorSiteController {
    @Resource
    private AuthorSiteService authorSiteService;

    @ApiOperation(value = "查询授权号列表", notes = "查询授权号列表")
    @PreAuthorize("hasAuthority('mdc:author:list')")
    @ApiMapping(value = "backend.mdc.author.site.list", method = RequestMethod.POST, permission = true)
    public List<AuthorSite> listAuthor(AuthorSiteListParam param) {
        return authorSiteService.listAuthor(param);
    }
}
