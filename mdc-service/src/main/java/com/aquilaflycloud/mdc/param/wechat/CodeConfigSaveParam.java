package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Accessors(chain = true)
public class CodeConfigSaveParam {

    @ApiModelProperty(value="模板配置id")
    private Long id;

    /**
     * 模板配置名称
     */
    @ApiModelProperty(value="模板配置名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String templateConfigName;

    /**
     * 代码模板id
     */
    @ApiModelProperty(value="代码模板id", required = true)
    @NotNull(message = "模板不能为空")
    private Integer templateId;

    /**
     * 来源小程序appId
     */
    @ApiModelProperty(value="来源小程序appId")
    private String sourceMiniProgramAppId;

    /**
     * 来源小程序名称
     */
    @ApiModelProperty(value="来源小程序名称")
    private String sourceMiniProgram;

    /**
     * 用户版本号
     */
    @ApiModelProperty(value="用户版本号")
    private String userVersion;

    /**
     * 用户描述
     */
    @ApiModelProperty(value="用户描述")
    private String userDesc;

    /**
     * 开发者
     */
    @ApiModelProperty(value="开发者")
    private String developer;

    /**
     * 模板创建时间
     */
    @ApiModelProperty(value="模板创建时间")
    private Date templateTime;

    /**
     * 小程序默认页面(生成体验码)
     */
    @ApiModelProperty(value="小程序默认页面(生成体验码)")
    private String defaultPage;

    /**
     * 小程序页面配置(多个以分号;区分)
     */
    @ApiModelProperty(value="小程序页面配置(多个以分号;区分)")
    private String pageConfig;

    /**
     * 小程序ext配置
     */
    @ApiModelProperty(value="小程序ext配置")
    private String extConfig;

    /**
     * 模板类型; LIANMENG:联萌小程序
     */
    @ApiModelProperty(value="模板类型; LIANMENG:联萌小程序")
    private String templateType;
}
