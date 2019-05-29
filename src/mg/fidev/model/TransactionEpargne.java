package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the transaction_epargne database table.
 * 
 */
@Entity
@Table(name="transaction_epargne")
@NamedQuery(name="TransactionEpargne.findAll", query="SELECT t FROM TransactionEpargne t")
public class TransactionEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_transaction_ep")
	private int idTransactionEp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_transaction")
	private Date dateTransaction;

	private String description;

	private double montant;

	@Column(name="pièce_compta")
	private String pièceCompta;

	@Column(name="type_trans_ep")
	private String typeTransEp;

	//bi-directional many-to-one association to CompteCaisse
	@ManyToOne
	@JoinColumn(name="Compte_caissenom_cpt_caisse")
	private CompteCaisse compteCaisse;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="Compte_epargnenum_compte_ep")
	private CompteEpargne compteEpargne;

	public TransactionEpargne() {
	}

	public int getIdTransactionEp() {
		return this.idTransactionEp;
	}

	public void setIdTransactionEp(int idTransactionEp) {
		this.idTransactionEp = idTransactionEp;
	}

	public Date getDateTransaction() {
		return this.dateTransaction;
	}

	public void setDateTransaction(Date dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMontant() {
		return this.montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public String getPièceCompta() {
		return this.pièceCompta;
	}

	public void setPièceCompta(String pièceCompta) {
		this.pièceCompta = pièceCompta;
	}

	public String getTypeTransEp() {
		return this.typeTransEp;
	}

	public void setTypeTransEp(String typeTransEp) {
		this.typeTransEp = typeTransEp;
	}

	public CompteCaisse getCompteCaisse() {
		return this.compteCaisse;
	}

	public void setCompteCaisse(CompteCaisse compteCaisse) {
		this.compteCaisse = compteCaisse;
	}

	public CompteEpargne getCompteEpargne() {
		return this.compteEpargne;
	}

	public void setCompteEpargne(CompteEpargne compteEpargne) {
		this.compteEpargne = compteEpargne;
	}

}