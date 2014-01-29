/**
 * 
 */
package br.com.sixinf.webtracker;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Usuario;

/**
 * @author maicon
 * 
 */
public class TrackerHelper {

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public static String getUsuarioSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		Object usuario = session.getAttribute(Usuario.SESSION_NOME_USUARIO);
		if (usuario != null)
			return usuario.toString();
		
		return "";
	}
	
	public static TipoUsuario getTipoUsuarioSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		Object tipoUsuario = session.getAttribute(Usuario.SESSION_TIPO_USUARIO);
		if (tipoUsuario != null) 
			return TipoUsuario.valueOf(tipoUsuario.toString());			
		
		
		return null;
	}

}
