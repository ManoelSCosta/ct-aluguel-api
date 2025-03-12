package com.mscosta.ctaluguelapi.repository;

import com.mscosta.ctaluguelapi.dto.response.CasaResponseDto;
import com.mscosta.ctaluguelapi.model.Casa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CasaRepository extends JpaRepository<Casa, Long> {
    Page<Casa> findByDescricaoLikeIgnoreCaseAndEnderecoLikeIgnoreCase(@Nullable String descricao, @Nullable String endereco, Pageable pageable);


    Optional<Casa> findByIdOrDescricaoLikeIgnoreCaseOrEnderecoLikeIgnoreCase(long id, @Nullable String descricao, @Nullable String endereco);

}
