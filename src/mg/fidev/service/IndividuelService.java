package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Adresse;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Docidentite;
import mg.fidev.model.DroitInscription;
import mg.fidev.model.Garant;
import mg.fidev.model.Individuel;
import mg.fidev.model.ListeRouge;

@WebService(name = "individuelService", targetNamespace = "http://individuel.fidev.com/", serviceName = "individuelService", portName = "individuelServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface IndividuelService {

	//Enregistrement garant crédit
	@WebMethod
	@WebResult(name = "resultat")
	public boolean saveGarant(
			@WebParam(name = "individuel") @XmlElement(required = true) Individuel individuel,
			@WebParam(name = "adresse") @XmlElement(required = true) Adresse adresse,
			@WebParam(name = "docIdentite") @XmlElement(required = true) Docidentite docId,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence);
	
	//Enregistrement client individuel
	@WebMethod
	@WebResult(name = "validation")
	public boolean insertIndividuel( 
			@WebParam(name = "individuel") @XmlElement(required = true) Individuel individuel,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence,
			@WebParam(name = "docIdentite") @XmlElement(required = true) Docidentite docIdentite,
			@WebParam(name = "adresse") @XmlElement(required = true) Adresse adresse,
			@WebParam(name = "codeGrp") @XmlElement(required = true) String codeGrp);
	
	//Modifier client individuel
	@WebMethod
	@WebResult(name = "validation")
	public boolean updateIndividuel(
			@WebParam(name = "individuel") @XmlElement(required = true) Individuel individuel,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence,
			@WebParam(name = "docIdentite") @XmlElement(required = true) Docidentite docIdentite,
			@WebParam(name = "adresse") @XmlElement(required = true) Adresse adresse,
			@WebParam(name = "codeGrp") @XmlElement(required = true) String codeGrp,
			@WebParam(name = "idAdresse") @XmlElement(required = true) int idAdresse
			);
	
	//Supprimer client individuel
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteIndividuel(
			@WebParam(name = "codeInd")@XmlElement(required = false)String code,
			@WebParam(name = "dateSupp")@XmlElement(required = false)String date,
			@WebParam(name = "raison")@XmlElement(required = false)String raison
			);
	
	//Liste de tous les clients individuels
	@WebMethod
	@WebResult(name = "resultat")
	public List<Individuel> getAllIndividuel();
	
	//Rapport client individuel
	@WebMethod
	@WebResult(name = "resultat")
	public List<Individuel> rapportsIndividuel(
			@WebParam(name = "agence") @XmlElement(required = false, nillable= true)String agence, 
			@WebParam(name = "adresse") @XmlElement(required = false, nillable= true)String adresse,
			@WebParam(name = "nivEtude") @XmlElement(required = false, nillable= true)String nivEtude,
			@WebParam(name = "sexe") @XmlElement(required = false, nillable= true)String sexe,
			@WebParam(name = "profession") @XmlElement(required = false, nillable= true)String profession,
			@WebParam(name = "dateDeb") @XmlElement(required = false, nillable= true)String dateDeb, 
			@WebParam(name = "dateFin") @XmlElement(required = false, nillable= true) String dateFin,
			@WebParam(name = "startDate") @XmlElement(required = false, nillable= true)String startDate, 
			@WebParam(name = "endDate") @XmlElement(required = false, nillable= true) String endDate);

	/***
	 * AJOUTER AU LISTE ROUGE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public String addListeRouge(
			@WebParam(name = "formulaire")@XmlElement(required = true) ListeRouge listerouge,
			@WebParam(name = "codeInd")@XmlElement(required = false) String codeInd,
			@WebParam(name = "codeGrp")@XmlElement(required = false) String codeGroupe);
	
	/***
	 * AFFICHE LISTE ROUGE
	 * ***/
	@WebMethod
	@WebResult(name="liste_rouges")
	public List<ListeRouge> afficheListeRouge(
			@WebParam(name = "agence")@XmlElement(required = false)String agence,
			@WebParam(name = "dateDeb")@XmlElement(required = false)String dateDeb,
			@WebParam(name = "dateFin")@XmlElement(required = false)String dateFin);
	
	/***
	 * CHERCHER CLIENT PAR CODE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<Individuel> findByCode(@WebParam(name = "code")@XmlElement(required = true)String code);
	
	//Liste garant de crédit
	@WebMethod
	@WebResult(name="resultat")
	public List<Garant> listeGarant();
	
	//Enregistrement droit d'inscription
	@WebMethod
	@WebResult(name="validation")
	public boolean saveDroitInscription(
			@WebParam(name="formulaire")@XmlElement(required=false,nillable=true) DroitInscription droit,
			@WebParam(name="codeInd")@XmlElement(required=false,nillable=true)String codeInd,
			@WebParam(name="codeGrp")@XmlElement(required=false,nillable=true)String codeGrp);
	
	//Rapport CIN
	@WebMethod
	@WebResult(name="resultat")
	public List<Docidentite> rapportCIN(
			@WebParam(name = "dateDeb")@XmlElement(required = false)String dateDeb,
			@WebParam(name = "dateFin")@XmlElement(required = false)String dateFin);
	
	//Liste crédit du client en question
	@WebMethod
	@WebResult(name="resultat")
	public List<DemandeCredit> getDetailCredit(
			@WebParam(name = "codeClient")@XmlElement(required = false)String code
			);
	
	//Détail compte épargne du client
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> getDetailEpargne(
			@WebParam(name = "codeClient")@XmlElement(required = false)String code
			);
	
	//Chercher client individuel par son code d'enregistrement
	@WebMethod
	@WebResult(name="resultat")
	public Individuel getOneIndividuel(
			@WebParam(name = "codeClient")@XmlElement(required = false)String code
			);
	
	//Vérification date de naissance du client
	@WebMethod
	@WebResult(name="resultat")
	public long verifDate(
	@WebParam(name = "dateDeb")@XmlElement(required = false)String dateDeb, 
	@WebParam(name = "dateFin")@XmlElement(required = false)String dateFin);
	
	//Chager client en sain
	@WebMethod
	@WebResult(name="validation")
	public boolean changerSain(
			@WebParam(name = "codeClient")@XmlElement(required = false)String code);
	
	//Approuver client
	@WebMethod
	@WebResult(name="validation")
	public boolean approbationClient(
	@WebParam(name = "codeClient")@XmlElement(required = false)String code);
	
	//Liste client approuvé
	@WebMethod
	@WebResult(name="resultat")
	public List<Individuel> getClientApprouver();
	
	//Liste client non approuvé
	@WebMethod
	@WebResult(name="resultat")
	public List<Individuel> getClientNonApprouver();
	
	//Recuperé CIN ou Passeport client
	@WebMethod
	@WebResult(name="resultat")
	public Docidentite getDocidentite(
			@WebParam(name = "codeInd")@XmlElement(required = false)String codeInd);
}
