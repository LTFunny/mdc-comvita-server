package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.parking.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * ParkingService
 *
 * @author star
 * @date 2020-03-05
 */
public interface ParkingService {
    IPage<ParkingAjbChargeRecord> pageCharge(PageParam param);

    IPage<ParkingAjbEnterRecord> pageEnter(PageParam param);

    IPage<ParkingAjbLockRecord> pageLock(PageParam param);

    IPage<ParkingAjbLockErrRecord> pageLockErr(PageParam param);

    IPage<ParkingAjbCouponRecord> pageCoupon(PageParam param);

    IPage<ParkingAjbInterfaceRecord> pageInterface(PageParam param);
}

