package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.ticket.TicketInterfaceAccountInfo;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoAddParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoEditParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoListParam;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoRelationResult;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoResult;

import java.util.List;

/**
 * <p>
 * 第三方接口(道控)账号信息服务类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
public interface TicketInterfaceAccountInfoService {
    /**
     * 获取第三方账号(道控)信息
     * @return
     */
    List<TicketInterfaceAccountInfoResult> getInterfaceAccountInfo();

    /**
     * 获取第三方账号(道控)信息(不包含租户解析)
     * @return
     */
    List<TicketInterfaceAccountInfoResult> normalGetInterfaceAccountInfo();

    /**
     * 获取账号列表
     * @return
     */
    List<TicketInterfaceAccountInfoRelationResult> list(InterfaceAccountInfoListParam param);

    /**
     * 添加账号信息及关联景区
     * @param param
     * @return
     */
    TicketInterfaceAccountInfoRelationResult add(InterfaceAccountInfoAddParam param);

    /**
     * 编辑账号信息
     * @param param
     * @return
     */
    TicketInterfaceAccountInfoRelationResult edit(InterfaceAccountInfoEditParam param);
}
