package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="cloture")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class Cloture implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	
	@Column(name="dateSave")
	private String dateSave;
	
	@Column(name="dateDebut")
	private String dateDebut;

	@Column(name="dateFin")
	private String dateFin;
	
	private boolean jour; 

	private boolean mois;
	
	private boolean annee; 
	
	//Relation one to many to Cloture compte
	@OneToMany(mappedBy="cloture")
	@XmlTransient
	private List<ClotureCompte> clotures;
	
	//Relation many to one to utilisateur
	@ManyToOne
	@JoinColumn(name="id_user")
	private Utilisateur user;

	public Cloture() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDateSave() {
		return dateSave;
	}

	public void setDateSave(String dateSave) {
		this.dateSave = dateSave;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public List<ClotureCompte> getClotures() {
		return clotures;
	}

	public void setClotures(List<ClotureCompte> clotures) {
		this.clotures = clotures;
	}

	public boolean isJour() {
		return jour;
	}

	public void setJour(boolean jour) {
		this.jour = jour;
	}

	public boolean isMois() {
		return mois;
	}

	public void setMois(boolean mois) {
		this.mois = mois;
	}

	public boolean isAnnee() {
		return annee;
	}

	public void setAnnee(boolean annee) {
		this.annee = annee;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}
	
}
