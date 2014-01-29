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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.SelectEvent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario = new Usuario();
	private Usuario usuarioSelecionado = null;
	private Endereco endereco = new Endereco();
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<String> tiposUsuario = new ArrayList<String>();
	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}
		
	public List<String> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<String> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void listaInformacoesInciciais(){
		TipoUsuario tipoUsuarioLogado = TrackerHelper.getTipoUsuarioSessao();
		
		usuarios = TrackerDAO.getInstance().buscarTodosUsuariosPerfil(tipoUsuarioLogado);
		
		if (tiposUsuario.isEmpty()) {
			switch(tipoUsuarioLogado) {
			case MASTER:
				tiposUsuario.add(TipoUsuario.MASTER.name());
				tiposUsuario.add(TipoUsuario.ADMIN.name());
				break;
			case ADMIN:
				tiposUsuario.add(TipoUsuario.ADMIN.name());
				break;
			case USER:
				break;			
			}
			
			tiposUsuario.add(TipoUsuario.USER.name());
		}
		
	}

	/**
	 * 
	 * @return
	 */
	public String salvarUsuario(){
		try {
			if (usuario.getId() != null && usuario.getId() > 0L) { // alteração
				TrackerFacade.getInstance().alterarCadastroUsuario(usuario, endereco);				
			} else { // inclusão
				
				TrackerFacade.getInstance().incluirCadastroUsuario(usuario, endereco);
			}
			
			usuario = new Usuario();
			endereco = new Endereco();
			usuarioSelecionado = null;
			
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
	 * @param event
	 */
	public void onSelect(SelectEvent event){
		Usuario u = (Usuario) event.getObject();
		
		usuario.setId(u.getId());
		usuario.setNomeUsuario(u.getNomeUsuario());
		usuario.setTipoUsuario(u.getTipoUsuario());
		usuario.setNome(u.getNome());
		usuario.setCpf(u.getCpf());
		usuario.setEmail(u.getEmail());
		usuario.setMelhorDiaVencimento(u.getMelhorDiaVencimento());
		usuario.setFoneCelular(u.getFoneCelular());
		usuario.setFoneContato(u.getFoneContato());
		usuario.setNomeUsuario(u.getNomeUsuario());		
		
		Endereco e =  TrackerDAO.getInstance().buscarEnderecoUsuario(u.getId());
		endereco.setId(e.getId());
		endereco.setBairro(e.getBairro());
		endereco.setCep(e.getCep());
		endereco.setComplemento(e.getComplemento());
		endereco.setLogradouro(e.getLogradouro());
		endereco.setMunicipio(e.getMunicipio());
		endereco.setNumero(e.getNumero());
		endereco.setUf(e.getUf());
		
		
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
	 * 
	 */
	public void reset(){
		usuario = new Usuario();
		endereco = new Endereco();
		usuarioSelecionado = null;
	}
	
	public void deleteUsuario(Usuario u) {
		try {
			
			TrackerFacade.getInstance().exclusaoLogicaGenerica(u.getId(), Usuario.class);
			usuario = new Usuario();
			usuarioSelecionado = null;
			
			listaInformacoesInciciais();
			
			FacesMessage m = new FacesMessage("Registro excluído com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, m);
			
		} catch (LoggerException e) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					e.getLocalizedMessage(), 
					e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
	}

}
