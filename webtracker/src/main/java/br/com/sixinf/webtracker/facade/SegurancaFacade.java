/**
 * 
 */
package br.com.sixinf.webtracker.facade;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.webtracker.dao.SegurancaDAO;
import br.com.sixinf.webtracker.entidades.Usuario;

/**
 * @author maicon
 *
 */
public class SegurancaFacade {
	
	private static SegurancaFacade facade;
	
	public static SegurancaFacade getInstance() {
		if (facade == null)
			facade = new SegurancaFacade();
		
		return facade;
	}
	
	public boolean logar(String nomeUsuario, String senha){
		// parâmetros passados como null
		if (nomeUsuario == null || senha == null)
			return false;
		
		SegurancaDAO dao = SegurancaDAO.getInstance();
		Usuario u = dao.buscarUsuario(nomeUsuario);
		
		// usuário não existe
		if (u == null) 
			return false;
		
		// senha não confere
		String senhaSHA2 = Utilitarios.geraHashSHA2(senha);
		if (!senhaSHA2.equals(u.getSenha())) 
			return false;
		
		return true;
	}

}
