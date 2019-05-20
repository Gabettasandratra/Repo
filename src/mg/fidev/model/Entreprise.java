package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the entreprise database table.
 * 
 */
@Entity
@NamedQuery(name="Entreprise.findAll", query="SELECT e FROM Entreprise e")
public class Entreprise implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String codeCliE;

	private String contactE;

	@Temporal(TemporalType.DATE)
	private Date dateInscritE;

	private String nomE;

	//bi-directional many-to-one association to Adresse
	@ManyToOne
	@JoinColumn(name="idAdresse")
	private Adresse adresse;

	public Entreprise() {
	}

	public String getCodeCliE() {
		return this.codeCliE;
	}

	public void setCodeCliE(String codeCliE) {
		this.codeCliE = codeCliE;
	}

	public String getContactE() {
		return this.contactE;
	}

	public void setContactE(String contactE) {
		this.contactE = contactE;
	}

	public Date getDateInscritE() {
		return this.dateInscritE;
	}

	public void setDateInscritE(Date dateInscritE) {
		this.dateInscritE = dateInscritE;
	}

	public String getNomE() {
		return this.nomE;
	}

	public void setNomE(String nomE) {
		this.nomE = nomE;
	}

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

}