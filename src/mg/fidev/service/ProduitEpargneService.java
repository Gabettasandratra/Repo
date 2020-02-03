package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.CatEpargne;
import mg.fidev.model.CompteDAT;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.CompteFerme;
import mg.fidev.model.ConfigGLDAT;
import mg.fidev.model.ConfigGeneralDAT;
import mg.fidev.model.ConfigGlEpargne;
import mg.fidev.model.ConfigInteretProdEp;
import mg.fidev.model.ConfigProdEp;
import mg.fidev.model.InteretEpargne;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.TypeEpargne;
import mg.fidev.utils.CalculDAT;
import mg.fidev.utils.FicheCaisseEpargne;
import mg.fidev.utils.SoldeEpargne;

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
	public List<ProduitEpargne> findAllProduit();
	
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
	
	/***
	 * modification d'un produit épargne
	 * ***/
	@WebMethod
	@WebResult(name = "validation")
	public boolean modifierProduit(
			@WebParam(name = "idProd")String idProd,
			@WebParam(name = "nomProd")String nomProd, 
			@WebParam(name = "isActive")boolean isActive);
	
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
	
	/***
	 * Calcul intérêt
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public List<InteretEpargne> calcInteret(
			@WebParam(name ="idProd") @XmlElement(required=true)String idProd,
			@WebParam(name ="dateDeb") @XmlElement(required=true)String date1,
			@WebParam(name ="dateFin") @XmlElement(required=true)String date2);
	
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
	
	/***
	 * CHERCHER COMPTE PAR NUMERO DE COMPTE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findCompteByCode(@WebParam(name ="numCmpt") @XmlElement(required=true) String numCmpt);
	
	
	/***
	 * OUVRIR COMPTE EPARGNE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public CompteEpargne ouvrirCompte(
			@XmlElement(required=true) @WebParam(name="dateCree") String dateOuverture,
			@XmlElement(required=true,nillable=false) @WebParam(name="cmptGeler") boolean geler,
			@XmlElement(required=false,nillable=true) @WebParam(name="pasRetrait") boolean pasRetrait,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateRetirer") String dateRetirer,
			@XmlElement(required=true) @WebParam(name="idProduitEp") String idProduitEp,
			@XmlElement(required=true) @WebParam(name="codeInd") String individuelId,
			@XmlElement(required=true) @WebParam(name="codeGrp") String groupeId,
			@XmlElement(required=true) @WebParam(name="userId") int userId
		);
	
	//CHERCHER COMPTE PAR CODE CLIENT ET PRODUIT
	
	@WebMethod
	@WebResult(name="resultat")
	public CompteEpargne findCompte(
			@XmlElement(required=true) @WebParam(name="idProduitEp")String idProduit,
			@XmlElement(required=false) @WebParam(name="codeInd")String codeInd,
			@XmlElement(required=false) @WebParam(name="codeGrp")String codeGrp);
	
	/***
	 * Transaction épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean trasactionEpargne(
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
	
	/***
	 * Virement
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public String virement(
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
	
	/***
	 * RAPPORT TRANSACTION
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> rapportTransactions(
			@XmlElement(required=false,nillable=true) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false,nillable=true) @WebParam(name="dateFin")String dateFin
			);
	
	/***
	 * CHERCHER COMPTE PAR CODE CLIENT
	 * ***/
	
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> findByCodeCli(
			@XmlElement(required=false,nillable=true) @WebParam(name="codeInd")String codeInd,
			@XmlElement(required=false,nillable=true) @WebParam(name="codeGrp")String codeGrp);
	
	/***
	 * Fermer compte épargne
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean fermerCompte(
			@XmlElement(required=false,nillable=true) @WebParam(name="comptFerm")CompteFerme compFerme, 
			@XmlElement(required=true,nillable=false) @WebParam(name="numCmpt")String numCmpt);
	
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
	@XmlElement(required=false,nillable=true) @WebParam(name="idProd")String idProd);
	
	//Rapport solde minimum maximum	
	@WebMethod
    @WebResult(name="resultat")
	public List<SoldeEpargne> getSoldeMinMax(
			@WebParam(name ="dateDeb") @XmlElement(required=true)String date1,
			@WebParam(name ="dateFin") @XmlElement(required=true)String date2);
	
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
	 * Rélever de compte
	 * ****/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getReleverCompte(
	@WebParam(name ="numCompte") @XmlElement(required=true)String numCompte,
	@WebParam(name ="dateDeb") @XmlElement(required=true)String dateDeb,
	@WebParam(name ="dateFin") @XmlElement(required=true)String dateFin
	);

	/***
	 * Rapport nouveau compte épargne
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteEpargne> rapportNouveauCompte(
	@WebParam(name ="dateDeb") @XmlElement(required=true)String dateDeb,
	@WebParam(name ="dateFin") @XmlElement(required=true)String dateFin
	);
	
	/***
	 * Rapport compte épargne fermer
	 * ***/	
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteFerme> getCompteFermer(
	@WebParam(name ="dateDeb") @XmlElement(required=true)String dateDeb,
	@WebParam(name ="dateFin") @XmlElement(required=true)String dateFin
	);
	
	/***
	 * Rapport fiche journalière épargne
	 * ****/
	@WebMethod
	@WebResult(name="resultat")
	public List<TransactionEpargne> getFicheJournalier(
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
	
	//Annuler dépôt à terme
	@WebMethod
	@WebResult(name="validation")
	public boolean annulerDAT(
	@XmlElement(required=false) @WebParam(name="compte") String compte,
	@XmlElement(required=false) @WebParam(name="raison") String raison);
	
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
	
}

