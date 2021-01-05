package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderExpressActionEnum {

    //物流状态
    NONE(0, "暂无轨迹信息"),
    COLLECTED(1, "已揽收"),
    ON_WAY (2, "在途中"),
    ARRIVE_CITY(201, "到达派件城市"),
    DISPATCH(202,"派件中"),
    BEENCOMPLETED(211,"已放入快递柜或驿站"),
    ALREADY_SIGNED(3,"已签收"),
    NORMAL_SIGNED(301,"正常签收"),
    ABNORMAL_DISPATCH_SIGNED(302,"派件异常后最终签收"),
    REPLACE_SIGNED(304,"代收签收"),
    CABINET_STATION_SIGNED(311,"快递柜或驿站签收"),
    PROBLEM(4,"问题件"),
    DELIVERY_NO_MESSAGE(401,"发货无信息"),
    OVERTIME_NO_SIGNED(402,"超时未签收"),
    OVERTIME_NO_UPDATE(403,"超时未更新"),
    REJECTION(404,"拒收(退件)"),
    DISPATCH_EXCEPTION(405,"派件异常"),
    RETURN_RECEIPT(406,"退货签收"),
    RETURN_NO_RECEIPT(407,"退货未签收"),
    OVERTIME_CABINET_STATION(412,"快递柜或驿站超时未取"),
    ;

    OrderExpressActionEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
