package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fichier_langue database table.
 * 
 */
@Entity
@Table(name="fichier_langue")
@NamedQuery(name="FichierLangue.findAll", query="SELECT f FROM FichierLangue f")
public class FichierLangue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String langue;

	private String pays;

	public FichierLangue() {
	}

	public String getLangue() {
		return this.langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}

	public String getPays() {
		return this.pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

}