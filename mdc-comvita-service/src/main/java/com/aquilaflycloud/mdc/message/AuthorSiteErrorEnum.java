package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * AuthorSiteErrorEnum
 *
 * @author star
 * @date 2019-10-30
 */
public enum AuthorSiteErrorEnum {
    /**
     * appId无效
     */
    AUTHOR_SITE_ERROR_10101("10101"),
    ;
    private ServiceErrorMeta errorMeta;

    AuthorSiteErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("comvita.author_site_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
