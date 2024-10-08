package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.CertificateDto;
import org.ftf.koifishveterinaryservicecenter.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CertificateMapper.class})
public interface CertificateMapper {
    CertificateMapper INSTANCE = Mappers.getMapper(CertificateMapper.class);

    CertificateDto convertToCertificateDto(Certificate certificate);

    Certificate convertToCertificateEntity(CertificateDto certificateDto);
}
