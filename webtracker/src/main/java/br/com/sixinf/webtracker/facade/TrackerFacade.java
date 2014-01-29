/**
 * 
 */
package br.com.sixinf.webtracker.facade;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Plano;
import br.com.sixinf.webtracker.entidades.Posicao;
import br.com.sixinf.webtracker.entidades.Setup;
import br.com.sixinf.webtracker.entidades.SituacaoUsuario;
import br.com.sixinf.webtracker.entidades.StatusRegistro;
import br.com.sixinf.webtracker.entidades.Tracker;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.entidades.UsuarioTracker;


/**
 * @author maicon
 *
 */
public class TrackerFacade {
	
	private static final Logger LOG = Logger.getLogger(TrackerFacade.class);
	
	private static TrackerFacade facade;
	private static TrackerDAO dao;
	
	public static TrackerFacade getInstance(){
		if (facade == null)
			facade = new TrackerFacade();
		
		return facade;
	}
	
	public TrackerFacade() {
		dao = TrackerDAO.getInstance();
	}
	
	public void alterarCadastroUsuario(Usuario usuario, Endereco endereco) throws LoggerException {
		
		Usuario usuarioBanco = dao.buscarUsuarioPeloNomeUsuario(usuario.getNomeUsuario());
		usuarioBanco.setCpf(usuario.getCpf());
		usuarioBanco.setEmail(usuario.getEmail());
		usuarioBanco.setFoneCelular(usuario.getFoneCelular());
		usuarioBanco.setFoneContato(usuario.getFoneContato());
		usuarioBanco.setMelhorDiaVencimento(usuario.getMelhorDiaVencimento());
		usuarioBanco.setNome(usuario.getNome());
		usuarioBanco.setNomeUsuario(usuario.getNomeUsuario());
		usuarioBanco.setTipoUsuario(usuario.getTipoUsuario());
		
		Endereco endBanco = dao.buscarEnderecoUsuario(usuario.getId());
		endBanco.setBairro(endereco.getBairro());
		endBanco.setCep(endereco.getCep());
		endBanco.setComplemento(endereco.getComplemento());
		endBanco.setLogradouro(endereco.getLogradouro());
		endBanco.setMunicipio(endereco.getMunicipio());
		endBanco.setNumero(endereco.getNumero());
		endBanco.setUf(endereco.getUf());
				
		dao.alterarCadastroUsuario(usuarioBanco, endBanco);
		
	}
	
	public void incluirCadastroUsuario(Usuario usuario, Endereco endereco) throws LoggerException {
		
		usuario.setId(null);
		usuario.setDataRegistro(new Date());
		usuario.setPrimeiroLogin(true);
		usuario.setSituacao(SituacaoUsuario.ATIVO.name());
		usuario.setStatusRegistro(StatusRegistro.A.getChar());
		usuario.setTipoUsuario(usuario.getTipoUsuario());
		usuario.getEnderecos().add(endereco);
		
		endereco.setStatusRegistro(StatusRegistro.A.getChar());
		endereco.setUsuario(usuario);
		
		dao.incluirCadastroUsuario(usuario, endereco);
		
	}
	
	public void salvarPetUsuario(String nomeUsuario, Pet pet, Plano plano) throws LoggerException {		
		UsuarioTracker ut = null;
		
		if (pet.getId() != null &&
				!pet.getId().equals(0L)) {
		
			ut = dao.buscarUsuarioTrackerDoUsuario(nomeUsuario, pet.getId());
			
			dao.alterarPet(pet);
			
		} else {
			
			ut = new UsuarioTracker();
			Usuario u = dao.buscarUsuarioPeloNomeUsuario(nomeUsuario);
			
			ut.setCarencia(0); // TODO [Maicon] Buscar no setup
			ut.setDataAtivacao(new Date());
			ut.setPlano(plano);
			ut.setTracker(pet);
			ut.setUsuario(u);
			
			dao.incluirNovoPetUsuario(pet, ut);
		}
		
	}
	
	public void incluirPet(Pet p) throws LoggerException{
		dao.adicionar(p);
	}
	
	/**
	 * 
	 * @param tracker
	 * @throws LoggerException
	 */
	public void salvarTrackerFabricante(Tracker tracker) throws LoggerException{
		Pet pet = dao.buscar(tracker.getId(), Pet.class);
		pet.setNumeroSerie(tracker.getNumeroSerie());
		pet.setDataFabricacao(tracker.getDataFabricacao());
		pet.setModeloTracker(tracker.getModeloTracker());
		pet.setFabricanteTracker(tracker.getFabricanteTracker());
		dao.alterar(pet);
		
	}
	
	public void exclusaoLogicaTrackerFabricante(Tracker t) throws LoggerException{
		dao.exclusaoLogicaTracker(t);
	}
	
	public <T> void exclusaoLogicaGenerica(Long id, Class<T> clazz) throws LoggerException{
		dao.exclusaoLogicaGenerica(id, clazz);
	}
	
	public Endereco buscarCEP(String cep){
		InputStream is = null;
		HttpURLConnection connection = null;
		Endereco endereco = new Endereco();
		endereco.setCep(cep);
		
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
					endereco.setUf(uf);
					endereco.setMunicipio(municipio);
					endereco.setLogradouro(tipoLogradouro + " " + logradouro);
					endereco.setBairro(bairro);
				} else 
					if (resultado.equals("2")) { // cep único
						String uf = jo.getString("uf");
						String municipio = jo.getString("cidade");
						endereco.setUf(uf);
						endereco.setMunicipio(municipio);
					}
				
			} finally {
				if (is != null)
					is.close();
			}
			
		} catch (IOException | JSONException e) {
			LOG.error("Erro ao buscar CEP", e);
		}
		
		return endereco;
	}
	
	public List<Posicao> buscarPosicoesPetIntervalo(Pet pet, Date dataInicial, Date dataFinal){
		Calendar cIni = Calendar.getInstance();
		cIni.setTime(dataInicial);
		cIni.set(Calendar.SECOND, 0);
		cIni.set(Calendar.MILLISECOND, 0);
		
		Calendar cFim = Calendar.getInstance();
		cFim.setTime(dataFinal);
		cFim.set(Calendar.SECOND, cFim.getMaximum(Calendar.SECOND));
		cFim.set(Calendar.MILLISECOND, cFim.getMaximum(Calendar.MILLISECOND));
		
		return dao.buscarPosicoesPetIntervalo(pet, cIni.getTime(), cFim.getTime());
		
	}
	
	public Setup buscarUltimoSetup(){
		return dao.buscarUltimoSetup();
	}
	
	public List<Plano> buscarTodosPlanos(){
		return dao.buscarTodosPlanos();
	}

	public Plano buscarPlanoTracker(Tracker tracker) {
		return dao.buscarPlanoTracker(tracker);
	}
	
	public Usuario buscarUsuarioTracker(Tracker tracker) {
		return dao.buscarUsuarioTracker(tracker);
	}
}
