package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.result.member.MemberSignAddResult;
import com.aquilaflycloud.mdc.result.member.MemberSignDescInfoResult;
import com.aquilaflycloud.mdc.service.MemberSignService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberSignApi
 *
 * @author star
 * @date 2020-01-03
 */
@RestController
@Api(tags = "会员签名相关接口")
public class MemberSignApi {

    @Resource
    private MemberSignService memberSignService;

    @ApiOperation(value = "查询会员签到详情", notes = "查询会员签到详情")
    @ApiMapping(value = "mdc.sign.info.get", method = RequestMethod.POST)
    public MemberSignDescInfoResult getSignInfo() {
        return memberSignService.getSignInfo();
    }

    @ApiOperation(value = "会员签到", notes = "会员签到")
    @ApiMapping(value = "mdc.sign.info.add", method = RequestMethod.POST)
    public MemberSignAddResult addSignInfo() {
        return memberSignService.addSignInfo();
    }
}
