package mg.fidev.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mg.fidev.model.Calapresdebl;
import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.DemandeCredit;

public class CodeIncrement {
	
	///	Récupère le dernier index d'un client d'une même agence
	private static int getLastIndex(EntityManager em, String agence) {
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'"
				+ "and estClientIndividuel = true";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	private static int getLastIndexGar(EntityManager em, String agence) {
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'"
				+ "and estGarant = true ";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
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
	
	public static String getCodeGar(EntityManager em, String agence){
		int lastIndex = getLastIndexGar(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence + "/" + index;
	}
	
	///	Générer le code de client individuel
	public static String getCodeInd(EntityManager em, String agence){
		int lastIndex = getLastIndex(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence.substring(0, 2)+"/I/" + index;
	}
	
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
			double montDuJr = (int) (dmd.getMontantDemande() / confInd.getTranches());
			
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

			double mont_par_jour = (int) (dm.getMontantDemande() / confGroup.getTranche());

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
	*
	*/
	
}
