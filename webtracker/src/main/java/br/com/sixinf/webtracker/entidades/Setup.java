package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * Setup generated by hbm2java
 */
@Entity
@Table(name = "setup", schema = "public")
public class Setup implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqSetup", sequenceName="setup_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqSetup")
	@Column(name="id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_cadastro", length = 29)
	private Date dataHoraCadastro;
	
	@Column(name = "valor_mensalidade", precision = 131089, scale = 0)
	private BigDecimal valorMensalidade;
	
	@Column(name = "valor_tracker", precision = 131089, scale = 0)
	private BigDecimal valorTracker;
	
	@Column(name = "quantidade_parcelas")
	private Integer quantidadeParcelas;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "setup")
	private Set<Lancamento> lancamentos;

	public Setup() {
	}

	public Setup(Long id) {
		this.id = id;
	}

	public Setup(Long id, Date dataHoraCadastro, BigDecimal valorMensalidade,
			BigDecimal valorTracker, Integer quantidadeParcelas,
			Character statusRegistro, Set<Lancamento> lancamentos) {
		this.id = id;
		this.dataHoraCadastro = dataHoraCadastro;
		this.valorMensalidade = valorMensalidade;
		this.valorTracker = valorTracker;
		this.quantidadeParcelas = quantidadeParcelas;
		this.statusRegistro = statusRegistro;
		this.lancamentos = lancamentos;
	}
	
	public Date getDataHoraCadastro() {
		return this.dataHoraCadastro;
	}

	public void setDataHoraCadastro(Date dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
	}

	public BigDecimal getValorMensalidade() {
		return this.valorMensalidade;
	}

	public void setValorMensalidade(BigDecimal valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}

	public BigDecimal getValorTracker() {
		return this.valorTracker;
	}

	public void setValorTracker(BigDecimal valorTracker) {
		this.valorTracker = valorTracker;
	}

	public Integer getQuantidadeParcelas() {
		return this.quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Character getStatusRegistro() {
		return this.statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	public Set<Lancamento> getLancamentos() {
		return this.lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}
