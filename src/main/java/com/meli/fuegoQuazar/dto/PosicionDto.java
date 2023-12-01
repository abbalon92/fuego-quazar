package com.meli.fuegoQuazar.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PosicionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Double x;
	private Double y;
}
