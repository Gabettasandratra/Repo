package mg.fidev.utils.epargne;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import mg.fidev.model.TransactionEpargne;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ficheCaisseEpargne")
public class FicheCaisseEpargne implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "nomCaisse", required = false, nillable = true)
	private String nomCaisse;
	
	@XmlElement(name = "transaction", required = false, nillable = true)
	private List<TransactionEpargne> transaction;

	public FicheCaisseEpargne() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNomCaisse() {
		return nomCaisse;
	}

	public void setNomCaisse(String nomCaisse) {
		this.nomCaisse = nomCaisse;
	}

	public List<TransactionEpargne> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<TransactionEpargne> transaction) {
		this.transaction = transaction;
	}
	
}
