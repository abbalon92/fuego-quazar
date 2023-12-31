package com.meli.fuegoQuazar.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.fuegoQuazar.dto.PortacargaDto;
import com.meli.fuegoQuazar.dto.PosicionDto;
import com.meli.fuegoQuazar.dto.SateliteDto;
import com.meli.fuegoQuazar.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {
	
	private static Logger LOG = LogManager.getLogger(LocationServiceImpl.class);

	final static double EPSILON = 10;
	final static int[] kenobi = new int[] { -500, -200 };
	final static int[] skywalker = new int[] { 100, -100 };
	final static int[] sato = new int[] { 500, 100 };

	public static final Map<String, SateliteDto> satellitesInMemory = new HashMap<String, SateliteDto>();

	/**
	 * Metodo que retona las coordenadas de la nave en base a las 3 dstancias de las
	 * naves Metodo tomado del repositorio
	 * https://github.com/jsovalles/spring-boot-quasar-mission/blob/master/src/main/java/com/mercadolibre/quasar/utils/FunctionUtils.java
	 * 
	 * @param satellites
	 * @return Position objeto con las coordenadas de la nave
	 */
	@Override
	public PosicionDto getLocation(List<SateliteDto> satelites) {
		PosicionDto position = new PosicionDto();
		int cont = 0;
		double[] x = new double[3], y = new double[3], r = new double[3];

		for (SateliteDto satellite : satelites) {
			if (satellite.getName().equalsIgnoreCase("kenobi")) {
				r[cont] = satellite.getDistance(); // 100
				x[cont] = kenobi[0]; // -500
				y[cont] = kenobi[1]; // -200
			} else if (satellite.getName().equalsIgnoreCase("skywalker")) {
				r[cont] = satellite.getDistance();// 115.5
				x[cont] = skywalker[0]; // 100
				y[cont] = skywalker[1]; // -100
			} else {
				r[cont] = satellite.getDistance();
				x[cont] = sato[0];// 500
				y[cont] = sato[1];// 100
			}
			cont = cont + 1;
		}

		double a, dx, dy, d, h, rx, ry;
		double point2_x, point2_y;

		/*
		 * dx and dy are the vertical and horizontal distances between the circle
		 * centers.
		 */
		dx = x[1] - x[0];// -400
		dy = y[1] - y[0];// -300

		/* Determine the straight-line distance between the centers. */
		d = Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));// raiz(90000+160000)=500

		/* Check for solvability. */
		if (d > (r[0] + r[1])) {
			/* no solution. circles do not intersect. */
			LOG.error("Sin solucion, ambos satelites se intersectan");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sin solucion, ambos satelites se intersectan");
		}
		if (d < Math.abs(r[0] - r[1])) {
			/* no solution. one circle is contained in the other */
			LOG.error("no solution, one satellite is contained in the other");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"no solution, one satellite is contained in the other");
		}

		/*
		 * 'point 2' is the point where the line through the circle intersection points
		 * crosses the line between the circle centers.
		 */

		/* Determine the distance from point 0 to point 2. */
		a = (Math.pow(r[0], 2) - Math.pow(r[1], 2) + (Math.pow(d, 2))) / (2.0 * d);

		/* Determine the coordinates of point 2. */
		point2_x = x[0] + (dx * a / d);
		point2_y = y[0] + (dy * a / d);

		/*
		 * Determine the distance from point 2 to either of the intersection points.
		 */
		h = Math.sqrt(Math.pow(r[0], 2) - Math.pow(a, 2));

		/*
		 * Now determine the offsets of the intersection points from point 2.
		 */
		rx = -dy * (h / d);
		ry = dx * (h / d);

		/* Determine the absolute intersection points. */
		double intersectionPoint1_x = point2_x + rx;
		double intersectionPoint2_x = point2_x - rx;
		double intersectionPoint1_y = point2_y + ry;
		double intersectionPoint2_y = point2_y - ry;

		/*
		 * Lets determine if circle 3 intersects at either of the above intersection
		 * points.
		 */
		dx = intersectionPoint1_x - x[2];
		dy = intersectionPoint1_y - y[2];
		double d1 = Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));

		dx = intersectionPoint2_x - x[2];
		dy = intersectionPoint2_y - y[2];
		double d2 = Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));

		if (Math.abs(d1 - r[2]) < EPSILON) {
			position.setX(intersectionPoint1_x);
			position.setY(intersectionPoint1_y);
		} else if (Math.abs(d2 - r[2]) < EPSILON) {
			position.setX(intersectionPoint2_x);
			position.setY(intersectionPoint2_y);
		} else {
			LOG.error("no solution, no satellite can intersect to a common point");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"no solution, no satellite can intersect to a common point");
		}

		return position;
	}

	/**
	 * Metodo de validacion de los textos enviados por los satelites
	 * 
	 * @param mensajesActuales arreglo acumulativo
	 * @param mensajesNuevos   arreglo con la nueva cadena a acumular
	 * @return metodo que retonar el mensaje unificado
	 */
	@Override
	public String getMessage(List<SateliteDto> satellites) {
		List<String> nuevaTrama = new ArrayList<String>();
		for (SateliteDto satelite : satellites) {
			nuevaTrama = getMessageUtil(nuevaTrama, satelite.getMessage());
		}
		return nuevaTrama.stream().collect(Collectors.joining(" "));
	}

	/**
	 * Metodo que realiza la unificacion de los arreglos para generar uno unificado
	 * segun la demanda
	 * 
	 * @param mensajesActuales arreglo cumulativo que lleva la unificacion
	 * @param mensajesNuevos   arreglo con las nuevas cadenas
	 * @return arreglo unificado entre el acumulado y el nuevo
	 */
	private List<String> getMessageUtil(List<String> mensajesActuales, List<String> mensajesNuevos) {
		int index = 0;
		if (mensajesActuales == null || mensajesActuales.isEmpty()) {
			return mensajesNuevos;
		}
		for (String mensaje : mensajesNuevos) {
			if (mensajesActuales.get(index).isEmpty() && !mensaje.isEmpty()) {
				try {
					mensajesActuales.set(index, mensaje);
				} catch (IndexOutOfBoundsException e) {
					mensajesActuales.add(index, mensaje);
				}
			}
			index++;
		}
		return mensajesActuales;
	}

	/**
	 * Metodo de persistencia local de los satelites para efectos del ejemplo se persiste en una variable global sin embargo se podria persistir sobre una base de datos
	 */
	@Override
	public void saveSatellite(SateliteDto satellite) {
		if (!satellite.getName().equalsIgnoreCase("kenobi") && !satellite.getName().equalsIgnoreCase("skywalker") && !satellite.getName().equalsIgnoreCase("sato")) {
			LOG.error("Satelite no valido");
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		} else {
			satellitesInMemory.put(satellite.getName(), satellite);
		}
	}
	
	/**
	 * Metodo de obtencion de ubicacion del portacargo con la informacion registrada, siempre y cuando existan los datos necesarios
	 * @return PortacargaDto objeto con la informacion de la ubicacion y mensaje del portacargo
	 */
	@Override
	public PortacargaDto getPortaCargo() {
		if(satellitesInMemory.containsKey("kenobi") && satellitesInMemory.containsKey("skywalker") && satellitesInMemory.containsKey("sato")) {
			List<SateliteDto> satelites=new ArrayList<SateliteDto>();
			satellitesInMemory.forEach((k,v)->satelites.add(v));
			PosicionDto posicion=getLocation(satelites);
			String mensaje=getMessage(satelites);
			return new PortacargaDto(posicion, mensaje);
		}else {
			LOG.error("No contamos con la informacion completa para realizar el calculo de la ubicacion del portacargo");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

}
