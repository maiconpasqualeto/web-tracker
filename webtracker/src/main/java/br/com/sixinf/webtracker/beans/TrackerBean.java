/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.StatusRegistro;
import br.com.sixinf.webtracker.entidades.Tracker;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="trackerBean")
@ViewScoped
public class TrackerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(TrackerBean.class);
	
	private Tracker tracker = new Pet();
	private Tracker trackerSelecionado;
	private List<Tracker> trackers = new ArrayList<Tracker>();

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
			
	public List<Tracker> getTrackers() {
		return trackers;
	}

	public void setTrackers(List<Tracker> trackers) {
		this.trackers = trackers;
	}

	public Tracker getTrackerSelecionado() {
		return trackerSelecionado;
	}

	public void setTrackerSelecionado(Tracker trackerSelecionado) {
		this.trackerSelecionado = trackerSelecionado;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void listaInformacoesInciciais(){
		trackers = TrackerDAO.getInstance().buscarTodosTrackersAtivos();
	}

	/**
	 * 
	 * @return
	 */
	public String salvarTracker(){
		try {
			if (tracker.getId() != null && tracker.getId() > 0L) { // alteração
				TrackerFacade.getInstance().salvarTrackerFabricante(tracker);				
			} else { // inclusão
				
				Pet p = TrackerDAO.getInstance().buscarPetPeloSerial(tracker.getNumeroSerie());
				if (p != null) {
					FacesMessage m = new FacesMessage(
							FacesMessage.SEVERITY_WARN, 
							"Já existe outro aparelho cadastrado com o mesmo serial",
							null);
					FacesContext.getCurrentInstance().addMessage(null, m);
					return null;
				}
				p = new Pet();
				p.setNumeroSerie(tracker.getNumeroSerie());
				p.setFabricanteTracker(tracker.getFabricanteTracker());
				p.setModeloTracker(tracker.getModeloTracker());
				p.setDataFabricacao(tracker.getDataFabricacao());
				p.setFabricanteTracker(tracker.getFabricanteTracker());
				p.setStatusRegistro(StatusRegistro.A.getChar());
				p.setDataRegistroTracker(new Date());
				
				TrackerFacade.getInstance().incluirPet(p);
			}
			
			tracker = new Pet();
			trackerSelecionado = null;
			
			listaInformacoesInciciais();
			
			FacesMessage m = new FacesMessage("Registro salvo com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, m);
			
		} catch (LoggerException e) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					e.getLocalizedMessage(), 
					e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param e
	 */
	public void onSelect(SelectEvent event){
		Tracker t = (Tracker) event.getObject();
		tracker.setId(t.getId());
		tracker.setNumeroSerie(t.getNumeroSerie());
		tracker.setFabricanteTracker(t.getFabricanteTracker());
		tracker.setModeloTracker(t.getModeloTracker());
		tracker.setDataFabricacao(t.getDataFabricacao());
	}
	
	public void deleteTracker(Tracker t) {
		try {
			if (t != null) {
				TrackerFacade.getInstance().exclusaoLogicaTrackerFabricante(t);
				
				tracker = new Pet();
				trackerSelecionado = null;
				
				listaInformacoesInciciais();
				
				FacesMessage m = new FacesMessage("Tracker excluído com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, m);
			}
		} catch (LoggerException e) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					e.getLocalizedMessage(), 
					e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
	}
}
