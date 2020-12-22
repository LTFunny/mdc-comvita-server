package com.aquilaflycloud.mdc.controller;


import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoAddParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoEditParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoListParam;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoRelationResult;
import com.aquilaflycloud.mdc.service.TicketInterfaceAccountInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 第三方接口(道控)账号信息控制器
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-29
 */
@RestController
@Api(tags = "第三方接口(道控)账号信息相关接口")
public class TicketInterfaceAccountInfoController {
    @Resource
    private TicketInterfaceAccountInfoService ticketInterfaceAccountInfoService;

    @ApiOperation("获取景区账号关联列表")
    @PreAuthorize("hasAuthority('mdc:ticket:interfaceAccount:list')")
    @ApiMapping(value = "backend.mdc.ticket.interfaceAccount.list", method = RequestMethod.POST, permission = true)
    public List<TicketInterfaceAccountInfoRelationResult> list(InterfaceAccountInfoListParam param) {
        return ticketInterfaceAccountInfoService.list(param);
    }

    @ApiOperation("添加景区账号关联信息")
    @PreAuthorize("hasAuthority('mdc:ticket:interfaceAccount:add')")
    @ApiMapping(value = "backend.mdc.ticket.interfaceAccount.add", method = RequestMethod.POST, permission = true)
    public TicketInterfaceAccountInfoRelationResult add(InterfaceAccountInfoAddParam param) {
        return ticketInterfaceAccountInfoService.add(param);
    }

    @ApiOperation("编辑景区账号关联信息")
    @PreAuthorize("hasAuthority('mdc:ticket:interfaceAccount:edit')")
    @ApiMapping(value = "backend.mdc.ticket.interfaceAccount.edit", method = RequestMethod.POST, permission = true)
    public TicketInterfaceAccountInfoRelationResult edit(InterfaceAccountInfoEditParam param) {
        return ticketInterfaceAccountInfoService.edit(param);
    }
}
