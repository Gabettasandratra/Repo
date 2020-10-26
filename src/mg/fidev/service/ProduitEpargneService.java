package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.CalendrierPep;
import mg.fidev.model.CalendrierPepView;
import mg.fidev.model.CatEpargne;
import mg.fidev.model.CompteDAT;
import mg.fidev.model.CompteDATSupp;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.CompteEpargneSupp;
import mg.fidev.model.CompteFerme;
import mg.fidev.model.ComptePep;
import mg.fidev.model.ComptePepSupp;
import mg.fidev.model.ConfigGLDAT;
import mg.fidev.model.ConfigGLPEP;
import mg.fidev.model.ConfigGeneralDAT;
import mg.fidev.model.ConfigGeneralPEP;
import mg.fidev.model.ConfigGlEpargne;
import mg.fidev.model.ConfigInteretProdEp;
import mg.fidev.model.ConfigProdEp;
import mg.fidev.model.InteretEpargne;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.TransactionEpargneSupp;
import mg.fidev.model.TransactionPep;
import mg.fidev.model.TypeEpargne;
import mg.fidev.utils.epargne.CalculDAT;
import mg.fidev.utils.epargne.FicheCaisseEpargne;
import mg.fidev.utils.epargne.SoldeEpargne;

@WebService(name = "epargneService", targetNamespace = "http://fidev.com/epargneService", serviceName = "epargneService", portName = "epargneServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ProduitEpargneService {

	/**
	 * enregistrer produit épargne
	 * **/
	@WebMethod
	@WebResult(name = "validation") 
	public boolean saveProduit(
			@WebParam(name = "nomProd") @XmlElement(required=true,nillable=false) String nomProdEp,
			@WebParam(name = "typeEpargne") @XmlElement(required=true,nillable=false) String nomTypeEp,
			@WebParam(name = "isActive") @XmlElement(required=true,nillable=false) boolean isActive);
	
	/**
	 * chercher tout produit
	 * ***/
	@WebMethod
	@WebResult(name="liste")
	public List<ProduitEpargne> findAllProduit(
			@WebParam(name = "type") @XmlElement(required=true,nillable=false) String type);
	
	/**
	 * Chercher un produit épargne par son indentifiant
	 * **/
	@WebMethod
	@WebResult(name="resultat")
	public ProduitEpargne findProduitById(@WebParam(name = "idProd") @XmlElement(required=true,nillable=false) String idProd);
	
	/**
	 * désactiver un produit épargne
	 * **/
	@WebMethod
	@WebResult(name = "validation")
	public boolean desactiverProduit(@WebParam(name = "idProd") String idProd);
	
	//Activer produit épargne
	@WebMethod
	@WebResult(name = "validation")
	public boolean activerProduit(@WebParam(name = "idProd") String idProd);
	
	//Supprimer produit épargne
	@WebMethod
	@WebResult(name = "validation")
	public boolean supprimerProduitEpargne(@WebParam(name = "idProd") String idProd);
	
	/***
	 * modification d'un produit épargne
	 * ***/
	@WebMethod
	@WebResult(name = "validation")
	public boolean modifierProduit(
			@WebParam(name = "idProd")String idProd,
			@WebParam(name = "nomProd")String nomProd, 
			@WebParam(name = "isActive")boolean isActive);
	
	//--------------------------------------------------------------------------------------------------
	/********************************* Configuration produits épargne *********************************/
	/**
	 * configuration intérêt d'épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean saveConfigIntProduit(
			@WebParam(name = "configInteret") @XmlElement(required=false,nillable=true) ConfigInteretProdEp confIntEp,
			@WebParam(name = "idproduit") @XmlElement(required=true,nillable=false) String idProduit);
	
	/***
	 * Configuration générale d'un produit épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean saveConfigProduit(
			@WebParam(name = "configProduit") @XmlElement(required=false,nillable=true) ConfigProdEp confEp,
			@WebParam(name = "idproduit") @XmlElement(required=true,nillable=false) String idProduit);
	
	/***
	 * Configuration grand livre épargne
	 * ***/
	@WebMethod
	@WebResult(name = "validation")
	public boolean configGLepargne(
			@WebParam(name= "configGlEpargne") @XmlElement(required=true,nillable=false) ConfigGlEpargne configGlEpargne,
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit
			);
	
	//Config GL dépôt à terme
	@WebMethod
	@WebResult(name = "validation")
	public boolean configGlDatEpargne(
	@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit,
	@WebParam(name= "configGl") @XmlElement(required=true,nillable=false) ConfigGLDAT confDAT);
	
	//Config Général dépôt à terme
	@WebMethod
	@WebResult(name = "validation")
	public boolean configGeneralDatEpargne(
	@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit,
	@WebParam(name= "configGeneral") @XmlElement(required=true,nillable=false) ConfigGeneralDAT confGen);
	
	
	//Config Général plan d'épargne
	@WebMethod
	@WebResult(name = "validation")
	public boolean saveConfigGeneralPEP(
			@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit,
			@WebParam(name= "configGeneral") @XmlElement(required=true,nillable=false) ConfigGeneralPEP confGen);
	
	//Config GL dépôt à terme
	@WebMethod
	@WebResult(name = "validation")
	public boolean saveConfigGlPEP(
	@WebParam(name= "idProduit") @XmlElement(required=true,nillable=false) String idProduit,
	@WebParam(name= "configGl") @XmlElement(required=true,nillable=false) ConfigGLPEP confPEP);
	
	
	//--------------------------------------------------------------------------------------------------
		/********************************* Intérêt épargne épargne *********************************/
	
	/***
	 * Calcul intérêt
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<InteretEpargne> calcInteret(
			@WebParam(name ="dateDeb") @XmlElement(required=true)String date1);
	
	//--------------------------------------------------------------------------------------------------
		/********************************* Type et categorie produits épargne *********************************/
	
	/***
	 * SELECTION DE TYPE D'EPARGNE
	 * ***/
	@WebMethod
	@WebResult(name="listTypeEpargne")
	public List<TypeEpargne> getTypeEpargne();
	
	/***
	 * SELECTION CATEGORIE D'EPARGNE
	 * ***/
	@WebMethod
	@WebResult(name="categorieEp")
	public List<CatEpargne> getCategorieEp();
	
	//--------------------------------------------------------------------------------------------------
		/********************************* Compte épargne *********************************/
	
	/***
	 * OUVRIR COMPTE EPARGNE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public CompteEpargne ouvrirCompte(
			@XmlElement(required=true) @WebParam(name="dateCree") String dateOuverture,
			@XmlElement(required=false,nillable=true) @WebParam(name="pasRetrait") boolean pasRetrait,
			@XmlElement(required=false,nillable=true) @WebParam(name="prioritaire") boolean prioritaire,
			@XmlElement(required=true) @WebParam(name="idProduitEp") String idProduitEp,
			@XmlElement(required=true) @WebParam(name="codeInd") String individuelId,
			@XmlElement(required=true) @WebParam(name="codeGrp") String groupeId,
			@XmlElement(required=true) @WebParam(name="userId") int userId
			);	

	/***
	 * CHERCHER COMPTE ACTIF PAR NUMERO DE COMPTE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findCompteByCode(@WebParam(name ="numCmpt") @XmlElement(required=true) String numCmpt);
	
	/***
	 * CHERCHER COMPTE INACTIF PAR NUMERO DE COMPTE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findCompteInactifByCode(@WebParam(name ="numCmpt") @XmlElement(required=true) String numCmpt);
	
	
	//CHERCHER COMPTE PAR CODE CLIENT ET PRODUIT
	@WebMethod
	@WebResult(name="resultat")
	public CompteEpargne findCompte(
			@XmlElement(required=true) @WebParam(name="idProduitEp")String idProduit,
			@XmlElement(required=false) @WebParam(name="codeInd")String codeInd,
			@XmlElement(required=false) @WebParam(name="codeGrp")String codeGrp);
	

	/****************************Modification compte*****************************************************/
	//Chercher compte par numéro compte
	@WebMethod
	@WebResult(name="resultat")
	public CompteEpargne findUniqueCompte(
			@XmlElement(required=true, nillable=false) @WebParam(name="numCompte")String numCompte);
	
	//Enregistremeent modification compte épargne
	@WebMethod
	@WebResult(name="validation")
	public boolean updateCompte(
			@XmlElement(required=true, nillable=false) @WebParam(name="numCompte")String numCompte,
			@XmlElement(required=true, nillable=false) @WebParam(name="date")String date);
	
	/***
	 * Fermer compte épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean fermerCompte(
			@XmlElement(required=false,nillable=true) @WebParam(name="comptFerm")CompteFerme compFerme, 
			@XmlElement(required=true,nillable=false) @WebParam(name="numCmpt")String numCmpt);
	
	
	//Supprimer compte épargne 
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteCompte(
			@XmlElement(required=true,nillable=false) @WebParam(name="numCmpt")String numCmpt,
			@XmlElement(required=true,nillable=false) @WebParam(name="user")int user
			);
	
	//Liste compte épargne supprimé
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargneSupp> getCompteSupprimer();
	
	//--------------------------------------------------------------------------------------------------
		/********************************* Transactions épargne *********************************/
	/***
	 * Transaction épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public String trasactionEpargne(
			@XmlElement(required=true) @WebParam(name="type") String typeTransac,
			@XmlElement(required=true) @WebParam(name="date") String dateTransac,
			@XmlElement(required=true) @WebParam(name="montant") double montant,
			@XmlElement(required=true) @WebParam(name="description") String description,
			@XmlElement(required=true) @WebParam(name="piece") String pieceCompta,
			@XmlElement(required=true) @WebParam(name="typPaie")String typPaie,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
			@XmlElement(required=false) @WebParam(name="CompteCaisse") String nomCptCaisse,
			@XmlElement(required=true) @WebParam(name="numCompte") String numCptEp,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser
			);
	
		/****************************Modification Transaction*****************************************************/
		//Recupère detail transaction
		@WebMethod
		@WebResult(name="resultat")
		public TransactionEpargne getDetailTrans(
				@XmlElement(required=true, nillable=false) @WebParam(name="codeTrans")String codeTrans);
		
		//Enregistrement modification transaction
		@WebMethod
		@WebResult(name="resultat")
		public boolean updateTransaction(
			@XmlElement(required=true) @WebParam(name="codeTrans") String codeTrans,
			@XmlElement(required=true) @WebParam(name="typeTrans") String typeTrans,
			@XmlElement(required=true) @WebParam(name="dateTrans") String dateTrans,
			@XmlElement(required=true) @WebParam(name="montant") double montant,
			@XmlElement(required=true) @WebParam(name="description") String description,
			@XmlElement(required=true) @WebParam(name="piece") String pieceCompta,
			@XmlElement(required=true) @WebParam(name="typPaie")String typPaie,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
			@XmlElement(required=false) @WebParam(name="CompteCaisse") String nomCptCaisse,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser);
			
		//Suppression transaction
		@WebMethod
		@WebResult(name="resultat")
		public boolean deleteTransaction(
				@XmlElement(required=true) @WebParam(name="codeTrans") String codeTrans,
				@XmlElement(required=true) @WebParam(name="utilisateur") int idUser);	
		
		//Liste Transactions supprimés
		@WebMethod
		@WebResult(name="resultat")
		public List<TransactionEpargneSupp> getTransactionSupprimer(
				@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
				@XmlElement(required=false,nillable=true) @WebParam(name="produit")String produit,
				@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
				@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin);
		
		//------------------------------------------------------------------------------------------------------	
		/********************************* Transfert solde épargne *********************************/
	/***
	 * Virement
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public boolean virement(
			@XmlElement(required=true) @WebParam(name="comptRetirer")String cmpt1,
			@XmlElement(required=true) @WebParam(name="comptDepot")String cmpt2,
			@XmlElement(required=true) @WebParam(name="montant")double montant,
			@XmlElement(required=true) @WebParam(name="piece")String piece,
			@XmlElement(required=true) @WebParam(name="date")String date,
			@XmlElement(required=true) @WebParam(name="typePaie")String typPaie,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel,
			@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
			@XmlElement(required=false) @WebParam(name="comptCaisse")String caisse,
			@XmlElement(required=true) @WebParam(name="utilisateur")int user);
	
	//-----------------------------------------------------------------------------------------------------
	
	/***************************** Rapport sur épargne **************************/
	
	
	//Chercher compte épargne par code client et type d'épargne
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findCompteEpByType(
			@XmlElement(required=true,nillable=false) @WebParam(name="typeEp")String typeEp,
			@XmlElement(required=false,nillable=true) @WebParam(name="codeInd")String codeInd,
			@XmlElement(required=false,nillable=true) @WebParam(name="codeGrp")String codeGrp
			);
	
	
	//Recuperer tous les comptes inactif
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> getCompteEpargne(
			@XmlElement(required=true,nillable=false) @WebParam(name="etat")boolean etat);
	
	/***
	 * RAPPORT TRANSACTION
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> rapportTransactions(			
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
			@XmlElement(required=false,nillable=true) @WebParam(name="produit")String produit,
			@XmlElement(required=false,nillable=true) @WebParam(name="trans")String trans,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin);
	
	/***
	 * CHERCHER COMPTE PAR CODE CLIENT
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findByCodeCli(
			@XmlElement(required=false,nillable=true) @WebParam(name="codeInd")String codeInd,
			@XmlElement(required=false,nillable=true) @WebParam(name="codeGrp")String codeGrp);
	
	/***
	 * Rapport épargne par produit
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> rapportParProduit(
			@XmlElement(required=true,nillable=false) @WebParam(name="idProd")String idProd);
	
	/***
	 * RAPPORT SOLDE D'EPARGNE
	 * ***/

	@WebMethod  
    @WebResult(name="resultat")
	public List<SoldeEpargne> getSoldeEpargne(
	@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
	@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
	@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
	@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String idProd,
	@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb
	);
	
	//Rapport solde minimum maximum	
	@WebMethod
    @WebResult(name="resultat")
	public List<SoldeEpargne> getSoldeMinMax(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
			@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String idProd,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin);
	
	/***
	 * Rapport intérêt
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<InteretEpargne> rapportInteret(
	@WebParam(name ="idProd") @XmlElement(required=true)String idProd,
	@WebParam(name ="dateDeb") @XmlElement(required=true)String date1,
	@WebParam(name ="dateFin") @XmlElement(required=true)String date2
	);
	
	/***
	 * Relevé de compte
	 * ****/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getReleverCompte(
	@WebParam(name ="numCompte") @XmlElement(required=true)String numCompte,
	@WebParam(name ="dateDeb") @XmlElement(required=true)String dateDeb,
	@WebParam(name ="dateFin") @XmlElement(required=true)String dateFin
	);
	
	
	/***
	 * Demande relevé de compte
	 * ****/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getDemandeReleverCompte(
			@WebParam(name ="numCompte") @XmlElement(required=true)String numCompte,
			@WebParam(name ="dateDeb") @XmlElement(required=true)String dateDeb,
			@WebParam(name ="dateFin") @XmlElement(required=true)String dateFin,
			@WebParam(name ="dateDem") @XmlElement(required=true)String dateDem,
			@XmlElement(required=false) @WebParam(name="frais")double frais,
			@XmlElement(required=false) @WebParam(name="user")int user,
			@XmlElement(required=true) @WebParam(name="piece") String pieceCompta,
			@XmlElement(required=true) @WebParam(name="typPaie")String typPaie,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="compteCaisse")String compteCaisse
			);

	/***
	 * Rapport nouveau compte épargne
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> rapportNouveauCompte(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
			@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String produit,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin
	);
	
	/***
	 * Rapport compte épargne fermer
	 * ***/	
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteFerme> getCompteFermer(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
			@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String produit,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin
	);
	
	/***
	 * Rapport fiche journalière épargne
	 * ****/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getFicheJournalier(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="type")String type,
			@WebParam(name="date") @XmlElement(required=true)String date);
	
	/**
	 * Rapport etat des comptes épargne
	 * **/
	@WebMethod
	@WebResult(name="resultat")
	public List<String> getEtatCompte(
			@WebParam(name="dateDeb") @XmlElement(required=true)String dateDeb,
			@WebParam(name="dateFin") @XmlElement(required=true)String dateFin,
			@WebParam(name="cat1") @XmlElement(required=true)int cat1,
			@WebParam(name="cat2") @XmlElement(required=true)int cat2,
			@WebParam(name="cat3") @XmlElement(required=true)int cat3
			);
	
	//Rapport fiche caisse d'epargne
	@WebMethod
	@WebResult(name="resultat")
	public List<FicheCaisseEpargne> getFicheCaisse(
	@WebParam(name="date") @XmlElement(required=true)String date); 
	
	//Get all Transaction
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getAllTransaction();
	
	
	//------------------------------------------------------------------------------------------------
	//Ouvrir dépôt à terme
	@WebMethod
	@WebResult(name="resultat")
	public String ouvrirDAT(
	@XmlElement(required=true) @WebParam(name="idProduitEp") String idProduitEp,
	@XmlElement(required=false) @WebParam(name="codeInd") String individuelId,
	@XmlElement(required=false) @WebParam(name="codeGrp") String groupeId,
	@XmlElement(required=true) @WebParam(name="userId") int userId,
	@XmlElement(required=false) @WebParam(name="compte") CompteDAT compte,
	@XmlElement(required=true) @WebParam(name="piece")String piece,
	@XmlElement(required=true) @WebParam(name="typePaie")String typePaie,
	@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
	@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
	@XmlElement(required=false) @WebParam(name="CompteCaisse") String nomCptCaisse,
	@XmlElement(required=false) @WebParam(name="numCompte") String numCptEp);
	
	//Retrait DAT
	@WebMethod
	@WebResult(name="resultat")
	public String retaraitDAT(
	@XmlElement(required=false) @WebParam(name="compte") String compte,
	@XmlElement(required=false) @WebParam(name="date") String dateRetrait,
	@XmlElement(required=true) @WebParam(name="userId") int userId,
	@XmlElement(required=true) @WebParam(name="piece")String piece,
	@XmlElement(required=true) @WebParam(name="typePaie")String typePaie,
	@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
	@XmlElement(required=false) @WebParam(name="numCheq")String numCheq,
	@XmlElement(required=false) @WebParam(name="CompteCaisse") String nomCptCaisse,
	@XmlElement(required=false) @WebParam(name="numCompte") String numCptEp);
	
	//Retrait automatique sur DAT
	@WebMethod
	@WebResult(name="resultat")   
	public List<CompteDAT> retraitDATAuto(
			@XmlElement(required=false) @WebParam(name="date") String dateRetrait,
			@XmlElement(required=true) @WebParam(name="userId") int userId
			);
	
	//Annuler dépôt à terme
	@WebMethod
	@WebResult(name="validation")
	public boolean annulerDAT(
	@XmlElement(required=false) @WebParam(name="compte") String compte,
	@XmlElement(required=false) @WebParam(name="raison") String raison);
	
	//Supprimer Compte DAT 
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteCompteDAT(
			@XmlElement(required=false) @WebParam(name="numCompte") String compte,
			@XmlElement(required=false) @WebParam(name="user") int user);
	
	//Liste comptes DAT supprimés
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteDATSupp> getCompteDATSupprimer();
	
	//----------------------------------------------------------------------------------------------
	//Rapport dépôt à terme
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteDAT> rapportDAT(
	@XmlElement(required=false) @WebParam(name="dateDeb") String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin") String dateFin,
	@XmlElement(required=false) @WebParam(name="payer") boolean payer,
	@XmlElement(required=false) @WebParam(name="fermer") boolean fermer);
	
	//Rapport échéance dépôt à terme
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteDAT> rapportEcheanceDAT(
	@XmlElement(required=false) @WebParam(name="dateDeb") String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin") String dateFin);
	
	
	//Chercher compte DAT
	@WebMethod
	@WebResult(name="resultat")
	public CompteDAT getCompteDAT(
	@XmlElement(required=false) @WebParam(name="compte") String compte,
	@XmlElement(required=false) @WebParam(name="payer") boolean payer,
	@XmlElement(required=false) @WebParam(name="fermer") boolean fermer);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteDAT> findCompteDAT(
	@XmlElement(required=false) @WebParam(name="compte") String compte,
	@XmlElement(required=false) @WebParam(name="payer") boolean payer,
	@XmlElement(required=false) @WebParam(name="fermer") boolean fermer);
	
	
	//-------------------------------------------------------------------------------------------
	
	//Pour faire selection du produit de DAT ou DAV
	@WebMethod
	@WebResult(name="resultat")
	public List<ProduitEpargne> getProduit(
			@XmlElement(required=false) @WebParam(name="debut") String debut,
			@XmlElement(required=false) @WebParam(name="val") int val);
	 
	//Pour connaitre la date d'échéanche
	@WebMethod
	@WebResult(name="resultat")
	public String getDateEcheanche(
			@XmlElement(required=false) @WebParam(name="date") String date,
			@XmlElement(required=false) @WebParam(name="periode") int periode);
	
	//Calcul montant retrait DAT
	@WebMethod
	@WebResult(name="resultat")
	public CalculDAT calculMontantDAT(
	@XmlElement(required=false) @WebParam(name="date")String date,
	@XmlElement(required=false) @WebParam(name="compte")String numCompte);
	
	//Mettre un compte en inactif
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> desactiverCompte(
			@XmlElement(required=false) @WebParam(name="date")String date);
	
	//Activer un compte
	@WebMethod
	@WebResult(name="validation")
	public boolean activerCompte(
			@XmlElement(required=false) @WebParam(name="compte")String numCompte,
			@XmlElement(required=false) @WebParam(name="date")String date,
			@XmlElement(required=false) @WebParam(name="frais")double frais,
			@XmlElement(required=false) @WebParam(name="user")int user,
			@XmlElement(required=true) @WebParam(name="piece") String pieceCompta,
			@XmlElement(required=true) @WebParam(name="typPaie")String typPaie,
			@XmlElement(required=false) @WebParam(name="numTel")String numTel, 
			@XmlElement(required=false) @WebParam(name="compteCaisse")String compteCaisse);
	
	//------------------------------------------------------------------------------------------
	/*******************************Plan d'épargne ********************************************/
	
	//Compte Plan d'épargne 
	@WebMethod
	@WebResult(name="resultat")
	public String ouvrirComptePep(
			@XmlElement(required=false) @WebParam(name="compte")ComptePep compte,
			@XmlElement(required=true) @WebParam(name="idProduit") String idProduit,
			@XmlElement(required=true) @WebParam(name="codeInd") String codeInd,
			@XmlElement(required=true) @WebParam(name="codeGrp") String codeGrp,
			@XmlElement(required=true) @WebParam(name="userId") int userId,
			
			@XmlElement(required=true) @WebParam(name="transaction") String trans,
			@XmlElement(required=true) @WebParam(name="piece") String piece,
			@XmlElement(required=true) @WebParam(name="typePaie") String typePaie,
			@XmlElement(required=false) @WebParam(name="numTel") String numTel,
			@XmlElement(required=false) @WebParam(name="numCheq") String numCheq,
			@XmlElement(required=false) @WebParam(name="CompteCaisse") String CompteCaisse,
			@XmlElement(required=true) @WebParam(name="montant") double montant
			);
	
	//Modifier compte plan d'épargne 
	@WebMethod
	@WebResult(name="validation")
	public boolean modifierComptePep(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte,
			@XmlElement(required=false) @WebParam(name="compte")ComptePep compte,
			@XmlElement(required=false) @WebParam(name="userId")int userId
			);

	//Chercher unique compte pep
	@WebMethod
	@WebResult(name="resultat")
	public ComptePep getUniqueComptePep(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte);
	
	//Supprimer compte pep
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteComptePep(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte,
			@XmlElement(required=false) @WebParam(name="userId")int userId);
	
	//Chercher compte Pep par mot clé
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePep> findComptePep(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte);
	
	//Liste des comptes Pep  
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePep> getAllComptePep();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePepSupp> getComptePepSupp();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePep> getComptePepByCli(
			@XmlElement(required=false) @WebParam(name="codeInd") String codeInd,
			@XmlElement(required=false) @WebParam(name="codeGrp") String codeGrp);
	
	//-----------------------------------------------------------------------------------------------
	//Transaction dépôt sur pep
	@WebMethod
	@WebResult(name="validation")
	public boolean saveTransPep(
			@XmlElement(required=false) @WebParam(name="trans") TransactionPep trans,
			@XmlElement(required=true) @WebParam(name="numCompte") String numCptEp,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser,
			@XmlElement(required=false) @WebParam(name="numTel") String numTel,
			@XmlElement(required=false) @WebParam(name="numCheq") String numCheq,
			@XmlElement(required=false) @WebParam(name="CompteCaisse") String CompteCaisse
			);
	
	//Transaction retrait sur pep
	@WebMethod
	@WebResult(name="validation")
	public boolean saveRetraitPep(
			@XmlElement(required=false) @WebParam(name="trans") TransactionPep trans,
			@XmlElement(required=true) @WebParam(name="numCompte") String numCptEp,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser,
			@XmlElement(required=false) @WebParam(name="numTel") String numTel,
			@XmlElement(required=false) @WebParam(name="numCheq") String numCheq,
			@XmlElement(required=false) @WebParam(name="CompteCaisse") String CompteCaisse
			);
	
	@WebMethod
	@WebResult(name="resultat")
	public TransactionPep getUniqueTransPep(
			@XmlElement(required=true) @WebParam(name="idTrans") String idTrans);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionPep> getAllTransPep();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionPep> getTransPepByCompte(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean updateTransPep(
			@XmlElement(required=true) @WebParam(name="idTrans") String idTrans,
			@XmlElement(required=false) @WebParam(name="trans") TransactionPep trans,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteTransPep(
			@XmlElement(required=true) @WebParam(name="idTrans") String idTrans,
			@XmlElement(required=true) @WebParam(name="utilisateur") int idUser);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean verifierExecution(
			@XmlElement(required=true) @WebParam(name="date") String date);
	
	//Calendrier prévue plan d'épargne
	@WebMethod
	@WebResult(name="resultat")
	public List<CalendrierPepView> getCalendrierViewPep(
			@XmlElement(required=true) @WebParam(name="produit") String produit,
			@XmlElement(required=false) @WebParam(name="codeInd") String codInd,
			@XmlElement(required=false) @WebParam(name="codeGrp") String codGroupe,
			@XmlElement(required=true) @WebParam(name="dateOverture") String dateOverture,
			@XmlElement(required=true) @WebParam(name="duree") int duree,
			@XmlElement(required=true) @WebParam(name="frequence") String frequence,
			@XmlElement(required=true) @WebParam(name="montant") double montant,
			@XmlElement(required=true) @WebParam(name="taux") double taux);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean viderCalendrierPepView();
	
	//Chercher compte plan d'épargne par client
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePep> findComptePepByCli(
			@XmlElement(required=false) @WebParam(name="codeInd") String codInd,
			@XmlElement(required=false) @WebParam(name="codeGrp") String codGroupe);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<CalendrierPep> getCalPepByCompte(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte);
	
	@WebMethod
	@WebResult(name="resultat")
	public long calculDate(
			@XmlElement(required=false) @WebParam(name="idCompte")String idCompte,
			@XmlElement(required=false) @WebParam(name="date")String date);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<ComptePep> rapportNouveauPEP(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String produit,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionPep> rapportTransactionPEP(
			@XmlElement(required=false,nillable=true) @WebParam(name="agence")String agence,
			@XmlElement(required=false,nillable=true) @WebParam(name="client")String client,
			@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String produit,
			@XmlElement(required=false,nillable=true) @WebParam(name="trans")String trans,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin);
	
	
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionPep> getReleverComptePEP(
			@XmlElement(required=true,nillable=false) @WebParam(name="numCompte")String numCompte);
	
}

