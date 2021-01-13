package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyCatalog;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.folksonomy.*;
import com.aquilaflycloud.mdc.result.folksonomy.FolksonomyCatalogNode;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * FolksonomyController
 *
 * @author star
 * @date 2019-11-28
 */
@RestController
@Api(tags = "标签管理")
public class FolksonomyController {

    @Resource
    private FolksonomyService folksonomyService;

    @ApiOperation(value = "获取功能标签列表", notes = "获取功能标签列表")
    @PreAuthorize("hasAuthority('mdc:folksonomy:list')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.list", method = RequestMethod.POST, permission = true)
    public List<FolksonomyInfo> listBusiness(FolksonomyListParam param) {
        return folksonomyService.listBusinessFolksonomy(param);
    }

    @ApiOperation(value = "获取功能标签列表(分页)", notes = "获取功能标签列表(分页)")
    @PreAuthorize("hasAuthority('mdc:folksonomy:list')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.page", method = RequestMethod.POST, permission = true)
    public IPage<FolksonomyInfo> pageBusiness(FolksonomyPageParam param) {
        return folksonomyService.pageBusinessFolksonomy(param);
    }

    @ApiOperation(value = "新增标签目录", notes = "新增标签目录")
    @PreAuthorize("hasAuthority('mdc:folksonomyCatalog:add')")
    @ApiMapping(value = "backend.comvita.folksonomy.catalog.add", method = RequestMethod.POST, permission = true)
    public BaseResult<Long> addCatalog(FolksonomyCatalogAddParam param) {
        return folksonomyService.addFolksonomyCatalog(param);
    }

    @ApiOperation(value = "编辑标签目录", notes = "编辑标签目录")
    @PreAuthorize("hasAuthority('mdc:folksonomyCatalog:edit')")
    @ApiMapping(value = "backend.comvita.folksonomy.catalog.edit", method = RequestMethod.POST, permission = true)
    public void editCatalog(FolksonomyCatalogEditParam param) {
        folksonomyService.editFolksonomyCatalog(param);
    }

    @ApiOperation(value = "删除标签目录", notes = "删除标签目录")
    @PreAuthorize("hasAuthority('mdc:folksonomyCatalog:delete')")
    @ApiMapping(value = "backend.comvita.folksonomy.catalog.delete", method = RequestMethod.POST, permission = true)
    public void deleteCatalog(FolksonomyCatalogDeleteParam param) {
        folksonomyService.deleteFolksonomyCatalog(param);
    }

    @ApiOperation(value = "获取标签目录", notes = "获取标签目录")
    @PreAuthorize("hasAuthority('mdc:folksonomyCatalog:get')")
    @ApiMapping(value = "backend.comvita.folksonomy.catalog.get", method = RequestMethod.POST, permission = true)
    public FolksonomyCatalog getCatalog(FolksonomyCatalogGetParam param) {
        return folksonomyService.getFolksonomyCatalog(param);
    }

    @ApiOperation(value = "获取标签树", notes = "获取标签树")
    @PreAuthorize("hasAuthority('mdc:folksonomy:list')")
    @ApiMapping(value = "backend.comvita.folksonomy.tree.list", method = RequestMethod.POST, permission = true)
    public List<FolksonomyCatalogNode> listTree(FolksonomyCatalogListParam param) {
        return folksonomyService.listFolksonomyTree(param);
    }

    @ApiOperation(value = "新增功能标签", notes = "新增功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:add')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.add", method = RequestMethod.POST, permission = true)
    public BaseResult<Long> addBusiness(FolksonomyAddParam param) {
        return folksonomyService.addBusinessFolksonomy(param);
    }

    @ApiOperation(value = "修改功能标签", notes = "修改功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:edit')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.edit", method = RequestMethod.POST, permission = true)
    public void editBusiness(FolksonomyEditParam param) {
        folksonomyService.editBusinessFolksonomy(param);
    }

    @ApiOperation(value = "获取功能标签", notes = "获取功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:get')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.get", method = RequestMethod.POST, permission = true)
    public FolksonomyInfo getBusiness(FolksonomyGetParam param) {
        return folksonomyService.getFolksonomy(param);
    }

    @ApiOperation(value = "删除功能标签", notes = "删除功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:delete')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.delete", method = RequestMethod.POST, permission = true)
    public void deleteBusiness(FolksonomyGetParam param) {
        folksonomyService.deleteFolksonomy(param);
    }

    @ApiOperation(value = "会员打上功能标签", notes = "会员打上功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomyRel:add')")
    @ApiMapping(value = "backend.comvita.folksonomy.memberRel.add", method = RequestMethod.POST, permission = true)
    public void addFolksonomyMemberRel(FolksonomyMemberRelParam param) {
        folksonomyService.addFolksonomyMemberRel(param);
    }

    @ApiOperation(value = "删除会员功能标签", notes = "删除会员功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomyRel:delete')")
    @ApiMapping(value = "backend.comvita.folksonomy.memberRel.delete", method = RequestMethod.POST, permission = true)
    public void deleteFolksonomyMemberRel(FolksonomyMemberRelParam param) {
        folksonomyService.deleteFolksonomyMemberRel(param);
    }

    @ApiOperation(value = "获取会员标签列表(分页)", notes = "获取会员标签列表(分页)")
    @PreAuthorize("hasAuthority('mdc:folksonomy:list')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.page", method = RequestMethod.POST, permission = true)
    public IPage<FolksonomyInfo> pageMember(FolksonomyPageParam param) {
        return folksonomyService.pageMemberFolksonomy(param);
    }
}
