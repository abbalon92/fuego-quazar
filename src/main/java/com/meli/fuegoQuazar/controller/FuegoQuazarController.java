package com.meli.fuegoQuazar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meli.fuegoQuazar.dto.PortacargaDto;
import com.meli.fuegoQuazar.dto.SateliteDto;
import com.meli.fuegoQuazar.service.LocationService;

@RestController
@RequestMapping("/${controller.contextPath}")
public class FuegoQuazarController {
	
	@Autowired
	private LocationService locationService;
	
	/**
	 * Servicio que realiza el calculode la posicion estimada del portacargo en base a las distancias de los 3 satelites
	 * @param satellites listado de la informacion de los satelites
	 * @return retona objeto con la ubicacion estimada del satelite y el mensaje secreto
	 */
	@PostMapping("/topsecret")
	public ResponseEntity<PortacargaDto> getPortaCarga(@RequestBody List<SateliteDto> satellites){
			PortacargaDto portaCargo=new PortacargaDto();
			portaCargo.setPosition(locationService.getLocation(satellites));
			portaCargo.setMessage(locationService.getMessage(satellites));
			return new ResponseEntity<PortacargaDto>(portaCargo,HttpStatus.OK);
	}
	
	@PostMapping("/topsecret_split/{satellite_name}")
	public ResponseEntity<SateliteDto> getPortaCarga(@PathVariable String satellite_name ,@RequestBody SateliteDto satellite){
			satellite.setName(satellite_name);
			SateliteDto newSatellite=locationService.saveSatellite(satellite);
			return new ResponseEntity<SateliteDto>(newSatellite,HttpStatus.CREATED);
	}
	
	

}
