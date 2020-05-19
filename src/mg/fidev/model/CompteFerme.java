package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


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
	
	@Column(name="date_cloture")
	private String dateCloture;

	@Column(name="frais_cloture")
	private double fraisCloture;

	private String numRecue;

	private String raison;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="num_compte")
	private CompteEpargne compteEpargne;

	public CompteFerme() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateCloture() {
		return this.dateCloture;
	}

	public void setDateCloture(String dateCloture) {
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