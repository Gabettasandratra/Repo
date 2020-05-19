package mg.fidev.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="soldeEpargne")
public class SoldeEpargne implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numCompte", required = false, nillable = true)
	private String numCompte;
	
	@XmlElement(name = "codeProd", required = false, nillable = true)
	private String codeProd;
	
	@XmlElement(name = "nomProd", required = false, nillable = true)
	private String nomPro;
	
	@XmlElement(name = "devise", required = false, nillable = true)
	private String devise;
	
	@XmlElement(name = "codeClient", required = false, nillable = true)
	private String codeClient;
	
	@XmlElement(name = "nomClient", required = false, nillable = true)
	private String nomClient;
	
	@XmlElement(name = "dateTrans", required = false, nillable = true)
	private String dateTrans;
	
	@XmlElement(name = "montant", required = false, nillable = true)
	private double montant;
	
	@XmlElement(name = "soldeFinPeriode", required = false, nillable = true)
	private double soldeFinPeriode;
	
	@XmlElement(name = "solde", required = false, nillable = true)
	private double solde;
	
	@XmlElement(name = "fermer", required = false, nillable = true)
	private String fermer;

	public SoldeEpargne() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(String numCompte) {
		this.numCompte = numCompte;
	}

	public String getCodeProd() {
		return codeProd;
	}

	public void setCodeProd(String codeProd) {
		this.codeProd = codeProd;
	}

	public String getNomPro() {
		return nomPro;
	}

	public void setNomPro(String nomPro) {
		this.nomPro = nomPro;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public String getDateTrans() {
		return dateTrans;
	}

	public void setDateTrans(String dateTrans) {
		this.dateTrans = dateTrans;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public String getFermer() {
		return fermer;
	}

	public void setFermer(String fermer) {
		this.fermer = fermer;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public double getSoldeFinPeriode() {
		return soldeFinPeriode;
	}

	public void setSoldeFinPeriode(double soldeFinPeriode) {
		this.soldeFinPeriode = soldeFinPeriode;
	}
	
}
