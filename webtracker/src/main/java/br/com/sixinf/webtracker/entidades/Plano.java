/**
 * 
 */
package br.com.sixinf.webtracker.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@Entity
@Table(name = "plano", schema = "public")
public class Plano implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqPlano", sequenceName="plano_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqPlano")
	@Column(name="id")
	private Long id;
	
	@Column(name = "nome_plano")
	private String nomePlano;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "valor", precision = 131089, scale = 0)
	private BigDecimal valor;
	
	@Column(name = "tipo_cobranca")
	private String tipoCobranca;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;
	
	@Column(name="tipo_tracker_plano")
	private String tipoTrackerPlano;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="plano")
	private List<UsuarioTracker> usuariosTrackers;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomePlano() {
		return nomePlano;
	}

	public void setNomePlano(String nomePlano) {
		this.nomePlano = nomePlano;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(String tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Character getStatusRegistro() {
		return statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	public String getTipoTrackerPlano() {
		return tipoTrackerPlano;
	}

	public void setTipoTrackerPlano(String tipoTrackerPlano) {
		this.tipoTrackerPlano = tipoTrackerPlano;
	}

	public List<UsuarioTracker> getUsuariosTrackers() {
		return usuariosTrackers;
	}

	public void setUsuariosTrackers(List<UsuarioTracker> usuariosTrackers) {
		this.usuariosTrackers = usuariosTrackers;
	}

	@Override
	public Long getIdentificacao() {		
		return id;
	}

}
