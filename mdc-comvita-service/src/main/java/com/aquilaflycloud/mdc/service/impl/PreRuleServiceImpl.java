package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.pre.RuleDefaultEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.result.pre.PreRuleDetailResult;
import com.aquilaflycloud.mdc.service.PreRuleService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * PreRuleServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreRuleServiceImpl implements PreRuleService {

    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;

    @Override
    public IPage<PreRuleDetailResult> page(PreRulePageParam param) {
        return preRuleInfoMapper.selectPage(param.page(), Wrappers.<PreRuleInfo>lambdaQuery()
                .like( param.getRuleName()!=null,PreRuleInfo::getRuleName, param.getRuleName())
                .eq( param.getRuleState()!=null,PreRuleInfo::getRuleState, param.getRuleState())
                .eq( param.getRuleType()!=null,PreRuleInfo::getRuleType, param.getRuleType())
                .orderByDesc(PreRuleInfo::getIsDefault,PreRuleInfo::getCreateTime)
        ).convert(this::dataConvert);
    }

    @Override
    public void add(PreRuleAddParam param) {
        checkNameParam(param.getRuleName());
        PreRuleInfo info = new PreRuleInfo();
        BeanUtil.copyProperties(param, info);
        info.setId(MdcUtil.getSnowflakeId());
        if(param.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderDiscount()));
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderFullReduce()));
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getRefGoods()));
        }
        int count = preRuleInfoMapper.insert(info);
        if (count == 1) {
            log.info("新增营销规则成功");
        } else {
            throw new ServiceException("新增营销规则失败");
        }
    }

    private PreRuleDetailResult dataConvert(PreRuleInfo info){
        if(null != info){
            PreRuleDetailResult p = new PreRuleDetailResult();
            BeanUtil.copyProperties(info, p);
            if(info.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
                JSONObject jsonObject = JSONUtil.parseObj(info.getTypeDetail());
                BigDecimal bigDecimal = jsonObject.getBigDecimal("discount");
                p.setDiscount(bigDecimal);
            }
            if(info.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
                JSONObject jsonObject = JSONUtil.parseObj(info.getTypeDetail());
                BigDecimal fullPrice = jsonObject.getBigDecimal("fullPrice");
                BigDecimal reducePrice = jsonObject.getBigDecimal("reducePrice");
                PreRuleOrderFullReduceParam preRuleOrderFullReduceParam = new PreRuleOrderFullReduceParam();
                preRuleOrderFullReduceParam.setFullPrice(fullPrice);
                preRuleOrderFullReduceParam.setReducePrice(reducePrice);
                p.setOrderFullReduce(preRuleOrderFullReduceParam);
            }
            if(info.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
                JSONArray jsonArray = JSONUtil.parseArray(info.getTypeDetail());
                List<PreRuleGoodsParam> list = new ArrayList<>();
                if(null != jsonArray){
                    for (JSONObject jsonObj : jsonArray.jsonIter()) {
                        Long goodsId = jsonObj.getLong("goodsId");
                        String goodsCode = jsonObj.getStr("goodsCode");
                        PreRuleGoodsParam param1 = new PreRuleGoodsParam();
                        param1.setGoodsCode(goodsCode);
                        param1.setGoodsId(goodsId);
                        list.add(param1);
                    }
                }
                p.setRefGoods(list);
            }
            return p;
        }else {
            return null;
        }
    }

    @Override
    public PreRuleDetailResult get(PreRuleIdParam param) {
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        return dataConvert(info);
    }

    private void checkNameParam(String name) {
        PreRuleInfo info =  preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getRuleName,name));
        if(null != info){
            throw new ServiceException("已存在相同名称的规则,名称为:" + info.getRuleName());
        }
    }

    @Override
    public void update(PreRuleUpdateParam param) {
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(param, info,"id","typeDetail");
        if(param.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
            String jsonStr = JSONUtil.toJsonStr(param.getOrderDiscount());
            if(!info.getTypeDetail().equals(jsonStr)){
                info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderDiscount()));
            }
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
            String jsonStr = JSONUtil.toJsonStr(param.getOrderFullReduce());
            if(!info.getTypeDetail().equals(jsonStr)){
                info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderFullReduce()));
            }
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
            String jsonStr = JSONUtil.toJsonStr(param.getRefGoods());
            if(!info.getTypeDetail().equals(jsonStr)){
                info.setTypeDetail(JSONUtil.toJsonStr(param.getRefGoods()));
            }
        }
        preRuleInfoMapper.updateById(info);
    }

    @Override
    public void cancelStart(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        if(RuleStateEnum.DISABLE.getType() == info.getRuleType().getType()){
            info.setRuleState(RuleStateEnum.ENABLE);
        }else if(RuleStateEnum.ENABLE.getType() == info.getRuleType().getType()){
            info.setRuleState(RuleStateEnum.DISABLE);
        }
        preRuleInfoMapper.updateById(info);
    }

    @Override
    public void setDefault(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo oldInfo =  preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getIsDefault,RuleDefaultEnum.DEFAULT));
        oldInfo.setIsDefault(RuleDefaultEnum.NOT_DEFAULT);
        preRuleInfoMapper.updateById(oldInfo);
        log.info("默认撤销成功!");
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        info.setIsDefault(RuleDefaultEnum.DEFAULT);
        preRuleInfoMapper.updateById(info);
        log.info("默认设置成功!");
    }

    @Override
    public List<PreEnableRuleResult> getEnableRules() {
        List<PreRuleInfo> infos = preRuleInfoMapper.selectList(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getRuleState, RuleStateEnum.ENABLE)
        );
        if(CollUtil.isNotEmpty(infos)){
            List<PreEnableRuleResult> results = new ArrayList<>();
            infos.forEach(r -> {
                PreEnableRuleResult ruleResult = new  PreEnableRuleResult();
                ruleResult.setRuleId(r.getId());
                ruleResult.setRuleName(r.getRuleName());
            });
            return results;
        }else{
            return null;
        }
    }
}
