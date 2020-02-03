package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="personnel_institution")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class Personnel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String codePersonnel;
	
	private String nom;
	
	private String prenom;
	
	@ManyToOne
	@JoinColumn(name="id_fonction")
	private Fonction fonction;
	
	@ManyToOne
	@JoinColumn(name="id_agence")
	private Agence agence;
	
	@OneToMany(mappedBy="agent")
	@XmlTransient
	private List<DemandeCredit> numCredit;
	

	public Personnel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getCodePersonnel() {
		return codePersonnel;
	}

	public void setCodePersonnel(String codePersonnel) {
		this.codePersonnel = codePersonnel;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Fonction getFonction() {
		return fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

	public Agence getAgence() {
		return agence;
	}

	public void setAgence(Agence agence) {
		this.agence = agence;
	}

	public List<DemandeCredit> getNumCredit() {
		return numCredit;
	}

	public void setNumCredit(List<DemandeCredit> numCredit) {
		this.numCredit = numCredit;
	}
	
}
