package com.meli.fuegoQuazar.service;

import java.util.List;

import com.meli.fuegoQuazar.dto.PortacargaDto;
import com.meli.fuegoQuazar.dto.PosicionDto;
import com.meli.fuegoQuazar.dto.SateliteDto;

public interface LocationService {
	public PosicionDto getLocation(List<SateliteDto> satelites);
	public String getMessage(List<SateliteDto> satellites);
	public void saveSatellite(SateliteDto satellite);
	public PortacargaDto getPortaCargo();
	
}
