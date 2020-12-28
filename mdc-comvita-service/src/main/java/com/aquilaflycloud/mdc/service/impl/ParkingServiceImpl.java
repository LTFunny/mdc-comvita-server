package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.service.ParkingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ParkingMemberCarServiceImpl
 *
 * @author star
 * @date 2020-02-10
 */
@Slf4j
@Service
public class ParkingServiceImpl implements ParkingService {
    @Resource
    private ParkingAjbChargeRecordMapper parkingAjbChargeRecordMapper;
    @Resource
    private ParkingAjbEnterRecordMapper parkingAjbEnterRecordMapper;
    @Resource
    private ParkingAjbLockRecordMapper parkingAjbLockRecordMapper;
    @Resource
    private ParkingAjbLockErrRecordMapper parkingAjbLockErrRecordMapper;
    @Resource
    private ParkingAjbCouponRecordMapper parkingAjbCouponRecordMapper;
    @Resource
    private ParkingAjbInterfaceRecordMapper parkingAjbInterfaceRecordMapper;

    @Override
    public IPage<ParkingAjbChargeRecord> pageCharge(PageParam param) {
        return parkingAjbChargeRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbChargeRecord>lambdaQuery()
                .orderByDesc(ParkingAjbChargeRecord::getId));
    }

    @Override
    public IPage<ParkingAjbEnterRecord> pageEnter(PageParam param) {
        return parkingAjbEnterRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbEnterRecord>lambdaQuery()
                .orderByDesc(ParkingAjbEnterRecord::getId));
    }

    @Override
    public IPage<ParkingAjbLockRecord> pageLock(PageParam param) {
        return parkingAjbLockRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbLockRecord>lambdaQuery()
                .orderByDesc(ParkingAjbLockRecord::getId));
    }

    @Override
    public IPage<ParkingAjbLockErrRecord> pageLockErr(PageParam param) {
        return parkingAjbLockErrRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbLockErrRecord>lambdaQuery()
                .orderByDesc(ParkingAjbLockErrRecord::getId));
    }

    @Override
    public IPage<ParkingAjbCouponRecord> pageCoupon(PageParam param) {
        return parkingAjbCouponRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbCouponRecord>lambdaQuery()
                .orderByDesc(ParkingAjbCouponRecord::getId));
    }

    @Override
    public IPage<ParkingAjbInterfaceRecord> pageInterface(PageParam param) {
        return parkingAjbInterfaceRecordMapper.selectPage(param.page(), Wrappers.<ParkingAjbInterfaceRecord>lambdaQuery()
                .orderByDesc(ParkingAjbInterfaceRecord::getId));
    }
}
