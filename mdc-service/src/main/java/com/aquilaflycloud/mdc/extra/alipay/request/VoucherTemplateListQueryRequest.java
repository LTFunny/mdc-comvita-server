package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VoucherTemplateListQueryRequest
 *
 * @author star
 * @date 2020-06-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherTemplateListQueryRequest extends AlipayBaseRequest {
    /**
     * 模板创建结束时间，格式为：yyyy-MM-dd HH:mm:ss
     */
    private String createEndTime;

    /**
     * 模板创建开始时间，格式为：yyyy-MM-dd HH:mm:ss
     */
    private String createStartTime;

    /**
     * 页码，必须为大于0的整数， 1表示第一页，2表示第2页，依次类推。
     */
    private Long pageNum;

    /**
     * 每页记录条数，必须为大于0的整数，最大值为30
     */
    private Long pageSize;
}
