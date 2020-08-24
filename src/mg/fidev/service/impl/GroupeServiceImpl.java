package mg.fidev.service.impl;

import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Adresse;
import mg.fidev.model.FonctionMembreGroupe;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.MembreGroupe;
import mg.fidev.model.MembreView;
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
		Individuel individuel = em.find(Individuel.class, codeInd);
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

		String result = "";
		groupe.setCodeGrp(CodeIncrement.getCodeGrp(em, codeAgence));
		groupe.setAdresse(adresse);
		try {
			transaction.begin();
			em.persist(adresse);	
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
		/*if(groupe.getIndividuels() != null){
			ind.setGroupe(groupe);
		}
		List<MembreGroupe> membres = new ArrayList<MembreGroupe>();
		List<MembreView> mview = getallMembreView();
		for (MembreView membreView : mview) {
			MembreGroupe m = new MembreGroupe();  
			Individuel ind = em.find(Individuel.class, membreView.getCodeInd());
			String sql = "select f from FonctionMembreGroupe f where f.nomFonction='"+membreView.getFonction()+"'";
			FonctionMembreGroupe f = (FonctionMembreGroupe) em.createQuery(sql).getSingleResult();
			m.setFonctionMembre(f);
			m.setIndividuel(ind); 
			m.setGroupe(groupe); 
			membres.add(m);
			ind.setGroupe(groupe);
			try {
				transaction.begin();
				em.merge(ind);
				transaction.commit();
				em.refresh(ind); 
				System.out.println("table individuel à jour");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(mview == null){
			result = "erreur";
		}else{
			groupe.setMembres(membres); 
			
		}*/
		
	}

	@Override
	public List<Individuel> getListeMembreGroupe(String nomGroupe) {
		TypedQuery<Groupe> q1 = em.createQuery(
				"select g from Groupe g where g.codeGrp = :groupe",
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
						
							if(groupe.getPresident() == individuel){
								groupe.setPresident(null);
								transaction.begin();
								em.flush();							
								transaction.commit();
								em.refresh(groupe);
							}
							
							if(groupe.getSecretaire() == individuel){
								groupe.setSecretaire(null);
								transaction.begin();
								em.flush();							
								transaction.commit();
								em.refresh(groupe);
							}
							
							if(groupe.getTresorier() == individuel){
								groupe.setTresorier(null);
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
	
	//Enregistrement membre groupe
	
	//Généré code membre groupe
	public static int getLastIndexMembre(String codeGrp) {
		
		String sql = "select count(m) from MembreGroupe m join m.groupe g "
				+ " where g.codeGrp = '"+ codeGrp +"'";
		Query q = em.createQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	public static String genCodeMembre(String codeGrp){
		
		int lastIndex = getLastIndexMembre(codeGrp);
		if(lastIndex == 0){
			return codeGrp +"/01";
		}else{
			String index = String.format("%02d", ++lastIndex);		
			return codeGrp +"/"+ index;
		}
	}
	
	@Override
	public String saveMembre(String codeInd, String codeGroupe, int fonction,
			String dateAdd) {
		//Individuel
		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGroupe);
		FonctionMembreGroupe f = em.find(FonctionMembreGroupe.class, fonction);
		
		String result = "";
		
		if(ind.getGroupe() != null){
			result = "Le client "+ind.getCodeInd()+" est déjà membre du groupe "+ind.getGroupe().getNomGroupe();
		}else{
						
			if(f.getNomFonction().equalsIgnoreCase("Président")){
				if(grp.getPresident() != null){
					result = "Le président du groupe doit être unique";
				}else{
					grp.setPresident(ind); 
				}
			}else if(f.getNomFonction().equalsIgnoreCase("Trésorier")){
				if(grp.getTresorier() != null){
					result = "Le tresorier du groupe doit être unique";
				}else{
					grp.setTresorier(ind); 
				}
			}else if(f.getNomFonction().equalsIgnoreCase("Secrétaire")){
				if(grp.getSecretaire() != null){
					result = "Le secretaire du groupe doit être unique";
				}else{
					grp.setSecretaire(ind); 
				}
			}
			
			ind.setGroupe(grp);
			ind.setEstMembreGroupe(true); 
			
			MembreGroupe m = new MembreGroupe();
			String code = genCodeMembre(grp.getCodeGrp());
			
			m.setIdMembre(code);
			m.setDateAjout(dateAdd);
			m.setIndividuel(ind);
			m.setGroupe(grp);
			m.setFonctionMembre(f);
			
			try {
				transaction.begin();
				em.flush();
				em.flush();
				em.persist(m);
				transaction.commit();
				em.refresh(m); 
				result = "Membre ajouté";
			} catch (Exception e) {
				e.printStackTrace();
				result = "Erreur enregistrement";
			}
			
		}
		
		System.out.println(result);
		return result;
	}
	
	
	/***
	 * CHERCHER GROUPE PAR SON CODE
	 * ***/
	@Override
	public List<Groupe> findByCode(String code) {
		TypedQuery<Groupe> query = em.createQuery("SELECT g FROM Groupe g WHERE g.codeGrp LIKE :code OR "
				+ " g.nomGroupe LIKE :code",Groupe.class);
		query.setParameter("code", code+"%");
		
		List<Groupe> result = query.getResultList();
		
		if(!result.isEmpty())return result;
		else System.out.println("Auccun client trouvé!!!");
		
		return null;
	}
	
	
	//Ajouter au membre view
	@Override
	public String addMembre(MembreView membre) {
		String result = "";
		boolean ok = false;
		if(membre.getFonction().equalsIgnoreCase("Président")){
			String sql = "select m from MembreView m where m.fonction = 'Président'";
			Query q = em.createQuery(sql);
			if(q.getResultList().isEmpty()){
				ok = true;
			}
			else{
				ok = false;
				result = "La fonction Président doit ajouter une seule fois!!!";
			}
		}else if
		(membre.getFonction().equalsIgnoreCase("Secrétaire")){
			String sql = "select m from MembreView m where m.fonction = 'Secrétaire'";
			Query q = em.createQuery(sql);
			if(q.getResultList().isEmpty()){
				ok = true;
			}
			else{
				ok = false;
				result = "La fonction Secrétaire doit ajouter une seule fois!!!";
			}
		}
		else if
		(membre.getFonction().equalsIgnoreCase("Trésorier")){
			String sql = "select m from MembreView m where m.fonction = 'Trésorier'";
			Query q = em.createQuery(sql);
			if(q.getResultList().isEmpty()){
				ok = true;
			}
			else{
				ok = false;
				result = "La fonction Trésorier doit ajouter une seule fois!!!";
			}
		}
		else{
			ok = true;
		}
		
		if(ok == true){
			try {
				transaction.begin();
				em.persist(membre);
				transaction.commit();
				em.refresh(membre);
				System.out.println("Membre ajouté");
				result =  "Membre ajouté!!!";
			} catch (Exception e) {
				e.printStackTrace();
				result ="Erreur enregistrement";
			}						
		}
		return result;
	}
	
	
	//liste membre view
	@Override
	public List<MembreView> getallMembreView() {
		String sql = "select m from MembreView m";
		return em.createQuery(sql,MembreView.class).getResultList();
	}
	
	
	//Vider membre view
	@Override
	public boolean deleteMembreView() {
		Query query = em.createNativeQuery("TRUNCATE TABLE membre_view");
		try {
			
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table membre_view vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public List<FonctionMembreGroupe> getFonctionMembre() {
		String sql = "select f from FonctionMembreGroupe f";
		return em.createQuery(sql,FonctionMembreGroupe.class).getResultList();
	}
	
	
	@Override
	public List<MembreGroupe> getMembreGroupe(String code) {
		String sql = "select m from MembreGroupe m join m.groupe g where g.codeGrp ='"+code+"'";
		TypedQuery<MembreGroupe> query = em.createQuery(sql,MembreGroupe.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	
	@Override
	public boolean tansfertMembreGroupe(int id, String codeGrp, String codeInd,
			int fonction) {
		MembreGroupe m = em.find(MembreGroupe.class, id);
		Groupe g = em.find(Groupe.class, codeGrp);
		FonctionMembreGroupe f = em.find(FonctionMembreGroupe.class, fonction);
		m.setGroupe(g);
		m.setFonctionMembre(f);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(m); 
			System.out.println("Membre transférer");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
}
