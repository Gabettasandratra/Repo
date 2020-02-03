package mg.fidev.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Account;
import mg.fidev.model.CalView;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.CommissionCredit;
import mg.fidev.model.CompteCaisse;
import mg.fidev.model.ConfigCredit;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigFraisCredit;
import mg.fidev.model.ConfigFraisCreditGroupe;
import mg.fidev.model.ConfigGarantieCredit;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.ConfigGlCredit;
import mg.fidev.model.ConfigPenaliteCredit;
import mg.fidev.model.Decaissement;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Garant;
import mg.fidev.model.GarantieCredit;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.Personnel;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.Remboursement;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.CreditService;
import mg.fidev.utils.CodeIncrement;
import mg.fidev.utils.FicheCred;

@WebService(name = "creditProduitService", targetNamespace = "http://fidev.mg.creditProduitService", serviceName = "creditProduitService", portName = "creditServicePort", endpointInterface = "mg.fidev.service.CreditService")
public class CreditServiceImpl implements CreditService {
	
	
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	
	/************************************************************************************************************************************************/

						/*********************** CRUD SUR PRODUITS CREDIT ****************************/
	
	/***********************************************************************************************************************************************/
	
	
	
	/**
	 * Récupère le dernier index d'un produit crédit
	 * **/
	static int getLastIndexPdt() {
		String sql = "select count(*) from produit_credit";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	/**
	 * Insertion d'un produit crédit
	 * **/
	@Override
	public ProduitCredit saveProduit_Credit(String nomProdCredit, boolean etat) {
		try {
			ProduitCredit pdtCredit = new ProduitCredit();
			pdtCredit.setNomProdCredit(nomProdCredit);
			pdtCredit.setEtat(etat);
			int lastIndex = getLastIndexPdt();
			String index = String.format("%03d", ++lastIndex);
			pdtCredit.setIdProdCredit("L" + index);

			transaction.begin();
			em.persist(pdtCredit);
			transaction.commit();
			em.refresh(pdtCredit);
			System.out.println("produit crédit inséré");
			return pdtCredit;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/***
	 * 
	 * HISTORIQUE DEMANDE CREDIT
	 * ***/
	public List<DemandeCredit> findAllDemand() {
		TypedQuery<DemandeCredit> q = em.createQuery("SELECT c FROM DemandeCredit c", DemandeCredit.class);
		List<DemandeCredit> l = q.getResultList();
		return l;
	}

	
	/**
	 * List des Produits Credit
	 * **/
	public List<ProduitCredit> findAllCredit() {
		TypedQuery<ProduitCredit> query = em.createQuery("SELECT c FROM ProduitCredit c", ProduitCredit.class);
		List<ProduitCredit> list = query.getResultList();
		return list;
	}
	
	/**
	 * Chercher credit par indentifiant
	 * **/
	@Override
	public ProduitCredit findOne(String id) {
		 ProduitCredit produit = em.find(ProduitCredit.class, id);
		if (produit != null) {
			return produit;
		} else {
			return null;
		}
	}

	/**
	 * Chercher Credit par Mot clé
	 **/
	@Override
	public List<ProduitCredit> findCreditByMc(String mc) {
			TypedQuery<ProduitCredit> query = em.createQuery(
					" SELECT c FROM ProduitCredit c WHERE c.nomProdCredit like :mc", ProduitCredit.class);
			query.setParameter("mc", "%"+mc+"%");
			List<ProduitCredit> result = query.getResultList();

		return result; 
	}
	
	/***
	 * CHERCHER DEMANDE
	 * ***/
	
	@Override
	public List<DemandeCredit> findDemandeByMc(String mc,String m2) {
		
		List<DemandeCredit> result = new ArrayList<DemandeCredit>();
		if(m2.equals("")){
			TypedQuery<DemandeCredit> query = em.createQuery("SELECT d FROM DemandeCredit d WHERE d.approbationStatut like :mc", DemandeCredit.class);
			query.setParameter("mc", "%"+mc+"%");
			result = query.getResultList();		
		}else{
			TypedQuery<DemandeCredit> query = em.createQuery("SELECT d FROM DemandeCredit d WHERE d.approbationStatut like :mc"
					+ " OR d.approbationStatut like :motCle", DemandeCredit.class);
			query.setParameter("mc", "%"+mc+"%");
			query.setParameter("motCle", "%"+m2+"%");
			result = query.getResultList();	
			
		}
		return result;
	}

	/**
	 * Modifier Credit
	 * **/
	@Override
	public ProduitCredit updateProduitCredit(String numProd, ProduitCredit p) {
		ProduitCredit produit = findOne(numProd);
		try {
			transaction.begin();
			produit.setNomProdCredit(p.getNomProdCredit());
			produit.setEtat(p.getEtat());
			em.merge(produit);
			transaction.commit();
			em.refresh(produit);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produit;
	}

	/**
	 * Supprimer un credit
	 **/
	@Override
	public String deleteProduitCredit(String id) {
		transaction.begin();
		ProduitCredit p = findOne(id);
		
		String res = "";

		if(p != null){
			em.remove(p);
			res = "Suppression avec succes!!!";
		}
		
		else {
				res = "Aucun produit de ce numero";			
		}			
		transaction.commit();
		//em.refresh(p); 
		//em.close();	
		System.out.println(res); 
		return res;
	}
	
	
	/********************************************************DEMANDE CREDIT*************************************************************************/
	
	/**
	 * Methode pour recuperer le dernier index d'un num crédit
	 **/

	static int getLastIndex(String codeInd) {
		String code = codeInd.substring(0, 2);
		String sql = "select count(*) from demande_credit where left(num_credit, 2) = '"+code+"'";// String num_crd
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	//Demande crédit
	@Override
	public List<Calpaiementdue> insertDemande(String idProduit, String codeInd,	String codeGroupe, String date_dem, double montant, String agent,String but, int user_id) {
		
		DemandeCredit dmd = new DemandeCredit();
		
		List<Calpaiementdue> resultat = new ArrayList<Calpaiementdue>();

		Utilisateur user = em.find(Utilisateur.class, user_id);
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		
		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGroupe);
		
		/*****************************************************************************************************************************/
						/********************** CHARGEMENT DES CONFIGURATIONS *******************************************/
		/*****************************************************************************************************************************/	
		
		/***
		 * CHARGE CONFIGURATION POUR TOUT CREDIT
		 * ***/
		
		
		//ConfigCredit confCred = pdtCredit.getConfigCredit();
		
		
		/***
		 * CHARGE LES CONFIGURATIONS DE FRAIS
		 * ***/
		
		/******************VERIFICATION SI CLIENT EST INDIVIDUEL OU GROUPE**************************************/
		

		/* Insertion d'information de demande credit */

		if(ind != null){
			//VERIFICATION AUTORISATION
			boolean autorisation = false;
			
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = ind.getCodeInd().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setIndividuel(ind);
			
			System.out.println("Demande credit pour client individuel");

			/***
			 * CHARGE LES CONFIGURATIONS POUR LA DEMANDE CREDIT INDIVIDUEL
			 * ***/
			ConfigCreditIndividuel conInd = pdtCredit.getConfigCreditIndividuel();
			
			double montMaxAutoriser = conInd.getMontantMaxCredit();
			double montMinAutoriser = conInd.getMontantMinCredit();
			double montantUnique = conInd.getMontantCredit();
			
			if(montMinAutoriser != 0 || montMaxAutoriser != 0){
				
				if(montant < montMinAutoriser || montant > montMaxAutoriser){
					System.out.println("Le montant doit compris entre "+montMinAutoriser+" et "+montMaxAutoriser);
				}else{				
					autorisation = true;
				}
			}else if(montantUnique != 0){
				if(montant != montantUnique){
					System.out.println("Le montant saisie different de  "+montantUnique);
				}else{
					autorisation = true;
				}
			}

			
			if(autorisation == true){
				dmd.setUtilisateur(user);
				dmd.setProduitCredit(pdtCredit);
				dmd.setNbCredit(CodeIncrement.nombreCreditInd(em, ind.getCodeInd()));
				dmd.setDateDemande(date_dem);
				dmd.setMontantDemande(montant);
//				dmd.setAgentName(agent);
//				dmd.setButCredit(but);
				dmd.setApprobationStatut("Approbation en attente");
				dmd.setCalpaiementdues(CodeIncrement.getCalendrierPaiement(dmd));
				
				System.out.println("information demande crédit prête");
				try {
					transaction.begin();
					em.persist(dmd);
					transaction.commit();    
					em.refresh(dmd);
					TypedQuery<Calpaiementdue> requet = em.createQuery("SELECT cals FROM Calpaiementdue cals"
							+ " JOIN cals.demandeCredit numCredit WHERE numCredit.numCredit =:nums ORDER BY cals.date asc ",Calpaiementdue.class);
					requet.setParameter("nums", dmd.getNumCredit());
					resultat = requet.getResultList();
					//resultat = dmd.getCalpaiementdues();
					System.out.println("Demande crédit enregistrée");
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}else{
				System.out.println("Montant incorrect,,, Veuillez vérifier la configuration de ce credit SVP!!!");
			}	
		
			
		}
		else if(grp != null){
			
			int lastIndex = getLastIndex(codeGroupe);
			String index = String.format("%05d", ++lastIndex);
			
			String gr = grp.getCodeGrp().substring(0, 2);
			dmd.setNumCredit(gr + "/" + index);
			dmd.setGroupe(grp);
			
		
			/***
			 * CHARGE LES CONFIGURATIONS POUR LA DEMANDE CREDIT D'UN GROUPE 
			 * ***/
			
			ConfigCreditGroup configGroupe = pdtCredit.getConfigCreditGroupe();
			
			double montMin = configGroupe.getMontanMinParMembre();
			double montMax = configGroupe.getMontantMaxParMembre();
			
			
			if (montant < montMin || montant > montMax) {
				System.out.println("Le montant doit compris entre "+montMin+" et "+montMax);
			}else{
				
				dmd.setUtilisateur(user);
				dmd.setProduitCredit(pdtCredit);
				dmd.setNbCredit(CodeIncrement.nombreCreditGrp(em, grp.getCodeGrp()));
				dmd.setDateDemande(date_dem);
				dmd.setMontantDemande(montant);
//				dmd.setAgentName(agent);
//				dmd.setButCredit(but);
				dmd.setApprobationStatut("Approbation en attente");
				dmd.setCalpaiementdues(CodeIncrement.getCalendrierPaiement(dmd));
				
				System.out.println("information demande crédit prête");
				try {
					transaction.begin();
					em.persist(dmd);
					transaction.commit();
					em.refresh(dmd);
					TypedQuery<Calpaiementdue> requet = em.createQuery("SELECT cals FROM Calpaiementdue cals"
							+ " JOIN cals.demandeCredit numCredit WHERE numCredit.numCredit =:nums ORDER BY cals.date asc ",Calpaiementdue.class);
					requet.setParameter("nums", dmd.getNumCredit());
					resultat = requet.getResultList();
					//resultat = dmd.getCalpaiementdues();
					System.out.println("Demande crédit enregistrée");
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			}
			
		}
		
		return resultat;
	}
	
	
	/***
	 * SAVE DEMANDE CREDIT
	 * ***/
	@Override
	public boolean demandePret(String idProduit, String codeInd,
			String codeGroupe, String date_dem, double montant, String agent,
			String butEco, String butSps, GarantieCredit g1, GarantieCredit g2,
			GarantieCredit g3,String codeGar1,String codeGar2,String codeGar3, int user_id, double totGarantie) {
		
		//Instancie les classe necessaire
		//Produit et utilisateur
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		Utilisateur user = em.find(Utilisateur.class, user_id);		
		//Agent de credit
		Personnel p = em.find(Personnel.class, agent);		
		//Garant		
		Garant ga1 = em.find(Garant.class, codeGar1);
		Garant ga2 = em.find(Garant.class, codeGar2);
		Garant ga3 = em.find(Garant.class, codeGar3);		
		//Client
		Individuel ind = em.find(Individuel.class, codeInd);
		//Groupe grp = em.find(Groupe.class, codeGroupe);		
		//Demande crédit
		DemandeCredit dmd = new DemandeCredit();
		
		//Récuperation calendrier
		List<Calpaiementdue> calend = new ArrayList<Calpaiementdue>();
		List<CalView> calV = getAllCalView();
		for (CalView calView : calV) {
			Calpaiementdue cald = new Calpaiementdue();
			cald.setDate(calView.getDate());
			cald.setMontantComm(calView.getMontantComm());
			cald.setMontantInt(calView.getMontantInt());
			cald.setMontantPenal(calView.getMontantPenal());
			cald.setMontantPrinc(calView.getMontantPrinc());
			cald.setDemandeCredit(dmd); 
			calend.add(cald);
		}
		
		//Grand Livre		
		Grandlivre debit = new Grandlivre();
		Grandlivre credit = new Grandlivre();
		
		if(ind != null){
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);		
			String ag = ind.getCodeInd().substring(0, 2);
			
			//information de demande credit
			dmd.setNumCredit(ag + "/" + index);
			dmd.setIndividuel(ind);
			dmd.setUtilisateur(user);
			dmd.setProduitCredit(pdtCredit);
			dmd.setNbCredit(CodeIncrement.nombreCreditInd(em, ind.getCodeInd()));
			dmd.setDateDemande(date_dem);
			dmd.setMontantDemande(montant);
			dmd.setAgent(p);
			dmd.setButEconomique(butEco);
			dmd.setButSocial(butSps);
			dmd.setApprobationStatut("Approbation en attente");
			dmd.setCalpaiementdues(calend);
			deleteCalendrier();
			
			System.out.println("information demande crédit prête");
			try {
				
				//garantie de credit
				//si garantie1 existe
								
				if(ga1 != null && codeGar1 != null){
					g1.setDemandeCredit(dmd);
					g1.setGarant(ga1);					
					transaction.begin();
					em.persist(g1);
					transaction.commit();
					em.refresh(g1);
				}
				//si garantie2 existe
				if(ga2 != null && codeGar2 != null){
					g2.setDemandeCredit(dmd);
					g2.setGarant(ga2);					
					transaction.begin();
					em.persist(g2);
					transaction.commit();
					em.refresh(g2);
				}
				//si garantie3 existe
				if(ga3 != null && codeGar3 != null){
					g1.setDemandeCredit(dmd);
					g1.setGarant(ga3);					
					transaction.begin();
					em.persist(g3);
					transaction.commit();
					em.refresh(g3);
				}
				String indexTcode = CodeIncrement.genTcode(em);

				//Compte à débit
				
				Account cmpDebit = CodeIncrement.getAcount(em, "935");
				
				debit.setTcode(indexTcode);
				debit.setDate(date_dem);
				debit.setDescr("Total garantie du crédit "+dmd.getNumCredit());
				debit.setPiece("reçus du credit "+dmd.getNumCredit());
				debit.setCompte("935");
				debit.setDebit(totGarantie);
				
				debit.setSolde(cmpDebit.getSoldeProgressif() + totGarantie);
				cmpDebit.setSoldeProgressif(cmpDebit.getSoldeProgressif() + totGarantie);
				
				debit.setUtilisateur(user);
				debit.setCodeInd(dmd.getIndividuel());
				debit.setDemandeCredit(dmd);
				debit.setAccount(cmpDebit);
				
				//Compte à crédité
				
				Account cmptCredit = CodeIncrement.getAcount(em, "941");
				
				credit.setTcode(indexTcode);
				credit.setDate(date_dem);
				credit.setDescr("Total garantie du crédit "+dmd.getNumCredit());
				credit.setPiece("reçus du credit "+dmd.getNumCredit());
				credit.setCompte("941");
				credit.setCredit(totGarantie);
				
				credit.setSolde(cmptCredit.getSoldeProgressif() + totGarantie);
				cmptCredit.setSoldeProgressif(cmptCredit.getSoldeProgressif() + totGarantie);

				credit.setUtilisateur(user);
				credit.setCodeInd(dmd.getIndividuel());
				credit.setDemandeCredit(dmd);
				credit.setAccount(cmptCredit);
				
				
				transaction.begin();
				em.flush();
				em.flush();
				em.persist(dmd);
				em.persist(debit);
				em.persist(credit);
				transaction.commit();
				em.refresh(dmd);
				em.refresh(debit);
				em.refresh(credit);
				
				System.out.println("Demande crédit enregistrée");
				return true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}
			
		}	
		return false;
		
	}
	
	//SAISIE DEMANDE CREDIT
	
	@Override
	public List<CalView> demCredit(String codeInd,
			String date_dem, double montant, double tauxInt, int nbTranche,
			String typeTranche, int diffPaie, String modCalcul) {

		List<CalView> resultat = new ArrayList<CalView>();
		
		DemandeCredit dmd = new DemandeCredit();
		Individuel ind = em.find(Individuel.class, codeInd);
		
		int lastIndex = getLastIndex(codeInd);
		String index = String.format("%05d", ++lastIndex);
		
		String ag = ind.getCodeInd().substring(0, 2);
		dmd.setNumCredit(ag + "/" + index);
		dmd.setIndividuel(ind);		
		System.out.println("Demande credit pour client individuel");
			
			//Interet dans 1 mois en pourcentage
			double intMens = tauxInt / 12;
			
			//Montant d'interet par mois
			double intTot = (montant * intMens) / 100;

			//Interet principale
			double montDuJr =  montant / nbTranche;//(int)
			
			System.out.println(montDuJr);
			
			LocalDate dtDmd = LocalDate.now().plusDays(diffPaie);
			
			switch (typeTranche) {
			case "Quotidiennement":
				//Montant d'Interet par Jours 
				double intDuJr = (intTot / nbTranche);
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusDays(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) intDuJr);
					cal.setNumCred(dmd.getNumCredit());
					resultat.add(cal);
					try {
						transaction.begin();
						em.persist(cal);
						transaction.commit();
						System.out.println("calendrier enregistrer");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "Hebdomadairement":
				double interetSemaine = intTot / nbTranche;
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interetSemaine);
					cal.setNumCred(dmd.getNumCredit());
					resultat.add(cal);
					try {
						transaction.begin();
						em.persist(cal);
						transaction.commit();
						System.out.println("calendrier enregistrer");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTot / nbTranche;
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusWeeks(2);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interet_quinz);
					cal.setNumCred(dmd.getNumCredit());
					resultat.add(cal);
					try {
						transaction.begin();
						em.persist(cal);
						transaction.commit();
						System.out.println("calendrier enregistrer");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusMonths(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) intTot);
					cal.setNumCred(dmd.getNumCredit());
					resultat.add(cal);
					try {
						transaction.begin();
						em.persist(cal);
						transaction.commit();
						System.out.println("calendrier enregistrer");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		
		return resultat;
	}
	
	//Modification calendrier
	@Override
	public boolean modifCalendrier(int id, CalView cal) {
		CalView calBac = em.find(CalView.class, id);
		calBac.setDate(cal.getDate());
		calBac.setMontantPrinc(cal.getMontantPrinc());
		calBac.setMontantInt(cal.getMontantInt());
		calBac.setMontantComm(cal.getMontantComm());
		try {
			transaction.begin();
			em.merge(calBac);
			transaction.commit();
			em.refresh(calBac); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//recupérer calendrier view
	@Override
	public List<CalView> getAllCalView() {
		String sql = "select c from CalView c";
		return em.createQuery(sql,CalView.class).getResultList();
	}

	
	//Supprimer données 
	@Override
	public boolean deleteCalendrier() {
		Query query = em.createNativeQuery("TRUNCATE TABLE calendrierview");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/***
	 * COMMISSION CREDIT
	 * ***/
	@Override
	public boolean insertCommission(boolean cash,String date, String piece,double commission,double papeterie,
			String numCredit,int userId, String nomCptCaisse) {

		//Instance des classes necessaires
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		CommissionCredit paie = new CommissionCredit();	
		
		//CHARGE LES CONFIGURATIONS FRAIS DE CREDIT
		ConfigFraisCredit confFrais = dm.getProduitCredit().getConfigFraisCredit();
		ConfigFraisCreditGroupe confGroupe = dm.getProduitCredit().getConfFraisGroupe();
		//valeur de retour
		boolean result = false;	
		//Tester si la commission est demandé au décaissement ou à la demande 
		if(confFrais.getFraisDemandeOuDecais() == true || confGroupe.getFraisDemandeOuDecais() == true){
				
				//CHARGE LES CONFIGURATIONS GL DU CREDIT
				ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
				
				//pour incrémenter le tcode dans la table grandlivre
				String indexTcode = CodeIncrement.genTcode(em);
				//Instance du compte caisse
				CompteCaisse cptCaisse = em.find(CompteCaisse.class, nomCptCaisse);
				
				//Commission total Crédit
				Grandlivre debitCom = new Grandlivre();
				//Commission
				Grandlivre creditCom = new Grandlivre();
				//Papeterie
				Grandlivre creditPap = new Grandlivre();
				
				
				//Total debité
				double debitTot = 0.0;
				double commissions = 0.0;
				double papeteries = 0.0;
				
				//si c'est un crédit individuel
				if(dm.getIndividuel() != null && confFrais.isIndOuGroupe() == true){
					debitTot = confFrais.getCommission() + confFrais.getPapeterie();
					commissions = confFrais.getCommission();
					papeteries = confFrais.getPapeterie();
				}//si crédit groupe
				else if(dm.getGroupe() != null){
					debitTot = confGroupe.getCommission() + confGroupe.getPapeterie();
					commissions = confGroupe.getCommission();
					papeteries = confGroupe.getPapeterie();
				}
				
				System.out.println("Paiement commission");
				
				//Vérification de statut de crédit
				if (dm.getApprobationStatut().equalsIgnoreCase("payé apres approbation")
						|| dm.getApprobationStatut().equalsIgnoreCase("payé avant approbation")
						|| dm.getApprobationStatut().equalsIgnoreCase("Demande decaissé")
						|| dm.getApprobationStatut().equalsIgnoreCase("Credit remboursé")) {
					
					System.out.println("le Paiement de commission de cet Crédit est déjà fait");
					
				} else {

					try {
						//Si payement commission est avant approbation
						if (dm.getApprobationStatut().equalsIgnoreCase("Approbation en attente")) {
							paie.setStatut_comm("Paiement de commission avant approbation");
							dm.setApprobationStatut("payé avant approbation");				
							
						} //Si payement commission est après approbation
						else if (dm.getApprobationStatut().equalsIgnoreCase("Approuver")) {
							paie.setStatut_comm("Paiement de commission apres approbation");
							dm.setApprobationStatut("payé apres approbation");
						}
						
						//Insertion au GrandLivre
						//compte débité
						String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
						
						System.out.println("Compte débit "+cptC);
						Account account;
						account = CodeIncrement.getAcount(em, cptC);						
						double sd = account.getSoldeProgressif()+debitTot;
				
						debitCom.setTcode(indexTcode);
						debitCom.setDate(date);
						debitCom.setDescr("Total commision crédit " +numCredit);
						debitCom.setPiece(piece);
						debitCom.setUserId(ut.getNomUtilisateur());
						debitCom.setCompte(cptC);					
						debitCom.setDebit(debitTot);
						debitCom.setCodeInd(dm.getIndividuel());
	
						debitCom.setAccount(account);
						debitCom.setUtilisateur(ut);
						debitCom.setDemandeCredit(dm);
						debitCom.setSolde(sd);
						account.setSoldeProgressif(sd);					
						
						//compte crédité						
						Account crdCom = CodeIncrement.getAcount(em, confGl.getCptCommCredit());						
						System.out.println("Compte Crédit com "+crdCom.getNumCpt());
						//commissions
						
						double scCom = crdCom.getSoldeProgressif() - commissions;

						creditCom.setTcode(indexTcode);
						creditCom.setDate(date);
						creditCom.setDescr("Commision crédit " +numCredit);
						creditCom.setPiece(piece);
						creditCom.setUserId(ut.getNomUtilisateur());
						creditCom.setCompte(confGl.getCptCommCredit());					
						creditCom.setCredit(commissions);
						creditCom.setCodeInd(dm.getIndividuel());
	
						creditCom.setAccount(crdCom);
						creditCom.setUtilisateur(ut);
						creditCom.setDemandeCredit(dm);
						
						creditCom.setSolde(scCom);
						crdCom.setSoldeProgressif(scCom);						
						
						//Solde Crédité Papeterie						
						Account crdPap = CodeIncrement.getAcount(em, confGl.getCptPapeterie());						
						System.out.println("Compte Crédit papeterie "+crdPap.getNumCpt());	
						
						creditPap.setTcode(indexTcode);
						creditPap.setDate(date);
						creditPap.setDescr("Frais Papéterie crédit " +numCredit);
						creditPap.setPiece(piece);
						creditPap.setUserId(ut.getNomUtilisateur());
						creditPap.setCompte(confGl.getCptPapeterie());					
						creditPap.setCredit(papeteries);
						creditPap.setCodeInd(dm.getIndividuel());
	
						creditPap.setAccount(crdPap);
						creditPap.setUtilisateur(ut);
						creditPap.setDemandeCredit(dm);
						
						double scPap = crdPap.getSoldeProgressif() - papeteries;
						crdPap.setSoldeProgressif(scPap);						
						creditPap.setSolde(scPap);
						
						//Information des Commission Crédit
						paie.setTcode(indexTcode);
						paie.setDemandeCredit(dm);
						paie.setUtilisateur(ut);
						paie.setCash(cash);
						//paie.setCheqid(comm.getCheqid());
						paie.setDatePaie(date);
						paie.setLcomm(commissions);
						paie.setPiece(piece);
						paie.setStationery(papeteries);
						//paie.setTdf(comm.getTdf());
						//paie.setTotvat(comm.getTotvat());

						transaction.begin();
						em.flush();
						em.flush();
						em.flush();
						em.persist(paie);
						em.persist(debitCom);
						em.persist(creditCom);
						em.persist(creditPap);
						em.merge(dm);
						transaction.commit();
						// transaction.commit();
						em.refresh(paie);
						System.out.println("Paiement de commission effectué");
						result = true;
						
					} catch (Exception e) {
						System.err.println(e.getMessage());
						e.printStackTrace();
						result = false;
					}				
				}			
		}
		return result;
	}
	
	/***
	 * APPROBATION
	 * ***/
	@Override
	public String saveApprobation(String numCredit, String Appby,String dateApp, String descApp, double montantApp,
			String stat_demande) {

		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		String result = "";
		boolean validation = false;

		// Vérifie si le numero de crédit n'est pas null
		if (dmd != null) {

			if (dmd.getApprobationStatut().equalsIgnoreCase("Approbation en attente")
					|| dmd.getApprobationStatut().equalsIgnoreCase("payé avant approbation")) {

				// Changer Statut demande en approuvé

				if (dmd.getMontantApproved() == 0 && (montantApp <= dmd.getMontantDemande())) {

					dmd.setAppBy(Appby);
					dmd.setDateApprobation(dateApp);
					dmd.setApprobationStatut(stat_demande);
					dmd.setDescrApprobation(descApp);
					dmd.setMontantApproved(montantApp);
					
					try {
						transaction.begin();
						em.flush();
						transaction.commit();
						em.refresh(dmd);
						System.out.println("crédit "+ stat_demande+ " avec succes!!!");
						result = "Crédit " + stat_demande + " par "	+ dmd.getAppBy();
						validation = true;
					} catch (Exception e) {
						System.err.println("Erreur approbation "
								+ e.getMessage());
						result = "Erreur approbation";
						validation = false;
					}
				} else {
					System.err.println("Montant approuvé null");
					result = "Montant approuvé superieur à "+dmd.getMontantDemande();
				}

			} else {
				result = "Crédit déjà approuvé!!!";
			}

		} else {
			result = "Numéro Crédit non trouvé!!!";
		}
		System.out.println(validation);
		return result;
	}
	

	/***
	 * DECAISSEMENT
	 * ***/
	@SuppressWarnings("unused")
	@Override
	public String saveDecaisement(String date,String typePaie, double montant, double commission, double papeterie, 
			String piece, String numCheque, String numTel,String comptCaise,String numCredit, int userId) {
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		Decaissement decais = new Decaissement();
		Remboursement remb = new Remboursement();
		ConfigCreditIndividuel confInds = dm.getProduitCredit().getConfigCreditIndividuel();
		ConfigGlCredit confGL = dm.getProduitCredit().getConfigGlCredit();
		
		///	pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		List<Calapresdebl> calRembAprDebl = CodeIncrement.getCalendrierRemboursement(dm);
		
		//Solde Demander
		Grandlivre debit = new Grandlivre();
		//Solde Decaissé
		Grandlivre credit1= new Grandlivre();
		//Papeterie
		Grandlivre credit2 = new Grandlivre();
		//Commission
		Grandlivre credit3 = new Grandlivre();

		String result = "";
		//Vérifie si le numero crédit est null
		if (dm != null) {
			//si statut de demande eqal Approuvé ou paye apres approbation on entre
			if (dm.getIndividuel() != null && (dm.getApprobationStatut().equalsIgnoreCase("Approuver") || dm.getApprobationStatut().equalsIgnoreCase(
							"payé apres approbation"))) {

				try {
					//Total solde à décaissé = montant approuvé - (commission + papeterie)
					double soldeDecais = (dm.getMontantApproved()-(commission+papeterie));				
					
					//Interet Total 
					//Interet dans 1 mois en %
					double interAnnuel = 0.0;
					
						if(dm.getIndividuel() != null){
							interAnnuel = confInds.getTauxInteretAnnuel();
						}else if(dm.getGroupe() != null){
							interAnnuel = dm.getProduitCredit().getConfigCreditGroupe().getInteretAnnuel();
						}
					
					double intMensuel = interAnnuel / 12;					
					
					//Montant d'interet par mois
					double intTot = (dm.getMontantApproved() * intMensuel) / 100;
					
					//Solde Total = Montant Approuvé + Interet Total
					double soldeTotal = dm.getMontantApproved() + intTot;
					
					dm.setInteret_total(intTot);
					dm.setPrincipale_total(dm.getMontantApproved());
					dm.setSolde_total(soldeTotal);
					

					//Insertion au GrandLivre
					Account accCred;
					CompteCaisse cptCaisse = new CompteCaisse();
					if(!comptCaise.equalsIgnoreCase("")){
						System.out.println("Compte caisse");
						cptCaisse = em.find(CompteCaisse.class, comptCaise);
						String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
						accCred = CodeIncrement.getAcount(em, cptC);//cptCaisse.getAccount();
						System.out.println("Compte crédité "+accCred.getNumCpt());
						decais.setCptCaisseNum(cptC);
						remb.setCptCaisseNum(cptC);
						credit1.setCompte(cptC);
						credit1.setAccount(accCred);
						
						double sds = accCred.getSoldeProgressif() - dm.getMontantApproved();
						credit1.setSolde(sds);
						accCred.setSoldeProgressif(sds);
					}
					
					if(typePaie.equalsIgnoreCase("cheque")){
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						credit1.setAccount(accCred);
						
						double sds = accCred.getSoldeProgressif() - dm.getMontantApproved();
						credit1.setSolde(sds);
						accCred.setSoldeProgressif(sds);
						
						remb.setValPaie(numCheque);
					}else if(typePaie.equalsIgnoreCase("mobile")){
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						credit1.setAccount(accCred);
						
						double sds = accCred.getSoldeProgressif() - dm.getMontantApproved();
						credit1.setSolde(sds);
						accCred.setSoldeProgressif(sds);
						
						remb.setValPaie(numTel);
					}
					//decais.setTcode(decaissement.getTcode());

					decais.setTcode(indexTcode);
					decais.setUtilisateur(ut);
					decais.setDemandeCredit(dm);
					decais.setDateDec(date);
					decais.setCommission(commission);
					decais.setStationnary(papeterie);
					decais.setMontantDec(montant);
					decais.setPiece(piece);
					
					remb.setTcode(indexTcode);
					remb.setDemandeCredit(dm);
					remb.setUtilisateur(ut);
					remb.setDateRemb(date);
					remb.setPiece(piece);
					remb.setTypeAction("Décaissement");
					remb.setTypePaie(typePaie);
					remb.setSolde(soldeTotal);
					remb.setTotalInteret(intTot);
					remb.setTotalPrincipale(dm.getMontantApproved());				
					remb.setStationery((float)papeterie);
					remb.setOverpay((float)commission);
					
					dm.setApprobationStatut("Demande decaissé");
					
					//Solde Debité
					Account accDeb = CodeIncrement.getAcount(em, confGL.getCptPrincEnCoursInd());
					debit.setTcode(indexTcode);
					debit.setDate(date);
					debit.setDescr("Decaissement crédit " +numCredit);
					debit.setPiece(piece);
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(confGL.getCptPrincEnCoursInd());					
					debit.setDebit(dm.getMontantApproved());
					debit.setCodeInd(dm.getIndividuel());

					double sdDeb = dm.getMontantApproved()+accDeb.getSoldeProgressif();
					
					debit.setAccount(accDeb);
					debit.setUtilisateur(ut);
					debit.setDemandeCredit(dm);
					debit.setSolde(sdDeb);
					accDeb.setSoldeProgressif(sdDeb); 
					
					//Solde Crédité 1 (Solde Decaissé)
					credit1.setTcode(indexTcode);
					credit1.setDate(date);
					credit1.setDescr("Decaissement crédit "+numCredit);
					credit1.setPiece(piece);
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCredit(soldeDecais);
					credit1.setCodeInd(dm.getIndividuel());
					
					credit1.setUtilisateur(ut);
					credit1.setDemandeCredit(dm);
					
					transaction.begin();
					//Solde Crédité 2 (Papeterie Decaissement)
					if(papeterie != 0){
						Account cred2 = CodeIncrement.getAcount(em, confGL.getPapeterieDec());
						credit2.setTcode(indexTcode);
						credit2.setDate(date);
						credit2.setDescr("Papeterie au decaissement de crédit "+numCredit);
						credit2.setPiece(piece);
						credit2.setUserId(ut.getNomUtilisateur());
						credit2.setCompte(confGL.getPapeterieDec());
						credit2.setDebit(papeterie);	
						
						credit2.setCodeInd(dm.getIndividuel());
						credit2.setAccount(cred2);
						credit2.setUtilisateur(ut);
						credit2.setDemandeCredit(dm);
						
						double sCred2 = cred2.getSoldeProgressif() + papeterie;	
						credit2.setSolde(sCred2);
						cred2.setSoldeProgressif(sCred2);
						em.flush();
						em.persist(credit2);
					
					}
					
					//Solde Crédité 3 (Commission au Decaissement)
					if(commission != 0){
						Account deb3 = CodeIncrement.getAcount(em, confGL.getCommDec());
						
						credit3.setTcode(indexTcode);
						credit3.setDate(date);
						credit3.setDescr("Commission au decaissement de crédit "+numCredit);
						credit3.setPiece(piece);
						credit3.setUserId(ut.getNomUtilisateur());
						credit3.setCompte(confGL.getCommDec());
						credit3.setDebit(commission);

						credit3.setCodeInd(dm.getIndividuel());
						credit3.setAccount(deb3);
						credit3.setUtilisateur(ut);
						credit3.setDemandeCredit(dm);
						
						double sCred3 = deb3.getSoldeProgressif() + commission;	
						credit3.setSolde(sCred3);
						deb3.setSoldeProgressif(sCred3);
						em.flush();						
						em.persist(credit3);
					}
					em.flush();
					em.flush(); 
					em.persist(remb);
					em.persist(debit);
					em.persist(credit1);		
					em.merge(dm);
					em.persist(decais);
					for (Calapresdebl calapresdebl : calRembAprDebl) {
						for (int i = 0; i < calRembAprDebl.size()-1; i++) {
							calapresdebl.setDemandeCredit(dm); 							
						}  					  
					  em.persist(calapresdebl);
					}
	
					transaction.commit();
					em.refresh(dm);
					em.refresh(remb);
					em.refresh(decais);
					
					System.out.println("Decaisemment reussit!!!");
					result = "Decaisemment reussit!!!";

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				result = "Desolé, Vous ne pouvez pas decaisser cet demande!!!";
			}

		}
		else{
			result = "Numéro crédit non trouvé!!!";
			}
		return result;
	}

	static Long calculDifferenceDate(String date, String numCredit){
		Query requete = em
				.createQuery("SELECT MIN(c.date) FROM Calapresdebl c JOIN c.demandeCredit numCred WHERE"
						+ " c.payer = :p AND numCred.numCredit = :numC");
		requete.setParameter("p", false);
		requete.setParameter("numC", numCredit);
		String dateCal = (String) requete.getSingleResult();
		try {
			
			LocalDate dateparm = LocalDate.parse(date);
			LocalDate dateDu = LocalDate.parse(dateCal);
			System.out.println(dateparm);
			System.out.println(dateDu);
	
			// Verifie la difference entre la date de remboursement par
			// rapport à la date de calandrier remboursement
			//int val = Period.between(dateDu, dateparm).getDays();
			Long val = ChronoUnit.DAYS.between(dateDu,dateparm);			
			System.out.println(val + "\n");			
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/***
	 * REMBOURSEMENT
	 * ***/

	@SuppressWarnings({ "rawtypes", "unused"})
	@Override
	public boolean saveRemboursement(String numCredit, int userId, String date,
			double montant, String piece,String typePaie,String numTel,String numCheque, String cmptCaisse) { 

		DemandeCredit dm = em.find(DemandeCredit.class, numCredit); Utilisateur ut = em.find(Utilisateur.class, userId);		
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
		int pen = 5;
		
		//valeur de retour		
		boolean retour = false;  
		
		CompteCaisse cptCaisse = new CompteCaisse();
		String cptC = "";		
		if(!cmptCaisse.equalsIgnoreCase("")){
			cptCaisse = em.find(CompteCaisse.class, cmptCaisse);
			cptC = String.valueOf(cptCaisse.getAccount().getTkey());
		}
		// Instatiation grand livre
		Grandlivre debit;
		Grandlivre credit1;
		Grandlivre credit2;		

		//pour afficher le resultat après une requette ou calcul
		String results = "";
		
		//Verifie si le credit existe
		if (dm != null) {
			
			//Verifie si le crédit est déjà remboursé ou pas
			if (dm.getSolde_total() != 0) {//>	
				
				// recupérer le dernier Principale total, intérêt total, solde total  du crédit
				double principalTotal = dm.getPrincipale_total();
				double interetTotal = dm.getInteret_total();
				double soldeTotal = dm.getSolde_total();
				
				//calcul difference entre date échéance due parraport à la date aujourd'hui
				//Long diffDate = calculDifferenceDate(date, numCredit);
									
				//initialisation de la valeur du capital à ajouter dans le grand livre
				double capitals = 0.0;
				double inters = 0.0;
				double tots = 0.0;
						
				//Traitement remboursement
				while(montant > 0){					
					//Instance des classes necessaire
					Remboursement saveRemb = new Remboursement(); 
					Calapresdebl cals = new Calapresdebl(); 
					
					// / pour incrémenter le tcode dans la table grandlivre
					String indexTcode = CodeIncrement.genTcode(em);	
					
					//selection de date minimum au calendrier après deblocage
					Query requete = em
							.createQuery("SELECT MIN(c.date) FROM Calapresdebl c JOIN c.demandeCredit numCred WHERE"
									+ " c.payer = :p AND numCred.numCredit = :numC");
					requete.setParameter("p", false);
					requete.setParameter("numC", numCredit);
					String dateCal = (String) requete.getSingleResult();
										
					// Selection de principale due et intérêt due
					Query query = em.createQuery("SELECT cl FROM Calapresdebl cl JOIN cl.demandeCredit num"
									+ " WHERE cl.date = :dateRemb AND num.numCredit = :numCred");
					
					// query.setParameter("x", false);cl.payer = :x AND
					query.setParameter("dateRemb", dateCal);
					query.setParameter("numCred", "" + numCredit + "");
					
					double princ = 0.0;
					double inter = 0.0;
					int rowId = 0;
					double restMontant = 0.0;
					
					// Recupère le montant principal et interet à rembouser
					List resultList = query.getResultList();
					if (resultList != null) {
						
						cals = (Calapresdebl) query.getSingleResult();
						
						princ = cals.getMprinc();
						inter = cals.getMint();
						rowId = cals.getRowId();
					
						System.out.println("calendrier numero: "+ rowId);
						System.out.println("Principal total: " + princ);
						System.out.println("Interet total: " + inter);
						
						// Montant a remboursé
						double montRembourser = montant;							
						// Recuperer le montant reste au dernier remboursement			
						/*Query qr = em.createQuery("SELECT r.restaPaie FROM Remboursement r JOIN r.demandeCredit numDem"
										+ " WHERE r.dateRemb =( SELECT MAX(rb.dateRemb) FROM Remboursement rb JOIN rb.demandeCredit nums WHERE nums.numCredit"
										+ "= :numsCredit AND r.typeAction = :a) AND numDem.numCredit = :numero");
						qr.setParameter("numsCredit", numCredit);
						qr.setParameter("a", "Remboursement");
						qr.setParameter("numero", numCredit);						

						if (qr.getResultList().isEmpty()) {
							montRembourser = montant;
						} else {
							double derRestPaie = (double)qr.getSingleResult();
							montRembourser = montant + derRestPaie;								
							System.out.println("montant reste de dernière paiement : "+ derRestPaie);
						}*/
						// On considère que le paiement de l'intérêt est
						// prioritaire
						//soustraction du montant payé par le montant d'intérêt due
						double restPaieInt = montRembourser - inter;						
						
						double interetRemb = 0.0;
						double principaleRem = 0.0;
						double restPaiePrinc = 0.0;

						if (restPaieInt > 0.0) {
							
							interetRemb = inter;
							inters = interetRemb;

							restPaiePrinc = restPaieInt - princ;

							if (restPaiePrinc >= 0.0) {
								principaleRem = princ; cals.setPayer(true);	
							} else if (restPaiePrinc < 0.0) {
								principaleRem = restPaieInt; 
								cals.setMpen((float)((restPaiePrinc*(-1))*pen)/100 ); cals.setPayer(false);
							}
							
							restMontant = restPaiePrinc;
							capitals = principaleRem;

							principalTotal = principalTotal - principaleRem;
							interetTotal = interetTotal - interetRemb;
							soldeTotal = principalTotal + interetTotal;

							if (soldeTotal == 0.0) {
								dm.setApprobationStatut("Credit remboursé");
							}

							// Insertion à l'Entité Remboursement
							
							//requette pour recupérer le nombre échéance déjà fait
							String rba = "Remboursement";
					
							Query qu =  em.createQuery("SELECT MAX(re.nbEcheance) FROM Remboursement re JOIN re.demandeCredit numCred WHERE"
											+ " re.typeAction = :te AND numCred.numCredit = :num");
							qu.setParameter("te", rba);
							qu.setParameter("num", numCredit);
							int nbEcheance=1;
							
							if((qu.getSingleResult()) == null)
								nbEcheance = 1;
							else{
								int a = Integer.parseInt(qu.getSingleResult().toString());
								nbEcheance = a+1;
							}
							
							//Prend le type de paiement fait par l'utilisateur: paiement cash, par chèque ou par mobile
							if(typePaie.equalsIgnoreCase("cash")){
								saveRemb.setCptCaisseNum(cptC);
							}else if(typePaie.equalsIgnoreCase("cheque")){
								saveRemb.setValPaie(numCheque);
							}else if(typePaie.equalsIgnoreCase("mobile")){
								saveRemb.setValPaie(numTel);
							}
							saveRemb.setTcode(indexTcode);
							saveRemb.setDemandeCredit(dm);
							saveRemb.setUtilisateur(ut);
							saveRemb.setDateRemb(date);
							saveRemb.setPiece(piece);
							saveRemb.setNbEcheance(nbEcheance);
							saveRemb.setTypeAction("Remboursement");
							saveRemb.setTypePaie(typePaie);
							
							
							saveRemb.setCheqcomm(0);
							saveRemb.setStationery(0);
							saveRemb.setOverpay(0);
							
							saveRemb.setPrincipal(principaleRem);
							saveRemb.setInteret(interetRemb);
							saveRemb.setMontant_paye(montant);
							saveRemb.setRestaPaie(restPaiePrinc);
							
							saveRemb.setSolde(soldeTotal);
							saveRemb.setTotalPrincipale(principalTotal);
							saveRemb.setTotalInteret(interetTotal);

							// Modification sur l'entité DemandeCredit
							dm.setSolde_total(soldeTotal);
							dm.setPrincipale_total(principalTotal);
							dm.setInteret_total(interetTotal);
							
							try {
								transaction.begin();
								em.merge(cals);
								em.merge(dm);
								em.persist(saveRemb);
								transaction.commit();
								em.refresh(saveRemb);
								em.refresh(dm);
								em.refresh(cals); 
								//montant = montant - restPaiePrinc;
								results = "Remboursement Enregistrer!!!";
								System.out.println(results);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								if (em == null) {
									em.close();
								}
							}										

						} else if (restPaieInt < 0.0) {
							System.out.println("montant inferieur au interet due");
						}				
					} else {
						results = "Ce client ne pouvez pas rembourser aujourd'hui";
					}
					
				
				//imputation comptable remboursemnt
				//Remboursement des client sain
				if(dm.getIndividuel().isSain() == true){
					debit = new Grandlivre(); 
					credit1 = new Grandlivre(); 
					credit2 = new Grandlivre();	
					
					tots = capitals + inters;
					
					// Solde Debité
					Account accDebit = CodeIncrement.getAcount(em, cptC);
					double sdDeb = accDebit.getSoldeProgressif() + tots;
					
					debit.setTcode(indexTcode);
					debit.setDate(date);
					debit.setDescr("Remboursement du crédit "
							+ numCredit);
					debit.setPiece(piece);
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(cptC);
					debit.setDebit(tots);
					
					debit.setCodeInd(dm.getIndividuel());
					debit.setDemandeCredit(dm); 
					debit.setUtilisateur(ut); 
					debit.setAccount(accDebit);
					debit.setSolde(sdDeb);
					accDebit.setSoldeProgressif(sdDeb); 		
					
					// Solde Crédité 1 (Principal)
					Account accCred1 = CodeIncrement.getAcount(em, confGl.getCptPrincEnCoursInd());
					double sdCrd1 = accCred1.getSoldeProgressif() - capitals;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(date);
					credit1.setDescr("Remboursement principal du crédit "
							+ numCredit);
					credit1.setPiece(piece);
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(confGl.getCptPrincEnCoursInd());
					credit1.setCredit(capitals);
					
					credit1.setCodeInd(dm.getIndividuel());
					credit1.setDemandeCredit(dm); 
					credit1.setUtilisateur(ut); 
					credit1.setAccount(accCred1);
					credit1.setSolde(sdCrd1);
					accCred1.setSoldeProgressif(sdCrd1); 
					
					// Solde Crédité 2 (Interet)
					Account accCred2 = CodeIncrement.getAcount(em, confGl.getCptIntRecCrdtInd());
					double sdCrd2 = accCred2.getSoldeProgressif() - inters;

					credit2.setTcode(indexTcode);
					credit2.setDate(date);
					credit2.setDescr("Remboursement Interet du crédit "
							+ numCredit);
					credit2.setPiece(piece);
					credit2.setUserId(ut.getNomUtilisateur());
					credit2.setCompte(confGl.getCptIntRecCrdtInd());
					credit2.setCredit(inters);					
					
					credit2.setCodeInd(dm.getIndividuel());
					credit2.setDemandeCredit(dm); 
					credit2.setUtilisateur(ut); 
					
					credit2.setAccount(accCred2);
					credit2.setSolde(sdCrd2);
					accCred2.setSoldeProgressif(sdCrd2); 
					
					try {
						
						transaction.begin();
						em.flush();
						em.flush();
						em.flush();
						em.persist(debit);
						em.persist(credit1);
						em.persist(credit2);
						transaction.commit();
						em.refresh(debit);
						em.refresh(credit1);
						em.refresh(credit2);
						em.refresh(accDebit);
						em.refresh(accCred1);
						em.refresh(accCred2); 
						System.out.println("Remboursement normal du crédit n° "+ dm.getNumCredit() +" bien enregistré");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(dm.getIndividuel().isListeNoir() == true){
					//Instance de vérification grand livre
					Grandlivre glOld = new Grandlivre();
					Account accVNI = CodeIncrement.getAcount(em, "263");
					
					//Remboursement VNI
					debit = new Grandlivre();
					credit1 = new Grandlivre();
					
					//Information de compte à débité : Compte caisse 101
					Account debiCaisse = CodeIncrement.getAcount(em, cptC);
					double sdDebs = debiCaisse.getSoldeProgressif() + capitals;
					
					debit.setTcode(indexTcode);
					debit.setDate(date);
					debit.setDescr("Remboursement capital VNI du crédit "
							+ dm.getNumCredit());
					debit.setPiece(piece);
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(cptC);
					debit.setDebit(capitals);
					
					debit.setCodeInd(dm.getIndividuel());
					debit.setDemandeCredit(dm); 
					debit.setUtilisateur(ut); 
					debit.setAccount(debiCaisse);
					debit.setSolde(sdDebs);
					debiCaisse.setSoldeProgressif(sdDebs); 
					
					
					// Information de compte à crédité :compte 26xxxx
					double sdCrdVNI = accVNI.getSoldeProgressif() - capitals;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(date);
					credit1.setDescr("Remboursement capital VNI du crédit "
							+ dm.getNumCredit());
					credit1.setPiece(piece);
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(accVNI.getNumCpt());
					credit1.setCredit(capitals);
					
					credit1.setCodeInd(dm.getIndividuel());
					credit1.setDemandeCredit(dm); 
					credit1.setUtilisateur(ut); 
					credit1.setAccount(accVNI);
					credit1.setSolde(sdCrdVNI);
					accVNI.setSoldeProgressif(sdCrdVNI); 
					
				
					try {
						
						transaction.begin();
						em.flush();
						em.flush();
						em.persist(debit);
						em.persist(credit1);
						transaction.commit();
						
						em.refresh(debit);
						em.refresh(credit1);
						em.refresh(accVNI);
						System.out.println("Reprise VNI du crédit n° "+ dm.getNumCredit() +" bien enregistré");
				
					} catch (Exception e) {
						e.printStackTrace();
					}								
					
				}
				else if(dm.getIndividuel().isListeRouge() == true){
					    
					Account accDouteux = CodeIncrement.getAcount(em, "27");
						
					//Recouvrement de créance douteux
					debit = new Grandlivre();
					credit1 = new Grandlivre();
					
					//Information de compte à débité : Compte caisse 101
					Account debiCaisse = CodeIncrement.getAcount(em, cptC);
					double sdDebs = debiCaisse.getSoldeProgressif() + capitals;
					
					debit.setTcode(indexTcode);
					debit.setDate(date);
					debit.setDescr("Recouvrement capital du crédit "
							+ dm.getNumCredit());
					debit.setPiece(piece);
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(cptC);
					debit.setDebit(capitals);
					
					debit.setCodeInd(dm.getIndividuel());
					debit.setDemandeCredit(dm); 
					debit.setUtilisateur(ut); 
					debit.setAccount(debiCaisse);
					debit.setSolde(sdDebs);
					debiCaisse.setSoldeProgressif(sdDebs); 
					
					
					// Information de compte à crédité :compte 27xxxx
					double sdCrdDouteux = accDouteux.getSoldeProgressif() - capitals;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(date);
					credit1.setDescr("Recouvrement capital du crédit "
							+ dm.getNumCredit());
					credit1.setPiece(piece);
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(accDouteux.getNumCpt());
					credit1.setCredit(capitals);
					
					credit1.setCodeInd(dm.getIndividuel());
					credit1.setDemandeCredit(dm); 
					credit1.setUtilisateur(ut); 
					credit1.setAccount(accDouteux);
					credit1.setSolde(sdCrdDouteux);
					accDouteux.setSoldeProgressif(sdCrdDouteux); 				
				
					try {							
						transaction.begin();
						em.flush();
						em.flush();
						em.persist(debit);
						em.persist(credit1);
						transaction.commit();
						em.refresh(debit);
						em.refresh(credit1);
						System.out.println("Reprise dotation du crédit n° "+ dm.getNumCredit() +" bien enregistré");
				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
				
				montant = restMontant;
				
				System.out.println("Montant = "+ montant);
			}
				
				retour = true;
			} else {
				results = "Crédit déjà rembourser!!!";
			}
		} else {
			results = "ce numero credit n'existe pas!";
		}
		return retour;
	}

	
	/***
	 * DERNIERE REMBOURSEMENT
	 * ***/
	@SuppressWarnings({"rawtypes" })
	@Override
	public List<String> dernierRemboursement(String numCredit) {
		///Recuperer le montant reste au dernier remboursement
		List<String> lst = new ArrayList<String>();
		try {
			Query qr = em.createQuery("SELECT MAX(r.dateRemb), numDem.solde_total FROM Remboursement r JOIN r.demandeCredit numDem"
					+ " WHERE numDem.numCredit = :numero AND r.typeAction= :x");
			qr.setParameter("numero", numCredit);		
			qr.setParameter("x", "Remboursement");
			
			//On considère que le paiement de l'interet est prioritaire
			
			List listReq = qr.getResultList();
			String dateR = new String();
			String solde = new String();
			
				for (Object ligneAsobject : listReq) {
					Object[] ligne = (Object[]) ligneAsobject;
					
					dateR = ligne[0].toString();
					solde = ligne[1].toString();
					
					lst.add(solde);
					lst.add(dateR);
					System.out.println("Principal total: " + dateR);
					System.out.println("Interet total: " + solde);
				}
			
			
		} catch (NullPointerException e) {
			//e.printStackTrace();
			String erreur = "pas de remboursement jusque là";
			lst.add("0");
			lst.add("xxxx-xx-xx");
			System.err.println(erreur);
		} 
	
		return lst;
	}
	
	/***
	 * AFFICHE MONTANT
	 * ***/
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<String> getMontaRemb(String numCredit,String date) {
		
		List<String> result = new ArrayList<String>();
		
		// Selection de date qu'on doit faire le remboursement
		Query requete = em.createQuery("SELECT MIN(c.date) FROM Calapresdebl c JOIN c.demandeCredit numCred WHERE"
						+ " c.payer = :p AND numCred.numCredit = :numC");
		requete.setParameter("p", false);
		requete.setParameter("numC", numCredit);

		String dateCal = (String) requete.getSingleResult();
		
		try {
		
			LocalDate dateparm = LocalDate.parse(date);
			LocalDate dateDu = LocalDate.parse(dateCal);
			System.out.println(dateparm);
			System.out.println(dateDu);
	
			// Verifie la difference entre la date de remboursement par rapport à la date de calandrier remboursement
			
			//int val = Period.between(dateDu, dateparm).getDays();
	        Long val = ChronoUnit.DAYS.between(dateDu,dateparm);

			//Days.daysBetween
			
			//int v2 = Days.daysBetween(arg0, arg1);
			System.out.println("difference entre "+dateDu+" et "+dateparm+" est "+val + "\n");
			//List<E>
			if(val < 0){
				String info = "Remboursement anticipative";
				String nbJours = "remboursement dans:"+val+" jour(s)";
				System.out.println(info);
				result.add("0");
				result.add("5000");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add("0");
				result.add(info);
				result.add(nbJours);
				result.add("");
			}
			else if(val == 0){
				System.out.println("Remboursement normal");
				// Selection de principale due et intérêt due

				Query query = em.createQuery("SELECT cl FROM Calapresdebl cl JOIN cl.demandeCredit num"
								+ " WHERE cl.date = :dateRemb AND num.numCredit = :numCred");
				// query.setParameter("x", false);cl.payer = :x AND
				query.setParameter("dateRemb", dateCal);
				query.setParameter("numCred", "" + numCredit + "");

				double princ = 0.0;
				double inter = 0.0;
				double motantTotal = 0.0;

				// Recupère le montant principal et interet à rembouser

				if (! query.getResultList().isEmpty()) {
					
					Calapresdebl clps = (Calapresdebl) query.getSingleResult();
					
					princ = clps.getMprinc();
					inter = clps.getMint();
					
					System.out.println("Principal total: " + princ);
					System.out.println("Interet total: " + inter);
					motantTotal = princ + inter;
					
					String info = "Remboursement normal";
					//String nbJours = "remboursement dans "+val+" jour(s)";

					result.add("0");
					result.add("0");
					result.add("0");
					result.add("0");
					result.add("0");
					result.add(String.valueOf(inter));
					result.add(String.valueOf(princ));
					result.add("0");
					result.add("0");
					result.add(String.valueOf(motantTotal));
					result.add(info);
					result.add("");
					result.add("");
				}

			}
			else if(val > 0){
				
				List<Calapresdebl> calPenlt = new ArrayList<Calapresdebl>();
				TypedQuery<Calapresdebl> rq = em.createQuery("select clp from Calapresdebl clp JOIN clp.demandeCredit num"
								+ " where clp.payer = :py AND clp.date < :dateRembs AND num.numCredit = :numCred",Calapresdebl.class);
				
				rq.setParameter("py", false);
				rq.setParameter("dateRembs", date);
				rq.setParameter("numCred", numCredit);
				calPenlt = rq.getResultList();
				
				String echeansTard = "Echéance de retard : "+calPenlt.size();
				
				System.out.println(echeansTard);
				double montRest = calPenlt.get(0).getMprinc() + calPenlt.get(0).getMint();
				int tauxPenalite = 5;
				double totalPenalte = ((montRest * tauxPenalite)/100)*calPenlt.size();
				
				System.out.println("pénalité de retard = "+totalPenalte);
				
				String info = "";
				String nbJours = "";
				
				if(val < 90){
					System.out.println("Remboursement en retard sans declassement créance saine");
					
					// Selection de principale due et intérêt due

					Query query = em.createQuery("SELECT SUM(cl.mprinc),SUM(cl.mint) FROM Calapresdebl cl JOIN cl.demandeCredit num"
									+ " WHERE cl.payer = :x AND cl.date <= :dateRemb AND num.numCredit = :numCred");
					query.setParameter("x", false);
					query.setParameter("dateRemb", date);
					query.setParameter("numCred", "" + numCredit + "");

					String princ = "";
					String inter = "";
					double motantTotal = 0.0;

					// Recupère le montant principal et interet à rembouser
					
					info = "Remboursement en retard sans declassemen";
					nbJours = "nombre jour de retard "+val+" jour(s)";

					List resultList = query.getResultList();
					if (!resultList.isEmpty()) {
				
						for (Object ligneAsobject : resultList) {
							Object[] ligne = (Object[]) ligneAsobject;

							princ = ligne[0].toString();
							inter = ligne[1].toString();
							motantTotal = Double.parseDouble(princ) + Double.parseDouble(inter);
							
							System.out.println("Principal total: " + princ);
							System.out.println("Interet total: " + inter);
						
						}
						result.add("0");
						result.add(String.valueOf(totalPenalte));
						result.add("0");
						result.add(inter);
						result.add(princ);
						result.add("0");
						result.add("0");
						result.add("0");
						result.add("0");
						result.add(String.valueOf(motantTotal));
						result.add(info);
						result.add(nbJours);
						result.add(echeansTard);
					}
					
				}else if(val > 90){
					System.out.println("Remboursement en retard, crédit déclassé au créance douteux");
					// Selection de principale due et intérêt due

					Query query = em.createQuery("SELECT SUM(cl.mprinc),SUM(cl.mint) FROM Calapresdebl cl JOIN cl.demandeCredit num"
									+ " WHERE cl.payer = :x AND cl.date <= :dateRemb AND num.numCredit = :numCred");
					query.setParameter("x", false);
					query.setParameter("dateRemb", date);
					query.setParameter("numCred", "" + numCredit + "");

					String princ = "";
					String inter = "";
					double motantTotal = 0.0;

					// Recupère le montant principal et interet à rembouser
					
					info = "Remboursement en retard, crédit déclassé au créance douteux";
					nbJours = "nombre jour de retard "+val+" jour(s)";

					List resultList = query.getResultList();
					if (!resultList.isEmpty()) {
				
						for (Object ligneAsobject : resultList) {
							Object[] ligne = (Object[]) ligneAsobject;

							princ = ligne[0].toString();
							inter = ligne[1].toString();
							motantTotal = Double.parseDouble(princ) + Double.parseDouble(inter);
							
							System.out.println("Principal total: " + princ);
							System.out.println("Interet total: " + inter);
						
						}
						result.add("0");
						result.add(String.valueOf(totalPenalte));
						result.add("0");
						result.add(inter);
						result.add(princ);
						result.add("0");
						result.add("0");
						result.add("0");
						result.add("0");
						result.add(String.valueOf(motantTotal));
						result.add(info);
						result.add(nbJours);
						result.add(echeansTard);
					}
				}					
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return result;
	}

	/***
	 * HISTORIQUE CREDIT
	 * ***/
	@Override
	public List<Calapresdebl> historiqueCredit(String numCredit) {
		List<Calapresdebl> result = new ArrayList<Calapresdebl>();
		TypedQuery<Calapresdebl> q = em.createQuery("SELECT cl FROM Calapresdebl cl JOIN  cl.demandeCredit dm"
				+ " WHERE dm.numCredit = :num ORDER BY cl.date ASC",Calapresdebl.class);
		q.setParameter("num", numCredit);
		
		result = q.getResultList();
		return result;
	}	
	
	/***
	 * LISTES COMPTES CAISSES
	 * ****/
	

	@Override
	public List<CompteCaisse> findAllComptCaisse() {
		TypedQuery<CompteCaisse> query = em.createQuery("SELECT c FROM CompteCaisse c",CompteCaisse.class);
		
		List<CompteCaisse> result = query.getResultList();
		return result;
	}

	
	//recuperer information client
	@Override
	public String getInfoClient(String code) {
		
		//DemandeCredit dm = new DemandeCredit();
		String result = "";
		
		String sql = "select max(d.nbCredit) from DemandeCredit d join d.individuel i where i.codeInd = '"+code+"'";
		Query q = em.createQuery(sql);
		
		int nbCredit=Integer.parseInt(q.getSingleResult().toString());
		
		System.out.println("nombre crédit: "+nbCredit);
		
		int nb = nbCredit+1;
		
		result +="C'est la "+nb;
		
		if(nbCredit < 1){
			result += "ère";
		}else{
			result += "ème"; 
		}
		
		result +=" fois que ce client fait une demande crédit";
		
		return result;
	}

	/*******************************************************************************************************************************/
	
								/******************* CONFIGURATION PRODUIT CREDIT *************************/
	
	/******************************************************************************************************************************/

	/***
	 * CONFIGURATION POUR TOUT CREDIT
	 * ***/

	@Override
	public void configToutCredit(ConfigCredit configs) {

		try {
			transaction.begin();
			em.persist(configs);			
			transaction.commit();
			System.out.println("Configuration crédit enregistré");
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Configuration GENERAL produit crédit
	 * **/ 
	@Override
	public void configGnrlCredit(ConfigGeneralCredit configGenCredit,String idProduit) {
	
		ProduitCredit pdt = em.find(ProduitCredit.class, idProduit);
		
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
				
		pdt.setConfigCredit(conf);
		pdt.setConfigGeneralCredit(configGenCredit);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out
					.println("Configuration générale du produit " + idProduit);
		} catch (Exception e) {
			System.err.println("Configuration générale non insérée "
					+ e.getMessage());
		}
	}

	
	/**
	 * Configuration crédit individuel
	 * **/
	
	@Override
	public boolean configCreditInd(ConfigCreditIndividuel configIndCredit,String idProduit) {
		ProduitCredit pdtCrdt = em.find(ProduitCredit.class, idProduit);
		
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
						
		try {
			pdtCrdt.setConfigCredit(conf);
			pdtCrdt.setConfigCreditIndividuel(configIndCredit);
			
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration crédit individuel du produit "
					+ pdtCrdt.getIdProdCredit() + " inséré");
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
		return true	;
	}
	
	/***
	 * CONFIGURATION CREDIT D'UN GROUPE
	 * ***/
	
	@Override
	public void configGroup(ConfigCreditGroup confGroup, String idProduit) {
		ProduitCredit pdtCrdt = em.find(ProduitCredit.class, idProduit);
		
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			pdtCrdt.setConfigCredit(conf);
			pdtCrdt.setConfigCreditGroupe(confGroup);
			
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration crédit group du produit "
					+ pdtCrdt.getIdProdCredit() + " inséré");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Configuration frais crédit
	 * **/ 
	@Override
	public void configFraisCredits(ConfigFraisCredit configFraisCredit,	String idProduit) {
		
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfigFraisCredit(configFraisCredit);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration frais crédit du produit "
					+ idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
	}
	
	/***
	 * CONFIGURATION FRAIS CREDIT POUR GROUPE
	 * ***/

	@Override
	public void configFraisCreditGroupes(ConfigFraisCreditGroupe confFraisGroupe, String idPr) {
		
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idPr);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfFraisGroupe(confFraisGroupe);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration frais groupe du produit crédit "
					+ idPr);
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
		
	}
	
	/**
	 * Configuration garantie crédit
	 * **/
	@Override
	public void configGarantiCredit(ConfigGarantieCredit configGarCredit,String idProduit) {
	
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfigGarantieCredit(configGarCredit);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration Grantie crédit du produit "
					+ idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
	}

	/**
	 * Configuration GL crédit
	 * **/ 
	@Override
	public void configGLCredit(ConfigGlCredit configGLCredit, String idProduit) {
	
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfigGlCredit(configGLCredit);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration Gl crédit du produit "
					+ idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
	}
	
	/***
	 * CONFIG PENALITE
	 * ***/

	@Override
	public void configPenalite(ConfigPenaliteCredit confPen, String idProduit) {

		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfigPenaliteCredit(confPen);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration penalité crédit du produit "
					+ idProduit);
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
		}
	}

	//HISTORIQUE CREDIT
	@Override
	public List<Remboursement> historiqueCreditTout(String numCredit) {
		List<Remboursement> result = new ArrayList<Remboursement>();
		TypedQuery<Remboursement> quer = em.createQuery("SELECT r FROM Remboursement r JOIN "
				+ "r.demandeCredit d WHERE d.numCredit = :x",Remboursement.class);
		quer.setParameter("x", numCredit);
		if(!quer.getResultList().isEmpty()){
			result = quer.getResultList();
			return result;
		}else{
			return null;			
		}
	}

	@Override
	public void saveCalendrier(List<Calpaiementdue> calendrier) {
		for (int i = 0; i < calendrier.size(); i++) {
			System.out.println("date "+ calendrier.get(i).getDate());
			System.out.println("principale "+ calendrier.get(i).getMontantPrinc());
			System.out.println("intérêt "+ calendrier.get(i).getMontantInt());
		}
		
	}

	//Fiche crédit
	@Override
	public List<FicheCred> ficheCredit(String numCred) {
		
		List<FicheCred> retour = new ArrayList<FicheCred>();
		
		FicheCred demand = new FicheCred();
		FicheCred approb = new FicheCred();
		
		//Recuperation du credit en question
		DemandeCredit dm = em.find(DemandeCredit.class, numCred);
		
		//Recuperation calendrier
		String sql = "select c from Calapresdebl c join c.demandeCredit d where d.numCredit= '"+numCred+"' order by c.date asc";
		
		List<Calapresdebl> listCal  = em.createQuery(sql,Calapresdebl.class).getResultList();
		
		CommissionCredit comms = (CommissionCredit) 
		em.createQuery("select c from CommissionCredit c join c.demandeCredit d where d.numCredit= '"+numCred+"'")
				.getSingleResult();
		
		List<Remboursement> remb = em.createQuery
				("select r from Remboursement r join r.demandeCredit d where d.numCredit= '"+numCred+"'",
				Remboursement.class).getResultList();
		
		//Decaissement
		Decaissement dec = (Decaissement) em.createQuery("select c from Decaissement c join c.demandeCredit d where d.numCredit= '"+numCred+"'")
				.getSingleResult();
			
		//ligne demande credit
		demand.setDate(dm.getDateDemande());
		demand.setTransaction("Demande Credit");
		demand.setPiece("");
		demand.setPrincipal(dm.getMontantDemande());
		demand.setInteret(0);
		demand.setPenalite(0);
		demand.setSoldeTotal(dm.getMontantDemande());		
		retour.add(demand);
		
		//ligne approbation
		approb.setDate(dm.getDateApprobation());
		approb.setTransaction("Approbation Credit");
		approb.setPiece("");
		approb.setPrincipal(dm.getMontantApproved());
		approb.setInteret(0);
		approb.setPenalite(0);
		approb.setSoldeTotal(dm.getMontantApproved());
		retour.add(approb);
		//Commission
		if(comms != null){
			FicheCred com = new FicheCred();
			com.setDate(comms.getDatePaie());
			com.setTransaction("Commission Credit");
			com.setPiece(comms.getPiece());
			com.setPrincipal(comms.getLcomm());
			com.setInteret(comms.getStationery());
			com.setPenalite(0);
			com.setSoldeTotal(comms.getLcomm() + comms.getStationery());
			retour.add(com);
		}
		
		//Decaissement
		if(dec != null){
			FicheCred fichDec = new FicheCred();
			fichDec.setDate(dec.getDateDec());
			fichDec.setTransaction("Décaissement");
			fichDec.setPiece(dec.getPiece());
			fichDec.setPrincipal(dec.getMontantDec());
			fichDec.setInteret(dec.getDemandeCredit().getInteret_total());
			fichDec.setPenalite(dec.getPocFee());
			fichDec.setSoldeTotal(dec.getMontantDec());
			retour.add(fichDec);
		}
		
		//calendrier
		if(listCal != null){
			for (Calapresdebl calapresdebl : listCal) {
				FicheCred fichCal = new FicheCred();
				fichCal.setDate(calapresdebl.getDate());
				fichCal.setTransaction("Tranche due");
				fichCal.setPiece("");
				fichCal.setPrincipal(calapresdebl.getMprinc());
				fichCal.setInteret(calapresdebl.getMint());
				fichCal.setPenalite(calapresdebl.getMpen());
				fichCal.setSoldeTotal(calapresdebl.getMprinc() + calapresdebl.getMint());
				retour.add(fichCal);
			}
		}
		
		//remboursement
		
		if(remb != null){
			for (Remboursement remboursement : remb) {
				FicheCred fichRem = new FicheCred();
				fichRem.setDate(remboursement.getDateRemb());
				fichRem.setTransaction("Remboursement");
				fichRem.setPiece(remboursement.getPiece());
				fichRem.setPrincipal(remboursement.getPrincipal());
				fichRem.setInteret(remboursement.getInteret());
				fichRem.setPenalite(remboursement.getStationery());
				fichRem.setSoldeTotal(dm.getSolde_total());
				retour.add(fichRem);
			}
		}
	
		
		return retour;
	}

	//Chercher crédit par clé primaire
	@Override
	public DemandeCredit getCreditById(String numCred) {		
		return em.find(DemandeCredit.class, numCred);
	}

	//Chercher demande crédit entre deux dates
	@Override
	public List<DemandeCredit> getCreditByDate(String dateDeb, String dateFin) {
		String sql = "select d from DemandeCredit d where d.dateDemande between '"+dateDeb+"' and '"+dateFin+"'";
		List<DemandeCredit> result = em.createQuery(sql,DemandeCredit.class).getResultList();
		return result;
	}

	//rapport decaissement en attente
	@Override
	public List<DemandeCredit> getDecaissementAttente(String dateDeb) {
		String sql = "select d from DemandeCredit d where d.approbationStatut ='Approuver' or d.approbationStatut = 'payé apres approbation'"
				+ " and d.dateDemande < '"+dateDeb+"'";
		TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}
		return null;
	}

	//rapport decaissement
	@Override
	public List<Decaissement> getDecaissement(String dateDeb,String dateFin) {
		String sql = "select d from Decaissement d ";
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql+=" where d.dateDec = '"+dateDeb+"'";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql+=" where d.dateDec between '"+dateDeb+"' and '"+dateFin+"'";
		}
		TypedQuery<Decaissement> query = em.createQuery(sql,Decaissement.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	/*********************************************************************************************************************************************/
	
	/*********************************************************************************************************************************************/


	/***
	 * HISTORIQUE DEMANDE
	 * ***/
	/*
	@Override
	public List<DemandeCredit> historiqueDemande() {
		TypedQuery<DemandeCredit> query = em.createQuery("SELECT c FROM DemandeCredit c", DemandeCredit.class);
		List<DemandeCredit> list = query.getResultList();
		return list;
	}
*/
	
}
