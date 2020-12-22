package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import com.aquilaflycloud.mdc.enums.system.AccountTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AccountListParam
 *
 * @author star
 * @date 2019-12-04
 */
@Data
@Accessors(chain = true)
public class AccountListParam {
    @ApiModelProperty(value = "第三方系统账号类型(system.AccountTypeEnum)")
    private AccountTypeEnum accountType;

    @ApiModelProperty(value = "第三方系统账号子类型(system.AccountSubTypeEnum)")
    private AccountSubTypeEnum accountSubType;
}
