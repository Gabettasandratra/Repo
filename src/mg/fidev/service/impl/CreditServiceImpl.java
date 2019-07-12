package mg.fidev.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.CommissionCredit;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.GarantieCredit;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.CreditService;

@WebService(name = "creditService", targetNamespace = "http://fidev.mg.creditService", serviceName = "creditService", portName = "creditServicePort", endpointInterface = "mg.fidev.service.CreditService")
public class CreditServiceImpl implements CreditService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();

	/*
	 * Method recupere le dernier index
	 */
	static int getLastIndex(String agence) {
		String sql = "select count(*) from demande_credit where left(num_credit, 2) = '"+agence+"' ";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	static int getLastIndexPdt() {
		String sql = "select count(*) from produit_credit";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	static List<Calpaiementdue> getCalendrierPaiement(DemandeCredit dmd){
		ConfigCreditIndividuel confInd = dmd.getProduitCredit().getConfigCreditIndividuel();
		double intMens = confInd.getTauxInteretAnnuel() / 12;
		double intTot = (dmd.getMontantDmd() * intMens) / 100;
		double intDuJr = intTot / confInd.getTranches();
		double montDuJr = (int) (dmd.getMontantDmd() / confInd.getTranches());
		LocalDate dtDmd = LocalDate.now().plusDays(confInd.getDifferePaiement());
		List<Calpaiementdue> calPaieDue = new ArrayList<Calpaiementdue>();
		for(int i = 0; i < confInd.getTranches(); i++){
			Calpaiementdue cal = new Calpaiementdue();
			dtDmd = dtDmd.plusDays(1);
			if(dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
				dtDmd = dtDmd.plusDays(2);
			}else if(dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
				dtDmd = dtDmd.plusDays(1);
			}
			cal.setDate(dtDmd.toString());
			cal.setMontantPrinc(montDuJr);
			cal.setMontantInt((int) intDuJr);
			cal.setDemandeCredit(dmd);
			calPaieDue.add(cal);
		}
		return calPaieDue;
	}
	
	@Override
	public boolean saveProduit(String nomProdCredit, boolean etat) {
		try {
			ProduitCredit pdtCredit = new ProduitCredit();
			pdtCredit.setNomProdCredit(nomProdCredit);
			pdtCredit.setEtat(etat);
			int lastIndex = getLastIndexPdt();
			String index = String.format("%03d", ++lastIndex);
			pdtCredit.setIdProdCredit("L"+index);
			
			transaction.begin();
			em.persist(pdtCredit);
			transaction.commit();
			em.refresh(pdtCredit);
			System.out.println("produit crédit inséré");
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public void configGnrlCredit(ConfigGeneralCredit configGenCredit, String idProduit) {
		
	}

	@Override
	public void demandeCredit(DemandeCredit dmd, String agence, String idProduit, String codeInd, String codeGrp, GarantieCredit gar, int agentCredit_id) {
		int lastIndex = getLastIndex(agence);
		String index = String.format("%05d", ++lastIndex);
		dmd.setNumCredit(agence + "/" + index);
		Utilisateur agent = em.find(Utilisateur.class, agentCredit_id);
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		Individuel ind = em.find(Individuel.class, codeInd);
		dmd.setAgentCredit_id(agent);
		Groupe grp = em.find(Groupe.class, codeGrp);
		if(ind != null)
			dmd.setIndividuel(ind);
		else if(grp != null)
			dmd.setGroupe(grp);
		dmd.setProduitCredit(pdtCredit);
		dmd.setCalpaiementdues(getCalendrierPaiement(dmd));
		gar.setDemandeCredit(dmd);
		System.out.println("information demande crédit prête");
		try {
			transaction.begin();
			em.persist(gar);
			transaction.commit();
			em.refresh(gar);
			System.out.println("Demande crédit enregistrée");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void updateDemandeCredit(String numCredit, DemandeCredit dmd, int agentCredit_id, String idProduit) {
		DemandeCredit dem = em.find(DemandeCredit.class, numCredit);
		ProduitCredit pdt = em.find(ProduitCredit.class, idProduit);
		Utilisateur agent = em.find(Utilisateur.class, agentCredit_id);
		dem.setAgentCredit_id(agent);
		dem.setButCredit(dmd.getButCredit());
		dem.setMontantDmd(dmd.getMontantDmd());
		dem.setDateDemande(dmd.getDateDemande());
		dem.setProduitCredit(pdt);
		System.out.println("Modification demande crédit");
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Modification réussie");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void paiementCommission(CommissionCredit comm, String numCredit,
			int userId) {
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		comm.setUtilisateur(ut);
		comm.setDemandeCredit(dm);
		System.out.println("Paiement commission");
		try {
			transaction.begin();
			em.persist(comm);
			transaction.commit();
			em.refresh(comm);
			System.out.println("Paiement commission enregistré");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void configCreditInd(ConfigCreditIndividuel configIndCredit,
			String idProduit) {
		ProduitCredit pdtCrdt = em.find(ProduitCredit.class, idProduit);
		pdtCrdt.setConfigCreditIndividuel(configIndCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration crédit individuel du produit "+pdtCrdt.getIdProdCredit()+" inséré");
		} catch (Exception e) {
			System.err.println("Configuration non inséré "+e.getMessage());
		}
	}

	@Override
	public void approbationCredit(String numCredit, String statuApp,
			String dateApp, String descApp, double montantApp) {
		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		dmd.setApprobationStatut(statuApp);
		dmd.setDateApprobation(dateApp);
		dmd.setDescrApprobation(descApp);
		dmd.setMontantApproved(montantApp);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(dmd);
			System.out.println("Approbation de crédit succes");
		} catch (Exception e) {
			System.err.println("Erreur approbation "+e.getMessage());
		}
	}

}
