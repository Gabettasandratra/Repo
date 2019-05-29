package mg.fidev.service;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Acces;

@WebService(name="UserService", targetNamespace="http://user.fidev.com/", serviceName="userService", portName="userServicePort")
@SOAPBinding(parameterStyle=ParameterStyle.WRAPPED)
public interface UserService {
	@WebMethod @WebResult(name="resultInsert")
	public boolean insertUser(
			@XmlElement(required=true) @WebParam(name="nomUser") String nomUser,
			@XmlElement(required=true) @WebParam(name="loginUser") String loginUser,
			@XmlElement(required=true) @WebParam(name="mdpUser") String mdpUser,
			@XmlElement(required=true) @WebParam(name="genreUser") String genreUser,
			@XmlElement(required=true) @WebParam(name="telUser") String telUser,
			@XmlElement(required=true) @WebParam(name="fonction") int fonctionId);
	
	@WebMethod @WebResult(name="resultAuth")
	public List<Acces> authentifie(
			@XmlElement(required=true) @WebParam(name="login") String loginUser, 
			@XmlElement(required=true) @WebParam(name="mdp") String mdpUser);
	
	@WebMethod @WebResult(name="listeAcces")
	public List<String> getAcces(
			@XmlElement(required=true) @WebParam(name="userName") String userName
			);
	
	@WebMethod @WebResult(name="statusCreateCmpt")
	public boolean creerCompteClient(
			@XmlElement(required=true) @WebParam(name="numCompte") String numCompte,
			@XmlElement(required=true) @WebParam(name="dateCree") Date dateOuverture,
			@XmlElement(required=true) @WebParam(name="dateEcheance") Date dateEcheance,
			@XmlElement(required=true) @WebParam(name="soldeCpt") double solde,
			@XmlElement(required=true) @WebParam(name="idProduitEp") String idProduitEp,
			@XmlElement(required=true) @WebParam(name="codeInd") int individuelId,
			@XmlElement(required=true) @WebParam(name="codeGrp") int groupeId,
			@XmlElement(required=true) @WebParam(name="userId") int userId
			);
	
	@WebMethod @WebResult(name="transacResult")
	public boolean transactionCompte(
			@XmlElement(required=true) @WebParam(name="type") String typeTransac,
			@XmlElement(required=true) @WebParam(name="date") Date dateTransac,
			@XmlElement(required=true) @WebParam(name="montant") double montant,
			@XmlElement(required=true) @WebParam(name="description") String description,
			@XmlElement(required=true) @WebParam(name="pieceCompta") String pieceCompta,
			@XmlElement(required=true) @WebParam(name="nomCompteCaisse") String nomCptCaisse,
			@XmlElement(required=true) @WebParam(name="numCptEp") String numCptEp
			);
}
