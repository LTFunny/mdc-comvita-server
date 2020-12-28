package com.aquilaflycloud.mdc.param.alipay;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AlipayAuthorSitePageParam extends PageParam<AlipayAuthorSite> {
    @ApiModelProperty(value = "应用名")
    private String appName;

    @ApiModelProperty(value="授权类型(alipay.SiteSourceEnum)")
    private SiteSourceEnum source;

    @ApiModelProperty(value="是否第三方代理(common.WhetherEnum)")
    private WhetherEnum isAgent;
}
