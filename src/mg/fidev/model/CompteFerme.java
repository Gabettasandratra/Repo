package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.Date;


/**
 * The persistent class for the compte_ferme database table.
 * 
 */
@Entity
@Table(name="compte_ferme")
@NamedQuery(name="CompteFerme.findAll", query="SELECT c FROM CompteFerme c")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteFerme implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_cloture")
	private Date dateCloture;

	@Column(name="frais_cloture")
	private double fraisCloture;

	private String numRecue;

	private String raison;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="num_compte")
	@XmlTransient
	private CompteEpargne compteEpargne;

	public CompteFerme() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateCloture() {
		return this.dateCloture;
	}

	public void setDateCloture(Date dateCloture) {
		this.dateCloture = dateCloture;
	}

	public double getFraisCloture() {
		return this.fraisCloture;
	}

	public void setFraisCloture(double fraisCloture) {
		this.fraisCloture = fraisCloture;
	}

	public String getNumRecue() {
		return this.numRecue;
	}

	public void setNumRecue(String numRecue) {
		this.numRecue = numRecue;
	}

	public String getRaison() {
		return this.raison;
	}

	public void setRaison(String raison) {
		this.raison = raison;
	}

	public CompteEpargne getCompteEpargne() {
		return this.compteEpargne;
	}

	public void setCompteEpargne(CompteEpargne compteEpargne) {
		this.compteEpargne = compteEpargne;
	}

}