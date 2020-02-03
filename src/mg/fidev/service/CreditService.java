package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.CalView;
import mg.fidev.model.Calapresdebl;
import mg.fidev.model.Calpaiementdue;
import mg.fidev.model.CompteCaisse;
import mg.fidev.model.ConfigCredit;
import mg.fidev.model.ConfigCreditGroup;
import mg.fidev.model.ConfigCreditIndividuel;
import mg.fidev.model.ConfigFraisCredit;
import mg.fidev.model.ConfigFraisCreditGroupe;
import mg.fidev.model.ConfigGarantieCredit;
import mg.fidev.model.ConfigGeneralCredit;
import mg.fidev.model.ConfigGlCredit;
import mg.fidev.model.ConfigPenaliteCredit;
import mg.fidev.model.Decaissement;
import mg.fidev.model.DemandeCredit;
import mg.fidev.model.GarantieCredit;
import mg.fidev.model.ProduitCredit;
import mg.fidev.model.Remboursement;
import mg.fidev.utils.FicheCred;


@WebService(name = "creditProduitService", targetNamespace = "http://fidev.mg.creditProduitService", serviceName = "creditProduitService", portName = "creditServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)//style= Style.RPC
public interface CreditService {
	//style=Style.DOCUMENT,use=Use.LITERAL
	
	
	/****************************************************** CRUD SUR PRODUITS CREDIT *******************************************************************/
	
	/**
	 *  Methode pour l'enregistrement de credit 
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public ProduitCredit saveProduit_Credit(@WebParam(name = "nomProd") @XmlElement(required=true,nillable=false) String nomProdCredit,@WebParam(name = "etat") @XmlElement(required=true,nillable=false) boolean etat);
	
	@WebMethod
	@WebResult(name="voir_Demande")
	public List<DemandeCredit> findAllDemand();

	/** 
	 * Methode pour Lister les Produits Credits  
	 **/
	@WebMethod
	@WebResult(name="Listes_ProduitCredit")
	public List<ProduitCredit> findAllCredit();
	
	/***
	 * AFFICHE LES COMPTES CAISSES
	 * ***/
	
	@WebMethod
	@WebResult(name="Liste_CompteCaisse")
	public List<CompteCaisse> findAllComptCaisse();
	
	/**
	 * Methode pour Chercher un Produit Credit par la Clé primaire
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public ProduitCredit findOne(@WebParam(name="idProduitCredit") String id);
	
	
	/**
	 * Methode Pour Chercher Produit Par Mot Clé
	 **/	
	@WebMethod
	@WebResult(name = "validation")
	public List<ProduitCredit> findCreditByMc(@WebParam(name = "mc") String mc);
	
	
	/***
	 * CHERCHER DEMANDE CREDIT
	 * ***/
	@WebMethod
	@WebResult(name="Liste_demande")
	public List<DemandeCredit> findDemandeByMc(@WebParam(name = "mc") String mc,
			@WebParam(name = "mc2") @XmlElement(required=false,nillable=true) String m2);
	
	/**
	 * Methode pour Modifier un Produit Credit
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public ProduitCredit updateProduitCredit(@WebParam(name = "numCredit") @XmlElement(required=true,nillable=false) String numcredit,
			@WebParam(name = "produit") ProduitCredit p);
	
	/**
	 * Methode Pour Supprimer un Credit Produit
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public String deleteProduitCredit(@WebParam(name="idProduitCredit") String id);
	
	
	/***
	 * DEMANDE CREDIT
	 * ***/
	
	@WebMethod
	@WebResult(name="validation")
	public boolean demandePret(
			@WebParam(name="idProduit") @XmlElement(required=true,nillable=false) String idProduit, 
			@WebParam(name="codeInd") @XmlElement(required=false,nillable=true)  String codeInd, 
			@WebParam(name="codeGroupe") @XmlElement(required=false,nillable=true) String codeGroupe,
			@WebParam(name="dat_dem") @XmlElement(required=true,nillable=false) String date_dem, 
			@WebParam(name="montant") @XmlElement(required=true,nillable=false) double montant,
			@WebParam(name="nom_agent") @XmlElement(required=true,nillable=false) String agent,
			@WebParam(name="butEco") @XmlElement(required=false,nillable=true) String butEco,
			@WebParam(name="butSos") @XmlElement(required=false,nillable=true) String butSps,
			@WebParam(name="gar1") @XmlElement(required=false,nillable=true)GarantieCredit g1,
			@WebParam(name="gar2") @XmlElement(required=false,nillable=true)GarantieCredit g2,
			@WebParam(name="gar3") @XmlElement(required=false,nillable=true)GarantieCredit g3,
			@WebParam(name="codeGar1") @XmlElement(required=false,nillable=true)String codeGar1,
			@WebParam(name="codeGar2") @XmlElement(required=false,nillable=true)String codeGar2,
			@WebParam(name="codeGar3") @XmlElement(required=false,nillable=true)String codeGar3,
			@WebParam(name="user_id") @XmlElement(required=true,nillable=false) int user_id,
			@WebParam(name="totGarantie") @XmlElement(required=false,nillable=true)double totGarantie
			);
	

	@WebMethod
	@WebResult(name="result")
	public List<Calpaiementdue> insertDemande(
			@WebParam(name="idProduit") @XmlElement(required=true,nillable=false) String idProduit, 
			@WebParam(name="codeInd") @XmlElement(required=false,nillable=true)  String codeInd, 
			@WebParam(name="codeGroupe") @XmlElement(required=false,nillable=true) String codeGroupe,
			@WebParam(name="dat_dem") @XmlElement(required=true,nillable=false) String date_dem, 
			@WebParam(name="montant") @XmlElement(required=true,nillable=false) double montant,
			@WebParam(name="nom_agent") @XmlElement(required=true,nillable=false) String agent,
			@WebParam(name="but") @XmlElement(required=false,nillable=true) String but,
			@WebParam(name="user_id") @XmlElement(required=true,nillable=false) int user_id);
	
	//générer calendrier
	@WebMethod
	@WebResult(name="resultat")
	public List<CalView> demCredit(
			@WebParam(name="codeInd") @XmlElement(required=false,nillable=true)  String codeInd, 
			@WebParam(name="dat_dem") @XmlElement(required=true,nillable=false) String date_dem, 
			@WebParam(name="montant") @XmlElement(required=true,nillable=false) double montant,
			@WebParam(name="tauxInt") @XmlElement(required=true,nillable=false) double tauxInt,
			@WebParam(name="nbTranche") @XmlElement(required=false,nillable=true) int nbTranche,
			@WebParam(name="typeTranche") @XmlElement(required=false,nillable=true) String typeTranche,
			@WebParam(name="diffPaie") @XmlElement(required=false,nillable=true) int diffPaie,
			@WebParam(name="modCalcul") @XmlElement(required=false,nillable=true) String modCalcul
			);
	//modification calendrier
	@WebMethod
	@WebResult(name="validation")
	public boolean modifCalendrier(
	@WebParam(name="id") @XmlElement(required=false,nillable=true) int id,
	@WebParam(name="cals") @XmlElement(required=false,nillable=true) CalView cal);
	
	//supprimer données calendrier
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteCalendrier();
	
	//recupérer calendrier view
	@WebMethod
	@WebResult(name="resultat")
	public List<CalView> getAllCalView();
		
	/***
	 * COMMISSION CREDIT 
	 * ***/
	@WebMethod
	@WebResult(name = "validation")
	public boolean insertCommission(@WebParam(name="cash") @XmlElement(required=false,nillable=true)boolean cash,
			@WebParam(name="date") @XmlElement(required=false,nillable=true) String date,
			@WebParam(name="piece") @XmlElement(required=false,nillable=true)String piece,
			@WebParam(name="comm") @XmlElement(required=false,nillable=true)double commission,
			@WebParam(name="papeterie") @XmlElement(required=false,nillable=true)double papeterie,
			@WebParam(name="numCredit") @XmlElement(required=true,nillable=false) String numCredit,
			@WebParam(name="userId") @XmlElement(required=true,nillable=false) int userId,
			@WebParam(name="cptCaisse") @XmlElement(required=true,nillable=false) String nomCptCaisse);

	/***
	 * APPROBATION CREDIT
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public String saveApprobation(	@WebParam(name="num_credit") @XmlElement(required=true,nillable=false) String numCredit,
			@WebParam(name="approuv_par") @XmlElement(required=true,nillable=false) String Appby,
			@WebParam(name="dateApp") @XmlElement(required=true,nillable=false) String dateApp,
			@WebParam(name="descApp") @XmlElement String descApp,
			@WebParam(name="montantApp") @XmlElement(required=true,nillable=false) double montantApp,
			@WebParam(name="statut") @XmlElement(required=true,nillable=false) String stat_demande);
	
	/***
	 * DECAISSEMENT
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public String saveDecaisement(
			@WebParam(name="date") @XmlElement(required=true,nillable=false)String date,
			@WebParam(name="typePaie") @XmlElement(required=true,nillable=false)String typePaie, 
			@WebParam(name="montant") @XmlElement(required=true,nillable=false)double montant,
			@WebParam(name="commission") @XmlElement(required=true,nillable=false)double commission,
			@WebParam(name="papeterie") @XmlElement(required=true,nillable=false)double papeterie, 
			@WebParam(name="piece") @XmlElement(required=true,nillable=false)String piece,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
			@WebParam(name="comptCaisse") @XmlElement(required=false,nillable=true)String comptCaise,
			@WebParam(name="num_credit")String numCredit,
			@WebParam(name="id_utilisateur")int userId);

	/***
	 * REMBOURSEMENT
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public boolean saveRemboursement(
			@WebParam(name="numCredit") String numCredit,
			@WebParam(name="utilisateur") int userId ,
			@WebParam(name="date") @XmlElement(required=true,nillable=false)String date,
			@WebParam(name="montant") @XmlElement(required=true,nillable=false)double montant,
			@WebParam(name="piece") @XmlElement(required=true,nillable=false)String piece,
			@WebParam(name="typePaie") @XmlElement(required=true,nillable=false)String typePaie, 
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
			@WebParam(name="comptCaisse") @XmlElement(required=false,nillable=true)String cmptCaisse
			);
	
	
	@WebMethod
	@WebResult(name="resultat")
	public List<String> dernierRemboursement(@WebParam(name="numCredit")String numCredit);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Calapresdebl> historiqueCredit(@WebParam(name="numCredit")String numCredit);
	
	/***
	 * AFFICHE MONTANT A REMBOURSER
	 * ***/
	@WebMethod
	@WebResult(name="list")
	public List<String> getMontaRemb(@WebParam(name="numCredit") String numCredit
			,@WebParam(name="date") String date);
	
/*************************************************** CONFIGURATION CREDITS *********************************************************************/
	
	/***
	 * CONFIGURATION POUR TOUT CREDIT
	 * ***/
	
	@WebMethod
	@WebResult(name="validation")
	public void configToutCredit(@WebParam(name="configCredits") ConfigCredit configs);
	
	/**
	 *Methode pour la configuration Generale d'un credit 
	 **/
	@WebMethod
	@WebResult(name = "validation")
	public void configGnrlCredit(
			@WebParam(name= "configGnrlCredit") @XmlElement(required=false,nillable=true) ConfigGeneralCredit configGenCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	/** 
	 * Methode pour la Configuration d'un Credit pour un Client Individuel
	 * **/
	@WebMethod
	@WebResult(name = "validation")
	public boolean configCreditInd(
			@WebParam(name= "configIndCredit") @XmlElement(required=false,nillable=true) ConfigCreditIndividuel configIndCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	/***
	 * CONFIGURATION CREDIT GROUPE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public void configGroup(@WebParam(name="configCreditGroup") @XmlElement(required=false,nillable=true) ConfigCreditGroup confGroup,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit);
	
	/**
	 *CONFIGURATION FRAIS CREDIT INDIVIDUEL
	 * **/	
	@WebMethod
	@WebResult(name = "validation")
	public void configFraisCredits(
			@WebParam(name= "configFraisCredit") @XmlElement(required=false,nillable=true) ConfigFraisCredit configFraisCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	/***
	 * CONFIGURATION FRAIS CREDIT GROUPE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public void configFraisCreditGroupes(@WebParam(
			name= "confFraisGroupe") @XmlElement(required=false,nillable=true) ConfigFraisCreditGroupe confFraisGroupe,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idPr);
	
	/**
	 * Methode pour la Configuration d'une Garantie Credit
	 * **/
	@WebMethod
	@WebResult(name = "validation")
	public void configGarantiCredit(
			@WebParam(name= "configGarCredit") @XmlElement(required=false,nillable=true) ConfigGarantieCredit configGarCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	/**
	 * Methode pour la Configuration GL Credit
	 * **/
	@WebMethod
	@WebResult(name = "validation")
	public void configGLCredit(
			@WebParam(name= "configGlCredit") @XmlElement(required=false,nillable=true) ConfigGlCredit configGLCredit,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	/***
	 * CONFIG PENALITE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public void configPenalite(@WebParam(name="configPenalite") @XmlElement(required=false,nillable=true) ConfigPenaliteCredit confPen, 
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit);
	
	/**
	 * HISTORIQUE CREDIT
	 * **/
	@WebMethod
	@WebResult(name="resultat")
	public List<Remboursement> historiqueCreditTout(@WebParam(name="numCredit")String numCredit);
	
	//Recuperer l'information du client lors de demande crédit
	@WebMethod
	@WebResult(name="resultat")
	public String getInfoClient(
			@WebParam(name="code")@XmlElement(required=false,nillable=true)String code);
	
	//enregistrement calendrier prévu du remboursement
	@WebMethod
	@WebResult(name="resultat")
	public void saveCalendrier(
			@WebParam(name="calendrier")@XmlElement(required=false,nillable=true)List<Calpaiementdue> calendrier);
	
	//rapport fiche crédit
	@WebMethod
	@WebResult(name="resultat")
	public List<FicheCred> ficheCredit(
			@WebParam(name="numCred")@XmlElement(required=false,nillable=true)String numCred);
	
	//Get crédit par Identifiant
	@WebMethod
	@WebResult(name="resultat")
	public DemandeCredit getCreditById(
			@WebParam(name="numCred")@XmlElement(required=false,nillable=true)String numCred);
	
	//Chercher demande crédit entre deux date
	@WebMethod
	@WebResult(name="resultat")
	public List<DemandeCredit> getCreditByDate(
	@WebParam(name="dateDeb")@XmlElement(required=false,nillable=true)String dateDeb,
	@WebParam(name="dateFin")@XmlElement(required=false,nillable=true)String dateFin);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<DemandeCredit> getDecaissementAttente(
	@WebParam(name="dateDeb")@XmlElement(required=false,nillable=true)String dateDeb);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Decaissement> getDecaissement(
	@WebParam(name="dateDeb")@XmlElement(required=false,nillable=true)String dateDeb,
	@WebParam(name="dateFin")@XmlElement(required=false,nillable=true)String dateFin);
	
/*	@WebMethod
	@WebResult(name="historique_demande")
	public List<DemandeCredit> historiqueDemande();
	*/
	
}
