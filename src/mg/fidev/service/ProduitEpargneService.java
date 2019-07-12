package mg.fidev.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlElement;

import mg.fidev.model.ProduitEpargne;

@WebService(name = "epargneService", targetNamespace = "http://fidev.com/epargneService", serviceName = "epargneService", portName = "epargneServicePort")
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface ProduitEpargneService {

	@WebMethod
	@WebResult(name = "validation")
	public boolean saveProduit(
			@WebParam(name = "nomProd") @XmlElement(required=true,nillable=false) String nomProdEp,
			@WebParam(name = "typeEpargne") @XmlElement(required=true,nillable=false) String nomTypeEp,
			@WebParam(name = "isActive") @XmlElement(required=true,nillable=false) boolean isActive);
	
	@WebMethod
	@WebResult(name="liste")
	public List<ProduitEpargne> findAllProduit();
	
	@WebMethod
	@WebResult(name="produitEntity")
	public ProduitEpargne findProduitById(@WebParam(name = "idProd") @XmlElement(required=true,nillable=false) String idProd);
	
	@WebMethod
	@WebResult(name = "validation")
	public boolean desactiverProduit(@WebParam(name = "idProd") String idProd);
	
	@WebMethod
	@WebResult(name = "validation")
	public boolean modifierProduit(@WebParam(name = "idProd")String idProd, @WebParam(name = "nomProd")String nomProd, @WebParam(name = "isActive")boolean isActive);
	
	@WebMethod
	@WebResult(name="validationInteret")
	public boolean updateConfigIntProduit(@WebParam(name = "periodeInt") @XmlElement(required=true,nillable=false) int periodeInteret,
			@WebParam(name = "tauxInt") @XmlElement(required=true,nillable=false) float tauxInteret,
			@WebParam(name = "nbrJrInt") @XmlElement(required=true,nillable=false) int nbrJrInteret,
			@WebParam(name = "nbrSemInt") @XmlElement(required=true,nillable=false) int nbrSemInteret,
			@WebParam(name = "soldeMinInd") @XmlElement(required=true,nillable=false) double soldeMinInd,
			@WebParam(name = "soldeMinGrp") @XmlElement(required=true,nillable=false) double soldeMinGrp,
			@WebParam(name = "intMinInd") @XmlElement(required=true,nillable=false) double interetMinInd,
			@WebParam(name = "intMinGrp") @XmlElement(required=true,nillable=false) double interetMinGrp,
			@WebParam(name = "modeCalcul") @XmlElement(required=true,nillable=false) String modeCalcul,
			@WebParam(name = "idproduit") @XmlElement(required=true,nillable=false) List<String> idProduit);
	
	@WebMethod
	@WebResult(name="validationConfig")
	boolean updateConfigProduit(@WebParam(name = "nbrJrInactif") @XmlElement(required=true,nillable=false) int nbrJrIn,
			@WebParam(name = "nbrJrMinRetrait") @XmlElement(required=true,nillable=false) int nbrJrMinRetrait,
			@WebParam(name = "nbrJrMaxDepot") @XmlElement(required=true,nillable=false) int nbrJrMaxDepot,
			@WebParam(name = "ageMinCpt") @XmlElement(required=true,nillable=false) int ageMinCpt,
			@WebParam(name = "fraisTenuCpt") @XmlElement(required=true,nillable=true) float fraisTenuCpt,
			@WebParam(name = "idproduit") @XmlElement(required=true,nillable=false) List<String> idProduit);
	
	@WebMethod
	@WebResult(name = "interet")
	void calculInteret(@WebParam(name = "numCpt") @XmlElement(required=true) List<String> listNumCpt);
}

