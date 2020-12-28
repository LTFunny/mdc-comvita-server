package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardPageParam;
import com.aquilaflycloud.mdc.result.pre.PrePickingCardAnalysisResult;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PrePickingCardServiceImpl
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
        return prePickingCardMapper.selectPage(param.page(), Wrappers.<PrePickingCard> lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getPickingCode()), PrePickingCard::getPickingCode, param.getPickingCode())
                .eq(ObjectUtil.isNotNull(param.getPickingState()), PrePickingCard::getPickingState, param.getPickingState())
                .orderByDesc(PrePickingCard::getCreateTime)
        );
    }

    @Override
    public PrePickingCardAnalysisResult analysis() {
        List<PrePickingCard> prePickingCards = prePickingCardMapper.selectList(null);
        PrePickingCardAnalysisResult result = new PrePickingCardAnalysisResult();

        Long allCount = 0L;
        Long saleCount = 0L;
        Long reserveCount = 0L;
        Long verificateCount = 0L;

        if (null != prePickingCards && prePickingCards.size() > 0) {
            allCount = new Long(prePickingCards.size());
            saleCount = prePickingCards.stream().filter(item -> ObjectUtil.equal(item.getPickingState(), PickingCardStateEnum.SALE)).count();
            reserveCount = prePickingCards.stream().filter(item -> ObjectUtil.equal(item.getPickingState(), PickingCardStateEnum.RESERVE)).count();
            verificateCount = prePickingCards.stream().filter(item -> ObjectUtil.equal(item.getPickingState(), PickingCardStateEnum.VERIFICATE)).count();
        }

        result.setCount(allCount, saleCount, reserveCount, verificateCount);
        return result;
    }
}
