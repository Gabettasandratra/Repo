package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the docidentite database table.
 * 
 */
@Entity
@NamedQuery(name="Docidentite.findAll", query="SELECT d FROM Docidentite d")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Docidentite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String numero;

	//@Temporal(TemporalType.DATE)
	private String dateEmis;

	//@Temporal(TemporalType.DATE)
	private String dateExpire;

	private String delivrePar;

	private int priorite;

	private String typedoc;
	
	@Column(name="recto")
	private String recto;
	
	@Column(name="verso")
	private String verso;

	//bi-directional many-to-one association to Individuel
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="codeClient")
	private Individuel individuel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="codeGarant")
	private Garant garant;

	public Docidentite() {
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDateEmis() {
		return this.dateEmis;
	}

	public void setDateEmis(String dateEmis) {
		this.dateEmis = dateEmis;
	}

	public String getDateExpire() {
		return this.dateExpire;
	}

	public void setDateExpire(String dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getDelivrePar() {
		return this.delivrePar;
	}

	public void setDelivrePar(String delivrePar) {
		this.delivrePar = delivrePar;
	}

	public int getPriorite() {
		return this.priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public String getTypedoc() {
		return this.typedoc;
	}

	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}

	public String getRecto() {
		return recto;
	}

	public void setRecto(String recto) {
		this.recto = recto;
	}

	public String getVerso() {
		return verso;
	}

	public void setVerso(String verso) {
		this.verso = verso;
	}

	public Individuel getIndividuel() {
		return this.individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

	public Garant getGarant() {
		return garant;
	}

	public void setGarant(Garant garant) {
		this.garant = garant;
	}
	
	

}