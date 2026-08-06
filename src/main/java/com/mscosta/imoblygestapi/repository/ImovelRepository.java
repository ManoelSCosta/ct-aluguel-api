package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

    /**
     * Busca por descrição e/ou logradouro do endereço; filtro nulo é ignorado.
     * Escrito em JPQL porque o logradouro está na entidade associada Endereco,
     * o que uma query derivada do nome do método não consegue expressar.
     */
    @Query("""
            SELECT i FROM Imovel i
            WHERE (:descricao IS NULL OR LOWER(i.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
              AND (:logradouro IS NULL OR LOWER(i.endereco.logradouro) LIKE LOWER(CONCAT('%', :logradouro, '%')))
            """)
    Page<Imovel> buscarPorDescricaoOuLogradouro(@Param("descricao") @Nullable String descricao,
                                                @Param("logradouro") @Nullable String logradouro,
                                                Pageable pageable);
}
