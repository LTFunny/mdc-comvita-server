package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * AlipayShopCategoryInfoListParam
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
@Accessors(chain = true)
public class ShopCategoryInfoListApiParam extends AuthParam implements Serializable {
}
