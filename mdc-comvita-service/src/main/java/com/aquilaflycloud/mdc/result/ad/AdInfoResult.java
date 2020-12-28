package com.aquilaflycloud.mdc.result.ad;

import com.aquilaflycloud.mdc.model.ad.AdInfo;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * AdInfoResult
 *
 * @author star
 * @date 2019-11-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdInfoResult extends AdInfo {
    @ApiModelProperty(value = "展示数")
    private Long showNum;

    @ApiModelProperty(value = "标签信息列表")
    private List<FolksonomyInfo> folksonomyInfoList;
}
