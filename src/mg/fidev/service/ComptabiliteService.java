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
import mg.fidev.model.CompteAuxilliaire;
import mg.fidev.model.ConfigTransactionCompta;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.OperationView;
import mg.fidev.utils.AfficheBalance;
import mg.fidev.utils.AfficheBilan;
import mg.fidev.utils.AfficheListeCreditDeclasser;
import mg.fidev.utils.HeaderCompta;
import mg.fidev.utils.MouvementCompta;


@WebService(name="comptabliteService", targetNamespace = "http://fidev.mg.comptabliteService", serviceName = "comptabliteService"
,portName ="comptabliteServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ComptabiliteService {
	
	/***
	 * METHODE POUR AJOUTER UN NOUVEAU COMPTE COMPTA
	 * ***/ 
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
	public List<Account> getAccounts();

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
	
	    
	//Recupère le solde d'une compte avant la date en paramètre
	@WebMethod
	@WebResult(name="resultat")
	public double getSoldeAvant(
			@WebParam(name = "numCompte") @XmlElement(required=false,nillable=true)String num,
			@WebParam(name = "date") @XmlElement(required=false,nillable=true)String date);

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
	
	//Liste des comptes à 4 positions de plus
	@WebMethod
	@WebResult(name="list")
	public List<Account> getCompte();
	
	//Chercher comptes à 4 positions de plus
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> findComptes(
			@XmlElement(required=false) @WebParam(name="compte")String compte);
	

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
			@XmlElement(required=false) @WebParam(name="analytique")String analytique,
			@XmlElement(required=false) @WebParam(name="auxilliaire")String auxilliaire,
			@XmlElement(required=false) @WebParam(name="budget")String budget,
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
	
	//Affiche tous les comptes auxilliaires
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteAuxilliaire> getAllAuxilliaire();
	
	//Chercher code auxilliaire
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteAuxilliaire> chercherAuxilliaire(
			@XmlElement(required=false) @WebParam(name="code")String code);
	
	//Enregistrement des divers opérations comptable
	@WebMethod
	@WebResult(name="validation")
	public boolean saveOperationDivers(
	@XmlElement(required=true) @WebParam(name="date")String date,
	@XmlElement(required=true) @WebParam(name="piece")String piece,
	@XmlElement(required=true) @WebParam(name="description")String description,
	@XmlElement(required=false) @WebParam(name="analytique")String analytique,
	@XmlElement(required=false) @WebParam(name="auxilliaire")String auxilliaire,
	@XmlElement(required=false) @WebParam(name="budget")String budget,
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
	
	//Recupère tout les comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<Account> getAcountDistinct();
	
	//Grand Livre général
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
	
	//Analyse comptes
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getAnalyseCompte(
			@XmlElement(required=false) @WebParam(name="compte")String compte,
			@XmlElement(required=false) @WebParam(name="compte2")String compte2,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//Déclasser crédits
	@WebMethod
	@WebResult(name="resultat")
	public List<AfficheListeCreditDeclasser> declasserCredit(
	@XmlElement(required=false) @WebParam(name="date")String dateDeb);
	
	//Enregistrement déclassement crédit
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
	
	//recupération de la configuration transaction comptable
	@WebMethod
	@WebResult(name="resultat")
	public ConfigTransactionCompta getConfigCompta();
	
	//Ajout opération view
	@WebMethod
	@WebResult(name="validation")
	public boolean addOperationView(
	@XmlElement(required=false) @WebParam(name="operation")OperationView operation);
	
	//List des opérations view
	@WebMethod
	@WebResult(name="resultat")
	public List<OperationView> listOperationView();
	
	//vider opération view
	@WebMethod
	@WebResult(name="validation")
	public boolean viderOperationView();
	
	//Modifier opération view
	@WebMethod
	@WebResult(name="resultat")
	public OperationView updateOperationView(
	@XmlElement(required=false) @WebParam(name="id")int id,
	@XmlElement(required=false) @WebParam(name="operation")OperationView operation);
	
	//-------------------------------------------------------------------------------------
	/*********************************** Cloture compte **********************************/
	//Ajout Cloture
	@WebMethod
	@WebResult(name="validation")
	public boolean addCloture(
	@XmlElement(required=true) @WebParam(name="dateDeb")String dateDeb,
	@XmlElement(required=true) @WebParam(name="dateFin")String dateFin,
	@XmlElement(required=true) @WebParam(name="desc")String desc,
	@XmlElement(required=true) @WebParam(name="user")int user
	);
	
	//Affiche plan comptable
	@WebMethod
	@WebResult(name="resultat")
	public List<HeaderCompta> getPlanCompta();
	
	//Réinitialiser solde des comptes
	@WebMethod
	@WebResult(name="validation")
	public boolean reinitialiserSolde();
	
	//Chercher code analytique
	@WebMethod
	@WebResult(name="resultat")
	public List<Analytique> findCodeAn(
			@XmlElement(required=true) @WebParam(name="code")String  code);
	
	//Chercher codes budgétaires
	@WebMethod
	@WebResult(name="resultat")
	public List<Budget> findCodeBud(
			@XmlElement(required=true) @WebParam(name="code")String  code);
	
	//Chercher comptes auxilliaires
	@WebMethod
	@WebResult(name="resultat")
	public List<CompteAuxilliaire> findCodeAux(
			@XmlElement(required=true) @WebParam(name="code")String  code);
	
	//Analyse comptes analytiques
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getAnalyseCompteAn(
			@XmlElement(required=false) @WebParam(name="compte")String compte,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//Analyse comptes Budgétaire
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getAnalyseCompteBud(
			@XmlElement(required=false) @WebParam(name="compte")String compte,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
	//Analyse comptes aucilliaires
	@WebMethod
	@WebResult(name="resultat")
	public List<Grandlivre> getAnalyseCompteAux(
			@XmlElement(required=false) @WebParam(name="compte")String compte,
			@XmlElement(required=false) @WebParam(name="dateDeb")String dateDeb,
			@XmlElement(required=false) @WebParam(name="dateFin")String dateFin);
	
}
