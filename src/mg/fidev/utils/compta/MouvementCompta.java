package mg.fidev.utils.compta;

import java.io.Serializable;
import java.util.List;

import mg.fidev.model.Grandlivre;

public class MouvementCompta implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String libelle;
	
	private String compte;
	
	private List<Grandlivre> grandLivres;

	public MouvementCompta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public List<Grandlivre> getGrandLivres() {
		return grandLivres;
	}

	public void setGrandLivres(List<Grandlivre> grandLivres) {
		this.grandLivres = grandLivres;
	}
	
}
