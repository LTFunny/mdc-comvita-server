package com.aquilaflycloud.mdc.job;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PreOrderGoodsAutoSignJob
 *
 * @author star
 * @date 2021/1/8
 */
@Slf4j
@Component
public class PreOrderGoodsAutoSignJob extends JavaProcessor {
    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job PreOrderGoodsAutoSignJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        try {
            PreOrderGoodsMapper preOrderGoodsMapper = SpringUtil.getBean(PreOrderGoodsMapper.class);
            //查询超过10天未签收的订单商品
            List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.normalSelectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.ALSENDGOODS)
                    .le(PreOrderGoods::getDeliveryTime, DateTime.now().offset(DateField.DAY_OF_YEAR, -10))
            );
            PreOrderInfoService preOrderInfoService = SpringUtil.getBean(PreOrderInfoService.class);
            for (PreOrderGoods preOrderGoods : preOrderGoodsList) {
                ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, preOrderGoods.getTenantId());
                ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, preOrderGoods.getSubTenantId());
                preOrderInfoService.confirmReceiptOrderGoods(preOrderGoods.getId(), "系统自动签收");
            }
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job PreOrderGoodsAutoSignJob Error...");
        }
        log.info("Job PreOrderGoodsAutoSignJob End...");
        return processResult;
    }
}
