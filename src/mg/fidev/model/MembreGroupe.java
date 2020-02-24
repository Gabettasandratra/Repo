package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="membre_groupe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MembreGroupe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String idMembre;
	
	private double capitaleCredit;
	
	private int tauxInteretCredit;
	
	private String fonction;
	
	@ManyToOne
	@JoinColumn(name="code_individuel")
	private Individuel individuel;
	

	@ManyToOne
	@JoinColumn(name="code_groupe")
	private Groupe groupe;


	public MembreGroupe() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getIdMembre() {
		return idMembre;
	}


	public void setIdMembre(String idMembre) {
		this.idMembre = idMembre;
	}


	public double getCapitaleCredit() {
		return capitaleCredit;
	}


	public void setCapitaleCredit(double capitaleCredit) {
		this.capitaleCredit = capitaleCredit;
	}


	public int getTauxInteretCredit() {
		return tauxInteretCredit;
	}


	public void setTauxInteretCredit(int tauxInteretCredit) {
		this.tauxInteretCredit = tauxInteretCredit;
	}


	public String getFonction() {
		return fonction;
	}


	public void setFonction(String fonction) {
		this.fonction = fonction;
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
