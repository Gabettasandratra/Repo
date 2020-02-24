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
	
	@Column(name="devise")
	private String devise;
	
	@Column(name="ferme")
	private boolean ferme;
	
	@Column(name="solde_init")
	private double soldeInit;
	
	@Column(name="solde_progressif")
	private double soldeProgressif;

	//private double @javax.persistence.Transient

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="parent")
	private Account account;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="account")
	@XmlTransient
	private List<Account> accounts;

	//bi-directional many-to-one association to CompteCaisse
	@OneToMany(mappedBy="account",cascade= CascadeType.ALL)
	@XmlTransient
	private List<Caisse> caisses;
	
	//bi-directional many-to-one association to GranLivre
	@OneToMany(mappedBy="account",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@XmlTransient
	private List<Grandlivre> granLivres;
	
	//relation OneToMany	
	@OneToMany(mappedBy="account",cascade=CascadeType.ALL)
	@XmlTransient
	private List<Budget> budgets;

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

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public boolean isFerme() {
		return ferme;
	}

	public void setFerme(boolean ferme) {
		this.ferme = ferme;
	}

	public double getSoldeInit() {
		return soldeInit;
	}

	public void setSoldeInit(double soldeInit) {
		this.soldeInit = soldeInit;
	}

	public double getSoldeProgressif() {
		return soldeProgressif;
	}

	public void setSoldeProgressif(double soldeProgressif) {
		this.soldeProgressif = soldeProgressif;
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

	public List<Grandlivre> getGranLivres() {
		return granLivres;
	}

	public void setGranLivres(List<Grandlivre> granLivres) {
		this.granLivres = granLivres;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public List<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<Budget> budgets) {
		this.budgets = budgets;
	}

	public List<Caisse> getCaisses() {
		return caisses;
	}

	public void setCaisses(List<Caisse> caisses) {
		this.caisses = caisses;
	}
	
}