package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.TicketScenicSpotInfoResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 景区信息服务类
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
public interface TicketScenicSpotInfoService {
    /**
     * 获取景区信息列表
     * @param param
     * @return
     */
    IPage<TicketScenicSpotInfo> listScenicSpotInfo(ScenicSpotInfoListParam param);

    /**
     * 不分页获取景区信息列表
     * @return
     */
    List<TicketScenicSpotInfo> list();

    /**
     * 编辑景区信息
     * @param param
     * @return
     */
    TicketScenicSpotInfo getScenicSpotInfoWithAuth(ScenicSpotInfoGetParam param);

    /**
     * 小程序获取景区信息
     * @param param
     * @return
     */
    TicketScenicSpotInfoResult getScenicSpotInfo(ScenicSpotInfoGetParam param);

    /**
     * 保存景区信息
     * @param param
     * @return
     */
    TicketScenicSpotInfo saveScenicSpotInfo(ScenicSpotInfoSaveParam param);

    /**
     * 根据景区id更新指定可看部门ids
     * @param param
     * @return
     */
    int updateById(ScenicSpotInfoUpdateByIdParam param);
}
