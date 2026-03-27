package com.mscosta.imoblygestapi.enums;
public enum EstadoCivilEnum
{
	SOLTEIRO("Solteiro"),
	CASADO("Casado"),
	DIVORCIADO("Divorciado"),
	VIUVO("Viúvo");
	private final String descricao;

	private EstadoCivilEnum(String descricao)
	{
		this.descricao = descricao;
	}
	public String getDescricao()
	{
		return descricao;
	}
}