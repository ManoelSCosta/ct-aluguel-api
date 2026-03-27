package com.mscosta.imoblygestapi.repository;

import com.mscosta.imoblygestapi.entity.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {
    Page<Imovel> findByDescricaoLikeIgnoreCaseAndEnderecoLikeIgnoreCase(@Nullable String descricao, @Nullable String endereco, Pageable pageable);

}
