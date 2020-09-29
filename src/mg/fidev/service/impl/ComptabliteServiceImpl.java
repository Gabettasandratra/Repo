package mg.fidev.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Account;
import mg.fidev.model.Agence;
import mg.fidev.model.Analytique;
import mg.fidev.model.Budget;
import mg.fidev.model.Caisse;
import mg.fidev.model.Cloture;
import mg.fidev.model.ClotureCompte;
import mg.fidev.model.CompteAuxilliaire;
import mg.fidev.model.CompteDAT;
import mg.fidev.model.CompteEpargne;
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
import mg.fidev.utils.CompteCompta;
import mg.fidev.utils.HeaderCompta;
import mg.fidev.utils.MouvementCompta;
import mg.fidev.utils.PositionCompta4;
import mg.fidev.utils.PositionCompta5;
import mg.fidev.utils.PositionCompta6;
import mg.fidev.utils.PositionCompta7;
import mg.fidev.utils.PositionCompta8;
import mg.fidev.utils.RubriqueCompta;

@WebService(name="comptabliteService", targetNamespace="http://fidev.mg.comptabliteService", serviceName="comptabliteService",
portName="comptabliteServicePort", endpointInterface="mg.fidev.service.ComptabiliteService")
public class ComptabliteServiceImpl implements ComptabiliteService {
	
	//Declaration de l'EntityManager 
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	
	//Enregistrement transaction debit, credit pour Cr�dit
	public static void saveImputationLoan(String indexTcode, String date, String description, String piece, double credit,
			double debit, Utilisateur ut, Individuel ind, Groupe grp, DemandeCredit dm, Agence ag, Account ac){
		System.out.println("Imputation pour cr�dit");
		double sd = 0;	
		if(debit != 0.0){
			sd = ac.getSoldeProgressif() + debit;
			System.out.println("Transaction d�bit");
		}
		if(credit != 0.0){
			sd = ac.getSoldeProgressif() - credit;
			System.out.println("Transaction cr�dit");			
		}
								
		ac.setSoldeProgressif(sd);	
		
		Grandlivre g = new 
		 Grandlivre(indexTcode, date, description , piece, credit, debit, sd, 
					ut, dm.getIndividuel(), null, dm, null, ac);
		System.out.println("Account num�ro :"+ ac.getNumCpt());
		try {
			transaction.begin();
			em.flush();
			em.persist(g);
			transaction.commit();
			em.refresh(g); 
			System.out.println("Transaction enregistr�");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur enregistrement debit loan");
		}
		
	}
	
	//Enregistrement transaction debit, credit pour Epargne
	public static void saveImputationEpargne(String indexTcode, String date, String description, String piece, double credit,
			double debit, Utilisateur ut, Individuel ind, Groupe grp, CompteEpargne cpt, Agence ag, Account ac){
		System.out.println("Imputation pour �pargne");
		double sd = 0;	
		if(debit != 0.0){
			sd = ac.getSoldeProgressif() + debit;
			System.out.println("Transaction d�bit");
		}
		if(credit != 0.0){
			sd = ac.getSoldeProgressif() - credit;
			System.out.println("Transaction cr�dit");			
		}
								
		ac.setSoldeProgressif(sd);	
		
		Grandlivre g = new 
			Grandlivre(indexTcode, date, description, piece, credit, debit, sd, ut,
					ind, grp, ag, cpt, ac)	;
		
		System.out.println("Account num�ro :"+ ac.getNumCpt());
		try {
			transaction.begin();
			em.flush();
			em.persist(g);
			transaction.commit();
			em.refresh(g); 
			System.out.println("Transaction enregistr�");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur enregistrement debit loan");
		}
		
	}
	
	//Enregistrement transaction debit, credit pour D�p�t � terme 
	public static void saveImputationDAT(String indexTcode, String date, String description, String piece, double credit,
			double debit, Utilisateur ut, Individuel ind, Groupe grp, CompteDAT dat, Agence ag, Account ac){
		System.out.println("Imputation pour �pargne");
		double sd = 0;	

		if(debit != 0.0){
			sd = ac.getSoldeProgressif() + debit;
			System.out.println("Transaction d�bit");
		}
		if(credit != 0.0){
			sd = ac.getSoldeProgressif() - credit;
			System.out.println("Transaction cr�dit");			
		}
								
		ac.setSoldeProgressif(sd);	
		
		Grandlivre g = new 
			Grandlivre(indexTcode, date, description, piece, credit, debit, sd, ut,
					ind, grp, ag, dat, ac)	;
		
		System.out.println("Account num�ro :"+ ac.getNumCpt());
		try {
			transaction.begin();
			em.flush();
			em.persist(g);
			transaction.commit();
			em.refresh(g); 
			System.out.println("Transaction enregistr�");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur enregistrement imputation");
		}
		
	}
	
	//Liste des comptes � 4 positions de plus
	@Override
	public List<Account> getCompte() {
		String sql = "select a from Account a where a.hlevel >= '6' order by a.numCpt asc";
		TypedQuery<Account> result = em.createQuery(sql, Account.class);
		return result.getResultList();
	}
	
	//Chercher comptes � 4 positions de plus
	@Override
	public List<Account> findComptes(String compte) {
		String sql = "select a from Account a where a.hlevel >= '6' and a.isActive = '"+true+"' "
				+ "and a.numCpt like '"+ compte +"%' order by a.numCpt asc";
		TypedQuery<Account> result = em.createQuery(sql, Account.class);
		return result.getResultList();
	}
		
	//FONCTION QUI AFFICHE LE GRAND LIVRE 
	@Override
	public List<Grandlivre> afficheGrandLivre(String compte,String nomUtilisateur,String dateDeb, String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		try {
			//Listes de donn�es dans le grand livre: affichage entre deux periode
			
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
				System.err.println("Il n'y a pas de resultat");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//Recup�re le solde d'une compte avant la date en param�tre
	@Override
	public double getSoldeAvant(String num, String date) {
		
		double tDebit = 0;
		Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
				   + " where a.numCpt='"+ num +"' and g.date < '"+ date +"'");
		if(q1.getSingleResult() != null){
			tDebit = (Double) q1.getSingleResult(); 
		}
		System.out.println("Total solde d�bit avant le "+ date +" est "+ (tDebit));
		//total cr�dit
		double tCredit = 0 ; 
		Query q2 = em.createQuery("select sum(g.credit) from Grandlivre g join g.account a"
			   + " where a.numCpt='"+ num +"' and g.date < '"+ date +"'");
		if(q2.getSingleResult() != null){
			tCredit = (Double)q2.getSingleResult();
		}
		System.out.println("Total solde cr�dit avant le "+ date +" est "+ (tCredit));
		System.out.println("Total solde avant le "+ date +" est "+ (tDebit - tCredit));
		
		return (tDebit - tCredit);
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
			System.out.println("Nouveau compte caisse ins�r�");
			return true;
		} catch (Exception e) {
			System.err.println("Erreur d'insertion de compte caisse");
			return false;
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
				compte.setHlevel(parrent.getHlevel() + 1);
				parrent.setActive(false);
			}
			
			transaction.begin();
			em.flush();
			em.persist(compte);
			transaction.commit();
			em.refresh(compte);
			if(parrent != null)
				em.refresh(parrent); 
			return compte;				
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	//Recup�rer les comptes comptable
	@Override
	public List<Account> getAccounts() {
		
		try {
			String sql = "SELECT c FROM Account c join c.account a order by a.numCpt asc";	// ORDER BY c.account and 
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
			String compte,String compte2,double debit,String analytique,String auxilliaire,String budget, int user) {
		
		//instaciation de l'utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, user);
		//instance de compte que l'utilisateur saisit 
		Account cmpt =CodeIncrement.getAcount(em, compte);
		
		//Instance de compte d'equilibre � la configuration
		Account cmpt2 = CodeIncrement.getAcount(em, compte2);
		Budget bdg = em.find(Budget.class, budget);
		Analytique anlt = saveAnalytique(analytique);
		CompteAuxilliaire cmpAux = saveAuxilliaire(auxilliaire);
		
		//on instancie le grand livre	
		Grandlivre debiter = new Grandlivre();
		Grandlivre crediter = new Grandlivre();
		
		debiter.setTcode(CodeIncrement.genTcode(em));
		debiter.setDate(date);
		debiter.setDescr(description);
		debiter.setPiece(piece);
		debiter.setAccount(cmpt);
		debiter.setBudget(bdg);
		debiter.setAnalytique(anlt);
		debiter.setAuxilliaire(cmpAux); 
		
		debiter.setDebit(debit);
		debiter.setUserId(ut.getNomUtilisateur());
		debiter.setSolde(cmpt.getSoldeProgressif()+debit);
		cmpt.setSoldeProgressif(cmpt.getSoldeProgressif()+debit);
		
		
		crediter.setTcode(CodeIncrement.genTcode(em));
		crediter.setDate(date);
		crediter.setDescr(description);
		crediter.setPiece(piece);
		crediter.setAccount(cmpt2);
		crediter.setBudget(bdg);
		crediter.setAnalytique(anlt);
		crediter.setAuxilliaire(cmpAux); 
		
		crediter.setCredit(debit);
		crediter.setUserId(ut.getNomUtilisateur());
		crediter.setSolde(cmpt2.getSoldeProgressif()-debit);
		cmpt2.setSoldeProgressif(cmpt2.getSoldeProgressif()-debit);
		
		try {		
			transaction.begin();
			em.persist(debiter);
			em.persist(crediter);
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

	//Get grand livre par code client
	@Override
	public List<Grandlivre> grandLivreClient(String codeCli, String codeGrp) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		try {
			//Listes de donn�es dans le grand livre: affichage par code Client
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
			System.out.println("libell�: "+ account.getLabel()+"\n");
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
			 System.out.println("D�bit : "+ String.valueOf(grandLivre.getDebit()));
			 System.out.println("Cr�dit : "+ String.valueOf(grandLivre.getCredit()) +"\n");
			
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
	
	//Enregistrement Analytique
		static CompteAuxilliaire saveAuxilliaire(String nom) {
			CompteAuxilliaire auxiliaire = new CompteAuxilliaire();
			try {
				Query verification = em
						.createQuery("select a from CompteAuxilliaire a where a.nom = :x");
				verification.setParameter("x", nom);

				if (!verification.getResultList().isEmpty()) {
					auxiliaire = (CompteAuxilliaire) verification.getSingleResult();
					return auxiliaire;
				} else {
					auxiliaire.setId(CodeIncrement.getCodeAuxilliaire(em));
					auxiliaire.setNom(nom);
					transaction.begin();
					em.persist(auxiliaire);
					transaction.commit();
					em.refresh(auxiliaire);
					return auxiliaire;
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
	
	@Override
	public List<CompteAuxilliaire> getAllAuxilliaire() {

		String sql = "select a from CompteAuxilliaire a";
		TypedQuery<CompteAuxilliaire> query = em.createQuery(sql, CompteAuxilliaire.class);

		if (!query.getResultList().isEmpty()) {
			return  query.getResultList();
		}

		return null;
	}

	@Override
	public List<CompteAuxilliaire> chercherAuxilliaire(String code) {
		String sql = "select a from CompteAuxilliaire a where a.id like '" + code + "%'";
		TypedQuery<CompteAuxilliaire> query = em.createQuery(sql,CompteAuxilliaire.class);
		if (!query.getResultList().isEmpty()) {
			return query.getResultList();
		}
		return null;
	}
	
	
	//Enregistrement op�ration divers comptable
	@Override
	public boolean saveOperationDivers(String date, String piece,
			String description,String analytique, String auxilliaire, String budget, int user) {

		// on selectionne l'utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, user);

		Budget bdg = em.find(Budget.class, budget);
		Analytique anlt = saveAnalytique(analytique);
		CompteAuxilliaire cmpAux = saveAuxilliaire(auxilliaire);
		
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
		debiter.setAuxilliaire(cmpAux); 
		
		Grandlivre crediter = new Grandlivre();
		crediter.setTcode(CodeIncrement.genTcode(em));
		crediter.setDate(date);
		crediter.setDescr(description);
		crediter.setPiece(piece);
		crediter.setUserId(ut.getNomUtilisateur());
		crediter.setUtilisateur(ut);
		crediter.setAnalytique(anlt);
		crediter.setBudget(bdg); 
		crediter.setAuxilliaire(cmpAux); 

		List<OperationView> listOperation = listOperationView();
		
		for (OperationView operationView : listOperation) {
			//On test la valeur de credit et debit dans l'op�ration view
			//si valeur de credit est �gal � 0 donc l'op�ration est d�bit
		
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
			em.persist(debiter);
			em.persist(crediter);
			em.flush();
			transaction.commit();
			System.out.println("Enregistrement op�ration reussit");
			//On va vider l'op�ration view apr�s l'enregistrement
			//viderOperationView();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur enregistrement d�bit");
			return false;
		}
		
		//return false;
	}

	//Get grand livre budg�taire
	@Override
	public List<Grandlivre> getGrandLivreBudget(String code, String dateDeb,
			String dateFin) {
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
			return query.getResultList();
		}
		return null;
	}

	//Get grand livre analytique
	@Override
	public List<Grandlivre> getGrandLivreAnalytique(String code,
			String dateDeb, String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();

		String sql = "select g from Grandlivre g join g.analytique a where g.analytique is not null";

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
			System.out.println("D�bit"+ grandlivre.getDebit());  
			System.out.println("Cr�dit :"+ grandlivre.getCredit()+"\n");
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
		return em.createQuery(sql, Account.class).getResultList();
	}
	
	static  List<Account> getDistAccount(String dateDeb,String dateFin){
		String sql = "select distinct g.account from Grandlivre g join g.account a "
				+ " where g.date between '"+dateDeb+"' and '"+dateFin+"' order by a.numCpt asc";
		return em.createQuery(sql, Account.class).getResultList();
	}

	@Override
	public List<AfficheBalance> getBalance(String dateDeb,String dateFin) {
		
		 List<AfficheBalance> result = new ArrayList<AfficheBalance>();
		
		List<Account> distinct = getDistAccount(dateDeb, dateFin);
		
		for (Account ac : distinct) {
			AfficheBalance affiche = new AfficheBalance();
			
			//Total d�bit
			double tDebit = 0;
			Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
					   + " where a.numCpt='"+ac.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
			if(q1.getSingleResult() != null){
				tDebit = (Double) q1.getSingleResult(); 
			}
			
			//total cr�dit
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
					//Total d�bit
					double tDebit = 0;
					Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
							   + " where a.numCpt='"+account.getNumCpt()+"' and g.date <= '"+dateDeb+"'");
					if(q1.getSingleResult() != null){
						tDebit = (Double) q1.getSingleResult(); 
					}				
					//total cr�dit
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
					//total cr�dit
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

	//Analyse comptes
	@Override
	public List<Grandlivre> getAnalyseCompte(String compte, String compte2,
			String dateDeb, String dateFin) {

		String sql = "select g from Grandlivre g join g.account ac ";
		if(!compte.equals("") || !compte2.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			sql +="where ";
			
			/*if(!nomUtilisateur.equals("")){
				sql+= "g.userId ='"+nomUtilisateur+"'";
				
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}			
			}*/
			
			if(!compte.equals("") && compte2.equals("")){
				sql+= " ac.numCpt ='"+compte+"'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	   
			}
			
			if(!compte.equals("") && !compte2.equals("")){
				sql+= " ac.numCpt between '"+compte+"' and '"+compte2+"'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	   
			}
			
			if(!dateDeb.equals("") && dateFin.equals("") && compte.equals("") && compte2.equals("")){
				sql+= "g.date ='"+dateDeb+"'";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("") && compte.equals("") && compte2.equals("")){
				sql+= "g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
			}	
			
		}
		
		sql +=" order by g.tcode asc";
		//sql +=" order by ac.numCpt asc";
		
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		if(!(query.getResultList().isEmpty()))
			return query.getResultList();
		else{
			System.err.println("Il n'y a pas de resultat");
			return null;			
		}
		
	}
	
	//affiche une liste des cr�dits qui peuvent �tre d�classer 
	@Override
	public  List<AfficheListeCreditDeclasser> declasserCredit(String dateDeb) {
		
		 List<DemandeCredit> listCredit = new ArrayList<DemandeCredit>();
		 String sql = "select d from DemandeCredit d where d.approbationStatut = 'Demande decaiss�'";
		 TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		 listCredit = query.getResultList();
		 
		 List<AfficheListeCreditDeclasser> retour = new ArrayList<AfficheListeCreditDeclasser>();
		 
		 
		 for (DemandeCredit demandeCredit : listCredit) {
			 System.out.println("credit: "+ demandeCredit.getNumCredit());
			AfficheListeCreditDeclasser declasse = new AfficheListeCreditDeclasser();		
			
			//calcul nombre jours de retard de chaque cr�dit
			Long diff = CreditServiceImpl.calculDifferenceDate(dateDeb, demandeCredit.getNumCredit());
			if(diff > 30){// && diff < 90
				
				//selection de derni�re remboursement
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

	//Enregistrement des cr�dits d�class�
	@Override
	public boolean saveDeclassement(String dateDeb, int user) {
		
		 List<DemandeCredit> listCredit = new ArrayList<DemandeCredit>();
		 String sql = "select d from DemandeCredit d where d.approbationStatut = 'Demande decaiss�'";
		 TypedQuery<DemandeCredit> query = em.createQuery(sql,DemandeCredit.class);
		 listCredit = query.getResultList();
		 
		 for (DemandeCredit demandeCredit : listCredit) {
			//calcul nombre jours de retard de chaque cr�dit
			Long diff = CreditServiceImpl.calculDifferenceDate(dateDeb, demandeCredit.getNumCredit());
			
			//
			if(diff > 30){
				System.out.println("credit: "+ demandeCredit.getNumCredit());						
				
				//instanciation de l'entit� liste rouge
				ListeRouge lrs = new ListeRouge();
				//instanciation de client individuel
				Individuel inds = demandeCredit.getIndividuel(); 
				
				if(diff < 90){
					System.out.println("Remboursement en retard inferieur � 90 jours \n");					
					//Ajout du client au liste noir					
					inds.setSain(false);
					inds.setListeRouge(false);
					inds.setListeNoir(true);
				}				
				if(diff > 90){
					System.out.println("Remboursement en retard superieur � 90 jours \n");					
					
					//Ajout du client au liste rouge					
					inds.setSain(false);
					inds.setListeNoir(false);
					inds.setListeRouge(true);
					
					//ajout au liste rouge
					lrs.setDate(dateDeb); 
					lrs.setRaison("non remboursement du cr�dit "+ demandeCredit.getNumCredit()+", cr�dit plus de 3 mois");
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
				System.out.println("Changement de crit�re client");
		
				//Imputation comptable
				//Incrementation Tcode
				String indexTcode = CodeIncrement.genTcode(em);	
				
				//Utilisateur 				
				Utilisateur ut = em.find(Utilisateur.class, user);
				
				//Instance du grand livre d�bit et cr�dit
				Grandlivre debit = new Grandlivre();
				Grandlivre credit1 = new Grandlivre();
				
				//instance du configuration grand livre cr�dit
				ConfigGlCredit glVNI = demandeCredit.getProduitCredit().getConfigGlCredit();
				
				//capital total restant
				double capital = demandeCredit.getPrincipale_total();
				
				//Declassement au VNI
				if(demandeCredit.getIndividuel().isListeNoir() == true){
					//Instance de v�rification grand livre
		
					Account accVNI = CodeIncrement.getAcount(em, "263");//glVNI.getComptVNI();
					double soldVni = accVNI.getSoldeProgressif() + capital;
					
					//Information de compte � d�bit� : le compte VNI 26xxxx
					debit.setTcode(indexTcode);
					debit.setDate(dateDeb);
					debit.setDescr("declassement au VNI du cr�dit "
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
					
					//Information du compte � cr�dit�
					Account accCred = CodeIncrement.getAcount(em, glVNI.getCptPrincEnCoursInd());
					double sdCrd = accCred.getSoldeProgressif() - capital;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(dateDeb);
					credit1.setDescr("declassement au VNI du capital du cr�dit "
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
						System.out.println("Declassement au VNI du cr�dit n� "+ demandeCredit.getNumCredit() +" bien enregistr�");
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
					
					//Information de compte � d�bit� : le compte 27xxxx
					debit.setTcode(indexTcode);
					debit.setDate(dateDeb);
					debit.setDescr("declassement au compte douteux du cr�dit "
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
					
					
					// Information de compte � cr�dit� capital
					Account accCred = CodeIncrement.getAcount(em, glVNI.getCptPrincEnCoursInd());
					double sdCrd = accCred.getSoldeProgressif() - capital;
					
					credit1.setTcode(indexTcode);
					credit1.setDate(dateDeb);
					credit1.setDescr("declassement au compte douteux du principal du cr�dit "
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
						System.out.println("Declassement au Douteux du cr�dit n� "+ demandeCredit.getNumCredit() +" bien enregistr�");
						//return true;
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
			}
		}	 
		
		return true;
	}
	
	//resultat financiers par p�riode
	@Override
    public List<AfficheBilan> getResultat(String dateDeb, String dateFin) {	
		
		List<AfficheBilan> result = new ArrayList<AfficheBilan>();
		
		int nb = 6;	
		
		for (int i = 7; i >= nb; i--) {
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
					//Total d�bit
					double tDebit = 0;
					Query q1 = em.createQuery("select sum(g.debit) from Grandlivre g join g.account a"
							   + " where a.numCpt='"+account.getNumCpt()+"' and g.date between '"+dateDeb+"' and '"+dateFin+"'");
					if(q1.getSingleResult() != null){
						tDebit = (Double) q1.getSingleResult(); 
					}				
					//total cr�dit
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
			System.out.println("Configuration transaction enregistr�");
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

	//Ajout op�ration view
	@Override
	public boolean addOperationView(OperationView operation) {
		try {
			transaction.begin();
			em.persist(operation); 
			transaction.commit();
			em.refresh(operation); 
			System.out.println("Nouveau ligne d'op�ration view");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//List des op�rations view
	@Override
	public List<OperationView> listOperationView() {
		String sql = "select o from OperationView o";
		TypedQuery<OperationView> query = em.createQuery(sql,OperationView.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	//vider op�ration view
	@Override
	public boolean viderOperationView() {
		Query query = em.createNativeQuery("TRUNCATE TABLE operation_view");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table op�ration view vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//Modifier op�ration view
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
			System.out.println("op�ration view modifi�");
			return op;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//-------------------------------------------------------------------------------------------------------
	/******************************** Clotur compte *******************************************/
	@Override
	public boolean addCloture(String dateDeb, String dateFin, String desc, int user) {
		Cloture cl = new Cloture();
		Utilisateur ut = em.find(Utilisateur.class, user);
		List<Account> accounts = getDistAccount(dateDeb, dateFin);
		
		LocalDate now = LocalDate.now();
		
		cl.setDateSave(now.toString());
		cl.setDateDebut(dateDeb);
		cl.setDateFin(dateFin); 
		cl.setUser(ut); 
		if(desc.equalsIgnoreCase("Jour"))
			cl.setJour(true);
		else if (desc.equalsIgnoreCase("Mois"))
			cl.setMois(true);
		else if (desc.equalsIgnoreCase("Annee"))
			cl.setAnnee(true);
		try {
			transaction.begin();
			em.persist(cl);
			transaction.commit();
			em.refresh(cl);
			for (Account account : accounts) {
				ClotureCompte cls = new ClotureCompte();
				cls.setCloture(cl); 
				cls.setAccount(account);
				cls.setSolde(account.getSoldeProgressif());
				double sd = account.getSoldeInit() + account.getSoldeProgressif();
				account.setSoldeInit(sd);
				account.setSoldeProgressif(0); 
				transaction.begin();
				em.flush();
				em.persist(cls); 
				transaction.commit();
				em.refresh(cls);
				em.refresh(account);
			}
			System.out.println("Cloture "+desc+" enregistr�!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur cloture");
			return false;
		}		
	}
	
	//Chercher cloture
	static Cloture getCloture(String date){
		String sql ="select c from Cloture c where c.dateDebut ='"+date+"'";
		TypedQuery<Cloture> q = em.createQuery(sql, Cloture.class);
		if(!q.getResultList().isEmpty())
			return q.getSingleResult();
		else 
			return null;
			
	}
	
	//V�rification cloture
	public static boolean verifieCloture(String date){
		Cloture c = getCloture(date);
		if(c != null){
			if(c.isJour() == true)
				return true;
			else 
				return false;			
		}else
			return false;
	}
		
	//Affiche plan comptable
	@Override   
	public List<HeaderCompta> getPlanCompta() {
		List<HeaderCompta> result = new ArrayList<HeaderCompta>();

		String sql = "select a from Account a where a.hlevel ='1'";
		TypedQuery<Account> qH = em.createQuery(sql, Account.class);
		
		for (Account a : qH.getResultList()) {
			String sqlPos = "select a from Account a join a.account ac where ac.numCpt = '"+ a.getNumCpt() +"' order by a.numCpt";//a.hlevel ='3' and
			TypedQuery<Account> qP = em.createQuery(sqlPos, Account.class);
			
			List<RubriqueCompta> r = new ArrayList<RubriqueCompta>();
			
			for (Account c : qP.getResultList()) {
				
				RubriqueCompta ru = new RubriqueCompta(c.getNumCpt(), c.getLabel(), c.getIsActive(), c.getDevise(), null);
				
				if(c.getIsHeader() == true){
					List<CompteCompta> comptes = new ArrayList<CompteCompta>();					
					
					for (Account a3 : c.getAccounts()) {
						
						CompteCompta cmpt = new 
								CompteCompta(a3.getNumCpt(), a3.getLabel(),	a3.getIsActive(), a3.getDevise(), null);
						
						if(a3.getIsHeader() == true){
							List<PositionCompta4> pos4 = new ArrayList<PositionCompta4>();							
												
							for (Account a4 : a3.getAccounts()) {
								PositionCompta4 p4 = new 
								PositionCompta4(a4.getNumCpt(), a4.getLabel(), a4.getIsActive(), a4.getDevise(), null);
								
								if(a4.getIsHeader() == true){
									List<PositionCompta5> pos5 = new ArrayList<PositionCompta5>();
									
									for (Account a5 : a4.getAccounts()) {
										
										PositionCompta5 p5 = new 
												PositionCompta5(a5.getNumCpt(), a5.getLabel(), a5.getIsActive(), a5.getDevise(), null);
										if(a5.getIsHeader() == true){
											
											List<PositionCompta6> pos6 = new ArrayList<PositionCompta6>();
											
											for (Account a6 : a5.getAccounts()) {												
												PositionCompta6 p6 = new 
														PositionCompta6(a6.getNumCpt(), a6.getLabel(), a6.getIsActive(), a6.getDevise(), null);
												
												if(a6.getIsHeader() == true){
													List<PositionCompta7> pos7 = new ArrayList<PositionCompta7>();
													
													for (Account a7 : a6.getAccounts()) {
														
														PositionCompta7 p7 = new 
																PositionCompta7(a7.getNumCpt(), a7.getLabel(), a7.getIsActive(), a7.getDevise(), null);
														
														if(a7.getIsHeader() == true){
															List<PositionCompta8> pos8 = new ArrayList<PositionCompta8>();
															
															for (Account a8: a7.getAccounts()) {
																PositionCompta8 p8 = new 
																		PositionCompta8(a8.getNumCpt(), a8.getLabel(), a8.getIsActive(), a8.getDevise());
																pos8.add(p8);
															}
															p7.setPos8(pos8); 
														}
														pos7.add(p7);
													}
													
													p6.setPos7(pos7); 
												}
												pos6.add(p6);
											}
											
											p5.setPos6(pos6); 
											
										}								
										pos5.add(p5);
									}
									
									p4.setPos5(pos5); 
								}
								pos4.add(p4);
							}
							
							cmpt.setPos4(pos4); 
						}
						
						comptes.add(cmpt);
					}					
					
					ru.setComptes(comptes); 
				}
				
				r.add(ru);				
			}
			
			HeaderCompta h = new HeaderCompta(a.getNumCpt(), a.getLabel(), a.getIsActive(), a.getDevise(), r);
			
			result.add(h);
		}
		
		return result;
	}
	
	static void testPlanCompta(){
		
		List<Account> result = new ArrayList<Account>();
		String sql = "select a from Account a where a.hlevel ='1'";
		TypedQuery<Account> qH = em.createQuery(sql, Account.class);
		
		for (Account a : qH.getResultList()) {
			String sqlPos = "select a from Account a join a.account ac where ac.numCpt = '"+ a.getNumCpt() +"'";//a.hlevel ='3' and
			TypedQuery<Account> qP = em.createQuery(sqlPos, Account.class);
			
			for (Account c : qP.getResultList()) {
				if(c.getIsHeader() == true){
					//List<Account> ls = c.getAccounts();
				}
					
			}
			result.add(a);
		}
	}

	//Reinitialiser solde
	@Override
	public boolean reinitialiserSolde() {
		String sql ="select a from Account a";
		TypedQuery<Account> q = em.createQuery(sql, Account.class);
		if(!q.getResultList().isEmpty()){
			List<Account> ac = q.getResultList();
			for (Account account : ac) {
				account.setSoldeInit(0.0);
				account.setSoldeProgressif(0.0); 
				try {
					transaction.begin();
					em.merge(account);
					transaction.commit();
					em.refresh(account); 
					return true;
				} catch (Exception e) {
					e.printStackTrace(); 
					return false;
				}
			}			
		}
		return false;
	}

	//Chercher code analytique par mot cl�
	@Override
	public List<Analytique> findCodeAn(String code) {
		String sql = "select a from Analytique a where a.id like '"+ code +"%' or a.nom like '"+ code +"%' "
				+ " order by a.nom asc";
		TypedQuery<Analytique> q = em.createQuery(sql, Analytique.class);
		
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Chercher code budg�taire par mot cl�
	@Override
	public List<Budget> findCodeBud(String code) {
		String sql = "select a from Budget a where a.code like '"+ code +"%' or a.nom like '"+ code +"%' "
				+ " order by a.nom asc";
		TypedQuery<Budget> q = em.createQuery(sql, Budget.class);
		
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Chercher compte auxilliaire par mot cl�
	@Override
	public List<CompteAuxilliaire> findCodeAux(String code) {
		String sql = "select a from CompteAuxilliaire a where a.id like '"+ code +"%' or a.nom like '"+ code +"%' "
				+ " order by a.nom asc";
		TypedQuery<CompteAuxilliaire> q = em.createQuery(sql, CompteAuxilliaire.class);
		
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Analyse compte analytique
	@Override
	public List<Grandlivre> getAnalyseCompteAn(String compte, String dateDeb,
			String dateFin) {
		String sql = "select g from Grandlivre g join g.analytique a ";
		if(!compte.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			sql +="where ";
		
			if(!compte.equals("")){
				sql+= " a.id ='"+compte+"'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	   
			}
						
			if(!dateDeb.equals("") && dateFin.equals("") && compte.equals("")){
				sql+= "g.date ='"+dateDeb+"'";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("") && compte.equals("")){
				sql+= "g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
			}	
			
		}
		
		sql +=" order by g.tcode asc";
		//sql +=" order by ac.numCpt asc";
		
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		if(!(query.getResultList().isEmpty()))
			return query.getResultList();
		else{
			System.err.println("Il n'y a pas de resultat");
			return null;			
		}
		
	}

	//Analyse compte budg�taires
	@Override
	public List<Grandlivre> getAnalyseCompteBud(String compte, String dateDeb,
			String dateFin) {
		String sql = "select g from Grandlivre g join g.budget a ";
		if(!compte.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			sql +="where ";
		
			if(!compte.equals("")){
				sql+= " a.code ='"+compte+"'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	   
			}
						
			if(!dateDeb.equals("") && dateFin.equals("") && compte.equals("")){
				sql+= "g.date ='"+dateDeb+"'";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("") && compte.equals("")){
				sql+= "g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
			}	
			
		}
		
		sql +=" order by g.tcode asc";
		//sql +=" order by ac.numCpt asc";
		
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		if(!(query.getResultList().isEmpty()))
			return query.getResultList();
		else{
			System.err.println("Il n'y a pas de resultat");
			return null;			
		}
	}

	//Analyse compte auxilliaires
	@Override
	public List<Grandlivre> getAnalyseCompteAux(String compte, String dateDeb,
			String dateFin) {
		String sql = "select g from Grandlivre g join g.auxilliaire a ";
		if(!compte.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			sql +="where ";
		
			if(!compte.equals("")){
				sql+= " a.id ='"+compte+"'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+= " AND g.date ='"+dateDeb+"'";				
				}
				if(!dateDeb.equals("") && !dateFin.equals("")){
					sql+= " AND g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
				}	   
			}
						
			if(!dateDeb.equals("") && dateFin.equals("") && compte.equals("")){
				sql+= "g.date ='"+dateDeb+"'";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("") && compte.equals("")){
				sql+= "g.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'";
			}	
			
		}
		
		sql +=" order by g.tcode asc";
		//sql +=" order by ac.numCpt asc";
		
		TypedQuery<Grandlivre> query = em.createQuery(sql,Grandlivre.class);
		if(!(query.getResultList().isEmpty()))
			return query.getResultList();
		else{
			System.err.println("Il n'y a pas de resultat");
			return null;			
		}
	}

}
