package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * AuthorSiteErrorEnum
 *
 * @author star
 * @date 2019-10-30
 */
public enum ShopInfoErrorEnum {
    /**
     * appId无效
     */
    SHOP_INFO_SYNCHRONIZATION_ERROR_10801("10801"),
    ;
    private ServiceErrorMeta errorMeta;

    ShopInfoErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.shop_info_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
