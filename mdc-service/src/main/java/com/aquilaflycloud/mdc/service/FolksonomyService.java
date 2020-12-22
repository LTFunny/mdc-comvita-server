package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.folksonomy.*;
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

    void saveFolksonomyMemberRel(Long businessId);

    void addFolksonomyMemberRel(FolksonomyGetParam param);

    void deleteFolksonomyMemberRel(FolksonomyGetParam param);

    void initDataFolksonomy();

    List<FolksonomyInfo> listBusinessFolksonomy(FolksonomyListParam param);

    IPage<FolksonomyInfo> pageBusinessFolksonomy(FolksonomyPageParam param);

    BaseResult<Long> addBusinessFolksonomy(FolksonomyAddParam param);

    void editBusinessFolksonomy(FolksonomyEditParam param);

    FolksonomyInfo getFolksonomy(FolksonomyGetParam param);

    void deleteFolksonomy(FolksonomyGetParam param);

    IPage<FolksonomyInfo> pageMemberFolksonomy(FolksonomyPageParam param);

    BaseResult<Long> addMemberFolksonomy(FolksonomyAddParam param);

    void editMemberFolksonomy(FolksonomyEditParam param);

    List<FolksonomyInfo> listMemberFolksonomy(FolksonomyListParam param);
}

