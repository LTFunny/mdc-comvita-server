package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aquilaflycloud.mdc.enums.pre.RuleDefaultEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.PreRuleAddParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleIdParam;
import com.aquilaflycloud.mdc.param.pre.PreRulePageParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleUpdateParam;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.service.PreRuleService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public IPage<PreRuleInfo> page(PreRulePageParam param) {
        IPage<PreRuleInfo> list = preRuleInfoMapper.selectPage(param.page(), Wrappers.<PreRuleInfo>lambdaQuery()
                .like( param.getRuleName()!=null,PreRuleInfo::getRuleName, param.getRuleName())
                .eq( param.getRuleState()!=null,PreRuleInfo::getRuleState, param.getRuleState())
                .eq( param.getRuleType()!=null,PreRuleInfo::getRuleType, param.getRuleType())
        );
        return list;
    }

    @Override
    public void add(PreRuleAddParam param) {
        checkNameParam(param.getRuleName());
        PreRuleInfo info = new PreRuleInfo();
        BeanUtil.copyProperties(param, info);
        info.setId(MdcUtil.getSnowflakeId());
        int count = preRuleInfoMapper.insert(info);
        if (count == 1) {
            log.info("新增营销规则成功");
        } else {
            throw new ServiceException("新增营销规则失败");
        }
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
        checkNameParam(param.getRuleName());
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(param, info,"id");
        preRuleInfoMapper.updateById(info);
    }

    @Override
    public void stop(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        info.setRuleState(RuleStateEnum.DISABLE.getType());
        preRuleInfoMapper.updateById(info);
    }

    @Override
    public void setDefault(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        info.setIsDefault(RuleDefaultEnum.DEFAULT.getType());
        preRuleInfoMapper.updateById(info);
    }

    @Override
    public List<PreEnableRuleResult> getEnableRules() {
        List<PreRuleInfo> infos = preRuleInfoMapper.selectList(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getRuleState, RuleStateEnum.ENABLE.getType())
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
