/**
 * 
 */
package br.com.sixinf.webtracker.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.ferramentas.persistencia.PersistenciaException;

/**
 * @author maicon
 *
 */
public class WebMediaListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AdministradorPersistencia.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			AdministradorPersistencia.createEntityManagerFactory("webtracker");
		} catch (PersistenciaException e) {
			Logger.getLogger(WebMediaListener.class).error(e);
		}
	}

}
