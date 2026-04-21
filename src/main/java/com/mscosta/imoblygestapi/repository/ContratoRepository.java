package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Contrato;
import com.mscosta.imoblygestapi.enums.StatusContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByImovelId(long imovelId);

    List<Contrato> findByInquilinoId(long inquilinoId);

    List<Contrato> findByLocadorId(long locadorId);

    List<Contrato> findAllByStatusContrato(StatusContrato statusContrato);

    boolean existsByImovelIdAndStatusContrato(Long imovelId, StatusContrato statusContrato);
}
