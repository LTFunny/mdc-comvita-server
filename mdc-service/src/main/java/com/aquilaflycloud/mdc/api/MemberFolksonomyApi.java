package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.folksonomy.FolksonomyGetParam;
import com.aquilaflycloud.mdc.param.folksonomy.FolksonomyListParam;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * MemberFolksonomyApi
 *
 * @author star
 * @date 2020-03-07
 */
@RestController
@Api(tags = "会员标签接口")
public class MemberFolksonomyApi {
    @Resource
    private FolksonomyService folksonomyService;

    @ApiOperation(value = "获取会员标签列表", notes = "获取会员标签列表")
    @ApiMapping(value = "mdc.folksonomy.member.list", method = RequestMethod.POST)
    public List<FolksonomyInfo> listMemberFolksonomy(FolksonomyListParam param) {
        return folksonomyService.listMemberFolksonomy(param);
    }

    @ApiOperation(value = "关联标签与会员", notes = "关联标签与会员")
    @ApiMapping(value = "mdc.folksonomy.memberRel.add", method = RequestMethod.POST)
    public void addFolksonomyMemberRel(FolksonomyGetParam param) {
        folksonomyService.addFolksonomyMemberRel(param);
    }

    @ApiOperation(value = "删除标签与会员关联", notes = "删除标签与会员关联")
    @ApiMapping(value = "mdc.folksonomy.memberRel.delete", method = RequestMethod.POST)
    public void deleteFolksonomyMemberRel(FolksonomyGetParam param) {
        folksonomyService.deleteFolksonomyMemberRel(param);
    }

    @ApiOperation(value = "获取标签与会员关联列表", notes = "获取标签与会员关联列表")
    @ApiMapping(value = "mdc.folksonomy.memberRel.list", method = RequestMethod.POST)
    public List<FolksonomyInfo> listMemberRelFolksonomy() {
        return folksonomyService.listMemberRelFolksonomy();
    }
}
