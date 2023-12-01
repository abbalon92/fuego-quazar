package com.meli.fuegoQuazar.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.meli.fuegoQuazar.dto.PortacargaDto;
import com.meli.fuegoQuazar.dto.PosicionDto;
import com.meli.fuegoQuazar.dto.SateliteDto;
import com.meli.fuegoQuazar.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService{
	
	
    final static double EPSILON = 10;
    final static int[] kenobi = new int[]{-500, -200};
    final static int[] skywalker = new int[]{100, -100};
    final static int[] sato = new int[]{500, 100};

	@Override
	public PortacargaDto getLocation(List<SateliteDto> satellites) {
		
		return new PortacargaDto(getLocationUtil(satellites),"");
	}


	@Override
	public String getMessage(List<SateliteDto> satellites) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Metodo que retona las coordenadas de la nave en base a las 3 dstancias de las naves
	 * Metodo tomado del repositorio https://github.com/jsovalles/spring-boot-quasar-mission/blob/master/src/main/java/com/mercadolibre/quasar/utils/FunctionUtils.java
	 * @param satellites
	 * @return Position objeto con las coordenadas de la nave
	 */
    private PosicionDto getLocationUtil(List<SateliteDto> satellites) {

    	PosicionDto position = new PosicionDto();
        int cont = 0;
        double[] x = new double[3], y = new double[3], r = new double[3];

        for (SateliteDto satellite : satellites) {
            if (satellite.getName().equalsIgnoreCase("kenobi")) {
                r[cont] = satellite.getDistance();  //100
                x[cont] = kenobi[0]; //-500
                y[cont] = kenobi[1]; //-200
            } else if (satellite.getName().equalsIgnoreCase("skywalker")) {
                r[cont] = satellite.getDistance();//115.5
                x[cont] = skywalker[0]; //100
                y[cont] = skywalker[1]; //-100
            } else {
                r[cont] = satellite.getDistance();//115.5
                x[cont] = sato[0];//500
                y[cont] = sato[1];//100
            }
            cont = cont+1;
        }

        double a, dx, dy, d, h, rx, ry;
        double point2_x, point2_y;

        /* dx and dy are the vertical and horizontal distances between
         * the circle centers.
         */
        dx = x[1] - x[0];//-400
        dy = y[1] - y[0];//-300

        /* Determine the straight-line distance between the centers. */
        d = Math.sqrt(Math.pow(dy,2) + Math.pow(dx,2));//raiz(90000+160000)=500

        /* Check for solvability. */
        if (d > (r[0] + r[1])) {
            /* no solution. circles do not intersect. */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no solution, both satellites do not intersect");
        }
        if (d < Math.abs(r[0] - r[1])) {
            /* no solution. one circle is contained in the other */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no solution, one satellite is contained in the other");
        }

        /* 'point 2' is the point where the line through the circle
         * intersection points crosses the line between the circle
         * centers.
         */

        /* Determine the distance from point 0 to point 2. */
        a = (Math.pow(r[0],2)- Math.pow(r[1],2) + (Math.pow(d,2))) / (2.0 * d);

        /* Determine the coordinates of point 2. */
        point2_x = x[0] + (dx * a / d);
        point2_y = y[0] + (dy * a / d);

        /* Determine the distance from point 2 to either of the
         * intersection points.
         */
        h = Math.sqrt(Math.pow(r[0],2) - Math.pow(a,2));

        /* Now determine the offsets of the intersection points from
         * point 2.
         */
        rx = -dy * (h / d);
        ry = dx * (h / d);

        /* Determine the absolute intersection points. */
        double intersectionPoint1_x = point2_x + rx;
        double intersectionPoint2_x = point2_x - rx;
        double intersectionPoint1_y = point2_y + ry;
        double intersectionPoint2_y = point2_y - ry;

        //System.out.println("INTERSECTION Circle1 AND Circle2:" + "(" + intersectionPoint1_x + "," + intersectionPoint1_y + ")" + " AND (" + intersectionPoint2_x + "," + intersectionPoint2_y + ")");

        /* Lets determine if circle 3 intersects at either of the above intersection points. */
        dx = intersectionPoint1_x - x[2];
        dy = intersectionPoint1_y - y[2];
        double d1 = Math.sqrt(Math.pow(dy,2) + Math.pow(dx,2));

        dx = intersectionPoint2_x - x[2];
        dy = intersectionPoint2_y - y[2];
        double d2 = Math.sqrt(Math.pow(dy,2) + Math.pow(dx,2));

        if (Math.abs(d1 - r[2]) < EPSILON) {
            position.setX(intersectionPoint1_x);
            position.setY(intersectionPoint1_y);
        } else if (Math.abs(d2 - r[2]) < EPSILON) {
            position.setX(intersectionPoint2_x);
            position.setY(intersectionPoint2_y);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no solution, no satellite can intersect to a common point");
        }

        return position;
    }


}
