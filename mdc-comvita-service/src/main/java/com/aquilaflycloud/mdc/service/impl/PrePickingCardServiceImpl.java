package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aquilaflycloud.mdc.enums.pre.IsUpdateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.PreGoodsInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PrePickingCardAnalysisResult;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toMap;

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

    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;

    @Resource
    private PreGoodsInfoMapper preGoodsInfoMapper;

    @Override
    public IPage<PrePickingCard> page(PrePickingCardPageParam param) {
        return prePickingCardMapper.selectPage(param.page(), Wrappers.<PrePickingCard> lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getPickingCode()), PrePickingCard::getPickingCode, param.getPickingCode())
                .eq(ObjectUtil.isNotNull(param.getPickingState()), PrePickingCard::getPickingState, param.getPickingState())
                .orderByDesc(PrePickingCard::getPickingCode)
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

    @Override
    @Transactional
    public void batchAdd(PrePickingCardBatchAddParam param) {
        Long tenantId = MdcUtil.getCurrentTenantId();

        //????????????id
        RedisUtil.syncLoad("batchAddPrePickingCard" + tenantId, () -> {
            int count = param.getCount();
            //??????????????????
            List<String> codeList = buildPrePickingCardCode(count);

            //?????????????????????
            JSONObject jsonObject = buildCardIdAndPass(count);
            List<Long> idList = jsonObject.getJSONArray("idList").toList(Long.class);
            List<String> passList = jsonObject.getJSONArray("passList").toList(String.class);

            if (codeList.size() != count || idList.size() != count || passList.size() != count) {
                throw new ServiceException("???????????????????????????????????????");
            }

            //????????????
            List<PrePickingCard> insertCards = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Long id = idList.get(i);
                String pass = passList.get(i);
                String code = codeList.get(i);

                PrePickingCard item = new PrePickingCard(id, code, pass, PickingCardStateEnum.NO_SALE);
                insertCards.add(item);
            }

            int insertCount = prePickingCardMapper.insertAllBatch(insertCards);

            if (insertCount != count) {
                throw new ServiceException("???????????????????????????????????????");
            }
            return null;
        });
    }

    @Override
    public void update(PrePickingCardUpdateParam param) {
        PrePickingCard card = prePickingCardMapper.selectById(param.getId());

        if (ObjectUtil.equal(card.getPickingState(), PickingCardStateEnum.NO_SALE)) {
            PrePickingCard update = new PrePickingCard();
            update.setId(card.getId());
            update.setPickingState(PickingCardStateEnum.CANCEL);
            int count = prePickingCardMapper.updateById(update);

            if (count != 1) {
                throw new ServiceException("?????????????????????????????????");
            }
        } else {
            throw new ServiceException("????????????????????????????????????????????????");
        }
    }

    /**
     * ?????????????????????????????????????????????4??????7
     * @param count
     * @return
     */
    private List<String> buildPrePickingCardCode(int count) {
        //??????????????????
        String bigCode = "00000";

        //????????????????????????????????????
        List<PrePickingCard> prePickingCards = prePickingCardMapper.selectList(Wrappers.<PrePickingCard>lambdaQuery()
                .orderByDesc(PrePickingCard::getPickingCode)
                .last(" limit 1")
        );

        //???????????????????????????
        if (null != prePickingCards && prePickingCards.size() == 1) {
            PrePickingCard card = prePickingCards.get(0);
            bigCode = card.getPickingCode();
        }

        //??????????????????????????????
        List<String> codeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            //????????????code
            Long code = Long.valueOf(bigCode);
            code = code + 1;

            //????????????code???????????????code???????????????code
            String newCode = String.format("%05d", code);
            bigCode = newCode;

            //??????4???7???????????????????????????i-1
            if (newCode.contains("4") || newCode.contains("7")) {
                i = i-1;
                continue;
            }

            codeList.add(newCode);
        }

        return codeList;
    }

    private JSONObject buildCardIdAndPass(int count) {
        List<Long> idList = new ArrayList<>();
        List<String> passList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            long id = MdcUtil.getSnowflakeId();
            idList.add(id);
            passList.add(buildPrePickingCardPass(id));
        }

        JSONObject result = new JSONObject();
        result.set("idList", idList);
        result.set("passList", passList);

        return result;
    }

    /**
     * ?????????????????????
     * @param cardId
     * @return
     */
    private String buildPrePickingCardPass(Long cardId) {
        if (MdcUtil.getCurrentTenantId() == null) {
            return null;
        }
        String setKey = "prePickingCardPassSet" + MdcUtil.getCurrentTenantId();
        String mapKey = "prePickingCardPassMap" + MdcUtil.getCurrentTenantId();
        if (!RedisUtil.redis().hasKey(setKey)) {
            initPrePickingCardPass();
        }
        String code = RedisUtil.<Long, String>hashRedis().get(mapKey, cardId);
        if (code != null) {
            return code;
        }
        code = RandomUtil.randomNumbers(11);
        Long count = RedisUtil.setRedis().add(setKey, code);
        if (count < 1) {
            code = buildPrePickingCardPass(cardId);
        }
        RedisUtil.hashRedis().put(mapKey, cardId, code);
        RedisUtil.redis().expire(setKey, 30, TimeUnit.DAYS);
        RedisUtil.redis().expire(mapKey, 30, TimeUnit.DAYS);
        return code;
    }

    /**
     * ????????????????????????
     */
    private void initPrePickingCardPass() {
        String setKey = "prePickingCardPassSet" + MdcUtil.getCurrentTenantId();
        String mapKey = "prePickingCardPassMap" + MdcUtil.getCurrentTenantId();
        if (!RedisUtil.redis().hasKey(setKey)) {
            List<PrePickingCard> cardInfoList = prePickingCardMapper.selectList(Wrappers.<PrePickingCard>lambdaQuery()
                    .select(PrePickingCard::getPassword, PrePickingCard::getId)
                    .isNotNull(PrePickingCard::getPassword)
            );
            if (cardInfoList.size() > 0) {
                RedisUtil.setRedis().add(setKey, cardInfoList.stream().map(PrePickingCard::getPassword).distinct().toArray(String[]::new));
                RedisUtil.hashRedis().putAll(mapKey, cardInfoList.stream().collect(toMap(PrePickingCard::getId, PrePickingCard::getPassword)));
                RedisUtil.redis().expire(setKey, 30, TimeUnit.DAYS);
                RedisUtil.redis().expire(mapKey, 30, TimeUnit.DAYS);
            }
        }
    }


    @Override
    public void validationPickingCard(PrePickingCardValidationParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
        .eq(PrePickingCard::getPickingCode,param.getPickingCode())
        .eq(PrePickingCard::getPickingState,PickingCardStateEnum.NO_SALE));
        if(prePickingCard == null){
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    public PreOrderGoods validationCardPassWord(PreReservationOrderGoodsParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPassword,param.getPassword())
                .eq(PrePickingCard::getPickingState,PickingCardStateEnum.SALE));
        if (prePickingCard == null) {
            throw new ServiceException("??????????????????????????????????????????");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getCardId, prePickingCard.getId())
                .notIn(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.REFUND));
        if (preOrderGoods == null) {
            throw new ServiceException("???????????????????????????????????????????????????");
        }
        if (StrUtil.isBlank(preOrderGoods.getCardCode())) {
            throw new ServiceException("?????????????????????");
        }
        return preOrderGoods;
    }
}
