package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fichier_docidentite database table.
 * 
 */
@Entity
@Table(name="fichier_docidentite")
@NamedQuery(name="FichierDocidentite.findAll", query="SELECT f FROM FichierDocidentite f")
public class FichierDocidentite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String codeDocument;

	private String delivrePar;

	private String format;

	private boolean isObligatoire;

	private boolean isUnique;

	private int largeur;

	private String nom;

	public FichierDocidentite() {
	}

	public String getCodeDocument() {
		return this.codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	public String getDelivrePar() {
		return this.delivrePar;
	}

	public void setDelivrePar(String delivrePar) {
		this.delivrePar = delivrePar;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean getIsObligatoire() {
		return this.isObligatoire;
	}

	public void setIsObligatoire(boolean isObligatoire) {
		this.isObligatoire = isObligatoire;
	}

	public boolean getIsUnique() {
		return this.isUnique;
	}

	public void setIsUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public int getLargeur() {
		return this.largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}