package com.aquilaflycloud.mdc.model.ticket;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 第三方接口(道控)返回记录表
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-29
 */
@Data
@TableName(value = "ticket_interface_return_record")
public class TicketInterfaceReturnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "是否正确(true|false)")
    @TableField("is_true")
    private String isTrue;

    @ApiModelProperty(value = "返回的状态码(200:成功;201:操作失败;202:参数错误;203:签名错误;204:合作协议不存在;205:产品协议不存在(请检查产品 ID 是否正确,或在有效期内); 206:财务账户不存在(请联系景区进行开通);207:订单号重复;208:产品不符合售卖规则;209:订单已支付;210:订单号不存在;211:凭证码不存在;212:当前门票状态不可退;213:产品不符合退款规则;214:订单不可退(未支付);215:账户余额不足;216:退款申请成功(请等待商户审核);999:系统错误;)")
    @TableField("result_code")
    private Integer resultCode;

    @ApiModelProperty(value = "返回的提示信息")
    @TableField("result_msg")
    private String resultMsg;

    @ApiModelProperty(value = "返回的数据json串")
    @TableField("result_json")
    private String resultJson;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "请求url")
    @TableField("url")
    private String url;

    public TicketInterfaceReturnRecord(String isTrue, Integer resultCode, String resultMsg, String resultJson, String url) {
        this.isTrue = isTrue;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultJson = resultJson;
        this.url = url;
    }

    public TicketInterfaceReturnRecord() {
    }
}
