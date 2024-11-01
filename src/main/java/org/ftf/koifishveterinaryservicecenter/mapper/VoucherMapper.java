package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.VoucherDto;
import org.ftf.koifishveterinaryservicecenter.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = VoucherMapper.class)
public interface VoucherMapper {

    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    //@Mapping(source = "users.quantity", target = "quantity")
    @Mapping(source = "discountAmount", target = "discountAmount")
    VoucherDto convertToVoucherDto(Voucher voucher);
}
