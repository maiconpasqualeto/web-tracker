/**
 * 
 */
package br.com.sixinf.webtracker.facade;

import java.util.Date;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.SituacaoUsuario;
import br.com.sixinf.webtracker.entidades.StatusRegistro;
import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Tracker;
import br.com.sixinf.webtracker.entidades.Usuario;


/**
 * @author maicon
 *
 */
public class TrackerFacade {
	
	private static final Logger LOG = Logger.getLogger(TrackerFacade.class);
	
	private static TrackerFacade facade;
	
	public static TrackerFacade getInstance(){
		if (facade == null)
			facade = new TrackerFacade();
		
		return facade;
	}
	
	public void salvarAutoCadastro(Usuario usuario, Endereco endereco, Pet pet) throws LoggerException {
		
		usuario.setDataRegistro(new Date());
		usuario.setPrimeiroLogin(false);
		usuario.setSituacao(SituacaoUsuario.ATIVO.name());
		usuario.setStatusRegistro(StatusRegistro.A.getChar());
		usuario.setTipoUsuario(TipoUsuario.USER.name());
		usuario.getEnderecos().add(endereco);
		usuario.getTrackers().add(pet);
		usuario.setSenha(Utilitarios.geraHashSHA2(usuario.getSenha()));
		
		endereco.setStatusRegistro(StatusRegistro.A.getChar());
		endereco.setUsuario(usuario);
		
		pet.setUsuario(usuario);
					
		TrackerDAO.getInstance().incluirAutoCadastro(usuario, endereco, pet);
		
	}

}
