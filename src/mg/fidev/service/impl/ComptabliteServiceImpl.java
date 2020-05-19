package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Account;
import mg.fidev.model.Analytique;
import mg.fidev.model.Budget;
import mg.fidev.model.Caisse;
import mg.fidev.model.Compte;
import mg.fidev.model.ConfigGlCredit;
import mg.fidev.model.ConfigTransactionCompta;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ListeRouge;
import mg.fidev.model.OperationView;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.ComptabiliteService;
import mg.fidev.utils.AfficheBalance;
import mg.fidev.utils.AfficheBilan;
import mg.fidev.utils.AfficheListeCreditDeclasser;
import mg.fidev.utils.CodeIncrement;
import mg.fidev.utils.MouvementCompta;

@WebService(name="comptabliteService", targetNamespace="http://fidev.mg.comptabliteService", serviceName="comptabliteService",
portName="comptabliteServicePort", endpointInterface="mg.fidev.service.ComptabiliteService")
public class ComptabliteServiceImpl implements ComptabiliteService {
	
	//Declaration de l'EntityManager 
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	
	//FONCTION QUI AFFICHE LE GRAND LIVRE 
	@Override
	public List<Grandlivre> afficheGrandLivre(String compte,String nomUtilisateur,String dateDeb, String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		try {
			//Listes de données dans le grand livre: affichage entre deux periode
			
			String sql = "SELECT g FROM Grandlivre g join g.account ac ";
			if(!compte.equals("") || !nomUtilisateur.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
				
				sql +=" WHERE ";
				
				if(!nomUtilisateur.equals("")){
					sql+= "g.userId ='"+nomUtilisateur+"'";
					
					if(!dateDeb.equals("") && dateFin.equals("")){
						sql+= " AND g.date ='"+dateDeb+"'";				
					}
					if(!dateDeb.equals("") && !dateFin.equals("")){
						sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
					}			
				}
				
				if(!compte.equals("")){
					sql+= " ac.numCpt ='"+compte+"'";
					if(!dateDeb.equals("") && dateFin.equals("")){
						sql+= " AND g.date ='"+dateDeb+"'";				
					}
					if(!dateDeb.equals("") && !dateFin.equals("")){
						sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
					}	   
				}
				
				if(!dateDeb.equals("") && dateFin.equals("") && compte.equals("") && nomUtilisateur.equals("")){
					sql+= "g.date ='"+dateDeb+"'";
				}
				
				if(!dateDeb.equals("") && !dateFin.equals("") && compte.equals("") && nomUtilisateur.equals("")){
					sql+= "g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	
				
			}
			
			sql +=" order by g.tcode asc";
			//sql +=" order by ac.numCpt asc";
			
			TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
			if(!(query.getResultList().isEmpty()))
				result = query.getResultList();
			else
				System.err.println("il n'y a pas de resultat");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/***
	 * LISTE DES DONNEES DANS GRAND LIVRE
	 * ***/
	@Override
	public List<Grandlivre> findGranLivre() {
		TypedQuery<Grandlivre> query = em.createQuery("SELECT g FROM Grandlivre g",Grandlivre.class);
		List<Grandlivre> result = query.getResultList();
		return result;
	}

	
	/***
	 * Liste des comptes comptable
	 * ***/
	@Override
	public List<Account> listComptes() {
		TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a WHERE a.isHeader = :x",Account.class);
		query.setParameter("x", false);
		List<Account> result = query.getResultList();
		return result;
	}

	/***
	 * AJOUT COMPTES CAISSES
	 * ***/
	@Override
	public boolean addCompteCaisse(String cptCaisse, String devise, int planCompta) {
		
		Account cptGL = em.find(Account.class, planCompta);
		Caisse cmpt = new Caisse();
		System.out.println("Nouveau compte caisse");
		try {
			cmpt.setNomCptCaisse(cptCaisse);
			cmpt.setDevise(devise);
			cmpt.setAccount(cptGL);		
			transaction.begin();
			em.persist(cmpt);
			transaction.commit();
			System.out.println("Nouveau compte caisse inséré");
			return true;
		} catch (Exception e) {
			System.err.println("Erreur d'insertion de compte caisse");
			return false;
		}
	}
	
	//Ajout nouveau compte comptable (Test)
	@Override
	public Compte ajoutCompte(Compte compte, String compteParent) {
		/*
		 * La liste des paramètres requis sont :
		 * -code de compte
		 * -libelle de compte
		 * -devise
		 * */ 	
		if(compte.getCompte().equals("") || compte.getLibelle().equals("")){
			System.out.println("Numero de compte null");		
			return null;
		}
		
		try {	
			compte.setFerme(false);
			compte.setActive(true);
			compte.setType("Compte");
			
			if(!compteParent.equals("")){
				Compte parent = em.createQuery("select p from Compte p where p.compte = "
						+ "'"+compteParent+"'", Compte.class).getSingleResult();
				compte.setCompteParent(parent);
				compte.setLevel(parent.getLevel()+1);
				parent.setActive(false);
			}
			
			transaction.begin();
			em.persist(compte);
			em.flush();
			transaction.commit();
			em.refresh(compte);
			return compte;				
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public Account addAccount(Account compte, int compteParent) {
		try {
			compte.setFerme(false);
			compte.setActive(true);
			
			Account parrent = em.find(Account.class, compteParent);
			
			if(parrent != null ){
				compte.setAccount(parrent); 
				compte.setHlevel(parrent.getHlevel()+1);
				parrent.setActive(false);
			}
			
			transaction.begin();
			em.persist(compte);
			em.flush();
			transaction.commit();
			em.refresh(compte);
			em.refresh(parrent);
			return compte;				
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Recupérer les comptes comptable (Test)
	@Override
	public List<Compte> getComptes() {
		try {
			String sql = "SELECT c FROM Compte c GROUP BY c.compte ";	
			TypedQuery<Compte> query = em.createQuery(sql,Compte.class);
			
			if(!query.getResultList().isEmpty()){
				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}	
		return null;
	}
	
	//Recupérer les comptes comptable
	@Override
	public List<Account> getAccounts() {
		
		try {
			String sql = "SELECT c FROM Account c order by c.numCpt asc";	// ORDER BY c.account and 
			TypedQuery<Account> query = em.createQuery(sql,Account.class);
			
			if(!query.getResultList().isEmpty()){
				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}	
		
		return null;
	}
	
	/***
	 * Fonction qui supprime un compte compta 
	 * ***/
	@Override
	public boolean supprimerCompte(int compte){
		//on selectionne le compte
		Account ac = em.find(Account.class, compte);
		try {
			transaction.begin();
			em.remove(ac);
			transaction.commit();
			em.refresh(ac);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(em!=null){
				em.close();
			}
		}	
		return false;
	}
	
	//Transaction caisse et banque comptable
	@Override
	public boolean saveTransaction(String date,String piece, String description,
			String compte,String compte2,double debit, int user) {
		
		//instaciation de l'utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, user);
		//instance de compte que l'utilisateur saisit 
		Account cmpt =CodeIncrement.getAcount(em, compte);
		
		//Instance de compte d'equilibre à la configuration
		Account cmpt2 = CodeIncrement.getAcount(em, compte2);
		
		//on instancie le grand livre	
		Grandlivre debiter = new Grandlivre();
		Grandlivre crediter = new Grandlivre();
		
		debiter.setTcode(CodeIncrement.genTcode(em));
		debiter.setDate(date);
		debiter.setDescr(description);
		debiter.setPiece(piece);
		debiter.setAccount(cmpt);
		
		debiter.setDebit(debit);
		debiter.setUserId(ut.getNomUtilisateur());
		debiter.setSolde(cmpt.getSoldeProgressif()+debit);
		cmpt.setSoldeProgressif(cmpt.getSoldeProgressif()+debit);
		
		
		crediter.setTcode(CodeIncrement.genTcode(em));
		crediter.setDate(date);
		crediter.setDescr(description);
		crediter.setPiece(piece);
		crediter.setAccount(cmpt2);
		
		crediter.setCredit(debit);
		crediter.setUserId(ut.getNomUtilisateur());
		crediter.setSolde(cmpt2.getSoldeProgressif()-debit);
		cmpt2.setSoldeProgressif(cmpt2.getSoldeProgressif()-debit);
		
		try {		
			transaction.begin();
			em.persist(crediter);
			em.persist(debiter);
			em.flush();
			transaction.commit();
			System.out.println("Enregistrement reussit");
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur enregistrement");
			return false;
		}
	
	}
	
	//Get Compte actif (Test)
	@Override
	public List<Compte> getComptesActif(String compte) {
		try {
			String sql = "SELECT c FROM Compte c WHERE c.active = '"+true+"'";	
			
			if(!compte.equals("")){
				sql+=" AND c.compte like '"+ compte +"%'";
			}
			
			TypedQuery<Compte> query = em.createQuery(sql,Compte.class);
			
			if(!query.getResultList().isEmpty()){
				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}	
		return null;
	}
	
	//Get grand livre par code client
	@Override
	public List<Grandlivre> grandLivreClient(String codeCli, String codeGrp) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		try {
			//Listes de données dans le grand livre: affichage par code Client
			if(!codeCli.equals("")){
				Individuel ind = em.find(Individuel.class, codeCli);
				result = ind.getGrandLivre();
			}else if(!codeGrp.equals("")){
				Groupe grp = em.find(Groupe.class, codeGrp);
				result = grp.getGrandLivre();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	static List<Account> getAccDistinct(){
		List<Account> result = new ArrayList<Account>();
		
		Query query = em.createQuery("select distinct g.account from Grandlivre g");
		
		if(!query.getResultList().isEmpty()){
			result = (List<Account>) query.getResultList();
			return result;
		}		
		return null;
	}
	
	
	//balance des comptes
	@Override
	public List<String> balance(String dateDeb, String dateFin) {
		
		List<Account> compte = getAccDistinct();
		
		for (Account account : compte) {
			System.out.println("Compte: "+ account.getNumCpt() +"\n");
			System.out.println("libellé: "+ account.getLabel()+"\n");
			System.out.println("Solde: "+ account.getSoldeProgressif()+"\n\n");
		}
		
		return null;
	}

	static List<Account> getCompteDistinct(){
		String sql = "select distinct g.account from Grandlivre g order by g.account asc";
		
		List<Account> compte = new ArrayList<Account>();
		
		TypedQuery<Account> query = em.createQuery(sql,Account.class);
		
		compte = query.getResultList(); 
		
		for (Account account : compte) {
			System.out.println("Id :"+account.getTkey());
			System.out.println("Compte :"+account.getNumCpt()+"\n");
		}
		return compte;
	}
	
	static List<Grandlivre> reqs(int compte){
		List<Grandlivre> list = new ArrayList<Grandlivre>();
		
		String sql = "select gr from Grandlivre gr join gr.account a where a.tkey = '"+compte+"'";
		
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		
		list = query.getResultList();	
		return list;
	}

	
	@Override
	public List<Grandlivre> tests() {
		String sql = "select distinct g.account from Grandlivre g order by g.account asc";
		
		List<Account> compte = new ArrayList<Account>();
		
		TypedQuery<Account> query = em.createQuery(sql,Account.class);
		
		compte = query.getResultList(); 
		
		List<Grandlivre> list = new ArrayList<Grandlivre>();
		
		for (Account account : compte) {
	//		System.out.println("Id :"+account.getTkey());
	//		System.out.println("Compte :"+account.getNumCpt()+"\n");
	
			list = reqs(account.getTkey());		
			for (int i = 0; i < list.size(); i++) {
				System.out.println("Tkey :"+list.get(i).getAccount().getTkey());
				
				System.out.println("Tcode :"+list.get(i).getTcode());
				System.out.println("Compte :"+list.get(i).getAccount().getNumCpt());
				System.out.println("Debit :"+list.get(i).getDebit());
				System.out.println("Credit :"+list.get(i).getCredit());
				System.out.println("Date :"+list.get(i).getDate() +"\n");
			}
			
			//return list;
		}
		return list;
		
	}
	
	static List<Grandlivre> lMouvmnts(String compte){
		List<Grandlivre> operations = new ArrayList<Grandlivre>();
		
		String sql2 = "select t from Grandlivre t where t.compte = '"+compte+"'";	
		TypedQuery<Grandlivre> qr = em.createQuery(sql2, Grandlivre.class);
		operations =  qr.getResultList();
		
		 for(Grandlivre grandLivre : operations) {			
			 System.out.println("Date : "+ grandLivre.getDate());
			 System.out.println("Description : "+ grandLivre.getDescr());
			 System.out.println("Piece : "+ grandLivre.getPiece());
			 System.out.println("Débit : "+ String.valueOf(grandLivre.getDebit()));
			 System.out.println("Crédit : "+ String.valueOf(grandLivre.getCredit()) +"\n");
			
		}	
		return operations;
	}
	
	//Test pour afficher le grand livre
	@SuppressWarnings("unchecked")
	@Override
	public List<MouvementCompta> mouvementCompte() {
		
		List<MouvementCompta> r = new ArrayList<MouvementCompta>();
		
		MouvementCompta mv = new MouvementCompta();
		
		List<Grandlivre> operations = new ArrayList<Grandlivre>();
		
		String sql = "select distinct g.compte from Grandlivre g order by g.compte asc";
		List<String> compteGrandLivre = (List<String>) em.createQuery(sql).getResultList();
		
		for (String compte : compteGrandLivre) {
		
			Object[] obj = (Object[]) em.createQuery("select a.label,a.numCpt from Account a where a.numCpt = '"+compte+"'").getSingleResult();
	
				String libelle = (String) obj[0];
				String cmpt = (String) obj[1];
				mv.setCompte(cmpt);
				mv.setLibelle(libelle);
				
				operations = lMouvmnts(compte);
	
			 mv.setGrandLivres(operations);
			 r.add(mv);		
			
		}
			
		for (int i = 0; i < r.size(); i++) {
			 System.out.println(r.get(i).getCompte());
			 System.out.println(r.get(i).getLibelle());
			 
			 for(Grandlivre grandLivre : r.get(i).getGrandLivres()) {			
				 System.out.println(grandLivre.getDate());
				 System.out.println(grandLivre.getDescr());
				 System.out.println(grandLivre.getPiece());
				 System.out.println(String.valueOf(grandLivre.getDebit()));
				 System.out.println(String.valueOf(grandLivre.getCredit()));
				
			}
			
		}
		return r;
	}


	//Enregistrement budget
	@Override
	public boolean saveBudget(Budget budget) {
		try {
			budget.setCode(CodeIncrement.getCodeBudget(em));
			budget.setRealisation(0); 
			transaction.begin();
			em.persist(budget);
			transaction.commit();
			System.out.println("Enregistrement budget reussit");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur enregistrement!!!");
			return false;
		}
	}

	//Get list budget
	@Override
	public List<Budget> listBudget() {

		List<Budget> result = new ArrayList<Budget>();

		String sql = "select b from Budget b";

		TypedQuery<Budget> query = em.createQuery(sql, Budget.class);

		if (!query.getResultList().isEmpty()) {
			result = query.getResultList();
			return result;
		}

		return null;
	}

	//Chercher compte comptable par numero de compte
	@Override
	public List<Account> chercheAccount(String compte) {
		try {
			String sql = "SELECT c FROM Account c WHERE c.isHeader='" + false
					+ "' AND c.isActive = '" + true + "'";

			if (!compte.equals("")) {
				sql += " AND c.numCpt like '" + compte + "%'";
			}

			TypedQuery<Account> query = em.createQuery(sql, Account.class);

			if (!query.getResultList().isEmpty()) {
				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Chercher budgets par code budget
	@Override
	public List<Budget> chercherBudget(String code) {
		String sql = "select b from Budget b where b.code like '" + code + "%'";
		TypedQuery<Budget> query = em.createQuery(sql,Budget.class);
		if (!query.getResultList().isEmpty()) {
			return query.getResultList();
		}
		return null;
	}

	//Chercher code analytique
	@Override
	public List<Analytique> chercherAnalytique(String code) {
		String sql = "select a from Analytique a where a.id like '" + code + "%'";
		TypedQuery<Analytique> query = em.createQuery(sql,Analytique.class);
		if (!query.getResultList().isEmpty()) {
			return query.getResultList();
		}
		return null;
	}

	// Chercher budget par son identifiant
	@Override
	public Budget findBudget(String code) {
		Budget budget = em.find(Budget.class, code);
		return budget;
	}

	//Enregistrement Analytique
	static Analytique saveAnalytique(String nom) {
		Analytique analytique = new Analytique();
		try {
			Query verification = em
					.createQuery("select a from Analytique a where a.nom = :x");
			verification.setParameter("x", nom);

			if (!verification.getResultList().isEmpty()) {
				analytique = (Analytique) verification.getSingleResult();
				return analytique;
			} else {
				analytique.setId(CodeIncrement.getCodeAnalytique(em));
				analytique.setNom(nom);
				transaction.begin();
				em.persist(analytique);
				transaction.commit();
				em.refresh(analytique);
				return analytique;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Get list analytique
	@Override
	public List<Analytique> getAnalytique() {
		List<Analytique> result = new ArrayList<Analytique>();
		String sql = "select a from Analytique a";

		TypedQuery<Analytique> query = em.createQuery(sql, Analytique.class);

		if (!query.getResultList().isEmpty()) {
			result = query.getResultList();

			return result;
		}

		return null;
	}
	
	
	//Enregistrement opération divers comptable
	@Override
	public boolean saveOperationDivers(String date, String piece,
			String description,String analytique, String budget, int user) {

		// on selectionne l'utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, user);

		Budget bdg = em.find(Budget.class, budget);
		Analytique anlt = saveAnalytique(analytique);
		
		//Information grand livre
		Grandlivre debiter = new Grandlivre();
		debiter.setTcode(CodeIncrement.genTcode(em));
		debiter.setDate(date);
		debiter.setDescr(description);
		debiter.setPiece(piece);
		debiter.setUserId(ut.getNomUtilisateur());
		debiter.setUtilisateur(ut);
		debiter.setAnalytique(anlt);
		debiter.setBudget(bdg); 
		
		Grandlivre crediter = new Grandlivre();
		crediter.setTcode(CodeIncrement.genTcode(em));
		crediter.setDate(date);
		crediter.setDescr(description);
		crediter.setPiece(piece);
		crediter.setUserId(ut.getNomUtilisateur());
		crediter.setUtilisateur(ut);
		crediter.setAnalytique(anlt);
		crediter.setBudget(bdg); 

		List<OperationView> listOperation = listOperationView();
		
		for (OperationView operationView : listOperation) {
			//On test la valeur de credit et debit dans l'opération view
			//si valeur de credit est égal à 0 donc l'opération est débit
		
			if(operationView.getCredit() == 0){
				
				Account compte1 = CodeIncrement.getAcount(em, operationView.getNumCmpt());
							
				debiter.setDebit(operationView.getDebit());
				debiter.setAccount(compte1);
				debiter.setSolde(compte1.getSoldeProgressif() + operationView.getDebit());
				compte1.setSoldeProgressif(compte1.getSoldeProgressif() + operationView.getDebit());			
			
			}
			
			if(operationView.getDebit() == 0){
				
				Account compte2 = CodeIncrement.getAcount(em, operationView.getNumCmpt());
					
				crediter.setCredit(operationView.getCredit());
				crediter.setAccount(compte2);
				crediter.setSolde(compte2.getSoldeProgressif() - operationView.getCredit());
				compte2.setSoldeProgressif(compte2.getSoldeProgressif() - operationView.getCredit());
				
			}	
			
		}	
		
		try {
			transaction.begin();
			em.persist(crediter);
			em.persist(debiter);
			em.flush();
			transaction.commit();
			System.out.println("Enregistrement opération reussit");
			//On va vider l'opération view après l'enregistrement
			//viderOperationView();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur enregistrement débit");
			return false;
		}
		
		//return false;
	}

	//Get grand livre budgétaire
	@Override
	public List<Grandlivre> getGrandLivreBudget(String code, String dateDeb,
			String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();

		String sql = "select g from Grandlivre g join g.budget b where g.budget is not null";

		if (!code.equals("") || !dateDeb.equals("") || !dateFin.equals("")) {

			if (!code.equals("")) {
				sql += " and b.code = '" + code + "'";
				if (!dateDeb.equals("") && dateFin.equals("")) {
					sql += " and g.date = '" + dateDeb + "'";
				}
				if (!dateDeb.equals("") && !dateFin.equals("")) {
					sql += " and g.date between '" + dateDeb + "' and '"
							+ dateFin + "'";
				}

			}

			if (!dateDeb.equals("") && dateFin.equals("") && code.equals("")) {
				sql += " and g.date = '" + dateDeb + "'";
			}
			if (!dateDeb.equals("") && !dateFin.equals("") && code.equals("")) {
				sql += " and g.date between '" + dateDeb + "' and '" + dateFin
						+ "'";
			}

		}

		TypedQuery<Grandlivre> query = em.createQuery(sql, Grandlivre.class);

		if (!query.getResultList().isEmpty()) {
			result = query.getResultList();
			return result;
		}

		return null;
	}

	//Get grand livre analytique
	@Override
	public List<Grandlivre> getGrandLivreAnalytique(String code,
			String dateDeb, String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();

		String sql = "select g from Grandlivre g join g.analytique a where g.budget is null and g.analytique is not null";

		if (!code.equals("") || !dateDeb.equals("") || !dateFin.equals("")) {

			if (!code.equals("")) {
				sql += " and a.id = '" + code + "'";
				if (!dateDeb.equals("") && dateFin.equals("")) {
					sql += " and g.date = '" + dateDeb + "'";
				}
				if (!dateDeb.equals("") && !dateFin.equals("")) {
					sql += " and g.date between '" + dateDeb + "' and '"
							+ dateFin + "'";
				}

			}

			if (!dateDeb.equals("") && dateFin.equals("") && code.equals("")) {
				sql += " and g.date = '" + dateDeb + "'";
			}
			if (!dateDeb.equals("") && !dateFin.equals("") && code.equals("")) {
				sql += " and g.date between '" + dateDeb + "' and '" + dateFin
						+ "'";
			}

		}

		TypedQuery<Grandlivre> query = em.createQuery(sql, Grandlivre.class);

		if (!query.getResultList().isEmpty()) {
			result = query.getResultList();
			return result;
		}
		return null;
	}

	@Override
	public List<Grandlivre> getGrandLivreGen(int id) {		
		List<Grandlivre> gl = new ArrayList<Grandlivre>();
		
		Account ic = em.find(Account.class, id);			
		gl = ic.getGranLivres();
		for (Grandlivre grandlivre : gl) {
			System.out.println("tcode :"+ grandlivre.getTcode());
			System.out.println("Description :"+ grandlivre.getDescr());
			System.out.println("Débit"+ grandlivre.getDebit());  
			System.out.println("Crédit :"+ grandlivre.getCredit()+"\n");
		}	
		return gl;
	}


	@Override
	public List<Grandlivre> getJournal(String date, String numCmpt) {
		System.out.println("date: "+date);
		System.out.println("compte: "+numCmpt);
		
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		
		String sql ="select g from Grandlivre g join g.account a where a.numCpt like '" + numCmpt + "%'";
		
		if(!date.equals("")){
			sql += " and g.date <= '" + date + "'";
		}
		sql +=" order by a.numCpt asc";
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;			
		}
		return null;
	}


	@Override
	public List<Grandlivre> getJournalDivers(String date) {
		
		String sql ="select g from Grandlivre g join g.account a where a.numCpt not like '" + 1 + "%' and a.numCpt not like '" + 2 + "%'" ;
		
		if(!date.equals("")){
			sql += " and g.date <= '" + date + "'";
		}
		sql +=" order by a.numCpt asc";
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}
		return null;
	}

	@Override
	public List<Account> getAcountDistinct() {
		String sql = "select distinct g.account from Grandlivre g join g.account a order by a.numCpt asc";
		List<Account> ac = new ArrayList<Account>();
		TypedQuery<Account> query = em.createQuery(sql, Account.class);
		ac = query.getResultList();
		return ac;
	}
	
	static  List<Account> getDistAccount(String dateDeb,String dateFin){
		String sql = "select distinct g.account from Grandlivre g join g.account a "
				+ " where g.date between '"+dateDeb+"' and '"+dateFin+"' order by a.numCpt asc";
		List<Account> ac = new ArrayList<Account>();
		TypedQuery<Account> query = em.createQuery(sql, Account.class);
		ac = query.getResultList();
		return ac;
	}

	@Override
	public List<AfficheBalance> getBalance(String dateDeb,String dateFin) {
		
		 List<AfficheBalance> result = new ArrayList<AfficheBalance>();
		
		List<Account> distinct = getDistAccount(dateDeb,dateFin);
		
		for (Account ac : distinct) {
			AfficheBalance affiche = new AfficheBalance();
			
			//Total débit
			double tDebit = 0;
			Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
					   + " where a.numCpt='"+ac.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
			if(q1.getSingleResult() != null){
				tDebit = (Double) q1.getSingleResult(); 
			}
			
			//total crédit
			double tCredit = 0 ; 
			Query q2 = em.createQuery("select sum(g.credit) from Grandlivre g join g.account a"
				   + " where a.numCpt='"+ac.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
			if(q2.getSingleResult() != null){
				tCredit = (Double)q2.getSingleResult();
			}		
			
			double soldeFinal = tDebit - tCredit;
			String stat = "";
			if(tDebit > tCredit){
				stat = "DEBITEUR";
			}else{
				stat = "CREDITEUR";
			}
			affiche.setNumCompte(ac.getNumCpt());
			affiche.setLibele(ac.getLabel());
			affiche.setSoldeInit(ac.getSoldeInit());
			affiche.setSommeDeb(tDebit);
			affiche.setSommeCred(tCredit);
			affiche.setSoldeFin(soldeFinal);
			affiche.setStat(stat);
			result.add(affiche);		
			
		}	
		return result;//2019-11-28 2019-11-21
	}
	
	//Affiche Bilan
	@Override
	public List<AfficheBilan> getBilan(String dateDeb, String dateFin) {
		List<AfficheBilan> result = new ArrayList<AfficheBilan>();
		int nb = 5;
		for (int i = 1; i <= nb; i++) {			
			String num = String.valueOf(i);
			
			//comptparent		
			Account parent = CodeIncrement.getAcount(em, num);
			AfficheBilan bilan = new AfficheBilan();
			
			//tout comptes ayant le numero ex: 1 ou 3 , 3 ou 4
			String sql = "select distinct g.account from Grandlivre g join g.account a "
					+ " where a.numCpt like '" + num + "%' and g.date between '"+dateDeb+"' and '"+dateFin+"' order by a.numCpt asc";
			List<Account> ac = new ArrayList<Account>();
			TypedQuery<Account> query = em.createQuery(sql, Account.class);
			
			if(!query.getResultList().isEmpty()){
				ac = query.getResultList();					
				List<AfficheBalance> lCompt = new ArrayList<AfficheBalance>();
				
				for (Account account : ac) {
					AfficheBalance b = new AfficheBalance();
					
					//Solde au dateDeb
					//Total débit
					double tDebit = 0;
					Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
							   + " where a.numCpt='"+account.getNumCpt()+"' and g.date <= '"+dateDeb+"'");
					if(q1.getSingleResult() != null){
						tDebit = (Double) q1.getSingleResult(); 
					}				
					//total crédit
					double tCredit = 0 ; 
					Query q2 = em.createQuery("select sum(g.credit) from Grandlivre g join g.account a"
						   + " where a.numCpt='"+account.getNumCpt()+"' and g.date <= '"+dateDeb+"'");
					if(q2.getSingleResult() != null){
						tCredit = (Double)q2.getSingleResult();
					}	
					
					double soldInit = tDebit - tCredit;
					
					
					//Solde au dateFin 
					double tDebit2 = 0;
					Query quer1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
							   + " where a.numCpt='"+account.getNumCpt()+"' and g.date <= '"+dateFin+"'");
					if(quer1.getSingleResult() != null){
						tDebit2 = (Double) quer1.getSingleResult(); 
					}				
					//total crédit
					double tCredit2 = 0 ; 
					Query quer2 = em.createQuery("select sum(g.credit) from Grandlivre g join g.account a"
						   + " where a.numCpt='"+account.getNumCpt()+"' and g.date <= '"+dateFin+"'");
					if(quer2.getSingleResult() != null){
						tCredit2 = (Double)quer2.getSingleResult();
					}	
					
					double soldFinal = tDebit2 - tCredit2;

					b.setNumCompte(account.getNumCpt());
					b.setLibele(account.getLabel());      
					b.setSoldeInit(soldInit);
					b.setSommeDeb(0); 
					b.setSommeCred(0);  
					b.setStat("");
					b.setSoldeFin(soldFinal); 
					
					lCompt.add(b);
				}
				bilan.setCompteParent(parent.getLabel());
				bilan.setListCompte(lCompt); 
				result.add(bilan);
			}
			
		}
		return result;
	}

	//affiche une liste des crédits qui peuvent être déclasser 
	@Override
	public  List<AfficheListeCreditDeclasser> declasserCredit(String dateDeb) {
		
		 List<DemandeCredit> listCredit = new ArrayList<DemandeCredit>();
		 String sql = "select d from DemandeCredit d where d.approbationStatut = 'Demande decaissé'";
		 TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		 listCredit = query.getResultList();
		 
		 List<AfficheListeCreditDeclasser> retour = new ArrayList<AfficheListeCreditDeclasser>();
		 
		 
		 for (DemandeCredit demandeCredit : listCredit) {
			 System.out.println("credit: "+ demandeCredit.getNumCredit());
			AfficheListeCreditDeclasser declasse = new AfficheListeCreditDeclasser();		
			
			//calcul nombre jours de retard de chaque crédit
			Long diff = CreditServiceImpl.calculDifferenceDate(dateDeb, demandeCredit.getNumCredit());
			if(diff > 30){// && diff < 90
				
				//selection de dernière remboursement
				Query qr = em.createQuery("SELECT MAX(r.dateRemb) FROM Remboursement r JOIN r.demandeCredit numDem"
						+ " WHERE numDem.numCredit = :numero AND r.typeAction= :x");
				qr.setParameter("numero", demandeCredit.getNumCredit());		
				qr.setParameter("x", "Remboursement");
				
				String dateDer = (String) qr.getSingleResult();
				
				String d = "aucun";
				
				if(dateDer != null)
					d=dateDer;
				
				String nom = "";
				String prenom = "";
				
				if(demandeCredit.getIndividuel() != null){
					nom = demandeCredit.getIndividuel().getNomClient();
					prenom = demandeCredit.getIndividuel().getPrenomClient();
				}else if(demandeCredit.getGroupe() != null){
					nom = demandeCredit.getGroupe().getNomGroupe();
					prenom = "";
				}
				
				declasse.setNumCred(demandeCredit.getNumCredit());
				declasse.setNom(nom);
				declasse.setPrenom(prenom);
				declasse.setCodeProduit(demandeCredit.getProduitCredit().getIdProdCredit());
				declasse.setMontantCred(demandeCredit.getMontantApproved());
				declasse.setInteret(demandeCredit.getInteret_total());
				declasse.setPrincArr(demandeCredit.getPrincipale_total());
				declasse.setInteretArr(0);
				declasse.setPenaliteArr(0);
				declasse.setNbJourRetar(diff);
				declasse.setDernierRemb(d); 
				declasse.setTotalArr(demandeCredit.getSolde_total());
				
				if(diff < 90){
					declasse.setStat("VNI");
				}else if(diff > 90){
					declasse.setStat("Douteux");
				}			
				retour.add(declasse);			
			}
		}
		 
		 return retour;
		
	}

	//Enregistrement des crédits déclassé
	@Override
	public boolean saveDeclassement(String dateDeb, int user) {
		
		 List<DemandeCredit> listCredit = new ArrayList<DemandeCredit>();
		 String sql = "select d from DemandeCredit d where d.approbationStatut = 'Demande decaissé'";
		 TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		 listCredit = query.getResultList();
		 
		 for (DemandeCredit demandeCredit : listCredit) {
			//calcul nombre jours de retard de chaque crédit
			Long diff = CreditServiceImpl.calculDifferenceDate(dateDeb, demandeCredit.getNumCredit());
			
			//
			if(diff > 30){
				System.out.println("credit: "+ demandeCredit.getNumCredit());						
				
				//instanciation de l'entité liste rouge
				ListeRouge lrs = new ListeRouge();
				//instanciation de client individuel
				Individuel inds = demandeCredit.getIndividuel(); 
				
				if(diff < 90){
					System.out.println("Remboursement en retard inferieur à 90 jours \n");					
					//Ajout du client au liste noir					
					inds.setSain(false);
					inds.setListeRouge(false);
					inds.setListeNoir(true);
				}				
				if(diff > 90){
					System.out.println("Remboursement en retard superieur à 90 jours \n");					
					
					//Ajout du client au liste rouge					
					inds.setSain(false);
					inds.setListeNoir(false);
					inds.setListeRouge(true);
					
					//ajout au liste rouge
					lrs.setDate(dateDeb); 
					lrs.setRaison("non remboursement du crédit "+ demandeCredit.getNumCredit()+", crédit plus de 3 mois");
					lrs.setIndividuel(inds); 
				}
				transaction.begin();
				em.flush();
				em.flush();
				em.persist(lrs); 
				transaction.commit();
				em.refresh(inds);
				em.refresh(demandeCredit); 
				em.refresh(lrs);
				System.out.println("Changement de critère client");
		
				//Imputation comptable
				//Incrementation Tcode
				String indexTcode = CodeIncrement.genTcode(em);	
				
				//Utilisateur 				
				Utilisateur ut = em.find(Utilisateur.class, user);
				
				//Instance du grand livre débit et crédit
				Grandlivre debit = new Grandlivre();
				Grandlivre credit1 = new Grandlivre();
				
				//instance du configuration grand livre crédit
				ConfigGlCredit glVNI = demandeCredit.getProduitCredit().getConfigGlCredit();
				
				//capital total restant
				double capital = demandeCredit.getPrincipale_total();
				
				//Declassement au VNI
				if(demandeCredit.getIndividuel().isListeNoir() == true){
					//Instance de vérification grand livre
		
					Account accVNI = CodeIncrement.getAcount(em, "263");//glVNI.getComptVNI();
					double soldVni = accVNI.getSoldeProgressif() + capital;
					
					//Information de compte à débité : le compte VNI 26xxxx
					debit.setTcode(indexTcode);
					debit.setDate(dateDeb);
					debit.setDescr("declassement au VNI du crédit "
							+ demandeCredit.getNumCredit());
					debit.setPiece("piece "+demandeCredit.getNumCredit());
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(accVNI.getNumCpt());
					debit.setDebit(capital);
					
					debit.setCodeInd(demandeCredit.getIndividuel());
					debit.setDemandeCredit(demandeCredit); 
					debit.setUtilisateur(ut); 
					debit.setAccount(accVNI);
					debit.setSolde(soldVni);
					accVNI.setSoldeProgressif(soldVni); 
					
					//Information du compte à crédité
					Account accCred = CodeIncrement.getAcount(em, glVNI.getCptPrincEnCoursInd());
					double sdCrd = accCred.getSoldeProgressif() - capital;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(dateDeb);
					credit1.setDescr("declassement au VNI du capital du crédit "
							+ demandeCredit.getNumCredit());
					credit1.setPiece("piece "+demandeCredit.getNumCredit());
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(glVNI.getCptPrincEnCoursInd());
					credit1.setCredit(capital);
					
					credit1.setCodeInd(demandeCredit.getIndividuel());
					credit1.setDemandeCredit(demandeCredit); 
					credit1.setUtilisateur(ut); 
					credit1.setAccount(accCred);
					credit1.setSolde(sdCrd);
					accCred.setSoldeProgressif(sdCrd); 		
					
					try {
						
						transaction.begin();
						em.flush();
						em.flush();			
						em.persist(debit);
						em.persist(credit1);
						transaction.commit();
						em.refresh(debit);
						em.refresh(credit1);						
						em.refresh(accCred);
						em.refresh(accVNI);
						System.out.println("Declassement au VNI du crédit n° "+ demandeCredit.getNumCredit() +" bien enregistré");
						System.out.println("Enregistrement au Vni du compte "+demandeCredit.getNumCredit());
						//return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				if(demandeCredit.getIndividuel().isListeRouge() == true){
					
					//Instance du compte 27xxxx
					Account accDouteux = CodeIncrement.getAcount(em, "27");
					
					double soldeCompteDouteux = accDouteux.getSoldeProgressif() + capital;
					
					//Information de compte à débité : le compte 27xxxx
					debit.setTcode(indexTcode);
					debit.setDate(dateDeb);
					debit.setDescr("declassement au compte douteux du crédit "
							+ demandeCredit.getNumCredit());
					debit.setPiece("piece "+demandeCredit.getNumCredit());
					debit.setUserId(ut.getNomUtilisateur());
					debit.setCompte(accDouteux.getNumCpt());
					debit.setDebit(capital);
					
					debit.setCodeInd(demandeCredit.getIndividuel());
					debit.setDemandeCredit(demandeCredit); 
					debit.setUtilisateur(ut); 
					debit.setAccount(accDouteux);
					debit.setSolde(soldeCompteDouteux);
					accDouteux.setSoldeProgressif(soldeCompteDouteux); 
					
					
					// Information de compte à crédité capital
					Account accCred = CodeIncrement.getAcount(em, glVNI.getCptPrincEnCoursInd());
					double sdCrd = accCred.getSoldeProgressif() - capital;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(dateDeb);
					credit1.setDescr("declassement au compte douteux du principal du crédit "
							+ demandeCredit.getNumCredit());
					credit1.setPiece("piece "+demandeCredit.getNumCredit());
					credit1.setUserId(ut.getNomUtilisateur());
					credit1.setCompte(glVNI.getCptPrincEnCoursInd());
					credit1.setCredit(capital);
					
					credit1.setCodeInd(demandeCredit.getIndividuel());
					credit1.setDemandeCredit(demandeCredit); 
					credit1.setUtilisateur(ut); 
					credit1.setAccount(accCred);
					credit1.setSolde(sdCrd);
					accCred.setSoldeProgressif(sdCrd); 
					
					try {
						
						transaction.begin();
						em.flush();
						em.flush();
						em.persist(debit);
						em.persist(credit1);
						transaction.commit();
						em.refresh(debit);
						em.refresh(credit1);
						System.out.println("Declassement au Douteux du crédit n° "+ demandeCredit.getNumCredit() +" bien enregistré");
						//return true;
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
			}
		}	 
		
		return true;
	}
	
	

	//resultat financiers par période
	@Override
    public List<AfficheBilan> getResultat(String dateDeb, String dateFin) {	
		
		List<AfficheBilan> result = new ArrayList<AfficheBilan>();
		
		int nb = 7;	
		
		for (int i = 6; i <= nb; i++) {
			String num = String.valueOf(i); 			
			//compte parent		
			Account parent = CodeIncrement.getAcount(em, num);
			AfficheBilan bilan = new AfficheBilan();
			
			String sql = "select distinct g.account from Grandlivre g join g.account a "
					+ " where a.numCpt like '" + num + "%' and g.date between '"+dateDeb+"' and '"+dateFin+"' order by a.numCpt asc";
			List<Account> ac = new ArrayList<Account>();
			TypedQuery<Account> query = em.createQuery(sql, Account.class);
			
			if(!query.getResultList().isEmpty()){
				ac = query.getResultList();					
				List<AfficheBalance> lCompt = new ArrayList<AfficheBalance>();
				
				for (Account account : ac) {
					AfficheBalance b = new AfficheBalance();
					
					//Solde au dateDeb
					//Total débit
					double tDebit = 0;
					Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
							   + " where a.numCpt='"+account.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
					if(q1.getSingleResult() != null){
						tDebit = (Double) q1.getSingleResult(); 
					}				
					//total crédit
					double tCredit = 0 ; 
					Query q2 = em.createQuery("select sum(g.credit) from Grandlivre g join g.account a"
						   + " where a.numCpt='"+account.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
					if(q2.getSingleResult() != null){
						tCredit = (Double)q2.getSingleResult();
					}	
					
					double solde = tDebit - tCredit;
					
					if(num.equals("6")){
						b.setSommeCred(solde); 
						b.setSommeDeb(0);
					}
					else{
						b.setSommeCred(0); 
						b.setSommeDeb(solde);
					}

					b.setNumCompte(account.getNumCpt());
					b.setLibele(account.getLabel());      
					b.setSoldeInit(0); 
					b.setStat("");
					b.setSoldeFin(0); 
					
					lCompt.add(b);
				}
				bilan.setCompteParent(parent.getLabel());
				bilan.setListCompte(lCompt); 
				result.add(bilan);
			}
		
		}		
		return result;
	}
	
	
	//Enregistrement configuration transaction
	@Override
	public boolean saveConfigTransaction(int caise, int banque) {
		Account cCaisse = em.find(Account.class, caise);
		Account cBanque = em.find(Account.class, banque);
		ConfigTransactionCompta conf = new ConfigTransactionCompta();
		conf.setCaisse(cCaisse);
		conf.setBanque(cBanque); 
		try {
			transaction.begin();
			em.persist(conf); 
			transaction.commit();
			em.refresh(conf); 
			System.out.println("Configuration transaction enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Enregistrement configuration comptable
	@Override
	public ConfigTransactionCompta getConfigCompta() {		
		return em.find(ConfigTransactionCompta.class, 1);
	}

	//Ajout opération view
	@Override
	public boolean addOperationView(OperationView operation) {
		try {
			transaction.begin();
			em.persist(operation); 
			transaction.commit();
			em.refresh(operation); 
			System.out.println("Nouveau ligne d'opération view");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//List des opérations view
	@Override
	public List<OperationView> listOperationView() {
		String sql = "select o from OperationView o";
		TypedQuery<OperationView> query = em.createQuery(sql,OperationView.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	//vider opération view
	@Override
	public boolean viderOperationView() {
		Query query = em.createNativeQuery("TRUNCATE TABLE operation_view");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table opération view vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//Modifier opération view
	@Override
	public OperationView updateOperationView(int id, OperationView operation) {
		OperationView op = em.find(OperationView.class, id);
		try {
			op.setNumCmpt(operation.getNumCmpt());
			op.setLabel(operation.getLabel());
			op.setCredit(operation.getCredit());
			op.setDebit(operation.getDebit());
			transaction.begin();
			em.flush();
			transaction.commit();
			System.out.println("opération view modifié");
			return op;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
