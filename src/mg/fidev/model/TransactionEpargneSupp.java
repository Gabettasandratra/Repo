package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="transaction_epargne_supprimer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionEpargneSupp implements Serializable {

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
	
	private String utilisateur;
	
	private String compteEpargne;
	
	private String codeClient;
	
	private String idProduit;

	private String codeAgence;
	
	public TransactionEpargneSupp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionEpargneSupp(String idTransactionEp,
			String dateTransaction, String description, double montant,
			String pieceCompta, double solde, String typeTransEp,
			String typePaie, String valPaie, double commRet, double commTrans,
			double penalPrelev, String utilisateur, String compteEpargne) {
		super();
		this.idTransactionEp = idTransactionEp;
		this.dateTransaction = dateTransaction;
		this.description = description;
		this.montant = montant;
		this.pieceCompta = pieceCompta;
		this.solde = solde;
		this.typeTransEp = typeTransEp;
		this.typePaie = typePaie;
		this.valPaie = valPaie;
		this.commRet = commRet;
		this.commTrans = commTrans;
		this.penalPrelev = penalPrelev;
		this.utilisateur = utilisateur;
		this.compteEpargne = compteEpargne;
	}

	public String getIdTransactionEp() {
		return idTransactionEp;
	}

	public void setIdTransactionEp(String idTransactionEp) {
		this.idTransactionEp = idTransactionEp;
	}

	public String getDateTransaction() {
		return dateTransaction;
	}

	public void setDateTransaction(String dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public String getPieceCompta() {
		return pieceCompta;
	}

	public void setPieceCompta(String pieceCompta) {
		this.pieceCompta = pieceCompta;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public String getTypeTransEp() {
		return typeTransEp;
	}

	public void setTypeTransEp(String typeTransEp) {
		this.typeTransEp = typeTransEp;
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

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getCompteEpargne() {
		return compteEpargne;
	}

	public void setCompteEpargne(String compteEpargne) {
		this.compteEpargne = compteEpargne;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	public String getCodeAgence() {
		return codeAgence;
	}

	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}

}
