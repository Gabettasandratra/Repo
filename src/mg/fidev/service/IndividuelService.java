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

import mg.fidev.model.Adresse;
import mg.fidev.model.Docidentite;
import mg.fidev.model.Individuel;
import mg.fidev.xmlRequest.IndividuelXml;

@WebService(name = "individuelService", targetNamespace = "http://individuel.fidev.com/", serviceName = "individuelService", portName = "individuelServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface IndividuelService {
	@WebMethod
	@WebResult(name = "resultat")
	public String saveIndividuel(@WebParam(name = "individuel") @XmlElement(required = true) IndividuelXml request,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence);
	
	@WebMethod
	@WebResult(name = "resultat")
	public String insertIndividuel(@WebParam(name = "individuel") @XmlElement(required = true) Individuel individuel,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence,
			@WebParam(name = "docIdentite") @XmlElement(required = true) Docidentite docIdentite,
			@WebParam(name = "adresse") @XmlElement(required = true) Adresse adresse);
	
	@WebMethod
	@WebResult(name = "resultat")
	public List<IndividuelXml> getAllIndividuel();
	
	@WebMethod
	@WebResult(name = "resultat")
	public List<IndividuelXml> getAllIndividuelByDate(
			@WebParam(name = "startDate") @XmlElement(required = true)Date startDate, 
			@WebParam(name = "endDate") @XmlElement(required = true) Date endDate);

	@WebMethod
	@WebResult(name = "resultat")
	public boolean testMoxy(@WebParam(name = "individuel") Individuel individu);
	
	@WebMethod
	@WebResult(name = "resultat")
	public String saveIndividuelByEntity(@WebParam(name = "individuel")@XmlElement(required = true) Individuel request);
	
}
