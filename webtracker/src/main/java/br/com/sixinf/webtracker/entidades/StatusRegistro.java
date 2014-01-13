package br.com.sixinf.webtracker.entidades;

/**
 * 
 * @author maicon
 *
 */
public enum StatusRegistro {
			
	A, // ATIVO 
	D; // DELETADO

	
	public Character getChar(){		
		return this.name().charAt(0);
	}
}
