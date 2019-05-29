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
	
	@Override
	public List<Acces> authentifie(String loginUser, String mdpUser) {
		// select * from utilisateur where loginUtilisateur=? and mdpUtilisateur=?
		List<Acces> a = new ArrayList<Acces>();	//	Instance une liste d'acc�s pour stocker
												//	la liste d'acc�s d'un utilisateur quelconque
		
		//	Requ�te pour v�rifier si le login et le mote de passe sont
		//	valides et correspondent bien
		TypedQuery<Utilisateur> q = em.createQuery("select u from Utilisateur u where u.loginUtilisateur= :param1 and u.mdpUtilisateur= :param2", Utilisateur.class);
		q.setParameter("param1", loginUser);
		q.setParameter("param2", mdpUser);
		
		//	Instance utilisateur correspondant � la r�ponse de la requ�te pr�c�dente
		Utilisateur ut = q.getSingleResult();
		
		//	Traitement � faire en fonction du r�sultat de la requ�te
		//	Login et mot de passe valides ou non
		System.out.println("Utilisateur ready !!");
		if(!ut.equals(null)){
			for(Acces ac : ut.getFonction().getAcces()){
				System.out.println(ac.getTitreAcces());
				a.add(ac);
			}
			System.out.println("Connexion r�ussie");
			return a;
		}
		System.out.println("Utilisateur null !!");
		return null;
	}

	@Override
	public boolean insertUser(String nomUser, String loginUser, String mdpUser,
			String genreUser, String telUser, int fonctionId) {
		//	Instance nouvel utilisateur � ins�rer
		Utilisateur user = new Utilisateur();
		
		//	Affectation informations utilisateur
		user.setNomUtilisateur(nomUser);
		user.setLoginUtilisateur(loginUser);
		user.setMdpUtilisateur(mdpUser);
		user.setGenreUser(genreUser);
		user.setTelUser(telUser);
		Fonction fct = em.find(Fonction.class, fonctionId);
		user.setFonction(fct);
		
		//	Insertion dans la base de donn�es
		System.out.println("Information utilisateur pr�te");
		try{
			transaction.begin();
			em.persist(user);
			transaction.commit();
			System.out.println("Nouvel utilisateur ins�r�");
			return true;
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			return false;
		}
	}

	@Override
	public List<String> getAcces(String userName) {
		return null;
	}

	@Override
	public boolean creerCompteClient(String numCompte, Date dateOuverture,
			Date dateEcheance, double solde, String idProduitEp,
			int individuelId, int groupeId, int userId) {
		//	Instance nouveau compte_epargne
		CompteEpargne cpt_ep = new CompteEpargne();
		
		//	Insertion des informations sur le compte
		cpt_ep.setNumCompteEp(numCompte);
		cpt_ep.setDateOuverture(dateOuverture);
		cpt_ep.setDateEcheance(dateEcheance);
		cpt_ep.setSolde(solde);
		ProduitEpargne pdt_ep = em.find(ProduitEpargne.class, idProduitEp);
		Individuel ind = em.find(Individuel.class, individuelId);
		Groupe grp = em.find(Groupe.class, groupeId);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		cpt_ep.setProduitEpargne(pdt_ep);
		cpt_ep.setIndividuel(ind);
		cpt_ep.setGroupe(grp);
		cpt_ep.setUtilisateur(ut);
		
		//	Enregistrement du compte lorsque toutes les informations n�cessaires
		//	sont valides et compl�tes
		System.out.println("Information compte �pargne pr�te");
		
		try{
			transaction.begin();
			em.persist(cpt_ep);
			transaction.commit();
			System.out.println("Nouveau compte �pargne ouvert");
			return true;
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean transactionCompte(String typeTransac, Date dateTransac,
			double montant, String description, String pieceCompta,
			String nomCptCaisse, String numCptEp) {
		//	Instance de nouvelle transaction
		TransactionEpargne trans = new TransactionEpargne();
		
		//	Initialisation des informations sur la transaction
		trans.setTypeTransEp(typeTransac);
		trans.setDateTransaction(dateTransac);
		trans.setMontant(montant);
		trans.setDescription(description);
		trans.setPi�ceCompta(pieceCompta);
		CompteCaisse cptCaisse = em.find(CompteCaisse.class, nomCptCaisse);
		CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
		trans.setCompteCaisse(cptCaisse);
		trans.setCompteEpargne(cptEp);
		
		System.out.println("Informations sur la transaction pr�tes");
		
		//	Enregistrement de la transaction
		try {
			transaction.begin();
			if(trans.getTypeTransEp().equals("DE")){
				trans.getCompteEpargne().setSolde(trans.getCompteEpargne().getSolde() + trans.getMontant());
				System.out.println("D�pot");
			}
			else if(trans.getTypeTransEp().equals("RE")){
				trans.getCompteEpargne().setSolde(trans.getCompteEpargne().getSolde() - trans.getMontant());
				System.out.println("Retrait");
			}
			em.flush();
			em.persist(trans);
			transaction.commit();
			System.out.println("Transaction r�ussie");
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

}
