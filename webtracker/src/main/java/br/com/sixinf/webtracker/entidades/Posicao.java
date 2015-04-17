/**
 * 
 */
package br.com.sixinf.webtracker.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author maicon
 *
 */
@Entity
@Table(name="posicao")
public class Posicao implements Entidade, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqPosicao", sequenceName="posicao_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqPosicao")
	@Column(name="id")
	private Long id;
	
	@Column(name="data_hora_mensagem")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraMensagem;
	
	@Column(name="mensagem")
	private String mensagem;
	
	@Column(name="latitude")
	private String latitude;
	
	@Column(name="longitude")
	private String longitude;
	
	@Column(name="latitude_quadrante")
	private Character latitudeQuadrante;
	
	@Column(name="longitude_quadrante")
	private Character longitudeQuadrande;
	
	@Column(name="latitude_decimal")
	private BigDecimal latitudeDecimal;
	
	@Column(name="longitude_decimal")
	private BigDecimal longitudeDecimal;
	
	@Column(name="data_hora_coordenada")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraCoordenada;
	
	@Column(name="status_registro")
	private Character statusRegistro;
	
	@Column(name="velocidade")
	private BigDecimal velocidade;
	
	@Column(name="curso")
	private BigDecimal curso;
	
	@ManyToOne(targetEntity=Tracker.class)
	@JoinColumn(name="id_tracker")
	private Tracker tracker;
	
	@Column(name="mensagem_alerta")
	private String mensagemAlerta;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHoraMensagem() {
		return dataHoraMensagem;
	}

	public void setDataHoraMensagem(Date dataHoraMensagem) {
		this.dataHoraMensagem = dataHoraMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;		
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Character getLatitudeQuadrante() {
		return latitudeQuadrante;
	}

	public void setLatitudeQuadrante(Character latitudeQuadrante) {
		this.latitudeQuadrante = latitudeQuadrante;
	}

	public Character getLongitudeQuadrande() {
		return longitudeQuadrande;
	}

	public void setLongitudeQuadrande(Character longitudeQuadrande) {
		this.longitudeQuadrande = longitudeQuadrande;
	}
	
	public Date getDataHoraCoordenada() {
		return dataHoraCoordenada;
	}

	public void setDataHoraCoordenada(Date dataHoraCoordenada) {
		this.dataHoraCoordenada = dataHoraCoordenada;
	}

	public Character getStatusRegistro() {
		return statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
	
	public BigDecimal getLatitudeDecimal() {
		return latitudeDecimal;
	}

	public void setLatitudeDecimal(BigDecimal latitudeDecimal) {
		this.latitudeDecimal = latitudeDecimal;
	}

	public BigDecimal getLongitudeDecimal() {
		return longitudeDecimal;
	}

	public void setLongitudeDecimal(BigDecimal longitudeDecimal) {
		this.longitudeDecimal = longitudeDecimal;
	}

	public BigDecimal getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(BigDecimal velocidade) {
		this.velocidade = velocidade;
	}

	public BigDecimal getCurso() {
		return curso;
	}

	public void setCurso(BigDecimal curso) {
		this.curso = curso;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}
