package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.Adresse;
import mg.fidev.model.FonctionMembreGroupe;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.MembreGroupe;
import mg.fidev.model.MembreView;

@WebService(name = "groupeService", targetNamespace = "http://individuel.fidev.com/", serviceName = "groupeService", portName = "groupeServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface GroupeService {
	
	/***
	 * AJOUT MEMBRE GROUPE
	 * ***/
	@WebMethod
	@WebResult(name = "resultat")
	public boolean addMembreGroupe(
			@WebParam(name = "nomGroupe") String codeGroupe,
			@WebParam(name = "rowIndividuel") String codeInd);
	/***
	 * LISTES DES GROUPES
	 * ***/
	@WebMethod  
	@WebResult(name = "resultat")
	public List<Groupe> getAllGroupe();
	
	/***
	 * CHERCHER GROUPE PAR SON CODE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public Groupe findOneGroupe(@WebParam(name = "codeGroupe")@XmlElement(required = true)String codeCode);

	/***
	 * AJOUT NOUVEAU GROUPE
	 * ***/
	@WebMethod
	@WebResult(name = "resultat")
	public String saveGroupe(@WebParam(name = "groupe")@XmlElement(required = false) Groupe goupe,
			@WebParam(name = "adresse")@XmlElement(required = false) Adresse adresse,
			@WebParam(name = "agence") @XmlElement(required = true) String codeAgence);
	
	/***
	 * LISTE DES MEMBRES DANS UNE GROUPE
	 * ***/
	@WebMethod
	@WebResult(name = "resultat")
	public List<Individuel> getListeMembreGroupe(@WebParam(name = "nomGroupe")@XmlElement(required = true) String nomGroupe);
	
	/***
	 * LISTE DES INDIVIDUELS NON MEMBRE DE GROUPE
	 * ***/
	@WebMethod
	@WebResult(name = "resultat")
	public List<Individuel> getListeNonMembre();
	
	/***
	 * AJOUT CONSEIL ADMIN D'UNE GROUPE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public void addConseil(
			@WebParam(name = "codeGroupe") @XmlElement(required=true,nillable=false) String codeGroupe,
			@WebParam(name = "president") @XmlElement(required=true,nillable=false) String president,
			@WebParam(name = "secretaire") @XmlElement(required=false,nillable=true) String secretaire,
			@WebParam(name = "tresorier") @XmlElement(required=false,nillable=true) String tresorier);
	
	/***
	 * TRANSFERER MEMBRE GROUPE
	 * ***/
	@WebMethod
	@WebResult(name="validation")
	public boolean transferMembre(
			@WebParam(name = "codeGroupe1") @XmlElement(required=true,nillable=false) String codeGrp1,
			@WebParam(name = "codeGroupe2") @XmlElement(required=true,nillable=false) String codeGrp2,
			@WebParam(name = "codeInd") @XmlElement(required=true,nillable=false) String codeInd);
	
	
	/***
	 * CHERCHER PAR SON CODE
	 * ***/
	@WebMethod
	@WebResult(name="resultat")
	public List<Groupe> findByCode(@WebParam(name = "code")@XmlElement(required = true)String code);
	
	//Ajouter individuel au membreView
	@WebMethod
	@WebResult(name="validation")
	public String addMembre(
	@WebParam(name = "membre")@XmlElement(required = false)MembreView membre); 
	
	//Liste membreView
	@WebMethod
	@WebResult(name="resultat")
	public List<MembreView> getallMembreView();
	
	//Vider membreView
	@WebMethod
	@WebResult(name="validation")
	public boolean deleteMembreView();
	
	//Recupérer fonction membre groupe
	@WebMethod
	@WebResult(name="resultat")
	public List<FonctionMembreGroupe> getFonctionMembre();
	
	//Liste membre groupe
	@WebMethod
	@WebResult(name="resultat")
	List<MembreGroupe> getMembreGroupe(
	@WebParam(name = "code")@XmlElement(required = true)String code);
	
	//Transeferer membre groupe
	@WebMethod
	@WebResult(name="validation")
	public boolean tansfertMembreGroupe(
	@WebParam(name = "id")@XmlElement(required = true)int id,
	@WebParam(name = "codeGroupe")@XmlElement(required = false)String codeGrp,
	@WebParam(name = "codeInd")@XmlElement(required = false)String codeInd,
	@WebParam(name = "fonction")@XmlElement(required = false)int fonction);
	

}
