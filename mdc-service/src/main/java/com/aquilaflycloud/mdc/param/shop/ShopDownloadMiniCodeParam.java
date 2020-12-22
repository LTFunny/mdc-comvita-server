package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopStateEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopTenantTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ShopDownloadMiniCodeParam
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
@Accessors(chain = true)
public class ShopDownloadMiniCodeParam extends AuthParam implements Serializable {

    private static final long serialVersionUID = -2597059778994313243L;

    @ApiModelProperty(value = "商户状态")
    private ShopStateEnum state;

    @ApiModelProperty(value = "租赁类型")
    private ShopTenantTypeEnum tenantType;

    @ApiModelProperty(value = "商户简称")
    private String shopName;
}