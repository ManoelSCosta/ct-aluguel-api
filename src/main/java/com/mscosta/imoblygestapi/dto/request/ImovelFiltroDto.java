package com.mscosta.imoblygestapi.dto.request;

/**
 * Filtro de busca paginada de imóveis, preenchido a partir de query params.
 * Todos os campos são opcionais — daí os wrappers e os padrões abaixo.
 */
public record ImovelFiltroDto(
        String descricao,
        String logradouro,
        Integer page,
        Integer size
) {

    private static final int PAGINA_PADRAO = 0;
    private static final int TAMANHO_PADRAO = 20;
    private static final int TAMANHO_MAXIMO = 100;

    public int paginaOuPadrao() {
        return page == null || page < 0 ? PAGINA_PADRAO : page;
    }

    public int tamanhoOuPadrao() {
        if (size == null || size < 1) {
            return TAMANHO_PADRAO;
        }
        return Math.min(size, TAMANHO_MAXIMO);
    }
}
