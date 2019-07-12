package mg.fidev.service.impl;

import java.util.ArrayList;
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
import mg.fidev.xmlRequest.AdresseXml;
import mg.fidev.xmlRequest.GroupeXml;
import mg.fidev.xmlRequest.IndividuelXml;

@WebService(name = "groupeService", targetNamespace = "http://individuel.fidev.com/", serviceName = "groupeService", portName = "groupeServicePort", endpointInterface = "mg.fidev.service.GroupeService")
public class GroupeServiceImpl implements GroupeService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();

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
						"select i from Individuel i where i.codeClient = :codeInd",
						Individuel.class);
		q2.setParameter("codeInd", codeInd);
		Individuel individuel = q2.getSingleResult();

		// on ajoute dans le groupe
		transaction.begin();
		groupe.addIndividuel(individuel);
		individuel.setEstMembreGroupe(true);
		em.flush();
		em.flush();
		transaction.commit();
		return true;
	}

	@Override
	public List<GroupeXml> getAllGroupe() {
		TypedQuery<Groupe> q1 = em.createQuery("select g from Groupe g",
				Groupe.class);
		List<Groupe> results = q1.getResultList();
		List<GroupeXml> groups = new ArrayList<GroupeXml>();
		for (Groupe g : results) {
			GroupeXml groupe = new GroupeXml();
			groupe.setCodeClient(g.getCodeGrp());
			//groupe.setCodeAgence(g.getCodeAgence());
			groupe.setNomGroupe(g.getNomGroupe());
			groupe.setNumeroMobile(g.getNumeroMobile());
			groupe.setEmail(g.getEmail());
			groupe.setDateInscription(g.getDateInscription());
			groups.add(groupe);
		}
		return groups;
	}

	@Override
	public String saveGroupe(GroupeXml request, String codeAgence) {
		Groupe groupe = new Groupe();
		//groupe.setCodeAgence(request.getCodeAgence());
		groupe.setCodeGrp(CodeIncrement.getCodeGrp(em, codeAgence));
		groupe.setNomGroupe(request.getNomGroupe());
		groupe.setNumeroMobile(request.getNumeroMobile());
		groupe.setEmail(request.getEmail());
		groupe.setDateInscription(request.getDateInscription());

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
			groupe.setAdresse(adresse);
		}

		transaction.begin();
		em.persist(groupe);
		transaction.commit();
		em.refresh(groupe);
		return "Verifier la base de donnée";
	}

	@Override
	public List<IndividuelXml> getListeMembreGroupe(String nomGroupe) {
		TypedQuery<Groupe> q1 = em.createQuery(
				"select g from Groupe g where g.nomGroupe = :groupe",
				Groupe.class);
		q1.setParameter("groupe", nomGroupe);
		Groupe groupe = q1.getSingleResult();
		List<Individuel> membre = groupe.getIndividuels();
		// on converte en IndividuelXml
		List<IndividuelXml> individuels = new ArrayList<IndividuelXml>();
		for (Individuel i : membre) {
			IndividuelXml individuXml = new IndividuelXml();
			individuXml.setCodeClient(i.getCodeInd());
			individuXml.setNomClient(i.getNomClient());
			individuXml.setPrenomClient(i.getPrenomClient());
			individuXml.setEmail(i.getEmail());
			individuXml.setNumeroMobile(i.getNumeroMobile());
			individuXml.setDateInscription(i.getDateInscription());

			individuels.add(individuXml);
		}
		return individuels;
	}

	@Override
	public List<IndividuelXml> getListeNonMembre() {
		TypedQuery<Individuel> q1 = em
				.createQuery(
						"select i from Individuel i where i.estMembreGroupe = :value",
						Individuel.class);
		q1.setParameter("value", false);
		List<Individuel> results = q1.getResultList();

		// on converte en IndividuelXml
		List<IndividuelXml> individuels = new ArrayList<IndividuelXml>();
		for (Individuel i : results) {
			IndividuelXml individuXml = new IndividuelXml();
			individuXml.setCodeClient(i.getCodeInd());
			individuXml.setNomClient(i.getNomClient());
			individuXml.setPrenomClient(i.getPrenomClient());
			individuXml.setSexe(i.getSexe());
			individuXml.setEmail(i.getEmail());
			individuXml.setNumeroMobile(i.getNumeroMobile());
			individuXml.setDateInscription(i.getDateInscription());
			individuXml.setDateNaissance(i.getDateNaissance());
			//individuXml.setCodeAgence(i.getCodeAgence());
			individuXml.setTitre(i.getTitre());
			individuels.add(individuXml);
		}

		return individuels;
	}

}
