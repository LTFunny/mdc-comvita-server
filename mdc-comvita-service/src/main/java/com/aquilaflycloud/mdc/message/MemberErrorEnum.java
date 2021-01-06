package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * MemberErrorEnum
 *
 * @author star
 * @date 2019-09-20
 */
public enum MemberErrorEnum {
    /**
     * 会员未登录
     */
    MEMBER_ERROR_10001("10001"),
    /**
     * 会员不存在
     */
    MEMBER_ERROR_10002("10002"),
    /**
     * 会员未授权
     */
    MEMBER_ERROR_10003("10003"),
    /**
     * 会员上传图片非人脸
     */
    MEMBER_ERROR_10004("10004"),
    /**
     * 会员手机号重复
     */
    MEMBER_ERROR_10005("10005"),
    ;
    private ServiceErrorMeta errorMeta;

    MemberErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("comvita.member_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
