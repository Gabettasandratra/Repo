package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="membre_groupe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MembreGroupe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idMembre;
		
	@ManyToOne
	@JoinColumn(name="id_fonction")
	private FonctionMembreGroupe fonctionMembre;
	
	@ManyToOne
	@JoinColumn(name="code_individuel")
	private Individuel individuel;
	

	@ManyToOne
	@JoinColumn(name="code_groupe")
	private Groupe groupe;


	public MembreGroupe() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getIdMembre() {
		return idMembre;
	}



	public void setIdMembre(int idMembre) {
		this.idMembre = idMembre;
	}



	public FonctionMembreGroupe getFonctionMembre() {
		return fonctionMembre;
	}


	public void setFonctionMembre(FonctionMembreGroupe fonctionMembre) {
		this.fonctionMembre = fonctionMembre;
	}


	public Individuel getIndividuel() {
		return individuel;
	}


	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}


	public Groupe getGroupe() {
		return groupe;
	}


	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
	
	
}
