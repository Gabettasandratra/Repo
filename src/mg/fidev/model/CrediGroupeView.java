package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="credit_groupe_view")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CrediGroupeView implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	//montant principal par Membre
	private double principale;
	//montant intérêt par membres
	private double interet;
	//taux d'intérêt par membre
	private int tauxInt;
	//Numéro crédit
	private String numCredit;
	//Code Individuel
	private String codeInd;	
	//Code groupe
	private String codeGrp;
	public CrediGroupeView() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getTauxInt() {
		return tauxInt;
	}
	public void setTauxInt(int tauxInt) {
		this.tauxInt = tauxInt;
	}
	public String getNumCredit() {
		return numCredit;
	}
	public void setNumCredit(String numCredit) {
		this.numCredit = numCredit;
	}
	public String getCodeInd() {
		return codeInd;
	}
	public void setCodeInd(String codeInd) {
		this.codeInd = codeInd;
	}
	public String getCodeGrp() {
		return codeGrp;
	}
	public void setCodeGrp(String codeGrp) {
		this.codeGrp = codeGrp;
	}	
}
