package mg.fidev.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the plan_comptable database table.
 * 
 */
@Entity
@Table(name="plan_comptable")
@NamedQuery(name="PlanComptable.findAll", query="SELECT p FROM PlanComptable p")
public class PlanComptable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="num_plan_comptable")
	private String numPlanComptable;

	private String libelle;

	//bi-directional many-to-one association to CompteCaisse
	@OneToMany(mappedBy="planComptable")
	private List<CompteCaisse> compteCaisses;

	public PlanComptable() {
	}

	public String getNumPlanComptable() {
		return this.numPlanComptable;
	}

	public void setNumPlanComptable(String numPlanComptable) {
		this.numPlanComptable = numPlanComptable;
	}

	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<CompteCaisse> getCompteCaisses() {
		return this.compteCaisses;
	}

	public void setCompteCaisses(List<CompteCaisse> compteCaisses) {
		this.compteCaisses = compteCaisses;
	}

	public CompteCaisse addCompteCaiss(CompteCaisse compteCaiss) {
		getCompteCaisses().add(compteCaiss);
		compteCaiss.setPlanComptable(this);

		return compteCaiss;
	}

	public CompteCaisse removeCompteCaiss(CompteCaisse compteCaiss) {
		getCompteCaisses().remove(compteCaiss);
		compteCaiss.setPlanComptable(null);

		return compteCaiss;
	}

}