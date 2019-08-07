package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the compte_caisse database table.
 * 
 */
@Entity
@Table(name="compte_caisse")
@NamedQuery(name="CompteCaisse.findAll", query="SELECT c FROM CompteCaisse c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteCaisse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="nom_cpt_caisse")
	private String nomCptCaisse;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="Plan_comptablenum_plan_comptable")
	@XmlTransient
	private Account account;

	//bi-directional many-to-one association to TransactionEpargne
	@OneToMany(mappedBy="compteCaisse")
	@XmlTransient
	private List<TransactionEpargne> transactionEpargnes;

	//bi-directional many-to-many association to Utilisateur
	@ManyToMany(mappedBy="compteCaisses")
	@XmlTransient
	private List<Utilisateur> utilisateurs;

	public CompteCaisse() {
	}

	public String getNomCptCaisse() {
		return this.nomCptCaisse;
	}

	public void setNomCptCaisse(String nomCptCaisse) {
		this.nomCptCaisse = nomCptCaisse;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<TransactionEpargne> getTransactionEpargnes() {
		return this.transactionEpargnes;
	}

	public void setTransactionEpargnes(List<TransactionEpargne> transactionEpargnes) {
		this.transactionEpargnes = transactionEpargnes;
	}

	public TransactionEpargne addTransactionEpargne(TransactionEpargne transactionEpargne) {
		getTransactionEpargnes().add(transactionEpargne);
		transactionEpargne.setCompteCaisse(this);

		return transactionEpargne;
	}

	public TransactionEpargne removeTransactionEpargne(TransactionEpargne transactionEpargne) {
		getTransactionEpargnes().remove(transactionEpargne);
		transactionEpargne.setCompteCaisse(null);

		return transactionEpargne;
	}

	public List<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

}