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
import mg.fidev.model.ApprobationCredit;
import mg.fidev.model.Caisse;
import mg.fidev.model.CalView;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.CommissionCredit;
import mg.fidev.model.ConfigCredit;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigDeclasseCredit;
import mg.fidev.model.ConfigFraisCredit;
import mg.fidev.model.ConfigFraisCreditGroupe;
import mg.fidev.model.ConfigGarantieCredit;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.ConfigGlCredit;
import mg.fidev.model.ConfigPenaliteCredit;
import mg.fidev.model.CrediGroupeView;
import mg.fidev.model.CreditMembreGroupe;
import mg.fidev.model.Decaissement;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.FicheCredit;
import mg.fidev.model.Garant;
import mg.fidev.model.GarantieCredit;
import mg.fidev.model.GarantieView;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.Remboursement;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.CreditService;
import mg.fidev.utils.AfficheSoldeRestantDu;
import mg.fidev.utils.Agent;
import mg.fidev.utils.ClientAgent;
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
	
	//Chercher crédit par mot clé
	@Override
	public List<DemandeCredit> chercherCredit(String mc) {
		TypedQuery<DemandeCredit> query = em.createQuery("select d from DemandeCredit d where d.numCredit like :x"
				+ " and d.approbationStatut =:ap", DemandeCredit.class);
		query.setParameter("x", mc+"%");
		query.setParameter("ap","Demande decaissé");
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
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

	/***
	 * SAVE DEMANDE CREDIT Individuel (Opérationnel)
	 * ***/
	@Override
	public boolean saveDemandeCreditIndividuel(String idProduit, String codeInd,DemandeCredit demande, int idAgent,
			String codeGar1, String codeGar2, String codeGar3, int user_id,
			double tauxGar1, double tauxGar2, double tauxGar3) {
		
		//Instancie les classe necessaire
		//Produit et utilisateur
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		//Utilisateur de saisie
		Utilisateur user = em.find(Utilisateur.class, user_id);
		//Agent de credit
		Utilisateur agent = em.find(Utilisateur.class, idAgent);
		
		//ajout demande credit au Fihe credit
		FicheCredit ficheCredit= new FicheCredit();		
		//Client
		Individuel ind = em.find(Individuel.class, codeInd);	
		
		if(ind != null){
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);		
			String ag = ind.getCodeInd().substring(0, 2);
			
			//information de demande credit
			demande.setNumCredit(ag + "/" + index);
			
			//Récuperation calendrier due
			double interet = 0;
			List<Calpaiementdue> calend = new ArrayList<Calpaiementdue>();
			List<CalView> calV = getAllCalView();
			double soldCourant = ficheCredit.getSoldeCourant();
			double totInter = ficheCredit.getInteret();
			
			for (CalView calView : calV) {
				Calpaiementdue cald = new Calpaiementdue();
				cald.setDate(calView.getDate());
				cald.setMontantComm(calView.getMontantComm());
				cald.setMontantInt(calView.getMontantInt());
				cald.setMontantPenal(calView.getMontantPenal());
				cald.setMontantPrinc(calView.getMontantPrinc());
				cald.setDemandeCredit(demande); 
				interet += calView.getMontantInt();
				calend.add(cald);
				//Total solde courant
				soldCourant += calView.getMontantPrinc() + calView.getMontantInt() - calView.getMontantPenal();
				//total intérêt				
				totInter += calView.getMontantInt();
				//Solde total
				double soldTotal = totInter + demande.getMontantDemande();
				
				//ajout calendrier due au Fihe credit
				FicheCredit fiche = new FicheCredit();
				fiche.setDate(calView.getDate()); 
				fiche.setPiece("");
				fiche.setTransaction("Tranche due");
				fiche.setPrincipale(calView.getMontantPrinc()); 
				fiche.setInteret(calView.getMontantInt()); 
				fiche.setPenalite(calView.getMontantPenal());
				fiche.setSoldeCourant(soldCourant);
				fiche.setTotalPrincipal(demande.getMontantDemande());
				fiche.setTotalInteret(totInter);
				fiche.setTotalSolde(soldTotal); 
				fiche.setNum_credit(demande.getNumCredit());
				try {
					transaction.begin();
					em.persist(fiche);
					transaction.commit();
					em.refresh(fiche); 
					System.out.println("Fiche credit enregitré");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			//Importation garatie from garantie view
			List<GarantieCredit> garaties = new ArrayList<GarantieCredit>();
			List<GarantieView> listGaraties = getAllGarantieView();
			if(listGaraties != null){
				for (GarantieView garantieView : listGaraties) {
					GarantieCredit gar = new GarantieCredit();
					gar.setLever(false);
					gar.setNomGarantie(garantieView.getNomGarantie());
					gar.setPourcentage(garantieView.getPourcentage());
					gar.setTypeGarantie(garantieView.getTypeGarantie());
					gar.setValeur(garantieView.getValeur());
					gar.setDemandeCredit(demande);
					garaties.add(gar);
				}
				//Enregistrement de la garantie
				demande.setGarantieCredits(garaties);				
			}			
			
			demande.setIndividuel(ind);
			demande.setUtilisateur(user);
			demande.setProduitCredit(pdtCredit);
			demande.setNbCredit(CodeIncrement.nombreCreditInd(em, ind.getCodeInd()));
			demande.setAgent(agent);
			demande.setApprobationStatut("Approbation en attente");
			demande.setCalpaiementdues(calend);
			demande.setInteret(interet); 
			//Vider table calendrier view
			deleteCalendrier();
			//Vider table garantie view
			deleteGarantieVIew();
			
			//Enregistrement Fiche credit
			
			ficheCredit.setDate(demande.getDateDemande()); 
			ficheCredit.setPiece("");
			ficheCredit.setTransaction("Demande crédit");
			ficheCredit.setPrincipale(demande.getMontantDemande()); 
			ficheCredit.setInteret(interet); 
			ficheCredit.setPenalite(0);
			ficheCredit.setSoldeCourant(0);
			ficheCredit.setTotalPrincipal(demande.getMontantDemande());
			ficheCredit.setTotalInteret(0);
			ficheCredit.setTotalSolde(demande.getMontantDemande());
			ficheCredit.setNum_credit(demande.getNumCredit());
		
			System.out.println("information demande crédit prête");
			try {			
				transaction.begin();
				em.persist(demande);
				em.persist(ficheCredit); 
				transaction.commit();
				em.refresh(demande);
				em.refresh(ficheCredit);
				//Garant crédit				
				//Garant
				//Garant1
				if(!codeGar1.equals("")){
					Garant ga1 = em.find(Garant.class, codeGar1);
					double montant = (demande.getMontantDemande()*tauxGar1)/100;
					ga1.setTauxCred(tauxGar1); 
					ga1.setMontant(montant);
					ga1.setDemandeCredit(demande); 
					
					transaction.begin();
					em.merge(ga1);
					transaction.commit();
					em.refresh(ga1);
				}
				//Garant 2
				if(!codeGar2.equals("")){
					Garant ga2 = em.find(Garant.class, codeGar2);	
					double montant = (demande.getMontantDemande()*tauxGar2)/100;
					ga2.setTauxCred(tauxGar2); 
					ga2.setMontant(montant);
					ga2.setDemandeCredit(demande); 
					
					transaction.begin();
					em.merge(ga2);
					transaction.commit();
					em.refresh(ga2);
				}
				//Garant 3
				if(!codeGar3.equals("")){
					Garant ga3 = em.find(Garant.class, codeGar3);	
					double montant = (demande.getMontantDemande()*tauxGar3)/100;
					ga3.setTauxCred(tauxGar3); 
					ga3.setMontant(montant);
					ga3.setDemandeCredit(demande); 
					
					transaction.begin();
					em.merge(ga3);
					transaction.commit();
					em.refresh(ga3);
				}		
				
				System.out.println("Demande crédit de "+demande.getNumCredit()+" enregistré");
				return true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}
			
		}	
		return false;
		
	}
	

	@Override
	public boolean updateDemandeCredit(String numCredit,DemandeCredit demande) {
		
		//DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		
		return false;
	}

	static int getLastIndexMembre(String codeGrp){
		String sql = "select count(*) from membre_groupe where groupe='"+codeGrp+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	//Enregistrement montant crédit par membre  
	@Override
	public List<CrediGroupeView> addmontantMembreGroupe(String numCredit,String codeGrp,String codeInd, 
			double montant, double interet, int taux) {
		
		List<CrediGroupeView> result = new ArrayList<CrediGroupeView>();
		//Instance de classe CreditMembre pour enregistrer les montants par membres
		CrediGroupeView cdm = new CrediGroupeView();
		
		//Instance de groupe (code goupe), crédit(numCredit), individuel(codeIndividuel)
		/*Groupe groupe = em.find(Groupe.class, codeGrp);
		Individuel individuel = em.find(Individuel.class, codeInd);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);	*/			
		
		//Vérification si le montant du crédit est déjà attribué au membre
		String sql = "select c from CrediGroupeView c "
				+ " where c.numCredit='"+numCredit+"' and c.codeInd='"+codeInd+"' and c.codeGrp='"+codeGrp+"'";
		
		Query q = em.createQuery(sql);
		
		if(!q.getResultList().isEmpty()){
			System.out.println("montant déjà ajouté");	
			return null;
		}else{
			try {
				cdm.setPrincipale(montant);
				cdm.setInteret(interet);
				cdm.setTauxInt(taux); 
				cdm.setCodeGrp(codeGrp);
				cdm.setCodeInd(codeInd);
				cdm.setNumCredit(numCredit);
				
				transaction.begin();
				em.persist(cdm);
				transaction.commit();
				em.refresh(cdm);
				System.out.println(montant+" ajouter au membre n° "+codeInd);	
				//System.out.println("montant "+montant+" ajouter au membre numero "+index);
				result .add(cdm);
				return result;
				
			} catch (Exception e) {
				e.printStackTrace(); 
				return null;
			}
		}	
	}

	//Modifier montant ajouter au membre groupe
	@Override
	public boolean updateMontant(int id, CrediGroupeView crediGroupeView) {
		CrediGroupeView crd = em.find(CrediGroupeView.class, id);
		
		crd.setInteret(crediGroupeView.getInteret());
		crd.setPrincipale(crediGroupeView.getPrincipale());
		crd.setTauxInt(crediGroupeView.getTauxInt());
		crd.setCodeGrp(crediGroupeView.getCodeGrp());
		crd.setCodeInd(crediGroupeView.getCodeInd());
		crd.setNumCredit(crediGroupeView.getNumCredit());
		try {
			transaction.begin();
			em.merge(crd);
			transaction.commit();
			em.refresh(crd); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Supprimer table view créditMembreView
	@Override
	public boolean deleteMontant() {
		Query query = em.createNativeQuery("TRUNCATE TABLE credit_groupe_view");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table CreditMembreView vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//Récuperer tous ce qui est dans la table créditMembreView
	@Override
	public List<CrediGroupeView> getAllCrediGroupeView() {
		String sql = "select c from CrediGroupeView c";
		return em.createQuery(sql,CrediGroupeView.class).getResultList();
	}
	
	//Enregistrement demande crédit groupe
	@Override
	public boolean saveDemandeCreditGroupe(String idProduit, String codeGrp,
			DemandeCredit demande, int idAgent, int user_id) {
		// Instancie les classe necessaire
		// Produit et utilisateur
		ProduitCredit pdtCredit = em.find(ProduitCredit.class, idProduit);
		// Utilisateur de saisie
		Utilisateur user = em.find(Utilisateur.class, user_id);
		// Agent de credit
		Utilisateur agent = em.find(Utilisateur.class, idAgent);

		// ajout demande credit au Fihe credit
		FicheCredit ficheCredit = new FicheCredit();
		// Client
		Groupe grp = em.find(Groupe.class, codeGrp);

		if (grp != null) {
			int lastIndex = getLastIndex(codeGrp);
			String index = String.format("%05d", ++lastIndex);
			String ag = grp.getCodeGrp().substring(0, 2);

			// information de demande credit
			demande.setNumCredit(ag + "/" + index);

			// Information calendrier d'écheance
			// Récuperation calendrier due
			double interet = 0;
			List<Calpaiementdue> calend = new ArrayList<Calpaiementdue>();
			List<CalView> calV = getAllCalView();
			double soldCourant = ficheCredit.getSoldeCourant();
			double totInter = ficheCredit.getInteret();

			for (CalView calView : calV) {
				Calpaiementdue cald = new Calpaiementdue();
				cald.setDate(calView.getDate());
				cald.setMontantComm(calView.getMontantComm());
				cald.setMontantInt(calView.getMontantInt());
				cald.setMontantPenal(calView.getMontantPenal());
				cald.setMontantPrinc(calView.getMontantPrinc());
				cald.setDemandeCredit(demande);
				interet += calView.getMontantInt();
				calend.add(cald);
				// Total solde courant
				soldCourant += calView.getMontantPrinc()
						+ calView.getMontantInt() - calView.getMontantPenal();
				// total intérêt
				totInter += calView.getMontantInt();
				// Solde total
				double soldTotal = totInter + demande.getMontantDemande();

				// ajout calendrier due au Fihe credit
				FicheCredit fiche = new FicheCredit();
				fiche.setDate(calView.getDate());
				fiche.setPiece("");
				fiche.setTransaction("Tranche due");
				fiche.setPrincipale(calView.getMontantPrinc());
				fiche.setInteret(calView.getMontantInt());
				fiche.setPenalite(calView.getMontantPenal());
				fiche.setSoldeCourant(soldCourant);
				fiche.setTotalPrincipal(demande.getMontantDemande());
				fiche.setTotalInteret(totInter);
				fiche.setTotalSolde(soldTotal);
				fiche.setNum_credit(demande.getNumCredit());
				try {
					transaction.begin();
					em.persist(fiche);
					transaction.commit();
					em.refresh(fiche);
					System.out.println("Fiche credit groupe enregitré");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// Infomation garantie crédit
			// Importation garatie from garantie view
			List<GarantieCredit> garaties = new ArrayList<GarantieCredit>();
			List<GarantieView> listGaraties = getAllGarantieView();
			if (listGaraties != null) {
				for (GarantieView garantieView : listGaraties) {
					GarantieCredit gar = new GarantieCredit();
					gar.setLever(false);
					gar.setNomGarantie(garantieView.getNomGarantie());
					gar.setPourcentage(garantieView.getPourcentage());
					gar.setTypeGarantie(garantieView.getTypeGarantie());
					gar.setValeur(garantieView.getValeur());
					gar.setDemandeCredit(demande);
					garaties.add(gar);
				}
				// Enregistrement de la garantie
				demande.setGarantieCredits(garaties);
			}
			
			// Vider table calendrier view
			deleteCalendrier();
			// Vider table garantie view
			deleteGarantieVIew();

			// Enregistrement Fiche credit
			ficheCredit.setDate(demande.getDateDemande());
			ficheCredit.setPiece("");
			ficheCredit.setTransaction("Demande crédit");
			ficheCredit.setPrincipale(demande.getMontantDemande());
			ficheCredit.setInteret(interet);
			ficheCredit.setPenalite(0);
			ficheCredit.setSoldeCourant(0);
			ficheCredit.setTotalPrincipal(demande.getMontantDemande());
			ficheCredit.setTotalInteret(0);
			ficheCredit.setTotalSolde(demande.getMontantDemande());
			ficheCredit.setNum_credit(demande.getNumCredit());
			
			// Information montant crédit pour chaque membre du groupe
			List<CreditMembreGroupe> crediMembre = new ArrayList<CreditMembreGroupe>();
			List<CrediGroupeView> lCredGroupView = getAllCrediGroupeView();
			for (CrediGroupeView crediGroupeView : lCredGroupView) {
				CreditMembreGroupe membre = new CreditMembreGroupe();
				Individuel i = em.find(Individuel.class, crediGroupeView.getCodeInd());
				membre.setDemandeCredit(demande); 
				membre.setGroupe(grp);
				membre.setIndividuel(i);
				membre.setPrincipale(crediGroupeView.getPrincipale());
				membre.setInteret(crediGroupeView.getInteret());
				membre.setTauxInt(crediGroupeView.getTauxInt());
				System.out.println("Montant crédit du membre n°: "
						+ i.getCodeInd() + " enregistré");
				crediMembre.add(membre);
				/*try {
					transaction.begin();
					em.persist(membre);
					transaction.commit();
					em.refresh(membre);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				
			}

			demande.setGroupe(grp);
			demande.setUtilisateur(user);
			demande.setProduitCredit(pdtCredit);
			demande.setNbCredit(CodeIncrement.nombreCreditGrp(em,
					grp.getCodeGrp()));
			demande.setAgent(agent);
			demande.setApprobationStatut("Approbation en attente");
			demande.setCalpaiementdues(calend);
			demande.setMontantMembres(crediMembre); 
			demande.setInteret(interet);

			
			System.out.println("information demande crédit groupe prête");
			try {				
				transaction.begin();
				em.persist(demande);
				em.persist(ficheCredit);
				transaction.commit();				
				System.out.println("Demande crédit de "
						+ demande.getNumCredit() + " enregistré");
				
				
				em.refresh(demande);
				em.refresh(ficheCredit);
				return true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}

		}
		return false;
	}
		
	//SAISIE information demande crédit
	//Générer calendrier remboursement d'une demande crédit groupe
	@Override
	public List<CalView> demCredit(String codeInd, String codeGrp,
			String date_dem, double montant, double tauxInt, int nbTranche,
			String typeTranche, int diffPaie, String modCalcul) {

		List<CalView> resultat = new ArrayList<CalView>();
		
		DemandeCredit dmd = new DemandeCredit();
		
		if(!codeInd.equals("")){
			Individuel ind = em.find(Individuel.class, codeInd);
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = ind.getCodeInd().substring(0, 2);
			dmd.setNumCredit(ag + "/" + index);
			dmd.setIndividuel(ind);		
			System.out.println("Demande credit pour client individuel");
		}
		if(!codeGrp.equals("")){
			Groupe grp = em.find(Groupe.class, codeGrp);
			int lastIndex = getLastIndex(codeGrp);
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
				double interetSemaine = intTot / 4;
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
				double interet_quinz = intTot / 2;
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
	
	//Liste Commission avant ou après approbation	
	@Override
	public List<DemandeCredit> getCommissionAvantApprobation(boolean a) {		
		//Si a = true on affiche les crédits payé le commission avant approbation sinon on affiche les après approbation
		
		String sql = "select d from DemandeCredit d join d.produitCredit p join p.configFraisCredit ci "
				+ " join p.confFraisGroupe cg ";//avant approbation
		if(a==true){
			sql+=" where d.commission='"+false+"' and (ci.avantOuApres='avant approbation' or cg.avantOuApres='avant approbation')";
		}else{
			sql+=" where d.commission='"+false+"' and (ci.avantOuApres='apres approbation' or cg.avantOuApres='apres approbation')";
		}
		TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	//COMMISSION CREDIT
	@Override
	public boolean insertCommission(boolean cash,String paye,String date, String piece,double commission,double papeterie,
			String numCredit,int userId, String nomCptCaisse) {

		//Instance des classes necessaires
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
		CommissionCredit paie = new CommissionCredit();	
		
		
		//valeur de retour
		boolean result = false;	
				
		//CHARGE LES CONFIGURATIONS GL DU CREDIT
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
		
		//pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		//Instance du compte caisse
		Caisse cptCaisse = em.find(Caisse.class, nomCptCaisse);
		
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
		double fraisDevel = 0.0;
		double FraisRefinance = 0.0;
		
		//si c'est un crédit individuel
		if(dm.getIndividuel() != null ){//confFrais.isIndOuGroupe() == true
			System.out.println("Paiement commission crédit individuel");
			//CHARGE La CONFIGURATIONS FRAIS DE CREDIT
			ConfigFraisCredit confFrais = dm.getProduitCredit().getConfigFraisCredit();
			
			double comm = 0;
			double pap = 0;
			double frDev = 0;
			double frRef = 0;
			
			//Si frais = montant fixe
			if(confFrais.getCommission() != 0)
				comm = confFrais.getCommission();
			if(confFrais.getPapeterie() != 0)
				pap = confFrais.getPapeterie();
			if(confFrais.getFraisDeveloppement() !=0)
				frDev = confFrais.getFraisDeveloppement();
			if(confFrais.getFraisRefinancement() != 0)
				frRef=confFrais.getFraisRefinancement();
			
			//Si frais en pourcentage
			if(confFrais.getTauxCommission() != 0)
				comm = (dm.getMontantDemande() * confFrais.getTauxCommission()/100);
			if(confFrais.getTauxPapeterie() != 0)
				pap = (dm.getMontantDemande() * confFrais.getTauxPapeterie()/100);
			if(confFrais.getTauxFraisDev() != 0)
				frDev = (dm.getMontantDemande() * confFrais.getTauxFraisDev()/100);
			if(confFrais.getTauxRef() != 0)
				frRef = (dm.getMontantDemande() * confFrais.getTauxRef()/100);
				
			debitTot = comm + pap + frDev + frRef;
			
			commissions = comm;
			papeteries = pap;
			fraisDevel = frDev;
			FraisRefinance = frRef;
		}//si crédit groupe
		else if(dm.getGroupe() != null){
			System.out.println("Paiement commission crédit groupe");
			//Charge la configuration frais groupe
			ConfigFraisCreditGroupe confGroupe = dm.getProduitCredit().getConfFraisGroupe();
			
			double comm = 0;
			double pap = 0;
			double frDev = 0;
			double frRef = 0;
			
			//Si frais = montant fixe
			if(confGroupe.getCommission() != 0)
				comm = confGroupe.getCommission();
			if(confGroupe.getPapeterie() != 0)
				pap = confGroupe.getPapeterie();
			if(confGroupe.getFraisDeveloppement() !=0)
				frDev = confGroupe.getFraisDeveloppement();
			if(confGroupe.getFraisRefinancement() != 0)
				frRef=confGroupe.getFraisRefinancement();
			
			//Si frais en pourcentage
			if(confGroupe.getTauxCommission() != 0)
				comm = (dm.getMontantDemande() * confGroupe.getTauxCommission()/100);
			if(confGroupe.getTauxPapeterie() != 0)
				pap = (dm.getMontantDemande() * confGroupe.getTauxPapeterie()/100);
			if(confGroupe.getTauxFraisDev() != 0)
				frDev = (dm.getMontantDemande() * confGroupe.getTauxFraisDev()/100);
			if(confGroupe.getTauxRef() != 0)
				frRef = (dm.getMontantDemande() * confGroupe.getTauxRef()/100);
				
			debitTot = comm + pap + frDev + frRef;
			
			commissions = comm;
			papeteries = pap;
			fraisDevel = frDev;
			FraisRefinance = frRef;
		}				
		
		//Vérification de statut de crédit
		if (dm.isCommission() == true) {			
			System.out.println("le Paiement de commission de cet Crédit est déjà fait");
			
		} else {

			try {
				paie.setStatut_comm(paye);
				dm.setApprobationStatut(paye);
				dm.setCommission(true); 
				
				//Insertion au GrandLivre
				//compte débité
				String cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
				
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
				paie.setTdf((float) fraisDevel);
				paie.setTotvat((float)FraisRefinance);

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
		return result;
	}
	
	//APPROBATION
	@Override
	public String saveApprobation(String numCredit, String Appby,String dateApp, String descApp, double montantApp,
			String stat_demande) {

		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		ApprobationCredit ap = new ApprobationCredit();
		String result = "";
		boolean validation = false;

		// Vérifie si le numero de crédit n'est pas null
		if (dmd != null) {

			if (dmd.getApprobationStatut().equalsIgnoreCase("Approbation en attente")
					|| dmd.getApprobationStatut().equalsIgnoreCase("commission payer")) {

				// Changer Statut demande en approuvé

				if (dmd.getMontantApproved() == 0 && (montantApp <= dmd.getMontantDemande())) {

					dmd.setMontantApproved(montantApp);
					dmd.setApprobationStatut(stat_demande); 
					dmd.setDateApprobation(dateApp); 
					
					ap.setDateAp(dateApp);
					ap.setDescription(descApp);
					ap.setMontantApprouver(montantApp);
					ap.setPersoneAp(Appby); 
					ap.setDemandeCredit(dmd); 
					
					//Fihe credit
					FicheCredit fiche = new FicheCredit();
					fiche.setDate(dateApp); 
					fiche.setPiece("");
					fiche.setTransaction("Approbation");
					fiche.setPrincipale(dmd.getMontantDemande()); 
					fiche.setInteret(dmd.getInteret()); 
					fiche.setPenalite(0);
					fiche.setSoldeCourant(0);
					fiche.setTotalPrincipal(dmd.getMontantDemande());
					fiche.setTotalInteret(0);
					fiche.setTotalSolde(dmd.getMontantDemande()); 
					fiche.setNum_credit(dmd.getNumCredit());
		
					try {
						transaction.begin();
						em.flush();
						em.persist(fiche); 
						em.persist(ap); 
						transaction.commit();
						em.refresh(dmd);
						em.refresh(fiche); 
						em.refresh(ap); 
						System.out.println("Fiche credit enregitré");
						System.out.println("crédit "+ stat_demande+ " avec succes!!!");
						result = "Crédit " + stat_demande + " par "	+ ap.getPersoneAp();
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
				result = "Crédits déjà approuvé!!!";
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
		
		ConfigCreditIndividuel confInds = dm.getProduitCredit().getConfigCreditIndividuel();
		ConfigGlCredit confGL = dm.getProduitCredit().getConfigGlCredit();
		ConfigGeneralCredit confGen = dm.getProduitCredit().getConfigGeneralCredit();
		
		///	pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		List<Calapresdebl> calRembAprDebl = new ArrayList<Calapresdebl>();
		
		//Suppression des calendrier dues au fiche crédit
		/*String sql_fiche = "select f from FicheCredit f where f.transaction='Tranche due' "
				+ " and f.num_credit='"+dm.getNumCredit()+"'";
		TypedQuery<FicheCredit> query_fiche = em.createQuery(sql_fiche,FicheCredit.class);
		List<FicheCredit> fs = query_fiche.getResultList();
		for (FicheCredit ficheCredit : fs) {
			try {
				transaction.begin();
				em.remove(ficheCredit); 
				transaction.commit();
				System.out.println("Tranche due fiche crédit n°: "+ficheCredit.getId()+" supprimé");
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		FicheCredit ficheCredit = new FicheCredit();
		double soldCourant = 0;
		double totInter = 0;*/
		
		//Tester si mettre à jour la date remboursement ou pas
		if(confGen.getRecalcDateEcheanceAuDecais().equalsIgnoreCase("ne pas mettre a jours les dates")){
			System.out.println("Ne pas mettre à jour le calendrier de remboursement");
			List<Calpaiementdue> calDue = dm.getCalpaiementdues();
			for (Calpaiementdue calpaiementdue : calDue) {
				Calapresdebl calFinal = new Calapresdebl();
				calFinal.setDate(calpaiementdue.getDate());
				calFinal.setDemandeCredit(calpaiementdue.getDemandeCredit());
				calFinal.setMcomm(calpaiementdue.getMontantComm());
				calFinal.setMint(calpaiementdue.getMontantInt());
				calFinal.setMpen(calpaiementdue.getMontantPenal());
				calFinal.setMprinc(calpaiementdue.getMontantPrinc());
				calFinal.setPayer(false);
				calRembAprDebl.add(calFinal);
				
				//Total solde courant
				/*soldCourant += calFinal.getMprinc() + calFinal.getMint() - calFinal.getMpen();
				//total intérêt				
				totInter += calFinal.getMint();
				//Solde total
				double soldTotal = totInter + dm.getMontantDemande();
								
				//ajout calendrier due au Fihe credit
				FicheCredit fiche = new FicheCredit();
				fiche.setDate(calFinal.getDate()); 
				fiche.setPiece("");
				fiche.setTransaction("Tranche due");
				fiche.setPrincipale(calFinal.getMprinc()); 
				fiche.setInteret(calFinal.getMint()); 
				fiche.setPenalite(calFinal.getMpen());
				fiche.setSoldeCourant(soldCourant);
				fiche.setTotalPrincipal(dm.getMontantDemande());
				fiche.setTotalInteret(totInter);
				fiche.setTotalSolde(soldTotal); 
				fiche.setNum_credit(dm.getNumCredit());
				try {
					transaction.begin();
					em.persist(fiche);
					transaction.commit();
					em.refresh(fiche); 
					System.out.println("Fiche credit enregitré");
					
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				
			}
		}else if(confGen.getRecalcDateEcheanceAuDecais().equalsIgnoreCase("mettre a jours les dates")){
			System.out.println("mettre à jour le calendrier de remboursement");
			String codeClient = "";
			String codeGroupe = "";
			if(dm.getIndividuel() != null){
				codeClient = dm.getIndividuel().getCodeInd();
			}
			if(dm.getGroupe() != null){
				codeGroupe = dm.getGroupe().getCodeGrp();
			}
			List<CalView> lisCalView = demCredit(codeClient, codeGroupe, date, montant, dm.getTaux()
					, dm.getNbTranche(), dm.getTypeTranche(), dm.getDiffPaie(), dm.getModeCalculInteret());
			for (CalView calView : lisCalView) {
				Calapresdebl calFinal = new Calapresdebl();
				calFinal.setDate(calView.getDate());
				calFinal.setDemandeCredit(dm);
				calFinal.setMcomm(calView.getMontantComm());
				calFinal.setMint(calView.getMontantInt());
				calFinal.setMpen(calView.getMontantPenal());
				calFinal.setMprinc(calView.getMontantPrinc());
				calFinal.setPayer(false);
				calRembAprDebl.add(calFinal);	
				
				//Total solde courant
				/*soldCourant += calView.getMontantPrinc() + calView.getMontantInt() - calView.getMontantPenal();
				//total intérêt				
				totInter += calView.getMontantInt();
				//Solde total
				double soldTotal = totInter + dm.getMontantDemande();
								
				//ajout calendrier due au Fihe credit
				FicheCredit fiche = new FicheCredit();
				fiche.setDate(calView.getDate()); 
				fiche.setPiece("");
				fiche.setTransaction("Tranche due");
				fiche.setPrincipale(calView.getMontantPrinc()); 
				fiche.setInteret(calView.getMontantInt()); 
				fiche.setPenalite(calView.getMontantPenal());
				fiche.setSoldeCourant(soldCourant);
				fiche.setTotalPrincipal(dm.getMontantDemande());
				fiche.setTotalInteret(totInter);
				fiche.setTotalSolde(soldTotal); 
				fiche.setNum_credit(dm.getNumCredit());
				try {
					transaction.begin();
					em.persist(fiche);
					transaction.commit();
					em.refresh(fiche); 
					System.out.println("Fiche credit enregitré");
					
				} catch (Exception e) {
					e.printStackTrace();
				}	*/	
			}
		}
		
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
			if ((dm.getApprobationStatut().equalsIgnoreCase("Approuver") || dm.getApprobationStatut().equalsIgnoreCase(
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
					Caisse cptCaisse = new Caisse();
					if(!comptCaise.equalsIgnoreCase("")){
						System.out.println("Compte caisse");
						cptCaisse = em.find(Caisse.class, comptCaise);
						String cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
						accCred = CodeIncrement.getAcount(em, cptC);//cptCaisse.getAccount(); 
						System.out.println("Compte crédité "+accCred.getNumCpt());
						decais.setCptCaisseNum(cptC);
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
						
					}else if(typePaie.equalsIgnoreCase("mobile")){
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						credit1.setAccount(accCred);
						
						double sds = accCred.getSoldeProgressif() - dm.getMontantApproved();
						credit1.setSolde(sds);
						accCred.setSoldeProgressif(sds);
						
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
					
					//Fihe credit
					FicheCredit fiche = new FicheCredit();
					fiche.setDate(date); 
					fiche.setPiece(piece);
					fiche.setTransaction("Decaissement");
					fiche.setPrincipale(montant); 
					fiche.setInteret(0); 
					fiche.setPenalite(0);
					fiche.setSoldeCourant(0);
					fiche.setTotalPrincipal(montant);
					fiche.setTotalInteret(0);
					fiche.setTotalSolde(montant); 
					fiche.setNum_credit(dm.getNumCredit());		
					
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
					em.persist(debit);
					em.persist(credit1);
					em.persist(fiche);
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
					em.refresh(fiche); 
					
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

	//Calucl difference entre la date au calendrier de remboursement et la date de paiement échéance
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
	@SuppressWarnings({"unused"})
	@Override
	public boolean saveRemboursement(String numCredit, int userId, String date,
			double montant, String piece,String typePaie,String numTel,String numCheque, String cmptCaisse) { 

		//Instance de crédit en question
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit); 
		//Instance de l'utilisateur qui enregistre l'opération
		Utilisateur ut = em.find(Utilisateur.class, userId);
		
		//Instance de configuration GrandLivre crédit
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
		//Instance de configuration pénalité
		ConfigPenaliteCredit confPenalit = dm.getProduitCredit().getConfigPenaliteCredit();
		int pen = 5;
		
		//Remboursement à partir d'un N° compte epargne 
		//String numCompteEp;
		
		//valeur de retour		
		boolean retour = false;  
	
		Caisse cptCaisse = new Caisse();
		String cptC = "";		
		if(!cmptCaisse.equalsIgnoreCase("")){
			cptCaisse = em.find(Caisse.class, cmptCaisse);
			cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
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
					
					cals = (Calapresdebl) query.getSingleResult();
					
					// Recupère le montant principal et interet à rembouser
					double princ = cals.getMprinc();
					double inter = cals.getMint();
					int rowId = cals.getRowId();
					
					System.out.println("calendrier numero: "+ rowId);
					System.out.println("Principal total: " + princ);
					System.out.println("Interet total: " + inter);
					
					double restMontant = 0.0;						
						
						// Montant a remboursé
						double montRembourser = 0;							
						// Recuperer le montant reste au dernier remboursement			
						Query qr = em.createQuery("SELECT r.restaPaie FROM Remboursement r JOIN r.demandeCredit numDem"
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
							System.out.println("montant reste au dernière paiement : "+ derRestPaie);
						}
						System.out.println("montant à rembourser : "+ montRembourser);
						
						//Calcul remboursement
						//Intérêt remboursé
						double interetRemb = 0.0;
						//Principal remboursé
						double principaleRem = 0.0;
						//reste de paiement principal
						double restPaiePrinc = 0.0;
						//reste de paiement intérêt
						double restInt = 0.0;
						
						//Instancie la configuration individuel ou la configuration groupe selon le client en question
						ConfigCreditGroup confGroupe = null;
						ConfigCreditIndividuel confInd = null;
						boolean cInd = false;
						boolean cGrp = false;
						
						if(dm.getIndividuel() != null){
							confInd = dm.getProduitCredit().getConfigCreditIndividuel();
							if(confInd.getPariementPrealableInt() == true)
								cInd = true;
							else
								cInd = false;
						}
						if(dm.getGroupe() != null){
							confGroupe = dm.getProduitCredit().getConfigCreditGroupe();
							if(confGroupe.isPaiePrealableInt() == true)
								cGrp = true;
							else 
								cGrp = false;
								
						}
						
						if(cInd == true || cGrp == true){
							System.out.println("paiement préalable des intérêts");
							//Si le paiement de l'intérêt est prioritaire
							//On soustraire le montant total remboursé par montant intéret dans le calendrier de remboursement
							double restPaieInt = montRembourser - inter;						
							System.out.println("Reste paiement intérêts: "+restPaieInt);
							//S'il y a de reste aprés soustraction par intérêt 
							if (restPaieInt > 0.0) {
								
								interetRemb = inter;
								inters = interetRemb;
								
								restPaiePrinc = restPaieInt - princ;
								System.out.println("Reste paiement principal: "+restPaiePrinc);
								if (restPaiePrinc >= 0.0) {
									//Si reste de paiement principal superieur ou égal à 0
									principaleRem = princ;
									//reste paiement
									restMontant = restPaiePrinc; 
									cals.setPayer(true);	
									System.out.println("Reste paiement: "+restMontant);
								} else if (restPaiePrinc < 0.0) {
									principaleRem = restPaieInt; 
									//calcul pénalité
									double montantPenal = 0;
									
									if(confPenalit.getModeCalcul().equalsIgnoreCase("montant fixe")){
										montantPenal = confPenalit.getMontantFixe();
									}
									if(confPenalit.getModeCalcul().equalsIgnoreCase("pourcentage")){
										montantPenal = (restPaiePrinc*(-1)*confPenalit.getPourcentage())/100;
									}
									System.out.println("penalite: "+montantPenal);
									cals.setMpen((float)montantPenal);
									cals.setPayer(true);
									restMontant = restPaiePrinc + (montantPenal*(-1));
									System.out.println("Reste paiement: "+restMontant);
								}
								
								capitals = principaleRem;						
								
								
							} else if (restPaieInt < 0.0) {
								System.out.println("montant inferieur au interet due");
								interetRemb = montRembourser;
								inters = montRembourser;
								//calcul pénalité
								double montantPenal = 0;
								
								if(confPenalit.getModeCalcul().equalsIgnoreCase("montant fixe")){
									montantPenal = confPenalit.getMontantFixe();
								}
								if(confPenalit.getModeCalcul().equalsIgnoreCase("pourcentage")){
									montantPenal = (restPaieInt*(-1)*confPenalit.getPourcentage())/100;
								}									
								cals.setMpen((float)montantPenal);
								cals.setPayer(true);
								restMontant = restPaieInt + (montantPenal*(-1));
								System.out.println("Interêt payer: "+inters);
							}				
							
						}			
						else if(cInd == false || cGrp == false){
							System.out.println("Paiement de capital prioritaire");
							//Si le paiement de capital est prioritaire
							//On soustraire le montant total remboursé par montant du principal dans le calendrier de remboursement
							double restPrinc = montRembourser - princ;						
							System.out.println("Reste de Paiement de capital: "+restPrinc);
							//S'il y a de reste aprés soustraction par intérêt 
							if (restPrinc > 0.0) {
								
								principaleRem = princ;
								capitals = princ;
								
								restInt = restPrinc - inter;
								System.out.println("Reste de Paiement intérêt: "+restInt);
								if (restInt >= 0.0) {
									//Si reste de paiement principal superieur ou égal à 0
									interetRemb = inter;
									//reste paiement
									restMontant = restInt; 
									cals.setPayer(true);
									System.out.println("intérêt remboursé: "+interetRemb);
									System.out.println("Reste montant: "+restMontant);
									
								} else if (restInt < 0.0) {
									interetRemb = restInt; 
									//calcul pénalité
									double montantPenal = 0;
									
									if(confPenalit.getModeCalcul().equalsIgnoreCase("montant fixe")){
										montantPenal = confPenalit.getMontantFixe();
									}
									if(confPenalit.getModeCalcul().equalsIgnoreCase("pourcentage")){
										montantPenal = (restInt*(-1)*confPenalit.getPourcentage())/100;
									}									
									cals.setMpen((float)montantPenal);
									cals.setPayer(true);
									restMontant = restInt + (montantPenal*(-1));
									System.out.println("pénalité: "+montantPenal);
									System.out.println("Reste montant: "+restMontant);
								}								
								inters = interetRemb;					
								
								
							} else if (restPrinc < 0.0) {
								System.out.println("montant inferieur au principal due");
								principaleRem = montRembourser;
								capitals = montRembourser;
								//calcul pénalité
								double montantPenal = 0;
								
								if(confPenalit.getModeCalcul().equalsIgnoreCase("montant fixe")){
									montantPenal = confPenalit.getMontantFixe();
								}
								if(confPenalit.getModeCalcul().equalsIgnoreCase("pourcentage")){
									montantPenal = (restPrinc*(-1)*confPenalit.getPourcentage())/100;
								}									
								cals.setMpen((float)montantPenal);
								cals.setPayer(true);
								restMontant = restPrinc + (montantPenal*(-1));
								System.out.println("pénalité: "+montantPenal);
								System.out.println("Reste montant: "+restMontant);
							}
						}

					// recupérer le dernier Principale total, intérêt total, solde total  du crédit
					double principalTotal = dm.getPrincipale_total();
					double interetTotal = dm.getInteret_total();
					double soldeTotal = dm.getSolde_total();
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
					
					//ajout calendrier due au Fihe credit
					FicheCredit fiche = new FicheCredit();
					fiche.setDate(date); 
					fiche.setPiece(piece);
					fiche.setTransaction("Remboursement");
					fiche.setPrincipale(principaleRem); 
					fiche.setInteret(interetRemb); 
					fiche.setPenalite(0);
					fiche.setSoldeCourant(0);
					fiche.setTotalPrincipal(montant);
					fiche.setTotalInteret(0);
					fiche.setTotalSolde(montant); 
					fiche.setNum_credit(dm.getNumCredit());	

					// Modification sur l'entité DemandeCredit
					dm.setSolde_total(soldeTotal);
					dm.setPrincipale_total(principalTotal);
					dm.setInteret_total(interetTotal);
					
					try {
						transaction.begin();
						em.merge(cals);
						em.merge(dm);
						em.persist(saveRemb);
						em.persist(fiche); 
						transaction.commit();
						em.refresh(saveRemb);
						em.refresh(dm);
						em.refresh(cals); 
						em.refresh(fiche); 
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
					debit.setDescr("Remboursement capital du crédit "
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
					credit1.setDescr("Remboursement capital du crédit "
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
					System.out.println("Remboursement en retard");
					
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
					
					info = "Remboursement en retard";
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
					System.out.println("Remboursement en retard");
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
	public List<Caisse> findAllComptCaisse() {
		TypedQuery<Caisse> query = em.createQuery("SELECT c FROM Caisse c",Caisse.class);
		
		List<Caisse> result = query.getResultList();
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
	
	/*********************************************************************************************************************/
								/*******************RAPPORT CREDIT********************/
	/*********************************************************************************************************************/
	

	//Rapport garant crédit
	@Override
	public List<Garant> getGarantCredit(String numCredit) {
		String sql = "select g from Garant g join g.demandeCredit d where d.numCredit='"+numCredit+"'";
		TypedQuery<Garant> query = em.createQuery(sql,Garant.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//Rapport garantie crédit
	@Override
	public List<GarantieCredit> getGarantieCredit(String numCredit) {
		String sql = "select g from GarantieCredit g join g.demandeCredit d where d.numCredit='"+numCredit+"'";
		TypedQuery<GarantieCredit> query = em.createQuery(sql,GarantieCredit.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
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
	public boolean configFraisCredits(ConfigFraisCredit configFraisCredit,	String idProduit) {
		
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
					+ idProduit+" bien enregistré");
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
			return false;
		}
	}
	
	/***
	 * CONFIGURATION FRAIS CREDIT POUR GROUPE
	 * ***/

	@Override
	public boolean configFraisCreditGroupes(ConfigFraisCreditGroupe confFraisGroupe, String idPr) {
		
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
					+ idPr+" bien enregistré");
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Configuration garantie crédit
	 * **/
	@Override
	public boolean configGarantiCredit(ConfigGarantieCredit configGarCredit,String idProduitEpargne,String idProduit) {
	
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		ProduitEpargne pdEp = em.find(ProduitEpargne.class, idProduitEpargne);
		Query q = em.createQuery("select i from ConfigCredit i");
		ConfigCredit conf = (ConfigCredit) q.getSingleResult();
		
		try {
			configGarCredit.setProduitEpargne(pdEp); 
			pdtCred.setConfigCredit(conf);
			pdtCred.setConfigGarantieCredit(configGarCredit);
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("Configuration Grantie crédit du produit "
					+ idProduit);
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
			return false;
		}
	}

	/**
	 * Configuration GL crédit
	 * **/ 
	@Override
	public boolean configGLCredit(ConfigGlCredit configGLCredit, String idProduit) {
	
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
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
			return false;
		}
	}
	

	@Override
	public boolean configGL2Credit(ConfigDeclasseCredit configGLCredit,
			String idProduit) {
		ProduitCredit pdtCred = em.find(ProduitCredit.class, idProduit);
		try {
			pdtCred.setConfigDeclasse(configGLCredit); 
			
			transaction.begin();
			em.flush();
			transaction.commit();
			
			System.out.println("Configuration Grand Livre 2 du produit "+ idProduit+" bien enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/***
	 * CONFIG PENALITE
	 * ***/

	@Override
	public boolean configPenalite(ConfigPenaliteCredit confPen, String idProduit) {

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
			return true;
		} catch (Exception e) {
			System.err.println("Configuration non inséré " + e.getMessage());
			return false;
		}
	}

	//*********************************************************************
	//HISTORIQUE Remboursement
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
	
	//Test enregistrement calendrier
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
	public List<FicheCredit> listeFicheCredit(String numCred) {
		//Recuperation calendrier
		String sql = "select f from FicheCredit f where f.num_credit= '"+numCred+"' order by f.date asc";
		TypedQuery<FicheCredit> query = em.createQuery(sql,FicheCredit.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();	
	
		return null;
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

	//Information garantie view
	
	@Override
	public boolean addGarantieView(GarantieView garantie,String codeInd) {
		try {
			//Individuel ind = em.find(Individuel.class, codeInd); 
			
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);
			
			String ag = codeInd.substring(0, 2);//ind.getCodeInd()
			garantie.setNumCredit(ag + "/" + index);
			transaction.begin();
			em.persist(garantie);
			transaction.commit();
			em.refresh(garantie);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean modifierGarantieVIew(int id, GarantieView garantie) {
		GarantieView g = em.find(GarantieView.class, id);
		if(g!=null){
			try {
				g.setNomGarantie(garantie.getNomGarantie());
				g.setNumCredit(garantie.getNumCredit());
				g.setPourcentage(garantie.getPourcentage());
				g.setTypeGarantie(garantie.getTypeGarantie());
				g.setValeur(garantie.getValeur());
				transaction.begin();
				em.merge(g);
				transaction.commit();
				em.refresh(g); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean deleteGarantieVIew() {
		Query query = em.createNativeQuery("TRUNCATE TABLE Garantie_view");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table garantie view"
					+ " vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GarantieView> getAllGarantieView() {
		TypedQuery<GarantieView> query = em.createQuery("select g from GarantieView g",GarantieView.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	//Rapport solde restant dû
	@Override
	public List<AfficheSoldeRestantDu> getSoldeRestantDu(String date) {
		String sql1 ="select d from DemandeCredit d where d.approbationStatut='Demande decaissé' and d.dateDemande <= '"+date+"'";
		TypedQuery<DemandeCredit> quer = em.createQuery(sql1,DemandeCredit.class);
		if(!quer.getResultList().isEmpty())
		{
			List<DemandeCredit> dm = quer.getResultList();
			List<AfficheSoldeRestantDu> result = new ArrayList<AfficheSoldeRestantDu>();
			for (DemandeCredit demandeCredit : dm) {
				AfficheSoldeRestantDu aff = new AfficheSoldeRestantDu();
				String sql = "select max(r.dateRemb) from Remboursement r join r.demandeCredit d "
						+ " where d.numCredit='"+demandeCredit.getNumCredit()+"'";
				String dernierRemb = ""; 
				Query q = em.createQuery(sql);
				if(q.getSingleResult() != null)
					dernierRemb = (String) q.getSingleResult();
				
				aff.setNumCred(demandeCredit.getNumCredit());
				if(demandeCredit.getIndividuel() != null)
					aff.setNom(demandeCredit.getIndividuel().getNomClient()+" "+ demandeCredit.getIndividuel().getPrenomClient());
				if(demandeCredit.getGroupe() != null)
					aff.setNom(demandeCredit.getGroupe().getNomGroupe());
				aff.setMontantCredit(demandeCredit.getMontantDemande());
				aff.setMontantPrincipal(demandeCredit.getMontantDemande());
				aff.setMontantInteret(demandeCredit.getInteret());
				aff.setPricipalRestant(demandeCredit.getPrincipale_total());
				aff.setInteretRestant(demandeCredit.getInteret_total());
				aff.setPrincipalPayer(demandeCredit.getMontantDemande() - demandeCredit.getPrincipale_total());
				aff.setInteretPayer(demandeCredit.getInteret() - demandeCredit.getInteret_total());
				aff.setTotalRestant(demandeCredit.getSolde_total());
				aff.setDernierRemboursement(dernierRemb);
				
				result.add(aff);
			}		
			return result;
		}
		return null;
	}

	//Analyse portefeuille par agent de credit
	@Override
	public List<Agent> analysePortefeuille(String dateDeb, String dateFin) {
		//on va selectionner tout d'abord tous les agents de crédit
		List<Utilisateur> agent = new ArrayList<Utilisateur>();		
		String selctAgent = "select ut from Utilisateur ut join ut.fonction f where f.libelleFonction='Agent crédit'";
		TypedQuery<Utilisateur> query1 =  em.createQuery(selctAgent,Utilisateur.class);
		
		
		if(!query1.getResultList().isEmpty()){
			agent = query1.getResultList();
			//On va parcourrir chaque utilisateur
			
			List<Agent> result = new ArrayList<Agent>();
			for (Utilisateur utilisateur : agent) {
				
				//on va sélectionner tous les clients de l'agent en question
				String sql ="select d from DemandeCredit d join d.agent u where u.idUtilisateur='"+utilisateur.getIdUtilisateur()+"' "
						+ " and d.approbationStatut='Demande decaissé' and d.dateDemande between '"+dateDeb+"' and '"+dateFin+"'";			
				TypedQuery<DemandeCredit> quer2 = em.createQuery(sql,DemandeCredit.class);
				//on va faire le parcours de demande crédit
				if(!quer2.getResultList().isEmpty()){
					List<DemandeCredit> dm = quer2.getResultList();
					Agent ag = new Agent();
					List<ClientAgent> clients = new ArrayList<ClientAgent>();
					
					int tEcheance = 0;
					int eche = 0;
					
					for (DemandeCredit demandeCredit : dm) {
						ClientAgent aff = new ClientAgent();
						//On recupère les attribues necessaire à l'affichage
						aff.setNumCredit(demandeCredit.getNumCredit());
						if(demandeCredit.getIndividuel() != null)
							aff.setNomCient(demandeCredit.getIndividuel().getNomClient()+" "+ demandeCredit.getIndividuel().getPrenomClient());
						if(demandeCredit.getGroupe() != null)
							aff.setNomCient(demandeCredit.getGroupe().getNomGroupe());
						aff.setMontantCredit(demandeCredit.getMontantDemande());
						aff.setMontantPrincipal(demandeCredit.getMontantDemande());
						aff.setMontantInteret(demandeCredit.getInteret());
						aff.setPricipalRestant(demandeCredit.getPrincipale_total());
						aff.setInteretRestant(demandeCredit.getInteret_total());
						aff.setPrincipalPayer(demandeCredit.getMontantDemande() - demandeCredit.getPrincipale_total());
						aff.setInteretPayer(demandeCredit.getInteret() - demandeCredit.getInteret_total());
						aff.setTotalRestant(demandeCredit.getSolde_total());
						
						//Select Montant decaisser && montant approuver
						double montDecs =(double) em.createQuery("select d.montantDec from Decaissement d join d.demandeCredit a "
								+ " where a.numCredit = '"+demandeCredit.getNumCredit()+"'").getSingleResult();						
						//select total echeance
						long totEcheance = (long) em.createQuery("select count(c) from Calapresdebl c join c.demandeCredit d "
								+ " where d.numCredit = '"+demandeCredit.getNumCredit()+"'").getSingleResult();
						//select echeance achevé
						long echeance = (long) em.createQuery("select count(r) from Remboursement r join r.demandeCredit d "
								+ " where d.numCredit = '"+demandeCredit.getNumCredit()+"'").getSingleResult();
						aff.setMontantDecaisser(montDecs);
						aff.setEcheanceAchever((int) echeance);
						aff.setTotalEchenance((int) totEcheance); 
						tEcheance += totEcheance;
						eche += echeance;
						clients.add(aff);
					}
					
					ag.setCodeAgent(utilisateur.getIdUtilisateur());
					ag.setFonction(utilisateur.getFonction().getLibelleFonction());
					ag.setNoomAgent(utilisateur.getNomUtilisateur());
					//ag.setTel(utilisateur.getTelUser());
					ag.setClients(clients);	
					ag.setTauxRemb(0);
					//calcul taux de risque
					int taux = (eche * 100)/tEcheance;					
					ag.setTauxArrieres(taux);
					result.add(ag);
				}
				
			}
			return result;
		}
		
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
		//Rapport solde restant dû
	@Override
	public List<DemandeCredit> getSoldeRestantDu(String dateDeb, String dateFin) {
		
		return null;
	}
*/
	
}
