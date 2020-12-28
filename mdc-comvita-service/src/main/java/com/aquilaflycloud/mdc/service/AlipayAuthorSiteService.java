package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.param.alipay.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * AlipayAuthorSiteService
 *
 * @author star
 * @date 2019-12-24
 */
public interface AlipayAuthorSiteService {
    IPage<AlipayAuthorSite> pageAuthor(AlipayAuthorSitePageParam param);

    BaseResult<String> getPreAuthUrl(AlipayPreAuthUrlGetParam param);

    void addAuthor(AlipayAuthorSiteAddParam param);

    void editAuthor(AlipayAuthorSiteEditParam param);

    void deleteAuthor(AlipayAuthorSiteGetParam param);

    void updatePublicInfo(AlipayAuthorSiteGetParam param);

    String getOauth2Url(AlipayOauth2UrlGetParam param);

    List<AlipayAuthorSite> listAuthor();
}

