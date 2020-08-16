package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name="clotureCompte")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClotureCompte implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	
	@Column(name="solde")
	private double solde;
	
	@ManyToOne
	@JoinColumn(name="id_account")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name="id_cloture")
	private Cloture cloture;
	
	public ClotureCompte() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Cloture getCloture() {
		return cloture;
	}

	public void setCloture(Cloture cloture) {
		this.cloture = cloture;
	}
	
}
