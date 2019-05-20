package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.xmlRequest.GroupeXml;
import mg.fidev.xmlRequest.IndividuelXml;

@WebService(name = "groupeService", targetNamespace = "http://individuel.fidev.com/", serviceName = "groupeService", portName = "groupeServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface GroupeService {
	@WebMethod
	@WebResult(name = "resultat")
	public boolean addMembreGroupe(
			@WebParam(name = "nomGroupe") String codeGroupe,
			@WebParam(name = "rowIndividuel") int rowIndividuel);
	@WebMethod
	@WebResult(name = "resultat")
	public List<GroupeXml> getAllGroupe();

	@WebMethod
	@WebResult(name = "resultat")
	public String saveGroupe(@WebParam(name = "groupe")@XmlElement(required = true) GroupeXml request);
	
	@WebMethod
	@WebResult(name = "resultat")
	public List<IndividuelXml> getListeMembreGroupe(@WebParam(name = "nomGroupe")@XmlElement(required = true) String nomGroupe);
	
	@WebMethod
	@WebResult(name = "resultat")
	public List<IndividuelXml> getListeNonMembre();
	
}
