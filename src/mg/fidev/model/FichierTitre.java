package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fichier_titre database table.
 * 
 */
@Entity
@Table(name="fichier_titre")
@NamedQuery(name="FichierTitre.findAll", query="SELECT f FROM FichierTitre f")
public class FichierTitre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String titre;

	private String description;

	public FichierTitre() {
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}