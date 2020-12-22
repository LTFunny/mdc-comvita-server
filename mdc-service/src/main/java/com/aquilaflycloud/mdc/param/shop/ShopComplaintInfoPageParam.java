package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintStateEnum;
import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ShopComplaintInfoPageParam extends PageAuthParam<ShopComplaintInfo> implements Serializable {
    private static final long serialVersionUID = 964760282570096374L;

    @ApiModelProperty(value = "状态(shop.ShopComplaintStateEnum)")
    private ShopComplaintStateEnum state;

    @ApiModelProperty(value = "投诉开始时间")
    private Date startTime;

    @ApiModelProperty(value = "投诉结束时间")
    private Date endTime;

}
