package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Account;
import mg.fidev.model.Analytique;
import mg.fidev.model.Budget;
import mg.fidev.model.Compte;
import mg.fidev.model.Grandlivre;
import mg.fidev.utils.AfficheBalance;
import mg.fidev.utils.AfficheBilan;
import mg.fidev.utils.AfficheListeCreditDeclasser;
import mg.fidev.utils.MouvementCompta;
import mg.fidev.utils.ResultatCompta;


@WebService(name="comptabliteService", targetNamespace = "http://fidev.mg.comptabliteService", serviceName = "comptabliteService"
,portName ="comptabliteServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ComptabiliteService {
	
	/***
	 * METHODE POUR AJOUTER UN NOUVEAU COMPTE COMPTA
	 * ***/ 
	
	@WebMethod
	@WebResult(name="resultat")
	public Compte ajoutCompte(
			@WebParam(name = "comptes") @XmlElement(required=false,nillable=true)Compte compte,
			@WebParam(name = "parentId") @XmlElement(required=false,nillable=true)String compteParent);
	
	@WebMethod
	@WebResult(name="resultat")
	public Account addAccount(
			@WebParam(name = "comptes") @XmlElement(required=false,nillable=true)Account compte,
			@WebParam(name = "parentId") @XmlElement(required=false,nillable=true)int compteParent);
	
	/***
	 * LISTES DES COMPTES COMPTA
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<Compte> getComptes();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> getAccounts();
	
	/***
	 * LISTES DES COMPTES Actif
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<Compte> getComptesActif(
			@XmlElement(required=false) @WebParam(name="compte")String compte);
	
	
	
	@WebMethod
	@WebResult(name="validation")
	public boolean supprimerCompte(int compte);

	/***
	 * METHODE POUR AFFICHER LE GRAND LIVRE PAR UTILISATEUR EN SESSION
	 * ***/
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> afficheGrandLivre(
			@WebParam(name = "compte") @XmlElement(required=false,nillable=true)String compte,
			@WebParam(name = "nomUtilisateur") @XmlElement(required=false,nillable=true)String nomUtilisateur,
			@WebParam(name = "dateDeb") @XmlElement(required=false,nillable=true)String dateDeb,
			@WebParam(name = "dateFin") @XmlElement(required=false,nillable=true)String dateFin);

	/***
	 * METHODE POUR LISTER LES DONNEEES DANS LE GRAND LIVRE 
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> findGranLivre(
			);
	
	
	/***
	 * LISTES DES COMPTES
	 * ***/
	
	@WebMethod
	@WebResult(name="list")
	public List<Account> listComptes();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> chercheAccount(@XmlElement(required=false) @WebParam(name="compte")String compte);
	
	/***
	 * AJOUT COMPTES CAISSES
	 * ***/
	
	@WebMethod
	@WebResult(name="validation")
	public boolean addCompteCaisse(
			@XmlElement(required=true) @WebParam(name="cptCaisse")String nomCompte,
			@XmlElement(required=true) @WebParam(name="devise")String devise,
			@XmlElement(required=true) @WebParam(name="planCompta") int planCompta);
	
	
	/***
	 * TRANSACTION MANUEL COMPTABLE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean saveTransaction(
			@XmlElement(required=true) @WebParam(name="date")String date,
			@XmlElement(required=true) @WebParam(name="piece")String piece,
			@XmlElement(required=true) @WebParam(name="description")String description,
			@XmlElement(required=true) @WebParam(name="compte1")String compte,
			@XmlElement(required=true) @WebParam(name="compte2")String compte2,
			@XmlElement(required=true) @WebParam(name="montant")double debit,
			@XmlElement(required=true) @WebParam(name="utilisateur")int user);
	
	@WebMethod
	@WebResult(name="validation")	
	public List<Grandlivre> grandLivreClient(
			@XmlElement(required=false) @WebParam(name="codeInd")String codeCli,
			@XmlElement(required=false) @WebParam(name="codeGrp")String codeGrp);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<String> balance(
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<MouvementCompta> mouvementCompte();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> tests();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean saveBudget(
			@XmlElement(required=false) @WebParam(name="budget")Budget budget, 
			@XmlElement(required=true) @WebParam(name="idCompte")int idCompte);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Budget> listBudget();
	
	@WebMethod
	@WebResult(name="resultat")
	public Budget findBudget(
			@XmlElement(required=false) @WebParam(name="code")String code);	
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Analytique> getAnalytique();
	
	@WebMethod
	@WebResult(name="validation")
	public boolean saveOperationDivers(
	@XmlElement(required=true) @WebParam(name="date")String date,
	@XmlElement(required=true) @WebParam(name="piece")String piece,
	@XmlElement(required=true) @WebParam(name="description")String description,
	@XmlElement(required=true) @WebParam(name="compte1")String compte,
	@XmlElement(required=true) @WebParam(name="compte2")String compte2,
	@XmlElement(required=true) @WebParam(name="montant")double debit,
	@XmlElement(required=true) @WebParam(name="analytique")String analytique,
	@XmlElement(required=true) @WebParam(name="budget")String budget,
	@XmlElement(required=true) @WebParam(name="utilisateur")int user
			);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreBudget(
			@XmlElement(required=false) @WebParam(name="code")String code,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=true) @WebParam(name="dateFin")String dateFin
			);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreAnalytique(
			@XmlElement(required=false) @WebParam(name="code")String code,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=true) @WebParam(name="dateFin")String dateFin
			);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> getAcountDistinct();
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreGen(
			@XmlElement(required=false) @WebParam(name="id")int id);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getJournal(
			@XmlElement(required=false) @WebParam(name="date")String date,
			@XmlElement(required=false) @WebParam(name="numCmpt")String numCmpt);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getJournalDivers(
			@XmlElement(required=false) @WebParam(name="date")String date);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheBalance> getBalance(
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheListeCreditDeclasser> declasserCredit(
	@XmlElement(required=false) @WebParam(name="date")String dateDeb);
	
	@WebMethod
	@WebResult(name="validation")
	public boolean saveDeclassement(
	@XmlElement(required=false) @WebParam(name="date")String dateDeb,
	@XmlElement(required=false) @WebParam(name="user")int user);

	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheBilan> getBilan(
	@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	@WebMethod
	@WebResult(name="resultat")
	public List<ResultatCompta> getResultat(
	@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
}
