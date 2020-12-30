package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardPageParam;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardValidationParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * PrePickingCardController
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
public interface PrePickingCardService {

    IPage<PrePickingCard> page(PrePickingCardPageParam param);

    Boolean validationPickingCard(PrePickingCardValidationParam param);
}
