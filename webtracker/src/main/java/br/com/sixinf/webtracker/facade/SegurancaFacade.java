/**
 * 
 */
package br.com.sixinf.webtracker.facade;

import java.util.Date;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.dao.SegurancaDAO;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.SituacaoUsuario;
import br.com.sixinf.webtracker.entidades.StatusRegistro;
import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.entidades.UsuarioTracker;

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
	
	public RetornoLogin logar(Usuario usuario){
		RetornoLogin retorno = new RetornoLogin();
		usuario.setPrimeiroLogin(null);
		
		// parâmetros passados como null
		if (usuario == null || 
				usuario.getNomeUsuario() == null || 
				usuario.getSenha() == null) {
			retorno.setValido(false);
			retorno.setMensagem("Usuário ou senha em branco");
			return retorno;
		}
		
		SegurancaDAO dao = SegurancaDAO.getInstance();
		Usuario usuarioBanco = dao.buscarUsuario(usuario.getNomeUsuario());
		
		// usuário não existe
		if (usuarioBanco == null) {
			retorno.setValido(false);
			retorno.setMensagem("Usuário não encontrado");
			return retorno;
		}
		
		// se a senha for em branco, e não é o primeiro login, então retorna mensagem para preencher a senha
		if (usuario.getSenha().isEmpty()) {
			if (usuarioBanco.getPrimeiroLogin().equals(Boolean.FALSE)){
				retorno.setValido(false);
				retorno.setMensagem("Senha não pode ser em branco");
				return retorno;
			} else {
				usuario.setPrimeiroLogin(usuarioBanco.getPrimeiroLogin());
				
				retorno.setValido(false);
				retorno.setMensagem("Definir Senha");
				return retorno;
			}
		} 
		
		// senha não confere
		String senhaSHA2 = Utilitarios.geraHashSHA2(usuario.getSenha());
		if (!senhaSHA2.equals(usuarioBanco.getSenha())) {
			retorno.setValido(false);
			retorno.setMensagem("Senha não confere");
			return retorno;
		}
		
		usuario.setTipoUsuario(usuarioBanco.getTipoUsuario());
		
		retorno.setValido(true);
		retorno.setMensagem("login OK");
		return retorno;		
	}
	
	/**
	 * 
	 * @param usuario
	 * @param endereco
	 * @param pet
	 * @throws LoggerException
	 */
	public void salvarAutoCadastro(Usuario usuario, Endereco endereco, Pet pet) throws LoggerException {
		Date dataRegistro = new Date();
		usuario.setDataRegistro(dataRegistro);
		usuario.setPrimeiroLogin(false);
		usuario.setSituacao(SituacaoUsuario.ATIVO.name());
		usuario.setStatusRegistro(StatusRegistro.A.getChar());
		usuario.setTipoUsuario(TipoUsuario.USER.name());
		usuario.getEnderecos().add(endereco);
		
		UsuarioTracker ut = new UsuarioTracker();
		ut.setUsuario(usuario);
		ut.setTracker(pet);
		ut.setCarencia(0); // TODO [Maicon] buscar no parâmetro
		ut.setDataAtivacao(dataRegistro);
		
		usuario.getTrackers().add(ut);
		usuario.setSenha(Utilitarios.geraHashSHA2(usuario.getSenha()));
		
		endereco.setStatusRegistro(StatusRegistro.A.getChar());
		endereco.setUsuario(usuario);
					
		TrackerDAO.getInstance().incluirAutoCadastro(usuario, endereco, pet, ut);
		
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void salvarNovaSenha(Usuario usuario) throws LoggerException {
		
		usuario.setSenha(Utilitarios.geraHashSHA2(usuario.getSenha()));
		
		TrackerDAO.getInstance().salvarNovaSenha(usuario);
	}
	
	/**
	 * 
	 * @author maicon
	 *
	 */
	public class RetornoLogin {
		
		private boolean valido;
		private String mensagem;
		
		public boolean isValido() {
			return valido;
		}
		
		public void setValido(boolean valido) {
			this.valido = valido;
		}
		
		public String getMensagem() {
			return mensagem;
		}
		
		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}
		
		
	}

}
