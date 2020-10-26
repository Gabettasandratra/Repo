package mg.fidev.utils.compta;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="position6")
public class PositionCompta6 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCompte", required = false, nillable = true)
	private String numCompte;
	
	@XmlElement(name = "libele", required = false, nillable = true)
	private String libele;
	
	@XmlElement(name = "isActive", required = false, nillable = true)
	private boolean isActive;
	
	@XmlElement(name = "devise", required = false, nillable = true)
	private String devise;
	
	@XmlElement(name = "pos7", required = false, nillable = true)
	private List<PositionCompta7> pos7;

	public PositionCompta6() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PositionCompta6(String numCompte, String libele, boolean isActive,
			String devise, List<PositionCompta7> pos7) {
		super();
		this.numCompte = numCompte;
		this.libele = libele;
		this.isActive = isActive;
		this.devise = devise;
		this.pos7 = pos7;
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

	public List<PositionCompta7> getPos7() {
		return pos7;
	}

	public void setPos7(List<PositionCompta7> pos7) {
		this.pos7 = pos7;
	}

}
