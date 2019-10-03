package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Calpaiementdue;
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
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitCredit;
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
	public List<DemandeCredit> findDemandeByMc(String mc) {
		TypedQuery<DemandeCredit> query = em.createQuery("SELECT d FROM DemandeCredit d WHERE d.approbationStatut like :mc", DemandeCredit.class);
		query.setParameter("mc", "%"+mc+"%");
		List<DemandeCredit> result = query.getResultList();
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

	static int getLastIndex() {
		String sql = "select count(*) from demande_credit";// where num_credit =
															// '"+num_crd+"'
															// String num_crd
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	
	@Override
	public List<Calpaiementdue> insertDemande(String idProduit, String codeInd,	String codeGroupe, String date_dem, double montant, String agent,String but, int user_id) {
		

		DemandeCredit dmd = new DemandeCredit();

		Utilisateur user = em.find(Utilisateur.class, user_id);
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		
		List<Calpaiementdue> listCalendrier = new ArrayList<Calpaiementdue>();
		
		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGroupe);	
		/***
		 * CHARGE LES CONFIGURATIONS DE FRAIS
		 * ***/
		
		/******************VERIFICATION SI CLIENT EST INDIVIDUEL OU GROUPE**************************************/
		

		/* Insertion d'information de demande credit */

		int lastIndex = getLastIndex();
		String index = String.format("%05d", ++lastIndex);
		
		if(ind != null){
			
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
				
				listCalendrier = CodeIncrement.getCalendrierPaiement(dmd);
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
			/***
			 * CHARGE LES CONFIGURATIONS POUR LA DEMANDE CREDIT D'UN GROUPE 
			 * ***/
			
			//ConfigCreditGroup configGroupe = pdtCredit.getConfigCreditGroupe();
			
		}	
		
		return listCalendrier;
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
