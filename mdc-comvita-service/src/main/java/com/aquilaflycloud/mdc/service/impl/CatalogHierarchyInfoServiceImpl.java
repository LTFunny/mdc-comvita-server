package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyEnableEnum;
import com.aquilaflycloud.mdc.mapper.CatalogHierarchyInfoMapper;
import com.aquilaflycloud.mdc.model.catalog.CatalogHierarchyInfo;
import com.aquilaflycloud.mdc.param.catalog.*;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyNodeInfo;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyPageInfo;
import com.aquilaflycloud.mdc.service.CatalogHierarchyInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CatalogHierarchyInfoServiceImpl
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Service
public class CatalogHierarchyInfoServiceImpl implements CatalogHierarchyInfoService {
    @Resource
    private CatalogHierarchyInfoMapper catalogHierarchyInfoMapper;

    @Override
    public void addCatalogHierarchyInfo(CatalogHierarchyInfoAddParam param) {
        // 判断对应父类下名称是否存在
        List<CatalogHierarchyInfo> catalogHierarchyInfos = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                .eq(CatalogHierarchyInfo::getName, param.getName())
                .eq(CatalogHierarchyInfo::getType, param.getType())
                .eq(CatalogHierarchyInfo::getPId, param.getPId())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (null != catalogHierarchyInfos && catalogHierarchyInfos.size() > 0) {
            throw new ServiceException("名称已存在");
        }

        CatalogHierarchyInfo info = new CatalogHierarchyInfo();
        BeanUtil.copyProperties(param, info);
        //获取路径
        String path = "-0-";
        int level = 1;
        if (param.getPId() != 0) {
            // 判断id是否存在
            CatalogHierarchyInfo pInfo = catalogHierarchyInfoMapper.selectOne(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                    .eq(CatalogHierarchyInfo::getId, param.getPId())
            );

            if (null == pInfo) {
                throw new ServiceException("父分类不存在，请重试");
            }

            path = pInfo.getPath() + pInfo.getId() + "-";
            level = pInfo.getLevel() + 1;
        }

        info.setPath(path);
        info.setLevel(level);
        info.setEnable(CatalogHierarchyEnableEnum.YES);

        int count = catalogHierarchyInfoMapper.insert(info);

        if (count < 1) {
            throw new ServiceException("添加分类失败");
        }
    }

    @Override
    public void editCatalogHierarchyInfo(CatalogHierarchyInfoEditParam param) {
        //获取分类信息
        CatalogHierarchyInfo info = catalogHierarchyInfoMapper.selectById(param.getId());
        if (null == info) {
            throw new ServiceException("查询不到该分类信息");
        }

        //禁用启用当前子节点是否符合操作
        if (ObjectUtil.isNotNull(param.getEnable())) {
            List<CatalogHierarchyInfo> existList = null;

            if (ObjectUtil.equal(CatalogHierarchyEnableEnum.NO, param.getEnable())) {
                //查询禁用节点的子节点是否启用
                existList = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo> lambdaQuery()
                        .eq(CatalogHierarchyInfo::getEnable, CatalogHierarchyEnableEnum.YES)
                        .eq(CatalogHierarchyInfo::getPId, param.getId())
                );
            } else if (ObjectUtil.equal(CatalogHierarchyEnableEnum.YES, param.getEnable())) {
                //查询启用节点的父节点是否禁用
                existList = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo> lambdaQuery()
                        .eq(CatalogHierarchyInfo::getEnable, CatalogHierarchyEnableEnum.NO)
                        .eq(CatalogHierarchyInfo::getId, info.getPId())
                );
            }

            if (null != existList && existList.size() > 0) {
                throw new ServiceException(ObjectUtil.equal(CatalogHierarchyEnableEnum.NO, param.getEnable())?"该分类的子分类全部禁用才能操作" : "该分类的父分类启用才能操作");
            }
        }

        List<CatalogHierarchyInfo> infos = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                .eq(CatalogHierarchyInfo::getName, param.getName())
                .eq(CatalogHierarchyInfo::getType, info.getType())
                .eq(CatalogHierarchyInfo::getPId, info.getPId())
                .ne(CatalogHierarchyInfo::getId, param.getId())
        );

        if (null != infos && infos.size() > 0) {
            throw new ServiceException("名称已存在");
        }

        CatalogHierarchyInfo item = new CatalogHierarchyInfo();
        BeanUtil.copyProperties(param, item);
        int count = catalogHierarchyInfoMapper.updateById(item);

        if (count < 1) {
            throw new ServiceException("编辑分类失败");
        }
    }

    @Override
    public void deleteCatalogHierarchyInfo(CatalogHierarchyInfoDeleteParam param) {
        //判断是否是最下级分类
        List<CatalogHierarchyInfo> infos = catalogHierarchyInfoMapper.selectList((Wrappers.<CatalogHierarchyInfo>lambdaQuery())
                .eq(CatalogHierarchyInfo::getPId, param.getId())
        );

        if (null != infos && infos.size() > 0) {
            throw new ServiceException("只允许对最下级分类进行删除");
        }

        int count = catalogHierarchyInfoMapper.deleteById(param.getId());
        if (count < 1) {
            throw new ServiceException("删除分类失败");
        }
    }

    @Override
    public List<CatalogHierarchyNodeInfo> listCatalogHierarchyInfo(CatalogHierarchyInfoListParam param) {
        List<CatalogHierarchyInfo> infos = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                .eq(CatalogHierarchyInfo::getType, param.getType())
                .eq(param.isFilterSign(), CatalogHierarchyInfo::getEnable, CatalogHierarchyEnableEnum.YES)
                .orderByAsc(CatalogHierarchyInfo::getLevel)
                .orderByDesc(CatalogHierarchyInfo::getSort)
        );

        return infos.stream().filter(item -> item.getPId() == 0).map(item -> cover(item, infos)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getCurrAndChildrenIds(List<Long> ids) {

        List<CatalogHierarchyInfo> infos = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                .in(CatalogHierarchyInfo::getId, ids)
        );

        LambdaQueryWrapper<CatalogHierarchyInfo> wrapper = Wrappers.<CatalogHierarchyInfo>lambdaQuery().in(CatalogHierarchyInfo::getId, ids);
        for (int i = 0; i < infos.size(); i++) {
            CatalogHierarchyInfo item = infos.get(i);
            wrapper.or().like(CatalogHierarchyInfo::getPath, item.getPath() + item.getId() + "%");
        }

        return catalogHierarchyInfoMapper.selectList(wrapper).stream().map(item -> item.getId()).collect(Collectors.toList());
    }

    @Override
    public IPage<CatalogHierarchyPageInfo> page(CatalogHierarchyInfoPageParam param) {
        LambdaQueryWrapper<CatalogHierarchyInfo> queryWrapper = Wrappers.<CatalogHierarchyInfo>lambdaQuery();

        if (ObjectUtil.isNotNull(param.getId())) {
            List<Long> currAndChildrenIds = getCurrAndChildrenIds(new ArrayList<>(Arrays.asList(param.getId())));
            queryWrapper.in(CatalogHierarchyInfo::getId, currAndChildrenIds);
        }

        if (ObjectUtil.isNotNull(param.getType())) {
            queryWrapper.eq(CatalogHierarchyInfo::getType, param.getType());
        }

        IPage resultPage = catalogHierarchyInfoMapper.selectPage(param.page(), queryWrapper.like(StrUtil.isNotBlank(param.getName()), CatalogHierarchyInfo::getName, param.getName())
                .orderByAsc(CatalogHierarchyInfo::getLevel)
                .orderByDesc(CatalogHierarchyInfo::getSort)
        );

        if (null == resultPage.getRecords() || resultPage.getRecords().size() == 0) {
            return resultPage;
        }

        List<CatalogHierarchyInfo> records = resultPage.getRecords();

        //获取父节点名称
        List<Long> pIds = records.stream().map(item -> item.getPId()).distinct().collect(Collectors.toList());
        Map<Long, String> parentMap = catalogHierarchyInfoMapper.selectList(Wrappers.<CatalogHierarchyInfo>lambdaQuery()
                .in(CatalogHierarchyInfo::getId, pIds)
        ).stream().collect(Collectors.toMap(CatalogHierarchyInfo::getId, CatalogHierarchyInfo::getName));

        List<CatalogHierarchyPageInfo> resultRecords = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            CatalogHierarchyInfo info = records.get(i);
            CatalogHierarchyPageInfo item = new CatalogHierarchyPageInfo();
            BeanUtil.copyProperties(info, item);


            if (0 == item.getPId()) {
                item.setParentName(item.getType().getName());
            } else {
                item.setParentName(parentMap.get(item.getPId()));
            }

            resultRecords.add(item);
        }

        resultPage.setRecords(resultRecords);

        return resultPage;
    }

    private CatalogHierarchyNodeInfo cover(CatalogHierarchyInfo item, List<CatalogHierarchyInfo> infos) {
        CatalogHierarchyNodeInfo info = new CatalogHierarchyNodeInfo();
        BeanUtil.copyProperties(item, info);
        List<CatalogHierarchyNodeInfo> children = infos.stream().filter(subItem -> subItem.getPId().equals(item.getId()))
                .map(subItem -> cover(subItem, infos))
                .collect(Collectors.toList());
        info.setChildren(children);

        return info;
    }
}
