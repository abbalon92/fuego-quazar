package com.meli.fuegoQuazar.service;

import java.util.List;

import com.meli.fuegoQuazar.dto.PosicionDto;
import com.meli.fuegoQuazar.dto.SateliteDto;

public interface LocationService {
	public PosicionDto getLocation(List<SateliteDto> satelites);
	public String getMessage(List<SateliteDto> satellites);
	
}
