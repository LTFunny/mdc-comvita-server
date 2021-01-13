package com.aquilaflycloud.mdc.result.folksonomy;

import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyNodeTypeEnum;
import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * FolksonomyNode
 *
 * @author star
 * @date 2021/1/12
 */
@Data
public class FolksonomyNode {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long pid;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "标签类型")
    private FolksonomyTypeEnum type;

    @ApiModelProperty(value = "节点类型")
    private FolksonomyNodeTypeEnum nodeType;

    @ApiModelProperty(value = "子目录")
    private List<FolksonomyNode> children;
}
