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
import mg.fidev.model.Fonction;
import mg.fidev.model.Personnel;
import mg.fidev.model.Utilisateur;

@WebService(name="UserService", targetNamespace="http://user.fidev.com/", serviceName="userService", portName="userServicePort")
@SOAPBinding(parameterStyle=ParameterStyle.WRAPPED)
public interface UserService {
	
	@WebMethod 
	@WebResult(name="validation")
	public boolean insertUser(
			@XmlElement(required=true) @WebParam(name="nomUser") String nomUser,
			@XmlElement(required=true) @WebParam(name="loginUser") String loginUser,
			@XmlElement(required=true) @WebParam(name="mdpUser") String mdpUser,
			@XmlElement(required=true) @WebParam(name="mdpUserConf") String mdpUserConf,
			@XmlElement(required=true) @WebParam(name="genreUser") String genreUser,
			@XmlElement(required=true) @WebParam(name="telUser") String telUser,			
			@XmlElement(required=true) @WebParam(name="fonction") int fonctionId);
	//@XmlElement(required=false,nillable=true) @WebParam(name="compteCaisse") List<String> listCptCaisse,
	
	@WebMethod
	@WebResult(name="resultAuth")
	public Utilisateur authentifie(
			@XmlElement(required=true) @WebParam(name="login") String loginUser, 
			@XmlElement(required=true) @WebParam(name="mdp") String mdpUser);
	
	@WebMethod @WebResult(name="listeAcces")
	public List<String> getAcces(
			@XmlElement(required=true) @WebParam(name="userName") int userName
			);
	
	
	@WebMethod @WebResult(name="cptCaisse")
	public boolean ajoutCptCaisse(
			@XmlElement(required=true) @WebParam(name="cptCaisse") Caisse cptCaisse,
			@XmlElement(required=true) @WebParam(name="planCompta") int numCptCompta
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
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Agence> getAgence();
	
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
	public Utilisateur updateUser(
			@XmlElement(required=true) @WebParam(name="utilisateur")Utilisateur ut);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteUser(
	@XmlElement(required=true) @WebParam(name="id")int id);
	
	//liste utilisateurs par fonction
	@WebMethod
	@WebResult(name="resultat")
	public List<Utilisateur> getUser(
	@XmlElement(required=true) @WebParam(name="fonction")String nomFonction);
}
