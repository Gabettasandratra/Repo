package mg.fidev.xmlRequest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AdresseXml", namespace = "http://individuel.fidev.mg")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdresseXml implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "adresseCommune",  required = false, nillable = true)
	private String adresseCommune;
	@XmlElement(name = "adresseDistrict",  required = false, nillable = true)
	private String adresseDistrict;
	@XmlElement(name = "adressePhysique", required = true)
	private String adressePhysique;
	@XmlElement(name = "adresseRegion",  required = false, nillable = true)
	private String adresseRegion;
	@XmlElement(name = "adresseVille",  required = false, nillable = true)
	private String adresseVille;
	@XmlElement(name = "distanceAgence", required = false, nillable = true)
	private int distanceAgence;
	@XmlElement(name = "codeRegion", required = true)
	private int codeRegion;
	
	public String getAdresseCommune() {
		return adresseCommune;
	}
	public void setAdresseCommune(String adresseCommune) {
		this.adresseCommune = adresseCommune;
	}
	public String getAdresseDistrict() {
		return adresseDistrict;
	}
	public void setAdresseDistrict(String adresseDistrict) {
		this.adresseDistrict = adresseDistrict;
	}
	public String getAdressePhysique() {
		return adressePhysique;
	}
	public void setAdressePhysique(String adressePhysique) {
		this.adressePhysique = adressePhysique;
	}
	public String getAdresseRegion() {
		return adresseRegion;
	}
	public void setAdresseRegion(String adresseRegion) {
		this.adresseRegion = adresseRegion;
	}
	public String getAdresseVille() {
		return adresseVille;
	}
	public void setAdresseVille(String adresseVille) {
		this.adresseVille = adresseVille;
	}
	public int getDistanceAgence() {
		return distanceAgence;
	}
	public void setDistanceAgence(int distanceAgence) {
		this.distanceAgence = distanceAgence;
	}
	public int getCodeRegion() {
		return codeRegion;
	}
	public void setCodeRegion(int codeRegion) {
		this.codeRegion = codeRegion;
	}
	
}
