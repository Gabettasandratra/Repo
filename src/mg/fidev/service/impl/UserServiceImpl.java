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

import mg.fidev.model.Acces;
import mg.fidev.model.Account;
import mg.fidev.model.Agence;
import mg.fidev.model.Caisse;
import mg.fidev.model.FichierEtatCivil;
import mg.fidev.model.FichierLangue;
import mg.fidev.model.FichierNiveauetude;
import mg.fidev.model.FichierRegiongeo;
import mg.fidev.model.FichierSecteurActiviter;
import mg.fidev.model.FichierTitre;
import mg.fidev.model.Fonction;
import mg.fidev.model.Institution;
import mg.fidev.model.JourFerier;
import mg.fidev.model.Personnel;
import mg.fidev.model.Utilisateur;
import mg.fidev.model.UtilisateurSupprimer;
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
			String genreUser, String telUser,String photo,  int fonctionId, List<String> agence) {//List<String> listCptCaisse,
		//	Instance nouvel utilisateur à insérer
		Utilisateur user = new Utilisateur();
		/*List<CompteCaisse> cptCaisse = new ArrayList<CompteCaisse>();
		for(String a : listCptCaisse){
		cptCaisse.add(em.find(CompteCaisse.class, a));
     	}
	  user.setCompteCaisses(cptCaisse);*/
		
		List<Agence> agences = new ArrayList<Agence>();
		if(agence != null){			
			for (String code : agence) {				
				Agence a = em.find(Agence.class, code);
				agences.add(a);
			}
		}
		
		String passHash = BCrypt.hashpw(mdpUser, BCrypt.gensalt());
		
		//	Affectation informations utilisateur
		if(mdpUser.equals(mdpConf)){
			user.setNomUtilisateur(nomUser);
			user.setLoginUtilisateur(loginUser);
			user.setMdpUtilisateur(passHash);
			user.setGenreUser(genreUser);
			user.setTelUser(telUser);
			user.setPhoto(photo); 
			user.setAgences(agences); 

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
	
	//Chercher tous les utilisateurs
	@Override
	public List<Utilisateur> getAllUser() {		
		String sql = "select u from Utilisateur u";		
		TypedQuery<Utilisateur> query = em.createQuery(sql,Utilisateur.class);			
		return query.getResultList();
	}

	//Chercher un utilisateur par identifiant
	@Override
	public Utilisateur findUser(int id) {
		Utilisateur ut = em.find(Utilisateur.class, id);
		return ut;
	}

	//Modifier utilisateur
	
	@Override
	public Utilisateur updateProfilUser(int idUser, String nomUser, String loginUser, String genreUser,
			String telUser, String photo, int fonctionId, List<String> agence) {
		
		//Instance classe fonction
		Fonction f = em.find(Fonction.class, fonctionId);
		//Chercher l'utilisateur 
		Utilisateur user = findUser(idUser);
		
		//Agences
		List<Agence> agences = new ArrayList<Agence>();
		if(agence != null){			
			for (String code : agence) {				
				Agence a = em.find(Agence.class, code);
				agences.add(a);
			}
		}
		
		user.setNomUtilisateur(nomUser);
		user.setLoginUtilisateur(loginUser);
		user.setTelUser(telUser);
		user.setGenreUser(genreUser);
		user.setAgences(agences); 
		if(!photo.equalsIgnoreCase(""))
			user.setPhoto(photo);
		user.setFonction(f);
		try {
			transaction.begin();
			em.merge(user);
			transaction.commit();
			em.refresh(user);
			System.out.println("Modification profil utilisateur réussit");
			return user;			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur modification");
			return null;
		}
	}

	//Supprimer utilisateur
	@Override
	public boolean deleteUser(int id) {
		try {
			Utilisateur ut = em.find(Utilisateur.class, id);
			UtilisateurSupprimer us = new
					UtilisateurSupprimer(ut.getGenreUser(), ut.getLoginUtilisateur(),
							ut.getMdpUtilisateur(), ut.getNomUtilisateur(), ut.getTelUser(), ut.getPhoto(),
							"", ut.getFonction().getLibelleFonction());
			String ag = "";
			for (int i = 0; i < ut.getAgences().size(); i++) {
				ag += ""+ut.getAgences().get(i).getNomAgence();
				if(i < ut.getAgences().size() - 1)
					ag +=", ";
			}
			
			us.setAgence(ag); 
			
			transaction.begin();
			em.persist(us); 
			em.remove(ut);
			transaction.commit();
			em.refresh(us); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Liste utilisateurs supprimés
	@Override
	public List<UtilisateurSupprimer> getUserSupprimer() {
		String sql = "select u from UtilisateurSupprimer u";
		TypedQuery<UtilisateurSupprimer> q = em.createQuery(sql, UtilisateurSupprimer.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
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
	//--------------------------------------------------------------------------------------------
	/****************************** Gestion agences ********************************************/

	static Institution saveInstitution(String nomInstitution){
		
		String sql = "select i from Institution i where i.nomInstitution = '"+ nomInstitution +"'";
		
		Query q =  em.createQuery(sql);
			
		if(!q.getResultList().isEmpty()){
			Institution inst = (Institution) q.getSingleResult();
			return inst;
		}else{
			try {
				
				Institution i = new Institution();
				i.setNomInstitution(nomInstitution); 
				transaction.begin();
				em.persist(i);
				transaction.commit();
				em.refresh(i); 
				System.out.println("Institution enregistré");
				return i;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Institution non enregistré");
			}
		}
		
		return null;
	}
	
	//Ajout agence
	@Override
	public boolean addAgence(Agence agence, String nomInstitution) {
		Institution i = saveInstitution(nomInstitution);
		agence.setInstitution(i); 
		LocalDate dt = LocalDate.parse(agence.getDateInscrit());
		System.out.println("Date inscrit: "+agence.getDateInscrit());
		dt = dt.plusMonths(agence.getLicence());
		System.out.println("Licence: "+agence.getLicence());
		agence.setDateExpire(dt.toString());
		System.out.println("Date expire: "+agence.getDateExpire());
		agence.setEtatLicence(true); 
		
		try {
			transaction.begin();
			em.persist(agence);
			transaction.commit();
			em.refresh(agence);			
			System.out.println("Agence enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Agence non enregistré");
			return false;
		}
	}
	
	//Activer licence
	@Override
	public boolean activerLicence(String codeAgence, String date, int licence) {
		Agence a = em.find(Agence.class, codeAgence);
		
		LocalDate dt = LocalDate.parse(date);
		dt.plusMonths(licence);
		a.setDateExpire(dt.toString());
		a.setEtatLicence(true); 
		a.setDateInscrit(date);
		a.setLicence(licence); 
		try {
			transaction.begin();
			em.flush();
			transaction.commit();			
			System.out.println("Activation agence enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur activation agence");
			return false;
		}
		
	}

	//Desactiver licence
	@Override
	public List<Agence> desactiverLicence(String date) {
		List<Agence> result = new ArrayList<Agence>();
		String sql = "select a from Agence a where a.dateExpire = '"+ date +"' and a.etatLicence='"+true+"'";
		TypedQuery<Agence> q = em.createQuery(sql, Agence.class);
		if(!q.getResultList().isEmpty()){
			for (Agence agence : q.getResultList()) {
				agence.setEtatLicence(false); 
				try {
					transaction.begin();
					em.flush();
					transaction.commit();
					result.add(agence);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur desactivation agence");
				}
			}
			
		}
		return result;
	}
	
	//Liste agences
	@Override
	public List<Agence> getAgence() {
		String sql = "select a from Agence a where a.etatLicence = '"+ true +"'";
		TypedQuery<Agence> query = em.createQuery(sql, Agence.class); 
		if(!query.getResultList().isEmpty()){
			return query.getResultList();			
		}
		return null;
	}
	
	//Chercher agence par Id
	@Override
	public Agence getAgenceById(String id) {
		return em.find(Agence.class, id);
	}
	
	//Liste agence par utilisateur
	@Override
	public List<Agence> getAgenceByUser(int idUser) {
		Utilisateur ut = em.find(Utilisateur.class, idUser);
		if(ut == null){
			return getAgence();
		}else{ 
			return ut.getAgences();			
		}
	}
	
	//Liste utilisateurs par agence
	@Override
	public List<Utilisateur> getUserByAgence(String code) {
		Agence a = em.find(Agence.class, code);
		return a.getUtilisateurs();
	}

	//--------------------------------------------------------------------------------------------
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

	

	//Ajout jour férié
	@Override
	public boolean addJourFerie(JourFerier jourFerier) {
		try {
			transaction.begin();
			em.persist(jourFerier);
			transaction.commit();
			em.refresh(jourFerier); 
			System.out.println("Jour Férié ajouté");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//List jours fériés (fonction local)
	public static List<JourFerier> listJoursFerier(){
		String sql = "select j from JourFerier j order by j.date asc";
		TypedQuery<JourFerier> query = em.createQuery(sql,JourFerier.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	//List jours fériés
	@Override
	public List<JourFerier> getJoursFerier() {
		return listJoursFerier();
	}

	//Modifier jour férié
	@Override
	public boolean editJourFerie(JourFerier jourFerier) {
		try {
			JourFerier j = em.find(JourFerier.class, jourFerier.getId());
			j.setDescription(jourFerier.getDescription());
			j.setDate(jourFerier.getDate());
			transaction.begin();
			em.merge(j);
			transaction.commit();
			System.out.println("Jour Férié modifié");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Supprimer jour férié
	@Override
	public boolean deleteJourFerier(int id) {
		try {
			JourFerier j = em.find(JourFerier.class, id);
			transaction.begin();
			em.remove(j);
			transaction.commit();
			System.out.println("Jour Férié supprimé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/********************************** Région géographique ******************************/
	//Fichier des supports
	//Titre
	@Override
	public boolean addFichierTitre(FichierTitre titre) {
		try {
			transaction.begin();
			em.persist(titre); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierTitre> getTitre() {
		String sql = "select f from FichierTitre f";
		return em.createQuery(sql, FichierTitre.class).getResultList();
	}

	@Override
	public boolean deleteTitre(int id) {
		FichierTitre t = em.find(FichierTitre.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Etat civil
	@Override
	public boolean addFichierEtatCivil(FichierEtatCivil etat) {
		try {
			transaction.begin();
			em.persist(etat); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierEtatCivil> getEtatCivil() {
		String sql = "select f from FichierEtatCivil f";
		return em.createQuery(sql, FichierEtatCivil.class).getResultList();
	}

	@Override
	public boolean deleteEtatCivil(int id) {
		FichierEtatCivil t = em.find(FichierEtatCivil.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	//Langue
	@Override
	public boolean addFichierLangue(FichierLangue langue) {
		try {
			transaction.begin();
			em.persist(langue); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierLangue> getLangue() {
		String sql = "select f from FichierLangue f";
		return em.createQuery(sql, FichierLangue.class).getResultList();
	}

	@Override
	public boolean deleteLangue(int id) {
		FichierLangue t = em.find(FichierLangue.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Niveau d'étude
	@Override
	public boolean addFichierNiveau(FichierNiveauetude niveau) {
		try {
			transaction.begin();
			em.persist(niveau); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierNiveauetude> getNiveau() {
		String sql = "select f from FichierNiveauetude f";
		return em.createQuery(sql, FichierNiveauetude.class).getResultList();
	}

	@Override
	public boolean deleteNiveau(int id) {
		FichierNiveauetude t = em.find(FichierNiveauetude.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Secteur d'activité
	@Override
	public boolean addFichierSecteur(FichierSecteurActiviter secteur) {
		try {
			transaction.begin();
			em.persist(secteur); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierSecteurActiviter> getSecteur() {
		String sql = "select f from FichierSecteurActiviter f";
		return em.createQuery(sql, FichierSecteurActiviter.class).getResultList();
	}

	@Override
	public boolean deleteSecteur(int id) {
		FichierSecteurActiviter t = em.find(FichierSecteurActiviter.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Fichier région géographique
	@Override
	public boolean addFichierRegion(FichierRegiongeo region) {
		try {
			transaction.begin();
			em.persist(region); 
			transaction.commit(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	@Override
	public List<FichierRegiongeo> getRegion() {
		String sql = "select f from FichierRegiongeo f";
		return em.createQuery(sql, FichierRegiongeo.class).getResultList();
	}

	@Override
	public boolean deleteRegion(String id) {
		FichierRegiongeo t = em.find(FichierRegiongeo.class, id);
		try {
			transaction.begin();
			em.remove(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<FichierRegiongeo> findCommune(String code) {
		String sql = "select f from FichierRegiongeo f where f.codeCommune like = '"+ code +"%'";
		return em.createQuery(sql, FichierRegiongeo.class).getResultList();
	}

	//-------------------------------------------------------------------------------------------
	//Institution

	//Enregistrement institution
	@Override
	public boolean addInstitution(Institution data) {
		try {
			transaction.begin();
			em.persist(data);
			transaction.commit();
			em.refresh(data); 
			System.out.println("Enregistrement institution reussi");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur enregistrement institution");
			return false;
		}
	}

	//Modification Institution
	@Override
	public boolean updateInstitution(int id, Institution data) {
		Institution i = findUniqueInstitution(id);
		i = data;
		try {
			transaction.begin();
			em.merge(i);
			transaction.commit();
			em.refresh(i); 
			System.out.println("Modification institution reussie");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur modification institution");
			return false;
		}
	}

	//Liste institutions
	@Override
	public List<Institution> getAllInstitution() {
		String sql = "select i from Institution i";
		TypedQuery<Institution> q = em.createQuery(sql, Institution.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Suppression institution
	@Override
	public boolean deleteInstitution(int id) {
		Institution i = findUniqueInstitution(id);
		try {
			transaction.begin();
			em.remove(i); 
			transaction.commit();
			System.out.println("Suppression institution reussie");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur suppression institution");
			return false;
		}
	}

	//Chercher Institution par identifiant
	@Override
	public Institution findUniqueInstitution(int id) {
		return em.find(Institution.class, id);
	}

}
