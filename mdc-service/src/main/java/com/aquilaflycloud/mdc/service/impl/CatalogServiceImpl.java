package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.catalog.CatalogBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.catalog.CatalogTypeEnum;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.mapper.CatalogInfoMapper;
import com.aquilaflycloud.mdc.mapper.CatalogRelMapper;
import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.catalog.CatalogRel;
import com.aquilaflycloud.mdc.param.catalog.CatalogAddParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogEditParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogGetParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogPageParam;
import com.aquilaflycloud.mdc.result.catalog.CatalogResult;
import com.aquilaflycloud.mdc.service.CatalogService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CatalogServiceImpl
 *
 * @author star
 * @date 2020-03-08
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Resource
    private CatalogInfoMapper catalogInfoMapper;
    @Resource
    private CatalogRelMapper catalogRelMapper;

    private CatalogInfo initOtherCatalog(CatalogBusinessTypeEnum businessType) {
        Long tenantId = MdcUtil.getCurrentTenantId();
        return RedisUtil.syncValueGet("initCatalogLock" + tenantId, "initCatalog" + tenantId, () -> {
            CatalogInfo catalog = catalogInfoMapper.selectOne(Wrappers.<CatalogInfo>lambdaQuery()
                    .eq(CatalogInfo::getCatalogType, CatalogTypeEnum.OTHER)
                    .eq(CatalogInfo::getBusinessType, businessType)
                    .orderByAsc(CatalogInfo::getSubTenantId)
                    .last("limit 1")
            );
            if (catalog == null) {
                catalog = new CatalogInfo();
                catalog.setCatalogName("其他");
                catalog.setCatalogType(CatalogTypeEnum.OTHER);
                catalog.setBusinessType(businessType);
                catalog.setCatalogOrder(0);
                int count = catalogInfoMapper.insert(catalog);
                if (count > 0) {
                    return catalog;
                }
            }
            return catalog;
        });
    }

    private IPage<CatalogResult> pageCatalog(CatalogPageParam param, CatalogBusinessTypeEnum businessType) {
        initOtherCatalog(businessType);
        return catalogInfoMapper.selectPage(param.page(), Wrappers.<CatalogInfo>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getCatalogName()), CatalogInfo::getCatalogName, param.getCatalogName())
                .eq(CatalogInfo::getBusinessType, businessType)
                .eq(param.getState() != null, CatalogInfo::getState, param.getState())
                .orderByDesc(CatalogInfo::getCatalogOrder, CatalogInfo::getCreateTime)
        ).convert(catalog -> {
            CatalogResult result = new CatalogResult();
            BeanUtil.copyProperties(catalog, result);
            Integer count = catalogRelMapper.selectCount(Wrappers.<CatalogRel>lambdaQuery()
                    .eq(CatalogRel::getCatalogId, catalog.getId())
            );
            result.setBusinessCount(count);
            return result;
        });
    }

    private List<CatalogInfo> listCatalog(CatalogBusinessTypeEnum businessType) {
        return catalogInfoMapper.selectList(Wrappers.<CatalogInfo>lambdaQuery()
                .eq(CatalogInfo::getBusinessType, businessType)
                .eq(CatalogInfo::getState, StateEnum.NORMAL)
                .orderByDesc(CatalogInfo::getCatalogOrder, CatalogInfo::getCreateTime)
        );
    }

    private void addCatalog(CatalogAddParam param, CatalogBusinessTypeEnum businessType) {
        int flag = catalogInfoMapper.selectCount(Wrappers.<CatalogInfo>lambdaQuery()
                .eq(CatalogInfo::getBusinessType, businessType)
                .eq(CatalogInfo::getCatalogName, param.getCatalogName())
        );
        if (flag > 0) {
            throw new ServiceException("分类已存在");
        }
        CatalogInfo catalogInfo = new CatalogInfo();
        BeanUtil.copyProperties(param, catalogInfo);
        catalogInfo.setCatalogType(CatalogTypeEnum.NORMAL);
        catalogInfo.setBusinessType(businessType);
        int count = catalogInfoMapper.insert(catalogInfo);
        if (count <= 0) {
            throw new ServiceException("新增分类失败");
        }
    }

    private void editCatalog(CatalogEditParam param, CatalogBusinessTypeEnum businessType) {
        int flag = catalogInfoMapper.selectCount(Wrappers.<CatalogInfo>lambdaQuery()
                .eq(CatalogInfo::getBusinessType, businessType)
                .eq(CatalogInfo::getCatalogName, param.getCatalogName())
                .ne(CatalogInfo::getId, param.getId())
        );
        if (flag > 0) {
            throw new ServiceException("分类已存在");
        }
        CatalogInfo update = new CatalogInfo();
        BeanUtil.copyProperties(param, update);
        int count = catalogInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑分类失败");
        }
    }

    private void deleteCatalog(CatalogGetParam param, CatalogBusinessTypeEnum businessType) {
        CatalogInfo catalog = catalogInfoMapper.selectById(param.getId());
        if (catalog == null) {
            throw new ServiceException("分类不存在");
        }
        if (catalog.getCatalogType() == CatalogTypeEnum.OTHER) {
            throw new ServiceException("该分类不能删除");
        }
        int count = catalogInfoMapper.deleteById(param.getId());
        if (count > 0) {
            List<Long> list = catalogRelMapper.selectMaps(new QueryWrapper<CatalogRel>()
                    .select("business_id, count(1) as relCount")
                    .lambda()
                    .eq(CatalogRel::getBusinessType, businessType)
                    .eq(CatalogRel::getCatalogId, param.getId())
                    .groupBy(CatalogRel::getBusinessId)
                    .having("relCount = 1")
            ).stream().map(map -> Convert.toLong(map.get("business_id"))).collect(Collectors.toList());
            int relCount = catalogRelMapper.delete(Wrappers.<CatalogRel>lambdaQuery()
                    .eq(CatalogRel::getCatalogId, param.getId())
            );
            if (relCount > 0) {
                List<CatalogRel> relList = new ArrayList<>();
                CatalogInfo otherCatalog = initOtherCatalog(businessType);
                for (Long couponId : list) {
                    CatalogRel rel = new CatalogRel();
                    rel.setCatalogId(otherCatalog.getId());
                    rel.setBusinessType(businessType);
                    rel.setBusinessId(couponId);
                    relList.add(rel);
                }
                int addCount = catalogRelMapper.insertAllBatch(relList);
                if (addCount <= 0) {
                    throw new ServiceException("迁移分类失败");
                }
            }
        } else {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    public IPage<CatalogResult> pageCouponCatalog(CatalogPageParam param) {
        return pageCatalog(param, CatalogBusinessTypeEnum.COUPON);
    }

    @Override
    public List<CatalogInfo> listCouponCatalogInfo() {
        initOtherCatalog(CatalogBusinessTypeEnum.COUPON);
        return listCatalog(CatalogBusinessTypeEnum.COUPON);
    }

    @Override
    public void addCouponCatalog(CatalogAddParam param) {
        addCatalog(param, CatalogBusinessTypeEnum.COUPON);
    }

    @Override
    public void editCouponCatalog(CatalogEditParam param) {
        editCatalog(param, CatalogBusinessTypeEnum.COUPON);
    }

    @Transactional
    @Override
    public void deleteCouponCatalog(CatalogGetParam param) {
        deleteCatalog(param, CatalogBusinessTypeEnum.COUPON);
    }

    @Override
    public IPage<CatalogResult> pageExchangeCatalog(CatalogPageParam param) {
        return pageCatalog(param, CatalogBusinessTypeEnum.EXCHANGE);
    }

    @Override
    public List<CatalogInfo> listExchangeCatalogInfo() {
        initOtherCatalog(CatalogBusinessTypeEnum.EXCHANGE);
        return listCatalog(CatalogBusinessTypeEnum.EXCHANGE);
    }

    @Override
    public void addExchangeCatalog(CatalogAddParam param) {
        addCatalog(param, CatalogBusinessTypeEnum.EXCHANGE);
    }

    @Override
    public void editExchangeCatalog(CatalogEditParam param) {
        editCatalog(param, CatalogBusinessTypeEnum.EXCHANGE);
    }

    @Transactional
    @Override
    public void deleteExchangeCatalog(CatalogGetParam param) {
        deleteCatalog(param, CatalogBusinessTypeEnum.EXCHANGE);
    }

    @Override
    public void toggleCatalog(CatalogGetParam param) {
        CatalogInfo catalog = catalogInfoMapper.selectById(param.getId());
        if (catalog == null) {
            throw new ServiceException("分类不存在");
        }
        if (catalog.getCatalogType() == CatalogTypeEnum.OTHER) {
            throw new ServiceException("该分类不能停用");
        }
        CatalogInfo update = new CatalogInfo();
        update.setId(param.getId());
        update.setState(catalog.getState() == StateEnum.NORMAL ? StateEnum.DISABLE : StateEnum.NORMAL);
        int count = catalogInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public CatalogInfo getCatalog(CatalogGetParam param) {
        return catalogInfoMapper.selectById(param.getId());
    }

    @Override
    public List<CatalogInfo> listCouponCatalog() {
        return listCatalog(CatalogBusinessTypeEnum.COUPON);
    }

    @Override
    public List<CatalogInfo> listExchangeCatalog() {
        return listCatalog(CatalogBusinessTypeEnum.EXCHANGE);
    }
}
