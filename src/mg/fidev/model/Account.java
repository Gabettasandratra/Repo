package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the accounts database table.
 * 
 */
@Entity
@Table(name="accounts")
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tkey;

	private int hlevel;

	private boolean isActive;

	private boolean isHeader;

	private String label;

	@Column(name="num_cpt")
	private String numCpt;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="parent")
	@XmlTransient
	private Account account;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="account")
	@XmlTransient
	private List<Account> accounts;

	//bi-directional many-to-one association to CompteCaisse
	@OneToMany(mappedBy="account")
	@XmlTransient
	private List<CompteCaisse> compteCaisses;

	public Account() {
	}

	public int getTkey() {
		return this.tkey;
	}

	public void setTkey(int tkey) {
		this.tkey = tkey;
	}

	public int getHlevel() {
		return this.hlevel;
	}

	public void setHlevel(int hlevel) {
		this.hlevel = hlevel;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsHeader() {
		return this.isHeader;
	}

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNumCpt() {
		return this.numCpt;
	}

	public void setNumCpt(String numCpt) {
		this.numCpt = numCpt;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setAccount(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setAccount(null);

		return account;
	}

	public List<CompteCaisse> getCompteCaisses() {
		return this.compteCaisses;
	}

	public void setCompteCaisses(List<CompteCaisse> compteCaisses) {
		this.compteCaisses = compteCaisses;
	}

	public CompteCaisse addCompteCaiss(CompteCaisse compteCaiss) {
		getCompteCaisses().add(compteCaiss);
		compteCaiss.setAccount(this);

		return compteCaiss;
	}

	public CompteCaisse removeCompteCaiss(CompteCaisse compteCaiss) {
		getCompteCaisses().remove(compteCaiss);
		compteCaiss.setAccount(null);

		return compteCaiss;
	}

}