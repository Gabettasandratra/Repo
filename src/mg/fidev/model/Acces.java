package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the acces database table.
 * 
 */
@Entity
@NamedQuery(name="Acces.findAll", query="SELECT a FROM Acces a")
public class Acces implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idAcces;

	private String titreAcces;

	//bi-directional many-to-many association to Fonction
	@ManyToMany(mappedBy="acces")
	private List<Fonction> fonctions;

	public Acces() {
	}

	public int getIdAcces() {
		return this.idAcces;
	}

	public void setIdAcces(int idAcces) {
		this.idAcces = idAcces;
	}

	public String getTitreAcces() {
		return this.titreAcces;
	}

	public void setTitreAcces(String titreAcces) {
		this.titreAcces = titreAcces;
	}

	public List<Fonction> getFonctions() {
		return this.fonctions;
	}

	public void setFonctions(List<Fonction> fonctions) {
		this.fonctions = fonctions;
	}

}