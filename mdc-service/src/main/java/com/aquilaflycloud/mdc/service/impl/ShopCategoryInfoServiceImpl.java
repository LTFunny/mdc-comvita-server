package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aquilaflycloud.mdc.mapper.ShopCategoryInfoMapper;
import com.aquilaflycloud.mdc.model.shop.ShopCategoryInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.service.ShopCategoryInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SystemShopCategoryInfoServiceImpl
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Service
public class ShopCategoryInfoServiceImpl implements ShopCategoryInfoService {

    @Resource
    private ShopCategoryInfoMapper shopCategoryInfoMapper;


    @Override
    public IPage<ShopCategoryInfo> page(ShopCategoryInfoListParam param) {
        return shopCategoryInfoMapper.selectPage(param.page(), Wrappers.<ShopCategoryInfo>lambdaQuery()
                .orderByDesc(ShopCategoryInfo::getCatalogOrder, ShopCategoryInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public ShopCategoryInfo addShopCategoryInfo(ShopCategoryInfoAddParam param) {
        //判断名称是否重复
        List<ShopCategoryInfo> list = shopCategoryInfoMapper.selectList(Wrappers.<ShopCategoryInfo>lambdaQuery()
                .eq(ShopCategoryInfo::getName, param.getName())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (null != list && list.size() > 0) {
            throw new ServiceException("分类名称不能重复，请重新输入");
        }
        ShopCategoryInfo info = new ShopCategoryInfo();
        BeanUtil.copyProperties(param, info);

        int count = shopCategoryInfoMapper.insert(info);

        if (count <= 0) {
            throw new ServiceException("分类信息保存失败，请重试");
        }
        return info;
    }

    @Override
    public ShopCategoryInfo getShopCategoryInfo(ShopCategoryInfoGetParam param) {
        ShopCategoryInfo info = shopCategoryInfoMapper.selectById(param.getId());
        return info;
    }

    @Override
    public void editShopCategoryInfo(ShopCategoryInfoEditParam param) {
        //判断名称是否重复
        List<ShopCategoryInfo> list = shopCategoryInfoMapper.selectList(Wrappers.<ShopCategoryInfo>lambdaQuery()
                .ne(ShopCategoryInfo::getId, param.getId())
                .eq(ShopCategoryInfo::getName, param.getName())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (null != list && list.size() > 0) {
            throw new ServiceException("分类名称不能重复，请重新输入");
        }

        ShopCategoryInfo info = new ShopCategoryInfo();
        BeanUtil.copyProperties(param, info);
        int count = shopCategoryInfoMapper.updateById(info);

        if (count <= 0) {
            throw new ServiceException("分类信息保存失败，请重试");
        }
    }

    @Override
    public void deleteShopCategoryInfo(ShopCategoryInfoGetParam param) {
        int count = shopCategoryInfoMapper.deleteById(param.getId());

        if (count <= 0) {
            throw new ServiceException("删除分类信息失败，请重试");
        }
    }

    @Override
    public List<ShopCategoryInfo> listShopCategoryInfo(ShopCategoryInfoListApiParam param) {
        return shopCategoryInfoMapper.selectList(Wrappers.<ShopCategoryInfo> lambdaQuery()
                .orderByDesc(ShopCategoryInfo::getCatalogOrder, ShopCategoryInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }
}
