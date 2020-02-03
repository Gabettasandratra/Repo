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
@Table(name="config_GL_DAT")
@XmlRootElement(name="cofigGlDat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigGLDAT implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Identifiant
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//Compte de dépôt à terme
	private String cmptDAT;
	
	//Compte intérêt dû sur DAT
	private String cmptIntDuDAT;
	
	//Compte pénalité DAT
	private String cmptPenalDAT;
	
	//Compte intérêt payé sur DAT
	private String cmptIntPayeDAT;
	
	//Compte intérêt échus
	private String cmptIntEchus;
	
	//Compte coût d'intérêt accumulé
	private String cmptAccumule;
	
	//compte difference de cash	
	private String cmptDiffCash;
	
	//Chèque sur compte d'attente;
	private String cmptCheque;
	
	//compte taxe retenue
	private String cmptTaxeRetenu;
	
	@OneToMany(mappedBy="configGlDat")
	@XmlTransient
	private List<ProduitEpargne> produitEpargnes;

	public ConfigGLDAT() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCmptDAT() {
		return cmptDAT;
	}

	public void setCmptDAT(String cmptDAT) {
		this.cmptDAT = cmptDAT;
	}

	public String getCmptIntDuDAT() {
		return cmptIntDuDAT;
	}

	public void setCmptIntDuDAT(String cmptIntDuDAT) {
		this.cmptIntDuDAT = cmptIntDuDAT;
	}

	public String getCmptPenalDAT() {
		return cmptPenalDAT;
	}

	public void setCmptPenalDAT(String cmptPenalDAT) {
		this.cmptPenalDAT = cmptPenalDAT;
	}

	public String getCmptIntPayeDAT() {
		return cmptIntPayeDAT;
	}

	public void setCmptIntPayeDAT(String cmptIntPayeDAT) {
		this.cmptIntPayeDAT = cmptIntPayeDAT;
	}

	public String getCmptIntEchus() {
		return cmptIntEchus;
	}

	public void setCmptIntEchus(String cmptIntEchus) {
		this.cmptIntEchus = cmptIntEchus;
	}

	public String getCmptAccumule() {
		return cmptAccumule;
	}

	public void setCmptAccumule(String cmptAccumule) {
		this.cmptAccumule = cmptAccumule;
	}

	public String getCmptCheque() {
		return cmptCheque;
	}

	public void setCmptCheque(String cmptCheque) {
		this.cmptCheque = cmptCheque;
	}

	public String getCmptTaxeRetenu() {
		return cmptTaxeRetenu;
	}

	public void setCmptTaxeRetenu(String cmptTaxeRetenu) {
		this.cmptTaxeRetenu = cmptTaxeRetenu;
	}

	public List<ProduitEpargne> getProduitEpargnes() {
		return produitEpargnes;
	}

	public void setProduitEpargnes(List<ProduitEpargne> produitEpargnes) {
		this.produitEpargnes = produitEpargnes;
	}

	public String getCmptDiffCash() {
		return cmptDiffCash;
	}

	public void setCmptDiffCash(String cmptDiffCash) {
		this.cmptDiffCash = cmptDiffCash;
	}
	
}
