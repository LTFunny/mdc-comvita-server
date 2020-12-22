package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.mapper.InformationMapper;
import com.aquilaflycloud.mdc.model.information.Information;
import com.aquilaflycloud.mdc.param.information.*;
import com.aquilaflycloud.mdc.service.InformationService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * InformationServiceImpl
 *
 * @author star
 * @date 2020-03-18
 */
@Slf4j
@Service
public class InformationServiceImpl implements InformationService {
    @Resource
    private InformationMapper informationMapper;

    @Override
    public IPage<Information> pageInfo(InfoPageParam param) {
        return informationMapper.selectPage(param.page(), Wrappers.<Information>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), Information::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getInfoTitle()), Information::getInfoTitle, param.getInfoTitle())
                .eq(param.getInfoType() != null, Information::getInfoType, param.getInfoType())
                .eq(param.getState() != null, Information::getState, param.getState())
                .eq(param.getImportance() != null, Information::getImportance, param.getImportance())
                .ge(param.getCreateTimeStart() != null, Information::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, Information::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(Information::getCreateTime)
        ).convert(info -> {
            info.setInfoContent(null);
            return info;
        });
    }

    @Override
    public Information getInfo(InfoGetParam param) {
        return informationMapper.selectById(param.getId());
    }

    @Override
    public Information getInfo(InfoOneGetParam param) {
        List<Information> list = informationMapper.selectList(Wrappers.<Information>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), Information::getAppId, param.getAppId())
                .eq(Information::getInfoType, param.getInfoType())
                .orderByDesc(Information::getImportance, Information::getCreateTime)
                .last("limit 1")
        );
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void addInfo(InfoAddParam param) {
        Information info = new Information();
        BeanUtil.copyProperties(param, info);
        int count = informationMapper.insert(info);
        if (count <= 0) {
            throw new ServiceException("新增资讯失败");
        }
    }

    @Override
    public void editInfo(InfoEditParam param) {
        Information update = new Information();
        BeanUtil.copyProperties(param, update);
        int count = informationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑资讯失败");
        }
    }

    @Override
    public void toggleInfo(InfoGetParam param) {
        Information info = informationMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("资讯不存在");
        }
        Information update = new Information();
        update.setId(param.getId());
        update.setState(info.getState() == StateEnum.NORMAL ? StateEnum.DISABLE : StateEnum.NORMAL);
        int count = informationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void deleteInfo(InfoGetParam param) {
        Information info = informationMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("资讯不存在");
        }
        if (info.getState() == StateEnum.NORMAL) {
            throw new ServiceException("只能删除停用资讯");
        }
        int count = informationMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    public List<Information> listInformation(InfoListParam param) {
        String otherAppId = MdcUtil.getOtherAppId();
        return informationMapper.selectList(Wrappers.<Information>lambdaQuery()
                .and(i -> i.eq(StrUtil.isNotBlank(otherAppId), Information::getAppId, otherAppId)
                        .or().eq(Information::getAppId, MdcConstant.UNIVERSAL_APP_ID)
                )
                .eq(Information::getInfoType, param.getInfoType())
                .eq(Information::getState, StateEnum.NORMAL)
                .orderByDesc(Information::getImportance, Information::getCreateTime)
        );
    }

    @Override
    public Information getImportantestInfo(InfoListParam param) {
        String otherAppId = MdcUtil.getOtherAppId();
        List<Information> list = informationMapper.selectList(Wrappers.<Information>lambdaQuery()
                .and(i -> i.eq(StrUtil.isNotBlank(otherAppId), Information::getAppId, otherAppId)
                        .or().eq(Information::getAppId, MdcConstant.UNIVERSAL_APP_ID)
                )
                .eq(Information::getInfoType, param.getInfoType())
                .eq(Information::getState, StateEnum.NORMAL)
                .orderByDesc(Information::getImportance, Information::getCreateTime)
                .last("limit 1")
        );
        if (list.size() == 0) {
            throw new ServiceException("资讯不存在");
        }
        return list.get(0);
    }
}
