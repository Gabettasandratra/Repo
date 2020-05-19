package mg.fidev.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="afficheBalance")
public class AfficheBalance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCompte", required = false, nillable = true)
	private String numCompte;
	
	@XmlElement(name = "libele", required = false, nillable = true)
	private String libele;
	
	@XmlElement(name = "soldeInit", required = false, nillable = true)
	private double soldeInit;
	
	@XmlElement(name = "sommeDeb", required = false, nillable = true)
	private double sommeDeb;
	
	@XmlElement(name = "sommeCred", required = false, nillable = true)
	private double sommeCred;
	
	@XmlElement(name = "soldeFin", required = false, nillable = true)
	private double soldeFin;
	
	@XmlElement(name = "stat", required = false, nillable = true)
	private String stat;

	public AfficheBalance() {
		super();
		// TODO Auto-generated constructor stub
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

	public double getSoldeInit() {
		return soldeInit;
	}

	public void setSoldeInit(double soldeInit) {
		this.soldeInit = soldeInit;
	}

	public double getSommeDeb() {
		return sommeDeb;
	}

	public void setSommeDeb(double sommeDeb) {
		this.sommeDeb = sommeDeb;
	}

	public double getSommeCred() {
		return sommeCred;
	}

	public void setSommeCred(double sommeCred) {
		this.sommeCred = sommeCred;
	}

	public double getSoldeFin() {
		return soldeFin;
	}

	public void setSoldeFin(double soldeFin) {
		this.soldeFin = soldeFin;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

}
