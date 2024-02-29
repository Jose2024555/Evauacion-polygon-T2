package com.cibertec.assessment.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.assessment.beans.PolygonBean;
import com.cibertec.assessment.beans.SquareBean;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.repo.SquareRepo;
import com.cibertec.assessment.service.PolygonService;
import com.cibertec.assessment.service.SquareService;

@Service
public class SquareServiceImpl implements SquareService{

	@Autowired 
	SquareRepo squareRepo;
	
	@Autowired
	PolygonService polygonService;
	
	//Al momento de crear se debe validar si 
	//alguno de parte del cuadrado se encuentra dentro de algun
	//poligono y de ser asi se debe capturar el id de los poligonos y 
	//guardar como un string pero con formato de array
	//Ejemplo polygons = "["1","2"]"
	//Se guardan los ids correspondites
	//usar los metodos ya existentes para listar polygonos
	
    @Override
    public void create(Square s) {
    	 // Aquí se realiza la validación si el cuadrado se encuentra dentro de algún polígono
        List<String> intersectedPolygonIds = findIntersectedPolygonIds(s);

        // Se guarda el resultado como un string con formato de array
       //s.setPolygons(convertToFormattedArray(intersectedPolygonIds));
       
       squareRepo.save(s);
        
     // Convierte la lista de IDs en un string con formato de array
        String polygons = formatPolygonIds(intersectedPolygonIds);
        
        // Asigna el string de IDs al cuadrado
        s.setPolygons(polygons);
      
        
    }

    
    @Override
    public void create(List<Square> lp) {
    	squareRepo.saveAll(lp);
      }
    
	@Override
	public Square add(Square s) {
		// TODO Auto-generated method stub
		return squareRepo.save(s);
	}
	



    @Override
    public List<SquareBean> list() {
    	List<Square> list = squareRepo.findAll();
		List<SquareBean> listSquareBean = new ArrayList<>();
		list.forEach(p -> {
			Integer[] intArrayX = new Integer[5];
			Integer[] intArrayY = new Integer[5];

			convertStringInIntegerArray(p.getXPoints(), p.getYPoints(), intArrayX, intArrayY);

			SquareBean squareBean = new SquareBean();
			squareBean.setId(p.getId());
			squareBean.setName(p.getName());
			squareBean.setXPoints(intArrayX);
			squareBean.setYPoints(intArrayY);
			squareBean.setNpoints(p.getNpoints());

			listSquareBean.add(squareBean);
		});

		return listSquareBean;
	}

	private void convertStringInIntegerArray(String xPoints, String yPoints, Integer[] intArrayX, Integer[] intArrayY) {

		if (xPoints == null || yPoints == null) {
	        // Manejar el caso cuando xPoints o yPoints son null
	        return;
	    }
		
		String cleanedXPoints = xPoints.substring(1, xPoints.length() - 1);
		String cleanedYPoints = yPoints.substring(1, yPoints.length() - 1);

		// Split the string by commas
		String[] partsX = cleanedXPoints.split(", ");
		String[] partsY = cleanedYPoints.split(", ");

		for (int i = 0; i < partsX.length; i++) {
			intArrayX[i] = Integer.parseInt(partsX[i]);
		}
		
		for (int i = 0; i < partsY.length; i++) {
			intArrayY[i] = Integer.parseInt(partsY[i]);
		}
	}
	
	// Método para encontrar los IDs de los polígonos que intersectan con el cuadrado
	private List<String> findIntersectedPolygonIds(Square s) {
	    // Obtiene la lista de polígonos existentes
	    List<PolygonBean> polygons = polygonService.list();
	    
	    // Filtra los polígonos que intersectan con el cuadrado y mapea sus IDs
	    return polygons.stream()
	            .filter(polygon -> isSquareInsidePolygon(s, polygon))
	            .map(polygon -> polygon.getId().toString())
	            .collect(Collectors.toList());
	}

	// Convierte una lista de IDs en un string con formato de array
	private String formatPolygonIds(List<String> ids) {
	    return "[" + String.join(",", ids) + "]";
	}

  
    private boolean isSquareInsidePolygon(Square s, PolygonBean polygon) {
        int numIntersections = 0;
        Integer[] xPoints = polygon.getXPoints();
        Integer[] yPoints = polygon.getYPoints();
        
        // Obtener las coordenadas del cuadrado
        double x = Double.parseDouble(s.getXPoints()); // Coordenada x del punto superior izquierdo del cuadrado
        double y = Double.parseDouble(s.getYPoints()); // Coordenada y del punto superior izquierdo del cuadrado
        
        // Iterar sobre los lados del polígono
        for (int i = 0, j = xPoints.length - 1; i < xPoints.length; j = i++) {
            double xi = xPoints[i];
            double yi = yPoints[i];
            double xj = xPoints[j];
            double yj = yPoints[j];

         // Iterar sobre los lados del cuadrado
            for (int k = 0, l = xPoints.length - 1; k < xPoints.length; k++, l--) {
                double xk = xPoints[k].doubleValue();
                double yk = yPoints[k].doubleValue();
                double xl = xPoints[l].doubleValue();
                double yl = yPoints[l].doubleValue();

                // Comprobar si hay intersección entre los lados del polígono y los del cuadrado
                if (doIntersect(xi, yi, xj, yj, xk, yk, xl, yl)) {
                    numIntersections++;
                }
            }
        }

        // Si el número de intersecciones es impar, el cuadrado está dentro del polígono
        return numIntersections % 2 != 0;
    }
//
    // Método para comprobar si dos segmentos de línea se intersectan
    private boolean doIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double o1 = orientation(x1, y1, x2, y2, x3, y3);
        double o2 = orientation(x1, y1, x2, y2, x4, y4);
        double o3 = orientation(x3, y3, x4, y4, x1, y1);
        double o4 = orientation(x3, y3, x4, y4, x2, y2);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        return false;
    }

    // Método para obtener la orientación de tres puntos (p, q, r)
    private double orientation(double x1, double y1, double x2, double y2, double x3, double y3) {
        double val = (y2 - y1) * (x3 - x2) - (x2 - x1) * (y3 - y2);
        if (val == 0) {
            return 0;  // Colineales
        }
        return (val > 0) ? 1 : 2; // En sentido horario o antihorario
    }

    // Método para convertir una lista de IDs en un string con formato de array
    private String convertToFormattedArray(List<String> ids) {
        StringBuilder formattedArray = new StringBuilder("[");
        for (int i = 0; i < ids.size(); i++) {
            formattedArray.append("\"").append(ids.get(i)).append("\"");
            if (i < ids.size() - 1) {
                formattedArray.append(",");
            }
        }
        formattedArray.append("]");
        return formattedArray.toString();
    }



}