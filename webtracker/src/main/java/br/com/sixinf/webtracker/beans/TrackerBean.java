/**
 * 
 */
package br.com.sixinf.webtracker.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import br.com.sixinf.webtracker.TrackerHelper;
import br.com.sixinf.webtracker.dao.TrackerDAO;
import br.com.sixinf.webtracker.entidades.Pet;

/**
 * @author maicon
 *
 */
@ManagedBean(name="trackerBean")
public class TrackerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Pet> pets = new ArrayList<Pet>();
	
	public TrackerBean() {
		
	}
	
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	@PostConstruct
	public void listaInformacoesInciciais(){
		String usuario = TrackerHelper.getUsuarioSessao();
		pets = TrackerDAO.getInstance().buscarPetsUsuario(usuario);
	}
	
}
