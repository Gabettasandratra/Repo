package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Grandlivre;


@WebService(name="comptabliteService", targetNamespace = "http://fidev.mg.comptabliteService", serviceName = "comptabliteService"
,portName ="comptabliteServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ComptabiliteService {

	/***
	 * METHODE POUR AFFICHER LE GRAND LIVRE
	 * ***/
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> afficheGrandLivre(
			@WebParam(name = "nomUtilisateur") @XmlElement(required=true,nillable=false)String nomUtilisateur,
			@WebParam(name = "dateDeb") @XmlElement(required=true,nillable=false)String dateDeb,
			@WebParam(name = "dateFin") @XmlElement(required=false,nillable=true)String dateFin);
	
}
