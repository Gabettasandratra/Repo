package mg.fidev.service.impl;

/* Version 0.3 */

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import mg.fidev.model.Adresse;
import mg.fidev.model.Agence;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Docidentite;
import mg.fidev.model.DroitInscription;
import mg.fidev.model.Garant;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ListeRouge;
import mg.fidev.model.Utilisateur;
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
	 * ENREGISTREMENT CLIENT INDIVIDUEL
	 * ***/
	@Override
	public boolean insertIndividuel(Individuel individuel, String codeAgence,
			Docidentite docIdentite, Adresse adresse, String codeGrp) {
		
		if(!codeAgence.equals("")){			
			Agence ag = em.find(Agence.class, codeAgence);
			individuel.setCodeInd(CodeIncrement.getCodeInd(em, codeAgence));
			individuel.setEstClientIndividuel(true);
			individuel.setAdresse(adresse);
			individuel.setAgence(ag); 
			docIdentite.setIndividuel(individuel);
			try {
				
				if(individuel.getEstGarant() == true){
					Garant gar = new Garant(CodeIncrement.getCodeGar(em, codeAgence), individuel.getNomClient(), individuel.getPrenomClient(),
							individuel.getDateNaissance(),individuel.getDateInscription()
							, individuel.getEmail(), true, individuel.getProfession(), 
							individuel.getSexe(), individuel.getCodeInd(), null, adresse);
						
					docIdentite.setGarant(gar);
					transaction.begin();
					em.persist(gar);
					em.persist(docIdentite);
					transaction.commit();
					em.refresh(gar);
					individuel.setEstGarant(true);
					individuel.setCodeGarant(gar.getCodeGarant());
					
				}
				
				if(!codeGrp.equals("")){
					Groupe g = em.find(Groupe.class, codeGrp);
					individuel.setGroupe(g); 
				}
				
				transaction.begin();
				em.persist(individuel);
				em.persist(docIdentite);
				transaction.commit();
				em.refresh(individuel);
				em.refresh(docIdentite);
				System.out.println("Insertion nouveau client individuel");
				//return "Succes";
				return true;
			} catch (Exception e) {
				System.err.println("Erreur insertion individuel "+e.getMessage());
				//return "Error";
				return false;
			}
		}
		return false;
	}
	
	//Modifier client individuel
	@Override
	public boolean updateIndividuel(Individuel individuel, String codeAgence,
			Docidentite doc, Adresse adres, String codeGrp, int idAdresse) { 
		Individuel ind = em.find(Individuel.class, individuel.getCodeInd());
		Adresse ad = em.find(Adresse.class, idAdresse);
		ad = adres;
		
		Docidentite dc = em.find(Docidentite.class, doc.getNumero());
		dc.setDateEmis(doc.getDateEmis());
		dc.setDateExpire(doc.getDateExpire());
		dc.setDelivrePar(doc.getDelivrePar());
		dc.setNumero(doc.getNumero());
		dc.setPriorite(doc.getPriorite());
		if(!doc.getRecto().equals(""))
			dc.setRecto(doc.getRecto());
		if(!doc.getVerso().equals(""))
			dc.setVerso(doc.getVerso());
		dc.setTypedoc(doc.getTypedoc());
		
		Agence ag = em.find(Agence.class, codeAgence);
		
		ind.setAgence(ag); 
		ind.setNomClient(individuel.getNomClient());
		ind.setPrenomClient(individuel.getPrenomClient());
		ind.setDateInscription(individuel.getDateInscription());
		ind.setDateNaissance(individuel.getDateNaissance());
		ind.setEmail(individuel.getEmail());
		ind.setEstMembreGroupe(individuel.getEstMembreGroupe());
		ind.setEtatCivil(individuel.getEtatCivil());
		ind.setLangue(individuel.getLangue());
		ind.setLieuNaissance(individuel.getLieuNaissance());
		ind.setNbEnfant(individuel.getNbEnfant());
		ind.setNbPersCharge(individuel.getNbPersCharge());
		ind.setNiveauEtude(individuel.getNiveauEtude());
		ind.setNomConjoint(individuel.getNomConjoint());
		ind.setNumeroMobile(individuel.getNumeroMobile());
		ind.setParentAdresse(individuel.getParentAdresse());
		ind.setParentNom(individuel.getParentNom());
		if(!individuel.getPhoto().equals(""))
			ind.setPhoto(individuel.getPhoto());
		if(!individuel.getSignature().equals(""))
			ind.setSignature(individuel.getSignature());
		ind.setSecteurActiviter(individuel.getSecteurActiviter());
		ind.setProfession(individuel.getProfession());
		ind.setSexe(individuel.getSexe());
		ind.setTitre(individuel.getTitre());		
		
		ind.setAdresse(ad);
		dc.setIndividuel(ind);
		try {
			if(ind.getEstGarant() == false){
				if(individuel.getEstGarant() == true){
					Garant gar = new Garant(CodeIncrement.getCodeGar(em, codeAgence), individuel.getNomClient(), individuel.getPrenomClient(),
							individuel.getDateNaissance(),individuel.getDateInscription()
							, individuel.getEmail(), true, individuel.getProfession(), 
							individuel.getSexe(), ind.getCodeInd(), null, ad);
					
					dc.setGarant(gar);
					transaction.begin();
					em.persist(gar);
					em.persist(dc);
					transaction.commit();
					em.refresh(gar);
					ind.setEstGarant(true);
					ind.setCodeGarant(gar.getCodeGarant());
					
				}				
			}
						
			if(ind.getGroupe() == null && !codeGrp.equals("")){
				Groupe g = em.find(Groupe.class, codeGrp);
				ind.setGroupe(g); 
			}
			
			transaction.begin();
			em.merge(ind);
			em.merge(dc);
			transaction.commit();
			System.out.println("Modification client individuel reussit");
			//return "Succes";
			return true;
		} catch (Exception e) {
			System.err.println("Erreur modification individuel "+e.getMessage());
			//return "Error";
			return false;
		}
		
	}
	
	//Supprimer client individuel
	@Override
	public boolean deleteIndividuel(String code, String date, String raison) {
		Individuel ind = em.find(Individuel.class, code);
		ind.setDateSortie(date);
		ind.setRaisonSortie(raison); 
		ind.setSupprimer(true); 
		try {
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(ind); 
			System.out.println("Client individuel n° : "+ ind.getCodeInd() +" a été supprimé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur suppression client individuel");
			return false;
		}
	}

	/***
	 * ENREGISTREMENT GARANT
	 * ***/
	@Override
	public boolean saveGarant(Individuel individuel, Adresse adresse, Docidentite docId, String codeAgence) {
		
		Garant gar = new Garant();
		Agence ag = em.find(Agence.class, codeAgence);
		
		gar.setCodeGarant(CodeIncrement.getCodeGar(em, codeAgence));
		gar.setDateInscription(individuel.getDateInscription());
		gar.setDateNais(individuel.getDateNaissance());
		gar.setEmail(individuel.getEmail());
		gar.setNom(individuel.getNomClient());
		gar.setPrenom(individuel.getPrenomClient());
		gar.setProfession(individuel.getProfession());
		gar.setSexe(individuel.getSexe());
		gar.setSupprimer(false);
		gar.setAdresse(adresse);
		gar.setAgence(ag); 
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
	 * AFFICHE LISTE ROUGE
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
	public List<Individuel> findByCode(String code, int user) {
		Utilisateur ut = em.find(Utilisateur.class, user);
		List<Individuel> result = new ArrayList<Individuel>();
		
		List<Agence> ag = ut.getAgences();
		
		if(ag != null){
			for (Agence agence : ag) {
				String sql = "select i from Individuel i join i.agence ag where "
						+ "(i.codeInd LIKE '"+ code +"%' OR i.nomClient LIKE '"+ code +"%' OR i.prenomClient LIKE '"+ code +"%') "
						+ "AND i.estClientIndividuel = '"+ true +"' AND i.approuver ='"+ true +"' AND i.supprimer ='"+ false +"' "
								+ "AND ag.codeAgence='"+agence.getCodeAgence()+"'";
				
				TypedQuery<Individuel> query = em.createQuery(sql, Individuel.class);
							
				//List<Individuel> result = query.getResultList();
				
				if(!query.getResultList().isEmpty()){
					result.addAll(query.getResultList());
				}
				else System.out.println("Aucun client trouvé!!!");
			}			
		}
			
		return result;
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
				sql+=" and i.codeInd like '"+ agence +"%'";
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
		
		sql +=" and i.supprimer ='false' order by i.nomClient asc";
		
		TypedQuery<Individuel> query = em.createQuery(sql, Individuel.class);
				
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}		
		return null;
	}
	
	//Rapports clients individuels supprimés
	@Override
	public List<Individuel> rapportsIndividuelSupp(String agence, String dateDeb, String dateFin) {
		String sql = "select i from Individuel i where i.estClientIndividuel = '"+true+"' ";		
		
		if(!agence.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			if(!agence.equals("") ){
				sql+=" and i.codeInd like '"+ agence +"%'";
			}	
			
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql+=" and i.dateSortie between '"+ dateDeb +"' and '"+ dateFin +"'";	
			}	
			
		}
		
		sql +=" and i.supprimer ='true' order by i.nomClient asc";
		
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
			//if(val >= 18)
				
			//else
				
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
		ind.setSain(true);
		try {
			transaction.begin();
			em.merge(ind);
			transaction.commit();
			em.refresh(ind); 
			System.out.println("Client approuvé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Recuperé CIN ou Passeport client
	@Override
	public Docidentite getDocidentite(String codeInd) {
		String sql = "select d from Docidentite d join d.individuel i where i.codeInd='"+ codeInd +"'";
		TypedQuery<Docidentite> q = em.createQuery(sql, Docidentite.class);
		if(!q.getResultList().isEmpty())
			return (Docidentite) q.getSingleResult();
		return null;
	}

	//Recuperer client par utilisateurs
	@Override
	public List<Individuel> getIndividuelByUser(int user) {
		//String sql = "select i from Individuel i join i.agence a join a.utilisateurs u where  ";
		List<Individuel> result = new ArrayList<Individuel>();
		Utilisateur ut = em.find(Utilisateur.class, user);	
		for (Agence agence : ut.getAgences()) {
			for (Individuel ind : agence.getIndividuels()) {
				if(ind.getEstClientIndividuel() == true && ind.isApprouver() == true && ind.isSupprimer() == false)
					result.add(ind);					
			}
		}
		return result;
	}

	//Liste clients individuels approuvés
	@Override
	public List<Individuel> getClientApprouver(int user) {
		/*TypedQuery<Individuel> q1 = em
				.createQuery("select i from Individuel i where i.approuver = :ap and i.estClientIndividuel = :individuel AND i.supprimer =:z",
								Individuel.class);
				q1.setParameter("ap", true);
				q1.setParameter("individuel", true);
				q1.setParameter("z", false);
					
				return q1.getResultList();*/
		List<Individuel> result = new ArrayList<Individuel>();
		Utilisateur ut = em.find(Utilisateur.class, user);
		List<Agence> ag = ut.getAgences();
		for (Agence agence : ag) {
			List<Individuel> inds = agence.getIndividuels();
			for (Individuel individuel : inds) {
				if(individuel.isApprouver() == true && individuel.getEstClientIndividuel() == true 
						&& individuel.isSupprimer() == false)
					result.add(individuel);
			}
		}
		return result;
	}

	//Liste clients non approuvé
	@Override
	public List<Individuel> getClientNonApprouver(int user) {
		/*TypedQuery<Individuel> q1 = em
				.createQuery("select i from Individuel i where i.approuver = :individuel and i.estClientIndividuel = :ap"
						+ " AND i.supprimer =:z",Individuel.class);
				q1.setParameter("individuel", false);
				q1.setParameter("ap", true);
				q1.setParameter("z", false);
					
				return q1.getResultList();*/
		List<Individuel> result = new ArrayList<Individuel>();
		Utilisateur ut = em.find(Utilisateur.class, user);
		List<Agence> ag = ut.getAgences();
		for (Agence agence : ag) {
			List<Individuel> inds = agence.getIndividuels();
			for (Individuel individuel : inds) {
				if(individuel.isApprouver() == false && individuel.getEstClientIndividuel() == true 
						&& individuel.isSupprimer() == false)
					result.add(individuel);
			}
		}
		return result;
	}

	//Liste clients supprimés
	@Override
	public List<Individuel> getClientSupprimer(int user) {
		List<Individuel> result = new ArrayList<Individuel>();
		Utilisateur ut = em.find(Utilisateur.class, user);
		List<Agence> ag = ut.getAgences();
		for (Agence agence : ag) {
			List<Individuel> inds = agence.getIndividuels();
			for (Individuel individuel : inds) {
				if(individuel.isSupprimer() == true)
					result.add(individuel);
			}
		}
		return result;
	}
	
}
