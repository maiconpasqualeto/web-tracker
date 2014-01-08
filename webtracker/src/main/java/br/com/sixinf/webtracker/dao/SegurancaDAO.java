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
public class SegurancaDAO extends BridgeBaseDAO {
	
	private static SegurancaDAO dao;
	
	public static SegurancaDAO getInstance(){
		if (dao == null)
			dao = new SegurancaDAO();
		return dao;
	}

	public SegurancaDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	

}
