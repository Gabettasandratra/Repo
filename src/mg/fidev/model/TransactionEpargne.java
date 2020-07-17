package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the transaction_epargne database table.
 * 
 */
@Entity
@Table(name="transaction_epargne")
@NamedQuery(name="TransactionEpargne.findAll", query="SELECT t FROM TransactionEpargne t")
@XmlRootElement(name="transEp")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionEpargne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="tcode_ep")
	private String idTransactionEp;

	@Column(name="date_transaction")
	private String dateTransaction;

	private String description;

	private double montant;

	@Column(name="piece_compta")
	private String pieceCompta;

	private double solde;

	@Column(name="type_trans_ep")
	private String typeTransEp;
	
	@Column(name="type_paiement",nullable=false,length=50)
	private String typePaie;
	@Column(name="val_paie",nullable=true,length=50)
	private String valPaie;
	@Column(name="comm_retrait")
	private double commRet;
	@Column(name="comm_trans")
	private double commTrans;
	@Column(name="commPrelev")
	private double penalPrelev;
	//bi-directional many-to-one association to CompteCaisse
	@ManyToOne
	@JoinColumn(name="id_caisse")
	@XmlTransient
	private Caisse caisse;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="Compte_epargnenum_compte_ep")
	private CompteEpargne compteEpargne;

	public TransactionEpargne() {
	}
	
	public TransactionEpargne(String idTransactionEp, String dateTransaction,
			String description, double montant, String pieceCompta, String typeTransEp, String typePaie, String valPaie,
			double commRet, double commTrans, double penalPrelev,
			Caisse caisse, CompteEpargne compteEpargne) {
		super();
		this.idTransactionEp = idTransactionEp;
		this.dateTransaction = dateTransaction;
		this.description = description;
		this.montant = montant;
		this.pieceCompta = pieceCompta;
		this.typeTransEp = typeTransEp;
		this.typePaie = typePaie;
		this.valPaie = valPaie;
		this.commRet = commRet;
		this.commTrans = commTrans;
		this.penalPrelev = penalPrelev;
		this.caisse = caisse;
		this.compteEpargne = compteEpargne;
	}
	
	public String getIdTransactionEp() {
		return this.idTransactionEp;
	}

	public void setIdTransactionEp(String idTransactionEp) {
		this.idTransactionEp = idTransactionEp;
	}

	public String getDateTransaction() {
		return this.dateTransaction;
	}

	public void setDateTransaction(String dateTransaction) {
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

	public String getPieceCompta() {
		return this.pieceCompta;
	}

	public void setPieceCompta(String pieceCompta) {
		this.pieceCompta = pieceCompta;
	}

	public String getTypePaie() {
		return typePaie;
	}

	public void setTypePaie(String typePaie) {
		this.typePaie = typePaie;
	}

	public String getValPaie() {
		return valPaie;
	}

	public void setValPaie(String valPaie) {
		this.valPaie = valPaie;
	}

	public double getCommRet() {
		return commRet;
	}

	public void setCommRet(double commRet) {
		this.commRet = commRet;
	}

	public double getCommTrans() {
		return commTrans;
	}

	public void setCommTrans(double commTrans) {
		this.commTrans = commTrans;
	}

	public double getPenalPrelev() {
		return penalPrelev;
	}

	public void setPenalPrelev(double penalPrelev) {
		this.penalPrelev = penalPrelev;
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
	
	public Caisse getCaisse() {
		return caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
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