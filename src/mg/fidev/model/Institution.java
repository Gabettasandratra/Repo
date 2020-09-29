package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="institution")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Institution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String nomInstitution;
	
	@Column(name="logo")
	private String logo;
	
	//bi-directional one to many association to Agences
	@OneToMany(mappedBy="institution")
	@XmlTransient
	private List<Agence> agences;

	public Institution() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomInstitution() {
		return nomInstitution;
	}

	public void setNomInstitution(String nomInstitution) {
		this.nomInstitution = nomInstitution;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<Agence> getAgences() {
		return agences;
	}

	public void setAgences(List<Agence> agences) {
		this.agences = agences;
	}
}
