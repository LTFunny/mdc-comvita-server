package com.aquilaflycloud.mdc.result.folksonomy;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyCatalog;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * FolksonomyCatalogNode
 *
 * @author star
 * @date 2021/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FolksonomyCatalogNode extends FolksonomyCatalog {
    @ApiModelProperty(value = "子目录")
    private List<FolksonomyCatalogNode> children;

    @ApiModelProperty(value = "标签列表")
    private List<FolksonomyInfo> folksonomyList;
}
