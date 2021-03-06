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
import mg.fidev.model.ConfigTransactionCompta;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.OperationView;
import mg.fidev.utils.AfficheBalance;
import mg.fidev.utils.AfficheBilan;
import mg.fidev.utils.AfficheListeCreditDeclasser;
import mg.fidev.utils.MouvementCompta;


@WebService(name="comptabliteService", targetNamespace = "http://fidev.mg.comptabliteService", serviceName = "comptabliteService"
,portName ="comptabliteServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ComptabiliteService {
	
	
	//test
	@WebMethod
	@WebResult(name="resultat")
	public Compte ajoutCompte(
			@WebParam(name = "comptes") @XmlElement(required=false,nillable=true)Compte compte,
			@WebParam(name = "parentId") @XmlElement(required=false,nillable=true)String compteParent);
	/***
	 * METHODE POUR AJOUTER UN NOUVEAU COMPTE COMPTA
	 * ***/ 
	@WebMethod
	@WebResult(name="resultat")
	public Account addAccount(
			@WebParam(name = "comptes") @XmlElement(required=false,nillable=true)Account compte,
			@WebParam(name = "parentId") @XmlElement(required=false,nillable=true)int compteParent);
	
	//test
	@WebMethod
	@WebResult(name="resultat")
	public List<Compte> getComptes();
	
	/***
	 * LISTES DES COMPTES COMPTA
	 * ***/
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
	
	
	//Supprimer compte
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
	public List<Grandlivre> findGranLivre();
	/***
	 * LISTES DES COMPTES
	 * ***/
	
	@WebMethod
	@WebResult(name="list")
	public List<Account> listComptes();
	
	//CHERCHER COMPTE COMPTA 
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
	
	
	//Rapport grand livre d'un client
	@WebMethod
	@WebResult(name="validation")	
	public List<Grandlivre> grandLivreClient(
			@XmlElement(required=false) @WebParam(name="codeInd")String codeCli,
			@XmlElement(required=false) @WebParam(name="codeGrp")String codeGrp);

	//balance des comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<String> balance( 
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//Test
	@WebMethod
	@WebResult(name="resultat")
	public List<MouvementCompta> mouvementCompte();
	
	//Test
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> tests();
	
	//Enregistrement budget
	@WebMethod
	@WebResult(name="validation")
	public boolean saveBudget(
			@XmlElement(required=false) @WebParam(name="budget")Budget budget);
	
	//Affiche list budget
	@WebMethod
	@WebResult(name="resultat")
	public List<Budget> listBudget();
	
	//Chercher un budget par code budget
	@WebMethod
	@WebResult(name="resultat")
	public Budget findBudget(
			@XmlElement(required=false) @WebParam(name="code")String code);	
	
	//Chercher budgets par code budget
	@WebMethod
	@WebResult(name="resultat")
	public List<Budget> chercherBudget(
	@XmlElement(required=false) @WebParam(name="code")String code);
	
	//Chercher code analytique
	@WebMethod
	@WebResult(name="resultat")
	public List<Analytique> chercherAnalytique(
	@XmlElement(required=false) @WebParam(name="code")String code);
	
	//Affiche les codes analytiques
	@WebMethod
	@WebResult(name="resultat")
	public List<Analytique> getAnalytique();
	
	//Enregistrement des divers op�rations comptable
	@WebMethod
	@WebResult(name="validation")
	public boolean saveOperationDivers(
	@XmlElement(required=true) @WebParam(name="date")String date,
	@XmlElement(required=true) @WebParam(name="piece")String piece,
	@XmlElement(required=true) @WebParam(name="description")String description,
	@XmlElement(required=true) @WebParam(name="analytique")String analytique,
	@XmlElement(required=true) @WebParam(name="budget")String budget,
	@XmlElement(required=true) @WebParam(name="utilisateur")int user
			);
	
	//Grand livre budgetaires
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreBudget(
			@XmlElement(required=false) @WebParam(name="code")String code,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=true) @WebParam(name="dateFin")String dateFin
			);
	
	//Grand livre analytique
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreAnalytique(
			@XmlElement(required=false) @WebParam(name="code")String code,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=true) @WebParam(name="dateFin")String dateFin
			);
	
	//Recup�re tout les comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> getAcountDistinct();
	
	//Grand Livre g�n�ral
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getGrandLivreGen(
			@XmlElement(required=false) @WebParam(name="id")int id);
	
	//Journal caisse et journal banque
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getJournal(
			@XmlElement(required=false) @WebParam(name="date")String date,
			@XmlElement(required=false) @WebParam(name="numCmpt")String numCmpt);
	
	//Affiche journal des divers comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getJournalDivers(
			@XmlElement(required=false) @WebParam(name="date")String date);
	
	//Affiche balance
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheBalance> getBalance(
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//D�classer cr�dits
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheListeCreditDeclasser> declasserCredit(
	@XmlElement(required=false) @WebParam(name="date")String dateDeb);
	
	//Enregistrement d�classement cr�dit
	@WebMethod
	@WebResult(name="validation")
	public boolean saveDeclassement(
	@XmlElement(required=false) @WebParam(name="date")String dateDeb,
	@XmlElement(required=false) @WebParam(name="user")int user);

	//Affiche bilan des comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheBilan> getBilan(
	@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//Affiche resultats des comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheBilan> getResultat(
	@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
	@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	
	//Enregistrement configuration transaction
	@WebMethod
	@WebResult(name="validation")
	public boolean saveConfigTransaction(
	@XmlElement(required=false) @WebParam(name="compteCaisse")int caise,
	@XmlElement(required=false) @WebParam(name="compteBanque")int banque);
	
	//recup�ration de la configuration transaction comptable
	@WebMethod
	@WebResult(name="resultat")
	public ConfigTransactionCompta getConfigCompta();
	
	//Ajout op�ration view
	@WebMethod
	@WebResult(name="validation")
	public boolean addOperationView(
	@XmlElement(required=false) @WebParam(name="operation")OperationView operation);
	
	//List des op�rations view
	@WebMethod
	@WebResult(name="resultat")
	public List<OperationView> listOperationView();
	
	//vider op�ration view
	@WebMethod
	@WebResult(name="validation")
	public boolean viderOperationView();
	
	//Modifier op�ration view
	@WebMethod
	@WebResult(name="resultat")
	public OperationView updateOperationView(
	@XmlElement(required=false) @WebParam(name="id")int id,
	@XmlElement(required=false) @WebParam(name="operation")OperationView operation);
}
