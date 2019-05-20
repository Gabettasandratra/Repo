package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fichier_regiongeo database table.
 * 
 */
@Entity
@Table(name="fichier_regiongeo")
@NamedQuery(name="FichierRegiongeo.findAll", query="SELECT f FROM FichierRegiongeo f")
public class FichierRegiongeo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int codeRegion;

	private String region;

	public FichierRegiongeo() {
	}

	public int getCodeRegion() {
		return this.codeRegion;
	}

	public void setCodeRegion(int codeRegion) {
		this.codeRegion = codeRegion;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}