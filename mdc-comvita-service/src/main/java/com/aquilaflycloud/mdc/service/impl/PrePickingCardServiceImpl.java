package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardPageParam;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardValidationParam;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * PrePickingCardController
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@Slf4j
@Service
public class PrePickingCardServiceImpl implements PrePickingCardService {
    @Resource
    private PrePickingCardMapper prePickingCardMapper;

    @Override
    public IPage<PrePickingCard> page(PrePickingCardPageParam param) {
        return null;
    }

    @Override
    public Boolean validationPickingCard(PrePickingCardValidationParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
        .eq(PrePickingCard::getPickingCode,param.getPickingCode())
        .eq(PrePickingCard::getPickingState,PickingCardStateEnum.NORMAL));
        if(prePickingCard == null){
            throw new ServiceException("该卡号无法使用。");
        }
        return true;
    }
}
