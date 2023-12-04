package com.meli.fuegoQuazar.sevice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.meli.fuegoQuazar.dto.PosicionDto;
import com.meli.fuegoQuazar.dto.SateliteDto;
import com.meli.fuegoQuazar.service.impl.LocationServiceImpl;


@SpringBootTest
class LocationServiceImplTest {

	@InjectMocks
	private LocationServiceImpl locationService;
	
	private List<SateliteDto> satelites;
	
	@BeforeEach
	public void setup(){
		satelites=new ArrayList<SateliteDto>();
		SateliteDto kenobi=new SateliteDto();
		kenobi.setName("kenobi");
		kenobi.setDistance(Double.valueOf("538.5164807"));
		kenobi.setMessage(Arrays.asList("este","","","mensaje","") );
		satelites.add(kenobi);
		SateliteDto skywalker=new SateliteDto();
		skywalker.setName("skywalker");
		skywalker.setDistance(Double.valueOf("565.6854249"));
		skywalker.setMessage(Arrays.asList("","es", "","","secreto"));
		satelites.add(skywalker);
		SateliteDto sato=new SateliteDto();
		sato.setName("sato");
		sato.setDistance(Double.valueOf("824.6211251"));
		sato.setMessage(Arrays.asList("este","","un","",""));
		satelites.add(sato);
		
	}
	
	@DisplayName("Test: Posicion del carguero")
	@Test
	void getLocationTest() {
		PosicionDto posicion=locationService.getLocation(satelites);
		assertEquals(Double.valueOf("-299.99999996060967"), posicion.getX());
		assertEquals(Double.valueOf("299.9999999697573"), posicion.getY());
	}
	
	@DisplayName("Test: Distancias invalidas")
	@Test
	void getLocationInvalidDistanceTest() {
		ResponseStatusException thrown =Assertions.assertThrows(ResponseStatusException.class, ()->{
			satelites.get(2).setDistance(Double.valueOf(1));
			locationService.getLocation(satelites);
		});
		Assertions.assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
	}
	
	@DisplayName("Test: Mensaje del carguero")
	@Test
	void getMessageTest() {
		String mensaje=locationService.getMessage(satelites);
		assertEquals("este es un mensaje secreto",mensaje );
	}
	
	

}
