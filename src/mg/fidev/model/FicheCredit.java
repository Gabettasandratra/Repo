package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="fiche_credit")
@XmlRootElement(name="ficheCreds")
@XmlAccessorType(XmlAccessType.FIELD)
public class FicheCredit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String date;
	
	private String transaction;
	
	private String piece;
	
	private double principale;
	
	private double interet;
	
	private double penalite;
	
	private double soldeCourant;
	
	private double totalPrincipal;
	
	private double totalInteret;
	
	private double totalSolde;
	
	@Column(name="num_credit")
	private String num_credit;
	
	public FicheCredit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public double getPrincipale() {
		return principale;
	}

	public void setPrincipale(double principale) {
		this.principale = principale;
	}

	public double getInteret() {
		return interet;
	}

	public void setInteret(double interet) {
		this.interet = interet;
	}

	public double getPenalite() {
		return penalite;
	}

	public void setPenalite(double penalite) {
		this.penalite = penalite;
	}

	public double getSoldeCourant() {
		return soldeCourant;
	}

	public void setSoldeCourant(double soldeCourant) {
		this.soldeCourant = soldeCourant;
	}

	public double getTotalPrincipal() {
		return totalPrincipal;
	}

	public void setTotalPrincipal(double totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	public double getTotalInteret() {
		return totalInteret;
	}

	public void setTotalInteret(double totalInteret) {
		this.totalInteret = totalInteret;
	}

	public String getNum_credit() {
		return num_credit;
	}

	public void setNum_credit(String num_credit) {
		this.num_credit = num_credit;
	}
	
	
	public double getTotalSolde() {
		return totalSolde;
	}

	public void setTotalSolde(double totalSolde) {
		this.totalSolde = totalSolde;
	}
	
	
}
