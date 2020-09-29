package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the fichier_regiongeo database table.
 * 
 */
@Entity
@Table(name="fichier_regiongeo")
//@NamedQuery(name="FichierRegiongeo.findAll", query="SELECT f FROM FichierRegiongeo f")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FichierRegiongeo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codeCommune;

	private String nomCommune;
	
	private String district;

	private String region;
	
	private String ville;
	
	public FichierRegiongeo() {
	}

	public String getCodeCommune() {
		return codeCommune;
	}

	public void setCodeCommune(String codeCommune) {
		this.codeCommune = codeCommune;
	}

	public String getNomCommune() {
		return nomCommune;
	}

	public void setNomCommune(String nomCommune) {
		this.nomCommune = nomCommune;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
}