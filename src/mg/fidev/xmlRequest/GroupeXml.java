package mg.fidev.xmlRequest;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AdresseXml", namespace = "http://groupe.fidev.mg")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupeXml implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "rowId", required = false, nillable = true)
	private int rowId;
	@XmlElement(name = "codeAgence", required = true)
	private String codeAgence;
	@XmlElement(name = "codeClient",  required = false, nillable = true)
	private String codeClient;
	@XmlElement(name = "dateInscription",  required = true)
	private Date dateInscription;
	@XmlElement(name = "email",  required = false, nillable = true)
	private String email;
	@XmlElement(name = "nomGroupe",  required = true)
	private String nomGroupe;
	@XmlElement(name = "numeroMobile",  required = false, nillable = true)
	private String numeroMobile;
	@XmlElement(name = "adresse",  required = false, nillable = true)
	private AdresseXml adresse;
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public String getCodeAgence() {
		return codeAgence;
	}
	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public Date getDateInscription() {
		return dateInscription;
	}
	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNomGroupe() {
		return nomGroupe;
	}
	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}
	public String getNumeroMobile() {
		return numeroMobile;
	}
	public void setNumeroMobile(String numeroMobile) {
		this.numeroMobile = numeroMobile;
	}
	public AdresseXml getAdresse() {
		return adresse;
	}
	public void setAdresse(AdresseXml adresse) {
		this.adresse = adresse;
	}

}
