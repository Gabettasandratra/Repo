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
import mg.fidev.model.CompteEpargne;
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
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.JourFerier;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.Remboursement;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.CreditService;
import mg.fidev.utils.AfficheSoldeRestantDu;
import mg.fidev.utils.Agent;
import mg.fidev.utils.ClientAgent;
import mg.fidev.utils.CodeIncrement;

@WebService(name = "creditProduitService", targetNamespace = "http://fidev.mg.creditProduitService", serviceName = "creditProduitService", portName = "creditServicePort", endpointInterface = "mg.fidev.service.CreditService")
public class CreditServiceImpl implements CreditService {  
	
	
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	public static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
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
			int lastIndex = getLastIndexPdt();
			String index = String.format("%03d", ++lastIndex);

			ProduitCredit pdtCredit = new ProduitCredit("L" + index, etat, nomProdCredit); 

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
		TypedQuery<DemandeCredit> q = em.createQuery("select c from DemandeCredit c", DemandeCredit.class);
		List<DemandeCredit> l = q.getResultList();
		return l;
	}
	
	/**
	 * Liste des Produits Credit
	 * **/
	public List<ProduitCredit> findAllCredit() {
		TypedQuery<ProduitCredit> query = em.createQuery("select c from ProduitCredit c", ProduitCredit.class);
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
	
	//Chercher crédit par mot clé et situation 
	@Override
	public List<DemandeCredit> chercherCredit(String mc,String statu) {
		TypedQuery<DemandeCredit> query = em.createQuery("select d from DemandeCredit d where d.numCredit like :x"
				+ " and (d.approbationStatut =:ap or d.approbationStatut =:ax)", DemandeCredit.class);
		query.setParameter("x", mc+"%");
		query.setParameter("ap",statu);
		query.setParameter("ax","Rééchelonner");
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//chercher crédit par stituation
	@Override
	public List<DemandeCredit> findSituationCredit(boolean ap, boolean comm) {
		TypedQuery<DemandeCredit> query = em.createQuery("select d from DemandeCredit d where d.approuver =:x"
				+ " and d.commission =:y", DemandeCredit.class);
		query.setParameter("x", ap);
		query.setParameter("y",comm);
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
					" select c from ProduitCredit c where c.nomProdCredit like :mc", ProduitCredit.class);
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
			TypedQuery<DemandeCredit> query = em.createQuery("select d from DemandeCredit d where d.approbationStatut like :mc", DemandeCredit.class);
			query.setParameter("mc", "%"+mc+"%");  
			result = query.getResultList();		
		}else{
			TypedQuery<DemandeCredit> query = em.createQuery("select d from DemandeCredit d where d.approbationStatut like :mc"
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
			produit.setNomProdCredit(p.getNomProdCredit());
			produit.setEtat(p.getEtat());
			transaction.begin();
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
		String res = "";
		
		transaction.begin();
		ProduitCredit p = findOne(id);
		if(p != null){
			em.remove(p);
			res = "Suppression avec succes!!!";
		}		
		else {
			res = "Aucun produit de ce numéro";			
		}			
		transaction.commit();
		System.out.println(res); 
		return res;
	}
	
	
	/******************************************************** DEMANDE CREDIT *************************************************************************/
	
	//Modifier demande crédit
	@Override
	public boolean updateDemandeCredit(String numCredit,DemandeCredit demande) {
		
		//DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
				
		return false;
	}

	//Supprimer un crédit
	@Override
	public boolean deleteCredit(String numCred) {
		DemandeCredit dm = em.find(DemandeCredit.class, numCred);
		try {
			//Supprimer l'enregistrement du crédit au table fiche crédit
			//Query query = em.createNativeQuery("delete fiche_credit where num_credit="+dm.getNumCredit());
			String sql = "select f from FicheCredit f where f.num_credit='"+dm.getNumCredit()+"'";
			TypedQuery<FicheCredit> query = em.createQuery(sql,FicheCredit.class);
			if(!query.getResultList().isEmpty()){
				for (FicheCredit fiche : query.getResultList()) {
					try {
						transaction.begin();
						em.remove(fiche); 
						transaction.commit();
						System.out.println("Fiche crédit n°: "+fiche.getNum_credit()+" supprimé");
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
			}
			transaction.begin();
			em.remove(dm);
			transaction.commit();
			System.out.println("Crédit supprimé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur suppression crédit");
			return false;
		}
	}
	
	//Get calendrier crédit
	@Override
	public List<Calpaiementdue> getCalendrierCredit(String numCred) {
		String sql = "select c from Calpaiementdue c join c.demandeCredit d where d.numCredit='"+numCred+"' order by c.date asc";
		TypedQuery<Calpaiementdue> query = em.createQuery(sql, Calpaiementdue.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//Modification calendrier crédit
	@Override
	public Calpaiementdue updateCalendrierCredit(Calpaiementdue cal) {
		try {
			Calpaiementdue c = em.find(Calpaiementdue.class, cal.getRowId());
			c.setDate(cal.getDate());
			c.setMontantPrinc(cal.getMontantPrinc());
			c.setMontantInt(cal.getMontantInt());
			c.setMontantComm(cal.getMontantComm());
			c.setMontantPenal(cal.getMontantPenal());
			transaction.begin();
			em.merge(c);
			transaction.commit();
			System.out.println("Modification calendrier réussit!");
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Methode pour recuperer le dernier index d'un num crédit
	 **/

	public static int getLastIndex(String codeInd) {
		String code = codeInd.substring(0, 2);
		String sql = "select count(*) from demande_credit where left(num_credit, 2) = '"+code+"'";// String num_crd
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	
	//SAISIE information demande crédit
		//Générer calendrier remboursement d'une demande crédit groupe
		@Override
		public List<CalView> demCredit(String codeInd, String codeGrp,
				String date_dem, double montant, double tauxInt, int nbTranche,
				String typeTranche, int diffPaie, String modCalcul) {

			List<CalView> resultat = CodeIncrement.getCalendrierPaiement
					(codeInd, codeGrp, date_dem, montant, tauxInt, nbTranche, typeTranche, diffPaie, modCalcul);
			
			for (CalView cal : resultat) {
				try {
					transaction.begin();
					em.persist(cal);
					transaction.commit();
					System.out.println("calendrier enregistrer");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
					
			return resultat;
		}
		
		//Modification calendrier view
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
			String sql = "select c from CalView c order by c.date asc";
			return em.createQuery(sql,CalView.class).getResultList();
		}

		//Test calendrier view
		@Override
		public List<CalView> testCal(){
			List<CalView> cal = getAllCalView();
			for (CalView calView : cal) {
				System.out.println("id: "+calView.getRowId());
				System.out.println("date: "+calView.getDate());
				System.out.println("principal: "+calView.getMontantPrinc());
				System.out.println("intérêt: "+calView.getMontantInt());
				System.out.println("num crédit: "+calView.getNumCred());
				
			}
			return null;
			
		}
		
		//Supprimer données au calendrier view
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
	 * SAVE DEMANDE CREDIT Individuel (Opérationnel)
	 * ***/
	@Override
	public boolean saveDemandeCreditIndividuel(String idProduit, String codeInd,DemandeCredit demande, int idAgent,
			String codeGar1, String codeGar2, String codeGar3, int user_id,
			double tauxGar1, double tauxGar2, double tauxGar3) {
	
		//Pour initialiser les intérêt, soldCourant, total intérêt
		FicheCredit ficheCredit = new FicheCredit();			
		//Client
		Individuel ind = em.find(Individuel.class, codeInd);	
		
		if(ind != null){
			int lastIndex = getLastIndex(codeInd);
			String index = String.format("%05d", ++lastIndex);		
			String ag = ind.getCodeInd().substring(0, 2);
			
			//Information de demande credit
			//Assignation numéro crédit
			demande.setNumCredit(ag + "/" + index);
			//Initialisation intérêt 
			double interet = 0;	
			for (CalView view : getAllCalView()) {
				interet += view.getMontantInt();
			}			
			//Importation garatie from garantie view
			List<GarantieCredit> garaties = new ArrayList<GarantieCredit>();
			List<GarantieView> listGaraties = getAllGarantieView();
			if(listGaraties != null){
				for (GarantieView garantieView : listGaraties) {
					GarantieCredit gar = new 
							GarantieCredit(garantieView.getTypeGarantie(), garantieView.getNomGarantie(), garantieView.getValeur(),
									garantieView.getPourcentage(), false, "", demande);
	
					garaties.add(gar);
				}
				//Enregistrement de la garantie
				demande.setGarantieCredits(garaties);				
			}			
			
			demande.setIndividuel(ind);
			demande.setUtilisateur(em.find(Utilisateur.class, idAgent));//Utilisateur de saisie
			demande.setProduitCredit(em.find(ProduitCredit.class, idProduit));//Produit crédit
			demande.setNbCredit(CodeIncrement.nombreCreditInd(em, ind.getCodeInd()));
			demande.setAgent(em.find(Utilisateur.class, idAgent));//Agent de crédit
			demande.setApprobationStatut("Approbation en attente");
			demande.setInteret(interet); 
				
			//Vider table garantie view
			deleteGarantieVIew();
			
			//Enregistrement Fiche credit
			FicheCredit f = new FicheCredit(demande.getDateDemande(), "Demande crédit", "", demande.getMontantDemande(), interet,
					0, 0, demande.getMontantDemande(), 0, demande.getMontantDemande(), demande.getNumCredit());
					
			System.out.println("information demande crédit prête");
			try {			
				transaction.begin();
				em.persist(demande);
				em.persist(f);
				transaction.commit();
				em.refresh(demande);
				em.refresh(f);
				
				//Initialisation solde courant et total de l'intérêt, qui sont utilisés pour l'enregistrement de fiche crédit
				double soldCourant = ficheCredit.getSoldeCourant();
				double totInter = ficheCredit.getInteret();		
				//Récuperation calendrier due
				for (CalView calView : getAllCalView()) {
					try {// new
							// SimpleDateFormat("yyyy-MM-dd").parse(calView.getDate()
						Calpaiementdue cald = new Calpaiementdue(calView.getDate(), calView.getMontantComm(), 
								calView.getMontantInt(), calView.getMontantPenal(), calView.getMontantPrinc(), demande);
						
						// Total solde courant
						soldCourant += calView.getMontantPrinc() + calView.getMontantInt() - calView.getMontantPenal();
						// total intérêt
						totInter += calView.getMontantInt();
						// Solde total
						double soldTotal = totInter
								+ demande.getMontantDemande();

						// Ajout calendrier due au Fihe credit
						FicheCredit fiche = new FicheCredit(calView.getDate(), "Tranche due", "", calView.getMontantPrinc(),
								calView.getMontantInt(), calView.getMontantPenal(), soldCourant,
								demande.getMontantDemande(), totInter, soldTotal, demande.getNumCredit());

						transaction.begin();
						em.persist(fiche);
						em.persist(cald);
						transaction.commit();
						em.refresh(fiche);
						em.refresh(cald);
						System.out.println("Fiche credit enregitré");

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				//Vider table calendrier view
				deleteCalendrier();
				
				//Garant crédit			
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
	
	//Récuperer données de la table créditMembreView
	@Override
	public List<CrediGroupeView> getAllCrediGroupeView() {
		String sql = "select c from CrediGroupeView c";
		return em.createQuery(sql,CrediGroupeView.class).getResultList();
	}
	
	//Enregistrement demande crédit groupe
	@Override
	public boolean saveDemandeCreditGroupe(String idProduit, String codeGrp,
			DemandeCredit demande, int idAgent, int user_id) {
		
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
			
			double interet = 0;
			for (CalView calView : getAllCalView()) {
				interet += calView.getMontantInt();
			}
						
			// Infomation garantie crédit
			// Importation garatie from garantie view
			List<GarantieCredit> garaties = new ArrayList<GarantieCredit>();
			List<GarantieView> listGaraties = getAllGarantieView();
			if (listGaraties != null) {
				for (GarantieView garantieView : listGaraties) {
					GarantieCredit gar = new 
							GarantieCredit(garantieView.getTypeGarantie(), garantieView.getNomGarantie(), garantieView.getValeur(),
									garantieView.getPourcentage(), false, "", demande);
					
					garaties.add(gar);
				}
				// Enregistrement de la garantie
				demande.setGarantieCredits(garaties);
			}
			
			// Vider table garantie view
			deleteGarantieVIew();

			//Enregistrement Fiche credit
			FicheCredit f = new FicheCredit(demande.getDateDemande(), "Demande crédit", "", demande.getMontantDemande(), interet,
					0, 0, demande.getMontantDemande(), 0, demande.getMontantDemande(), demande.getNumCredit());
							
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
			//Utilisateur de saisie
			demande.setUtilisateur(em.find(Utilisateur.class, user_id));
			//Produit crédit
			demande.setProduitCredit(em.find(ProduitCredit.class, idProduit));
			demande.setNbCredit(CodeIncrement.nombreCreditGrp(em, grp.getCodeGrp()));
			//Agent crédit
			demande.setAgent(em.find(Utilisateur.class, idAgent));
			demande.setApprobationStatut("Approbation en attente");
			demande.setMontantMembres(crediMembre); 
			demande.setInteret(interet);
			
			System.out.println("information demande crédit groupe prête");
			try {				
				transaction.begin();
				em.persist(demande);
				em.persist(f);
				transaction.commit();				
				System.out.println("Demande crédit de "
						+ demande.getNumCredit() + " enregistré");		
				
				em.refresh(demande);
				em.refresh(f);
				
				// Récuperation calendrier due
				double soldCourant = ficheCredit.getSoldeCourant();
				double totInter = ficheCredit.getInteret();
				for (CalView calView : getAllCalView()) {
					Calpaiementdue cald = new 
							Calpaiementdue(calView.getDate(), calView.getMontantComm(), calView.getMontantInt(), 
									calView.getMontantPenal(), calView.getMontantPrinc(), demande);
					
					
					// Total solde courant
					soldCourant += calView.getMontantPrinc()
							+ calView.getMontantInt() - calView.getMontantPenal();
					// total intérêt
					totInter += calView.getMontantInt();
					// Solde total
					double soldTotal = totInter + demande.getMontantDemande();

					// ajout calendrier due au Fihe credit
					FicheCredit fiche = new 
							FicheCredit(calView.getDate(), "Tranche due", "", calView.getMontantPrinc(), calView.getMontantInt(),
									calView.getMontantPenal(), soldCourant, demande.getMontantDemande(), 
									totInter, soldTotal, demande.getNumCredit());
					
					try {
						transaction.begin();
						em.persist(cald);
						em.persist(fiche);
						transaction.commit();
						em.refresh(cald);
						em.refresh(fiche);
						System.out.println("Fiche credit groupe enregitré");

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// Vider table calendrier view
				deleteCalendrier();
				return true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}

		}
		return false;
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

	//Enregistrement commission crédit
	@Override
	public boolean insertCommission(boolean cash,String paye,String date, String piece,double commission,double papeterie,
			String numCredit,int userId, String nomCptCaisse) {

		//Instance des classes necessaires
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);		
		//valeur de retour
		boolean result = false;
		
		Individuel ind = null;
		Groupe grp = null;
				
		//CHARGE LES CONFIGURATIONS GL DU CREDIT
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
		
		//pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		//Instance du compte caisse
		Caisse cptCaisse = em.find(Caisse.class, nomCptCaisse);
				
		//Total debité
		double debitTot = 0.0;
		double commissions = 0.0;
		double papeteries = 0.0;
		double fraisDevel = 0.0;
		double FraisRefinance = 0.0;
		
		//si c'est un crédit individuel
		if(dm.getIndividuel() != null ){//confFrais.isIndOuGroupe() == true
			System.out.println("Paiement commission crédit individuel");
			
			ind = dm.getIndividuel();
			
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
			
			grp = dm.getGroupe();
			
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
		
		//Vérification du statut crédit
		if (dm.isCommission() == true) {			
			System.out.println("le Paiement commission de ce crédit est déjà fait");
			
		} else {

			try {
				
				dm.setApprobationStatut(paye);
				dm.setCommission(true); 
				
				//Insertion au GrandLivre
				//compte débité total commission + papeterie
				String cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
				
				System.out.println("Compte débit "+cptC);
				Account account = CodeIncrement.getAcount(em, cptC);	
				
				ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Total commision crédit " +numCredit, piece,
						0, debitTot, ut, ind, grp, dm, null, account);
		
				//compte crédité commission					
				Account crdCom = CodeIncrement.getAcount(em, confGl.getCptCommCredit());						
				System.out.println("Compte Crédit com "+crdCom.getNumCpt());
				
				ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Commision crédit " +numCredit, piece,
						commissions, 0, ut, ind, grp, dm, null, crdCom);
				
				//compte crédité Papeterie						
				Account crdPap = CodeIncrement.getAcount(em, confGl.getCptPapeterie());						
				System.out.println("Compte Crédit papeterie "+crdPap.getNumCpt());	
				
				ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Frais Papéterie crédit " +numCredit, piece,
						papeteries, 0, ut, ind, grp, dm, null, crdPap);
			
				//Information Commission Crédit
				CommissionCredit paie = new 
						CommissionCredit(indexTcode, cash, 0, date, commissions, paye,
								piece, papeteries, (float) fraisDevel, (float)FraisRefinance, dm, ut);
				transaction.begin();
				em.flush();
				em.persist(paie);
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
	
	//Enregistrement approbation crédit
	@Override
	public String saveApprobation(String numCredit, String Appby,String dateApp, String descApp, double montantApp,
			String stat_demande) {

		DemandeCredit dmd = em.find(DemandeCredit.class, numCredit);
		String result = "";
		// Vérifie si le numero de crédit n'est pas null
		if (dmd != null) {
			
			if(dmd.isApprouver() == false){
				
				boolean verif = false;
				
				if(dmd.getProduitCredit().getConfigFraisCredit().isAvantOuApres().equalsIgnoreCase("avant approbation")
					|| dmd.getProduitCredit().getConfFraisGroupe().isAvantOuApres().equalsIgnoreCase("avant approbation")){
					if(dmd.isCommission() == false){
						result = "Vous devez payer la commission avant l'approbation";
						verif = false;
					}else
						verif = true;
				}else 
					verif = true;
				
				if(verif == true){
					if (dmd.getMontantApproved() == 0 && (montantApp <= dmd.getMontantDemande())) {
						
						dmd.setMontantApproved(montantApp);
						dmd.setApprobationStatut(stat_demande); 
						dmd.setDateApprobation(dateApp); 
						dmd.setApprouver(true);
						
						//Enregistrement dans la table approbation
						ApprobationCredit ap = new ApprobationCredit(dateApp, Appby, descApp, montantApp, dmd);					
						
						//Fiche credit
						FicheCredit fiche = new FicheCredit(dateApp, "Approbation", "", dmd.getMontantDemande(), dmd.getInteret(), 
								0, 0, dmd.getMontantDemande(), 0, dmd.getMontantDemande(), dmd.getNumCredit());
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
							
						} catch (Exception e) {
							System.err.println("Erreur approbation "+ e.getMessage());
							result = "Erreur enregistrement approbation";
						}
					}else {
						System.err.println("Montant approuvé null");
						result = "Montant approuvé supérieur au montant demandé ("+dmd.getMontantDemande()+")";
					}
				}else{
					result = "Erreur approbation";
				}
					
			}else {
				result = "Crédit déjà approuvé!!!";
			}

		} else {
			result = "Numéro Crédit non trouvé!!!";
		}
		return result;
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	/*********************************************** Décaissement ********************************************************/
		
	//Suppression de calendrier dues au fiche crédit
	static void deleteFiche(String numCred){
		String sql_fiche = "select f from FicheCredit f where f.transaction='Tranche due' "
				+ " and f.num_credit='"+numCred+"'";
		TypedQuery<FicheCredit> query_fiche = em.createQuery(sql_fiche,FicheCredit.class);
		for (FicheCredit ficheCredit : query_fiche.getResultList()) {
			try {
				transaction.begin();
				em.remove(ficheCredit); 
				transaction.commit();
				System.out.println("Tranche due fiche crédit n°: "+ficheCredit.getId()+" supprimé");
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	/***
	 * Enregistrement décaissement
	 * ***/
	@Override
	public String saveDecaisement(String date,String typePaie, double montant, double commission, double papeterie, 
			String piece, String numCheque, String numTel, String numCompte,String comptCaise,String numCredit, int userId) {
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit);
				
		ConfigGlCredit confGL = dm.getProduitCredit().getConfigGlCredit();
		ConfigGeneralCredit confGen = dm.getProduitCredit().getConfigGeneralCredit();
		
		///	pour incrémenter le tcode dans la table grandlivre
		String indexTcode = CodeIncrement.genTcode(em);
		//On supprime le calendrier dues du fiche crédit pour le mettre à jour après l'enregistrement
		deleteFiche(dm.getNumCredit());		
		//On met dans la liste les calendrier après deblocage
		//List<Calapresdebl> calRembAprDebl = new ArrayList<Calapresdebl>();		
		FicheCredit ficheCredit = new FicheCredit();
		double soldCourant = ficheCredit.getSoldeCourant();
		double totInter = ficheCredit.getInteret();
		
		//Recupération du code client
		String codeClient = "";
		String codeGroupe = "";
		Individuel ind = null;
		Groupe grp = null;
		if(dm.getIndividuel() != null){
			codeClient = dm.getIndividuel().getCodeInd();
			ind = dm.getIndividuel();
		}
		if(dm.getGroupe() != null){
			codeGroupe = dm.getGroupe().getCodeGrp();
			grp = dm.getGroupe();
		}
		
		String result = "";
		//Vérifie si le numero crédit est null ou pas
		if (dm != null) {
			//si les statuts du crédit sont approuver et commission payé
			if (dm.isApprouver() == true && dm.isCommission() == true) {

				try {
					//Total solde à décaisser = montant approuvé - (commission + papeterie)
					double soldeDecais = (dm.getMontantApproved() - (commission + papeterie));			

					//Montant d'interet par mois
					//double intTot = (dm.getMontantApproved() * dm.getInteret()) / 100;					
					//Solde Total = Montant Approuvé + Interet Total*/
					double soldeTotal = dm.getMontantApproved() + dm.getInteret();
					
					dm.setInteret_total(dm.getInteret());
					dm.setPrincipale_total(dm.getMontantApproved());
					dm.setSolde_total(soldeTotal);	
					dm.setApprobationStatut("Demande decaissé");
					
					//Insertion au GrandLivre        
					Account accCred = null;     
					Caisse cptCaisse = new Caisse();
					
					String type = "";
					String val = "";
					if(!comptCaise.equalsIgnoreCase("")){
						System.out.println("Compte caisse");
						cptCaisse = em.find(Caisse.class, comptCaise);
						String cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
						accCred = CodeIncrement.getAcount(em, cptC);//cptCaisse.getAccount(); 
						System.out.println("Compte crédité "+accCred.getNumCpt());
						
						type = "Cash";
						val = cptC;
					}					
					
					if(typePaie.equalsIgnoreCase("cheque")){
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						
						type = "Cheque";
						val = numCheque;
						
					}else if(typePaie.equalsIgnoreCase("mobile")){
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						
						type = "Mobile";
						val = numTel;
						
					}else if(typePaie.equalsIgnoreCase("epargne")){
						//Compte du client en question
						CompteEpargne cptEp = em.find(CompteEpargne.class, numCompte);						
			
						accCred = CodeIncrement.getAcount(em, confGL.getCptCheque());
						
						type = "Transfert épargne";
						val = numCompte;
						//Initialisation des informations de transaction
						TransactionEpargne trans = new TransactionEpargne(indexTcode, date,
								"Dépôt via Crédit numéro "+dm.getNumCredit(), montant, piece, "DE", "transfert épargne", dm.getNumCredit(),
								0, 0, 0, null, cptEp);  	
						
						double tr = cptEp.getSolde() + montant;
						cptEp.setSolde(tr);
						
						try {  
							transaction.begin();
							em.persist(trans);
							em.merge(cptEp);
							transaction.commit();
							em.refresh(cptEp); 
							em.refresh(trans); 
							System.out.println("Depôt du compte "+cptEp.getNumCompteEp() +" a été effectuer");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
					
					Decaissement decais = new 
							Decaissement(indexTcode, date, montant, piece, type, val, commission, papeterie, 0, dm, ut);
					
					//Fihe credit
					FicheCredit f = new FicheCredit(date, "Decaissement", piece, montant,
							0, 0, 0, montant, 0, montant, dm.getNumCredit());
					
					//Imputation comptable
					//Compte crédité
					ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Decaissement crédit " +numCredit, piece,
							soldeDecais, 0, ut, ind, grp, dm, null, accCred);
					//Compte Debité
					Account accDeb = CodeIncrement.getAcount(em, confGL.getCptPrincEnCoursInd());
					ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Decaissement crédit " +numCredit, piece,
							0, dm.getMontantApproved(), ut, ind, grp, dm, null, accDeb);
					
					//Compte debité 2 (Papeterie Decaissement)
					if(papeterie != 0){
						Account cred2 = CodeIncrement.getAcount(em, confGL.getPapeterieDec());
						ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Papeterie au decaissement de crédit " +numCredit,
								piece, 0, papeterie, ut, ind, grp, dm, null, cred2);					
					}					
					//Compte debité 3 (Commission au Decaissement)
					if(commission != 0){
						Account deb3 = CodeIncrement.getAcount(em, confGL.getCommDec());
						ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Commission au decaissement de crédit " +numCredit,
								piece, 0, commission, ut, ind, grp, dm, null, deb3);
					}

					
					//Tester si on va mettre à jour la date remboursement ou pas 
					if(confGen.getRecalcDateEcheanceAuDecais().equalsIgnoreCase("ne pas mettre a jours les dates")){
						System.out.println("Ne pas mettre à jour le calendrier de remboursement");
						List<Calpaiementdue> calDue = dm.getCalpaiementdues();
						for (Calpaiementdue calpaiementdue : calDue) {
							
							Calapresdebl calFinal = new Calapresdebl(calpaiementdue.getDate(), calpaiementdue.getMontantComm(),
									calpaiementdue.getMontantInt(), calpaiementdue.getMontantPenal(), calpaiementdue.getMontantPrinc(),
									false, calpaiementdue.getDemandeCredit());
							//calRembAprDebl.add(calFinal);
							
							//Total solde courant
							soldCourant += calpaiementdue.getMontantPrinc() + calpaiementdue.getMontantInt() - calpaiementdue.getMontantPenal();
							//total intérêt				
							totInter += calpaiementdue.getMontantInt();
							//Solde total
							double soldTotal = totInter + dm.getMontantDemande();
											
							//ajout calendrier due au Fihe credit
							//calFinal.getDate()
							FicheCredit fiche = new FicheCredit(calpaiementdue.getDate(), "Tranche due", "", calpaiementdue.getMontantPrinc(), 
									calpaiementdue.getMontantInt(), calpaiementdue.getMontantPenal(), soldCourant, 
									dm.getMontantDemande(), totInter, soldTotal, dm.getNumCredit());
							try {
								transaction.begin();
								em.persist(fiche);
								em.persist(calFinal);
								transaction.commit();
								em.refresh(fiche); 
								em.refresh(calFinal); 
								System.out.println("Fiche credit enregitré");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}else if(confGen.getRecalcDateEcheanceAuDecais().equalsIgnoreCase("mettre a jours les dates")){
						System.out.println("mettre à jour le calendrier de remboursement");
									
						List<CalView> lisCalView = demCredit(codeClient, codeGroupe, date, montant, dm.getTaux()
								, dm.getNbTranche(), dm.getTypeTranche(), dm.getDiffPaie(), dm.getModeCalculInteret());
						
						for (CalView calView : lisCalView) {
							
							Calapresdebl calFinal = new Calapresdebl(calView.getDate(), calView.getMontantComm(), calView.getMontantInt(), calView.getMontantPenal(),
									calView.getMontantPrinc(), false, dm);
							//calRembAprDebl.add(calFinal);	
							
							//Total solde courant
							soldCourant += calView.getMontantPrinc() + calView.getMontantInt() - calView.getMontantPenal();
							//total intérêt				
							totInter += calView.getMontantInt();
							//Solde total
							double soldTotal = totInter + dm.getMontantDemande();
											
							//ajout calendrier due au Fihe credit
							FicheCredit fiche = new FicheCredit(calView.getDate(), "Tranche due", "", calView.getMontantPrinc(), 
									calView.getMontantInt(), calView.getMontantPenal(), soldCourant, 
									dm.getMontantDemande(), totInter, soldTotal, dm.getNumCredit());
							try {
								transaction.begin();
								em.persist(fiche);
								em.persist(calFinal);
								transaction.commit();
								em.refresh(fiche); 
								em.refresh(calFinal); 
								System.out.println("Fiche credit enregitré");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					transaction.begin();
					em.flush();
					em.persist(f);
					em.merge(dm);
					em.persist(decais);
					transaction.commit();
					em.refresh(dm);
					em.refresh(decais);
					em.refresh(f); 
					
					System.out.println("Decaisemment reussit!!!");
					result = "Decaisemment reussit!!!";

				} catch (Exception e) {
					e.printStackTrace();
					result = "Erreur decaissement!";
				}

			} else {
				result = "Desolé, Vous ne pouvez pas decaisser ce crédit!!!";
			}

		}
		
		return result;
	}
	
	//Chercher décaissement par clé primaire
	@Override
	public Decaissement getUniqueDecaissement(String code) {
		return em.find(Decaissement.class, code);
	}

	//Afficher tous les décaissements
	@Override
	public List<Decaissement> getAllDecaissement() {
		String sql = "select d from Decaissement d join d.demandeCredit dm where dm.approbationStatut='Demande decaissé' or "
				+ " dm.approbationStatut='Rééchelonner'";
		return em.createQuery(sql, Decaissement.class).getResultList();
	}
	
	//Update décaissement
	@Override
	public boolean updateDecaissement(Decaissement decaissement,
			String typePaie, String numTel, String numCheq, String numCompte,
			String comptCaise, int userUpdate, String numCred) {
		//utilisateur qui modifie l'enregistrement 
				Utilisateur ut = em.find(Utilisateur.class, userUpdate);
				Decaissement d = em.find(Decaissement.class, decaissement.getTcode());
				
				
				d.setDateDec(decaissement.getDateDec());
				d.setPiece(decaissement.getPiece());
				d.setMontantDec(decaissement.getMontantDec());
				d.setCommission(decaissement.getCommission());
				d.setStationnary(decaissement.getStationnary());
				d.setUserUpdate(ut);
				try {
					transaction.begin();
					em.merge(d);
					transaction.commit();
					System.out.println("Decaissement modifié!");
					return true;
				} catch (Exception e) {
					e.printStackTrace(); 
					return false;
				}
	}

	
	//Calcul difference entre la date au calendrier de remboursement et la date de paiement échéance
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
			Long val = ChronoUnit.DAYS.between(dateDu, dateparm);			
			System.out.println(val + "\n");			
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	//--------------------------------------------------------------------------------------------------------------
	/*********************************************** REMBOURSEMENT *************************************************/
	
	//Recupération calendrier après deblocage par numéro crédit
	static Calapresdebl getInstanceCalapredebl(String numCredit){
		Query requete = em.createQuery("select MIN(c.date) from Calapresdebl c join c.demandeCredit numCred where"
						+ " c.payer = :p AND numCred.numCredit = :numC");
		requete.setParameter("p", false);
		requete.setParameter("numC", numCredit);
		String dateCal = (String) requete.getSingleResult();
		// Selection de principale due et intérêt due
		Query query = em.createQuery("select cl from Calapresdebl cl join cl.demandeCredit num"
						+ " where cl.date = :dateRemb and num.numCredit = :numCred");
		// query.setParameter("x", false);cl.payer = :x AND
		query.setParameter("dateRemb", dateCal);
		query.setParameter("numCred", "" + numCredit + "");
		Calapresdebl cals = null;
		if(query.getSingleResult() != null)
			cals = (Calapresdebl) query.getSingleResult();
		
		double princ = cals.getMprinc();
		double inter = cals.getMint();
		int rowId = cals.getRowId();		
		System.out.println("calendrier numero: "+ rowId);
		System.out.println("Principal total: " + princ);
		System.out.println("Interet total: " + inter);
		return cals;
	}
	
	// Récupération du montant reste du dernier remboursement
	static double getRestPaie(String numCredit){
		double montant = 0;
		Query qr = em.createQuery("select r.restaPaie from Remboursement r join r.demandeCredit numDem"
						+ " where r.dateRemb =( SELECT MAX(rb.dateRemb) FROM Remboursement rb JOIN rb.demandeCredit nums WHERE nums.numCredit"
						+ "= :numsCredit AND r.typeAction = :a) AND numDem.numCredit = :numero");
		qr.setParameter("numsCredit", numCredit);
		qr.setParameter("a", "Remboursement");
		qr.setParameter("numero", numCredit);						

		if (!qr.getResultList().isEmpty())
			montant = (double)qr.getFirstResult();							
		System.out.println("montant reste au dernière paiement : "+ montant);
		return montant;
		
	}
	
	//Calcul pénalité 
	static double calculPenalite(ProduitCredit p, double montant){
		
		ConfigPenaliteCredit confPenalit = p.getConfigPenaliteCredit();
		
		double montantPenal = 0;
		
		if(confPenalit.getModeCalcul().equalsIgnoreCase("montant fixe")){
			montantPenal = confPenalit.getMontantFixe();
		}
		if(confPenalit.getModeCalcul().equalsIgnoreCase("pourcentage")){
			montantPenal = (montant*(-1)*confPenalit.getPourcentage())/100;
		}
		
		return montantPenal;
	}
	
	//requette pour incrementer le nombre échéance 
	static int getNombreEcheanche(String numCredit){
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
		return nbEcheance;
	}
	
	//enregistrement fiche crédit après un remboursement
	static void calculFiche(String numCred, String date, String trans, String piece, double penalite,
			double princ, double inter, String dateCal){

		List<FicheCredit> ls = getMultipleFicheByDate(numCred, dateCal);
		for (FicheCredit ficheCredit : ls) {
			
			double soldCourant1 =  ficheCredit.getSoldeCourant() - (princ + inter);
			double soldPrincipal1 = ficheCredit.getTotalPrincipal() - princ;
			double soldeInteret1 = ficheCredit.getTotalInteret() - inter;
			double soldTotal1 = soldPrincipal1 + soldeInteret1;
			
			ficheCredit.setSoldeCourant(soldCourant1);
			ficheCredit.setTotalPrincipal(soldPrincipal1);
			ficheCredit.setTotalInteret(soldeInteret1);
			ficheCredit.setTotalSolde(soldTotal1);
			try {
				transaction.begin();
				em.merge(ficheCredit);
				transaction.commit();
				em.refresh(ficheCredit);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FicheCredit f =  getFicheByDate(numCred, dateCal);
		double soldCourant =  f.getSoldeCourant() - (princ + inter);
		double soldPrincipal = f.getTotalPrincipal() - princ;
		double soldeInteret = f.getTotalInteret() - inter;
		double soldTotal = soldPrincipal + soldeInteret;
		FicheCredit save = new 
				FicheCredit(date, trans, piece, princ, 
						inter, penalite, soldCourant, soldPrincipal, soldeInteret, soldTotal, numCred);
		f.setPaie(true); 
		try {
			transaction.begin();
			em.persist(save);
			em.merge(f);
			transaction.commit();
			em.refresh(save);
			em.refresh(f);
			System.out.println("Update fiche crédit réussit");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur update fiche crédit lors d'un remboursement");
		}
	}
	
	//Récupération d'un fiche crédit dans une date 
	static FicheCredit getFicheByDate(String numCred, String date){
		String sql = "select f from FicheCredit f where f.num_credit='"+numCred+"' and f.date='"+date+"' "
				+ " and f.transaction='Tranche due'";
		FicheCredit f = (FicheCredit) em.createQuery(sql).getSingleResult();
		if(f != null)
			return f;
		return null;
	}

	static List<FicheCredit> getMultipleFicheByDate(String numCred, String date){
		String sql = "select f from FicheCredit f where f.num_credit='"+numCred+"' and f.date > '"+date+"' "
				+ " and f.transaction='Tranche due' order by f.date asc";
		TypedQuery<FicheCredit> query = em.createQuery(sql, FicheCredit.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
			
	//Enregistrement remboursement
	
	@Override
	public boolean saveRemboursement(String numCredit, int userId, String date,
			double montant, String piece,String typePaie,String numTel,String numCheque, String cmptCaisse) { 

		//Instance de crédit en question
		DemandeCredit dm = em.find(DemandeCredit.class, numCredit); 
		//Utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, userId);
		
		//Configuration GrandLivre crédit
		ConfigGlCredit confGl = dm.getProduitCredit().getConfigGlCredit();
	
		//Remboursement à partir d'un N° compte epargne 
		//String numCompteEp;
		
		//valeur de retour		
		boolean retour = false;  
	
		Caisse cptCaisse = null;
		String cptC = "";		
		if(!cmptCaisse.equalsIgnoreCase("")){
			cptCaisse = em.find(Caisse.class, cmptCaisse);
			cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
		}

		//Valeur de retour
		String results = "";
		
		Individuel ind = null;
		Groupe grp = null;
				
		//Verifie si le credit existe ou pas
		if (dm != null) {			
			//Verifie si le crédit est déjà remboursé ou pas
			if (dm.getSolde_total() != 0) {//>										
				//Initialisation de la valeur du capital, intérêt à ajouter dans le grand livre
				double capitals = 0.0;
				double inters = 0.0;
				double tots = 0.0;
						
				//Traitement remboursement
				while(montant > 0){											
					// / pour incrémenter le tcode dans la table grandlivre
					String indexTcode = CodeIncrement.genTcode(em);				
					//Recupération calendrier après deblocage du crédit 		
					Calapresdebl cals = getInstanceCalapredebl(numCredit);					
					//Récupération principal et interet du calapresdebl à rembouser
					double princ = cals.getMprinc();
					double inter = cals.getMint();
					
					//Reste de payement intérêt et principal
					double restMontant = 0.0;		
					
					//Montant reste du dernier remboursement
					double derRestPaie = getRestPaie(numCredit);
					//Total montant à rembourser
					double montRembourser = montant + derRestPaie;							
					System.out.println("Total montant à rembourser = "+ montRembourser);
					
									//Calcul remboursement
					
					//Intérêt remboursé
					double interetRemb = 0.0;
					//Principal remboursé
					double principaleRem = 0.0;
					//Pénalité
					double montantPenal = 0.0;
					
					//Instancie la configuration individuel ou groupe selon le client
					ConfigCreditGroup confGroupe = null;
					ConfigCreditIndividuel confInd = null;
					boolean cInd = false;
					boolean cGrp = false;					
					if(dm.getIndividuel() != null){
						ind = dm.getIndividuel();
						confInd = dm.getProduitCredit().getConfigCreditIndividuel();
						if(confInd.getPariementPrealableInt() == true)
							cInd = true;
					}
					if(dm.getGroupe() != null){
						grp = dm.getGroupe();
						confGroupe = dm.getProduitCredit().getConfigCreditGroupe();
						if(confGroupe.isPaiePrealableInt() == true)
							cGrp = true;						
					}
					
					
					//On teste la priorité du remboursement: 
					//si cInd ou cGrp est égal à true donc le payement de l'intérêt est priorité
					if(cInd == true || cGrp == true){
						System.out.println("paiement préalable des intérêts");
						
						//Si le payement de l'intérêt est prioritaire
						//On soustraire le montant total remboursé par montant intéret du calendrier de remboursement
						
						double restPaieInt = montRembourser - inter;						
						System.out.println("Reste paiement intérêt: "+restPaieInt);
						//S'il y a de reste aprés soustraction par intérêt 
						if (restPaieInt > 0.0) {
							
							interetRemb = inter;
							inters = interetRemb;
							
							double restPaiePrinc = restPaieInt - princ;
							System.out.println("Reste paiement principal: "+restPaiePrinc);
							if (restPaiePrinc >= 0.0) {
								//Si reste de paiement principal superieur ou égal à 0
								principaleRem = princ;
								//reste paiement
								restMontant = restPaiePrinc; 
								cals.setPayer(true);	
								//System.out.println("Reste paiement: "+restMontant);
							} else if (restPaiePrinc < 0.0) {
								principaleRem = restPaieInt; 
								//calcul pénalité
								montantPenal = calculPenalite(dm.getProduitCredit(), restPaiePrinc);
								
								System.out.println("penalite: "+montantPenal);
								cals.setMpen((float) montantPenal);
								cals.setPayer(true);
								restMontant = restPaiePrinc + (montantPenal*(-1));
							}
							System.out.println("Reste paiement: "+restMontant);							
							capitals = principaleRem;
							
						} else if (restPaieInt < 0.0) {
							System.out.println("montant inferieur au interet due");
							interetRemb = montRembourser;
							inters = montRembourser;
							//calcul pénalité
							montantPenal = calculPenalite(dm.getProduitCredit(), restPaieInt);
							
							System.out.println("penalite: "+montantPenal);
							cals.setMpen((float)montantPenal);
							cals.setPayer(true);
							restMontant = restPaieInt + (montantPenal*(-1));
							System.out.println("Interêt payer: "+inters);
						}				
						
					}			
					else if(cInd == false || cGrp == false){
						System.out.println("Paiement de capital est prioritaire");
						
						//Si le paiement de capital est prioritaire
						//On soustraire le montant total remboursé par montant du principal dans le calendrier de remboursement
						double restPrinc = montRembourser - princ;						
						System.out.println("Reste de Paiement de capital: "+restPrinc);
						//S'il y a de reste aprés soustraction par intérêt 
						if (restPrinc > 0.0) {
							
							principaleRem = princ;
							capitals = princ;
							
							double restInt = restPrinc - inter;
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
								montantPenal = calculPenalite(dm.getProduitCredit(), restInt);
														
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
							montantPenal = calculPenalite(dm.getProduitCredit(), restPrinc);
													
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
					
					//mise à jour du principal total, intérêt total et solde total
					principalTotal = principalTotal - principaleRem;
					interetTotal = interetTotal - interetRemb;
					soldeTotal = principalTotal + interetTotal;

					if (soldeTotal == 0.0) {
						dm.setApprobationStatut("Credit remboursé");
					}

					// Insertion à l'Entité Remboursement
					
					//Prend le type de paiement fait par l'utilisateur: paiement cash, par chèque ou par mobile
					String valeurPaie = "";
					if(typePaie.equalsIgnoreCase("cash")){
						valeurPaie =  cptC;
					}else if(typePaie.equalsIgnoreCase("cheque")){
						valeurPaie = numCheque;
					}else if(typePaie.equalsIgnoreCase("mobile")){
						valeurPaie = numTel;
					}
					
					//Instance des classes necessaire
					Remboursement saveRemb = new 
							Remboursement(indexTcode, date, piece, montant, principaleRem, interetRemb, restMontant,
									soldeTotal, "Remboursement", getNombreEcheanche(numCredit), typePaie,
									valeurPaie, principalTotal,
									interetTotal, 0, 0, 0, dm, ut); 
					
					// Modification sur l'entité DemandeCredit
					dm.setSolde_total(soldeTotal);
					dm.setPrincipale_total(principalTotal);
					dm.setInteret_total(interetTotal);
					
					try {
						//Enregistrement fiche crédit
						calculFiche(dm.getNumCredit(), date, "Remboursement", piece,
								montantPenal, principaleRem, interetRemb, cals.getDate());
						transaction.begin();
						em.merge(cals);
						em.merge(dm);
						em.persist(saveRemb);
						transaction.commit();
						em.refresh(saveRemb);
						em.refresh(dm);
						em.refresh(cals); 
						results = "Remboursement Enregistrer!!!";
						System.out.println(results);
						//imputation comptable 
						//Remboursement des clients sain
						if(dm.getIndividuel().isSain() == true || dm.getGroupe() != null){     
							tots = capitals + inters;
							
							// Solde Debité
							Account accDebit = CodeIncrement.getAcount(em, cptC);					
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement du crédit " +numCredit, 
									piece, 0, tots, ut, ind, grp, dm, null, accDebit);
							
							// Solde Crédité 1 (Principal)
							Account accCred1 = CodeIncrement.getAcount(em, confGl.getCptPrincEnCoursInd());				
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement principal du crédit " +numCredit, 
									piece, capitals, 0, ut, ind, grp, dm, null, accCred1);
							
							// Solde Crédité 2 (Interet)
							Account accCred2 = CodeIncrement.getAcount(em, confGl.getCptIntRecCrdtInd());
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement Interet du crédit " +numCredit, 
									piece, inters, 0, ut, ind, grp, dm, null, accCred2);
							
							System.out.println("Remboursement normal du crédit n° "+ dm.getNumCredit() +" bien enregistré");
						}
						else if(dm.getIndividuel().isListeNoir() == true){
							
							//Information de compte à débité : Compte caisse 101
							Account debiCaisse = CodeIncrement.getAcount(em, cptC);			
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement capital VNI du crédit " +numCredit, 
									piece, 0, capitals, ut, dm.getIndividuel(), null, dm, null, debiCaisse);
							
							// Information de compte à crédité :compte 26xxxx
							Account accVNI = CodeIncrement.getAcount(em, "263");
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement capital VNI du crédit " +numCredit, 
									piece, capitals, 0, ut, dm.getIndividuel(), null, dm, null, accVNI);	
							
							System.out.println("Remboursement VNI du crédit n° "+ dm.getNumCredit() +" bien enregistré");
						}
						else if(dm.getIndividuel().isListeRouge() == true){
							
							//Information de compte à débité : Compte caisse 101
							Account debiCaisse = CodeIncrement.getAcount(em, cptC);
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement capital du crédit " +numCredit, 
									piece, 0, capitals, ut, dm.getIndividuel(), null, dm, null, debiCaisse);
							
							// Information de compte à crédité :compte 27xxxx
							Account accDouteux = CodeIncrement.getAcount(em, "27");
							ComptabliteServiceImpl.saveImputationLoan(indexTcode, date, "Remboursement capital du crédit " +numCredit, 
									piece, capitals, 0, ut, dm.getIndividuel(), null, dm, null, accDouteux);
							
							System.out.println("Remboursement dotation du crédit n° "+ dm.getNumCredit() +" bien enregistré");
							
						}	
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (em == null) {
							em.close();
						}
					}					
				
				montant = restMontant;
				
				System.out.println("Montant = "+ montant);
			}				
				retour = true;
			} else {
				results = "Crédit déjà rembourser!!!";
			}
		}/* else {
			results = "ce numero credit n'existe pas!";
		}*/
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
	 * Affiche montant à rembourser
	 * ***/
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<String> getMontaRemb(String numCredit, String date) {
		
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
	
	//-----------------------------------------------------------------------------------
	/******************************* Rééchelonnement remboursement *********************************/
	
	//Get calendrier après déblocage (prend en paramètre: un numéro crédit et un boolean paie)
	static List<Calapresdebl> getCalapresDebl(String numCred, boolean paie){
		String sql = "select c from Calapresdebl c join c.demandeCredit d where "
				+ " d.numCredit ='"+numCred+"' and c.payer='"+paie+"' order by c.date";
		return em.createQuery(sql, Calapresdebl.class).getResultList();
	}
	
	//Get Fiche crédit (prend en paramètre: un numéro crédit et un boolean paie)
	static List<FicheCredit> getFiche(String numCred, boolean paie){
		String sql = "select f from FicheCredit f where "
				+ " f.num_credit ='"+numCred+"' and f.paie='"+paie+"' and f.transaction='Tranche due' order by f.date";
		return em.createQuery(sql, FicheCredit.class).getResultList();
	}
	
	//Calcul date
	static LocalDate verifDate(LocalDate date){
		List<JourFerier> jours = UserServiceImpl.listJoursFerier();
		for (JourFerier jourFerier : jours) {
			if(date.toString().equals(jourFerier.getDate())){
				date = date.plusDays(1);
			}
		}
		if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			date = date.plusDays(2);
		} else if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			date = date.plusDays(1);
		}
		return date;
	}
	
	//Update Calendrier après deblocage
	static void updateCalapresDebl(Calapresdebl cal){
		Calapresdebl c = em.find(Calapresdebl.class, cal.getRowId());
		c = cal;
		try {
			transaction.begin();
			em.merge(c);
			transaction.commit();
			em.refresh(c);
			System.out.println("Update calendrier reussit");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Update calendrier erreur");
		}
	}
	
	//Update Fiche crédit
	static void updateFiche(FicheCredit fic){
		FicheCredit f = em.find(FicheCredit.class, fic.getId());
		f = fic;
		try {
			transaction.begin();
			em.merge(f);
			transaction.commit();
			em.refresh(f);
			System.out.println("Update Fiche reussit");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Update Fiche erreur");
		}
	}
	
	//Enregistrement crédit rééchelonné
	@Override
	public boolean reechelonnerCredit(String numCred, String type, int nombre, String motif) {

		System.out.println("Type date :"+type);
		switch (type) {
		case "Jour": 
			for (Calapresdebl cal : getCalapresDebl(numCred, false)) {
				LocalDate date = LocalDate.parse(cal.getDate());				
				date = date.plusDays(nombre);	
				date = verifDate(date);				
				cal.setDate(date.toString());
				updateCalapresDebl(cal); 
			}
			for (FicheCredit f : getFiche(numCred, false)) {
				LocalDate date = LocalDate.parse(f.getDate());
				date = date.plusDays(nombre);		
				date = verifDate(date);
				f.setDate(date.toString());
				updateFiche(f); 
			}
			break;
		case "Semaine":
			for (Calapresdebl cal : getCalapresDebl(numCred, false)) {
				LocalDate date = LocalDate.parse(cal.getDate());				
				date = date.plusWeeks(nombre);		
				date = verifDate(date);				
				cal.setDate(date.toString());
				updateCalapresDebl(cal); 
			}
			for (FicheCredit f : getFiche(numCred, false)) {
				LocalDate date = LocalDate.parse(f.getDate());
				date = date.plusWeeks(nombre);		
				date = verifDate(date);
				f.setDate(date.toString());
				updateFiche(f); 
			}
			break;
		case "Quinzaine":
			for (Calapresdebl cal : getCalapresDebl(numCred, false)) {
				LocalDate date = LocalDate.parse(cal.getDate());				
				date = date.plusWeeks(nombre * 2);	
				date = verifDate(date);				
				cal.setDate(date.toString());
				updateCalapresDebl(cal); 
			}
			for (FicheCredit f : getFiche(numCred, false)) {
				LocalDate date = LocalDate.parse(f.getDate());
				date = date.plusWeeks(nombre * 2);		
				date = verifDate(date);
				f.setDate(date.toString());
				updateFiche(f); 
			}
			break;
		case "Mois":
			for (Calapresdebl cal : getCalapresDebl(numCred, false)) {
				LocalDate date = LocalDate.parse(cal.getDate());				
				date = date.plusMonths(nombre);	
				date = verifDate(date);				
				cal.setDate(date.toString());
				updateCalapresDebl(cal); 
			}
			for (FicheCredit f : getFiche(numCred, false)) {
				LocalDate date = LocalDate.parse(f.getDate());
				date = date.plusMonths(nombre);		
				date = verifDate(date);
				f.setDate(date.toString());
				updateFiche(f); 
			}
			break;
		default:
			break;
		}
		
		//Update information du table demande crédit
		DemandeCredit dm = em.find(DemandeCredit.class, numCred);
		dm.setApprobationStatut("Rééchelonner");
		dm.setTypeRechelonne(type);
		dm.setNombreRechelonne(nombre);
		dm.setMotifRechelonne(motif);
		try {
			transaction.begin();
			em.merge(dm);
			transaction.commit();
			em.refresh(dm); 
			System.out.println("Update demande réécheloné réussit");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur update demande réécheloné");
			return false;
		}
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
	
	////Rapprort montant dû aujourd'hui
	@Override
	public List<Calapresdebl> getMontantDu(String date) {
		
		String sql = "select c from Calapresdebl c where c.date='"+date+"' and c.payer='false'";
		TypedQuery<Calapresdebl> query = em.createQuery(sql,Calapresdebl.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//Select distinct numéro crédit dans calendrier après déblocage(Calapresdebl)
	static List<DemandeCredit> getDistinctCredit(){
		String sql = "select distinct c.demandeCredit from Calapresdebl c";
		TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//Rapport montant dû pour un payement futur 
	@Override
	public List<Calapresdebl> getMontantDuFutur() {
	  List<DemandeCredit> dm = getDistinctCredit();
	  List<Calapresdebl> result = new ArrayList<Calapresdebl>();
	
	  for (DemandeCredit demandeCredit : dm) {
		  Calapresdebl cl = new Calapresdebl();
		  String sql ="select c from Calapresdebl c join c.demandeCredit d where "
		  + "c.date=(select min(i.date) from Calapresdebl i join i.demandeCredit dm where i.payer='false' and "
		  + " dm.numCredit='"+demandeCredit.getNumCredit()+"') and c.payer='false' and d.numCredit='"+demandeCredit.getNumCredit()+"'";
		  Query query = em.createQuery(sql);
		  if(!query.getResultList().isEmpty()){
			  cl = (Calapresdebl) query.getSingleResult();
			  result.add(cl);			  				  
		  }
			  
	  }
	  return result;
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
