package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CodeAccountListParam extends PageParam {

    @ApiModelProperty(value = "小程序名称")
    private String nickName;

}
