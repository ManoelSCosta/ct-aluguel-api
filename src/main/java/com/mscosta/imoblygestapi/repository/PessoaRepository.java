package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
