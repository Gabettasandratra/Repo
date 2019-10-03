package mg.fidev.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import mg.fidev.model.Calapresdebl;
import mg.fidev.model.CommissionCredit;
import mg.fidev.model.CompteCaisse;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigFraisCredit;
import mg.fidev.model.ConfigFraisCreditGroupe;
import mg.fidev.model.ConfigGlCredit;
import mg.fidev.model.Decaissement;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.Remboursement;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.DemandeCreditService;
import mg.fidev.utils.CodeIncrement;

@WebService(name = "LoanService", targetNamespace = "http://demandeCrediService.com", serviceName = "LoanService", portName = "demandeServicePort", endpointInterface = "mg.fidev.service.DemandeCreditService")
public class DemandeCreditImpl implements DemandeCreditService {

	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	

	/*******************************************************************************************************************************************/
									/******************** DEMANDE CREDIT *********************************/
	/*******************************************************************************************************************************************/

	
	/**
	 * Methode pour recuperer le dernier index d'un num crédit
	 **/

	static int getLastIndex( String codeInd) {
		String code = codeInd.substring(0, 2);
		String sql = "select count(*) from demande_credit where left(num_credit, 2) = '"+code+"'";// String num_crd
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	/**
	 *	DEMANDE CREDIT
	 **/
	
	@Override
	public void demandeCredit(String date, double montant, String but,String agentName, String idProduit, String codeInd, String codeGrp,
			int userId) {

		DemandeCredit dmd = new DemandeCredit();

		Utilisateur user = em.find(Utilisateur.class, userId);
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		
		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGrp);
		
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

		
		
		/* Verification du client s'il est individuel ou groupe */
		/*if (ind != null) {
		
		} else if (grp != null) {
			String ag = grp.getCodeGrp().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setGroupe(grp);
		}
		*/
		
		if(ind != null){
			
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
			

			if(montant < montMinAutoriser || montant > montMaxAutoriser){
				System.out.println("Le montant doit compris entre "+montMinAutoriser+" et "+montMaxAutoriser);
			}else{

				dmd.setUtilisateur(user);
				dmd.setProduitCredit(pdtCredit);

				dmd.setDateDemande(date);
				dmd.setMontantDemande(montant);
				dmd.setAgentName(agentName);
				dmd.setButCredit(but);
				dmd.setApprobationStatut("Approbation en attente");
				dmd.setCalpaiementdues(CodeIncrement.getCalendrierPaiement(dmd));
				
				System.out.println("information demande crédit prête");
				try {
					transaction.begin();
					em.persist(dmd);
					transaction.commit();
					em.refresh(dmd);
					System.out.println("Demande crédit enregistrée");
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			}
		
			
		}
		else if(grp != null){
			
			int lastIndex = getLastIndex(codeGrp);
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

				dmd.setDateDemande(date);
				dmd.setMontantDemande(montant);
				dmd.setAgentName(agentName);
				dmd.setButCredit(but);
				dmd.setApprobationStatut("Approbation en attente");
				dmd.setCalpaiementdues(CodeIncrement.getCalendrierPaiement(dmd));
				
				System.out.println("information demande crédit prête");
				try {
					transaction.begin();
					em.persist(dmd);
					transaction.commit();
					em.refresh(dmd);
					System.out.println("Demande crédit enregistrée");
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			}
			
		}
		
	}


	/**
	 * Modification demande de crédit
	 * **/
	@Override
	public String updateDemandeCredit(String numCredit, DemandeCredit dmd,String idProduit, int userId) {

		DemandeCredit dem = em.find(DemandeCredit.class, numCredit);
		ProduitCredit pt = em.find(ProduitCredit.class, idProduit);
		Utilisateur user = em.find(Utilisateur.class, userId);

		String result = "";

		if (dem == null) {
			result = "Nummero crédit incorrecte!!!";
		} else {

			if (dem.getApprobationStatut().equalsIgnoreCase(
					"Approbation en attente")) {

				dem.setUtilisateur(user);
				dem.setProduitCredit(pt);
				dem.setMontantDemande(dmd.getMontantDemande());
				dem.setDateDemande(dmd.getDateDemande());
				dem.setButCredit(dmd.getButCredit());
				dem.setAgentName(dmd.getAgentName());

				System.out.println("Modification demande crédit");
				try {
					transaction.begin();
					em.flush();
					transaction.commit();
					System.out.println("Modification réussie");
					result = "Modification réussie!!!";
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			} else {
				result = "Desolé, On ne peut pas modifier cet Demande Crédit!!!";
			}
		}
		return result;
	}
	

	/************************************************************************************************************************************/
							/************************* COMMISSION CREDIT *************************************/
	/************************************************************************************************************************************/

	/**
	 * Paiement commission crédit
	 * **/

	@Override
	public String paiementCommission(CommissionCredit comm, String numCredit,int userId,
			String nomCptCaisse) {
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		CommissionCredit paie = new CommissionCredit();
		
		/***
		 * CHARGE LES CONFIGURATIONS FRAIS DE CREDIT
		 * ****/
		ConfigFraisCredit confFrais = dm.getProduitCredit().getConfigFraisCredit();
		ConfigFraisCreditGroupe confGroupe = dm.getProduitCredit().getConfFraisGroupe();
		
		String result = "";
		
		
		if(confFrais.getFraisDemandeOuDecais() == true || confGroupe.getFraisDemandeOuDecais() == true){
				
				/***
				 * CHARGE LES CONFIGURATIONS GL DU CREDIT
				 * ***/
				
				ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
				
				/***
				 * 	pour incrémenter le tcode dans la table grandlivre
				 * ***/
				
				String indexTcode = CodeIncrement.genTcode(em);
				
				CompteCaisse cptCaisse = em.find(CompteCaisse.class, nomCptCaisse);
				
				//Commission Crédit
				Grandlivre debitCom= new Grandlivre();
				//Commission
				Grandlivre creditCom = new Grandlivre();
				//Papeterie
				Grandlivre creditPap = new Grandlivre();
				
				//Total debité
				double debitTot = 0.0;
				double commissions = 0.0;
				double papeteries = 0.0;
				
				if(dm.getIndividuel() != null && confFrais.isIndOuGroupe() == true){
					debitTot = confFrais.getCommission() + confFrais.getPapeterie();
					commissions = confFrais.getCommission();
					papeteries = confFrais.getPapeterie();
				}
				else if(dm.getGroupe() != null){
					debitTot = confGroupe.getCommission() + confGroupe.getPapeterie();
					commissions = confGroupe.getCommission();
					papeteries = confGroupe.getPapeterie();
				}
				
				System.out.println("Paiement commission");

				if (dm.getApprobationStatut().equalsIgnoreCase("payé apres approbation")
						|| dm.getApprobationStatut().equalsIgnoreCase("payé avant approbation")
						|| dm.getApprobationStatut().equalsIgnoreCase("Demande decaissé")
						|| dm.getApprobationStatut().equalsIgnoreCase("Credit remboursé")) {
					
					result = "le Paiement de commission de cet Crédit est déjà fait";
					
				} else {

					try {

						if (dm.getApprobationStatut().equalsIgnoreCase("Approbation en attente")) {
							paie.setStatut_comm("Paiement de commission avant approbation");
							dm.setApprobationStatut("payé avant approbation");
						} else if (dm.getApprobationStatut().equalsIgnoreCase("Approuvé")) {
							paie.setStatut_comm("Paiement de commission apres approbation");
							dm.setApprobationStatut("payé apres approbation");
						}
						
						//Insertion au GrandLivre
						String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
						
						
						//Solde Debité
						debitCom.setTcode(indexTcode);
						debitCom.setDate(comm.getDatePaie());
						debitCom.setDescr("Commision crédit "+numCredit);
						debitCom.setPiece(comm.getPiece());
						debitCom.setUserId(ut.getNomUtilisateur());
						debitCom.setCompte(cptC);
						debitCom.setDebit(debitTot);
						debitCom.setCodeInd(dm.getIndividuel());
										
						//Solde Crédité Commission
						creditCom.setTcode(indexTcode);
						creditCom.setDate(comm.getDatePaie());
						creditCom.setDescr("Frais Commision crédit "+numCredit);
						creditCom.setPiece(comm.getPiece());
						creditCom.setUserId(ut.getNomUtilisateur());
						creditCom.setCompte(confGl.getCptCommCredit());
						creditCom.setCredit(commissions);
						creditCom.setCodeInd(dm.getIndividuel());
						
						//Solde Crédité Papeterie
						creditPap.setTcode(indexTcode);
						creditPap.setDate(comm.getDatePaie());
						creditPap.setDescr("Frais Papéterie crédit "+numCredit);
						creditPap.setPiece(comm.getPiece());
						creditPap.setUserId(ut.getNomUtilisateur());
						creditPap.setCompte(confGl.getCptPapeterie());
						creditPap.setCredit(papeteries);
						creditPap.setCodeInd(dm.getIndividuel());
						
						
						//Information des Commission Crédit
						paie.setTcode(indexTcode);
						paie.setDemandeCredit(dm);
						paie.setUtilisateur(ut);
						paie.setCash(comm.getCash());
						paie.setCheqid(comm.getCheqid());
						paie.setDatePaie(comm.getDatePaie());
						paie.setLcomm(commissions);
						paie.setPiece(comm.getPiece());
						paie.setStationery(papeteries);
						paie.setTdf(comm.getTdf());
						paie.setTotvat(comm.getTotvat());

						transaction.begin();
						em.persist(paie);
						em.persist(debitCom);
						em.persist(creditCom);
						em.persist(creditPap);
						em.merge(dm);
						transaction.commit();
						// transaction.commit();
						em.refresh(paie);
						System.out.println("Paiement de commission effectué");
						result = "Paiement de commission effectué";
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}				
				}			
		}
		return result;

	}
	
	
	/************************************************************************************************************************************/
						/********************* APPROBATION CREDIT *******************************/
	/************************************************************************************************************************************/

	/***
	 * Approbation,Rejet, Differer de demande crédit
	 * */

	@Override
	public String approbationCredit(String numCredit, String Appby,	String dateApp, String descApp, double montantApp,
			String stat_demande) {
		
		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		String result = "";

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
					} catch (Exception e) {
						System.err.println("Erreur approbation "
								+ e.getMessage());
						result = "Erreur approbation";
					}
				} else {
					System.err.println("Montant approuvé null");
					result = "Montant approuvé incorrecte!!!";
				}
			
			
			
			} else {
				result = "Crédit déjà approuvé!!!";
			}

		} else {
			result = "Numéro Crédit non trouvé!!!";
		}
		return result;

	}
	
	
	/************************************************************************************************************************************/
							/************************ DECAISSEMENT ********************************/
	/************************************************************************************************************************************/

	/**
	 * Decaisement demande Crédit
	 * **/

	@SuppressWarnings("unused")
	@Override
	public String decaisser(Decaissement decaissement, String numCredit,int userId) {
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		Decaissement decais = new Decaissement();
		ConfigCreditIndividuel confInds = dm.getProduitCredit().getConfigCreditIndividuel();
		ConfigGlCredit confGL = dm.getProduitCredit().getConfigGlCredit();
		
		///	pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		List<Calapresdebl> calRembAprDebl = CodeIncrement.getCalendrierRemboursement(dm);
		
		CompteCaisse cptCaisse = em.find(CompteCaisse.class, decaissement.getCptCaisseNum());
		
		//Solde Demander
		Grandlivre debit = new Grandlivre();
		//Solde Decaissé
		Grandlivre credit1= new Grandlivre();
		//Papeterie
		Grandlivre credit2 = new Grandlivre();
		//Commission
		Grandlivre credit3 = new Grandlivre();

		String result = "";

		if (dm != null) {
			if (dm.getIndividuel() != null && (dm.getApprobationStatut().equalsIgnoreCase("Approuvé") || dm.getApprobationStatut().equalsIgnoreCase(
							"payé apres approbation"))) {

				try {
					
					double soldeDecais = (dm.getMontantApproved()-decaissement.getCommission()-decaissement.getStationnary());
					
					
					//Interet Total 
					//Interet dans 1 mois en pourcentage
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
					String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
					
					//decais.setTcode(decaissement.getTcode());

					decaissement.setTcode(indexTcode);
					decaissement.setCptCaisseNum(cptC);
					decaissement.setUtilisateur(ut);
					decaissement.setDemandeCredit(dm);
					dm.setApprobationStatut("Demande decaissé");
					
					//Solde Debité
					debit.setTcode(indexTcode);
					debit.setDate(decaissement.getDateDec());
					debit.setDescr("Decaissement crédit " +numCredit);
					debit.setPiece(decaissement.getPiece());
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(confGL.getCptPrincEnCoursInd());
					debit.setDebit(dm.getMontantApproved());
					debit.setCodeInd(dm.getIndividuel());
					
					//Solde Crédité 1 (Solde Decaissé)
					credit1.setTcode(indexTcode);
					credit1.setDate(decaissement.getDateDec());
					credit1.setDescr("Decaissement crédit "+numCredit);
					credit1.setPiece(decaissement.getPiece());
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(cptC);
					credit1.setCredit(soldeDecais);
					credit1.setCodeInd(dm.getIndividuel());
					
					transaction.begin();
					//Solde Crédité 2 (Papeterie Decaissement)
					if(decaissement.getStationnary() != 0.0){
						
						credit2.setTcode(indexTcode);
						credit2.setDate(decaissement.getDateDec());
						credit2.setDescr("Papeterie au decaissement de crédit "+numCredit);
						credit2.setPiece(decaissement.getPiece());
						credit2.setUserId(ut.getNomUtilisateur());
						credit2.setCompte(confGL.getPapeterieDec());
						credit2.setCredit(decaissement.getStationnary());	
						credit2.setCodeInd(dm.getIndividuel());
						
						em.persist(credit2);
					
					}
					
					//Solde Crédité 3 (Commission au Decaissement)
					if(decaissement.getCommission() != 0.0){
						
						credit3.setTcode(indexTcode);
						credit3.setDate(decaissement.getDateDec());
						credit3.setDescr("Commission au decaissement de crédit "+numCredit);
						credit3.setPiece(decaissement.getPiece());
						credit3.setUserId(ut.getNomUtilisateur());
						credit3.setCompte(confGL.getCommDec());
						credit3.setCredit(decaissement.getCommission());
						credit3.setCodeInd(dm.getIndividuel());
						
						em.persist(credit3);
					}
					
					em.persist(debit);
					em.persist(credit1);		
					em.merge(dm);
					em.persist(decaissement);
					for (Calapresdebl calapresdebl : calRembAprDebl) {
						for (int i = 0; i < calRembAprDebl.size()-1; i++) {
							calapresdebl.setDemandeCredit(dm); 							
						}  					  
					  em.persist(calapresdebl); 
					}
	
					transaction.commit();
					System.out.println("Decaisemment reussit!!!");
					result = "Decaisemment reussit!!!";

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				result = "Desolé, Vous ne pouvez pas decaisser cet demande!!!";
			}

		} else{
			result = "Numéro crédit non trouvé!!!";
			}
		return result;
	}
	
	/************************************************************************************************************************************/
								/********************** REMBOURSEMENT *********************************/
	/************************************************************************************************************************************/

	
	/**
	 * Remboursement Crédit
	 * **/

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public String rembourser(String numCredit, int userId, Remboursement remb) {
		
		String date_remb = remb.getDateRemb();
		
		Remboursement saveRemb = new Remboursement();
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
	
		///	pour incrémenter le tcode dans la table grandlivre
			String indexTcode = CodeIncrement.genTcode(em);
			
			CompteCaisse cptCaisse = em.find(CompteCaisse.class, remb.getCptCaisseNum());
			
			//Solde debité (Solde total Remboursé)
			Grandlivre debit= new Grandlivre();
			
			//Principal Total
			Grandlivre credit1 = new Grandlivre();
			
			//Interet Total
			Grandlivre credit2 = new Grandlivre();
		
		String results = "";
		
		//Verifie si le credit existe!
		if(dm != null)
		{
			// Verifie si le crédit en selectionné est déjà remboursé!
			
			if (dm.getSolde_total() != 0) {
				
				//Paiement Normale
				//Paiement Anticipative
				//Paiement Retard
				
				double principalTotal = dm.getPrincipale_total();
				double interetTotal = dm.getInteret_total();
				double soldeTotal = dm.getSolde_total();
				
				Query requete = em.createQuery("SELECT MIN(c.date) FROM Calapresdebl c JOIN c.demandeCredit numCred WHERE"
						+ " c.payer = :p AND numCred.numCredit = :numC");
				requete.setParameter("p", false);
				requete.setParameter("numC", numCredit);
				
				String dateCal = (String)requete.getSingleResult();
				
				System.out.println("date remboursement: "+dateCal);
				
				Date date1;
				Date date2;
				try {
					date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateCal);
					date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date_remb);
					System.out.println(date1);
					System.out.println(date2);
					
					//int days = Days.daysBetween(date2.getTime(), date1.getTime());
					//Verifie la difference entre la date de remboursement par rapport à la date de calandrier remboursement
					long diff = date2.getTime() - date1.getTime();				 
				    
					int val = (int) (diff / (1000*60*60*24));
				    System.out.println(val+"\n");
				    //List<E>
					if (val == 0) {
						/***
						 * REMBOURSEMENT NORMAL
						 * ***/
						System.out.println("Remboursement normal sans retard, (Principale et interert normal) \n");
						
						
						Query query = em.createQuery("SELECT cl.mprinc,cl.mint,cl.rowId FROM Calapresdebl cl JOIN cl.demandeCredit num"
										+ " WHERE cl.date = :dateRemb AND num.numCredit = :numCred");
						//query.setParameter("x", false);cl.payer = :x AND
						query.setParameter("dateRemb", dateCal);
						query.setParameter("numCred", "" + numCredit + "");
						
						double princ = 0.0;
						double inter = 0.0;
						
						
						//Recupère le montant principal et interet à rembouser
						
						List resultList = query.getResultList();
						if(resultList != null){
						int rowId = 0;
						
						for (Object ligneAsobject : resultList) {
							Object[] ligne = (Object[]) ligneAsobject;

							princ = Double.parseDouble(ligne[0].toString());
							inter = Double.parseDouble(ligne[1].toString());
							rowId =Integer.parseInt(ligne[2].toString());
						//String dat = ligne[2].toString();
							System.out.println("Principal total: " + princ);
							System.out.println("Interet total: " + inter);
							System.out.println("calendrier numero: "+rowId);
						}
						
						
						Calapresdebl cals = em.find(Calapresdebl.class, rowId);
						
						//System.out.println("Calendrier de remboursement : " + cals.getDate()+" "+cals.getRowId() +" "+cals.getMprinc() +" "+cals.getMint() ); 
						
						//Montant a remboursé
						double montRembourser = 0.0;
						//Recuperer le montant reste au dernier remboursement
						
						Query qr = em.createQuery("SELECT r.restaPaie FROM Remboursement r JOIN r.demandeCredit numDem"
								+ " WHERE r.dateRemb =( SELECT MAX(rb.dateRemb) FROM Remboursement rb JOIN rb.demandeCredit nums WHERE nums.numCredit"
								+ "= :numsCredit) AND numDem.numCredit = :numero");
						qr.setParameter("numsCredit", numCredit);
						qr.setParameter("numero", numCredit);			
						
						//On considère que le paiement de l'interet est prioritaire
						
						List listReq = qr.getResultList();
						if(listReq != null){
							
							double derRestPaie = (double)qr.getSingleResult();
						/*	
							for (Object object : listReq) {
								Object[] lignes = (Object[]) object;
								derRestPaie = Double.parseDouble(lignes[0].toString());
							}	*/
							
							System.out.println("montant reste de dernière paiement : " + derRestPaie); 
							montRembourser = remb.getMontant_paye() + derRestPaie;	
						}else{
							montRembourser = remb.getMontant_paye();
						}
						
						double restPaieInt = montRembourser - inter;
						double interetRemb = 0.0;
						double principaleRem = 0.0;
						double soldRest = 0.0;
						double restPaiePrinc = 0.0;
						
						if(restPaieInt > 0.0){
						
							interetRemb = inter;
							
							restPaiePrinc = restPaieInt - princ;
							
							if(restPaiePrinc >= 0.0){
								principaleRem = princ;
								cals.setPayer(true);
							}else if(restPaiePrinc < 0.0){
								principaleRem = restPaieInt;
								cals.setMprinc(restPaiePrinc);
								cals.setPayer(true);
							}
							
							soldRest = restPaiePrinc;			
							
							principalTotal = principalTotal - principaleRem;
							interetTotal = interetTotal - interetRemb;
							soldeTotal = principalTotal + interetTotal;//

							if(soldeTotal == 0.0){
								dm.setApprobationStatut("Credit remboursé");
							}
							
							//Insertion au GrandLivre
							double tots = principaleRem + interetRemb;
							String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
							
							//Solde Debité
							debit.setTcode(indexTcode);
							debit.setDate(date_remb);
							debit.setDescr("Remboursement du crédit "+numCredit);
							debit.setPiece(remb.getPiece());
							debit.setUserId(ut.getNomUtilisateur());
							debit.setCompte(cptC);
							debit.setDebit(tots);
							debit.setCodeInd(dm.getIndividuel());
							
							//Solde Crédité 1 (Principal)
							credit1.setTcode(indexTcode);
							credit1.setDate(date_remb);
							credit1.setDescr("Remboursement principal du crédit "+numCredit);
							credit1.setPiece(remb.getPiece());
							credit1.setUserId(ut.getNomUtilisateur());
							credit1.setCompte(confGl.getCptPrincEnCoursInd());
							credit1.setCredit(principaleRem);
							credit1.setCodeInd(dm.getIndividuel());
							
							
							//Solde Crédité 2 (Interet)
							
							credit2.setTcode(indexTcode);
							credit2.setDate(date_remb);
							credit2.setDescr("Remboursement Interet du crédit "+numCredit);
							credit2.setPiece(remb.getPiece());
							credit2.setUserId(ut.getNomUtilisateur());
							credit2.setCompte(confGl.getCptIntRecCrdtInd());
							credit2.setCredit(interetRemb);
							credit2.setCodeInd(dm.getIndividuel());
							
							//Inserer au Entité Remboursement
						
							saveRemb.setTcode(indexTcode);
							saveRemb.setDemandeCredit(dm);
							saveRemb.setUtilisateur(ut);
							saveRemb.setDateRemb(date_remb);
							saveRemb.setPiece(remb.getPiece());
							saveRemb.setCash(remb.getCash());
							saveRemb.setCptCaisseNum(cptC);
							saveRemb.setCheqid(remb.getCheqid());
							saveRemb.setCheqcomm(remb.getCheqcomm());
							saveRemb.setStationery(remb.getStationery());
							saveRemb.setOverpay(remb.getOverpay());
							saveRemb.setPrincipal(principaleRem);
							saveRemb.setInteret(interetRemb);
							saveRemb.setMontant_paye(remb.getMontant_paye());
							saveRemb.setRestaPaie(soldRest);
						
							//Modification sur l'entité DemandeCredit
							dm.setSolde_total(soldeTotal);
							dm.setPrincipale_total(principalTotal);
							dm.setInteret_total(interetTotal);
							
							//Modification sur l'entité Calapresdebl
							
							try {	
								transaction.begin();
								em.persist(saveRemb);
								em.persist(debit);
								em.persist(credit1);
								em.persist(credit2);
								em.merge(cals);
								em.merge(dm);
								transaction.commit();
								em.refresh(saveRemb);
								
								results = "Remboursement Enregistrer!!!";
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								if(em == null){
									em.close();
								}
								
							}
							
						}else if(restPaieInt < 0.0){
							System.out.println("montant inferieur au interet due");
						}
					}
				else{
					results = "Ce client ne pouvez pas rembourser aujourd'hui";
				}
						
						
					} else if (val > 0 && val < 90) {
						System.out.println(" Remboursement en retard inferieur à 90 jours, (Principale et interet normal) \n");
					} else if (val > 0 && val > 90) {
						System.out.println(" Remboursement en retard superieur à 90 jours, (Principale et interet normal + interet de retard) \n");
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
							
			} else {
				results = "Crédit déjà rembourser!!!";
			}
		} else {
			results = "cet numero credit n'existe pas!";
		}

		return results;
	}

}
