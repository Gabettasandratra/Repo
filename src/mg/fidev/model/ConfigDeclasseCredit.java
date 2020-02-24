package mg.fidev.model;

import java.io.Serializable;
import java.util.List;

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
@Table(name="config_declasse")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigDeclasseCredit implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId;
	
	private int debut;
	
	private int fin;
	
	private String comptGLind;
	
	private String comptGLgrp;
	
	//bi-directional many-to-one association to ProduitCredit
	@OneToMany(mappedBy="configDeclasse")
	@XmlTransient
	private List<ProduitCredit> produitCredits;

	public ConfigDeclasseCredit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getDebut() {
		return debut;
	}

	public void setDebut(int debut) {
		this.debut = debut;
	}

	public int getFin() {
		return fin;
	}

	public void setFin(int fin) {
		this.fin = fin;
	}

	public String getComptGLind() {
		return comptGLind;
	}

	public void setComptGLind(String comptGLind) {
		this.comptGLind = comptGLind;
	}

	public String getComptGLgrp() {
		return comptGLgrp;
	}

	public void setComptGLgrp(String comptGLgrp) {
		this.comptGLgrp = comptGLgrp;
	}

	public List<ProduitCredit> getProduitCredits() {
		return produitCredits;
	}

	public void setProduitCredits(List<ProduitCredit> produitCredits) {
		this.produitCredits = produitCredits;
	}
	
}
