package mg.fidev.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.CommissionCredit;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.GarantieCredit;


@WebService(name = "creditService", targetNamespace = "http://fidev.mg.creditService", serviceName = "creditService", portName = "creditServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED, style= Style.RPC)
public interface CreditService {
	@WebMethod
	@WebResult(name = "validation")
	public boolean saveProduit(
			@WebParam(name = "nomProd") @XmlElement(required=true,nillable=false) String nomProdCredit,
			@WebParam(name = "etat") @XmlElement(required=true,nillable=false) boolean etat);
	
	@WebMethod
	@WebResult(name = "validation")
	public void configGnrlCredit(
			@WebParam(name= "configGnrlCredit") @XmlElement(required=true,nillable=false) ConfigGeneralCredit configGenCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	@WebMethod
	@WebResult(name = "validation")
	public void configCreditInd(
			@WebParam(name= "configIndCredit") @XmlElement(required=true,nillable=false) ConfigCreditIndividuel configIndCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	@WebMethod
	@WebResult(name = "validation")
	public void demandeCredit(
			@WebParam(name="demandeCredit") @XmlElement(required=true,nillable=false) DemandeCredit dmd,
			@WebParam(name="agence") @XmlElement(required=true,nillable=false) String agence,
			@WebParam(name="idProduit") @XmlElement(required=true,nillable=false) String idProduit,
			@WebParam(name="codeInd") @XmlElement(required=true,nillable=false) String codeInd,
			@WebParam(name="codeGrp") @XmlElement(required=true,nillable=false) String codeGrp,
			@WebParam(name="garantie") @XmlElement(required=true,nillable=false) GarantieCredit gar,
			@WebParam(name="id_user") @XmlElement(required=true,nillable=false) int agenCredit_id
			//@WebParam(name="calPaiementDue") @XmlElement(required=true,nillable=false) Calpaiementdue cal
			);
	
	@WebMethod
	@WebResult(name = "validation")
	public void updateDemandeCredit(
			@WebParam(name = "num_credit") @XmlElement String numCredit,
			@WebParam(name = "demandeCredit") @XmlElement DemandeCredit dmd,
			@WebParam(name="id_user") @XmlElement(required=true,nillable=false) int agenCredit_id,
			@WebParam(name = "idProduit") @XmlElement String idProduit);
	
	@WebMethod
	@WebResult(name = "validation")
	public void paiementCommission(
			@WebParam(name="commission") @XmlElement CommissionCredit comm,
			@WebParam(name="numCredit") @XmlElement String numCredit,
			@WebParam(name="userId") @XmlElement int userId);
	
	@WebMethod
	@WebResult(name = "approbation")
	public void approbationCredit(
			@WebParam(name="num_credit") @XmlElement String numCredit,
			@WebParam(name="statuApp") @XmlElement String statuApp,
			@WebParam(name="dateApp") @XmlElement String dateApp,
			@WebParam(name="descApp") @XmlElement String descApp,
			@WebParam(name="montantApp") @XmlElement double montantApp);
}
