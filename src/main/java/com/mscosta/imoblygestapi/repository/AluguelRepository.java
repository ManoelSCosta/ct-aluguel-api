package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
}
