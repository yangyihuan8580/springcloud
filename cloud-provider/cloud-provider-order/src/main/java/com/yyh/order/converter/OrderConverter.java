package com.yyh.order.converter;

import com.yyh.order.dbo.OrderDO;
import com.yyh.order.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderConverter {

    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    @Mappings({
            @Mapping(source = "id.value", target = "id"),
            @Mapping(source = "orderCode.value", target = "orderCode"),
            @Mapping(source = "orderMoney.value", target = "orderMoney"),
            @Mapping(source = "couponMoney.value", target = "couponMoney"),
            @Mapping(source = "needMoney.value", target = "needMoney"),
            @Mapping(source = "parkRecordId.value", target = "parkRecordId"),
            @Mapping(source = "plate.plateNumber", target = "plateNumber"),
            @Mapping(source = "plate.plateColor", target = "plateColor"),
            @Mapping(source = "areaInfo.terminalName", target = "terminalName"),
            @Mapping(source = "areaInfo.terminalId", target = "terminalId"),
            @Mapping(source = "areaInfo.parkingAreaId", target = "parkingAreaId"),
            @Mapping(source = "areaInfo.parkingAreaName", target = "parkingAreaName"),
            @Mapping(source = "areaInfo.crossId", target = "crossId"),
            @Mapping(source = "areaInfo.crossName", target = "crossName"),
    })
    OrderDO domain2do(Order order);


    @Mappings({
            @Mapping(target = "id.value", source = "id"),
            @Mapping(target = "orderCode.value", source = "orderCode"),
            @Mapping(target = "orderMoney.value", source = "orderMoney"),
            @Mapping(target = "couponMoney.value", source = "couponMoney"),
            @Mapping(target = "needMoney.value", source = "needMoney"),
            @Mapping(target = "parkRecordId.value", source = "parkRecordId"),
            @Mapping(target = "plate.plateNumber", source = "plateNumber"),
            @Mapping(target = "plate.plateColor", source = "plateColor"),
            @Mapping(target = "areaInfo.terminalName", source = "terminalName"),
            @Mapping(target = "areaInfo.terminalId", source = "terminalId"),
            @Mapping(target = "areaInfo.parkingAreaId", source = "parkingAreaId"),
            @Mapping(target = "areaInfo.parkingAreaName", source = "parkingAreaName"),
            @Mapping(target = "areaInfo.crossId", source = "crossId"),
            @Mapping(target = "areaInfo.crossName", source = "crossName"),
    })
    Order do2domain(OrderDO orderDO);
}
