package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import mg.fidev.model.Acces;
import mg.fidev.model.Account;
import mg.fidev.model.Agence;
import mg.fidev.model.Caisse;
import mg.fidev.model.Fonction;
import mg.fidev.model.Personnel;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.UserService;
import mg.fidev.utils.CodeIncrement;

import org.mindrot.jbcrypt.BCrypt;

@WebService(name="UserService", targetNamespace="http://user.fidev.com/", serviceName="userService", portName="userServicePort", endpointInterface="mg.fidev.service.UserService")
public class UserServiceImpl implements UserService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	///	Méthode pour l'authentification d'un utilisateur
	@Override
	public Utilisateur authentifie(String loginUser, String mdpUser) {
		List<Acces> a = new ArrayList<Acces>();	//	Instance une liste d'accès pour stocker
												//	la liste d'accès d'un utilisateur quelconque
		
		//	Requête pour vérifier si le login et le mote de passe sont	valides et correspondent bien
		TypedQuery<Utilisateur> q = em.createQuery("select u from Utilisateur u where u.loginUtilisateur= :log", Utilisateur.class)
				.setParameter("log", loginUser);
		
		 List<Utilisateur> ut = new ArrayList<Utilisateur>();
		
		//	Traitement à faire en fonction du résultat de la requête
		//	Login et mot de passe valides ou non
		 
		System.out.println("Utilisateur ready !!");
		if(!(q.getResultList()).equals(null)){
			//	Instance utilisateur correspondant à la réponse de la requête précédente
			ut = q.getResultList();
			for (Utilisateur utilisateur : ut) {
				
				String mdpHash = utilisateur.getMdpUtilisateur();
				if(BCrypt.checkpw(mdpUser, mdpHash)){
					for(Acces ac : utilisateur.getFonction().getAcces()){
						System.out.println(ac.getTitreAcces());
						a.add(ac);
					}
					System.out.println("Connexion réussie");
					return utilisateur;
				}
				else{
					System.err.println("Mot de passe incorrect");
					return null;
				}
			}
			
			
		}
		else
			System.err.println("Utilisateur null !!");
		return null;
	}

	///	Méthode pour l'enregistrement d'un nouvel utilisateur
	@Override
	public boolean insertUser(String nomUser, String loginUser, String mdpUser, String mdpConf,
			String genreUser, String telUser,  int fonctionId) {//List<String> listCptCaisse,
		//	Instance nouvel utilisateur à insérer
		Utilisateur user = new Utilisateur();
		/*List<CompteCaisse> cptCaisse = new ArrayList<CompteCaisse>();
		for(String a : listCptCaisse){
		cptCaisse.add(em.find(CompteCaisse.class, a));
     	}
	  user.setCompteCaisses(cptCaisse);*/
		String passHash = BCrypt.hashpw(mdpUser, BCrypt.gensalt());
		
		//	Affectation informations utilisateur
		if(mdpUser.equals(mdpConf)){
			user.setNomUtilisateur(nomUser);
			user.setLoginUtilisateur(loginUser);
			user.setMdpUtilisateur(passHash);
			user.setGenreUser(genreUser);
			user.setTelUser(telUser);

			Fonction fct = em.find(Fonction.class, fonctionId);
			user.setFonction(fct);
			
			//	Insertion dans la base de données
			System.out.println("Information utilisateur prête");
			try{	//	Insertion résussie	
				transaction.begin();
				em.persist(user);
				transaction.commit();
				System.out.println("Nouvel utilisateur inséré");
				return true;
			}catch(Exception ex){	//	Erreur d'insertion
				System.err.println(ex.getMessage());
				return false;
			}
		}
		else{
			System.out.println("Mot de passe non conforme");
			return false;
		}
	}

	///	Méthode pour avoir les accès d'un utilisateur quelconque
	@Override
	public List<String> getAcces(int userName) {
		List<String> st = new ArrayList<String>();
		Utilisateur ut = em.find(Utilisateur.class, userName);
		for(Acces a: ut.getFonction().getAcces()){
			st.add(a.getTitreAcces());
		}
		return st;
	}

	@Override
	public boolean ajoutCptCaisse(Caisse	 cptCaisse, int numCptCompta) {
		Account cptGL = em.find(Account.class, numCptCompta);
		cptCaisse.setAccount(cptGL);
		System.out.println("Nouveau compte caisse");
		try {
			transaction.begin();
			em.persist(cptCaisse);
			transaction.commit();
			System.out.println("Nouveau compte caisse inséré");
			return true;
		} catch (Exception e) {
			System.err.println("Erreur d'insertion de compte caisse");
			return false;
		}
	}

	@Override
	public boolean addPersonnel(Personnel pers, String agence, int idFonction) {
		try {
			Agence ag = em.find(Agence.class, agence.substring(0,2));
			Fonction f = em.find(Fonction.class, idFonction);
			
			pers.setCodePersonnel(CodeIncrement.getCodePers(em, agence));
			pers.setAgence(ag);
			pers.setFonction(f);
			
			transaction.begin();
			em.persist(pers);
			transaction.commit();
			em.refresh(pers);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<Personnel> getPersonnels() {
		List<Personnel> result = new ArrayList<Personnel>();
		
		TypedQuery<Personnel> query = em.createQuery("select p from Personnel p",Personnel.class);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		return null;
	}
		

	@Override
	public List<Agence> getAgence() {
		List<Agence> result = new ArrayList<Agence>();		
		TypedQuery<Agence> query = em.createQuery("select a from Agence a",Agence.class);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		return null;
	}

	@Override
	public List<Fonction> getFonction() {
		List<Fonction> result = new ArrayList<Fonction>();
		
		TypedQuery<Fonction> query = em.createQuery("select f from Fonction f",Fonction.class);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		return null;
	}

	@Override
	public List<Utilisateur> getAllUser() {		
		String sql = "select u from Utilisateur u";		
		TypedQuery<Utilisateur> query = em.createQuery(sql,Utilisateur.class);			
		return query.getResultList();
	}

	@Override
	public Utilisateur findUser(int id) {
		Utilisateur ut = em.find(Utilisateur.class, id);
		return ut;
	}

	@Override
	public Utilisateur updateUser(Utilisateur ut) {
		Utilisateur user = findUser(ut.getIdUtilisateur());
		user = ut;
		transaction.begin();
		em.flush();
		transaction.commit();
		em.refresh(user);
		return user;
	}

	@Override
	public boolean deleteUser(int id) {
		try {
			Utilisateur ut = em.find(Utilisateur.class, id);
			transaction.begin();
			em.remove(ut);
			transaction.commit();
			return true;
			//em.refresh(ut); 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Liste utilisateurs par fonction en paramètre
	@Override
	public List<Utilisateur> getUser(String nomFonction) {
		String sql = "select u from Utilisateur u join u.fonction f where f.libelleFonction='"+nomFonction+"'";
		TypedQuery<Utilisateur> query = em.createQuery(sql,Utilisateur.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}


}
