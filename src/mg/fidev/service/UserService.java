package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Agence;
import mg.fidev.model.Caisse;
import mg.fidev.model.FichierEtatCivil;
import mg.fidev.model.FichierLangue;
import mg.fidev.model.FichierNiveauetude;
import mg.fidev.model.FichierRegiongeo;
import mg.fidev.model.FichierSecteurActiviter;
import mg.fidev.model.FichierTitre;
import mg.fidev.model.Fonction;
import mg.fidev.model.Institution;
import mg.fidev.model.JourFerier;
import mg.fidev.model.Personnel;
import mg.fidev.model.Utilisateur;
import mg.fidev.model.UtilisateurSupprimer;

@WebService(name="UserService", targetNamespace="http://user.fidev.com/", serviceName="userService", portName="userServicePort")
@SOAPBinding(parameterStyle=ParameterStyle.WRAPPED)
public interface UserService {
	
	@WebMethod 
	@WebResult(name="validation")
	public boolean insertUser(
			@XmlElement(required=true) @WebParam(name="user") Utilisateur user,
			@XmlElement(required=true) @WebParam(name="mdpUserConf") String mdpUserConf,
			@XmlElement(required=true) @WebParam(name="fonction") int fonctionId,
			@XmlElement(required=true) @WebParam(name="agence") List<String> agence,
			@XmlElement(required=false) @WebParam(name="compteCaisse") List<String> listCptCaisse);
	
	@WebMethod
	@WebResult(name="resultAuth")
	public Utilisateur authentifie(
			@XmlElement(required=true) @WebParam(name="login") String loginUser, 
			@XmlElement(required=true) @WebParam(name="mdp") String mdpUser
			);
	
	@WebMethod @WebResult(name="listeAcces")
	public List<String> getAcces(
			@XmlElement(required=true) @WebParam(name="userName") int userName
			);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean addPersonnel(
			@XmlElement(required=false) @WebParam(name="infoPersonel")Personnel pers,
			@XmlElement(required=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false) @WebParam(name="fonction")int idFonction);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Personnel> getPersonnels();
	
	//---------------------------------------------------------------------------------------------
	//Gestion Agences
	
	//ajout agence
	@WebMethod
	@WebResult(name="validation")
	public boolean addAgence(
			@XmlElement(required=false) @WebParam(name="agence")Agence agence,
			@XmlElement(required=true) @WebParam(name="nomInstitution")String nomInstitution);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean activerLicence(
			@XmlElement(required=true) @WebParam(name="codeAgence")String codeAgence,
			@XmlElement(required=true) @WebParam(name="date")String date,
			@XmlElement(required=true) @WebParam(name="licence")int licence);
	
	@WebMethod
	@WebResult(name="validation")
	public List<Agence> desactiverLicence(
			@XmlElement(required=true) @WebParam(name="date")String date);
	
	//Liste de tous agences
	@WebMethod
	@WebResult(name="resultat")
	public List<Agence> getAgence();
	
	//Chercher agence par Identifiant
	@WebMethod
	@WebResult(name="resultat")
	public Agence getAgenceById(
			@XmlElement(required=false) @WebParam(name="id")String id);
	
	//Liste agence par utilisateur
	@WebMethod
	@WebResult(name="resultat")
	public List<Agence> getAgenceByUser(
			@XmlElement(required=false) @WebParam(name="idUser")int idUser);
	
	
	//Liste utilisateurs par agences
	@WebMethod
	@WebResult(name="resultat")
	public List<Utilisateur> getUserByAgence(
			@XmlElement(required=false) @WebParam(name="codeAgence")String code);
	
	//--------------------------------------------------------------------------------------------
	/************************************ Gestion utilisateurs **********************************/
	@WebMethod
	@WebResult(name="resultat")
	public List<Fonction> getFonction();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Utilisateur> getAllUser();
	
	@WebMethod
	@WebResult(name="resultat")
	public Utilisateur findUser(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	@WebMethod
	@WebResult(name="resultat")
	public Utilisateur updateProfilUser(
		@XmlElement(required=true) @WebParam(name="idUser") int idUser,
		@XmlElement(required=true) @WebParam(name="nomUser") String nomUser,
		@XmlElement(required=true) @WebParam(name="loginUser") String loginUser,
		@XmlElement(required=true) @WebParam(name="genreUser") String genreUser,
		@XmlElement(required=true) @WebParam(name="telUser") String telUser,
		@XmlElement(required=true) @WebParam(name="adresse") String adresse,
		@XmlElement(required=true) @WebParam(name="photo") String photo,
		@XmlElement(required=true) @WebParam(name="validiterCompte") int validCompte,
		@XmlElement(required=true) @WebParam(name="validiterMdp") int validMdp,
		@XmlElement(required=true) @WebParam(name="fonction") int fonctionId,
		@XmlElement(required=true) @WebParam(name="agence") List<String> agence,
		@XmlElement(required=true) @WebParam(name="compteCaisse") List<String> caisse
		);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteUser(
	@XmlElement(required=true) @WebParam(name="id")int id);
	
	//liste utilisateurs par fonction
	@WebMethod
	@WebResult(name="resultat")
	public List<Utilisateur> getUser(
	@XmlElement(required=true) @WebParam(name="fonction")String nomFonction);
	
	//Liste Utilisateurs supprimés
	@WebMethod
	@WebResult(name="resultat")
	public List<UtilisateurSupprimer> getUserSupprimer();

	@WebMethod
	@WebResult(name="validation")
	public List<Utilisateur> desactiverCompteUser(
			@XmlElement(required=true) @WebParam(name="date")String date);
	
	@WebMethod
	@WebResult(name="validation")
	public List<Utilisateur> desactiverMdp(
			@XmlElement(required=true) @WebParam(name="date")String date);
	
	//------------------------------------------------------------------------------
	/*********************** Jours férié ****************************/
	//Liste Jours Feriés
	@WebMethod
	@WebResult(name="resultat")
	public List<JourFerier> getJoursFerier();
	
	//Ajout jour férié
	@WebMethod
	@WebResult(name="validation")
	public boolean addJourFerie(
	@XmlElement(required=true) @WebParam(name="jourFerier")JourFerier jourFerier);
	
	//Modification jour férié
	@WebMethod
	@WebResult(name="validation")
	public boolean editJourFerie(
	@XmlElement(required=true) @WebParam(name="jourFerier")JourFerier jourFerier);

	//Supprimer utilisateur
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteJourFerier(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	
	/****************************** Fichier de supports *************************/
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierTitre(
			@XmlElement(required=true) @WebParam(name="titre")FichierTitre titre);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierTitre> getTitre();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteTitre(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	//Etat civil
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierEtatCivil(
			@XmlElement(required=true) @WebParam(name="etatCivil")FichierEtatCivil etat);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierEtatCivil> getEtatCivil();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteEtatCivil(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	//Fichier Langue
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierLangue(
			@XmlElement(required=true) @WebParam(name="langue")FichierLangue langue);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierLangue> getLangue();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteLangue(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	//Fichier Niveau etude
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierNiveau(
			@XmlElement(required=true) @WebParam(name="niveau")FichierNiveauetude niveau);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierNiveauetude> getNiveau();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteNiveau(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	//Fichier Secteur activité
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierSecteur(
			@XmlElement(required=true) @WebParam(name="secteur")FichierSecteurActiviter secteur);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierSecteurActiviter> getSecteur();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteSecteur(
			@XmlElement(required=true) @WebParam(name="id")int id);

	//Fichier Region
	@WebMethod
	@WebResult(name="validation")
	public boolean addFichierRegion(
			@XmlElement(required=true) @WebParam(name="region")FichierRegiongeo region);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierRegiongeo> getRegion();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteRegion(
			@XmlElement(required=true) @WebParam(name="id")String id);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<FichierRegiongeo> findCommune(
			@XmlElement(required=true) @WebParam(name="code")String code);
	
	//--------------------------------------------------------------------------------
	//Institution
	@WebMethod
	@WebResult(name="validation")
	public boolean addInstitution(
			@XmlElement(required=false) @WebParam(name="data")Institution data);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean updateInstitution(
			@XmlElement(required=true) @WebParam(name="id")int id,
			@XmlElement(required=false) @WebParam(name="data")Institution data);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Institution> getAllInstitution();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteInstitution(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	@WebMethod
	@WebResult(name="resultat")
	public Institution findUniqueInstitution(
			@XmlElement(required=true) @WebParam(name="id")int id);
	
	//-------------------------------------------------------------------------------------
	/********************************** Gestion caisse ***********************************/
	@WebMethod @WebResult(name="cptCaisse")
	public boolean ajoutCptCaisse(
			@XmlElement(required=true) @WebParam(name="cptCaisse") Caisse cptCaisse,
			@XmlElement(required=true) @WebParam(name="planCompta") int numCptCompta
			);
	
	//Modifier caisse
	@WebMethod 
	@WebResult(name="validation")
	public boolean updateCaisse(
			@XmlElement(required=true) @WebParam(name="idCaisse") String idCaisse,
			@XmlElement(required=false) @WebParam(name="cptCaisse") Caisse cptCaisse,
			@XmlElement(required=true) @WebParam(name="planCompta") int numCptCompta);
	
	//Supprimer caisse 
	@WebMethod  
	@WebResult(name="validation")
	public boolean deleteCaisse(
			@XmlElement(required=true) @WebParam(name="idCaisse") String idCaisse);
	
	//Liste caisse par utilisateur
	@WebMethod 
	@WebResult(name="resultat")
	public List<Caisse> getCaisseByUser(
			@XmlElement(required=true) @WebParam(name="idUser") int idUser);
	
	//Liste de tous les comptes caisses
	@WebMethod 
	@WebResult(name="resultat")
	public List<Caisse> getAllCaisse();
	
}
