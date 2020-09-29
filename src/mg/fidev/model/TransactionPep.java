package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="transactionPep")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionPep implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idTrans")
	private String idTransaction;

	@Column(name="date_transaction")
	private String dateTransaction;

	private String description;

	private double montant;
	
	private int periode;
	
	private double soldeProgressif;
	
	private double interet;

	@Column(name="piece_compta")
	private String pieceCompta;
	
	@Column(name="type_trans")
	private String typeTrans;
	
	@Column(name="type_paiement",nullable=false,length=50)
	private String typePaie;
	
	@Column(name="val_paie",nullable=true,length=50)
	private String valPaie;
	
	@Column(name="comm_retrait")
	private double commRet;
	
	@Column(name="comm_trans")
	private double commTrans;
	
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="id_user")
	private Utilisateur utilisateur;
	
	//bi-directional many-to-one association to Utilisateur
	@ManyToOne
	@JoinColumn(name="id_user_update")
	private Utilisateur userUpdate;

	//bi-directional many-to-one association to ComptePEP
	@ManyToOne
	@JoinColumn(name="idComptePep")
	private ComptePep comptePep;

	public TransactionPep() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionPep(String idTransaction, String dateTransaction,
			String description, double montant, int periode,
			double soldeProgressif, double interet, String pieceCompta,
			String typeTrans, double commRet, double commTrans,
			Utilisateur utilisateur, Utilisateur userUpdate, ComptePep comptePep) {
		super();
		this.idTransaction = idTransaction;
		this.dateTransaction = dateTransaction;
		this.description = description;
		this.montant = montant;
		this.periode = periode;
		this.soldeProgressif = soldeProgressif;
		this.interet = interet;
		this.pieceCompta = pieceCompta;
		this.typeTrans = typeTrans;
		this.commRet = commRet;
		this.commTrans = commTrans;
		this.utilisateur = utilisateur;
		this.userUpdate = userUpdate;
		this.comptePep = comptePep;
	}

	public String getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
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

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public double getSoldeProgressif() {
		return soldeProgressif;
	}

	public void setSoldeProgressif(double soldeProgressif) {
		this.soldeProgressif = soldeProgressif;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public String getPieceCompta() {
		return pieceCompta;
	}

	public void setPieceCompta(String pieceCompta) {
		this.pieceCompta = pieceCompta;
	}

	public String getTypeTrans() {
		return typeTrans;
	}

	public void setTypeTrans(String typeTrans) {
		this.typeTrans = typeTrans;
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

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(Utilisateur userUpdate) {
		this.userUpdate = userUpdate;
	}

	public ComptePep getComptePep() {
		return comptePep;
	}

	public void setComptePep(ComptePep comptePep) {
		this.comptePep = comptePep;
	}
}
