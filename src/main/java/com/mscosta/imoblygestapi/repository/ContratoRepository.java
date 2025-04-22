package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Override
    Optional<Contrato> findById(Long aLong);

    Optional<Contrato> findByCasa_Id(long id);

    Optional<Contrato> findByInquilino_Id(long id);

    Optional<Contrato> findByLocatario_Id(long id);








}
