package mg.fidev.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Account;
import mg.fidev.model.CalView;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.CalendrierPepView;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.JourFerier;
import mg.fidev.service.impl.CreditServiceImpl;
import mg.fidev.service.impl.UserServiceImpl;

public class CodeIncrement {
	
	/****
	 * GENERE CODE CLIENT INDIVIDUEL
	 * ***/
	///	Récupère le dernier index d'un client d'une même agence
	private static int getLastIndex(EntityManager em, String agence) {
		String sql = "select count(i) from Individuel i join i.agence a where a.codeAgence = '"+agence+"'"
				+ " and i.estClientIndividuel = 'true'";
		Query q = em.createQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	} 
	
	///	Générer le code de client individuel
	public static String getCodeInd(EntityManager em, String agence){
		int lastIndex = getLastIndex(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence.substring(0, 2)+"/I/" + index;
	}
	/***************************************************************************/
	
	/***
	 * GENERER CODE GARANT
	 * ***/
	private static int getLastIndexGar(EntityManager em, String agence) {
		/*String sql = "select count(*) from garant_credit where left(codeGarant, 2) = '"+agence+"'";
		Query q = em.createNativeQuery(sql);*/
		String sql = "select count(g) from Garant g join g.agence a where a.codeAgence = '"+agence+"'";
		Query q = em.createQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	//Generate code garant
	public static String getCodeGar(EntityManager em, String agence){
		int lastIndex = getLastIndexGar(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence.substring(0, 2) + "/Gar/" + index;
	}
	
	//Generate code budgét
	public static String getCodeBudget(EntityManager em){
		String sql = "select count(*) from budget";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		String index = String.format("%03d", ++result);
		return "Bud" + index;
	}
	
	//Generate code analytique
	public static String getCodeAnalytique(EntityManager em){
		String sql = "select count(*) from analytique";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		String index = String.format("%03d", ++result);
		return "An" + index;
	}
	
	//Generate code auxilliaire
	public static String getCodeAuxilliaire(EntityManager em){
		String sql = "select count(*) from compteAuxilliaire";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		String index = String.format("%03d", ++result);
		return "Aux" + index;
	}
	
	/***************************************************************************/
	
	/***
	 * GENERER CODE GROUPE
	 * ***/
	
	private static int getLastIndexGrp(EntityManager em, String agence) {
		String sql = "select count(*) from groupe where left(codeGrp, 2) = '"+agence+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
		}

	public static String getCodeGrp(EntityManager em, String agence){
		int lastIndex = getLastIndexGrp(em, agence);
		if(lastIndex != 0){
			String index = String.format("%06d", ++lastIndex);
			return agence+"/G/" + index;
		}else{
			return agence+"/G/" + "000001";
		}
		
	}
	
	/****************************************************************************/
	
	/***
	 * GENERER CODE DU PERSONNEL
	 * ***/
	
	public static String getCodePers(EntityManager em, String agence){
		
		String sql = "select count(*) from personnel_institution where left(CODEPERSONNEL, 2) "
				+ "= '"+agence+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		if(result != 0){
			String index = String.format("%06d", ++result);
			return agence.substring(0, 2)+"/Pers/" + index;
		}else{
			return agence.substring(0, 2)+"/Pers/" + "000001";
		}
	}
	
	/*****************************************************************************/
	
	/***
	 * GENERER TCODE GRAND LIVRE
	 * ***/
	
	public static String genTcode(EntityManager em) {
		String sql = "select MAX(tcode) from grandlivre";
		Query q = em.createNativeQuery(sql);
		if((q.getSingleResult()) == null)
			return "00000001";
		else{
			int result = Integer.parseInt(q.getSingleResult().toString());
			String index = String.format("%08d", ++result);
			return index;
		}
	}
	
	
	/*************************INCREMENTATION NOMBRE DE CREDIT D'UNE CLIENT****************************/
	
	public static int nombreCreditInd(EntityManager em,String codeClient){
		int nb = 1;
		try {
			Query q = em.createQuery("SELECT COUNT(d) FROM DemandeCredit d JOIN d.individuel ind WHERE "
					+ " ind.codeInd= :x");
			q.setParameter("x", codeClient);
		
			if (q.getSingleResult() != null) {
				int credit = Integer.parseInt(q.getSingleResult().toString());
				nb = credit + nb;
			}
			
		} catch (NoResultException e) {
			e.printStackTrace();
		}			
		
		return nb;
	}
	
	public static int nombreCreditGrp(EntityManager em,String codeClient){
		int nb = 1;
		try {
			Query q = em.createQuery("SELECT COUNT(d) FROM DemandeCredit d JOIN d.groupe grp WHERE "
					+ " grp.codeGrp= :x");
			q.setParameter("x", codeClient);
		
			if (q.getSingleResult() != null) {
				int credit = Integer.parseInt(q.getSingleResult().toString());
				nb = credit + nb;
			}
			
		} catch (NoResultException e) {
			e.printStackTrace();
		}			
		
		return nb;
	}  
	
	//Chercher compte comptable dans la table account
	 public static Account getAcount(EntityManager em,String numCompte){
		 Query query = em.createQuery("select a from Account a where a.numCpt = :x");
		 query.setParameter("x", numCompte);
		 
		 if(query.getSingleResult() != null){		 
			 Account acount =(Account) query.getSingleResult();
			 return acount;
		 }
		 
		 return null;
	 }     
	
	 
	 //Chercher Grand livre par code transaction
	 public static List<Grandlivre> getGrandLivreByCodeTrans(EntityManager em, String tcode){
		 
		 String sql = "select g from Grandlivre g where g.tcode = '"+tcode+"'";
		 TypedQuery<Grandlivre> q = em.createQuery(sql, Grandlivre.class);
		 
		 if(!q.getResultList().isEmpty())
			 return q.getResultList();		 
		 return null;
	 }
	 
	 public static Grandlivre getDebitGL(EntityManager em, String tcode){
		 String sql = "select g from Grandlivre g where g.tcode = '"+tcode+"' and g.debit > 0 ";
		 Grandlivre q = (Grandlivre) em.createQuery(sql).getSingleResult();
		 return q;
	 }
	 
	 public static Grandlivre getCreditGL(EntityManager em, String tcode){
		 String sql = "select g from Grandlivre g where g.tcode = '"+tcode+"' and g.credit > 0 ";
		 Grandlivre q = (Grandlivre) em.createQuery(sql).getSingleResult();
		 return q;
	 }
	
	/**********************************************************************************************************************/
	
					/***********************CALCUL INTERET D'EPARGNE********************************/
	
	/**********************************************************************************************************************/

	/*****************************************************CALENDRIER DE REMBOURSEMENT****************************************************************/
	
	/*************************************************************************************************************************************************/
	
	/**
	 * Génère le calendrier de remboursement lors d'une demande de crédit
	 * quelconque
	 **/

	public static List<CalView> getCalendrierPaiement(String codeCred, String codeInd, String codeGrp,
			String date_dem, double montant, double tauxInt, int nbTranche,
			String typeTranche, int diffPaie, String modCalcul) {

		List<CalView> resultat = new ArrayList<CalView>();
		
		DemandeCredit dmd = null;
		
		if(!codeCred.equals("")){  
		    dmd = CreditServiceImpl.em.find(DemandeCredit.class, codeCred);
		}
		
		if(!codeInd.equals("")){
			dmd = new DemandeCredit();
			Individuel ind = CreditServiceImpl.em.find(Individuel.class, codeInd);
			int lastIndex = CreditServiceImpl.getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = ind.getCodeInd().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setIndividuel(ind);		
			System.out.println("Demande credit pour client individuel");
		}
		if(!codeGrp.equals("")){
			dmd = new DemandeCredit();
			Groupe grp =  CreditServiceImpl.em.find(Groupe.class, codeGrp);
			int lastIndex = CreditServiceImpl.getLastIndex(codeGrp);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = grp.getCodeGrp().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setGroupe(grp); 	
			System.out.println("Demande credit pour client groupe");
		}
			
			//Interet dans 1 mois en pourcentage
			double intMens = tauxInt / 12;
			
			//Montant d'interet par mois
			double intTot = (montant * intMens) / 100;
 
			//Interet principale
			double montDuJr =  montant / nbTranche;//(int)
			
			System.out.println(montDuJr);
			
			LocalDate dtDmd = LocalDate.parse(date_dem).plusDays(diffPaie);
			
			List<JourFerier> jours = UserServiceImpl.listJoursFerier();
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			switch (typeTranche) {
			case "Quotidiennement":
				//Montant d'Interet par Jours 
				double intDuJr = (intTot / nbTranche);
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView(); 
					dtDmd = dtDmd.plusDays(1);
					
					for (JourFerier jourFerier : jours) {
						if(dtDmd.toString().equals(jourFerier.getDate())){
							dtDmd = dtDmd.plusDays(1);
						}
					}
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
					
				}
				break;
			case "Hebdomadairement":
				double interetSemaine = intTot / 4;
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusWeeks(1);
					
					for (JourFerier jourFerier : jours) {
						if(dtDmd.toString().equals(jourFerier.getDate())){
							dtDmd = dtDmd.plusDays(1);
						}
					}
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
					
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTot / 2;
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusWeeks(2);
					
					for (JourFerier jourFerier : jours) {
						if(dtDmd.toString().equals(jourFerier.getDate())){
							dtDmd = dtDmd.plusDays(1);
						}
					}
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
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < nbTranche; i++) {
					CalView cal = new CalView();
					dtDmd = dtDmd.plusMonths(1);
					
					for (JourFerier jourFerier : jours) {
						if(dtDmd.toString().equals(jourFerier.getDate())){
							dtDmd = dtDmd.plusDays(1);
						}
					}
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					/*try {
						cal.setDat(simpleDateFormat.parse(dtDmd.toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}*/
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) intTot);
					cal.setNumCred(dmd.getNumCredit());
					resultat.add(cal);
				}
				break;
			default:
				break;
			}		
		return resultat;
	}

	/**
	 * CALENDRIER DE REMBOURSEMENT APRES DEBLOCAGE
	 * **/

	public static List<Calapresdebl> getCalendrierRemboursement(DemandeCredit dm) {
		List<Calapresdebl> listcal = new ArrayList<Calapresdebl>();
		// DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		
		if(dm.getIndividuel() != null){
			
			ConfigCreditIndividuel confInd = dm.getProduitCredit()
					.getConfigCreditIndividuel();
			String typeTranche = confInd.getTypeTranche();
			double interet_Mois = confInd.getTauxInteretAnnuel() / 12;
			double intTotal = (dm.getMontantDemande() * interet_Mois) / 100;

			double mont_par_jour = (int) (dm.getMontantDemande() / confInd.getTranches());

			LocalDate dtDmd = LocalDate.now()
					.plusDays(confInd.getDifferePaiement());

		switch (typeTranche) {
			case "Quotidiennement":
				double int_par_jour = (intTotal / confInd.getTranches());
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusDays(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) int_par_jour);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Hebdomadairement":
				double interetSemaine = intTotal / confInd.getTranches();
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) interetSemaine);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTotal / confInd.getTranches();
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusWeeks(2);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) interet_quinz);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusMonths(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) intTotal);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			default:
				break;
			}
		
			
		}else if(dm.getGroupe() != null){
			
			ConfigCreditGroup confGroup = dm.getProduitCredit().getConfigCreditGroupe();
			
			String typeTranche = confGroup.getTypeTranche();
			double interet_Mois = confGroup.getInteretAnnuel() / 12;
			double intTotal = (dm.getMontantDemande() * interet_Mois) / 100;

			double mont_par_jour = dm.getMontantDemande() / confGroup.getTranche();

			LocalDate dtDmd = LocalDate.now()
					.plusDays(confGroup.getDiffPaiement());

		switch (typeTranche) {
			case "Quotidiennement":
				double int_par_jour = (intTotal / confGroup.getTranche());
				for (int i = 0; i < confGroup.getTranche(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusDays(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) int_par_jour);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Hebdomadairement":
				double interetSemaine = intTotal / confGroup.getTranche();
				for (int i = 0; i < confGroup.getTranche(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) interetSemaine);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTotal / confGroup.getTranche();
				for (int i = 0; i < confGroup.getTranche(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusWeeks(2);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) interet_quinz);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < confGroup.getTranche(); i++) {
					Calapresdebl cal = new Calapresdebl();
					dtDmd = dtDmd.plusMonths(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMprinc(mont_par_jour);
					cal.setMint((int) intTotal);
					cal.setDemandeCredit(dm);
					listcal.add(cal);
				}
				break;
			default:
				break;
			}	
		}
	
		return listcal;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/*********************************** CALENDRIER PREVU PLAN EPARGNE **************************************/
	
	public static List<CalendrierPepView> getCalPrevuPep(String numCompte, int duree, String frequence, double montant, String date, double taux){
		
		List<CalendrierPepView> result = new ArrayList<CalendrierPepView>();
		
		LocalDate dtm = LocalDate.parse(date);
		List<JourFerier> jours = UserServiceImpl.listJoursFerier();
		int tmp = duree;
		switch (frequence) {
		case "Quotidiennement":
			for (int i = 1; duree >= i; duree--) {
				
				if(duree != tmp)
					dtm = dtm.plusDays(1);
				
				for (JourFerier jourFerier : jours) {
					if(dtm.toString().equals(jourFerier.getDate())){
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}				
				double interet = ((montant * taux)/100)*duree;				
				CalendrierPepView c = new CalendrierPepView(dtm.toString(), montant, duree, interet, numCompte);				
				result.add(c);
				
			}
			break;
		case "Hebdomadairement":
			for (int i = 1; duree >= i; duree--) {
				if(duree != tmp)
					dtm = dtm.plusWeeks(1);
					
				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				double interet = ((montant * taux) / 100) * duree;
				CalendrierPepView c = new CalendrierPepView(dtm.toString(),
						montant, duree, interet, numCompte);
				result.add(c);

			}
			break;
		case "Quinzaine":
			
			for (int i = 1; duree >= i; duree--) {

				if(duree != tmp)
					dtm = dtm.plusWeeks(2);
									
				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				double interet = ((montant * taux) / 100) * duree;
				CalendrierPepView c = new CalendrierPepView(dtm.toString(),
						montant, duree, interet, numCompte);
				result.add(c);

			}
			
			break;
		case "Mensuellement":
			
			for (int i = 1; duree >= i; duree--) {

				if(duree != tmp)
					dtm = dtm.plusMonths(1);
					
				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				double interet = ((montant * taux) / 100) * duree;
				CalendrierPepView c = new CalendrierPepView(dtm.toString(),
						montant, duree, interet, numCompte);
				result.add(c);

			}
			
			break;
		case "DeuxMois":
			
			for (int i = 1; duree >= i; duree--) {

				if(duree != tmp)
					dtm = dtm.plusMonths(2);				
				
				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				double interet = ((montant * taux) / 100) * duree;
				CalendrierPepView c = new CalendrierPepView(dtm.toString(),
						montant, duree, interet, numCompte);
				result.add(c);

			}
					
			break;
		case "Trimestriel":
			
			for (int i = 1; duree >= i; duree--) {

				if(duree != tmp)
					dtm = dtm.plusMonths(3);
					
				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				double interet = ((montant * taux) / 100) * duree;
				CalendrierPepView c = new CalendrierPepView(dtm.toString(),
						montant, duree, interet, numCompte);
				result.add(c);

			}
			
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public static String getDateFinDepot(int duree, String frequence, String date){
		String result = "";
		
		LocalDate dtm = LocalDate.parse(date);
		List<JourFerier> jours = UserServiceImpl.listJoursFerier();
		switch (frequence) {
		case "Quotidiennement":
			dtm = dtm.plusDays(duree + 1);
			
			for (JourFerier jourFerier : jours) {
				if(dtm.toString().equals(jourFerier.getDate())){
					dtm = dtm.plusDays(1);
				}
			}
			if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				dtm = dtm.plusDays(2);
			} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dtm = dtm.plusDays(1);
			}	
			result = dtm.toString();
			break;
		case "Hebdomadairement":
			
			dtm = dtm.plusWeeks(duree - 1);

			for (JourFerier jourFerier : jours) {
				if (dtm.toString().equals(jourFerier.getDate())) {
					dtm = dtm.plusDays(1);
				}
			}
			if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				dtm = dtm.plusDays(2);
			} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dtm = dtm.plusDays(1);
			}
			result = dtm.toString();
			break;
		case "Quinzaine":
			
				dtm = dtm.plusWeeks((duree -1 )*2);

				for (JourFerier jourFerier : jours) {
					if (dtm.toString().equals(jourFerier.getDate())) {
						dtm = dtm.plusDays(1);
					}
				}
				if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
					dtm = dtm.plusDays(2);
				} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					dtm = dtm.plusDays(1);
				}
				result = dtm.toString();
			
			break;
		case "Mensuellement":
			
			dtm = dtm.plusMonths((duree -1 ));

			for (JourFerier jourFerier : jours) {
				if (dtm.toString().equals(jourFerier.getDate())) {
					dtm = dtm.plusDays(1);
				}
			}
			if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				dtm = dtm.plusDays(2);
			} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dtm = dtm.plusDays(1);
			}
			result = dtm.toString();		
			
			break;
		case "DeuxMois":
			
			dtm = dtm.plusMonths((duree -1 ) * 2);

			for (JourFerier jourFerier : jours) {
				if (dtm.toString().equals(jourFerier.getDate())) {
					dtm = dtm.plusDays(1);
				}
			}
			if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				dtm = dtm.plusDays(2);
			} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dtm = dtm.plusDays(1);
			}
			result = dtm.toString();
					
			break;
		case "Trimestriel":
			
			dtm = dtm.plusMonths((duree - 1 ) * 3);

			for (JourFerier jourFerier : jours) {
				if (dtm.toString().equals(jourFerier.getDate())) {
					dtm = dtm.plusDays(1);
				}
			}
			if (dtm.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				dtm = dtm.plusDays(2);
			} else if (dtm.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dtm = dtm.plusDays(1);
			}
			result = dtm.toString();
			
			break;
		default:
			break;
		}
		
		return result;
	}
	
}
