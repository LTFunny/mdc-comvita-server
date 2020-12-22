package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.author.AuthorSiteListParam;
import com.aquilaflycloud.mdc.result.author.AuthorSite;

import java.util.List;

/**
 * AuthorSiteService
 *
 * @author star
 * @date 2020-04-04
 */
public interface AuthorSiteService {
    List<AuthorSite> listAuthor(AuthorSiteListParam param);
}

