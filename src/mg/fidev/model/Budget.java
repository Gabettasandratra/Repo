package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="budget")
@NamedQuery(name="Budget.findAll", query="SELECT b FROM Budget b")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Budget implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="code",length=10)
	private String code;
	
	@Column(name="nom",length=50)
	private String nom;
	
	@Column(name="prevision")
	private double prevision;
	
	@Column(name="date", length=10)
	private String date;
	
	@Column(name="description", length=250)
	private String description;
	
	@Column(name="realisation")
	private double realisation;
	
	//relation un à plusieur avec l'entité grand livre	
	@OneToMany(mappedBy="budget",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Grandlivre> grandlivres;
	
	public Budget() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return code;
	}

	public void setId(String id) {
		this.code = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrevision() {
		return prevision;
	}

	public void setPrevision(double prevision) {
		this.prevision = prevision;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRealisation() {
		return realisation;
	}

	public void setRealisation(double realisation) {
		this.realisation += realisation;
	}

	public List<Grandlivre> getGrandlivres() {
		return grandlivres;
	}

	public void setGrandlivres(List<Grandlivre> grandlivres) {
		this.grandlivres = grandlivres;
	}
	
	
}
