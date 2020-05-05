package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name="fonction_membre")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FonctionMembreGroupe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String nomFonction;

	@OneToMany(mappedBy="fonctionMembre",cascade= CascadeType.ALL)
	@XmlTransient 
	private List<MembreGroupe> membres;

	public FonctionMembreGroupe() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomFonction() {
		return nomFonction;
	}

	public void setNomFonction(String nomFonction) {
		this.nomFonction = nomFonction;
	}

	public List<MembreGroupe> getMembres() {
		return membres;
	}

	public void setMembres(List<MembreGroupe> membres) {
		this.membres = membres;
	}
	
}
