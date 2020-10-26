package mg.fidev.utils.compta;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="position7")
public class PositionCompta7 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCompte", required = false, nillable = true)
	private String numCompte;
	
	@XmlElement(name = "libele", required = false, nillable = true)
	private String libele;
	
	@XmlElement(name = "isActive", required = false, nillable = true)
	private boolean isActive;
	
	@XmlElement(name = "devise", required = false, nillable = true)
	private String devise;
	
	@XmlElement(name = "pos8", required = false, nillable = true)
	private List<PositionCompta8> pos8;

	public PositionCompta7() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PositionCompta7(String numCompte, String libele, boolean isActive,
			String devise, List<PositionCompta8> pos8) {
		super();
		this.numCompte = numCompte;
		this.libele = libele;
		this.isActive = isActive;
		this.devise = devise;
		this.pos8 = pos8;
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

	public List<PositionCompta8> getPos8() {
		return pos8;
	}

	public void setPos8(List<PositionCompta8> pos8) {
		this.pos8 = pos8;
	}

}
