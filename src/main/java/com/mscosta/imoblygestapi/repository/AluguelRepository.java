package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findAllByContratoId(Long contratoId);
}
