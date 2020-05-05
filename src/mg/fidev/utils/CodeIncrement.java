package mg.fidev.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import mg.fidev.model.Account;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.DemandeCredit;

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
	
	/*public static void calculInteret(EntityManager em,ProduitEpargne prodE){
	
		
		double a = (300000*14/365 + 200000*5/365 + 100000*11/365) * 10/100;
		System.out.println(a);
		
		
		
		int nbjr = 365;
		int taux = 10;
		
		double soldes = 300000;
		String dateTr = "2019-10-01";
		double solde1 = 200000;
		String dateTr1 = "2019-10-15";
		double solde2 = 100000;
		String dateTr2 = "2019-10-20";
		
		String[][] tab = {{"2019-10-01","300000"},{"2019-10-15","200000"},{"2019-10-20","100000"}};
		
		int f = tab.length;

		@SuppressWarnings("static-access")
		DateTime dt = new DateTime().now();
		int maxDay = dt.dayOfMonth().getMaximumValue();
		int m = dt.getMonthOfYear();
		int a = dt.getYear();
		
		String datf = a+"-"+m+"-"+maxDay;
		LocalDate fn = LocalDate.parse(datf); 
		System.out.println(fn);
		double s = 0.0;
		int i = 0;
		int val1 = 0;
		while ( i <= tab.length) {
			
			//System.out.println(tab.length +"\n");
		
			LocalDate dtpars1 = LocalDate.parse(tab[i][0]);
			s = Double.parseDouble(tab[i][1]);
			
			System.out.println(dtpars1);
		
			val1 = Period.between(LocalDate.parse(tab[i][0]), LocalDate.parse(tab[i+1][0])).getDays();
			System.out.println(val1);

			if(i == tab.length-2){
				
			val1 = Period.between(LocalDate.parse(tab[tab.length-1][0]), fn).getDays();
			System.out.println(val1+1);
			double interet = ((s*val1/nbjr)*taux/100);
			System.out.println(s+"*"+val1+"/"+nbjr+"*"+taux+"/100 = "+interet);
			//System.out.println(interet);
			}
			 i++;
		}*/
		
		/*LocalDate dtrans = LocalDate.parse(dateTr);
		LocalDate dtrans1 = LocalDate.parse(dateTr1);
		LocalDate dtrans2 = LocalDate.parse(dateTr2);
		//LocalDate fn = factu.toDateTime().toLocalDate().toDate();
		 System.out.println(dtrans);
		 System.out.println(dtrans1);
		 System.out.println(dtrans2);
	 
		@SuppressWarnings("static-access")
		DateTime dt = new DateTime().now();
		int maxDay = dt.dayOfMonth().getMaximumValue();
		int m = dt.getMonthOfYear();
		int a = dt.getYear();
		
		String datf = a+"-"+m+"-"+maxDay;
		LocalDate fn = LocalDate.parse(datf);
		System.out.println(fn);
		 
		 int val = Period.between(dtrans2, fn).getDays()+1;
		 int val1 = Period.between(dtrans1, dtrans2).getDays();
		 int val2 = Period.between(dtrans, dtrans1).getDays();
		 System.out.println(val);
		 System.out.println(val1);
		 System.out.println(val2);
		 double interet = (((solde2*val/nbjr)+(solde1*val1/nbjr)+(solde*val2/nbjr))*taux/100);
		 System.out.println(interet);
		
	/*	MutableDateTime factu = new MutableDateTime();
		factu.dayOfMonth().set(factu.dayOfMonth().getMaximumValue());
		System.out.println(factu);
		
		MutableDateTime day = new MutableDateTime();
		day.setDayOfMonth(13);
		for (int i = 1; i <= 12; i++) {
		    day.setMonthOfYear(i);
		    if (day.getDayOfWeek() == DateTimeConstants.FRIDAY) {
		        String m = day.monthOfYear().getAsText(Locale.FRENCH);
		        System.out.println("Vendredi 13 " + m);
		    }
		}
		
		@SuppressWarnings("static-access")
		DateTime dt = new DateTime().now();
		int maxDay = dt.dayOfMonth().getMaximumValue();
		
		System.out.println(maxDay);
		
		
		
		LocalDate thisMonth = LocalDate.now();
		int dayMonht = thisMonth.getDayOfMonth();
		System.out.println(dayMonht);*/
		
		/*	
		//recupere le periode d'intérêt
		double periodeInt = prodE.getConfigInteretProdEp().getPeriodeInteret();
		//recupere la date de calcul d'intérêt
		String dateCalc = prodE.getConfigInteretProdEp().getDateCalcul();
		//recupere le taux d'intérêt 
		double tauxInt = prodE.getConfigInteretProdEp().getTauxInteret();
		//recupere le nombre de jours d'intérêt dans l'année 
		int nbJour = prodE.getConfigInteretProdEp().getNbrJrInt();
		//recupere le nombre de semaine dans l'année 
		int nbSem = prodE.getConfigInteretProdEp().getNbrSemaineInt();
		//Recupere le solde minimum d'intérêt du client individuel
		double soldeMinInd = prodE.getConfigInteretProdEp().getSoldeMinInd();
		
		String modeCalcul = prodE.getConfigInteretProdEp().getModeCalcul();
		
		LocalDate thisMonth = LocalDate.now();
		int dayMonht = thisMonth.getMonthValue();
		System.out.println(dayMonht);
		switch (modeCalcul) {
		case "Solde courant":
			
			
			
			break;

		default:
			break;
		}
		
		
		
	}
	
	*/
	/*****************************************************CALENDRIER DE REMBOURSEMENT****************************************************************/
	
	/*************************************************************************************************************************************************/
	
	/**
	 * Génère le calendrier de remboursement lors d'une demande de crédit
	 * quelconque
	 **/

	public static List<Calpaiementdue> getCalendrierPaiement(DemandeCredit dmd) {
		
		List<Calpaiementdue> calPaieDue = new ArrayList<Calpaiementdue>();
		
		if(dmd.getIndividuel() != null){
			
			ConfigCreditIndividuel confInd = dmd.getProduitCredit().getConfigCreditIndividuel();
			
			String typeTranche = confInd.getTypeTranche();
			
			//Interet dans 1 mois en pourcentage
			double intMens = confInd.getTauxInteretAnnuel() / 12;
			
			//Montant d'interet par mois
			double intTot = (dmd.getMontantDemande() * intMens) / 100;
			// double intDuJr = intTot / confInd.getTranches();
			//Interet principale
			double montDuJr = dmd.getMontantDemande() / confInd.getTranches();
			
			LocalDate dtDmd = LocalDate.now().plusDays(confInd.getDifferePaiement());
			
			switch (typeTranche) {
			case "Quotidiennement":
				//Montant d'Interet par Jours 
				double intDuJr = (intTot / confInd.getTranches());
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusDays(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
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
				double interetSemaine = intTot / confInd.getTranches();
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interetSemaine);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTot / confInd.getTranches();
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interet_quinz);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < confInd.getTranches(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusMonths(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) intTot);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			default:
				break;
			}

			
		}
		else if(dmd.getGroupe() != null){
			
			ConfigCreditGroup confGroupr = dmd.getProduitCredit().getConfigCreditGroupe();
			
			String typeTranche = confGroupr.getTypeTranche();
			
			//Interet dans 1 mois en pourcentage
			double intMens = confGroupr.getInteretAnnuel() / 12;
			
			//Montant d'interet par mois
			double intTot = (dmd.getMontantDemande() * intMens) / 100;
			// double intDuJr = intTot / confInd.getTranches();
			//Interet principale
			double montDuJr = (int) (dmd.getMontantDemande() / confGroupr.getTranche());
			
			LocalDate dtDmd = LocalDate.now().plusDays(confGroupr.getDiffPaiement());
			
			switch (typeTranche) {
			case "Quotidiennement":
				//Montant d'Interet par Jours 
				double intDuJr = (intTot / confGroupr.getTranche());
				for (int i = 0; i < confGroupr.getTranche(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusDays(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
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
				double interetSemaine = intTot / confGroupr.getTranche();
				for (int i = 0; i < confGroupr.getTranche(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interetSemaine);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			case "Quinzaine":
				double interet_quinz = intTot / confGroupr.getTranche();
				for (int i = 0; i < confGroupr.getTranche(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusWeeks(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) interet_quinz);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			case "Mensuellement":
				for (int i = 0; i < confGroupr.getTranche(); i++) {
					Calpaiementdue cal = new Calpaiementdue();
					dtDmd = dtDmd.plusMonths(1);
					if (dtDmd.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
						dtDmd = dtDmd.plusDays(2);
					} else if (dtDmd.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						dtDmd = dtDmd.plusDays(1);
					}
					cal.setDate(dtDmd.toString());
					cal.setMontantPrinc(montDuJr);
					cal.setMontantInt((int) intTot);
					cal.setDemandeCredit(dmd);
					calPaieDue.add(cal);
				}
				break;
			default:
				break;
			}
			
		}

		return calPaieDue;
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
	

	
	
	/**
	 * 
	 * 	
		for (CompteEpargne compteEpargne : listComp) {
			
			String numCpt = compteEpargne.getNumCompteEp();
			CompteEpargne cmpte = em.find(CompteEpargne.class,numCpt);
			
			switch (confInteret.getModeCalcul()) {
			case "Solde actuel":
				LocalDate dateFinMois = LocalDate.parse(datfin); 
				
				System.out.println(datdeb);
				System.out.println(dateFinMois+"\n");
				//recuperer tout transaction du mois en cours
				List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
				
				TypedQuery<TransactionEpargne> q = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
						+ "WHERE c.numCompteEp=:id AND"
						+ " t.dateTransaction BETWEEN :dateDeb AND :dateFin ORDER BY t.dateTransaction ASC",TransactionEpargne.class);
				q.setParameter("id", numCpt);
				q.setParameter("dateDeb", datdeb);
				q.setParameter("dateFin", datfin);
				if(!q.getResultList().isEmpty()){
					result = q.getResultList();
				}
			
				// intitialisation des variable utilisé, val = difference du date
				// dépuis le dernier transaction
				// somme = somme solde progressif de ce compte
				int val = 0;
				double somme = 0.0;
				double derSome = 0.0;

				for (int i = 0; i < result.size() - 1; i++) {
					val = Period.between(LocalDate.parse(result.get(i).getDateTransaction()),
									LocalDate.parse(result.get(i + 1).getDateTransaction())).getDays();

					System.out.println(val + "\n");

					somme += (result.get(i).getSolde() * val / produit.getConfigInteretProdEp().getNbrJrInt());

					System.out.println("somme=" + result.get(i).getSolde() + "*"+ 
					val + "/" + produit.getConfigInteretProdEp().getNbrJrInt());

					if (i == result.size() - 2) {
						int v = Period.between(	LocalDate.parse(result.get(result.size() - 1)
										.getDateTransaction()), dateFinMois).getDays();
						int b = v + 1;

						derSome = result.get(result.size() - 1).getSolde() * b/ produit.getConfigInteretProdEp().getNbrJrInt();

						System.out.println(v);
						System.out.println(b);
						System.out.println("derSome="+ result.get(result.size() - 1).getSolde() + "*"+ b + "/365 \n");
					}
				}

				double Interet = (somme + derSome) * produit.getConfigInteretProdEp().getTauxInteret() / 100;
				System.out.println(Interet);

				saveInt.setDate(datfin);
				saveInt.setMontant(Interet);
				saveInt.setCompte(compteEpargne);
				compteEpargne.setSolde(compteEpargne.getSolde() + Interet);

				try {
					transaction.begin();
					em.merge(compteEpargne);
					em.persist(saveInt);
					transaction.commit();
					System.out.println("Enregistrement d'intérêt du compte "+numCpt);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "Solde minimum mensuel":
				
				double interet = 0.0;
				
				TypedQuery<TransactionEpargne> query = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
						+ "WHERE c.numCompteEp=:id AND t.dateTransaction <= :dateDeb ",TransactionEpargne.class);
				query.setParameter("id", numCpt);
				query.setParameter("dateDeb", datdeb);
				
				if(!query.getResultList().isEmpty()){
					
					Query query1 = em.createQuery("SELECT MIN(tr.solde) FROM TransactionEpargne tr JOIN tr.compteEpargne cpe "
							+ "WHERE cpe.numCompteEp=:id AND tr.dateTransaction BETWEEN :dateDeb1 AND :dateFin1 ");
					query1.setParameter("id", numCpt);
					query1.setParameter("dateDeb1", datdeb);
					query1.setParameter("dateFin1", datfin);
					
					double soldMin = (double)query1.getSingleResult();
					
					interet = (soldMin*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
					
				}else{
					interet = 0.0;
					System.out.println("L'intérêt de ce compte pour mois ci est 0.0");
				}
				
				System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interet);

				saveInt.setDate(datfin);
				saveInt.setMontant(interet);
				saveInt.setCompte(cmpte);
				cmpte.setSolde(cmpte.getSolde() + interet);

				try {
					transaction.begin();
					em.flush();
					em.persist(saveInt);
					transaction.commit();
					System.out.println("Enregistrement d'intérêt du compte "+numCpt);
					TypedQuery<InteretEpargne> qrs = em.createQuery("SELECT i FROM InteretEpargne i",InteretEpargne.class);
					return retour = qrs.getResultList();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
				
			case "Solde moyen mensuel":
				
				//double interMensuel = 0.0;
				
				TypedQuery<TransactionEpargne> quer2 = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
						+ "WHERE c.numCompteEp=:id AND t.dateTransaction < :dateDeb ",TransactionEpargne.class);
				quer2.setParameter("id", numCpt);
				quer2.setParameter("dateDeb", datdeb);
				
				if(!quer2.getResultList().isEmpty()){
					
					TypedQuery<TransactionEpargne> query1 = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
							+ "WHERE c.numCompteEp=:id AND"
							+ " t.dateTransaction BETWEEN :dateDeb AND :dateFin "
							+ "AND t.dadateTransaction",TransactionEpargne.class);
					
					
					query1.setParameter("id", numCpt);
					query1.setParameter("dateDeb", datdeb);
					query1.setParameter("dateFin", datfin);
					
					//double soldMin = (double)query1.getSingleResult();
					
					//interet = (soldMin*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
					
				}else{
					interet = 0.0;
					System.out.println("L'intérêt de ce compte pour mois ci est 0.0");
				}
				
				//System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interet);

				saveInt.setDate(datfin);
				//saveInt.setMontant(interet);
				saveInt.setCompte(cmpte);
				//cmpte.setSolde(cmpte.getSolde() + interet);
				
				/((Solde au début du mois + Solde à la fin du mois) / 2) x Taux d’intérêt annuel / 100) / 
				  Nombre de mois dans l’année)

				try {
					transaction.begin();
					em.flush();
					em.persist(saveInt);
					transaction.commit();
					TypedQuery<InteretEpargne> qrs = em.createQuery("SELECT i FROM InteretEpargne i",InteretEpargne.class);
					return retour = qrs.getResultList();

				} catch (Exception e) {
					e.printStackTrace();
				}		
				
				break;
				
			case "Solde fin periode":
				
				Query quers = em.createQuery("SELECT t FROM TransactionEpargne t "
						+ "WHERE t.dateTransaction = (SELECT MAX(tr.dateTransaction) FROM TransactionEpargne tr "
						+ "JOIN tr.compteEpargne c WHERE c.numCompteEp=:id AND "
						+ "tr.dateTransaction BETWEEN :dateDeb AND :dateFin )");
				quers.setParameter("id", numCpt);
				quers.setParameter("dateDeb", datdeb);
				quers.setParameter("dateFin", datfin);
				
				TransactionEpargne te = (TransactionEpargne) quers.getSingleResult();
				
				double mont = te.getSolde();
				
				double interetFinPeriode = 
						((mont*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret())*dt.getMonthOfYear();
				
				System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interetFinPeriode);

				saveInt.setDate(datfin);
				saveInt.setMontant(interetFinPeriode);
				saveInt.setCompte(cmpte);
				cmpte.setSolde(cmpte.getSolde() + interetFinPeriode);

				try {
					transaction.begin();
					em.flush();
					em.persist(saveInt);
					transaction.commit();
					System.out.println("Enregistrement d'intérêt du compte "+numCpt);
					TypedQuery<InteretEpargne> qrs = em.createQuery("SELECT i FROM InteretEpargne i",InteretEpargne.class);
					return retour = qrs.getResultList();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
				
			case "Solde fin mois":
				
				Query quers2 = em.createQuery("SELECT t FROM TransactionEpargne t "
						+ "WHERE t.dateTransaction = (SELECT MAX(tr.dateTransaction) FROM TransactionEpargne tr "
						+ "JOIN tr.compteEpargne c WHERE c.numCompteEp=:id AND "
						+ "tr.dateTransaction BETWEEN :dateDeb AND :dateFin )");
				quers2.setParameter("id", numCpt);
				quers2.setParameter("dateDeb", datdeb);
				quers2.setParameter("dateFin", datfin);
				
				TransactionEpargne te2 = (TransactionEpargne) quers2.getSingleResult();
				
				double mont2 = te2.getSolde();
				
				double interetFinPeriode2 = (mont2*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
				
				System.out.println("l'intérêt du mois de "+datfin +" est de: "+ interetFinPeriode2);

				saveInt.setDate(datfin);
				saveInt.setMontant(interetFinPeriode2);
				saveInt.setCompte(cmpte);
				cmpte.setSolde(cmpte.getSolde() + interetFinPeriode2);

				try {
					transaction.begin();
					em.flush();
					em.persist(saveInt);
					transaction.commit();
					TypedQuery<InteretEpargne> qrs = em.createQuery("SELECT i FROM InteretEpargne i",InteretEpargne.class);
					return retour = qrs.getResultList();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
				
			case "Solde courant":
				
				break;
				
			default:
				break;
			}
			
		}
	 */
	
	
/*
	private int getLastIndexCommission() {
		String sql = "select count(*) from commission_credit";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
		int lastIndex = getLastIndexCommission();
		if (lastIndex != 0) {
			String index = String.format("%010d", ++lastIndex);
			paie.setTcode(index);
		} else {
			String index = String.format("0000000001");
			paie.setTcode(index);
		}
	}
	
	private int lastIdDecais() {
		String sql = "select count(*) from Decaissement";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
		int lastIndex = lastIdDecais();
		if (lastIndex != 0) {
			String index = String.format("%010d", ++lastIndex);
			decais.setTcode(index);
		} else {
			String index = String.format("0000000001");
			decais.setTcode(index);
		}
	}
	
				if(soldeTotal <=  0.0){
							String sql = "UPDATE calapresdebl SET payer="+1+""
			+ " WHERE (demande_credit.num_credit=calapresdebl.num_credit) AND date > "+date_remb+" AND num_credit = "+numCredit+"";
							
							Query q = em.createNativeQuery(sql);
							System.out.println("Mis à jour reussit!!! ");
//							Query qr = em.createQuery("UPDATE Calapresdebl a SET payer= :t JOIN a.demandeCredit nums"
//									+ " WHERE a.date > :dateR AND nums.numCredit = :numC");
//							qr.setParameter("t", true);
//							qr.setParameter("dateR", date_remb);
//							qr.setParameter("numC", "" + numCredit + "");
						}
				
		Calcul remboursement
		
						else if(val == 0){
				System.out.println("Remboursement normal");
				// Selection de principale due et intérêt due

				Query query = em.createQuery("SELECT cl.mprinc,cl.mint FROM Calapresdebl cl JOIN cl.demandeCredit num"
								+ " WHERE cl.date = :dateRemb AND num.numCredit = :numCred");
				// query.setParameter("x", false);cl.payer = :x AND
				query.setParameter("dateRemb", dateCal);
				query.setParameter("numCred", "" + numCredit + "");

				String princ = "";
				String inter = "";
				double motantTotal = 0.0;

				// Recupère le montant principal et interet à rembouser

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
					result.add("0");
					result.add("0");
					result.add("0");
					result.add("0");
					result.add(inter);
					result.add(princ);
					result.add("0");
					result.add("0");
					result.add(String.valueOf(motantTotal));
				}
						
	*
	*/
	// int days = Days.daysBetween(date2.getTime(),
	// date1.getTime());
}
