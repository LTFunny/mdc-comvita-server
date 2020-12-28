package com.aquilaflycloud.mdc.extra.tencentPosition.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SuggestionListReq {

    private final String method = "/ws/place/v1/suggestion";

    private String keyword;

    private String region;

    private Integer regionFix;

    private String location;

    private Integer getSubpois;

    private Integer policy;

    private String filter;

    private String addressFormat;

    private Long pageIndex;

    private Long pageSize;

    private String output;

    private String callback;

}
