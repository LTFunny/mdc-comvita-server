package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyCatalog;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.folksonomy.*;
import com.aquilaflycloud.mdc.result.folksonomy.FolksonomyCatalogNode;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * FolksonomyService
 *
 * @author star
 * @date 2019-11-28
 */
public interface FolksonomyService {
    void saveFolksonomyBusinessRel(BusinessTypeEnum businessType, Long businessId, List<Long> folksonomyIds);

    List<FolksonomyInfo> getFolksonomyBusinessList(BusinessTypeEnum businessType, Long businessId);

    List<FolksonomyInfo> listMemberRelFolksonomy();

    List<FolksonomyInfo> listMemberRelFolksonomy(FolksonomyTypeEnum folksonomyType, Long memberId);

    void saveFolksonomyMemberRel(Long businessId);

    void addFolksonomyMemberRel(FolksonomyGetParam param);

    void deleteFolksonomyMemberRel(FolksonomyGetParam param);

    List<FolksonomyInfo> listBusinessFolksonomy(FolksonomyListParam param);

    IPage<FolksonomyInfo> pageBusinessFolksonomy(FolksonomyPageParam param);

    BaseResult<Long> addFolksonomyCatalog(FolksonomyCatalogAddParam param);

    void editFolksonomyCatalog(FolksonomyCatalogEditParam param);

    void deleteFolksonomyCatalog(FolksonomyCatalogDeleteParam param);

    FolksonomyCatalog getFolksonomyCatalog(FolksonomyCatalogGetParam param);

    List<FolksonomyCatalogNode> listFolksonomyCatalogTree(FolksonomyCatalogListParam param);

    BaseResult<Long> addBusinessFolksonomy(FolksonomyAddParam param);

    void editBusinessFolksonomy(FolksonomyEditParam param);

    FolksonomyInfo getFolksonomy(FolksonomyGetParam param);

    void deleteFolksonomy(FolksonomyGetParam param);

    IPage<FolksonomyInfo> pageMemberFolksonomy(FolksonomyPageParam param);

    BaseResult<Long> addMemberFolksonomy(FolksonomyAddParam param);

    void editMemberFolksonomy(FolksonomyEditParam param);

    List<FolksonomyInfo> listMemberFolksonomy(FolksonomyListParam param);
}

