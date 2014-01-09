/**
 * 
 */
package br.com.sixinf.webtracker.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@Entity
@Table(name="tracker")
@Inheritance(strategy=InheritanceType.JOINED)
public class Tracker implements Entidade, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqTracker", sequenceName="tracker_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqTracker")
	@Column(name="id")
	private Long id;
	
	@Column(name="numero_serie")
	private String numeroSerie;
	
	@Column(name="data_fabricacao")
	@Temporal(TemporalType.DATE)
	private Date dataFabricacao;
	
	@Column(name="modelo_tracker")
	private String modeloTracker;
	
	@Column(name="fabricante_tracker")
	private String fabricanteTracker;
	
	@Column(name="data_registro_tracker")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRegistroTracker;
	
	@Column(name="data_ativacao_tracker")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtivacaoTracker;
	
	@Column(name="status_registro")
	private Character statusRegistro;
	
	@ManyToOne(targetEntity=Usuario.class, fetch=FetchType.LAZY)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
		
	@OneToMany(mappedBy="tracker", fetch=FetchType.LAZY, cascade=CascadeType.DETACH)
	@Cascade(org.hibernate.annotations.CascadeType.DETACH)
	private List<Posicao> posicoes;
	
	public Tracker() {
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public String getModeloTracker() {
		return modeloTracker;
	}

	public void setModeloTracker(String modeloTracker) {
		this.modeloTracker = modeloTracker;
	}

	public String getFabricanteTracker() {
		return fabricanteTracker;
	}

	public void setFabricanteTracker(String fabricanteTracker) {
		this.fabricanteTracker = fabricanteTracker;
	}

	public Date getDataRegistroTracker() {
		return dataRegistroTracker;
	}

	public void setDataRegistroTracker(Date dataRegistroTracker) {
		this.dataRegistroTracker = dataRegistroTracker;
	}

	public Date getDataAtivacaoTracker() {
		return dataAtivacaoTracker;
	}

	public void setDataAtivacaoTracker(Date dataAtivacaoTracker) {
		this.dataAtivacaoTracker = dataAtivacaoTracker;
	}

	public Character getStatusRegistro() {
		return statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}
	
	public List<Posicao> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<Posicao> posicoes) {
		this.posicoes = posicoes;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
