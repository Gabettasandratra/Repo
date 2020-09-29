package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the fichier_niveauetude database table.
 * 
 */
@Entity
@Table(name="fichier_niveauetude")
@NamedQuery(name="FichierNiveauetude.findAll", query="SELECT f FROM FichierNiveauetude f")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FichierNiveauetude implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String niveauEtude;

	private String description;

	public FichierNiveauetude() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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