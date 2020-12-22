package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.member.MemberGrade;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberGradeResult;
import com.aquilaflycloud.mdc.service.MemberGradeService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * MemberGradeController
 *
 * @author star
 * @date 2020-03-05
 */
@RestController
@Api(tags = "会员等级管理")
public class MemberGradeController {

    @Resource
    private MemberGradeService memberGradeService;

    @ApiOperation(value = "查询会员等级列表", notes = "查询会员等级列表")
    @PreAuthorize("hasAuthority('mdc:memberGrade:list')")
    @ApiMapping(value = "backend.mdc.member.grade.list", method = RequestMethod.POST, permission = true)
    public List<MemberGradeResult> listMemberGrade(MemberGradeListParam param) {
        return memberGradeService.listMemberGrade(param);
    }

    @ApiOperation(value = "获取会员等级", notes = "获取会员等级")
    @PreAuthorize("hasAuthority('mdc:memberGrade:get')")
    @ApiMapping(value = "backend.mdc.member.grade.get", method = RequestMethod.POST, permission = true)
    public MemberGrade getMemberGrade(MemberGradeGetParam param) {
        return memberGradeService.getMemberGrade(param);
    }

    @ApiOperation(value = "批量删除并新增会员等级", notes = "批量删除并新增会员等级")
    @PreAuthorize("hasAuthority('mdc:memberGrade:add')")
    @ApiMapping(value = "backend.mdc.member.grade.batchAdd", method = RequestMethod.POST, permission = true)
    public void batchAddMemberGrade(MemberGradeBatchAddParam param) {
        memberGradeService.batchAddMemberGrade(param);
    }

    @ApiOperation(value = "新增会员等级", notes = "新增会员等级")
    @PreAuthorize("hasAuthority('mdc:memberGrade:add')")
    @ApiMapping(value = "backend.mdc.member.grade.add", method = RequestMethod.POST, permission = true)
    public void addMemberGrade(MemberGradeAddParam param) {
        memberGradeService.addMemberGrade(param);
    }

    @ApiOperation(value = "编辑会员等级", notes = "编辑会员等级")
    @PreAuthorize("hasAuthority('mdc:memberGrade:edit')")
    @ApiMapping(value = "backend.mdc.member.grade.edit", method = RequestMethod.POST, permission = true)
    public void editMemberGrade(MemberGradeEditParam param) {
        memberGradeService.editMemberGrade(param);
    }

    @ApiOperation(value = "删除会员等级", notes = "删除会员等级")
    @PreAuthorize("hasAuthority('mdc:memberGrade:delete')")
    @ApiMapping(value = "backend.mdc.member.grade.delete", method = RequestMethod.POST, permission = true)
    public void deleteMemberGrade(MemberGradeGetParam param) {
        memberGradeService.deleteMemberGrade(param);
    }
}
