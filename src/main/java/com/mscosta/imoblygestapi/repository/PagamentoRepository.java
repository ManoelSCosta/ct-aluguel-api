package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
