package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * PreGoodsInfoResult
 *
 * @author star
 * @date 2021/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreGoodsInfoResult extends PreGoodsInfo {
    @ApiModelProperty(value = "标签信息列表")
    private List<FolksonomyInfo> folksonomyInfoList;
}
