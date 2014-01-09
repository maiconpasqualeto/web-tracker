package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * Endereco generated by hbm2java
 */
@Entity
@Table(name = "endereco", schema = "public")
public class Endereco implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqEndereco", sequenceName="endereco_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqEndereco")
	@Column(name="id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "logradouro", length = 500)
	private String logradouro;
	
	@Column(name = "numero", length = 8)
	private String numero;
	
	@Column(name = "complemento", length = 30)
	private String complemento;
	
	@Column(name = "bairro", length = 100)
	private String bairro;
	
	@Column(name = "cep", length = 8)
	private String cep;
	
	@Column(name = "municipio", length = 150)
	private String municipio;
	
	@Column(name = "uf", length = 2)
	private String uf;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;

	public Endereco() {
	}

	public Endereco(Long id) {
		this.id = id;
	}

	public Endereco(Long id, Usuario usuario, String logradouro, String numero,
			String complemento, String bairro, String cep, String municipio,
			String uf, Character statusRegistro) {
		this.id = id;
		this.usuario = usuario;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.municipio = municipio;
		this.uf = uf;
		this.statusRegistro = statusRegistro;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLogradouro() {
		return this.logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return this.complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return this.bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return this.cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return this.uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Character getStatusRegistro() {
		return this.statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}