/**
 * 
 */
package br.com.sixinf.webtracker.dao;

import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;

/**
 * @author maicon
 *
 */
public class TrackerDAO extends BridgeBaseDAO {
	
	private static TrackerDAO dao;
	
	public static TrackerDAO getInstance() {
		if (dao == null)
			dao = new TrackerDAO();
		return dao;
	}
	
	public TrackerDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	

}
