/**
 * 
 */
package br.com.sixinf.webtracker.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Plano;
import br.com.sixinf.webtracker.entidades.Posicao;
import br.com.sixinf.webtracker.entidades.Setup;
import br.com.sixinf.webtracker.entidades.TipoUsuario;
import br.com.sixinf.webtracker.entidades.Tracker;
import br.com.sixinf.webtracker.entidades.Usuario;
import br.com.sixinf.webtracker.entidades.UsuarioTracker;

/**
 * @author maicon
 *
 */
public class TrackerDAO extends BridgeBaseDAO {
	
	private static final Logger LOG = Logger.getLogger(TrackerDAO.class);
	
	private static TrackerDAO dao;
	
	public static TrackerDAO getInstance() {
		if (dao == null)
			dao = new TrackerDAO();
		return dao;
	}
	
	public TrackerDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	public List<Pet> buscarPetsUsuario(String nomeUsuario){
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Pet> list = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Pet p join p.usuarioTracker ut join ut.usuario u ");
			hql.append("where u.nomeUsuario = :nomeUsuario ");
			TypedQuery<Pet> q = em.createQuery(hql.toString(), Pet.class);
			q.setParameter("nomeUsuario", nomeUsuario);
			
			list = q.getResultList();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			LOG.error("Erro ao buscar pets do usuario", e);
		} finally {
            em.close();
        }
		return list;
	}
	
	public Posicao buscarUltimaPosicaoPet(Pet pet){
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Posicao p = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Posicao p join p.tracker t ");
			hql.append("where t.id = :id ");
			hql.append("order by p.dataHoraMensagem desc ");
			TypedQuery<Posicao> q = em.createQuery(hql.toString(), Posicao.class);
			q.setParameter("id", pet.getId());
			q.setMaxResults(1);
			
			p = q.getSingleResult();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			LOG.error("Erro ao buscar ultima posicao do pet", e);
		} finally {
            em.close();
        }
		return p;
	}
	
	public List<Posicao> buscarPosicoesPetIntervalo(Pet pet, Date dataInicial, Date dataFinal){
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Posicao> list = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Posicao p join p.tracker t ");
			hql.append("where t.id = :idPet and ");
			hql.append("( p.dataHoraMensagem >= :dataInicial ) and ( p.dataHoraMensagem <= :dataFinal )");
			TypedQuery<Posicao> q = em.createQuery(hql.toString(), Posicao.class);
			q.setParameter("idPet", pet.getId());
			q.setParameter("dataInicial", dataInicial);
			q.setParameter("dataFinal", dataFinal);
			
			list = q.getResultList();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			LOG.error("Erro ao buscar posicoes.", e);
		} finally {
            em.close();
        }
		return list;
	}
	
	
	public void incluirAutoCadastro(Usuario usuario, Endereco endereco, Pet pet, UsuarioTracker ut) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.persist(usuario);
			em.persist(endereco);
			em.persist(ut);			
			em.merge(pet);
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao incluir autocadastro.", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	/**
	 * 
	 * @param serial
	 * @return
	 */
	public Tracker buscarTrackerPeloSerial(String serial) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Tracker t = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select t from Tracker t ");
			hql.append("where t.statusRegistro = 'A' ");
			hql.append("and t.numeroSerie = :numeroSerie ");
			TypedQuery<Tracker> q = em.createQuery(hql.toString(), Tracker.class);
			q.setMaxResults(1);
			q.setParameter("numeroSerie", serial);
						
			t = q.getSingleResult();
						
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar tracker pelo serial", e);
		} finally {
            em.close();
        }
		return t;
	}
	
	/**
	 * 
	 * @param serial
	 * @return
	 */
	public Pet buscarPetPeloSerial(String serial) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Pet p = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Pet p ");
			hql.append("where p.statusRegistro = 'A' ");
			hql.append("and p.numeroSerie = :numeroSerie ");
			TypedQuery<Pet> q = em.createQuery(hql.toString(), Pet.class);
			q.setMaxResults(1);
			q.setParameter("numeroSerie", serial);
						
			p = q.getSingleResult();
						
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar pet pelo serial", e);
		} finally {
            em.close();
        }
		return p;
	}
	
	/**
	 * 
	 * @param nomeUsuario
	 * @return
	 */
	public Usuario buscarUsuarioPeloNomeUsuario(String nomeUsuario) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Usuario u = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.statusRegistro = 'A' ");
			hql.append("and u.nomeUsuario = :nomeUsuario ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			q.setMaxResults(1);
			q.setParameter("nomeUsuario", nomeUsuario);
						
			u = q.getSingleResult();
						
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar usuario pelo nomeUsuario", e);
		} finally {
            em.close();
        }
		return u;
	}
	
	/**
	 * 
	 * @param nomeUsuario
	 * @return
	 */
	public UsuarioTracker buscarUsuarioTrackerDoUsuario(String nomeUsuario, Long idTracker) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		UsuarioTracker ut = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select ut from UsuarioTracker ut ");
			hql.append("join ut.usuario u ");
			hql.append("join ut.tracker t ");
			hql.append("where u.nomeUsuario = :nomeUsuario ");
			hql.append("and t.id = :idTracker ");
			TypedQuery<UsuarioTracker> q = em.createQuery(hql.toString(), UsuarioTracker.class);
			q.setMaxResults(1);
			q.setParameter("nomeUsuario", nomeUsuario);
			q.setParameter("idTracker", idTracker);
			
			ut = q.getSingleResult();
			
			//Hibernate.initialize(ut.getUsuario());
			//Hibernate.initialize(ut.getTracker());
			
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar usuario tracker do usuario", e);
		} finally {
            em.close();
        }
		return ut;
	}
	
	/**
	 * 
	 * @param pet
	 * @throws LoggerException
	 */
	public void alterarPet(Pet pet) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.merge(pet);
			
			t.commit();			
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao alterar pet.", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	/**
	 * 
	 * @param pet
	 * @throws LoggerException
	 */
	public void incluirNovoPetUsuario(Pet p, UsuarioTracker ut) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.persist(p);
			em.persist(ut);
			
			t.commit();			
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao incluir novo pet usuário", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	/**
	 * 
	 * @param serial
	 * @return
	 */
	public List<Tracker> buscarTodosTrackersAtivos() {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Tracker> trackers = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select t from Tracker t ");
			hql.append("where t.statusRegistro = 'A' ");
			TypedQuery<Tracker> q = em.createQuery(hql.toString(), Tracker.class);
			
			trackers = q.getResultList();
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar todos os tracker ativos", e);
		} finally {
            em.close();
        }
		return trackers;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Usuario> buscarTodosUsuariosPerfil(TipoUsuario tipoUsuario) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Usuario> usuarios = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.statusRegistro = 'A' ");
			if(tipoUsuario.equals(TipoUsuario.ADMIN)) {
				hql.append("and u.tipoUsuario = '").append(TipoUsuario.ADMIN.toString() + "'");
				hql.append(" or u.tipoUsuario = '").append(TipoUsuario.USER.toString() + "'");
			} else 
				if(tipoUsuario.equals(TipoUsuario.USER))
					hql.append("and u.tipoUsuario = '").append(TipoUsuario.USER.toString() + "'");;
			
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			
			usuarios = q.getResultList();
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar todos os usuarios ativos", e);
		} finally {
            em.close();
        }
		return usuarios;
	}
	
	
	/**
	 * 
	 * @param pet
	 * @throws LoggerException
	 */
	public void adicionarPet(Pet pet) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.persist(pet);
			
			t.commit();			
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao incluir pet.", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	public void exclusaoLogicaTracker(Tracker tracker) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Tracker t ");
	        hql.append("set t.statusRegistro = 'D' ");
	        hql.append("where t.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", tracker.getId());
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao fazer exclusão lógica.", e, LOG);
		} finally {
            em.close();
        }
	}
	
	public <T> void exclusaoLogicaGenerica(Long id, Class<T> clazz) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update " + clazz.getSimpleName() + " t ");
	        hql.append("set t.statusRegistro = 'D' ");
	        hql.append("where t.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", id);
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao fazer exclusão lógica.", e, LOG);
		} finally {
            em.close();
        }
	}
	
	/**
	 * 
	 * @return
	 */
	public Endereco buscarEnderecoUsuario(Long idUsuario) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Endereco endereco = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select e from Endereco e join e.usuario u ");
			hql.append("where u.id = :idUsuario ");
			hql.append("and e.statusRegistro = 'A' ");
			TypedQuery<Endereco> q = em.createQuery(hql.toString(), Endereco.class);
			q.setParameter("idUsuario", idUsuario);
			
			endereco = q.getSingleResult();
        	
        } catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar endereco do usuario", e);
		} finally {
            em.close();
        }
		return endereco;
	}
	
	/**
	 * 
	 * @param usuario
	 * @param endereco
	 * @throws LoggerException
	 */
	public void alterarCadastroUsuario(Usuario usuario, Endereco endereco) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.merge(endereco);			
			em.merge(usuario);
			
			t.commit();			
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao salvar cadastro usuario.", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	/**
	 * 
	 * @param usuario
	 * @param endereco
	 * @throws LoggerException
	 */
	public void incluirCadastroUsuario(Usuario usuario, Endereco endereco) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
			
			em.persist(usuario);
			em.persist(endereco);
			
			t.commit();			
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao incluir cadastro Usuário.", e, LOG);
		} finally {
            em.close();
        }		
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void salvarNovaSenha(Usuario usuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Usuario u ");
	        hql.append("set u.senha = :senha, u.primeiroLogin = :primeiroLogin ");
	        hql.append("where u.nomeUsuario = :nomeUsuario");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("nomeUsuario", usuario.getNomeUsuario());
	        q.setParameter("primeiroLogin", Boolean.FALSE);
	        q.setParameter("senha", usuario.getSenha());
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao salvar nova senha.", e, LOG);
		} finally {
            em.close();
        }
	}
	
	/**
	 * 
	 * @return
	 */
	public Setup buscarUltimoSetup(){
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Setup setup = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select s from Setup s ");
			hql.append("where s.statusRegistro = 'A' ");
			hql.append("order by s.id desc ");
			TypedQuery<Setup> q = em.createQuery(hql.toString(), Setup.class);
			
			setup = q.getSingleResult();
        	
        } catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar setup", e);
		} finally {
            em.close();
        }
		return setup;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Plano> buscarTodosPlanos(){
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Plano> planos = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Plano p ");
			hql.append("where p.statusRegistro = 'A' ");
			hql.append("order by p.id ");
			TypedQuery<Plano> q = em.createQuery(hql.toString(), Plano.class);
			
			planos = q.getResultList();
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar planos", e);
		} finally {
            em.close();
        }
		return planos;
	}
	
	public Plano buscarPlanoTracker(Tracker tracker) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Plano plano = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select p from Plano p ");
			hql.append("join p.usuariosTrackers ut ");
			hql.append("join ut.tracker t ");
			hql.append("where t.id = :idTracker ");
			
			TypedQuery<Plano> q = em.createQuery(hql.toString(), Plano.class);
			
			q.setParameter("idTracker", tracker.getId());
			
			plano = q.getSingleResult();
        
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar plano do tracker", e);
		} finally {
            em.close();
        }
		return plano;
	}
	
	public Usuario buscarUsuarioTracker(Tracker tracker) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Usuario usuario = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("join u.trackers ut ");
			hql.append("join ut.tracker t ");
			hql.append("where t.id = :idTracker ");
			
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			
			q.setParameter("idTracker", tracker.getId());
			
			usuario = q.getSingleResult();
        
		} catch (NoResultException e) {
        	
        } catch (Exception e) {
			LOG.error("Erro ao buscar usuario do tracker", e);
		} finally {
            em.close();
        }
		return usuario;
	}
}
