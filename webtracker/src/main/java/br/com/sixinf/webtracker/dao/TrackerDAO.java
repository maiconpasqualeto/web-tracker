/**
 * 
 */
package br.com.sixinf.webtracker.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.webtracker.entidades.Endereco;
import br.com.sixinf.webtracker.entidades.Pet;
import br.com.sixinf.webtracker.entidades.Posicao;
import br.com.sixinf.webtracker.entidades.Tracker;
import br.com.sixinf.webtracker.entidades.Usuario;

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
			hql.append("select p from Pet p join p.usuario u ");
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
	
	
	public void incluirAutoCadastro(Usuario usuario, Endereco endereco, Pet pet) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			t.begin();
						
			em.persist(usuario);
			em.persist(endereco);
			em.merge(pet);
			
			t.commit();			
			
		} catch (Exception e) {
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
	public Tracker buscarTodosTrackersPeloSerial(String serial) {
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
	public Pet buscarTodosPetsPeloSerial(String serial) {
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

}
