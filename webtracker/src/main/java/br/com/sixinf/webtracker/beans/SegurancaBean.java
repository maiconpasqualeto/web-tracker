/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.facade.SegurancaFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="segurancaBean")
@SessionScoped
public class SegurancaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(SegurancaBean.class);
	
	private Usuario usuario = new Usuario();
	/*private Pet pet = new Pet();
	private Endereco endereco = new Endereco();*/
	private String confirmaSenha;
	
	// permissoes
	public static List<String> permissoesMaster = new ArrayList<String>();
	public static List<String> permissoesAdmin = new ArrayList<String>();
	public static List<String> permissoesUser = new ArrayList<String>();
	
	// render menus
	private boolean renderRelatorio;
	private boolean renderCadastro;
	
	// reder submenus
	private boolean renderPrincipal;
	private boolean renderPet;
	private boolean renderTracker;
	private boolean renderUsuario;
	private boolean renderRelRastreamento;
	private boolean renderRelFinanceiro;
	private boolean renderSetup;
	
	static {
		// MASTER
		permissoesMaster.add("principal.xhtml");
		permissoesMaster.add("pet.xhtml");
		permissoesMaster.add("tracker.xhtml");
		permissoesMaster.add("usuario.xhtml");
		permissoesMaster.add("setup.xhtml");
		
		// ADMIN
		permissoesAdmin.add("principal.xhtml");
		permissoesAdmin.add("tracker.xhtml");
		permissoesAdmin.add("usuario.xhtml");
		
		// USUARIO
		permissoesUser.add("principal.xhtml");
		permissoesUser.add("pet.xhtml");
		
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	
	public boolean isRenderPrincipal() {
		return renderPrincipal;
	}

	public void setRenderPrincipal(boolean renderPrincipal) {
		this.renderPrincipal = renderPrincipal;
	}

	public boolean isRenderPet() {
		return renderPet;
	}

	public void setRenderPet(boolean renderPet) {
		this.renderPet = renderPet;
	}

	public boolean isRenderTracker() {
		return renderTracker;
	}

	public void setRenderTracker(boolean renderTracker) {
		this.renderTracker = renderTracker;
	}

	public boolean isRenderUsuario() {
		return renderUsuario;
	}

	public void setRenderUsuario(boolean renderUsuario) {
		this.renderUsuario = renderUsuario;
	}

	public boolean isRenderRelRastreamento() {
		return renderRelRastreamento;
	}

	public void setRenderRelRastreamento(boolean renderRelRastreamento) {
		this.renderRelRastreamento = renderRelRastreamento;
	}

	public boolean isRenderRelFinanceiro() {
		return renderRelFinanceiro;
	}

	public void setRenderRelFinanceiro(boolean renderRelFinanceiro) {
		this.renderRelFinanceiro = renderRelFinanceiro;
	}

	public boolean isRenderRelatorio() {
		return renderRelatorio;
	}

	public void setRenderRelatorio(boolean renderRelatorio) {
		this.renderRelatorio = renderRelatorio;
	}

	public boolean isRenderCadastro() {
		return renderCadastro;
	}

	public void setRenderCadastro(boolean renderCadastro) {
		this.renderCadastro = renderCadastro;
	}

	public boolean isRenderSetup() {
		return renderSetup;
	}

	public void setRenderSetup(boolean renderSetup) {
		this.renderSetup = renderSetup;
	}

	/**
	 * 
	 * @return
	 */
	public String logar(){
		
		SegurancaFacade.RetornoLogin retorno = SegurancaFacade.getInstance().logar(usuario);
		
		if (retorno.isValido()) {
			HttpSession sess = TrackerHelper.getSession();
			sess.setAttribute(Usuario.SESSION_NOME_USUARIO, usuario.getNomeUsuario());
			sess.setAttribute(Usuario.SESSION_TIPO_USUARIO, usuario.getTipoUsuario());
			
			carregaRenderMenusUsuario();
			
			return "/pages/principal.xhtml?faces-redirect=true";
		} else {
			if ("Definir Senha".equals(retorno.getMensagem())) {
				RequestContext context = RequestContext.getCurrentInstance();
				context.addCallbackParam("abreDialog", true);
				return null;
			}
				
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
		
		return "/pages/home.xhtml?faces-redirect=true";
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
	 * @return
	 */
	public String getUsuarioSessao(){
		return TrackerHelper.getUsuarioSessao();
	}
	
	/**
	 * 
	 * @return
	 */
	public String esqueceuSenha(){
		return "";
	}
	
	/**
	 * 
	 */
	public void carregaRenderMenusUsuario(){
		TipoUsuario t = TipoUsuario.valueOf(usuario.getTipoUsuario());
		switch (t){
		
		case MASTER:
			renderPrincipal = true;
			renderPet = true;
			renderTracker = true;
			renderUsuario = true;
			
			renderRelRastreamento = true;
			renderRelFinanceiro = true;	
			renderSetup = true;
			
			break;
			
		case ADMIN:
			renderPrincipal = true;			
			renderTracker = true;
			renderUsuario = true;
			
			renderPet = false;
			renderRelRastreamento = false;
			renderRelFinanceiro = false;
			renderSetup = false;
			break;
			
		case USER:
			renderPrincipal = true;
			renderPet = true;
			
			renderTracker = false;
			renderUsuario = false;
			renderRelRastreamento = false;
			renderRelFinanceiro = false;
			renderSetup = false;
			break;
			
		}
		
		// MENU PAI, não esquecer
		// se não houver nenhum submenu, não renderiza o menu pai
		if (!renderPet && 
				!renderUsuario &&
				!renderTracker)
			renderCadastro = false;
		else 
			renderCadastro = true;
		
		if (!renderRelFinanceiro &&
				!renderRelRastreamento)
			renderRelatorio = false;
		else 
			renderRelatorio = true;
		
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException
	 */
	public String salvarNovaSenha() throws LoggerException{		
		
		SegurancaFacade.getInstance().salvarNovaSenha(usuario);
		
		FacesMessage m = new FacesMessage("Senha gravada com sucesso, faça o login para acessar o sistema.");
		FacesContext.getCurrentInstance().addMessage(null, m);
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("fechaDialog", true);
		
		return "/pages/home.xhtml";
	}
	
}
