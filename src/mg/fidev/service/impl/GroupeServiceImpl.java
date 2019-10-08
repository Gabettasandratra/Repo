package mg.fidev.service.impl;

import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import mg.fidev.model.Adresse;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.service.GroupeService;
import mg.fidev.utils.CodeIncrement;

@WebService(name = "groupeService", targetNamespace = "http://individuel.fidev.com/", serviceName = "groupeService", portName = "groupeServicePort", endpointInterface = "mg.fidev.service.GroupeService")
public class GroupeServiceImpl implements GroupeService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();

	/***
	 * AJOUT MEMBRE GROUPE
	 * ***/
	@Override
	public boolean addMembreGroupe(String nomGroupe, String codeInd) {
		// Recupere le groupe
		TypedQuery<Groupe> q1 = em.createQuery(
				"select g from Groupe g where g.nomGroupe = :nomGroupe",
				Groupe.class);
		q1.setParameter("nomGroupe", nomGroupe);
		Groupe groupe = q1.getSingleResult();

		// Recupere l'individuel
		TypedQuery<Individuel> q2 = em
				.createQuery(
						"select i from Individuel i where i.codeInd = :codeInd",
						Individuel.class);
		q2.setParameter("codeInd", codeInd);
		Individuel individuel = q2.getSingleResult();
		try {
			// on ajoute dans le groupe
			transaction.begin();
			groupe.addIndividuel(individuel);
			individuel.setEstMembreGroupe(true);
			em.flush();
			em.flush();
			transaction.commit();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	/***
	 * AFFICHER TOUT LES GROUPES
	 * ***/
	@Override
	public List<Groupe> getAllGroupe() {
		TypedQuery<Groupe> q1 = em.createQuery("select g from Groupe g order by g.nomGroupe asc",
				Groupe.class);
		List<Groupe> results = q1.getResultList();	
		return results;
	}
	
	/***
	 * CHERCHER GROUPE PAR SON CODE
	 * ***/
	@Override
	public Groupe findOneGroupe(String codeCode) {
		Groupe groupe = em.find(Groupe.class, codeCode);
		if(groupe!=null){
			return groupe;
		}else{
			return null;			
		}
	}
	
	
	/***
	 * ENREGISTREMENT DE NOUVEAU GROUPE
	 * ***/

	@Override
	public String saveGroupe(Groupe groupe,Adresse adresse, String codeAgence) {

		Individuel ind = new Individuel();
		String result = "";
		groupe.setCodeGrp(CodeIncrement.getCodeGrp(em, codeAgence));
		groupe.setAdresse(adresse);
		if(groupe.getIndividuels() != null){
			ind.setGroupe(groupe);
		}

		try {
			transaction.begin();
			em.persist(adresse);			
			transaction.commit();
			transaction.begin();
			em.persist(groupe);
			//em.merge(ind);
			transaction.commit();
			result = "success";
			//em.refresh(groupe);
		} catch (Exception e) {
			e.printStackTrace();
			result = "erreur";
		}
		return result;
	}

	@Override
	public List<Individuel> getListeMembreGroupe(String nomGroupe) {
		TypedQuery<Groupe> q1 = em.createQuery(
				"select g from Groupe g where g.nomGroupe = :groupe",
				Groupe.class);
		q1.setParameter("groupe", nomGroupe);
		Groupe groupe = q1.getSingleResult();
		List<Individuel> membre = groupe.getIndividuels();
		return membre;
	}

	@Override
	public List<Individuel> getListeNonMembre() {
		TypedQuery<Individuel> q1 = em
				.createQuery(
						"select i from Individuel i where i.estMembreGroupe = :value",
						Individuel.class);
		q1.setParameter("value", false);
		List<Individuel> results = q1.getResultList();

		return results;
	}

	/***
	 * AJOUT CONSEIL ADMIN GROUPE
	 * ***/
	@Override
	public void addConseil(String codeGroupe, String president,
			String secretaire, String tresorier) {
		Groupe groupe = em.find(Groupe.class, codeGroupe);
		
		Individuel pres = em.find(Individuel.class, president);
		
		Individuel sec = em.find(Individuel.class, secretaire);
		
		Individuel tres = em.find(Individuel.class, tresorier);
		
		groupe.setPresident(pres.getNomClient());
		groupe.setSecretaire(sec.getNomClient());
		groupe.setTresorier(tres.getNomClient());
		
		try {		
			transaction.begin();
			em.flush();
			transaction.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/***
	 * TRANSFERER MEMBRE
	 * ***/
	@Override
	public boolean transferMembre(String codeGrp1, String codeGrp2, String codeInd) { 
		Groupe groupe = em.find(Groupe.class, codeGrp1);
		Groupe trasfers = em.find(Groupe.class, codeGrp2);
				
		TypedQuery<Individuel> query = em.createQuery("SELECT i FROM Individuel i JOIN i.groupe g WHERE g.codeGrp = :codeGroupes",Individuel.class);
		query.setParameter("codeGroupes", "" +codeGrp1+ "");
		List<Individuel> listInd = query.getResultList();
		
		for (Individuel individuel : listInd) {
			if(individuel.getCodeInd().equals(codeInd)){
				
				if(trasfers != null){
					individuel.setGroupe(trasfers);
					try {	
						
							if(groupe.getPresident().equalsIgnoreCase(individuel.getNomClient())){
								groupe.setPresident("");
								transaction.begin();
								em.flush();							
								transaction.commit();
								em.refresh(groupe);
							}
							
							if(groupe.getSecretaire().equalsIgnoreCase(individuel.getNomClient())){
								groupe.setSecretaire("");
								transaction.begin();
								em.flush();							
								transaction.commit();
								em.refresh(groupe);
							}
							
							if(groupe.getTresorier().equalsIgnoreCase(individuel.getNomClient())){
								groupe.setTresorier("");
								transaction.begin();
								em.flush();							
								transaction.commit();
								em.refresh(groupe);
					
							}
							
							transaction.begin();
							em.flush();
							transaction.commit();
							em.refresh(individuel);
							System.out.println("Transfer reussit!!!");
							return true;
						
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					
				}
			}
		}
		
		return true;
	}
	
}
