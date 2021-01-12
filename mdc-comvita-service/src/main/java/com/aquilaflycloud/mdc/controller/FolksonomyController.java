package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.folksonomy.*;
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

    @ApiOperation(value = "新增功能标签", notes = "新增功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:add')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.add", method = RequestMethod.POST, permission = true)
    public BaseResult<Long> addBusiness(FolksonomyAddParam param) {
        return folksonomyService.addBusinessFolksonomy(param);
    }

    @ApiOperation(value = "修改功能标签", notes = "修改功能标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:edit')")
    @ApiMapping(value = "backend.comvita.folksonomy.business.edit", method = RequestMethod.POST, permission = true)
    public void addBusiness(FolksonomyEditParam param) {
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

    @ApiOperation(value = "获取会员标签列表(分页)", notes = "获取会员标签列表(分页)")
    @PreAuthorize("hasAuthority('mdc:folksonomy:list')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.page", method = RequestMethod.POST, permission = true)
    public IPage<FolksonomyInfo> pageMember(FolksonomyPageParam param) {
        return folksonomyService.pageMemberFolksonomy(param);
    }

    @ApiOperation(value = "新增会员标签", notes = "新增会员标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:add')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.add", method = RequestMethod.POST, permission = true)
    public BaseResult<Long> addMember(FolksonomyAddParam param) {
        return folksonomyService.addMemberFolksonomy(param);
    }

    @ApiOperation(value = "修改会员标签", notes = "修改会员标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:edit')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.edit", method = RequestMethod.POST, permission = true)
    public void addMember(FolksonomyEditParam param) {
        folksonomyService.editMemberFolksonomy(param);
    }

    @ApiOperation(value = "获取会员标签", notes = "获取会员标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:get')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.get", method = RequestMethod.POST, permission = true)
    public FolksonomyInfo getMember(FolksonomyGetParam param) {
        return folksonomyService.getFolksonomy(param);
    }

    @ApiOperation(value = "删除会员标签", notes = "删除会员标签")
    @PreAuthorize("hasAuthority('mdc:folksonomy:delete')")
    @ApiMapping(value = "backend.comvita.folksonomy.member.delete", method = RequestMethod.POST, permission = true)
    public void deleteMember(FolksonomyGetParam param) {
        folksonomyService.deleteFolksonomy(param);
    }
}
