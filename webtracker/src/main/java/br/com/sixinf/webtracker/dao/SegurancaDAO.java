/**
 * 
 */
package br.com.sixinf.webtracker.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.webtracker.entidades.Usuario;

/**
 * @author maicon
 *
 */
public class SegurancaDAO extends BridgeBaseDAO {
	
	private static final Logger LOG = Logger.getLogger(SegurancaDAO.class);
	
	private static SegurancaDAO dao;
	
	public static SegurancaDAO getInstance(){
		if (dao == null)
			dao = new SegurancaDAO();
		return dao;
	}

	public SegurancaDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	public Usuario buscarUsuario(String nomeUsuario) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Usuario u = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.nomeUsuario = :nomeUsuario ");
			hql.append("and u.statusRegistro = 'A' ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			q.setParameter("nomeUsuario", nomeUsuario);
			q.setMaxResults(1);
			
			u = q.getSingleResult();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			LOG.error("Erro ao buscar usuario", e);
		} finally {
            em.close();
        }
		return u;
	}

}
