/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FlowEvent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.SegurancaDAO;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Plano;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.facade.SegurancaFacade;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="autoCadastroBean")
@ViewScoped
public class AutoCadastroBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario = new Usuario();
	private Pet pet = new Pet();
	private Endereco endereco = new Endereco();
	private String nomePlano;
	private boolean renderSerial;
	private List<Plano> planos = new ArrayList<Plano>();
	private Plano plano;
		
	@ManagedProperty(value="#{segurancaBean}")
	private SegurancaBean segurancaBean;
	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public SegurancaBean getSegurancaBean() {
		return segurancaBean;
	}

	public void setSegurancaBean(SegurancaBean segurancaBean) {
		this.segurancaBean = segurancaBean;
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

	/**
	 * 
	 * @param event
	 */
	public void buscarCEP(ValueChangeEvent event){
		String cep = event.getNewValue().toString();
		
		Endereco end = TrackerFacade.getInstance().buscarCEP(cep);
		this.endereco.setUf(end.getUf());
		this.endereco.setMunicipio(end.getMunicipio());
		this.endereco.setLogradouro(end.getLogradouro());
		this.endereco.setBairro(end.getBairro());
	}
	
	
	/**
	 * Faz as validações e grava o auto cadastro
	 * 
	 * @return
	 */
	public String salvarAutocadastro(){
		String retorno = "";
			
		try {
			
			if (!validaAutoCadastro())
				return retorno;
			
			Pet p = TrackerDAO.getInstance().buscarPetPeloSerial(pet.getNumeroSerie());
			if (p == null) {
				FacesMessage m = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, 
						"Aparelho com número de série '" + pet.getNumeroSerie() + "' não cadastrado no sistema, entre em contato com o fornecedor.",
						null);
				FacesContext.getCurrentInstance().addMessage(null, m);
				return null;
			}
			p.setNome(pet.getNome());
			p.setRaca(pet.getRaca());
			p.setDataNascimento(pet.getDataNascimento());
			
			SegurancaFacade.getInstance().salvarAutoCadastro(usuario, endereco, p);
			
			HttpSession sess = TrackerHelper.getSession();
			sess.setAttribute(Usuario.SESSION_NOME_USUARIO, usuario.getNomeUsuario());
			sess.setAttribute(Usuario.SESSION_TIPO_USUARIO, usuario.getTipoUsuario());
			
			segurancaBean.setUsuario(usuario);
			segurancaBean.carregaRenderMenusUsuario();
			
			FacesMessage m = new FacesMessage("Cadastro efetuado com sucesso, redirecionando...");
			FacesContext.getCurrentInstance().addMessage(null, m);
			
			retorno = "/pages/principal.xhtml?faces-redirect=true";
					
		} catch (LoggerException e) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					e.getLocalizedMessage(), 
					e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
		return retorno;
		
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validaAutoCadastro() {
		Usuario u = SegurancaDAO.getInstance().buscarUsuario(usuario.getNomeUsuario());
		if (u != null) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_WARN, 
					"Já existe usuário com esse nome.",
					null);
			FacesContext.getCurrentInstance().addMessage(null, m);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) { 
        return event.getNewStep();
    }  
	
	public String redireciona(){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("usuario", usuario);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("pet", pet);
		
		return "/pages/autocadastro.xhtml?faces-redirect=true&includeViewParams=true";
	}
	
	@PostConstruct
	public void init(){
		Usuario u = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("usuario");		
		if (u != null) 
			this.usuario = u;
		
		Pet p = (Pet) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("pet");
		if (p != null)
			this.pet = p;
		
		planos = TrackerFacade.getInstance().buscarTodosPlanos();
		plano = planos.get(0);
		nomePlano = plano.getNomePlano();
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
	
}
