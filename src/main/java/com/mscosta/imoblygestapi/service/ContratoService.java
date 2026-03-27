package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.repository.ContratoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }


    public boolean existsContratoById(long id) {
        return contratoRepository.findById(id).isPresent();
    }

    public boolean existsContratoByImovelId(long imovelId) {
        return contratoRepository.findByImovel_Id(imovelId).isPresent();
    }

    public boolean existsContratoByPessoaId(long pessoaId) {
        return existsContratoByInquilinoId(pessoaId) || existsContratoByLocadorId(pessoaId);
    }

    public boolean existsContratoByInquilinoId(long inquilinoId) {
        return contratoRepository.findByInquilino_Id(inquilinoId).isPresent();
    }

    public boolean existsContratoByLocadorId(long locadorId) {
        return contratoRepository.findByLocador_Id(locadorId).isPresent();
    }

}
