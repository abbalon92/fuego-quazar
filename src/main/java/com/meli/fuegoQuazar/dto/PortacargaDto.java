package com.meli.fuegoQuazar.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PortacargaDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private PosicionDto position;
	private String message;
	
	public PortacargaDto(PosicionDto position, String message) {
		super();
		this.position = position;
		this.message = message;
	}
	public PortacargaDto() {
	}
	
	
}
