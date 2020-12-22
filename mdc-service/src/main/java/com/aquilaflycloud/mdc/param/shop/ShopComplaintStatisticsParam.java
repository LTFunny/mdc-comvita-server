package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ShopComplaintStatisticsParam extends AuthParam implements Serializable {
    private static final long serialVersionUID = -3645018465352635859L;
}
