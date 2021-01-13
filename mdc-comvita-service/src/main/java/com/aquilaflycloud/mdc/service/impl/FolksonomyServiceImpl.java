package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.mapper.FolksonomyBusinessRelMapper;
import com.aquilaflycloud.mdc.mapper.FolksonomyCatalogMapper;
import com.aquilaflycloud.mdc.mapper.FolksonomyInfoMapper;
import com.aquilaflycloud.mdc.mapper.FolksonomyMemberRelMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyCatalog;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyMemberRel;
import com.aquilaflycloud.mdc.param.folksonomy.*;
import com.aquilaflycloud.mdc.result.folksonomy.FolksonomyCatalogNode;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FolksonomyServiceImpl
 *
 * @author star
 * @date 2019-11-28
 */
@Service
public class FolksonomyServiceImpl implements FolksonomyService {

    @Resource
    private FolksonomyInfoMapper folksonomyInfoMapper;

    @Resource
    private FolksonomyCatalogMapper folksonomyCatalogMapper;

    @Resource
    private FolksonomyBusinessRelMapper folksonomyBusinessRelMapper;

    @Resource
    private FolksonomyMemberRelMapper folksonomyMemberRelMapper;

    @Transactional
    @Override
    public void saveFolksonomyBusinessRel(BusinessTypeEnum businessType, Long businessId, List<Long> folksonomyIds) {
        //标签列表为空,表示不新增不删除
        if (folksonomyIds == null) {
            return;
        }
        List<Long> oldFolksonomyIds = folksonomyBusinessRelMapper.selectMaps(Wrappers.<FolksonomyBusinessRel>lambdaQuery()
                .select(FolksonomyBusinessRel::getFolksonomyId)
                .eq(FolksonomyBusinessRel::getBusinessType, businessType)
                .eq(FolksonomyBusinessRel::getBusinessId, businessId)).stream()
                .map(map -> Convert.toLong(map.get("folksonomy_id"))).collect(Collectors.toList());
        List<Long> del;
        List<Long> add;
        //原有标签为空,表示只新增标签
        if (oldFolksonomyIds.size() == 0) {
            del = new ArrayList<>();
            add = folksonomyIds;
        } else {
            folksonomyIds = CollUtil.distinct(folksonomyIds);
            List<Long> disjunction = (List<Long>) CollUtil.intersection(oldFolksonomyIds, folksonomyIds);
            del = (List<Long>) CollUtil.disjunction(oldFolksonomyIds, disjunction);
            add = (List<Long>) CollUtil.disjunction(folksonomyIds, disjunction);
        }
        if (del.size() > 0) {
            folksonomyBusinessRelMapper.delete(Wrappers.<FolksonomyBusinessRel>lambdaQuery()
                    .in(FolksonomyBusinessRel::getFolksonomyId, del));
        }
        if (add.size() > 0) {
            List<FolksonomyInfo> folksonomyList = folksonomyInfoMapper.selectList(Wrappers.<FolksonomyInfo>lambdaQuery()
                    .in(FolksonomyInfo::getId, add));
            List<FolksonomyBusinessRel> relList = new ArrayList<>();
            for (FolksonomyInfo folksonomy : folksonomyList) {
                FolksonomyBusinessRel rel = new FolksonomyBusinessRel();
                rel.setBusinessType(businessType);
                rel.setBusinessId(businessId);
                rel.setFolksonomyId(folksonomy.getId());
                rel.setType(folksonomy.getType());
                rel.setCatalogId(folksonomy.getCatalogId());
                rel.setCatalogName(folksonomy.getCatalogName());
                rel.setWeight(1);
                relList.add(rel);
            }
            if (relList.size() > 0) {
                folksonomyBusinessRelMapper.insertAllBatch(relList);
            }
        }
    }

    @Override
    public List<FolksonomyInfo> getFolksonomyBusinessList(BusinessTypeEnum businessType, Long businessId) {
        List<Long> folksonomyIds = folksonomyBusinessRelMapper.selectMaps(Wrappers.<FolksonomyBusinessRel>lambdaQuery()
                .select(FolksonomyBusinessRel::getFolksonomyId)
                .eq(FolksonomyBusinessRel::getBusinessType, businessType)
                .eq(FolksonomyBusinessRel::getBusinessId, businessId)).stream()
                .map(map -> Convert.toLong(map.get("folksonomy_id"))).collect(Collectors.toList());
        if (folksonomyIds.size() > 0) {
            return folksonomyInfoMapper.selectList(Wrappers.<FolksonomyInfo>lambdaQuery()
                    .in(FolksonomyInfo::getId, folksonomyIds));
        }
        return null;
    }

    @Override
    public List<FolksonomyInfo> listMemberRelFolksonomy() {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return getFolksonomyMemberList(FolksonomyTypeEnum.MEMBER, memberId);
    }

    @Override
    public List<FolksonomyInfo> listMemberRelFolksonomy(FolksonomyTypeEnum folksonomyType, Long memberId) {
        return getFolksonomyMemberList(folksonomyType, memberId);
    }

    private List<FolksonomyInfo> getFolksonomyMemberList(FolksonomyTypeEnum folksonomyType, Long memberId) {
        List<Long> folksonomyIds = folksonomyMemberRelMapper.selectMaps(Wrappers.<FolksonomyMemberRel>lambdaQuery()
                .select(FolksonomyMemberRel::getFolksonomyId)
                .eq(folksonomyType != null, FolksonomyMemberRel::getType, folksonomyType)
                .eq(FolksonomyMemberRel::getMemberId, memberId)).stream()
                .map(map -> Convert.toLong(map.get("folksonomy_id"))).collect(Collectors.toList());
        if (folksonomyIds.size() > 0) {
            return folksonomyInfoMapper.selectList(Wrappers.<FolksonomyInfo>lambdaQuery()
                    .in(FolksonomyInfo::getId, folksonomyIds));
        }
        return null;
    }

    @Override
    public void saveFolksonomyMemberRel(Long businessId) {
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId == null) {
            return;
        }
        List<FolksonomyMemberRel> list = folksonomyBusinessRelMapper.selectList(Wrappers.<FolksonomyBusinessRel>lambdaQuery()
                .eq(FolksonomyBusinessRel::getBusinessId, businessId))
                .stream().map((rel) -> {
                    FolksonomyMemberRel memberRel = new FolksonomyMemberRel();
                    memberRel.setBusinessId(rel.getBusinessId());
                    memberRel.setBusinessType(rel.getBusinessType());
                    memberRel.setCatalogId(rel.getCatalogId());
                    memberRel.setCatalogName(rel.getCatalogName());
                    memberRel.setFolksonomyId(rel.getFolksonomyId());
                    memberRel.setType(rel.getType());
                    memberRel.setWeight(rel.getWeight());
                    memberRel.setMemberId(memberId);
                    return memberRel;
                }).collect(Collectors.toList());
        if (list.size() > 0) {
            folksonomyMemberRelMapper.insertAllBatch(list);
        }
    }

    @Override
    public void addFolksonomyMemberRel(FolksonomyGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        addFolksonomyMemberRel(param.getId(), memberId, FolksonomyTypeEnum.MEMBER);
    }

    private void addFolksonomyMemberRel(Long folksonomyId, Long memberId, FolksonomyTypeEnum folksonomyType) {
        int count = folksonomyMemberRelMapper.selectCount(Wrappers.<FolksonomyMemberRel>lambdaQuery()
                .eq(FolksonomyMemberRel::getFolksonomyId, folksonomyId)
                .eq(FolksonomyMemberRel::getMemberId, memberId)
        );
        if (count <= 0) {
            FolksonomyCatalog catalog = folksonomyCatalogMapper.selectOne(Wrappers.<FolksonomyCatalog>lambdaQuery()
                    .eq(FolksonomyCatalog::getType, FolksonomyTypeEnum.MEMBER));
            if (catalog == null) {
                throw new ServiceException("标签目录不存在");
            }
            FolksonomyMemberRel memberRel = new FolksonomyMemberRel();
            memberRel.setCatalogId(catalog.getId());
            memberRel.setCatalogName(catalog.getName());
            memberRel.setFolksonomyId(folksonomyId);
            memberRel.setType(folksonomyType);
            memberRel.setWeight(1);
            memberRel.setMemberId(memberId);
            count = folksonomyMemberRelMapper.insert(memberRel);
            if (count <= 0) {
                throw new ServiceException("关联标签失败");
            }
        }
    }

    @Override
    public void deleteFolksonomyMemberRel(FolksonomyGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        deleteFolksonomyMemberRel(param.getId(), memberId);
    }

    private void deleteFolksonomyMemberRel(Long folksonomyId, Long memberId) {
        int count = folksonomyMemberRelMapper.delete(Wrappers.<FolksonomyMemberRel>lambdaQuery()
                .eq(FolksonomyMemberRel::getFolksonomyId, folksonomyId)
                .eq(FolksonomyMemberRel::getMemberId, memberId)
        );
        if (count <= 0) {
            throw new ServiceException("删除标签失败");
        }
    }

    private List<FolksonomyInfo> listFolksonomy(FolksonomyListParam param, FolksonomyTypeEnum folksonomyType) {
        return folksonomyInfoMapper.selectList(Wrappers.<FolksonomyInfo>lambdaQuery()
                .eq(FolksonomyInfo::getType, folksonomyType)
                .like(StrUtil.isNotBlank(param.getName()), FolksonomyInfo::getName, param.getName())
                .orderByDesc(FolksonomyInfo::getCreateTime));
    }

    private IPage<FolksonomyInfo> pageFolksonomy(FolksonomyPageParam param, FolksonomyTypeEnum folksonomyType) {
        return folksonomyInfoMapper.selectPage(param.page(), Wrappers.<FolksonomyInfo>lambdaQuery()
                .eq(FolksonomyInfo::getType, folksonomyType)
                .like(StrUtil.isNotBlank(param.getName()), FolksonomyInfo::getName, param.getName())
                .orderByDesc(FolksonomyInfo::getCreateTime));
    }

    private Long addFolksonomy(FolksonomyAddParam param, FolksonomyTypeEnum folksonomyType) {
        int existCount = folksonomyInfoMapper.selectCount(Wrappers.<FolksonomyInfo>lambdaQuery()
                .eq(FolksonomyInfo::getType, folksonomyType)
                .eq(FolksonomyInfo::getName, param.getName())
        );
        if (existCount > 0) {
            throw new ServiceException("该标签已存在");
        }
        FolksonomyCatalog catalog = folksonomyCatalogMapper.selectOne(Wrappers.<FolksonomyCatalog>lambdaQuery()
                .eq(FolksonomyCatalog::getType, folksonomyType)
                .eq(FolksonomyCatalog::getId, param.getCatalogId())
        );
        if (catalog == null) {
            throw new ServiceException("标签目录不存在");
        }
        FolksonomyInfo info = new FolksonomyInfo();
        BeanUtil.copyProperties(param, info);
        info.setType(catalog.getType());
        info.setCatalogId(catalog.getId());
        info.setCatalogName(catalog.getName());
        int count = folksonomyInfoMapper.insert(info);
        if (count <= 0) {
            throw new ServiceException("新增标签失败");
        }
        return info.getId();
    }

    private void editFolksonomy(FolksonomyEditParam param, FolksonomyTypeEnum folksonomyType) {
        int existCount = folksonomyInfoMapper.selectCount(Wrappers.<FolksonomyInfo>lambdaQuery()
                .eq(FolksonomyInfo::getType, folksonomyType)
                .eq(FolksonomyInfo::getName, param.getName())
                .ne(FolksonomyInfo::getId, param.getId())
        );
        if (existCount > 0) {
            throw new ServiceException("该功能标签已存在");
        }
        if (param.getCatalogId() != null) {
            FolksonomyInfo folksonomyInfo = folksonomyInfoMapper.selectById(param.getId());
            FolksonomyCatalog catalog = folksonomyCatalogMapper.selectById(param.getCatalogId());
            if (folksonomyInfo.getType() != catalog.getType()) {
                throw new ServiceException("不同类型的标签和目录不能切换");
            }
        }
        FolksonomyInfo info = new FolksonomyInfo();
        BeanUtil.copyProperties(param, info);
        int count = folksonomyInfoMapper.updateById(info);
        if (count <= 0) {
            throw new ServiceException("编辑功能标签失败");
        }
    }

    @Override
    public List<FolksonomyInfo> listBusinessFolksonomy(FolksonomyListParam param) {
        return listFolksonomy(param, FolksonomyTypeEnum.BUSINESS);
    }

    @Override
    public IPage<FolksonomyInfo> pageBusinessFolksonomy(FolksonomyPageParam param) {
        return pageFolksonomy(param, FolksonomyTypeEnum.BUSINESS);
    }

    @Override
    public BaseResult<Long> addFolksonomyCatalog(FolksonomyCatalogAddParam param) {
        FolksonomyCatalog catalogParent = folksonomyCatalogMapper.selectById(param.getPid());
        FolksonomyCatalog catalog = BeanUtil.copyProperties(param, FolksonomyCatalog.class);
        catalog.setType(catalogParent.getType());
        int count = folksonomyCatalogMapper.insert(catalog);
        if (count <= 0) {
            throw new ServiceException("新增标签目录失败");
        }
        return BaseResult.buildResult(catalog.getId());
    }

    @Override
    public void editFolksonomyCatalog(FolksonomyCatalogEditParam param) {
        FolksonomyCatalog catalog = folksonomyCatalogMapper.selectById(param.getId());
        if (param.getPid() != null) {
            FolksonomyCatalog catalogParent = folksonomyCatalogMapper.selectById(param.getPid());
            if (catalogParent.getType() != catalog.getType()) {
                throw new ServiceException("不同类型的标签目录不能切换");
            }
        }
        FolksonomyCatalog update = BeanUtil.copyProperties(param, FolksonomyCatalog.class);
        int count = folksonomyCatalogMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑标签目录失败");
        }
    }

    @Override
    public void deleteFolksonomyCatalog(FolksonomyCatalogDeleteParam param) {
        if (!param.getEnforceDelete()) {
            int canDelete = folksonomyCatalogMapper.selectCount(Wrappers.<FolksonomyCatalog>lambdaQuery()
                    .eq(FolksonomyCatalog::getPid, param.getId())
            );
            if (canDelete <= 0) {
                canDelete = folksonomyInfoMapper.selectCount(Wrappers.<FolksonomyInfo>lambdaQuery()
                        .eq(FolksonomyInfo::getCatalogId, param.getId())
                );
            }
            if (canDelete > 0) {
                throw new ServiceException("此目录下存在其他目录或标签");
            }
        }
        int count = folksonomyCatalogMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除标签目录失败");
        }
    }

    @Override
    public FolksonomyCatalog getFolksonomyCatalog(FolksonomyCatalogGetParam param) {
        FolksonomyCatalog catalog = folksonomyCatalogMapper.selectById(param.getId());
        if (catalog == null) {
            throw new ServiceException("标签目录不存在");
        }
        return catalog;
    }

    @Override
    public List<FolksonomyCatalogNode> listFolksonomyTree(FolksonomyCatalogListParam param) {
        List<FolksonomyCatalog> catalogList = folksonomyCatalogMapper.selectList(Wrappers.<FolksonomyCatalog>lambdaQuery()
                .eq(param.getType() != null, FolksonomyCatalog::getType, param.getType())
        );
        if (CollUtil.isNotEmpty(catalogList)) {
            Map<Long, List<FolksonomyInfo>> folksonomyMap = folksonomyInfoMapper.selectList(Wrappers.<FolksonomyInfo>lambdaQuery()
                    .in(FolksonomyInfo::getCatalogId, catalogList.stream().map(FolksonomyCatalog::getId).collect(Collectors.toList()))
            ).stream().collect(Collectors.groupingBy(FolksonomyInfo::getCatalogId));
            return catalogList.stream().filter(catalog -> catalog.getPid() == 0)
                    .map(catalog -> covert(catalog, catalogList, folksonomyMap))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private FolksonomyCatalogNode covert(FolksonomyCatalog catalog, List<FolksonomyCatalog> catalogList, Map<Long, List<FolksonomyInfo>> folksonomyMap) {
        FolksonomyCatalogNode node = BeanUtil.copyProperties(catalog, FolksonomyCatalogNode.class);
        List<FolksonomyCatalogNode> children = catalogList.stream()
                .filter(child -> child.getPid().equals(catalog.getId()))
                .map(child -> covert(child, catalogList, folksonomyMap)).collect(Collectors.toList());
        node.setChildren(children);
        node.setFolksonomyList(folksonomyMap.get(catalog.getId()));
        return node;
    }

    @Override
    public BaseResult<Long> addBusinessFolksonomy(FolksonomyAddParam param) {
        Long id = addFolksonomy(param, FolksonomyTypeEnum.BUSINESS);
        return BaseResult.buildResult(id);
    }

    @Override
    public void editBusinessFolksonomy(FolksonomyEditParam param) {
        editFolksonomy(param, FolksonomyTypeEnum.BUSINESS);
    }

    @Override
    public FolksonomyInfo getFolksonomy(FolksonomyGetParam param) {
        return folksonomyInfoMapper.selectById(param.getId());
    }

    @Override
    public void deleteFolksonomy(FolksonomyGetParam param) {
        int count = folksonomyInfoMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除标签失败");
        }
        //删除标签关联的业务关联表和会员关联表
        folksonomyBusinessRelMapper.delete(Wrappers.<FolksonomyBusinessRel>lambdaQuery()
                .eq(FolksonomyBusinessRel::getFolksonomyId, param.getId()));
        folksonomyMemberRelMapper.delete(Wrappers.<FolksonomyMemberRel>lambdaQuery()
                .eq(FolksonomyMemberRel::getFolksonomyId, param.getId()));
    }

    @Override
    public void addFolksonomyMemberRel(FolksonomyMemberRelParam param) {
        addFolksonomyMemberRel(param.getId(), param.getMemberId(), FolksonomyTypeEnum.BUSINESS);
    }

    @Override
    public void deleteFolksonomyMemberRel(FolksonomyMemberRelParam param) {
        deleteFolksonomyMemberRel(param.getId(), param.getMemberId());
    }

    @Override
    public IPage<FolksonomyInfo> pageMemberFolksonomy(FolksonomyPageParam param) {
        return pageFolksonomy(param, FolksonomyTypeEnum.MEMBER);
    }

    @Override
    public BaseResult<Long> addMemberFolksonomy(FolksonomyAddParam param) {
        Long id = addFolksonomy(param, FolksonomyTypeEnum.MEMBER);
        return BaseResult.buildResult(id);
    }

    @Override
    public void editMemberFolksonomy(FolksonomyEditParam param) {
        editFolksonomy(param, FolksonomyTypeEnum.MEMBER);
    }

    @Override
    public List<FolksonomyInfo> listMemberFolksonomy(FolksonomyListParam param) {
        return listFolksonomy(param, FolksonomyTypeEnum.MEMBER);
    }
}
