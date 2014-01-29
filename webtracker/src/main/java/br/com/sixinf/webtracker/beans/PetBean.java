/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Plano;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="petBean")
@ViewScoped
public class PetBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nomePlano;
	private boolean renderSerial;
	private List<Plano> planos = new ArrayList<Plano>();
	private Plano plano;
	private List<Pet> pets = new ArrayList<Pet>();
	
	private Pet pet = new Pet();
	private Pet petSelecionado;
	private String qrcode;
	
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Pet getPetSelecionado() {
		return petSelecionado;
	}

	public void setPetSelecionado(Pet petSelecionado) {
		this.petSelecionado = petSelecionado;
	}

	public String getNomePlano() {
		return nomePlano;
	}

	public void setNomePlano(String nomePlano) {
		this.nomePlano = nomePlano;
	}

	public boolean isRenderSerial() {
		return renderSerial;
	}

	public void setRenderSerial(boolean renderSerial) {
		this.renderSerial = renderSerial;
	}

	public List<Plano> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void listaInformacoesInciciais(){
		String usuario = TrackerHelper.getUsuarioSessao();
		pets = TrackerDAO.getInstance().buscarPetsUsuario(usuario);
		
		planos = TrackerFacade.getInstance().buscarTodosPlanos();
		plano = planos.get(0);
		nomePlano = plano.getNomePlano();
	}
	
	
	public String validaESalvaPet() {
		if (pet.getId() == null || pet.getId().equals(0)) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.addCallbackParam("abreDialog", true);
			return null;
		}
		
		return salvarPet();
	}
	
	/**
	 * 
	 * @return
	 */
	public String salvarPet(){
		try {
			
			Pet p = null;
			
			if ( "ATIVO".equals(plano.getTipoTrackerPlano()) ) {
				
				p = TrackerDAO.getInstance().buscarPetPeloSerial(pet.getNumeroSerie());
				
				if (p == null) {
					FacesMessage m = new FacesMessage(
							FacesMessage.SEVERITY_WARN, 
							"Aparelho ainda não cadastrado, entre em contato com o fornecedor", null);
					FacesContext.getCurrentInstance().addMessage(null, m);
					return null;
				}
				
				// se for do tipo ATIVO atualiza o número de série
				p.setNumeroSerie(pet.getNumeroSerie());
				
			} else { // PASSIVO - QR-code
				
				if (pet.getId() == null || pet.getId().equals(0)) {
					Date dataAtual = new Date();
					p = new Pet();
					p.setDataAtivacaoTracker(dataAtual);
					p.setDataRegistroTracker(dataAtual);
					p.setNumeroSerie(UUID.randomUUID().toString());
					p.setStatusRegistro('A');
					p.setTipoTracker(plano.getTipoTrackerPlano());
					p.setModeloTracker("QR-code");
				} else {
					p = TrackerDAO.getInstance().buscar(pet.getId(), Pet.class);					
				}
				
			}
			
			p.setNome(pet.getNome());
			p.setRaca(pet.getRaca());
			p.setDataNascimento(pet.getDataNascimento());
			
			
			String nomeUsuario = TrackerHelper.getUsuarioSessao();
			if ( nomeUsuario == null ){
				FacesMessage m = new FacesMessage(
						FacesMessage.SEVERITY_WARN, 
						"Sua sessão expirou. Favor logar novamente!", null);
				FacesContext.getCurrentInstance().addMessage(null, m);
				return null;
			}
			
			TrackerFacade.getInstance().salvarPetUsuario(nomeUsuario, p, plano);
			
			pet = new Pet();
			petSelecionado = null;
			qrcode = null;
			
			listaInformacoesInciciais();
			
			FacesMessage m = new FacesMessage("Pet salvo com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, m);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.addCallbackParam("fechaDialog", true);
			
		} catch (LoggerException e) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					e.getLocalizedMessage(), 
					e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
		
		return null;
	}
	
	
	public void onSelect(SelectEvent event) {
		Pet p = (Pet) event.getObject();
		pet.setId(p.getId());
		pet.setNome(p.getNome());
		pet.setRaca(p.getRaca());
		pet.setDataNascimento(p.getDataNascimento());
		pet.setNumeroSerie(p.getNumeroSerie());
		Plano plano = TrackerFacade.getInstance().buscarPlanoTracker(pet);
		Usuario u = TrackerFacade.getInstance().buscarUsuarioTracker(pet);
		
		if (plano != null && !plano.getNomePlano().equals("Plano Prata"))
			qrcode = "Nome pet: " + pet.getNome() + 
			" - Dono: " + u.getNome() + 
			" - Celular: " + u.getFoneCelularMask();
	}
	
	public void deletePet(Pet p) {
		try {
			if (p != null) {
				p.setNome(null);
				p.setDataNascimento(null);
				p.setRaca(null);
				TrackerDAO.getInstance().alterarPet(p);
				
				listaInformacoesInciciais();
				
				FacesMessage m = new FacesMessage("Pet excluído com sucesso!");
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
	
	/**
	 * 
	 * @param event
	 */
	public void planoChange() {
		if ("Plano Alternativo".equals(nomePlano)) 
			renderSerial = false;
		else 
			renderSerial = true;
		
		for (Plano p : planos) {
			if (p.getNomePlano().equals(nomePlano)){				
				plano = p;
				break;
			}
		}
	}
	
	/**
	 * 
	 */
	public void reset(){
		nomePlano = null;
		plano = null;
		renderSerial = false;
		pet = new Pet();
		petSelecionado = null;
		qrcode = null;
	}
	
	/**
	 * 
	 */
	public void fechaAba(){
		renderSerial = false;
	}
	
}
