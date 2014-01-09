package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * Lancamento generated by hbm2java
 */
@Entity
@Table(name = "lancamento", schema = "public")
public class Lancamento implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqLancamento", sequenceName="lancamento_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqLancamento")
	@Column(name="id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_conta")
	private Conta conta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_setup")
	private Setup setup;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tracker")
	private Tracker tracker;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_lancamento")
	private Date dataLancamento;
	
	@Column(name = "descricao", length = 150)
	private String descricao;
	
	@Column(name = "operacao", length = 1)
	private Character operacao;
	
	@Column(name = "valor", precision = 131089, scale = 0)
	private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento", length = 13)
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento", length = 13)
	private Date dataPagamento;
	
	@Column(name = "valor_pago", precision = 131089, scale = 0)
	private BigDecimal valorPago;
	
	@Column(name = "valor_juros", precision = 131089, scale = 0)
	private BigDecimal valorJuros;
	
	@Column(name = "valor_multa", precision = 131089, scale = 0)
	private BigDecimal valorMulta;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;

	public Lancamento() {
	}

	public Lancamento(Long id) {
		this.id = id;
	}

	public Lancamento(Long id, Usuario usuario, Conta conta, Setup setup,
			Tracker tracker, Date dataLancamento, String descricao,
			Character operacao, BigDecimal valor, Date dataVencimento,
			Date dataPagamento, BigDecimal valorPago, BigDecimal valorJuros,
			BigDecimal valorMulta, Character statusRegistro) {
		this.id = id;
		this.usuario = usuario;
		this.conta = conta;
		this.setup = setup;
		this.tracker = tracker;
		this.dataLancamento = dataLancamento;
		this.descricao = descricao;
		this.operacao = operacao;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.valorPago = valorPago;
		this.valorJuros = valorJuros;
		this.valorMulta = valorMulta;
		this.statusRegistro = statusRegistro;
	}
		
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Conta getConta() {
		return this.conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Setup getSetup() {
		return this.setup;
	}

	public void setSetup(Setup setup) {
		this.setup = setup;
	}

	public Tracker getTracker() {
		return this.tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public Date getDataLancamento() {
		return this.dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Character getOperacao() {
		return this.operacao;
	}

	public void setOperacao(Character operacao) {
		this.operacao = operacao;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getDataVencimento() {
		return this.dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorPago() {
		return this.valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public BigDecimal getValorJuros() {
		return this.valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorMulta() {
		return this.valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public Character getStatusRegistro() {
		return this.statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
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