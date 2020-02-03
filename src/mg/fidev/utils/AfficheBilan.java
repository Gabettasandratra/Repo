package mg.fidev.utils;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="afficheBilan")
public class AfficheBilan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "compteParent", required = false, nillable = true)
	private String compteParent;
	
	@XmlElement(name = "listCompte", required = false, nillable = true)
	private List<AfficheBalance> listCompte;

	public AfficheBilan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCompteParent() {
		return compteParent;
	}

	public void setCompteParent(String compteParent) {
		this.compteParent = compteParent;
	}

	public List<AfficheBalance> getListCompte() {
		return listCompte;
	}

	public void setListCompte(List<AfficheBalance> listCompte) {
		this.listCompte = listCompte;
	}
	
}
