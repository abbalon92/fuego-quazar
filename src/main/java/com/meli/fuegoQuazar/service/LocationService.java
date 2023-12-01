package com.meli.fuegoQuazar.service;

import java.util.List;

import com.meli.fuegoQuazar.dto.PortacargaDto;
import com.meli.fuegoQuazar.dto.SateliteDto;

public interface LocationService {
	public PortacargaDto getLocation(List<SateliteDto> satellites);
	public String getMessage(List<SateliteDto> satellites);
	
}
