package org.ftf.koifishveterinaryservicecenter.service.surchargeservice;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;

import java.util.List;

public interface SurchargeService {
    List<MovingSurchargeDTO> getAllMovingSurcharges();
    MovingSurchargeDTO getMovingSurchargeById(Integer id);
    MovingSurchargeDTO updateMovingSurcharge(Integer movingSurchargeId, MovingSurchargeDTO movingSurchargeDTO);
}
