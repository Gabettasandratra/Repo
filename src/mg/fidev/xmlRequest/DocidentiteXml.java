package mg.fidev.xmlRequest;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "DocidentiteXml", namespace = "http://individuel.fidev.mg")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocidentiteXml implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "numero", required = true)
	protected String numero;
	
	@XmlElement(name = "dateEmis", required = true)
	protected Date dateEmis;
	
	@XmlElement(name = "dateExpire", required = true)
	protected Date dateExpire;
	
	@XmlElement(name = "delivrePar", required = false)
	protected String delivrePar;
	
	@XmlElement(name = "priorite", required = true)
	protected int priorite;
	
	@XmlElement(name = "typedoc", required = true)
	protected String typedoc;
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getDateEmis() {
		return dateEmis;
	}
	public void setDateEmis(Date dateEmis) {
		this.dateEmis = dateEmis;
	}
	public Date getDateExpire() {
		return dateExpire;
	}
	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}
	public String getDelivrePar() {
		return delivrePar;
	}
	public void setDelivrePar(String delivrePar) {
		this.delivrePar = delivrePar;
	}
	public int getPriorite() {
		return priorite;
	}
	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}
	public String getTypedoc() {
		return typedoc;
	}
	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}

}
