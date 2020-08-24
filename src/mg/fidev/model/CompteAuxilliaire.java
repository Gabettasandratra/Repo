package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name="compteAuxilliaire")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompteAuxilliaire implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id",length=10)
	private String id;
	
	@Column(name="nom",length=200)
	private String nom;	
	
	//relation un à plusieur avec l'entité grand livre	
	@OneToMany(mappedBy="auxilliaire")
	@XmlTransient
	private List<Grandlivre> grandlivres;
	
	public CompteAuxilliaire() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Grandlivre> getGrandlivres() {
		return grandlivres;
	}

	public void setGrandlivres(List<Grandlivre> grandlivres) {
		this.grandlivres = grandlivres;
	}

}
