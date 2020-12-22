package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.parking.*;

/**
 * ParkingAjbDataService
 *
 * @author star
 * @date 2020-01-08
 */
public interface ParkingAjbDataService {
    void addRecord(ParkingAjbEnterRecord record);

    void addRecord(ParkingAjbChargeRecord record);

    void addRecord(ParkingAjbLockRecord record);

    void addRecord(ParkingAjbLockErrRecord record);

    void addRecord(ParkingAjbCouponRecord record);
}

