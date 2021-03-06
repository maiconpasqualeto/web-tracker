package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * Logs generated by hbm2java
 */
@Entity
@Table(name = "logs", schema = "public")
public class Logs implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqLogs", sequenceName="logs_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqLogs")
	@Column(name="id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora", length = 29)
	private Date dataHora;
	
	@Column(name = "tipo_log", length = 10)
	private String tipoLog;
	
	@Column(name = "mensagem", length = 500)
	private String mensagem;
	
	@Column(name = "usuario", length = 50)
	private String usuario;
	
	@Column(name = "id_tracker")
	private Integer idTracker;
	
	@Column(name = "numero_serie_tracker", length = 50)
	private String numeroSerieTracker;
	
	@Column(name = "tabelas", length = 500)
	private String tabelas;
	
	@Column(name = "acao", length = 10)
	private String acao;

	public Logs() {
	}

	public Logs(Long id) {
		this.id = id;
	}

	public Logs(Long id, Date dataHora, String tipoLog, String mensagem,
			String usuario, Integer idTracker, String numeroSerieTracker,
			String tabelas, String acao) {
		this.id = id;
		this.dataHora = dataHora;
		this.tipoLog = tipoLog;
		this.mensagem = mensagem;
		this.usuario = usuario;
		this.idTracker = idTracker;
		this.numeroSerieTracker = numeroSerieTracker;
		this.tabelas = tabelas;
		this.acao = acao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHora() {
		return this.dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public String getTipoLog() {
		return this.tipoLog;
	}

	public void setTipoLog(String tipoLog) {
		this.tipoLog = tipoLog;
	}

	public String getMensagem() {
		return this.mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getIdTracker() {
		return this.idTracker;
	}

	public void setIdTracker(Integer idTracker) {
		this.idTracker = idTracker;
	}

	public String getNumeroSerieTracker() {
		return this.numeroSerieTracker;
	}

	public void setNumeroSerieTracker(String numeroSerieTracker) {
		this.numeroSerieTracker = numeroSerieTracker;
	}

	public String getTabelas() {
		return this.tabelas;
	}

	public void setTabelas(String tabelas) {
		this.tabelas = tabelas;
	}

	public String getAcao() {
		return this.acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}
