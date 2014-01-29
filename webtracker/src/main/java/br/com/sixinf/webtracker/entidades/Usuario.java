package br.com.sixinf.webtracker.entidades;

// Generated Jan 9, 2014 12:27:35 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "usuario", schema = "public")
public class Usuario implements Entidade, Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SESSION_NOME_USUARIO = "usuario";
	public static final String SESSION_TIPO_USUARIO = "tipo_usuario";
	
	@Id
	@SequenceGenerator(name="seqUsuario", sequenceName="usuario_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqUsuario")
	@Column(name="id")
	private Long id;
	
	@Column(name = "usuario", length = 30)
	private String nomeUsuario;
	
	@Column(name = "senha", length = 30)
	private String senha;
	
	@Column(name = "primeiro_login")
	private Boolean primeiroLogin;
	
	@Column(name = "tipo_usuario", length = 10)
	private String tipoUsuario;
	
	@Column(name = "nome", length = 500)
	private String nome;
	
	@Column(name = "cpf", length = 11)
	private String cpf;
	
	@Column(name = "email", length = 80)
	private String email;
	
	@Column(name = "melhor_dia_vencimento")
	private Integer melhorDiaVencimento;
	
	@Column(name = "fone_celular", length = 15)
	private String foneCelular;
	
	@Column(name = "fone_contato", length = 15)
	private String foneContato;
	
	@Column(name = "situacao", length = 10)
	private String situacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_registro", length = 29)
	private Date dataRegistro;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private Set<Conta> contas = new HashSet<Conta>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private Set<UsuarioTracker> trackers = new HashSet<UsuarioTracker>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private Set<Lancamento> lancamentos = new HashSet<Lancamento>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private Set<Endereco> enderecos = new HashSet<Endereco>(0);
	
	public Usuario() {
	}

	public Usuario(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getPrimeiroLogin() {
		return this.primeiroLogin;
	}

	public void setPrimeiroLogin(Boolean primeiroLogin) {
		this.primeiroLogin = primeiroLogin;
	}

	public String getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getMelhorDiaVencimento() {
		return this.melhorDiaVencimento;
	}

	public void setMelhorDiaVencimento(Integer melhorDiaVencimento) {
		this.melhorDiaVencimento = melhorDiaVencimento;
	}

	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataRegistro() {
		return this.dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Character getStatusRegistro() {
		return this.statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	public Set<Conta> getContas() {
		return this.contas;
	}

	public void setContas(Set<Conta> contas) {
		this.contas = contas;
	}

	public Set<UsuarioTracker> getTrackers() {
		return this.trackers;
	}

	public void setTrackers(Set<UsuarioTracker> trackers) {
		this.trackers = trackers;
	}

	public Set<Lancamento> getLancamentos() {
		return this.lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Set<Endereco> getEnderecos() {
		return this.enderecos;
	}

	public void setEnderecos(Set<Endereco> enderecos) {
		this.enderecos = enderecos; 
	}
	
	public String getFoneCelular() {
		return foneCelular;
	}

	public void setFoneCelular(String foneCelular) {
		this.foneCelular = foneCelular;
	}

	public String getFoneContato() {
		return foneContato;
	}

	public void setFoneContato(String foneContato) {
		this.foneContato = foneContato;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}
	
	public String getCpfMask(){
		return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9); 
	}
	
	public String getFoneCelularMask(){
		return "(" + foneCelular.substring(0, 2) + ")" + foneCelular.substring(2); 
	}
	
	public String getFoneContatoMask(){		
		if (foneContato != null && !foneContato.isEmpty())
			return "(" + foneContato.substring(0, 2) + ")" + foneContato.substring(2);
		return null;
	}
}
