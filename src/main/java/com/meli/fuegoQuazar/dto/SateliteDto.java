package com.meli.fuegoQuazar.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SateliteDto implements Serializable{

    private static final long serialVersionUID = 1L;
	private String name;
    private double distance;
    private List<String> message;
	
}
