package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.mapper.TicketScenicSpotInfoMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.TicketScenicSpotInfoResult;
import com.aquilaflycloud.mdc.service.BizEnumService;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.TicketScenicSpotInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 景区信息服务实现类
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Service
public class TicketScenicSpotInfoServiceImpl implements TicketScenicSpotInfoService {
    @Resource
    private TicketScenicSpotInfoMapper ticketScenicSpotInfoMapper;
    @Resource
    private FolksonomyService folksonomyService;
    @Resource
    private BizEnumService bizEnumService;

    @Override
    public IPage<TicketScenicSpotInfo> listScenicSpotInfo(ScenicSpotInfoListParam param) {
        return ticketScenicSpotInfoMapper
                .selectPage(param.page(), Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                        .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()));
    }

    @Override
    public List<TicketScenicSpotInfo> list() {
        //不分页查询
        return ticketScenicSpotInfoMapper.selectList(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .isNotNull(TicketScenicSpotInfo::getAccountId)
                .isNotNull(TicketScenicSpotInfo::getType));
    }

    @Override
    public TicketScenicSpotInfo getScenicSpotInfoWithAuth(ScenicSpotInfoGetParam param) {
        //包含数据权限
        return ticketScenicSpotInfoMapper.selectOne(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getType, param.getType())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()));
    }

    @Override
    public TicketScenicSpotInfoResult getScenicSpotInfo(ScenicSpotInfoGetParam param) {
        TicketScenicSpotInfo info = ticketScenicSpotInfoMapper.selectOne(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getType, param.getType()));
        TicketScenicSpotInfoResult result = Convert.convert(TicketScenicSpotInfoResult.class, info);
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.SCENICSPOTS, info.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);
        return result;
    }

    @Override
    public TicketScenicSpotInfo saveScenicSpotInfo(ScenicSpotInfoSaveParam param) {
        TicketScenicSpotInfo ticketScenicSpotInfo = ticketScenicSpotInfoMapper.selectOne(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getType, param.getType()));
        int count;
        if (ticketScenicSpotInfo == null) {
            ticketScenicSpotInfo = new TicketScenicSpotInfo();
            BeanUtil.copyProperties(param, ticketScenicSpotInfo);
            //保存
            count = ticketScenicSpotInfoMapper.insert(ticketScenicSpotInfo);
        } else {
            TicketScenicSpotInfo update = new TicketScenicSpotInfo();
            BeanUtil.copyProperties(param, update);
            update.setId(ticketScenicSpotInfo.getId());
            update.setAccountId(ticketScenicSpotInfo.getAccountId());
            //不为空则是编辑，更新相关字段
            count = ticketScenicSpotInfoMapper.updateById(update);
        }
        if (count > 0) {
            //保存业务功能标签
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.SCENICSPOTS, ticketScenicSpotInfo.getId(), param.getFolksonomyIds());
            return ticketScenicSpotInfo;
        }
        throw new ServiceException("保存景区信息，请重试");
    }

    @Override
    public int updateById(ScenicSpotInfoUpdateByIdParam param) {
        TicketScenicSpotInfo ticketScenicSpotInfo = new TicketScenicSpotInfo();
        BeanUtil.copyProperties(param, ticketScenicSpotInfo);
        return ticketScenicSpotInfoMapper.updateById(ticketScenicSpotInfo);
    }
}
