package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fichier_niveauetude database table.
 * 
 */
@Entity
@Table(name="fichier_niveauetude")
@NamedQuery(name="FichierNiveauetude.findAll", query="SELECT f FROM FichierNiveauetude f")
public class FichierNiveauetude implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String niveauEtude;

	private String description;

	public FichierNiveauetude() {
	}

	public String getNiveauEtude() {
		return this.niveauEtude;
	}

	public void setNiveauEtude(String niveauEtude) {
		this.niveauEtude = niveauEtude;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}