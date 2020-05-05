package mg.fidev.service.impl;

/* Version 0.3 */

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import mg.fidev.model.Adresse;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Docidentite;
import mg.fidev.model.DroitInscription;
import mg.fidev.model.Garant;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ListeRouge;
import mg.fidev.service.IndividuelService;
import mg.fidev.utils.CodeIncrement;

@WebService(name = "individuelService", targetNamespace = "http://individuel.fidev.com/", serviceName = "individuelService", portName = "individuelServicePort", endpointInterface = "mg.fidev.service.IndividuelService")
public class IndividuelServiceImpl implements IndividuelService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction(); 

	/***
	 * LISTE DES CLIENTS INDIVIDUELS 
	 * ***/
	@Override
	public List<Individuel> getAllIndividuel() {
		TypedQuery<Individuel> q1 = em
				.createQuery(
						"select i from Individuel i where i.estClientIndividuel = :individuel",
						Individuel.class);
		q1.setParameter("individuel", true);
		List<Individuel> results = q1.getResultList();
		
		return results;
	}

	/***
	 * AFFICHER LES LISTES DES CLIENTS INDIVIDUELS INSCRIT DANS UNE CERTAINE PERIODE
	 * ***/
	public List<Individuel> getAllIndividuelByDate(Date startDate,
			Date endDate) {
		TypedQuery<Individuel> q1 = em
				.createQuery(
		"select i from Individuel i where i.dateInscription between :startDate and :endDate order by i.dateInscription",
		Individuel.class);
		q1.setParameter("startDate", startDate, TemporalType.DATE);
		q1.setParameter("endDate", endDate,  TemporalType.DATE);
		List<Individuel> results = q1.getResultList();

		return results;
	}


	/***
	 * ENREGISTREMENT INDIVIDUEL
	 * ***/
	@Override
	public String insertIndividuel(Individuel individuel, String codeAgence,
			Docidentite docIdentite, Adresse adresse) {
		
		individuel.setCodeInd(CodeIncrement.getCodeInd(em, codeAgence));
		individuel.setEstClientIndividuel(true);
		individuel.setAdresse(adresse);
		docIdentite.setIndividuel(individuel);
		try {
			
			if(individuel.getEstGarant() == true){
				Garant gar = new Garant();
				
				gar.setCodeGarant(CodeIncrement.getCodeGar(em, codeAgence));
				gar.setCodeIndividuel(individuel.getCodeInd());
				gar.setDateInscription(individuel.getDateInscription());
				gar.setDateNais(individuel.getDateNaissance());
				gar.setEmail(individuel.getEmail());
				gar.setEstClientIndividuel(true);
				gar.setNom(individuel.getNomClient());
				gar.setPrenom(individuel.getPrenomClient());
				gar.setProfession(individuel.getProfession());
				gar.setSexe(individuel.getSexe());
				gar.setAdresse(adresse);
				docIdentite.setGarant(gar);
				transaction.begin();
				em.persist(gar);
				em.persist(docIdentite);
				transaction.commit();
				em.refresh(gar);
				individuel.setEstGarant(true);
				individuel.setCodeGarant(gar.getCodeGarant());
				
			}
			
			transaction.begin();
			em.persist(individuel);
			em.persist(docIdentite);
			transaction.commit();
			em.refresh(individuel);
			em.refresh(docIdentite);
			System.out.println("Insertion nouveau client individuel avec succès");
			return "Succes";
		} catch (Exception e) {
			System.err.println("Erreur insertion individuel "+e.getMessage());
			return "Error";
		}
	}

	/***
	 * ENREGISTREMENT DE NOUVEAU GARANT
	 * ***/
	@Override
	public boolean saveGarant(Individuel individuel, Adresse adresse, Docidentite docId, String codeAgence) {
		
		Garant gar = new Garant();
		
		gar.setCodeGarant(CodeIncrement.getCodeGar(em, codeAgence));
		gar.setDateInscription(individuel.getDateInscription());
		gar.setDateNais(individuel.getDateNaissance());
		gar.setEmail(individuel.getEmail());
		gar.setNom(individuel.getNomClient());
		gar.setPrenom(individuel.getPrenomClient());
		gar.setProfession(individuel.getProfession());
		gar.setSexe(individuel.getSexe());
		gar.setAdresse(adresse);
		docId.setGarant(gar);
		
		try {
			
			if(individuel.getEstClientIndividuel() == true){
				individuel.setCodeInd(CodeIncrement.getCodeInd(em, codeAgence));				
				individuel.setEstGarant(true);
				individuel.setCodeGarant(gar.getCodeGarant());
				//individuel.setEstClientIndividuel(true);
				individuel.setAdresse(adresse);
				docId.setIndividuel(individuel);
			
				transaction.begin();
				em.persist(individuel);
				em.persist(docId);
				transaction.commit();
				em.refresh(individuel);
				em.refresh(docId);
	
				gar.setEstClientIndividuel(true);
				gar.setCodeIndividuel(individuel.getCodeInd());
			}
	
			transaction.begin();
			em.persist(gar);
			em.persist(docId);
			transaction.commit();
			em.refresh(gar);
			return true;
		} catch (Exception e) {
			e.getMessage();
			return false;
		}
	}
	
	/***
	 * AJOUTER UN CLIENT AU LISTE ROUGE
	 * ***/

	@Override
	public String addListeRouge(ListeRouge listerouge, String codeInd,
			String codeGroupe) {
		Groupe grp=null;
		Individuel ind=null;
		String result = "";
		try {			
			if(!codeInd.equals("")){
				ind = em.find(Individuel.class, codeInd);
				listerouge.setIndividuel(ind);
				if(listerouge.isRouge() == true){
					ind.setListeRouge(true);					
				}else{
					ind.setListeNoir(true);
				}
			}else if(!codeGroupe.equals("")){
				grp = em.find(Groupe.class, codeGroupe);
				listerouge.setGroupe(grp);
			}
			transaction.begin();
			em.flush();
			em.persist(listerouge);
			transaction.commit();
			em.refresh(listerouge);
			em.refresh(ind);
			result = "Enregistrement reussit!!!";
			System.out.println("Enregistrement reussit!!!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/***
	 * AFFICHAGE LISTE ROUGE
	 * ***/
	@Override
	public List<ListeRouge> afficheListeRouge(String agence,String dateDeb,String dateFin) {
		String sql = "SELECT l FROM ListeRouge l ";
		if(!agence.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			if(!agence.equals("")){
				sql+="JOIN l.individuel i WHERE i.codeInd LIKE '"+agence+"%'";
				if(!dateDeb.equals("") && dateFin.equals("")){
					sql+=" AND l.date = '"+dateDeb+"'" ;
				}
				if(!dateFin.equals("") && !dateFin.equals("")){
					sql+=" AND l.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'"  ;
				}
			}
			
			if(!dateDeb.equals("") && dateFin.equals("") && agence.equals("")){
				sql+="WHERE l.date = '"+dateDeb+"'" ;
			}
			if(!dateFin.equals("") && !dateFin.equals("") && agence.equals("")){
				sql+="WHERE l.date BETWEEN '"+dateDeb+"' AND '"+dateFin+"'" ;
			}	
		
		}
		TypedQuery<ListeRouge> query = em.createQuery(sql, ListeRouge.class);
		List<ListeRouge> listRouge = query.getResultList();
		return listRouge;
	}

	//chercher client individuel par son code
	@Override
	public List<Individuel> findByCode(String code) {
		TypedQuery<Individuel> query = em.createQuery("SELECT i FROM Individuel i WHERE i.codeInd LIKE :code"
				+ " AND i.estClientIndividuel = :x",Individuel.class);
		query.setParameter("code", code+"%");
		query.setParameter("x", true);
		
		List<Individuel> result = query.getResultList();
		
		if(!result.isEmpty())return result;
		else System.out.println("Auccun client trouvé!!!");
		
		return null;
	}

	//liste garant crédit
	@Override
	public List<Garant> listeGarant() {
		TypedQuery<Garant> query = em.createQuery("SELECT i FROM Garant i",Garant.class);
		if(!query.getResultList().isEmpty()){
			List<Garant> result = query.getResultList();
			return result;
		}
		return null;
	}


	//enregistrement droit d'inscription
	@Override
	public boolean saveDroitInscription(DroitInscription droit, String codeInd,String codeGrp) {
		
		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGrp);
		try {		
			if(ind!=null){
				droit.setCodeInd(ind);
				ind.setListeRouge(true);
			}else if(grp != null){
				droit.setGroupe(grp);
			}
			
			transaction.begin();
			em.flush();
			em.persist(droit);
			transaction.commit();
			em.refresh(droit);			
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//rapport individuel
	@Override
	public List<Individuel> rapportsIndividuel(String agence, String adresse,
			String nivEtude, String sexe, String profession, String dateDeb,
			String dateFin, String startDate, String endDate) {
		
		String sql = "select i from Individuel i join i.adresse a where i.estClientIndividuel = '"+true+"' ";		
		
		if(!agence.equals("") || !adresse.equals("") || !nivEtude.equals("") || !sexe.equals("") 
				|| !profession.equals("") || !dateDeb.equals("") || !dateFin.equals("") || !startDate.equals("")
				|| !endDate.equals("") ){
			
			if(!adresse.equals("")){
				sql+=" and a.adressePhysique like '%"+ adresse +"%'";
			}			
			if(!agence.equals("")){
				sql+=" and i.codeInd like '%"+ agence +"%'";
			}
			if(!nivEtude.equals("")){
				sql+=" and i.niveauEtude like '%"+ nivEtude +"%'";
			}
			if(!sexe.equals("")){
				sql+=" and i.sexe like '%"+ sexe +"%'";
			}
			if(!profession.equals("")){
				sql+=" and i.profession like '%"+ profession +"%'";
			}
			if(!dateDeb.equals("") && dateFin.equals("")){
				sql+=" and i.dateNaissance ='"+ dateDeb +"'";
							
			}
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql+=" and i.dateNaissance between '"+ dateDeb +"' and '"+ dateFin +"'";
				
			}	
			
			if(!startDate.equals("") && endDate.equals("")){
				sql+=" and i.dateInscription ='"+ startDate +"'";
						
			}	
			if(!startDate.equals("") && !endDate.equals("")){
				sql+=" and i.dateInscription between '"+ startDate +"' and '"+ endDate +"'";	
			}		
			
		}
		
		sql +=" order by i.nomClient asc";
		
		TypedQuery<Individuel> query = em.createQuery(sql, Individuel.class);
				
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}		
		return null;
	}

	//Rapport CIN
	@Override
	public List<Docidentite> rapportCIN(String dateDeb, String dateFin) {
		
		String sql = "select d from Docidentite d join d.individuel i where d.garant is null ";
		
		if(!dateDeb.equals("") || !dateFin.equals("") ){
			
			if(!dateDeb.equals("") && dateFin.equals("")){
				sql+=" and i.dateInscription ='"+ dateDeb +"'";
				
			}
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql+=" and i.dateInscription between '"+ dateDeb +"' and '"+ dateFin +"'";
				
			}				
			
		}
		
		TypedQuery<Docidentite> result = em.createQuery(sql,Docidentite.class);
		
		if(!result.getResultList().isEmpty())
			return 	result.getResultList();
		return null;
	}

	//recupérer les crédits d'un client
	@Override
	public List<DemandeCredit> getDetailCredit(String code) {
		Individuel ind = em.find(Individuel.class, code);
		return ind.getDemandeCredits();
	}

	//recupérer les comptes épargne d'un client
	@Override
	public List<CompteEpargne> getDetailEpargne(String code) {
		Individuel ind = em.find(Individuel.class, code);
		return ind.getCompteEpargnes();
	}

	@Override
	public Individuel getOneIndividuel(String code) {
		Individuel ind = em.find(Individuel.class, code);
		return ind;
	}

	@Override
	public boolean deleteIndividuel(String code) {
		Individuel ind = em.find(Individuel.class, code);
		try {
			transaction.begin();
			em.remove(ind);
			transaction.commit();
			em.refresh(ind); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * fonction qui permet de ferivier la date de naissance d'un client parraport au date délivrance CIN
	 * @paramètre(dateDebut,dateFin)
	 * */
	
	@Override
	public long verifDate(String dateDeb, String dateFin) {
		try {			
			LocalDate date1 = LocalDate.parse(dateDeb);
			LocalDate date2 = LocalDate.parse(dateFin);
			System.out.println("date nais "+date1);
			System.out.println("date CIN "+date2);
	
			long val = ChronoUnit.YEARS.between(date1,date2);			
			System.out.println(val + "\n");			
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}	
	/*
	 * méthode pour changer un client en sain
	 * @param(String codeIndividuel)
	 * */	
	@Override
	public boolean changerSain(String code) {
		Individuel ind = em.find(Individuel.class, code);
		try {
			ind.setListeNoir(false);
			ind.setListeRouge(false);
			ind.setSain(true);			
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(ind);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean approbationClient(String code) {
		Individuel ind = em.find(Individuel.class, code);
		ind.setApprouver(true);
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(ind); 
			System.out.println("Client approuvé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Individuel> getClientApprouver() {
		TypedQuery<Individuel> q1 = em
		.createQuery("select i from Individuel i where i.approuver = :individuel",
						Individuel.class);
		q1.setParameter("individuel", true);
			
		return q1.getResultList();
	}

	@Override
	public List<Individuel> getClientNonApprouver() {
		TypedQuery<Individuel> q1 = em
				.createQuery("select i from Individuel i where i.approuver = :individuel",
								Individuel.class);
				q1.setParameter("individuel", false);
					
				return q1.getResultList();
	}
	
}
