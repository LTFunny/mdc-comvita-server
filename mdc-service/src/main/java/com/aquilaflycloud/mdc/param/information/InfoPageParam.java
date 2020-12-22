package com.aquilaflycloud.mdc.param.information;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.information.ImportanceEnum;
import com.aquilaflycloud.mdc.enums.information.InfoTypeEnum;
import com.aquilaflycloud.mdc.model.information.Information;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * InfoPageParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InfoPageParam extends PageParam<Information> {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "资讯类型(information.InfoTypeEnum)")
    private InfoTypeEnum infoType;

    @ApiModelProperty(value = "资讯标题")
    private String infoTitle;

    @ApiModelProperty(value = "重要等级(information.ImportanceEnum)")
    private ImportanceEnum importance;

    @ApiModelProperty(value = "状态(common.StateEnum)")
    private StateEnum state;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}


