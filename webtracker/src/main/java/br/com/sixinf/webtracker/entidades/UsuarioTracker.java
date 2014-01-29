/**
 * 
 */
package br.com.sixinf.webtracker.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "usuario_tracker", schema = "public")
public class UsuarioTracker implements Serializable, Entidade {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqUsuarioTracker", sequenceName="usuario_tracker_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqUsuarioTracker")
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_tracker")
	private Tracker tracker;
	
	@ManyToOne
	@JoinColumn(name="id_plano")
	private Plano plano;
	
	@Column(name="carencia")
	private Integer carencia;
	
	@Column(name="data_ativacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtivacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public Integer getCarencia() {
		return carencia;
	}

	public void setCarencia(Integer carencia) {
		this.carencia = carencia;
	}

	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}
