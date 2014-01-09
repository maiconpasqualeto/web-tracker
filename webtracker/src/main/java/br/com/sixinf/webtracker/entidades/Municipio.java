package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * Municipio generated by hbm2java
 */
@Entity
@Table(name = "municipio", schema = "public")
public class Municipio implements Entidade, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqMunicipio", sequenceName="municipio_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqMunicipio")
	@Column(name="id")
	private Long id;
	
	@Column(name = "descricao", length = 250)
	private String descricao;
	
	@Column(name = "uf", length = 2)
	private String uf;
	
	@Column(name = "situacao", length = 1)
	private String situacao;

	public Municipio() {
	}

	public Municipio(Long id) {
		this.id = id;
	}

	public Municipio(Long id, String descricao, String uf, String situacao) {
		this.id = id;
		this.descricao = descricao;
		this.uf = uf;
		this.situacao = situacao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUf() {
		return this.uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}