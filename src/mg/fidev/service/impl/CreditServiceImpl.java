package mg.fidev.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.Remboursement;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.CreditService;
import mg.fidev.utils.CodeIncrement;

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
		em.close();	
		
		return res;
	}
	
	
	/********************************************************DEMANDE CREDIT*************************************************************************/
	
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

				dmd.setDateDemande(date_dem);
				dmd.setMontantDemande(montant);
				dmd.setAgentName(agent);
				dmd.setButCredit(but);
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

				dmd.setDateDemande(date_dem);
				dmd.setMontantDemande(montant);
				dmd.setAgentName(agent);
				dmd.setButCredit(but);
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
	 * COMMISSION CREDIT
	 * ***/
	@Override
	public boolean insertCommission(boolean cash,String date, String piece,double commission,double papeterie,
			String numCredit,int userId, String nomCptCaisse) {

		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		CommissionCredit paie = new CommissionCredit();
		
		/***
		 * CHARGE LES CONFIGURATIONS FRAIS DE CREDIT
		 * ****/
		ConfigFraisCredit confFrais = dm.getProduitCredit().getConfigFraisCredit();
		ConfigFraisCreditGroupe confGroupe = dm.getProduitCredit().getConfFraisGroupe();
		
		boolean result = false;
		
		
		
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
					
					System.out.println("le Paiement de commission de cet Crédit est déjà fait");
					
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
						debitCom.setDate(date);
						debitCom.setDescr("Commision crédit "+numCredit);
						debitCom.setPiece(piece);
						debitCom.setUserId(ut.getNomUtilisateur());
						debitCom.setCompte(cptC);
						debitCom.setDebit(debitTot);
						debitCom.setCodeInd(dm.getIndividuel());
										
						//Solde Crédité Commission
						creditCom.setTcode(indexTcode);
						creditCom.setDate(date);
						creditCom.setDescr("Frais Commision crédit "+numCredit);
						creditCom.setPiece(piece);
						creditCom.setUserId(ut.getNomUtilisateur());
						creditCom.setCompte(confGl.getCptCommCredit());
						creditCom.setCredit(commissions);
						creditCom.setCodeInd(dm.getIndividuel());
						
						//Solde Crédité Papeterie
						creditPap.setTcode(indexTcode);
						creditPap.setDate(date);
						creditPap.setDescr("Frais Papéterie crédit "+numCredit);
						creditPap.setPiece(piece);
						creditPap.setUserId(ut.getNomUtilisateur());
						creditPap.setCompte(confGl.getCptPapeterie());
						creditPap.setCredit(papeteries);
						creditPap.setCodeInd(dm.getIndividuel());
						
						
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
					result = "Montant approuvé incorrecte!!!";
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
	public String saveDecaisement(String date, boolean cash, double montant, double commission, double papeterie, 
			String piece, String comptCaise,String numCredit, int userId) {
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		Decaissement decais = new Decaissement();
		ConfigCreditIndividuel confInds = dm.getProduitCredit().getConfigCreditIndividuel();
		ConfigGlCredit confGL = dm.getProduitCredit().getConfigGlCredit();
		
		///	pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		List<Calapresdebl> calRembAprDebl = CodeIncrement.getCalendrierRemboursement(dm);
		
		CompteCaisse cptCaisse = em.find(CompteCaisse.class, comptCaise);
		
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
			if (dm.getIndividuel() != null && (dm.getApprobationStatut().equalsIgnoreCase("Approuvé") || dm.getApprobationStatut().equalsIgnoreCase(
							"payé apres approbation"))) {

				try {
					//Total solde à décaissé : montant approuvé - (commission + papeterie)
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
					String cptC = String.valueOf(cptCaisse.getAccount().getTkey());
					
					//decais.setTcode(decaissement.getTcode());

					decais.setTcode(indexTcode);
					decais.setUtilisateur(ut);
					decais.setDemandeCredit(dm);
					decais.setCash(cash);
					decais.setDateDec(date);
					decais.setCommission(commission);
					decais.setStationnary(papeterie);
					decais.setMontantDec(montant);
					decais.setPiece(piece);
					decais.setCptCaisseNum(cptC);
										
					dm.setApprobationStatut("Demande decaissé");
					
					//Solde Debité
					debit.setTcode(indexTcode);
					debit.setDate(date);
					debit.setDescr("Decaissement crédit " +numCredit);
					debit.setPiece(piece);
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(confGL.getCptPrincEnCoursInd());
					debit.setDebit(dm.getMontantApproved());
					debit.setCodeInd(dm.getIndividuel());
					
					//Solde Crédité 1 (Solde Decaissé)
					credit1.setTcode(indexTcode);
					credit1.setDate(date);
					credit1.setDescr("Decaissement crédit "+numCredit);
					credit1.setPiece(piece);
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(cptC);
					credit1.setCredit(soldeDecais);
					credit1.setCodeInd(dm.getIndividuel());
					
					transaction.begin();
					//Solde Crédité 2 (Papeterie Decaissement)
					if(papeterie != 0){
						
						credit2.setTcode(indexTcode);
						credit2.setDate(date);
						credit2.setDescr("Papeterie au decaissement de crédit "+numCredit);
						credit2.setPiece(piece);
						credit2.setUserId(ut.getNomUtilisateur());
						credit2.setCompte(confGL.getPapeterieDec());
						credit2.setCredit(papeterie);	
						credit2.setCodeInd(dm.getIndividuel());
						
						em.persist(credit2);
					
					}
					
					//Solde Crédité 3 (Commission au Decaissement)
					if(commission != 0){
						
						credit3.setTcode(indexTcode);
						credit3.setDate(date);
						credit3.setDescr("Commission au decaissement de crédit "+numCredit);
						credit3.setPiece(piece);
						credit3.setUserId(ut.getNomUtilisateur());
						credit3.setCompte(confGL.getCommDec());
						credit3.setCredit(commission);
						credit3.setCodeInd(dm.getIndividuel());
						
						em.persist(credit3);
					}
					
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
	
	
	
	
	/***
	 * REMBOURSEMENT
	 * ***/

	@SuppressWarnings({ "rawtypes", "unused"})
	@Override
	public boolean saveRemboursement(String numCredit, int userId, String date,
			double montant, String piece, boolean cash, String cmptCaisse) {

		Remboursement saveRemb = new Remboursement();
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();

		boolean retour = false;

		// / pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);

		CompteCaisse cptCaisse = em.find(CompteCaisse.class, cmptCaisse);

		// Solde debité (Solde total Remboursé)
		Grandlivre debit = new Grandlivre();

		// Principal Total
		Grandlivre credit1 = new Grandlivre();

		// Interet Total
		Grandlivre credit2 = new Grandlivre();

		String results = "";

		// Verifie si le credit existe
		if (dm != null) {
			// Verifie si le crédit selectionné est déjà remboursé!

			if (dm.getSolde_total() != 0) {

				// Paiement Normale
				// Paiement Anticipative
				// Paiement Retard
				// Prend le dernier Principale total, intérêt total, solde total
				// du crédit
				double principalTotal = dm.getPrincipale_total();
				double interetTotal = dm.getInteret_total();
				double soldeTotal = dm.getSolde_total();

				// Selection de date qu'on doit faire le remboursement
				Query requete = em
						.createQuery("SELECT MIN(c.date) FROM Calapresdebl c JOIN c.demandeCredit numCred WHERE"
								+ " c.payer = :p AND numCred.numCredit = :numC");
				requete.setParameter("p", false);
				requete.setParameter("numC", numCredit);

				String dateCal = (String) requete.getSingleResult();

				System.out.println("date remboursement: " + dateCal);

				// Comparaison de la date entrer en parametre et la de
				// remboursement
				Date date1;
				Date date2;
				try {
					date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateCal);
					date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
					System.out.println(date1);
					System.out.println(date2);

					// int days = Days.daysBetween(date2.getTime(),
					// date1.getTime());
					// Verifie la difference entre la date de remboursement par
					// rapport à la date de calandrier remboursement
					long diff = date2.getTime() - date1.getTime();

					int val = (int) (diff / (1000 * 60 * 60 * 24));
					System.out.println(val + "\n");

					// Si la date entrer en paramètre est égale à la date de
					// remboursement due on a une remboursement normal

					if (val == 0) {
						/***
						 * REMBOURSEMENT NORMAL
						 * ***/
						System.out
								.println("Remboursement normal sans retard, (Principale et interert normal) \n");

						// Selection de principale due et intérêt due

						Query query = em
								.createQuery("SELECT cl.mprinc,cl.mint,cl.rowId FROM Calapresdebl cl JOIN cl.demandeCredit num"
										+ " WHERE cl.date = :dateRemb AND num.numCredit = :numCred");
						// query.setParameter("x", false);cl.payer = :x AND
						query.setParameter("dateRemb", dateCal);
						query.setParameter("numCred", "" + numCredit + "");

						double princ = 0.0;
						double inter = 0.0;

						// Recupère le montant principal et interet à rembouser

						List resultList = query.getResultList();
						if (resultList != null) {
							int rowId = 0;

							for (Object ligneAsobject : resultList) {
								Object[] ligne = (Object[]) ligneAsobject;

								princ = Double.parseDouble(ligne[0].toString());
								inter = Double.parseDouble(ligne[1].toString());
								rowId = Integer.parseInt(ligne[2].toString());
								// String dat = ligne[2].toString();
								System.out.println("Principal total: " + princ);
								System.out.println("Interet total: " + inter);
								System.out.println("calendrier numero: "
										+ rowId);
							}

							Calapresdebl cals = em.find(Calapresdebl.class,	rowId);

							// Montant a remboursé
							double montRembourser = 0.0;
							
							// Recuperer le montant reste au dernier
							// remboursement

							Query qr = em.createQuery("SELECT r.restaPaie FROM Remboursement r JOIN r.demandeCredit numDem"
											+ " WHERE r.dateRemb =( SELECT MAX(rb.dateRemb) FROM Remboursement rb JOIN rb.demandeCredit nums WHERE nums.numCredit"
											+ "= :numsCredit) AND numDem.numCredit = :numero");
							qr.setParameter("numsCredit", numCredit);
							qr.setParameter("numero", numCredit);

							// On considère que le paiement de l'interet est
							// prioritaire

							List listReq = qr.getResultList();
							if (listReq.isEmpty()) {
								montRembourser = montant;

							} else {
								double derRestPaie = (double)qr.getSingleResult();
								montRembourser = montant + derRestPaie;								
								System.out.println("montant reste de dernière paiement : "+ derRestPaie);
							}

							double restPaieInt = montRembourser - inter;
							double interetRemb = 0.0;
							double principaleRem = 0.0;
							double soldRest = 0.0;
							double restPaiePrinc = 0.0;

							if (restPaieInt > 0.0) {

								interetRemb = inter;

								restPaiePrinc = restPaieInt - princ;

								if (restPaiePrinc >= 0.0) {
									principaleRem = princ;
									cals.setPayer(true);
								} else if (restPaiePrinc < 0.0) {
									principaleRem = restPaieInt;
									cals.setMprinc(restPaiePrinc);
									cals.setPayer(true);
								}

								soldRest = restPaiePrinc;

								principalTotal = principalTotal - principaleRem;
								interetTotal = interetTotal - interetRemb;
								soldeTotal = principalTotal + interetTotal;//

								if (soldeTotal == 0.0) {
									dm.setApprobationStatut("Credit remboursé");
								}

								// Insertion au GrandLivre
								double tots = principaleRem + interetRemb;
								String cptC = String.valueOf(cptCaisse
										.getAccount().getTkey());

								// Solde Debité
								debit.setTcode(indexTcode);
								debit.setDate(date);
								debit.setDescr("Remboursement du crédit "
										+ numCredit);
								debit.setPiece(piece);
								debit.setUserId(ut.getNomUtilisateur());
								debit.setCompte(cptC);
								debit.setDebit(tots);
								debit.setCodeInd(dm.getIndividuel());

								// Solde Crédité 1 (Principal)
								credit1.setTcode(indexTcode);
								credit1.setDate(date);
								credit1.setDescr("Remboursement principal du crédit "
										+ numCredit);
								credit1.setPiece(piece);
								credit1.setUserId(ut.getNomUtilisateur());
								credit1.setCompte(confGl
										.getCptPrincEnCoursInd());
								credit1.setCredit(principaleRem);
								credit1.setCodeInd(dm.getIndividuel());

								// Solde Crédité 2 (Interet)

								credit2.setTcode(indexTcode);
								credit2.setDate(date);
								credit2.setDescr("Remboursement Interet du crédit "
										+ numCredit);
								credit2.setPiece(piece);
								credit2.setUserId(ut.getNomUtilisateur());
								credit2.setCompte(confGl.getCptIntRecCrdtInd());
								credit2.setCredit(interetRemb);
								credit2.setCodeInd(dm.getIndividuel());

								// Inserer au Entité Remboursement

								saveRemb.setTcode(indexTcode);
								saveRemb.setDemandeCredit(dm);
								saveRemb.setUtilisateur(ut);
								saveRemb.setDateRemb(date);
								saveRemb.setPiece(piece);
								saveRemb.setCash(cash);
								saveRemb.setCptCaisseNum(cptC);
								saveRemb.setCheqid(0);
								saveRemb.setCheqcomm(0);
								saveRemb.setStationery(0);
								saveRemb.setOverpay(0);
								saveRemb.setPrincipal(principaleRem);
								saveRemb.setInteret(interetRemb);
								saveRemb.setMontant_paye(montant);
								saveRemb.setRestaPaie(soldRest);

								// Modification sur l'entité DemandeCredit
								dm.setSolde_total(soldeTotal);
								dm.setPrincipale_total(principalTotal);
								dm.setInteret_total(interetTotal);

								// Modification sur l'entité Calapresdebl

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
									System.out.println(results);
									retour = true;
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (em == null) {
										em.close();
									}

								}

							} else if (restPaieInt < 0.0) {
								System.out
										.println("montant inferieur au interet due");
							}
						} else {
							results = "Ce client ne pouvez pas rembourser aujourd'hui";
						}
					} else if (val > 0 && val < 90) {
						// Si date entrer en paramètre supérieur à la date de
						// remboursement due et inférieur à 90 jours
						System.out
								.println(" Remboursement en retard inferieur à 90 jours, (Principale et interet normal) \n");
					} else if (val > 0 && val > 90) {
						// Si date entrer en paramètre supérieur à la date de
						// remboursement due et supérieur à 90 jours
						System.out
								.println(" Remboursement en retard superieur à 90 jours, (Principale et interet normal + interet de retard) \n");
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
					+ " WHERE numDem.numCredit = :numero");
			qr.setParameter("numero", numCredit);			
			
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
			lst.add(erreur);
			System.err.println(erreur);
		} 
	
		return lst;
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
