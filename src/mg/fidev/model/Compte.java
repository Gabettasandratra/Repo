package mg.fidev.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the comptes database table.
 * 
 */
@Entity
@Table(name="comptes")
@NamedQuery(name="Compte.findAll", query="SELECT c FROM Compte c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Compte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger id;

	private boolean active;

	private String compte;

	private String devise;

	private boolean ferme;

	private int level;

	private String libelle;

	//bi-directional many-to-one association to Compte
	@ManyToOne
	@JoinColumn(name="parent_id")
	private Compte compteParent;

	//bi-directional many-to-one association to Compte
	@OneToMany(mappedBy="compteParent", cascade=CascadeType.REMOVE)
	@XmlTransient
	private List<Compte> comptes;

	@Column(name="solde_init")
	private double soldeInit;
	
	@Column(name="solde_progressif")
	private double soldeProgressif;

	private String type;

	public Compte() {
	}

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCompte() {
		return this.compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public String getDevise() {
		return this.devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public boolean getFerme() {
		return this.ferme;
	}

	public void setFerme(boolean ferme) {
		this.ferme = ferme;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Compte getCompteParent() {
		return compteParent;
	}

	public void setCompteParent(Compte compteParent) {
		this.compteParent = compteParent;
	}

	public List<Compte> getComptes() {
		return comptes;
	}

	public void setComptes(List<Compte> comptes) {
		this.comptes = comptes;
	}

	public double getSoldeInit() {
		return this.soldeInit;
	}

	public void setSoldeInit(double soldeInit) {
		this.soldeInit = soldeInit;
	}

	public double getSoldeProgressif() {
		return soldeProgressif;
	}

	public void setSoldeProgressif(double soldeProgressif) {
		this.soldeProgressif = soldeProgressif;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}