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
import mg.fidev.model.ConfigFraisCredit;
import mg.fidev.model.ConfigGarantieCredit;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.ConfigGlCredit;
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
	 * Methode pour recuperer le dernier index d'un num crédit
	 */
	static int getLastIndex(String agence) {
		String sql = "select count(*) from demande_credit where left(num_credit, 2) = '"+agence+"' ";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	///	Récupère le dernier index d'un produit crédit
	static int getLastIndexPdt() {
		String sql = "select count(*) from produit_credit";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	///	Génère le calendrier de remboursement lors d'une demande de crédit quelconque
	static List<Calpaiementdue> getCalendrierPaiement(DemandeCredit dmd){
		ConfigCreditIndividuel confInd = dmd.getProduitCredit().getConfigCreditIndividuel();
		String typeTranche = confInd.getTypeTranche();
		double intMens = confInd.getTauxInteretAnnuel() / 12;
		double intTot = (dmd.getMontantDemande() * intMens) / 100;
		double intDuJr = intTot / confInd.getTranches();
		double montDuJr = (int) (dmd.getMontantDemande() / confInd.getTranches());
		LocalDate dtDmd = LocalDate.now().plusDays(confInd.getDifferePaiement());
		List<Calpaiementdue> calPaieDue = new ArrayList<Calpaiementdue>();
		switch (typeTranche) {
			case "Quotidiennement":
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
				break;
			case "Hebdomadairement":
				for(int i = 0; i < confInd.getTranches(); i++){
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusWeeks(1);
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
				break;
			case "Mensuellement":
				for(int i = 0; i < confInd.getTranches(); i++){
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusMonths(1);
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
				break;
			default:
				break;
		}
		return calPaieDue;
	}
	
	///	Insertion d'un produit crédit
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

	///	Configuration GL produit crédit
	@Override
	public void configGnrlCredit(ConfigGeneralCredit configGenCredit, String idProduit) {
		ProduitCredit pdt = em.find(ProduitCredit.class, idProduit);
		pdt.setConfigGeneralCredit(configGenCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration générale du produit "+idProduit);
		} catch (Exception e) {
			System.err.println("Configuration générale non insérée "+e.getMessage());
		}
	}

	///	Demande de crédit
	@Override
	public void demandeCredit(DemandeCredit dmd, String agence, String idProduit, String codeInd, String codeGrp, GarantieCredit gar, int userId) {
		int lastIndex = getLastIndex(agence);
		String index = String.format("%05d", ++lastIndex);
		dmd.setNumCredit(agence + "/" + index);
		Utilisateur user = em.find(Utilisateur.class, userId);
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		Individuel ind = em.find(Individuel.class, codeInd);
		dmd.setDateApprobation(null);
		dmd.setApprobationStatut(null);
		dmd.setUtilisateur(user);
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

	///	Modification demande de crédit
	@Override
	public void updateDemandeCredit(String numCredit, DemandeCredit dmd, int userId, String idProduit) {
		DemandeCredit dem = em.find(DemandeCredit.class, numCredit);
		ProduitCredit pdt = em.find(ProduitCredit.class, idProduit);
		Utilisateur user = em.find(Utilisateur.class, userId);
		dem.setUtilisateur(user);
		dem.setButCredit(dmd.getButCredit());
		dem.setMontantDemande(dmd.getMontantDemande());
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

	///	Paiement commission crédit
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

	///	Configuration crédit individuel
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

	///	Approbation demande crédit
	@Override
	public void approbationCredit(String numCredit, String statuApp,
			String dateApp, String descApp, double montantApp) {
		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		dmd.setApprobationStatut(statuApp);
		dmd.setDateApprobation(dateApp);
		dmd.setDescrApprobation(descApp);
		dmd.setMontantApproved(montantApp);
		if(dmd.getMontantApproved() != 0){
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
		else
			System.err.println("Montant approuvé null");
	}

	///	Configuration frais crédit
	@Override
	public void configFraisCredit(ConfigFraisCredit configFraisCredit,
			String idProduit) {
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		pdtCred.setConfigFraisCredit(configFraisCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration frais crédit du produit "+idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré "+e.getMessage());
		}
	}

	///	Configuration garantie crédit
	@Override
	public void configGarantiCredit(ConfigGarantieCredit configGarCredit,
			String idProduit) {
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		pdtCred.setConfigGarantieCredit(configGarCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration frais crédit du produit "+idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré "+e.getMessage());
		}
	}

	///	Configuration GL crédit
	@Override
	public void configGLCredit(ConfigGlCredit configGLCredit, String idProduit) {
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		pdtCred.setConfigGlCredit(configGLCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration frais crédit du produit "+idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré "+e.getMessage());
		}
	}

}
