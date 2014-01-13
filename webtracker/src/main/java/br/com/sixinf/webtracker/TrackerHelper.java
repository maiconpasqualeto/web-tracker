/**
 * 
 */
package br.com.sixinf.webtracker;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		Object usuario = session.getAttribute(Usuario.SESSION_ID);
		if (usuario != null)
			return usuario.toString();
		
		return "";
	}

}
