package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import mg.fidev.model.Grandlivre;
import mg.fidev.service.ComptabiliteService;

@WebService(name="comptabliteService", targetNamespace="http://fidev.mg.comptabliteService", serviceName="comptabliteService",
portName="comptabliteServicePort", endpointInterface="mg.fidev.service.ComptabiliteService")
public class ComptabliteServiceImpl implements ComptabiliteService {
	
	//Declaration de l'EntityManager
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	//private static EntityTransaction transaction = em.getTransaction();
	
	
	//AFFICHE GRAND LIVRE
	@Override
	public List<Grandlivre> afficheGrandLivre(String nomUtilisateur,String dateDeb, String dateFin) {
		List<Grandlivre> result = new ArrayList<Grandlivre>();
		try {
			if(dateFin.equals("")){
				TypedQuery<Grandlivre> query = em.createQuery("SELECT g FROM Grandlivre g WHERE "
						+ "g.userId = :nom AND g.date >= :dateDeb ",Grandlivre.class); 
				
				query.setParameter("nom", nomUtilisateur);
				query.setParameter("dateDeb", dateDeb);
				if(!(query.getResultList().isEmpty()))
					result = query.getResultList();
				else
					System.err.println("il n'y a pas de resultat");
				
			}else{
				
				TypedQuery<Grandlivre> query = em.createQuery("SELECT g FROM Grandlivre g WHERE "
						+ "g.userId = :nom AND g.date BETWEEN :dateDeb AND :dateFin ",Grandlivre.class); 
				
				query.setParameter("nom", nomUtilisateur);
				query.setParameter("dateDeb", dateDeb);
				query.setParameter("dateFin", dateFin);
				if(!(query.getResultList().isEmpty()))
					result = query.getResultList();
				else
					System.err.println("il n'y a pas de resultat");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
}
