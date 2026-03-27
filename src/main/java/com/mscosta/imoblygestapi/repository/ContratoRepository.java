package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Object> findByImovel_Id(long imovelId);

    Optional<Object> findByInquilino_Id(long inquilinoId);

    Optional<Object> findByLocador_Id(long locadorId);
}
