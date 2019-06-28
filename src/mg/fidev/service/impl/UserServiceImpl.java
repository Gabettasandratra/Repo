package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import mg.fidev.model.Acces;
import mg.fidev.model.CompteCaisse;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.Fonction;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.UserService;

@WebService(name="UserService", targetNamespace="http://user.fidev.com/", serviceName="userService", portName="userServicePort", endpointInterface="mg.fidev.service.UserService")
public class UserServiceImpl implements UserService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	///	Méthode pour l'authentification d'un utilisateur
	@Override
	public List<Acces> authentifie(String loginUser, String mdpUser) {
		List<Acces> a = new ArrayList<Acces>();	//	Instance une liste d'accès pour stocker
												//	la liste d'accès d'un utilisateur quelconque
		
		//	Requête pour vérifier si le login et le mote de passe sont
		//	valides et correspondent bien
		TypedQuery<Utilisateur> q = em.createQuery("select u from Utilisateur u where u.loginUtilisateur= :param1 and u.mdpUtilisateur= :param2", Utilisateur.class);
		q.setParameter("param1", loginUser);
		q.setParameter("param2", mdpUser);
		
		Utilisateur ut = new Utilisateur();
		
		//	Traitement à faire en fonction du résultat de la requête
		//	Login et mot de passe valides ou non
		System.out.println("Utilisateur ready !!");
		if((q.getSingleResult()) != null){
			//	Instance utilisateur correspondant à la réponse de la requête précédente
			ut = q.getSingleResult();
			for(Acces ac : ut.getFonction().getAcces()){
				System.out.println(ac.getTitreAcces());
				a.add(ac);
			}
			System.out.println("Connexion réussie");
			return a;
		}
		System.out.println("Utilisateur null !!");
		return null;
	}

	///	Méthode pour l'enregistrement d'un nouvel utilisateur
	@Override
	public boolean insertUser(String nomUser, String loginUser, String mdpUser, String mdpConf,
			String genreUser, String telUser, List<String> listCptCaisse, int fonctionId) {
		//	Instance nouvel utilisateur à insérer
		Utilisateur user = new Utilisateur();
		List<CompteCaisse> cptCaisse = new ArrayList<CompteCaisse>();
		
		//	Affectation informations utilisateur
		if(mdpUser.equals(mdpConf)){
			user.setNomUtilisateur(nomUser);
			user.setLoginUtilisateur(loginUser);
			user.setMdpUtilisateur(mdpUser);
			user.setGenreUser(genreUser);
			user.setTelUser(telUser);
			for(String a : listCptCaisse){
				cptCaisse.add(em.find(CompteCaisse.class, a));
			}
			user.setCompteCaisses(cptCaisse);
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

	@Override
	public List<String> getAcces(String userName) {
		return null;
	}

	///	Méthode pour ouvrir un compte client
	@Override
	public boolean creerCompteClient(String dateOuverture,
			Date dateEcheance, String idProduitEp,
			String individuelId, String groupeId, int userId) {
		//	Instance nouveau compte_epargne
		CompteEpargne cpt_ep = new CompteEpargne();
		
		//	Insertion des informations sur le compte
		cpt_ep.setDateOuverture(dateOuverture);
		cpt_ep.setDateEcheance(dateEcheance);
		ProduitEpargne pdt_ep = em.find(ProduitEpargne.class, idProduitEp);
		Individuel ind = em.find(Individuel.class, individuelId);
		Groupe grp = em.find(Groupe.class, groupeId);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		cpt_ep.setUtilisateur(ut);
		cpt_ep.setProduitEpargne(pdt_ep);
		cpt_ep.setIsActif(true);
		if(ind != null){
			System.out.println("Compte individuel");
			cpt_ep.setIndividuel(ind);
		}
		else if(grp != null){
			System.out.println("Compte groupe");
			cpt_ep.setGroupe(grp);
		}
		
		//	Enregistrement du compte lorsque toutes les informations nécessaires
		//	sont valides et complètes
		if(cpt_ep.getIndividuel() != null || cpt_ep.getGroupe() != null){
			System.out.println("Information compte épargne prête");
			try{
				transaction.begin();
				em.persist(cpt_ep);
				transaction.commit();
				System.out.println("Nouveau compte épargne ouvert");
				return true;
			}catch(Exception ex){
				System.err.println(ex.getMessage());
				return false;
			}
		}
		else
			return false;
	}

	///	Méthode pour faire une transaction sur un compte épargne
	@Override
	public boolean transactionCompte(String typeTransac, String dateTransac,
			double montant, String description, String pieceCompta,
			String nomCptCaisse, String numCptEp) {
		//	Instance de nouvelle transaction
		TransactionEpargne trans = new TransactionEpargne();
		
		//	Initialisation des informations sur la transaction
		trans.setDateTransaction(dateTransac);
		trans.setTypeTransEp(typeTransac);
		trans.setMontant(montant);
		trans.setDescription(description);
		trans.setPièceCompta(pieceCompta);
		CompteCaisse cptCaisse = em.find(CompteCaisse.class, nomCptCaisse);
		CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
		trans.setCompteCaisse(cptCaisse);
		trans.setCompteEpargne(cptEp);
		
		//	Enregistrement de la transaction
		if(trans.getCompteEpargne() != null){
			System.out.println("Informations sur la transaction prêtes");
			try {
				transaction.begin();
				em.flush();
				em.persist(trans);
				transaction.commit();
				System.out.println("Transaction réussie");
				return true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}
		}
		else{
			System.out.println("Erreur de transaction, compte épargne null");
			return false;
		}
	}

}
