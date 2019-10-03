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
import mg.fidev.model.Decaissement;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.Remboursement;

@WebService(name="demandeService", targetNamespace = "http://fidev.mg.demandeCreditService", serviceName = "demandeService", portName = "demandeServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED, style= Style.RPC)//
public interface DemandeCreditService {
	
	/*******************************************************************************************************************************************/
									/******************** DEMANDE CREDIT *********************************/
	/*******************************************************************************************************************************************/
	
	/**
	 * Methode pour la Demande de Credit
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public void demandeCredit(
			@WebParam(name="date_demande") @XmlElement(required=true,nillable=false) String date,
			@WebParam(name="montant_demande") @XmlElement(required=true,nillable=false) double montant,
			@WebParam(name="but_credit") @XmlElement(required=true,nillable=false) String but,
			@WebParam(name="nom_agent") @XmlElement(required=true,nillable=false) String agent,
			@WebParam(name="idProduit") @XmlElement(required=true,nillable=false) String idProduit,
			@WebParam(name="codeInd") @XmlElement(required=true,nillable=false) String codeInd,
			@WebParam(name="codeGrp") @XmlElement(required=true,nillable=false) String codeGrp,
			@WebParam(name="id_user") @XmlElement(required=true,nillable=false) int userId
			);
	
	
	/**
	 * Methode pour Modifier un Demande Credit
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public String updateDemandeCredit(
			@WebParam(name = "num_credit") @XmlElement String numCredit,
			@WebParam(name = "demandeCredit") @XmlElement DemandeCredit dmd,
			@WebParam(name="idProduit") @XmlElement(required=true,nillable=false) String idProduit,
			@WebParam(name="id_user") @XmlElement(required=true,nillable=false) int userId);
	
	
	/************************************************************************************************************************************/
							/************************* COMMISSION CREDIT *************************************/
	/************************************************************************************************************************************/
	
	/**
	 * Methode pour le Paiement de Commission Credit
	 * **/
	@WebMethod
	@WebResult(name = "validation")
	public String paiementCommission(
			@WebParam(name="commission") @XmlElement CommissionCredit comm,
			@WebParam(name="numCredit") @XmlElement(required=true,nillable=false) String numCredit,
			@WebParam(name="userId") @XmlElement(required=true,nillable=false) int userId,
			@WebParam(name="cptCaisse") @XmlElement(required=true,nillable=false) String nomCptCaisse);
	
	/************************************************************************************************************************************/
									/********************* APPROBATION CREDIT *******************************/
	/************************************************************************************************************************************/
	
	/**
	 * Methode pour l'approbation d'un Credit
	 * **/
	@WebMethod
	@WebResult(name = "approbation")
	public String approbationCredit(
			@WebParam(name="num_credit") @XmlElement(required=true,nillable=false) String numCredit,
			@WebParam(name="approuv_par") @XmlElement(required=true,nillable=false) String Appby,
			@WebParam(name="dateApp") @XmlElement(required=true,nillable=false) String dateApp,
			@WebParam(name="descApp") @XmlElement String descApp,
			@WebParam(name="montantApp") @XmlElement(required=true,nillable=false) double montantApp,
			@WebParam(name="statut") @XmlElement(required=true,nillable=false) String stat_demande
			);
	
	/************************************************************************************************************************************/
									/************************ DECAISSEMENT ********************************/
	/************************************************************************************************************************************/
	
	
	/**
	 * Methode pour le decaissement d'un demande Crédit
	 * **/
	@WebMethod
	@WebResult(name="Decaissement_credit")
	public String  decaisser(@WebParam(name="decaisment")Decaissement decaissement,
			@WebParam(name="num_credit")String numCredit,
			@WebParam(name="id_utilisateur")int userId);
	
	/************************************************************************************************************************************/
									/********************** REMBOURSEMENT *********************************/
	/************************************************************************************************************************************/
	
	@WebMethod
	@WebResult(name="Remboursement_Credit")
	public String rembourser(@WebParam(name="numCredit") String numCredit,@WebParam(name="utilisateur") int userId ,
			@WebParam(name="renseignement") Remboursement remb);

}






