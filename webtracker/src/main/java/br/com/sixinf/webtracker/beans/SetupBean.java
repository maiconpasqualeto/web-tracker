/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.sixinf.webtracker.entidades.Setup;
import br.com.sixinf.webtracker.facade.TrackerFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="setupBean")
@ViewScoped
public class SetupBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Setup setup = new Setup();

	public Setup getSetup() {
		return setup;
	}

	public void setSetup(Setup setup) {
		this.setup = setup;
	}
	
	@PostConstruct
	public void init(){
		Setup setup = TrackerFacade.getInstance().buscarUltimoSetup();
		if (setup != null)
			this.setup = setup;
	}
	
	public String salvar(){
		return "";
	}
}
