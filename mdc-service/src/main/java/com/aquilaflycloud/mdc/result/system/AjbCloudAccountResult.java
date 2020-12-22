package com.aquilaflycloud.mdc.result.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AjbCloudAccountResult implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "密钥标识")
    private String secretId;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "企业编号")
    private String firmCode;

    @ApiModelProperty(value = "车场编号")
    private String parkCode;

    @ApiModelProperty(value = "密钥")
    private String secret;

    @ApiModelProperty(value = "商户标识")
    private String merchantId;

    @ApiModelProperty(value = "商户账号")
    private String tenantsAccount;

    @ApiModelProperty(value = "商户密码")
    private String tenantsPassword;

    @ApiModelProperty(value = "商户名称")
    private String tenantsName;

    @ApiModelProperty(value = "联系人名")
    private String linkMan;

    @ApiModelProperty(value = "联系方式")
    private String mobilePhone;

    @ApiModelProperty(value = "数据推送配置")
    private List<PostPathInfo> postPathList;

    @Data
    public static class PostPathInfo {
        @ApiModelProperty(value = "推送类型")
        private String type;

        @ApiModelProperty(value = "推送地址")
        private String postPath;

        @ApiModelProperty(value = "成功标识")
        private String sucFlag;
    }
}
