/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.event.FlowEvent;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.SegurancaDAO;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.facade.SegurancaFacade;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="segurancaBean")
@ViewScoped
public class SegurancaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(SegurancaBean.class);
	
	private Usuario usuario = new Usuario();
	private Pet pet = new Pet();
	private Endereco endereco = new Endereco();
	private String confirmaSenha;
	
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

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	/**
	 * 
	 * @return
	 */
	public String logar(){
		
		boolean valido = SegurancaFacade.getInstance().logar(
				usuario.getNomeUsuario(), usuario.getSenha());
		
		if (valido) {
			HttpSession sess = TrackerHelper.getSession();
			sess.setAttribute(Usuario.SESSION_ID, usuario.getNomeUsuario());
			
			return "/pages/principal.xhtml?faces-redirect=true";
		} else {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					"Usuário ou senha inválidos.", 
					"Usuário ou senha inválidos.");
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String logoff(){
		TrackerHelper.getSession().invalidate();
		
		return "/pages/login.xhtml?faces-redirect=true";
	}
			
	/**
	 * 
	 * @return
	 */
	public String registrar(){
		return "pages/autocadastro.xhtml";
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) { 
        return event.getNewStep();
    }  
	
	public void buscarCEP(ValueChangeEvent event){
		String cep = event.getNewValue().toString();
		InputStream is = null;
		HttpURLConnection connection = null;
		
		try {
			// Objeto URL
			URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?formato=json&cep=" + cep);
			
			try {
			
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("Request-Method", "GET");
				connection.setDoInput(true);
				connection.setDoOutput(false);
				connection.connect();
				
				is = connection.getInputStream();
				byte[] buf = Utilitarios.fazLeituraStreamEmByteArray(is);
				JSONObject jo = new JSONObject(new String(buf));
				String resultado = jo.getString("resultado");
				if (resultado.equals("1")) { // cep completo
					String uf = jo.getString("uf");
					String municipio = jo.getString("cidade");
					String tipoLogradouro = jo.getString("tipo_logradouro");
					String logradouro = jo.getString("logradouro");
					String bairro = jo.getString("bairro");
					this.endereco.setUf(uf);
					this.endereco.setMunicipio(municipio);
					this.endereco.setLogradouro(tipoLogradouro + " " + logradouro);
					this.endereco.setBairro(bairro);
				} else 
					if (resultado.equals("2")) { // cep único
						String uf = jo.getString("uf");
						String municipio = jo.getString("cidade");
						this.endereco.setUf(uf);
						this.endereco.setMunicipio(municipio);
					}
				
			} finally {
				if (is != null)
					is.close();
			}
			
		} catch (IOException | JSONException e) {
			LOG.error("Erro ao buscar CEP", e);
		}
	}
	
	/**
	 * Faz as validações e grava o auto cadastro
	 * 
	 * @return
	 */
	public String salvarAutocadastro(){
		String retorno = "";
			
		try {
			
			validaAutoCadastro();
			
			Pet p = TrackerDAO.getInstance().buscarTodosPetsPeloSerial(pet.getNumeroSerie());
			if (p == null) {
				throw new LoggerException(
						"Aparelho ainda não cadastrado, entre em contato com o fornecedor", LOG);
			}
			p.setNome(pet.getNome());
			p.setRaca(pet.getRaca());
			p.setDataNascimento(pet.getDataNascimento());
			
			TrackerFacade.getInstance().salvarAutoCadastro(usuario, endereco, p);
			
			HttpSession sess = TrackerHelper.getSession();
			sess.setAttribute(Usuario.SESSION_ID, usuario.getNomeUsuario());
			
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
	
	private void validaAutoCadastro() throws LoggerException{
		Usuario u = SegurancaDAO.getInstance().buscarUsuario(usuario.getNomeUsuario());
		if (u != null) {
			throw new LoggerException(
					"Já existe usuário com esse nome.", LOG);
		}
	}
	
	public String getUsuarioSessao(){
		return TrackerHelper.getUsuarioSessao();
	}
	
	public String esqueceuSenha(){
		return "";
	}

}
