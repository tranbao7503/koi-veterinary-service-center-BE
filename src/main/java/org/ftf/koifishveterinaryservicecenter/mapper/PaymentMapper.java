package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.PaymentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PaymentMapper.class, componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "transactionTime", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "status", defaultValue = "NOT_PAID")
    Payment convertToEntity(PaymentDto paymentDto);

    PaymentDto convertToDto(Payment payment);

}
