package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@Table(name="membre_view")
@XmlRootElement(name="membreview")
@XmlAccessorType(XmlAccessType.FIELD)
public class MembreView implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String codeInd;
	
	private String codeGroupe;
	
	private String fonction;

	public MembreView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodeInd() {
		return codeInd;
	}

	public void setCodeInd(String codeInd) {
		this.codeInd = codeInd;
	}

	public String getCodeCroupe() {
		return codeGroupe;
	}

	public void setCodeCroupe(String codeCroupe) {
		this.codeGroupe = codeCroupe;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

}
