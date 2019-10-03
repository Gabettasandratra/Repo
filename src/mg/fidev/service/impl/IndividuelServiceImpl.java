package mg.fidev.service.impl;

/* Version 0.3 */

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
import mg.fidev.model.Docidentite;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.ListeRouge;
import mg.fidev.service.IndividuelService;
import mg.fidev.utils.CodeIncrement;
import mg.fidev.xmlRequest.AdresseXml;
import mg.fidev.xmlRequest.DocidentiteXml;
import mg.fidev.xmlRequest.IndividuelXml;

@WebService(name = "individuelService", targetNamespace = "http://individuel.fidev.com/", serviceName = "individuelService", portName = "individuelServicePort", endpointInterface = "mg.fidev.service.IndividuelService")
public class IndividuelServiceImpl implements IndividuelService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	
	/****
	 * ENREGISTREMENT CLIENT INDIVIDUEL
	 * **/
	//Methode pour Enregister un Client individuel
	@Override
	public String saveIndividuel(IndividuelXml request, String codeAgence) {
		/* CETTE OPERATION CONSISTE A TRANSFORMER L'OBJET REQUEST EN PERSISTENCE */
		/* INDIVIDUEL A SUAVEGARDER */
		Individuel individu = new Individuel();
		individu.setNomClient(request.getNomClient());
		individu.setCodeInd(CodeIncrement.getCodeInd(em, codeAgence));
		individu.setPrenomClient(request.getPrenomClient());
		individu.setSexe(request.getSexe());
		individu.setEmail(request.getEmail());
		individu.setNumeroMobile(request.getNumeroMobile());
		individu.setDateInscription(request.getDateInscription());
		individu.setDateNaissance(request.getDateNaissance());
		//individu.setCodeAgence(request.getCodeAgence());
		individu.setTitre(request.getTitre());

		/* config */
		individu.setEstClientIndividuel(true);
		individu.setEstGarant(false);
		individu.setGroupe(null);

		/* les info perso */
		individu.setParentNom(request.getParentNom());
		individu.setParentAdresse(request.getParentAdresse());
		individu.setProfession(request.getProfession());
		individu.setLangue(request.getLangue());
		individu.setLieuNaissance(request.getLieuNaissance());
		individu.setEtatCivil(request.getEtatCivil());
		individu.setNbEnfant(request.getNbEnfant());
		individu.setNbPersCharge(request.getNbPersCharge());
		individu.setNiveauEtude(request.getNiveauEtude());
		individu.setNomConjoint(request.getNomConjoint());

		// Les docidentités sont justes préparés mais pas encore associés à
		// l'individu car pas de codeclient generer
		List<DocidentiteXml> listesDocRequest = request.getDocidentites();
		List<Docidentite> listesDocPersistence = new ArrayList<Docidentite>();
		for (DocidentiteXml doc : listesDocRequest) {
			Docidentite document = new Docidentite();
			document.setTypedoc(doc.getTypedoc());
			System.err.println("Num :"+doc.getNumero());
			document.setNumero(doc.getNumero());
			document.setDelivrePar(doc.getDelivrePar());
			document.setDateEmis(doc.getDateEmis());
			document.setDateExpire(doc.getDateExpire());
			document.setPriorite(doc.getPriorite());
			document.setIndividuel(individu);
			listesDocPersistence.add(document);
		}

		// L'adresse qui est optional
		if (request.getAdresse() != null) {
			AdresseXml adresseRequest = request.getAdresse();
			Adresse adresse = new Adresse();
			adresse.setAdresseRegion(adresseRequest.getAdresseRegion());
			adresse.setAdresseVille(adresseRequest.getAdresseVille());
			adresse.setAdresseDistrict(adresseRequest.getAdresseDistrict());
			adresse.setAdresseCommune(adresseRequest.getAdresseCommune());
			adresse.setAdressePhysique(adresseRequest.getAdressePhysique());
			adresse.setDistanceAgence(adresseRequest.getDistanceAgence());
			adresse.setCodeRegion(adresseRequest.getCodeRegion());
			individu.setAdresse(adresse);
		}

		transaction.begin();
		em.persist(individu);
		for (Docidentite doc : listesDocPersistence)
			em.persist(doc);
		transaction.commit();
		em.refresh(individu); // pour que le em connait le id et code du nouveau
		return "Okay";
	}

	
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

	@Override
	///	Inutile
	public boolean testMoxy(Individuel individu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String saveIndividuelByEntity(Individuel request) {
		transaction.begin();
		em.persist(request);
		for (Docidentite doc : request.getDocidentites())
			em.persist(doc);
		transaction.commit();
		em.refresh(request); // pour que le em connait le id et code du nouveau
		return "Jereo ny base";
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
	public String saveGarant(Individuel individuel, Adresse adresse, Docidentite docId, String codeAgence) {
		individuel.setEstGarant(true);
		individuel.setAdresse(adresse);
		docId.setIndividuel(individuel);
		
		if(individuel.getEstClientIndividuel()){
			individuel.setCodeInd(CodeIncrement.getCodeInd(em, codeAgence));
		}
		else{
			individuel.setCodeInd(CodeIncrement.getCodeGar(em, codeAgence));
		}
		try {
			transaction.begin();
			em.persist(individuel);
			em.persist(docId);
			transaction.commit();
			em.refresh(individuel);
			em.refresh(docId);
			return "Insertion garant succes";
		} catch (Exception e) {
			return "Insertion garant failed "+e.getMessage();
		}
	}
	
	/***
	 * AJOUTER AU LISTE ROUGE
	 * ***/

	@Override
	public String addListeRouge(ListeRouge listerouge, String codeInd,
			String codeGroupe) {

		Individuel ind = em.find(Individuel.class, codeInd);
		Groupe grp = em.find(Groupe.class, codeGroupe);
		String result = "";
		try {
			
			if(ind!=null){
				listerouge.setIndividuel(ind);
			}else if(grp != null){
				listerouge.setGroupe(grp);
			}
			transaction.begin();
			em.persist(listerouge);
			transaction.commit();
			result = "Enregistrement reussit!!!";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/***
	 * AFFICHAGE DES INDIVIDUELS AU LISTE ROUGE
	 * ***/
	@Override
	public List<ListeRouge> afficheListeRouge() {
		TypedQuery<ListeRouge> query = em.createQuery("SELECT l FROM ListeRouge l", ListeRouge.class);
		List<ListeRouge> listRouge = query.getResultList();
		return listRouge;
	}
}
