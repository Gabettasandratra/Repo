package mg.fidev.xmlRequest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ProduitCredit", namespace = "http://fidev.mg.creditProduitService")
public class ProduitCreditXML implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name="idProdCredit", required=false, nillable=true)
	private String idProdCredit;
	@XmlElement(name="etat", required=true)
	private boolean etat;
	@XmlElement(name="nomProdCredit", required=true)
	private String nomProdCredit;
	public ProduitCreditXML() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIdProdCredit() {
		return idProdCredit;
	}
	public void setIdProdCredit(String idProdCredit) {
		this.idProdCredit = idProdCredit;
	}
	public boolean isEtat() {
		return etat;
	}
	public void setEtat(boolean etat) {
		this.etat = etat;
	}
	public String getNomProdCredit() {
		return nomProdCredit;
	}
	public void setNomProdCredit(String nomProdCredit) {
		this.nomProdCredit = nomProdCredit;
	}
	/*  try {
	
	LocalDate date1 = LocalDate.parse(dateDeb);
	LocalDate date2 = LocalDate.parse(dateFin);
	System.out.println("date début "+date1);
	System.out.println("date fin "+date2);

	Long val = ChronoUnit.MONTHS.between(date1, date2);			
	
	System.out.println("Diferrence de mois "+val + "\n");
	
	String mois = dateDeb.substring(0,6);    
	
	if(!dateDeb.equals("") && dateFin.equals("")){
		String sql = "select sum(g.debit) from Grandlivre g join g.account a where "
				+ " a.numCpt ";
		Query q = em.createQuery("");
	}
	if(!dateDeb.equals("") && !dateFin.equals("")){
		
	}
	
} catch (Exception e) {
	e.printStackTrace();
}*/


}
