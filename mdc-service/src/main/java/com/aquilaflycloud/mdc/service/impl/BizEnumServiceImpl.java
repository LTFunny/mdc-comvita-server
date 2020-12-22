package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.aquilaflycloud.dataAuth.bean.DataAuthParam;
import com.aquilaflycloud.dataAuth.bean.EnumProperty;
import com.aquilaflycloud.dataAuth.util.DataAuthUtil;
import com.aquilaflycloud.mdc.mapper.EnumBusinessTypeMapper;
import com.aquilaflycloud.mdc.model.system.EnumBusinessType;
import com.aquilaflycloud.mdc.param.system.DictListParam;
import com.aquilaflycloud.mdc.param.system.DictNameParam;
import com.aquilaflycloud.mdc.service.BizEnumService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BizEnumServiceImpl
 *
 * @author star
 * @date 2019-11-16
 */
@Slf4j
@Service
public class BizEnumServiceImpl implements BizEnumService {

    @Resource
    private EnumBusinessTypeMapper enumBusinessTypeMapper;

    @PostConstruct
    @Transactional
    public void reloadEnumType() {
        List<String> nameList = enumBusinessTypeMapper.selectList(Wrappers.<EnumBusinessType>lambdaQuery()
                .select(EnumBusinessType::getEnumPackage)
                .groupBy(EnumBusinessType::getEnumPackage)
        ).stream().map(EnumBusinessType::getEnumPackage).collect(Collectors.toList());
        enumBusinessTypeMapper.delete(null);
        for (String packageName : nameList) {
            Class clazz;
            try {
                clazz = ClassUtil.<Enum>loadClass("com.aquilaflycloud.mdc.enums." + packageName);
            } catch (NoClassDefFoundError | Exception e) {
                log.error("字典加载错误,{}不存在", packageName, e);
                continue;
            }
            Collection<Enum> enums = EnumUtil.getEnumMap(clazz).values();
            List<EnumProperty> enumList = JSONUtil.toList(JSONUtil.parseArray(JSON.toJSONString(enums)), EnumProperty.class);
            List<EnumBusinessType> list = new ArrayList<>();
            for (EnumProperty enumProperty : enumList) {
                EnumBusinessType businessType = new EnumBusinessType();
                businessType.setEnumPackage(packageName);
                businessType.setType(enumProperty.getType());
                businessType.setName(enumProperty.getName());
                list.add(businessType);
            }
            enumBusinessTypeMapper.insertAllBatch(list);
        }
    }

    private Enum handler(Class clazz, EnumBusinessType type) {
        return EnumUtil.likeValueOf(clazz, type.getType());
    }

    @Override
    public Collection<Enum> listEnums(DictListParam param) {
        Class<Enum> clazz;
        try {
            clazz = ClassUtil.loadClass("com.aquilaflycloud.mdc.enums." + param.getName());
        } catch (NoClassDefFoundError | Exception e) {
            throw new ServiceException("字典不存在");
        }
        Set<String> nameSet = RedisUtil.<String, String>hashRedis().keys("dictDataAuth");
        if (CollUtil.contains(nameSet, param.getName())) {
            Map<String, DataAuthParam> dataAuthMap = DataAuthUtil.getDataAuth(new String[]{param.getName()}, MdcUtil.getCurrentUserId());
            if (dataAuthMap.size() >= 1) {
                param.setDataAuthParam(dataAuthMap.entrySet().stream().findAny().get().getValue());
            }
            List<Enum> list = enumBusinessTypeMapper.selectList(Wrappers.<EnumBusinessType>lambdaQuery()
                    .eq(EnumBusinessType::getEnumPackage, param.getName())
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            ).stream().map(type -> handler(clazz, type)).collect(Collectors.toList());
            if (list.size() > 0) {
                return list;
            }
        }
        if (param.getParent() == null) {
            return EnumUtil.getEnumMap(clazz).values();
        } else {
            Object[] array = EnumUtil.getEnumMap(clazz).values().stream().filter(enums -> {
                String json = JSON.toJSONString(enums);
                JSONObject object = JSONUtil.parseObj(json);
                return ObjectUtil.equal(object.getInt("parent"), param.getParent());
            }).toArray();
            return Convert.toList(Enum.class, array);
        }
    }

    @Override
    public void addDataAuth(DictNameParam param) {
        for (String name : param.getNameList()) {
            Class clazz;
            try {
                clazz = ClassUtil.<Enum>loadClass("com.aquilaflycloud.mdc.enums." + name);
            } catch (NoClassDefFoundError | Exception e) {
                throw new ServiceException("字典不存在");
            }
            enumBusinessTypeMapper.delete(Wrappers.<EnumBusinessType>lambdaQuery()
                    .eq(EnumBusinessType::getEnumPackage, name)
            );
            Collection<Enum> enums = EnumUtil.getEnumMap(clazz).values();
            List<EnumProperty> enumList = JSONUtil.toList(JSONUtil.parseArray(JSON.toJSONString(enums)), EnumProperty.class);
            List<EnumBusinessType> list = new ArrayList<>();
            for (EnumProperty enumProperty : enumList) {
                EnumBusinessType businessType = new EnumBusinessType();
                businessType.setEnumPackage(name);
                businessType.setType(enumProperty.getType());
                businessType.setName(enumProperty.getName());
                list.add(businessType);
            }
            enumBusinessTypeMapper.insertAllBatch(list);
            RedisUtil.hashRedis().put("dictDataAuth", name, name);
        }
    }

    @Override
    public void deleteDataAuth(DictNameParam param) {
        enumBusinessTypeMapper.delete(Wrappers.<EnumBusinessType>lambdaQuery()
                .in(EnumBusinessType::getEnumPackage, param.getNameList())
        );
        RedisUtil.hashRedis().delete("dictDataAuth", param.getNameList().toArray());
    }
}
