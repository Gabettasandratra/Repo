package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="droitinscription")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DroitInscription implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rowId;
	
	private String date_paye;
	
	private String piece;
	
	private double droits;
	
	private double fraisDossier;
	private double fraisAdmin;
	private String compteCaisse;
	
	@ManyToOne
	@JoinColumn(name="codeInd")
	@XmlTransient
	private Individuel codeInd;
	
	@ManyToOne
	@JoinColumn(name="codeGrp")
	@XmlTransient
	private Groupe groupe;
	public DroitInscription() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public String getDate_paye() {
		return date_paye;
	}
	public void setDate_paye(String date_paye) {
		this.date_paye = date_paye;
	}
	public String getPiece() {
		return piece;
	}
	public void setPiece(String piece) {
		this.piece = piece;
	}
	public double getDroits() {
		return droits;
	}
	public void setDroits(double droits) {
		this.droits = droits;
	}
	public double getFraisDossier() {
		return fraisDossier;
	}
	public void setFraisDossier(double fraisDossier) {
		this.fraisDossier = fraisDossier;
	}
	public double getFraisAdmin() {
		return fraisAdmin;
	}
	public void setFraisAdmin(double fraisAdmin) {
		this.fraisAdmin = fraisAdmin;
	}
	public String getCompteCaisse() {
		return compteCaisse;
	}
	public void setCompteCaisse(String compteCaisse) {
		this.compteCaisse = compteCaisse;
	}
	public Individuel getCodeInd() {
		return codeInd;
	}
	public void setCodeInd(Individuel codeInd) {
		this.codeInd = codeInd;
	}
	public Groupe getGroupe() {
		return groupe;
	}
	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
	
}
