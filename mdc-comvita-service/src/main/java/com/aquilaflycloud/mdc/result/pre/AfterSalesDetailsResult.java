package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class AfterSalesDetailsResult extends PreRefundOrderInfo {
   @ApiModelProperty(value = "订单明细")
    private List<PreOrderGoods> detailsList;

    @ApiModelProperty(value = "操作记录")
    private List<PreOrderOperateRecord> operationList ;
}
