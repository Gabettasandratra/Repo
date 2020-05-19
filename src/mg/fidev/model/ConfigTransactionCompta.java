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
@Table(name="config_transaction_compta")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigTransactionCompta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rowId; 
	
	@ManyToOne
	@JoinColumn(name="compteCaisse")
	private Account caisse;
	
	@ManyToOne
	@JoinColumn(name="compteBanque")
	private Account banque;

	public ConfigTransactionCompta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public Account getCaisse() {
		return caisse;
	}

	public void setCaisse(Account caisse) {
		this.caisse = caisse;
	}

	public Account getBanque() {
		return banque;
	}

	public void setBanque(Account banque) {
		this.banque = banque;
	}
}
