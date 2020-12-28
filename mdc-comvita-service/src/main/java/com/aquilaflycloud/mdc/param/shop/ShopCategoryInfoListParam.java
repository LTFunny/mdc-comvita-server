package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.shop.ShopCategoryInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ShopCategoryInfoListParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ShopCategoryInfoListParam extends PageAuthParam<ShopCategoryInfo> implements Serializable {
}
