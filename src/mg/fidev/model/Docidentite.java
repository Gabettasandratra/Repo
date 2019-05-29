package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the docidentite database table.
 * 
 */
@Entity
@NamedQuery(name="Docidentite.findAll", query="SELECT d FROM Docidentite d")
public class Docidentite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String numero;

	@Temporal(TemporalType.DATE)
	private Date dateEmis;

	@Temporal(TemporalType.DATE)
	private Date dateExpire;

	private String delivrePar;

	private int priorite;

	private String typedoc;

	//bi-directional many-to-one association to Individuel
	@ManyToOne
	@JoinColumn(name="rowIdClient")
	private Individuel individuel;

	public Docidentite() {
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDateEmis() {
		return this.dateEmis;
	}

	public void setDateEmis(Date dateEmis) {
		this.dateEmis = dateEmis;
	}

	public Date getDateExpire() {
		return this.dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getDelivrePar() {
		return this.delivrePar;
	}

	public void setDelivrePar(String delivrePar) {
		this.delivrePar = delivrePar;
	}

	public int getPriorite() {
		return this.priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public String getTypedoc() {
		return this.typedoc;
	}

	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}

	public Individuel getIndividuel() {
		return this.individuel;
	}

	public void setIndividuel(Individuel individuel) {
		this.individuel = individuel;
	}

}