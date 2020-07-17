package mg.fidev.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import mg.fidev.model.Account;
import mg.fidev.model.CalView;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.DemandeCredit;
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
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'"
				+ "and estClientIndividuel = true";
		Query q = em.createNativeQuery(sql);
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
		String sql = "select count(*) from garant_credit where left(codeGarant, 2) = '"+agence+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	public static String getCodeGar(EntityManager em, String agence){
		int lastIndex = getLastIndexGar(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence.substring(0, 2) + "/Gar/" + index;
	}
	
	public static String getCodeBudget(EntityManager em){
		String sql = "select count(*) from budget";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		String index = String.format("%03d", ++result);
		return "Bud" + index;
	}
	
	public static String getCodeAnalytique(EntityManager em){
		String sql = "select count(*) from analytique";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		
		String index = String.format("%03d", ++result);
		return "An" + index;
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
	
	
	/**********************************************************************************************************************/
	
					/***********************CALCUL INTERET D'EPARGNE********************************/
	
	/**********************************************************************************************************************/

	/*****************************************************CALENDRIER DE REMBOURSEMENT****************************************************************/
	
	/*************************************************************************************************************************************************/
	
	/**
	 * Génère le calendrier de remboursement lors d'une demande de crédit
	 * quelconque
	 **/

	public static List<CalView> getCalendrierPaiement(String codeInd, String codeGrp,
			String date_dem, double montant, double tauxInt, int nbTranche,
			String typeTranche, int diffPaie, String modCalcul) {

		List<CalView> resultat = new ArrayList<CalView>();
		
		DemandeCredit dmd = new DemandeCredit();
		
		if(!codeInd.equals("")){
			Individuel ind = CreditServiceImpl.em.find(Individuel.class, codeInd);
			int lastIndex = CreditServiceImpl.getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = ind.getCodeInd().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setIndividuel(ind);		
			System.out.println("Demande credit pour client individuel");
		}
		if(!codeGrp.equals("")){
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
	
}
