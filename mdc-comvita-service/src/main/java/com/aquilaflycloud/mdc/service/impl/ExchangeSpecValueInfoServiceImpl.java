package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aquilaflycloud.mdc.mapper.ExchangeSpecValueInfoMapper;
import com.aquilaflycloud.mdc.model.exchange.ExchangeSpecValueInfo;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueAddParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValueGetParam;
import com.aquilaflycloud.mdc.param.exchange.ExchangeSpecValuePageParam;
import com.aquilaflycloud.mdc.service.ExchangeSpecValueInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ExchangeSpecValueInfoServiceImpl
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@Slf4j
@Service
public class ExchangeSpecValueInfoServiceImpl implements ExchangeSpecValueInfoService {
    @Resource
    private ExchangeSpecValueInfoMapper exchangeSpecValueInfoMapper;

    @Override
    public IPage<ExchangeSpecValueInfo> page(ExchangeSpecValuePageParam param) {
        return exchangeSpecValueInfoMapper.selectPage(param.page(), Wrappers.<ExchangeSpecValueInfo> lambdaQuery()
                .eq(ExchangeSpecValueInfo::getType, param.getType())
                .eq(ObjectUtil.isNotNull(param.getPId()), ExchangeSpecValueInfo::getPId, param.getPId())
                .orderByDesc(ExchangeSpecValueInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public ExchangeSpecValueInfo add(ExchangeSpecValueAddParam param) {
        //判断名称
        List<ExchangeSpecValueInfo> infos = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                .eq(ExchangeSpecValueInfo::getType, param.getType())
                .eq(ExchangeSpecValueInfo::getName, param.getName())
        );

        if (null != infos && infos.size() > 0) {
            throw new ServiceException("名称重复，请重试");
        }

        Long id = MdcUtil.getSnowflakeId();
        ExchangeSpecValueInfo info = new ExchangeSpecValueInfo();
        BeanUtil.copyProperties(param, info);
        info.setId(id);

        int count = exchangeSpecValueInfoMapper.insert(info);
        if (count <= 0) {
            throw new ServiceException("保存失败，请重试");
        }

        return info;
    }

    @Override
    public ExchangeSpecValueInfo get(ExchangeSpecValueGetParam param) {
        List<ExchangeSpecValueInfo> infos = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                .eq(ExchangeSpecValueInfo::getType, param.getType())
                .eq(ExchangeSpecValueInfo::getName, param.getName())
                .eq(ObjectUtil.isNotNull(param.getPId()), ExchangeSpecValueInfo::getPId, param.getPId())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //存在一条记录，则直接返回数据
        if (null != infos && infos.size() == 1) {
            return infos.get(0);
        }

        //有两条相同类型和名称的数据
        if (null != infos && infos.size() > 1) {
            throw new ServiceException("名称重复，请重试");
        }

        //如果不存在数据，则先保存数据，然后返回保存的数据
        if (null == infos || infos.size() == 0) {
            Long id = MdcUtil.getSnowflakeId();
            ExchangeSpecValueInfo info = new ExchangeSpecValueInfo();
            BeanUtil.copyProperties(param, info);
            info.setId(id);

            int count = exchangeSpecValueInfoMapper.insert(info);
            if (count <= 0) {
                throw new ServiceException("保存失败，请重试");
            }

            return info;
        }

        return null;
    }
}
