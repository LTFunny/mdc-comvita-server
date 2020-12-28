package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopStateEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopTenantTypeEnum;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * BaseInfoListParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopInfoListParam extends PageAuthParam<ShopInfo> implements Serializable {
    @ApiModelProperty(value = "商户状态")
    private ShopStateEnum state;

    @ApiModelProperty(value = "租赁类型")
    private ShopTenantTypeEnum tenantType;

    @ApiModelProperty(value = "商户简称")
    private String shopName;
}
