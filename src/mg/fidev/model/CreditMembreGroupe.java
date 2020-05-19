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

@Entity
@Table(name="montant_credit_membre")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreditMembreGroupe implements Serializable {
	
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
	
	//numéro crédit
	@ManyToOne
	@JoinColumn(name="numCredit")
	private DemandeCredit demandeCredit;
	
	//code du membre
	@ManyToOne
	@JoinColumn(name="codeInd")
	private Individuel individuel;
	
	//code groupe
	@ManyToOne
	@JoinColumn(name="codeGrp")
	private Groupe groupe;

	public CreditMembreGroupe() {
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

	public DemandeCredit getDemandeCredit() {
		return demandeCredit;
	}

	public void setDemandeCredit(DemandeCredit demandeCredit) {
		this.demandeCredit = demandeCredit;
	}

	public Individuel getIndividuel() {
		return individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
	
}
