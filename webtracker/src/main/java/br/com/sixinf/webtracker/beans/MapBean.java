/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Posicao;

/**
 * @author maicon
 *
 */
@ManagedBean(name="mapBean")
@ViewScoped
public class MapBean implements Serializable {  
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(MapBean.class);
	
	private MapModel simpleModel;  
  
    public MapBean() {  
        simpleModel = new DefaultMapModel();  
        
        /*//Shared coordinates  
        LatLng coord1 = new LatLng(-20.47945, -54.55727);  
        LatLng coord2 = new LatLng(-20.48804, -54.58822); 
        
        //Basic marker  
        simpleModel.addOverlay(new Marker(coord1, "Peter"));  
        simpleModel.addOverlay(new Marker(coord2, "Tayra")); */
        atualizaPosicaoInicial();
        //caminho();
    }  
  
    public MapModel getSimpleModel() {  
        return simpleModel;  
    }  
    
    public void atualizaPosicaoPet(Pet pet){
		Posicao posicao = TrackerDAO.getInstance().buscarUltimaPosicaoPet(pet);		
		for (Marker m : simpleModel.getMarkers()){
			if ( m.getTitle().equals(pet.getNome()) ){
				LatLng coord = new LatLng(
					posicao.getLatitudeDecimal().doubleValue(), 
					posicao.getLongitudeDecimal().doubleValue());
				m.setLatlng(coord);				
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute(
						"updateMarker('" + pet.getNome() + "','" + 
								posicao.getLatitudeDecimal() + "', '" + 
								posicao.getLongitudeDecimal() +"')");
			}
		}
		
	}
    
    public void updateAll(){
    	TrackerDAO dao = TrackerDAO.getInstance(); 
    	String usuario = TrackerHelper.getUsuarioSessao();
    	List<Pet> pets = dao.buscarPetsUsuario(usuario);
    	for (Pet pet : pets) {
    		Posicao posicao = dao.buscarUltimaPosicaoPet(pet);
    		if (posicao == null) // pet ainda não tem posição registrada.
    			return;
    		if (simpleModel.getMarkers().size() == 0){
    			LatLng coord = new LatLng(
    					posicao.getLatitudeDecimal().doubleValue(), 
    					posicao.getLongitudeDecimal().doubleValue());
    			Marker m = new Marker(coord, pet.getNome());
    			simpleModel.getMarkers().add(m);
    		}
    		
    		for (Marker m : simpleModel.getMarkers()){
    			if ( m.getTitle().equals(pet.getNome()) ){
    				LatLng coord = new LatLng(
    					posicao.getLatitudeDecimal().doubleValue(), 
    					posicao.getLongitudeDecimal().doubleValue());
    				m.setLatlng(coord);				
    				RequestContext context = RequestContext.getCurrentInstance();
    				context.execute(
    						"updateMarker('" + pet.getNome() + "','" + 
    								posicao.getLatitudeDecimal() + "', '" + 
    								posicao.getLongitudeDecimal() +"')");
    			}
    		}
    	}
	}
    
    public void atualizaPosicaoInicial(){
    	TrackerDAO dao = TrackerDAO.getInstance(); 
    	String usuario = TrackerHelper.getUsuarioSessao();
    	List<Pet> pets = dao.buscarPetsUsuario(usuario);
    	for (Pet pet : pets) {
    		Posicao posicao = dao.buscarUltimaPosicaoPet(pet);
    		if (posicao == null) // pet ainda não tem posição registrada
    			return;
    		Marker m = new Marker(
    				new LatLng(posicao.getLatitudeDecimal().doubleValue(), posicao.getLongitudeDecimal().doubleValue()), 
    				pet.getNome());
    		simpleModel.addOverlay(m);
    	}
    }
    
    public void caminho(Pet pet){
    	
    	try {
	    	TrackerDAO dao = TrackerDAO.getInstance();
	    	//Pet pet = dao.buscar(3L, Pet.class);
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	Date dataInicial = sdf.parse("18/01/2014 00:00:00");
	    	
	    	Date dataFinal = sdf.parse("18/01/2014 23:59:59");
	    	
	    	List<Posicao> posicoes = dao.buscarPosicoesPetIntervalo(pet, dataInicial, dataFinal);
	    	
	    	Polyline polyline = new Polyline();
	    	polyline.setStrokeColor("#FF9900");  
	        polyline.setStrokeOpacity(0.7);
	        polyline.setStrokeWeight(7);
	        
	    	for (Posicao p : posicoes) {
	    		LatLng coord = new LatLng(
	    				p.getLatitudeDecimal().doubleValue(), 
	    				p.getLongitudeDecimal().doubleValue());
	    		
	            polyline.getPaths().add(coord);
	    	}
	    	
	    	simpleModel.addOverlay(polyline);
	    		    	
	    	RequestContext context = RequestContext.getCurrentInstance();
	    	context.addCallbackParam("paths", new JSONArray(polyline.getPaths(), true).toString());
	    	
    	} catch (ParseException e) {
    		LOG.error("Erro ao traçar o caminho", e);
    	}
    	
    	/*//Shared coordinates  
        LatLng coord1 = new LatLng(36.879466, 30.667648);
        LatLng coord2 = new LatLng(36.883707, 30.689216);
        LatLng coord3 = new LatLng(36.879703, 30.706707);
        LatLng coord4 = new LatLng(36.885233, 30.702323);
        
        //Polyline  
        Polyline polyline = new Polyline();
        polyline.getPaths().add(coord1);
        polyline.getPaths().add(coord2);
        polyline.getPaths().add(coord3);
        polyline.getPaths().add(coord4);
        
        polyline.setStrokeWeight(10);
        polyline.setStrokeColor("#FF9900");  
        polyline.setStrokeOpacity(0.7);
        
        simpleModel.addOverlay(polyline);*/
    }
}  
