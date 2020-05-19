package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="caisse")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Caisse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="nom_caisse")
	private String nomCptCaisse;

	@Column(name="devise")
	private String devise;

	//bi-directional many-to-one association to Account
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id_compte")
	private Account account;
	

	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="caisse")
	@XmlTransient
	private List<TransactionEpargne> transactionEpargnes;

	//bi-directional many-to-many association to Utilisateur
	@ManyToMany(mappedBy="caisses")
	@XmlTransient
	private List<Utilisateur> utilisateurs;

	public Caisse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNomCptCaisse() {
		return nomCptCaisse;
	}

	public void setNomCptCaisse(String nomCptCaisse) {
		this.nomCptCaisse = nomCptCaisse;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<TransactionEpargne> getTransactionEpargnes() {
		return transactionEpargnes;
	}

	public void setTransactionEpargnes(List<TransactionEpargne> transactionEpargnes) {
		this.transactionEpargnes = transactionEpargnes;
	}

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

}
