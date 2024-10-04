package org.ftf.koifishveterinaryservicecenter.service.surchargeservice;

import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;

import java.util.List;

public interface SurchargeService {
    List<MovingSurcharge> getAllMovingSurcharges();

    MovingSurcharge getMovingSurchargeById(Integer id);

    MovingSurcharge updateMovingSurcharge(Integer movingSurchargeId, MovingSurcharge movingSurcharge);
}
