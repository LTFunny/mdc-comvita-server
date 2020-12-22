package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.common.NoneBlank;
import com.gitee.sop.servercommon.param.validation.Group1;
import com.gitee.sop.servercommon.param.validation.Group2;
import com.gitee.sop.servercommon.param.validation.Group3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoneBlank(fieldNames = {"openId", "userId", "phoneNumber"}, message = "微信用户id, 支付宝用户id, 手机号不能同时为空", groups = Group1.class)
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "openId", notNullFieldName = "appId", message = "appId不能为空", groups = Group2.class),
        @AnotherFieldHasValue(fieldName = "userId", notNullFieldName = "appId", message = "appId不能为空", groups = Group3.class)
})
@Data
@Accessors(chain = true)
public class MemberLoginParam implements Serializable {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "微信openId")
    private String openId;

    @ApiModelProperty(value = "支付宝userId")
    private String userId;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;
}