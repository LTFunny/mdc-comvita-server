package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.parking.ParkingMemberCar;
import com.aquilaflycloud.mdc.param.parking.MemberCarParam;

import java.util.List;

/**
 * ParkingMemberCarService
 *
 * @author star
 * @date 2020-02-10
 */
public interface ParkingMemberCarService {
    void saveCar(MemberCarParam param);

    void deleteCar(MemberCarParam param);

    List<ParkingMemberCar> listCar();
}

