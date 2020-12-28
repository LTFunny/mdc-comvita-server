package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.SystemEnumMapper;
import com.aquilaflycloud.mdc.model.system.SystemEnum;
import com.aquilaflycloud.mdc.param.system.EnumAddParam;
import com.aquilaflycloud.mdc.param.system.EnumListParam;
import com.aquilaflycloud.mdc.service.SystemEnumService;
import com.aquilaflycloud.util.DynamicEnumUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * SystemEnumServiceImpl
 *
 * @author star
 * @date 2019-11-04
 */
@Service
public class SystemEnumServiceImpl implements SystemEnumService {
    @Resource
    private SystemEnumMapper systemEnumMapper;

    @Override
    public IPage<SystemEnum> page(EnumListParam param) {
        return systemEnumMapper.selectPage(param.page(), Wrappers.<SystemEnum>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), SystemEnum::getName, param.getName())
                .like(StrUtil.isNotBlank(param.getEnumName()), SystemEnum::getEnumName, param.getEnumName())
                .like(StrUtil.isNotBlank(param.getEnumPackage()), SystemEnum::getEnumPackage, param.getEnumPackage())
                .orderByDesc(SystemEnum::getEnumName, SystemEnum::getId)
        );
    }

    @Override
    public void add(EnumAddParam param) {
        SystemEnum systemEnum = new SystemEnum();
        BeanUtil.copyProperties(param, systemEnum);
        int count = systemEnumMapper.insert(systemEnum);
        if (count <= 0) {
            throw new ServiceException("新增系统枚举失败");
        }
    }

    @PostConstruct
    private void reload() {
        List<SystemEnum> list = systemEnumMapper.selectList(null);
        for (SystemEnum systemEnum : list) {
            Class clazz = ClassUtil.<Enum>loadClass("com.aquilaflycloud.mdc.enums." + systemEnum.getEnumPackage());
            DynamicEnumUtil.addEnum(clazz, systemEnum.getEnumName()
                    , new Class[]{int.class, String.class}, new Object[]{systemEnum.getType(), systemEnum.getName()});
        }
    }
}
