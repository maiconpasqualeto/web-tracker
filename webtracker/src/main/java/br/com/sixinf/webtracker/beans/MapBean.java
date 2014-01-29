/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
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
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="mapBean")
@ViewScoped
public class MapBean implements Serializable {  
	
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(MapBean.class);
	private static final String FIGURA_MARCADOR = "../images/dog_icon2.png";
	private static final String SOMBRA_MARCADOR = "../images/dog_icon2_shadow.png";
	
	private MapModel simpleModel;  
	private List<Pet> pets = new ArrayList<Pet>();
	private Date dataInicioCaminho;
	private Date dataFimCaminho;
	private Pet petCaminho;
	private String nomePetMarcado;
	private String center;
  
    public MapBean() {  
        simpleModel = new DefaultMapModel();  
    }  
  
    public MapModel getSimpleModel() {  
        return simpleModel;  
    }  
    
    /**
	 * 
	 */
	@PostConstruct
	public void listaInformacoesInciciais(){
		String usuario = TrackerHelper.getUsuarioSessao();
		pets = TrackerDAO.getInstance().buscarPetsUsuario(usuario);
		atualizaPosicaoInicial();
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
    			continue;
    		if ( !encontrouMarker(pet) ){
    			LatLng coord = new LatLng(
    					posicao.getLatitudeDecimal().doubleValue(), 
    					posicao.getLongitudeDecimal().doubleValue());
    			Marker m = new Marker(coord, pet.getNome());
    			m.setIcon(FIGURA_MARCADOR);
        		m.setShadow(SOMBRA_MARCADOR);
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
    
    public boolean encontrouMarker(Pet pet) {
    	boolean encontrou = false;
    	for (Marker m : simpleModel.getMarkers()){
    		if ( m.getTitle().equals(pet.getNome()) ) {
    			encontrou = true;
    			break;
    		}
    	}
    	return encontrou;
    }
    
    public void atualizaPosicaoInicial(){
    	TrackerDAO dao = TrackerDAO.getInstance(); 
    	String usuario = TrackerHelper.getUsuarioSessao();
    	List<Pet> pets = dao.buscarPetsUsuario(usuario);
    	int i = 0;
    	for (Pet pet : pets) {
    		Posicao posicao = dao.buscarUltimaPosicaoPet(pet);
    		if (posicao == null) // pet ainda não tem posição registrada
    			continue;
    		// centraliza mapa no primeiro pet
    		if (i++ == 0)
    			center = posicao.getLatitudeDecimal().doubleValue() + "," + 
    					posicao.getLongitudeDecimal().doubleValue();
    		
    		Marker m = new Marker(
    				new LatLng(posicao.getLatitudeDecimal().doubleValue(), posicao.getLongitudeDecimal().doubleValue()), 
    				pet.getNome());
    		m.setIcon(FIGURA_MARCADOR);
    		m.setShadow(SOMBRA_MARCADOR);
    		simpleModel.addOverlay(m);
    	}
    }
    
    public void caminho(){
    	
    	//try {
	    	TrackerFacade facade = TrackerFacade.getInstance();
	    	
	    	
	    	List<Posicao> posicoes = facade.buscarPosicoesPetIntervalo(petCaminho, dataInicioCaminho, dataFimCaminho);
	    	
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
	    	
    	/*} catch (ParseException e) {
    		LOG.error("Erro ao traçar o caminho", e);
    	}*/
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
    	Marker marker = (Marker) event.getOverlay();
    	nomePetMarcado = marker.getTitle();
    }

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Date getDataInicioCaminho() {
		return dataInicioCaminho;
	}

	public void setDataInicioCaminho(Date dataInicioCaminho) {
		this.dataInicioCaminho = dataInicioCaminho;
	}

	public Date getDataFimCaminho() {
		return dataFimCaminho;
	}

	public void setDataFimCaminho(Date dataFimCaminho) {
		this.dataFimCaminho = dataFimCaminho;
	}

	public Pet getPetCaminho() {
		return petCaminho;
	}

	public void setPetCaminho(Pet petCaminho) {
		this.petCaminho = petCaminho;
	}
	
	public void atualizaPetCaminho(Pet pet) {
		this.petCaminho = pet;
	}

	public String getNomePetMarcado() {
		return nomePetMarcado;
	}

	public void setNomePetMarcado(String nomePetMarcado) {
		this.nomePetMarcado = nomePetMarcado;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}
	
}  
