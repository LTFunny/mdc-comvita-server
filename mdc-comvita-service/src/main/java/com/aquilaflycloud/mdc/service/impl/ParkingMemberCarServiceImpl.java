package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aquilaflycloud.mdc.mapper.ParkingMemberCarMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.parking.ParkingMemberCar;
import com.aquilaflycloud.mdc.param.parking.MemberCarParam;
import com.aquilaflycloud.mdc.service.ParkingMemberCarService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ParkingMemberCarServiceImpl
 *
 * @author star
 * @date 2020-02-10
 */
@Slf4j
@Service
public class ParkingMemberCarServiceImpl implements ParkingMemberCarService {
    @Resource
    private ParkingMemberCarMapper parkingMemberCarMapper;

    @Override
    public void saveCar(MemberCarParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        int carCount = parkingMemberCarMapper.selectCount(Wrappers.<ParkingMemberCar>lambdaQuery()
                .eq(ParkingMemberCar::getMemberId, memberInfo.getId())
                .ne(ParkingMemberCar::getCarNo, param.getCarNo())
        );
        if (carCount >= 3) {
            throw new ServiceException("车辆已超过数量限制");
        }
        RedisUtil.syncLoad("saveCarNoLock" + memberInfo.getId(), () -> {
            ParkingMemberCar car = parkingMemberCarMapper.selectOne(Wrappers.<ParkingMemberCar>lambdaQuery()
                    .eq(ParkingMemberCar::getMemberId, memberInfo.getId())
                    .eq(ParkingMemberCar::getCarNo, param.getCarNo())
            );
            ParkingMemberCar newCar = new ParkingMemberCar();
            if (car == null) {
                BeanUtil.copyProperties(param, newCar);
            } else {
                newCar.setId(car.getId());
            }
            MdcUtil.setMemberInfo(newCar, memberInfo);
            int count;
            if (newCar.getId() == null) {
                count = parkingMemberCarMapper.insert(newCar);
            } else {
                count = parkingMemberCarMapper.updateById(newCar);
            }
            if (count <= 0) {
                throw new ServiceException("保存车辆失败");
            }
            return null;
        });
    }

    @Override
    public void deleteCar(MemberCarParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        int count = parkingMemberCarMapper.delete(Wrappers.<ParkingMemberCar>lambdaQuery()
                .eq(ParkingMemberCar::getMemberId, memberId)
                .eq(ParkingMemberCar::getCarNo, param.getCarNo())
        );
        if (count <= 0) {
            throw new ServiceException("删除车辆失败");
        }
    }

    @Override
    public List<ParkingMemberCar> listCar() {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return parkingMemberCarMapper.selectList(Wrappers.<ParkingMemberCar>lambdaQuery()
                .eq(ParkingMemberCar::getMemberId, memberId)
        );
    }
}
