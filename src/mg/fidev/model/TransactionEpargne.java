package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;

import java.time.LocalDateTime;


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

	@Column(name="date_transaction")
	private String dateTransaction;

	private String description;

	private double montant;

	@Column(name="piece_compta")
	private String pieceCompta;

	private double solde;

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

	public String getDateTransaction() {
		return this.dateTransaction;
	}

	public void setDateTransaction(String dateTransaction) {
		LocalDateTime dt = LocalDateTime.parse(dateTransaction);
		this.dateTransaction = dt.toString();
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

	public String getPieceCompta() {
		return this.pieceCompta;
	}

	public void setPieceCompta(String pieceCompta) {
		this.pieceCompta = pieceCompta;
	}

	public double getSolde() {
		return this.solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
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
		if(compteEpargne.getIsActif()){
			if(this.getTypeTransEp().equals("DE")){
				compteEpargne.setSolde(compteEpargne.getSolde() + montant);
				if(compteEpargne.getSolde() != 0){
					this.setSolde(compteEpargne.getSolde());
					this.setMontant(montant);
					this.compteEpargne = compteEpargne;
				}
				else{
					System.err.println("Erreur de transaction");
				}
			}
			else if(this.getTypeTransEp().equals("RE")){
				compteEpargne.setSolde(compteEpargne.getSolde() - montant);
				if(compteEpargne.getSolde() != 0){
					this.setSolde(compteEpargne.getSolde());
					this.setMontant(montant);
					this.compteEpargne = compteEpargne;
				}
				else{
					System.err.println("Erreur de transaction");
				}
			}
		}
		else
			System.err.println("Compte inactif");
	}

}