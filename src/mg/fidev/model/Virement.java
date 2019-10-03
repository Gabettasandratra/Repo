package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the virement database table.
 * 
 */
@Entity
@NamedQuery(name="Virement.findAll", query="SELECT v FROM Virement v")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Virement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_virement")
	private int idVirement;

	@Column(name="date_virement")
	private String dateVirement;

	private double montant;

	@Column(name="piece_compta")
	private String pieceCompta;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="numCptAdebite")
	@XmlTransient
	private CompteEpargne compteEpargne1;

	//bi-directional many-to-one association to CompteEpargne
	@ManyToOne
	@JoinColumn(name="numCptAcredite")
	@XmlTransient
	private CompteEpargne compteEpargne2;

	public Virement() {
	}

	public int getIdVirement() {
		return this.idVirement;
	}

	public void setIdVirement(int idVirement) {
		this.idVirement = idVirement;
	}

	public String getDateVirement() {
		return this.dateVirement;
	}

	public void setDateVirement(String dateVirement) {
		this.dateVirement = dateVirement;
	}

	public double getMontant() {
		return this.montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public String getPieceCompta() {
		return this.pieceCompta;
	}

	public void setPieceCompta(String pieceCompta) {
		this.pieceCompta = pieceCompta;
	}

	public CompteEpargne getCompteEpargne1() {
		return this.compteEpargne1;
	}

	public void setCompteEpargne1(CompteEpargne compteEpargne1) {
		this.compteEpargne1 = compteEpargne1;
	}

	public CompteEpargne getCompteEpargne2() {
		return this.compteEpargne2;
	}

	public void setCompteEpargne2(CompteEpargne compteEpargne2) {
		this.compteEpargne2 = compteEpargne2;
	}

}