package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.parking.CarStateEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.model.system.SystemAccountConfig;
import com.aquilaflycloud.mdc.service.ParkingAjbDataService;
import com.aquilaflycloud.mdc.service.SystemAccountService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ParkingAjbDataServiceImpl
 *
 * @author star
 * @date 2020-01-08
 */
@Slf4j
@Service
public class ParkingAjbDataServiceImpl implements ParkingAjbDataService {
    @Resource
    private SystemAccountService systemAccountService;
    @Resource
    private ParkingAjbEnterRecordMapper parkingAjbEnterRecordMapper;
    @Resource
    private ParkingAjbChargeRecordMapper parkingAjbChargeRecordMapper;
    @Resource
    private ParkingAjbLockRecordMapper parkingAjbLockRecordMapper;
    @Resource
    private ParkingAjbLockErrRecordMapper parkingAjbLockErrRecordMapper;
    @Resource
    private ParkingAjbCouponRecordMapper parkingAjbCouponRecordMapper;
    @Resource
    private ParkingUnlicensedCarRecordMapper parkingUnlicensedCarRecordMapper;

    @Override
    public void addRecord(ParkingAjbEnterRecord record) {
        SystemAccountConfig account = systemAccountService.getAjbCloudAccountByParkCode(record.getParkCode());
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, account.getTenantId());
        int count = parkingAjbEnterRecordMapper.insert(record);
        //保存无车牌记录
        if (count > 0) {
            if (StrUtil.equalsAny(record.getStatus(), "1", "4")) {
                List<ParkingUnlicensedCarRecord> inCarList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                        .eq(StrUtil.isNotBlank(record.getCarNo()), ParkingUnlicensedCarRecord::getCarNo, record.getCarNo())
                        .eq(StrUtil.isNotBlank(record.getCardId()), ParkingUnlicensedCarRecord::getCardId, record.getCardId())
                        .eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.SCAN)
                );
                ParkingUnlicensedCarRecord inCarRecord = new ParkingUnlicensedCarRecord();
                inCarRecord.setCardType(record.getCardType());
                inCarRecord.setInOperator(record.getOperator());
                inCarRecord.setInForceUp(StrUtil.equals(record.getUpseason(), "1") ? WhetherEnum.YES : WhetherEnum.NO);
                inCarRecord.setCarState(CarStateEnum.IN);
                inCarRecord.setInTime(DateUtil.parse(record.getAccTime()));
                if (inCarList.size() > 0) {
                    inCarRecord.setId(inCarList.get(0).getId());
                    parkingUnlicensedCarRecordMapper.updateById(inCarRecord);
                } else if (StrUtil.isBlank(record.getCarNo())) {
                    inCarRecord.setCardId(record.getCardId());
                    parkingUnlicensedCarRecordMapper.insertIgnoreAllBatch(CollUtil.newArrayList(inCarRecord));
                }
            } else if (StrUtil.equalsAny(record.getStatus(), "2", "5")) {
                List<ParkingUnlicensedCarRecord> inCarList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                        .eq(StrUtil.isNotBlank(record.getCarNo()), ParkingUnlicensedCarRecord::getCarNo, record.getCarNo())
                        .eq(StrUtil.isNotBlank(record.getCardId()), ParkingUnlicensedCarRecord::getCardId, record.getCardId())
                        .eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.IN)
                );
                ParkingUnlicensedCarRecord outCarRecord = new ParkingUnlicensedCarRecord();
                outCarRecord.setOutOperator(record.getOperator());
                outCarRecord.setOutForceUp(StrUtil.equals(record.getUpseason(), "1") ? WhetherEnum.YES : WhetherEnum.NO);
                outCarRecord.setCarState(CarStateEnum.OUT);
                outCarRecord.setOutTime(DateUtil.parse(record.getAccTime()));
                if (inCarList.size() > 0) {
                    outCarRecord.setId(inCarList.get(0).getId());
                    parkingUnlicensedCarRecordMapper.updateById(outCarRecord);
                } else if (StrUtil.isBlank(record.getCarNo())) {
                    outCarRecord.setCardId(record.getCardId());
                    outCarRecord.setCardType(record.getCardType());
                    parkingUnlicensedCarRecordMapper.insertIgnoreAllBatch(CollUtil.newArrayList(outCarRecord));
                }
            }
        }
    }

    @Override
    public void addRecord(ParkingAjbChargeRecord record) {
        SystemAccountConfig account = systemAccountService.getAjbCloudAccountByParkCode(record.getParkCode());
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, account.getTenantId());
        parkingAjbChargeRecordMapper.insert(record);
    }

    @Override
    public void addRecord(ParkingAjbLockRecord record) {
        SystemAccountConfig account = systemAccountService.getAjbCloudAccountByParkCode(record.getParkCode());
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, account.getTenantId());
        parkingAjbLockRecordMapper.insert(record);
    }

    @Override
    public void addRecord(ParkingAjbLockErrRecord record) {
        SystemAccountConfig account = systemAccountService.getAjbCloudAccountByParkCode(record.getParkCode());
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, account.getTenantId());
        parkingAjbLockErrRecordMapper.insert(record);
    }

    @Override
    public void addRecord(ParkingAjbCouponRecord record) {
        SystemAccountConfig account = systemAccountService.getAjbCloudAccountByParkCode(record.getParkCode());
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, account.getTenantId());
        parkingAjbCouponRecordMapper.insert(record);
    }
}
