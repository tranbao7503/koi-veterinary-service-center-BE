package org.ftf.koifishveterinaryservicecenter.service.surchargeservice;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;
import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;
import org.ftf.koifishveterinaryservicecenter.exception.MovingSurchargeNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MovingSurchargeMapper;
import org.ftf.koifishveterinaryservicecenter.repository.MovingSurchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurchargeServiceImpl implements SurchargeService {

    private final MovingSurchargeRepository movingSurchargeRepository;
    private final MovingSurchargeMapper movingSurchargeMapper;

    @Autowired
    public SurchargeServiceImpl(MovingSurchargeRepository movingSurchargeRepository, MovingSurchargeMapper movingSurchargeMapper) {
        this.movingSurchargeRepository = movingSurchargeRepository;
        this.movingSurchargeMapper = movingSurchargeMapper;
    }

    /*
     * Get all MovingSurcharge from database
     * */
    @Override
    public List<MovingSurchargeDTO> getAllMovingSurcharges() {

        List<MovingSurcharge> movingSurcharges = movingSurchargeRepository.findAll();

        // Map List of MovingSurCharge Entity into MovingSurcharge DTO
        List<MovingSurchargeDTO> movingSurchargeDTOs = movingSurcharges.stream()
                .map(movingSurchargeMapper::convertToMovingSurchargeDTO)
                .collect(Collectors.toList());

        return movingSurchargeDTOs;
    }

    /*
     * Get MovingSurcharge By ID
     * */
    @Override
    public MovingSurchargeDTO getMovingSurchargeById(Integer id) throws MovingSurchargeNotFoundException {

        MovingSurcharge movingSurcharge = movingSurchargeRepository.findById(id).orElse(null);

        if (movingSurcharge == null) { // MovingSurcharge not existed
            throw new MovingSurchargeNotFoundException("Moving Surcharge not found with id: " + id);
        } else { // MovingSurcharge existed
            return movingSurchargeMapper.convertToMovingSurchargeDTO(movingSurcharge);
        }

    }

    @Override
    public MovingSurchargeDTO updateMovingSurcharge(Integer movingSurchargeId, MovingSurchargeDTO movingSurchargeDTO) {
        MovingSurcharge movingSurcharge = movingSurchargeRepository.findById(movingSurchargeId).orElse(null);

        if (movingSurcharge == null) { // Moving surcharge not existed
            throw new MovingSurchargeNotFoundException("Moving Surcharge not found with id: " + movingSurchargeId);
        } else { // Moving surcharge existed
            movingSurcharge = movingSurchargeMapper.convertToMovingSurcharge(movingSurchargeDTO); // Convert MovingSurcharge DTO into Entity
            movingSurchargeRepository.save(movingSurcharge); // Update into database
            return movingSurchargeDTO;
        }
    }
}
