package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.service.ParkingAjbDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ParkingAjbDataController
 * 接受安居宝停车相关数据
 *
 * @author star
 * @date 2020-01-08
 */
@RestController
@Slf4j
public class ParkingAjbDataController {

    @Resource
    private ParkingAjbDataService parkingAjbDataService;

    @RequestMapping(value = "ajbParkingEnterHandler")
    public Object ajbParkingEnterHandler(@RequestParam Map map) {
        log.info("安居宝进离记录推送:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        ParkingAjbEnterRecord record = new ParkingAjbEnterRecord();
        Object id = map.get("id");
        map.remove("id");
        record = BeanUtil.fillBeanWithMap(map, record, true);
        record.setAjbId(Convert.toStr(id));
        record.setDataContent(JSONUtil.parseObj(map).toString());
        parkingAjbDataService.addRecord(record);
        return "success";
    }

    @RequestMapping(value = "ajbParkingChargeHandler")
    public Object ajbParkingChargeHandler(@RequestParam Map map) {
        log.info("安居宝收费记录推送:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        ParkingAjbChargeRecord record = new ParkingAjbChargeRecord();
        Object id = map.get("id");
        map.remove("id");
        record = BeanUtil.fillBeanWithMap(map, record, true);
        record.setAjbId(Convert.toStr(id));
        record.setDataContent(JSONUtil.parseObj(map).toString());
        parkingAjbDataService.addRecord(record);
        return "success";
    }

    @RequestMapping(value = "ajbParkingLockHandler")
    public Object ajbParkingLockHandler(@RequestParam Map map) {
        log.info("安居宝锁车记录推送:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        ParkingAjbLockRecord record = new ParkingAjbLockRecord();
        Object id = map.get("id");
        map.remove("id");
        record = BeanUtil.fillBeanWithMap(map, record, true);
        record.setAjbId(Convert.toStr(id));
        record.setDataContent(JSONUtil.parseObj(map).toString());
        parkingAjbDataService.addRecord(record);
        return "success";
    }

    @RequestMapping(value = "ajbParkingLockErrHandler")
    public Object ajbParkingLockErrHandler(@RequestParam Map map) {
        log.info("安居宝锁车异常记录推送:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        ParkingAjbLockErrRecord record = new ParkingAjbLockErrRecord();
        Object id = map.get("id");
        map.remove("id");
        record = BeanUtil.fillBeanWithMap(map, record, true);
        record.setAjbId(Convert.toStr(id));
        record.setDataContent(JSONUtil.parseObj(map).toString());
        parkingAjbDataService.addRecord(record);
        return "success";
    }

    @RequestMapping(value = "ajbParkingCouponHandler")
    public Object ajbParkingCouponHandler(@RequestParam Map map) {
        log.info("安居宝优惠核销记录推送:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        ParkingAjbCouponRecord record = new ParkingAjbCouponRecord();
        Object id = map.get("id");
        map.remove("id");
        record = BeanUtil.fillBeanWithMap(map, record, true);
        record.setAjbId(Convert.toStr(id));
        record.setDataContent(JSONUtil.parseObj(map).toString());
        parkingAjbDataService.addRecord(record);
        return "success";
    }

}
