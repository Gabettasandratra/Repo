package mg.fidev.utils.compta;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="rubriqueCompta")
public class RubriqueCompta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCompte", required = false, nillable = true)
	private String numCompte;
	
	@XmlElement(name = "libele", required = false, nillable = true)
	private String libele;
	
	@XmlElement(name = "isActive", required = false, nillable = true)
	private boolean isActive;
	
	@XmlElement(name = "devise", required = false, nillable = true)
	private String devise;

	@XmlElement(name = "comptes", required = false, nillable = true)
	private List<CompteCompta> comptes;

	public RubriqueCompta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RubriqueCompta(String numCompte, String libele, boolean isActive,
			String devise, List<CompteCompta> comptes) {
		super();
		this.numCompte = numCompte;
		this.libele = libele;
		this.isActive = isActive;
		this.devise = devise;
		this.comptes = comptes;
	}

	public String getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(String numCompte) {
		this.numCompte = numCompte;
	}

	public String getLibele() {
		return libele;
	}

	public void setLibele(String libele) {
		this.libele = libele;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public List<CompteCompta> getComptes() {
		return comptes;
	}

	public void setComptes(List<CompteCompta> comptes) {
		this.comptes = comptes;
	}

}
