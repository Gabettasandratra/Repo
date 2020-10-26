package mg.fidev.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.Account;
import mg.fidev.model.Caisse;
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
import mg.fidev.model.CounterExecution;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.InteretEpargne;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.TransactionEpargneSupp;
import mg.fidev.model.TransactionPep;
import mg.fidev.model.TypeEpargne;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.ProduitEpargneService;
import mg.fidev.utils.CodeIncrement;
import mg.fidev.utils.epargne.CalculDAT;
import mg.fidev.utils.epargne.FicheCaisseEpargne;
import mg.fidev.utils.epargne.SoldeEpargne;

@WebService(name = "epargneService", targetNamespace = "http://fidev.mg.epargneService", serviceName = "epargneService", portName = "epargneServicePort", endpointInterface = "mg.fidev.service.ProduitEpargneService")
public class ProduitEpargneServiceImpl implements ProduitEpargneService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transaction = em.getTransaction();
	/*  
	 * Method recupere le dernier index
	 */ 
	static int getLastIndex(String typeEp) { 
		//select count(*) from produit_epargne where Type_epargnenom_type_epargne = '"+typeEp+"' 
		String sql = "select count(p) from ProduitEpargne p join p.typeEpargne t "
				+ " where t.nomTypeEpargne = '"+typeEp+"' ";
		Query q = em.createQuery(sql);
		  
		long result = (long) q.getSingleResult();
		System.out.println("result count: "+ result);
		return (int)result;
	}

	//Enregistrer un nouveau produit
	@Override
	public boolean saveProduit(String nomProdEp, String nomTypeEp, boolean isActive) {
		try {
			ProduitEpargne pro = new ProduitEpargne();
			TypeEpargne typeEp = em.find(TypeEpargne.class, nomTypeEp);
			
			pro.setNomProdEpargne(nomProdEp);
			pro.setTypeEpargne(typeEp);
			pro.setEtat(isActive);
			
			String index = "";
			int lastIndex = getLastIndex(pro.getTypeEpargne().getNomTypeEpargne());
			if(lastIndex==0)
				index = String.format("001");
			else
				index = String.format("%03d", ++lastIndex);				
			
			if(pro.getTypeEpargne().getNomTypeEpargne().equals("DAT (Dépôt à terme)"))
				pro.setIdProdEpargne("AT" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("DAV (Dépôt à vue)"))
				pro.setIdProdEpargne("AV" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("Dépôt de garantie"))
				pro.setIdProdEpargne("Ga" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("Plan épargne"))
				pro.setIdProdEpargne("PE" + index);
			
			em.getTransaction().begin();
			em.persist(pro);
			em.getTransaction().commit();
			em.refresh(pro);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return false;
		}

	}

	//afficher tout les produits
	@Override
	public List<ProduitEpargne> findAllProduit(String type) {
		try {
			String sql = "select p from ProduitEpargne p join p.typeEpargne t where "
					+ "p.supprimer='false'";
			if(!type.equals(""))
				sql += " and t.abrev ='"+ type +"'";
			TypedQuery<ProduitEpargne> q = em.createQuery(sql, ProduitEpargne.class);
			
			List<ProduitEpargne> results = q.getResultList();
			//System.out.println("Nety le izy");
			return results;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	//désactiver un produit
	@Override
	public boolean desactiverProduit(String idProd) {
		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			p.setEtat(false);
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(p);
			System.out.println("Desactivé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return false;
		}
	}
	
	//Activer produit
	@Override
	public boolean activerProduit(String idProd) {
		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			p.setEtat(true);
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(p);
			System.out.println("Activé");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur");
			return false;
		}
	}
	
	//Supprimer produit épargne
	@Override
	public boolean supprimerProduitEpargne(String idProd) {
		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			p.setSupprimer(true);
			em.getTransaction().begin();
			em.merge(p); 
			em.getTransaction().commit();
			System.out.println("Suppression ok");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur suppression");
			return false;
		}
	}
	

	//modifier produit
	@Override
	public boolean modifierProduit(String idProd, String nomProd,
			boolean isActive) {
		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			p.setNomProdEpargne(nomProd);
			p.setEtat(isActive);
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(p);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return false;
		}

	}

	//Chercher produit par son identifiant
	@Override
	public ProduitEpargne findProduitById(String idProd) {

		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return null;
		}

	}
	/***
	 * CHERCHER COMPTE ACTIF PAR NUM COMPTE
	 * ***/
	@Override
	public List<CompteEpargne> findCompteByCode(String numCmpt) {
		TypedQuery<CompteEpargne> query = em.createQuery("SELECT c FROM CompteEpargne c WHERE c.fermer = :x AND c.numCompteEp LIKE :code"
				+ " AND c.isActif =:y"
				,CompteEpargne.class);
		query.setParameter("x", false);
		query.setParameter("code", numCmpt+"%");
		query.setParameter("y", true);
		
		List<CompteEpargne> result = query.getResultList();
		
		if(!result.isEmpty())return result;
		else System.out.println("Auccun client trouvé!!!");
		
		return null;
	}
	
	//Chercher compte inactif
	@Override
	public List<CompteEpargne> findCompteInactifByCode(String numCmpt) {
		TypedQuery<CompteEpargne> query = em.createQuery("SELECT c FROM CompteEpargne c WHERE c.fermer = :x AND c.numCompteEp LIKE :code"
				+ " AND c.isActif =:y"
				,CompteEpargne.class);
		query.setParameter("x", false);
		query.setParameter("code", numCmpt+"%");
		query.setParameter("y", false);
		
		List<CompteEpargne> result = query.getResultList();
		
		if(!result.isEmpty())return result;
		else System.out.println("Auccun client trouvé!!!");
		
		return null;
	}


	//Enregistrement configuration génerale d'un produit épargne
	@Override
	public boolean saveConfigProduit(ConfigProdEp confProd, String idProduit) {
		ProduitEpargne pdt = em.find(ProduitEpargne.class, idProduit);
		try {
			pdt.setConfigProdEp(confProd);
			
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(pdt);			
			System.out.println("Enregistrement reusiit!!!");			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problement d'enregistrement!");
			return false;
		}
	}
	
	//Enregistrement Configuration Intérêt d'un produit épargne
	@Override
	public boolean saveConfigIntProduit(ConfigInteretProdEp confInteret, String idProduit) {
	    
		ProduitEpargne pdt = em.find(ProduitEpargne.class, idProduit);
		
		try {
			pdt.setConfigInteretProdEp(confInteret);
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(pdt);
			System.out.println("Enregistrement reuissit!!!");			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Probleme d'enregisrtrement!");
			return false;
		}
	}
 
	///	Configuration GL épargne
	@Override
	public boolean configGLepargne(ConfigGlEpargne configGlEpargne,
			String idProduit) {
		ProduitEpargne pdtEp = em.find(ProduitEpargne.class, idProduit);
		
		try {
			pdtEp.setConfigGlEpargne(configGlEpargne);
			
			
			em.getTransaction().begin();
			em.flush();
			em.getTransaction().commit();
			em.refresh(pdtEp);
			System.out.println("Configuration GL épargne succes");
			return true;
		} catch (Exception e) {
			System.err.println("");
			return false;
		}
	}
	
	//Save configuration Grand livre dépôt à terme
	@Override
	public boolean configGlDatEpargne(String idProduit, ConfigGLDAT confDAT) {
		ProduitEpargne p = em.find(ProduitEpargne.class, idProduit);
		try {
			p.setConfigGlDat(confDAT);
			
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(p);
			System.out.println("Configuration GL DAT enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Save configuration général DAT produit épargne
	@Override
	public boolean configGeneralDatEpargne(String idProduit,
			ConfigGeneralDAT confGen) {
		ProduitEpargne p = em.find(ProduitEpargne.class, idProduit);
		try {
			p.setConfigGeneralDat(confGen); 
			
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(p);
			System.out.println("Configuration général DAT enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveConfigGeneralPEP(String idProduit,
			ConfigGeneralPEP confGen) {
		ProduitEpargne p = em.find(ProduitEpargne.class, idProduit);
		try {
			p.setConfigGeneralPEP(confGen); 
			
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(p);
			System.out.println("Configuration général PEP enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveConfigGlPEP(String idProduit, ConfigGLPEP confPEP) {
		ProduitEpargne p = em.find(ProduitEpargne.class, idProduit);
		try {
			p.setConfigGlPep(confPEP);
			
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(p);
			System.out.println("Configuration GL PEP enregistré");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//LIST TYPE EPARGNE
	@Override
	public List<TypeEpargne> getTypeEpargne() {
		return em.createQuery("select t from TypeEpargne t",TypeEpargne.class).getResultList();
	}
	
	//LIST CATEGORIE EPARGNE
	@Override
	public List<CatEpargne> getCategorieEp() {
		return em.createQuery("select c from CatEpargne c",CatEpargne.class).getResultList();	
	}

	/***
	 * OUVRIR COMPTE EPARGNE
	 * ***/
	@Override
	public CompteEpargne ouvrirCompte(String dateOuverture,boolean pasRetrait, boolean prioritaire
			, String idProduitEp,String individuelId, String groupeId, int userId) {
			//Instance nouveau compte_epargne
			CompteEpargne cpt_ep = new CompteEpargne();
			
			System.out.println("Code Ind = "+individuelId +" Code Grp = "+groupeId);
			ProduitEpargne pdt_ep = em.find(ProduitEpargne.class, idProduitEp);
			Individuel ind = em.find(Individuel.class, individuelId);
			Groupe grp = em.find(Groupe.class, groupeId);			
			Utilisateur ut = em.find(Utilisateur.class, userId);
			
			//Charge les configuration
			ConfigProdEp confProduit = pdt_ep.getConfigProdEp();
			
			//vérification si le client a déjà un compte avec le même produit
			boolean verInd = false;
			boolean verGrp = false;
			
			if(ind != null){
				String sql = "select c from CompteEpargne c join c.individuel i join c.produitEpargne p "
						+ " where i.codeInd='"+ind.getCodeInd()+"' and p.idProdEpargne='"+idProduitEp+"'";
				Query qrs = em.createQuery(sql);
				if(qrs.getResultList().isEmpty())
					verInd = true;
			}
			if(grp!=null){
				String sql = "select c from CompteEpargne c join c.groupe g join c.produitEpargne p "
						+ " where g.codeGrp='"+grp.getCodeGrp()+"' and p.idProdEpargne='"+idProduitEp+"'";
				Query qrs = em.createQuery(sql);
				if(qrs.getResultList().isEmpty())
					verGrp = true;
			}
			System.out.println("valeur de verInd = "+verInd +" valeur de VerGrp = "+verGrp);
			
						
			if(verInd == true || verGrp == true){
				//Insertion des informations sur le compte
				cpt_ep.setDateOuverture(dateOuverture);
				cpt_ep.setDateEcheance("");
				cpt_ep.setUtilisateur(ut);
				cpt_ep.setProduitEpargne(pdt_ep);
				cpt_ep.setIsActif(true);
				cpt_ep.setFermer(false);
				cpt_ep.setPasRetrait(false);
				cpt_ep.setPrioritaire(prioritaire);
				if(ind != null){
					System.out.println("Compte individuel");
					String dateNais = ind.getDateNaissance();				
					LocalDate dateNow = LocalDate.now();
					
					int age = Period.between(LocalDate.parse(dateNais), dateNow).getYears();
					System.out.println("âge = "+age);
					if(age >= confProduit.getAgeMinCpt()){					
						cpt_ep.setIndividuel(ind);
						cpt_ep.setNumCompteEp(individuelId+"/"+idProduitEp);
						
					}else{
						return null;
					}
				}
				else if(grp != null){
					System.out.println("Compte groupe");
					cpt_ep.setGroupe(grp);
					cpt_ep.setNumCompteEp(groupeId+"/"+idProduitEp);
				}
				
				//	Enregistrement du compte lorsque toutes les informations nécessaires
				//	sont valides et complètes
				if(cpt_ep.getIndividuel() != null || cpt_ep.getGroupe() != null){
					System.out.println("Information compte épargne prête");
					try{
						
						em.getTransaction().begin();
						em.persist(cpt_ep);
						em.getTransaction().commit();
						em.refresh(cpt_ep);
						System.out.println("Nouveau compte épargne ouvert");
						return cpt_ep;
					}catch(Exception ex){
						System.err.println(ex.getMessage());
						return null;
					}
				}
				else
					return null;
			}
			return null;
		
	}

	//CHERCHER COMPTE PAR CODE CLIENT ET PRODUIT
	@Override
	public CompteEpargne findCompte(String idProduit, String codeInd,
			String codeGrp) {
		CompteEpargne result = new CompteEpargne();
		
		if(codeInd.equals("")){
			
			Query q = em.createQuery("SELECT e FROM CompteEpargne e JOIN "
					+ "e.produitEpargne pe, e.groupe g WHERE pe.idProdEpargne = :idP AND g.codeGrp = :codeG");
			q.setParameter("idP", idProduit);
			q.setParameter("codeG", codeGrp);
			
			if(q.getSingleResult() == null)
				return null;
			else 
				result = (CompteEpargne) q.getSingleResult();
			
		}else if(codeGrp.equals("")){
			
			Query q = em.createQuery("SELECT e FROM CompteEpargne e JOIN "
					+ "e.produitEpargne pe , e.individuel i WHERE pe.idProdEpargne = :idP AND i.codeInd = :codeI");
			q.setParameter("idP", idProduit);
			q.setParameter("codeI", codeInd);
			
			if(q.getSingleResult() == null)
				return null;
			else 
				result = (CompteEpargne) q.getSingleResult();	
		}else{
			return null;
		}
		
		return result;
	}

	//Checher unique compte par numéro de compte
	@Override
	public CompteEpargne findUniqueCompte(String numCompte) {
		
		CompteEpargne c  = em.find(CompteEpargne.class, numCompte);
		if(c != null){
			System.out.println("Compte trouvé num : "+c.getNumCompteEp());
			return c;
		}else{
			System.out.println("Compte non trouvé");
			return null;
		}
			
	}
	
	
	/***
	 * TRASACTION EPARGNE
	 * ***/
	@SuppressWarnings("unused")
	@Override
	public String trasactionEpargne(String typeTransac, String dateTransac,
			double montant, String description, String pieceCompta,String typPaie,String numTel, String numCheq,
			String nomCptCaisse, String numCptEp,int idUser) {
					
			//UTILISATEUR DE SAISIE
			Utilisateur ut = em.find(Utilisateur.class, idUser);			
			///incrémenter le tcode dans la table grandlivre
			String indexTcode = CodeIncrement.genTcode(em);
			
			Individuel ind = null;
			Groupe grp = null;
			CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
			
			if(cptEp.getIndividuel() != null) 
				ind = cptEp.getIndividuel();
			if(cptEp.getGroupe() != null)
				grp = cptEp.getGroupe();
						
			//Charge les configurations du compte Epargne
			ConfigProdEp confProd = cptEp.getProduitEpargne().getConfigProdEp();
			ConfigGlEpargne confGlEp = cptEp.getProduitEpargne().getConfigGlEpargne();
			ConfigInteretProdEp confInter = cptEp.getProduitEpargne().getConfigInteretProdEp();

			String cptC = "";			
			Caisse cptCaisse = null;
			String vp = "";			
			if(typPaie.equalsIgnoreCase("cash")){
				cptCaisse = em.find(Caisse.class, nomCptCaisse);
				cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());							
				vp = String.valueOf(cptCaisse.getAccount().getNumCpt());		
			}else if(typPaie.equalsIgnoreCase("cheque")){
				vp = numCheq;
			}else if(typPaie.equalsIgnoreCase("mobile")){
				vp = numTel;
			}
			
			//	Enregistrement de la transaction
			TransactionEpargne trans = new TransactionEpargne(indexTcode, dateTransac,
					description, montant, pieceCompta, typeTransac, typPaie, vp, 0, 0, 0, null, ut);
			trans.setCaisse(cptCaisse);
			
			String result="";
			
			LocalDate now = LocalDate.now();
			boolean cloture = ComptabliteServiceImpl.verifieCloture(dateTransac);
			
			//Test des configurations
			if(cloture != true){
				if(cptEp != null && cptEp.getIsActif() == true && cptEp.isFermer() == false){
					System.out.println("Informations sur la transaction prêtes");
					try {
						if(ind != null && (ind.isApprouver() == false || ind.isSupprimer() == true)){
							result = "Désolé, le client numéro "+ind.getCodeInd()+" n'est plus membre de cette angence";
						}else{
						
						if(typeTransac.equalsIgnoreCase("DE")){
							
							//Pour vérifier les transactions du compte épargne en paramètre
							TypedQuery<TransactionEpargne> quer = em.createQuery("select t from TransactionEpargne t join t.compteEpargne c"
									+ " where c.numCompteEp= :x",TransactionEpargne.class);
							quer.setParameter("x", numCptEp);					
							
							//Compte comptable à débiter
							Account accDeb = null;
							
							if(typPaie.equalsIgnoreCase("cash")){							
								accDeb = cptCaisse.getAccount();							
							}else if(typPaie.equalsIgnoreCase("cheque")){
								accDeb = CodeIncrement.getAcount(em, confGlEp.getCptCheque());
								
							}else if(typPaie.equalsIgnoreCase("mobile")){							
								accDeb = CodeIncrement.getAcount(em, confGlEp.getChargeSMScpt());
							}
							
							//Compte comptable à créditer
							Account accCred = CodeIncrement.getAcount(em, confGlEp.getEpargneInd());
												
							String desc = "";						
							if(quer.getResultList().isEmpty()){
								
								if(confProd.getSoldeOverture() > montant){
									result = "Le montant est supérieur au solde d'ouverture ("+confProd.getSoldeOverture()+")";
									//return false;
								}else{
									desc = "Dépôt d'ouverture du compte "+numCptEp;
									trans.setCompteEpargne(cptEp);
									transaction.begin();
									em.persist(trans);
									transaction.commit();
									em.refresh(trans);
									ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, desc,
											pieceCompta, 0, montant, ut, ind, grp, cptEp, null, accDeb);
	
									ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, desc,
											pieceCompta, montant, 0, ut, ind, grp, cptEp, null, accCred);
									result = "Transaction enregistré";
									//return true;
								}
								
							}else{	
								desc = "Dépôt épargne du compte "+numCptEp;
								trans.setCompteEpargne(cptEp); 
								transaction.begin();
								em.persist(trans);
								transaction.commit();
								em.refresh(trans);
								ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, desc,
										pieceCompta, 0, montant, ut, ind, grp, cptEp, null, accDeb);
	
								ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, desc,
										pieceCompta, montant, 0, ut, ind, grp, cptEp, null, accCred);
								result = "Transaction enregistré";
								//return true;							
							}	
						
						}//Si transaction est retrait d'épargne
						else if(typeTransac.equalsIgnoreCase("RE")){
							//Vérifier si le compte est autorisé à faire de rétrait
							if(cptEp.isPasRetrait() == true){
								result = "Désolé, ce compte n'est pas autorisé à faire de retrait";
								//return false;
								
							}else{
								
								double sd = cptEp.getSolde() - montant;
								//Pour vérifier le solde minimum d'un compte
								if(sd < confInter.getSoldeMinInd() ){
									result = "Montant superieur au montant autorisé";
									//return false;
								}else{		
									//Pour vérifier le nombre de jours entre deux retrait
									int diff = confProd.getNbrJrMinRet(); 
									String rq = "select max(t.dateTransaction) from TransactionEpargne t join "
											+ " t.compteEpargne c where c.numCompteEp='"+numCptEp+"' and t.typeTransEp ='RE'";
									Query qs = em.createQuery(rq);
									int v = confProd.getNbrJrMinRet();
									if(qs.getSingleResult() != null){
										String dernierTransaction = (String)qs.getSingleResult();
										long difDate = calculDiffDate(dernierTransaction, dateTransac);
										v = (int)difDate;
									}
									
									System.out.println("valeur = "+ v);
									if(diff > v){
										int a = diff - v;
										result = "Le retrait est autorisé après "+ a + " jour(s)";
										//return false;
									}else{									
										//Compte comptable à créditer
										Account acCred = null;					
										
										if(typPaie.equalsIgnoreCase("cash")){
											acCred = cptCaisse.getAccount();							
											if(confProd.getCommRetraitCash() != 0){
												Account accGl = CodeIncrement.getAcount(em, confGlEp.getCommEpargne());
												ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Commission sur retrait cash du compte "+numCptEp, pieceCompta,
														0, confProd.getCommRetraitCash(), ut, ind, grp, cptEp, null, accGl);
											}
										}else if(typPaie.equalsIgnoreCase("cheque")){
											acCred = CodeIncrement.getAcount(em, confGlEp.getCptCheque());										
										}else if(typPaie.equalsIgnoreCase("mobile")){
											acCred = CodeIncrement.getAcount(em, confGlEp.getChargeSMScpt());
										}
										
										//compte comptable à débiter
										Account accDeb = CodeIncrement.getAcount(em, confGlEp.getEpargneInd());	
							
										if(cptEp.getProduitEpargne().getTypeEpargne().getAbrev().equalsIgnoreCase("DAT")){
											Account interDeb = CodeIncrement.getAcount(em, confGlEp.getIntPayeInd());	
											ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Intérêt sur rétrait du compte "+numCptEp,
													pieceCompta, 0, montant, ut, ind, grp, cptEp, null, interDeb);
										}								
										trans.setCompteEpargne(cptEp); 
										transaction.begin();
										em.persist(trans);
										transaction.commit();
										em.refresh(trans);		
										//Enregistrement imputation
										//Débit
										ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Retrait d'épargne du compte "+numCptEp,
												pieceCompta, 0, montant, ut, ind, grp, cptEp, null, accDeb);
										//Crédit
										ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Retrait d'épargne du compte "+numCptEp,
												pieceCompta, montant, 0, ut, ind, grp, cptEp, null, acCred);									
										result = "Transaction enregistré";
										
									}												
								}
							}					
							
						}
						}
						
					} catch (Exception e) {
						result = "Erreur enregistrement";
						//return false;
					}
				}
				else{
					result = "Pas de transaction autorisé, car ce compte est inactif";
					//return false;
				}			
		}else{
			result = "Transaction non autorisé, les comptes a été clôturer";
			//return false;
		}
			System.out.println(result);
			return result;
	}
	
	//----------------------------------------------------------------------------------------------------------
		/********************************* Modification transaction ***********************************************/
		//recupéré tous les transactions
		@Override
		public List<TransactionEpargne> getAllTransaction() {
			String sql = "select t from TransactionEpargne t ";
			return em.createQuery(sql, TransactionEpargne.class).getResultList();
		}
		
		//Recupéré une transaction
		@Override
		public TransactionEpargne getDetailTrans(String codeTrans) {
			return em.find(TransactionEpargne.class, codeTrans);
		}
		
		//Enregistrement modification transaction
		@Override
		public boolean updateTransaction(String codeTrans, String typeTrans,
				String dateTrans, double montant, String description,
				String pieceCompta, String typPaie, String numTel, String numCheq,
				String nomCptCaisse, int idUser) {
			
		TransactionEpargne tr = getDetailTrans(codeTrans);
		Utilisateur ut = em.find(Utilisateur.class, idUser);
		CompteEpargne ce = tr.getCompteEpargne();
		Individuel ind = null;
		Groupe grp = null;
		if (tr.getCompteEpargne().getIndividuel() != null)
			ind = tr.getCompteEpargne().getIndividuel();
		if (tr.getCompteEpargne().getGroupe() != null)
			grp = tr.getCompteEpargne().getGroupe();

		// Test si le client n'est pas approuvé ou supprimer
		if (ind != null
				&& (ind.isApprouver() == false || ind.isSupprimer() == true)) {
			return false;
		} else {
			/********************** Modification transaction épargne ******************/
			double ancienMontant = tr.getMontant();

			
			String typ = "";
			String vp = "";

			Caisse cptCaisse = null;
			if (numTel.equals("") && numCheq.equals("")
					&& nomCptCaisse.equals("")) {
				typ = tr.getTypePaie();
				vp = tr.getValPaie();
				cptCaisse = tr.getCaisse();
			} else {
				if (typPaie.equalsIgnoreCase("cash")) {
					cptCaisse = em.find(Caisse.class, nomCptCaisse);
					typ = "cash";
					vp = String.valueOf(cptCaisse.getAccount().getNumCpt());
				} else if (typPaie.equalsIgnoreCase("cheque")) {
					vp = numCheq;
					typ = "cheque";
				} else if (typPaie.equalsIgnoreCase("mobile")) {
					vp = numTel;
					typ = "mobile";
				}
			}

			if (typeTrans.equals(""))
				typeTrans = tr.getTypeTransEp();

			tr.setTypeTransEp(typeTrans);
			tr.setDateTransaction(dateTrans);
			tr.setDescription(description);
			tr.setPieceCompta(pieceCompta);
			tr.setMontant(montant);
			tr.setTypePaie(typ);
			tr.setValPaie(vp);
			tr.setUserUpdate(ut);

			// Compte comptable à débiter
			Account accDeb = null;

			if (typ.equalsIgnoreCase("cash")) {
				accDeb = cptCaisse.getAccount();
			} else if (typ.equalsIgnoreCase("cheque")) {
				accDeb = CodeIncrement.getAcount(em, ce.getProduitEpargne()
						.getConfigGlEpargne().getCptCheque());
			} else if (typ.equalsIgnoreCase("mobile")) {
				accDeb = CodeIncrement.getAcount(em, ce.getProduitEpargne()
						.getConfigGlEpargne().getChargeSMScpt());
			}
			// Compte comptable à créditer
			Account accCred = CodeIncrement.getAcount(em, ce
					.getProduitEpargne().getConfigGlEpargne().getEpargneInd());
			
			//List<Grandlivre> gs = CodeIncrement.getGrandLivreByCodeTrans(em, codeTrans);
			Grandlivre debit = CodeIncrement.getDebitGL(em, codeTrans);
			Grandlivre credit = CodeIncrement.getCreditGL(em, codeTrans);
			String nouvDesc = "Modification trans. du compte "
					+ ce.getNumCompteEp();

			if (typeTrans.equalsIgnoreCase("DE")) {
				double solde = (ce.getSolde() + (ancienMontant) + montant);
				ce.setSolde(solde);
				tr.setSolde(solde); 
				try {
									
					transaction.begin();
					em.flush();
					em.flush();					
					transaction.commit();
					em.refresh(tr);
					em.refresh(ce);
					
					debit.setDate(dateTrans);
					debit.setDescr(nouvDesc);
					debit.setPiece(pieceCompta);
					debit.setDebit(montant);
					debit.setCredit(0);
					debit.setUtilisateur(ut);
					debit.setCodeInd(ind);
					debit.setGroupe(grp);
					debit.setAgence(null);
					debit.setCompteEpargne(ce);
					debit.setAccount(accDeb);
					
					credit.setDate(dateTrans);
					credit.setDescr(nouvDesc);
					credit.setPiece(pieceCompta);
					credit.setDebit(0);
					credit.setCredit(montant);
					credit.setUtilisateur(ut);
					credit.setCodeInd(ind);
					credit.setGroupe(grp);
					credit.setAgence(null);
					credit.setCompteEpargne(ce);
					credit.setAccount(accCred);
					transaction.begin();
					em.flush();
					em.flush();
					transaction.commit();
					em.refresh(debit);
					em.refresh(credit);

					System.out.println("Modification transaction réussie");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur modification transaction");
				}

			} else if (typeTrans.equalsIgnoreCase("RE")) {
				double solde = (ce.getSolde() + (ancienMontant) - montant);
				ce.setSolde(solde);
				tr.setSolde(solde);
				try {
									
					transaction.begin();
					em.flush();
					em.flush();
					transaction.commit();
					em.refresh(tr);
					em.refresh(ce);
					
					debit.setDate(dateTrans);
					debit.setDescr(nouvDesc);
					debit.setPiece(pieceCompta);
					debit.setDebit(montant);
					debit.setCredit(0);
					debit.setUtilisateur(ut);
					debit.setCodeInd(ind);
					debit.setGroupe(grp);
					debit.setAgence(null);
					debit.setCompteEpargne(ce);
					debit.setAccount(accCred);
					
					credit.setDate(dateTrans);
					credit.setDescr(nouvDesc);
					credit.setPiece(pieceCompta);
					credit.setDebit(0);
					credit.setCredit(montant);
					credit.setUtilisateur(ut);
					credit.setCodeInd(ind);
					credit.setGroupe(grp);
					credit.setAgence(null);
					credit.setCompteEpargne(ce);
					credit.setAccount(accDeb);
					transaction.begin();
					em.flush();
					em.flush();
					transaction.commit();
					em.refresh(debit);
					em.refresh(credit);
					
					System.out.println("Modification transaction réussie");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur modification transaction");
				}
			}
		}		
			
			return false;
		}
		
		
		//Suppression transaction
		@Override
		public boolean deleteTransaction(String codeTrans, int idUser) {
			TransactionEpargne tr = getDetailTrans(codeTrans);
			Utilisateur ut = em.find(Utilisateur.class, idUser);
			CompteEpargne ce = tr.getCompteEpargne();
			
			Individuel ind = null;
			Groupe grp = null;
			if(tr.getCompteEpargne().getIndividuel() != null) 
				ind = tr.getCompteEpargne().getIndividuel();
			if(tr.getCompteEpargne().getGroupe() != null)
				grp = tr.getCompteEpargne().getGroupe();
			
			//Modification imputation comptable
			//Compte comptable à débiter
			Account Deb = null;
			
			if(tr.getTypePaie().equalsIgnoreCase("cash")){							
				Deb = tr.getCaisse().getAccount();							
			}else if(tr.getTypePaie().equalsIgnoreCase("cheque")){
				Deb = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getCptCheque());
			}else if(tr.getTypePaie().equalsIgnoreCase("mobile")){							
				Deb = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
			}	
					
			//Compte comptable à créditer
			Account Cred = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getEpargneInd());
			
			String desc = "Suppression trans. du compte "+ce.getNumCompteEp();	
			String piece = "Supp. transaction";
			LocalDate dt = LocalDate.now();
			try {				
				if(tr.getTypeTransEp().equalsIgnoreCase("DE")){
					
					ce.setSolde((ce.getSolde() - tr.getMontant()));						

					//debit
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, 0, tr.getMontant(), ut, ind, grp, ce, null, Cred);
					
					//crédit
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, tr.getMontant(), 0, ut, ind, grp, ce, null, Deb);
					
					
				}else if(tr.getTypeTransEp().equalsIgnoreCase("RE")){
					
					ce.setSolde((ce.getSolde() + tr.getMontant()));	
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, 0, tr.getMontant(), ut, ind, grp, ce, null, Deb);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, tr.getMontant(), 0, ut, ind, grp, ce, null, Cred);
					
				}
				TransactionEpargneSupp trs = new 
						TransactionEpargneSupp(tr.getIdTransactionEp(), tr.getDateTransaction(),
								tr.getDescription(), tr.getMontant(), tr.getPieceCompta(), tr.getSolde(),
								tr.getTypeTransEp(), tr.getTypePaie(), tr.getValPaie(), tr.getCommRet(),
								tr.getCommTrans(), tr.getPenalPrelev(), ut.getNomUtilisateur(), ce.getNumCompteEp());
				trs.setIdProduit(ce.getProduitEpargne().getIdProdEpargne());
				if(ce.getIndividuel() != null){
					trs.setCodeClient(ce.getIndividuel().getCodeInd());
					trs.setCodeAgence(ce.getIndividuel().getAgence().getCodeAgence());
				}
				if(ce.getGroupe() != null){
					trs.setCodeClient(ce.getGroupe().getCodeGrp());
					trs.setCodeAgence(ce.getGroupe().getAgence().getCodeAgence());					
				}
				
				transaction.begin();
				em.flush();
				em.persist(trs);
				em.remove(tr);
				transaction.commit();
				em.refresh(trs); 
				em.refresh(ce); 
				System.out.println("Transaction supprimé!");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erreur suppression transaction!");
				return false;
			}			
		}

		//----------------------------------------------------------------------------------------------------------

	//calcul difference entre deux date retrait
	static long calculDiffDate(String dateDeb, String dateFin) {
		try {			
			LocalDate date1 = LocalDate.parse(dateDeb);
			LocalDate date2 = LocalDate.parse(dateFin);
			System.out.println("dernière transaction "+date1);
			System.out.println("date retrait "+date2);
	
			long val = ChronoUnit.DAYS.between(date1, date2);			
			System.out.println(val + "\n");			
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/***
	 * VIREMENT
	 * ***/

	@SuppressWarnings("unused")
	@Override
	public boolean virement(String cmpt1, String cmpt2, double montant,String pieceCompta,String  dateTransac, 
			String typPaie,String numTel, String numCheq,String nomCptCaisse,int user) {
		
		//valeur de retour
		String result = "";
		
		//Instance des classes necessaire
		//utilisateur de saisie
		Utilisateur ut = em.find(Utilisateur.class, user);
		
		///	pour incrémenter le tcode dans la table grandlivre
		//compte caisse
		Caisse cptCaisse = em.find(Caisse.class, nomCptCaisse);
		String cptC = String.valueOf(cptCaisse.getAccount().getNumCpt());
		
		//Transaction
		TransactionEpargne retirer = new TransactionEpargne();
		TransactionEpargne depot = new TransactionEpargne();
		
		//COMPTE A RETIRER
		CompteEpargne cp1 = em.find(CompteEpargne.class, cmpt1);
		//COMPTE A VERSER
		CompteEpargne cp2 = em.find(CompteEpargne.class, cmpt2);
		
		//tester si le compte est actif ou non
		if(cp1.getIsActif() == true && cp2.getIsActif() == true){
		
			//tester si le solde du compte a retirer et inferieur au montant
			if(cp1.getSolde() < montant){
				result = "Solde du compte "+cmpt1+" est inferieur au montant";
				return false;
			}				
			else{
				
				//Initialisation des informations sur les transactions
				retirer.setDateTransaction(dateTransac);
				retirer.setTypeTransEp("RE");
				retirer.setMontant(montant);
				retirer.setDescription("Virement vers le compte "+ cmpt2);
				retirer.setPieceCompta(pieceCompta);
				retirer.setCompteEpargne(cp1);	
				retirer.setCommTrans(cp1.getProduitEpargne().getConfigProdEp().getCommTransfer());
				retirer.setTypePaie(typPaie);
				
				depot.setDateTransaction(dateTransac);
				depot.setTypeTransEp("DE");
				depot.setMontant(montant);
				depot.setDescription("Virement via de compte "+ cmpt1);
				depot.setPieceCompta(pieceCompta);				
				depot.setCompteEpargne(cp2);
				depot.setTypePaie(typPaie);
				
				//Type de payement 
				if(typPaie.equalsIgnoreCase("cash")){
					retirer.setCaisse(cptCaisse);		
					depot.setCaisse(cptCaisse);
				}else if(typPaie.equalsIgnoreCase("cheque")){
					retirer.setValPaie(numCheq);	
					depot.setValPaie(numCheq);	
				}else if(typPaie.equalsIgnoreCase("mobile")){
					retirer.setValPaie(numTel);	
					depot.setValPaie(numTel);	
				}
				
				//Enregistrement de la transaction
				if(retirer.getCompteEpargne() != null && depot.getCompteEpargne() != null){
					System.out.println("Informations sur la transaction prêtes");
					try {						
						if(retirer.getTypeTransEp().equals("RE")){
							
							Individuel ind = null;
							Groupe grp = null;
							
							if(cp1.getIndividuel() != null)
								ind = cp1.getIndividuel();
							if(cp1.getGroupe() != null)
								grp = cp1.getGroupe();
							
							String indexTcode = CodeIncrement.genTcode(em);
							retirer.setIdTransactionEp(indexTcode);
							
							//Ajout compte comptable au grand Livre

							//Si montant de commission lors de la configuration du produit est different de 0 
							if(cp1.getProduitEpargne().getConfigProdEp().getCommTransfer() != 0){
								
								Account accGl = CodeIncrement.getAcount(em, 
										cp1.getProduitEpargne().getConfigGlEpargne().getCommEpargne());
								
								ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Commission sur transfer de compte "+cmpt1+" vers "+cmpt2,
										pieceCompta, 0, cp1.getProduitEpargne().getConfigProdEp().getCommTransfer(), ut, ind, grp, cp1, null, accGl);						
							}
							
							//Débit
							Account AcDebit = CodeIncrement.getAcount(em, cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Virement de compte "+cmpt1+" vers "+cmpt2,
									pieceCompta, 0, montant, ut, ind, grp, cp1, null, AcDebit);
							
							Account accCredit = null;							
							//Ajout compte comptable au grand livre
							if(typPaie.equalsIgnoreCase("cash")){
								
								accCredit = cptCaisse.getAccount();								
							}else if(typPaie.equalsIgnoreCase("cheque")){
								
								accCredit = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());								
							}else if(typPaie.equalsIgnoreCase("mobile")){	
								
								accCredit = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());								
							}
							
							ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Virement de compte "+cmpt1+" vers "+cmpt2,
									pieceCompta, montant, 0, ut, ind, grp, cp1, null, accCredit);
													
							transaction.begin();
							em.persist(retirer);
							transaction.commit();
							em.refresh(retirer);
						}
						
						if(depot.getTypeTransEp().equals("DE")){
							
							String indexTcode = CodeIncrement.genTcode(em);
							
							Individuel ind = null;
							Groupe grp = null;
							
							if(cp2.getIndividuel() != null)
								ind = cp2.getIndividuel();
							if(cp2.getGroupe() != null)
								grp = cp2.getGroupe();
	
							//Compte comptable à débiter							
							Account accDb = null;
							
							//Ajout compte comptable en fonction de mode de payment
							//si mode de payement est cash
							if(typPaie.equalsIgnoreCase("cash")){
								accDb = cptCaisse.getAccount();
							
								//Si mode payement est chèque
							}else if(typPaie.equalsIgnoreCase("cheque")){
								accDb = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());
															
								//si mode de payement est mobile
							}else if(typPaie.equalsIgnoreCase("mobile")){
								accDb = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
							}
							
							ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Virement de compte "+cmpt1+" vers "+cmpt2,
									pieceCompta, 0, montant, ut, ind, grp, cp2, null, accDb);
							
							//Compte comptable à créditer
							
							Account accCredits=CodeIncrement.getAcount(em,
									cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							
							ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateTransac, "Virement de compte "+cmpt1+" vers "+cmpt2,
									pieceCompta, montant, 0, ut, ind, grp, cp2, null, accCredits);
						
							depot.setIdTransactionEp(indexTcode);
							
							transaction.begin();
							em.persist(depot);
							transaction.commit();
							em.refresh(depot);
							
						}
						System.out.println("Transaction réussie");
						
						 result = "Virement reussit!!!";
						 return true;
					} catch (Exception e) {
						System.err.println(e.getMessage());
						 result ="erreur de virement";
						 return false;
					}		
				}
			}
		}
		else{
			 result = "Désolé, vous ne pouvez pas faire cette action,le compte "+cmpt1+" est inactif";
			 return false;
		}
		
		return false;
	}
	
	//---------------------------------------------------------------------------------------------------
	//Enregistrement modification compte
	@Override
	public boolean updateCompte(String numCompte, String date) {
		CompteEpargne c = em.find(CompteEpargne.class, numCompte);
		LocalDate d1 = LocalDate.parse(date);
		LocalDate d2 = LocalDate.parse(c.getDateOuverture());
		Long diff = ChronoUnit.DAYS.between(d2, d1);
		if(diff <= 0){
			c.setDateOuverture(date); 
			try {
				transaction.begin();
				em.merge(c);
				transaction.commit();
				em.refresh(c);
				System.out.println("Modification compte reussit");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erreur modification compte");
				return false;
			}
		}else{
			return false;
		}
	}
	
	static List<CompteEpargne> getCompteDAVDist(String dateDeb, String dateFin){
		String sql = "select distinct t.compteEpargne from TransactionEpargne t where t.dateTransaction "
				+ "between '" + dateDeb	+ "' and '" + dateFin + "'";
		TypedQuery<CompteEpargne> query = em.createQuery(sql, CompteEpargne.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}
	
	/***
	 * CALCUL INTERET DAV
	 * ***/
	@Override
	public List<InteretEpargne> calcInteret(String date1) { 
	
		//valeur de retour de la methode
		List<InteretEpargne> retour = new ArrayList<InteretEpargne>();
		
		org.joda.time.LocalDate now = org.joda.time.LocalDate.now();
		String dateDebMois = now.toString().substring(0, 8);
		int d = now.dayOfMonth().getMinimumValue();
		dateDebMois += d;
		
		String dateFinMois = now.toString().substring(0, 8);
		int f = now.dayOfMonth().getMaximumValue();
		dateFinMois += f;
		System.out.println("début mois: "+ dateDebMois); 
		System.out.println("fin mois: "+ dateFinMois); 
		
		if(date1.equalsIgnoreCase(dateFinMois)){
			try {
				CounterExecution ct = new CounterExecution();
				ct.setDate(date1);
				ct.setExec(true);
				transaction.begin();
				em.persist(ct);
				transaction.commit();
				em.refresh(ct); 
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			List<CompteEpargne> cmpts = getCompteDAVDist(dateDebMois, dateFinMois);
			for (CompteEpargne compteEpargne : cmpts) {
				ConfigInteretProdEp confI = compteEpargne.getProduitEpargne().getConfigInteretProdEp();
				if(confI.getModeCalcul().equalsIgnoreCase("Solde moyen mensuel")){
					
					//Classe interetEpargne pour historiser l'intérêt d'une compte après le calcul
					InteretEpargne saveInt = new InteretEpargne();										
					String numCpt = compteEpargne.getNumCompteEp();
										
					TypedQuery<TransactionEpargne> quer2 = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
							+ "WHERE c.numCompteEp=:id AND t.dateTransaction < :dateDeb ",TransactionEpargne.class);
					quer2.setParameter("id", numCpt);
					quer2.setParameter("dateDeb", dateDebMois);
					
					if(!quer2.getResultList().isEmpty()){
						
						String sq1 = "select t.solde from TransactionEpargne t "
								+ "WHERE t.dateTransaction = (SELECT MIN(tr.dateTransaction) FROM TransactionEpargne tr "
								+ "JOIN tr.compteEpargne c WHERE c.numCompteEp='"+numCpt+"' AND "
								+ "tr.dateTransaction BETWEEN '"+dateDebMois+"' AND '"+dateFinMois+"')";
						
						Query q = em.createQuery(sq1);
						double soldMin = 0.0;		
						if(q.getSingleResult() != null){
							soldMin = (double) q.getSingleResult();
						}else
							soldMin = 0.0;
						
						String sq2 = "select t.solde from TransactionEpargne t "
								+ "WHERE t.dateTransaction = (SELECT MAX(tr.dateTransaction) FROM TransactionEpargne tr "
								+ "JOIN tr.compteEpargne c WHERE c.numCompteEp='"+numCpt+"' AND "
								+ "tr.dateTransaction BETWEEN '"+dateDebMois+"' AND '"+dateFinMois+"')";
						
						Query q2 = em.createQuery(sq2);
						double soldMax = 0.0;		
						if(q2.getSingleResult() != null){
							soldMax = (double) q2.getSingleResult();
						}else
							soldMax = 0.0;
						
						double interet = (((soldMin + soldMax) / 2) * confI.getTauxInteret() / 100);	
						
						saveInt.setDate(dateFinMois);
						saveInt.setMontant(interet);
						saveInt.setCompte(compteEpargne);
						saveInt.setSolde(compteEpargne.getSolde() + interet);
						compteEpargne.setSolde(compteEpargne.getSolde() + interet);
						
						/*((Solde au début du mois + Solde à la fin du mois) / 2) x Taux d’intérêt annuel / 100) 
						  Nombre de mois dans l’année) */ 
						
						try {
							transaction.begin();
							em.flush();
							em.persist(saveInt);
							transaction.commit();
							em.refresh(saveInt); 
							retour.add(saveInt);
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}else{
						//double interet = 0.0;
						System.out.println("L'intérêt de ce compte pour mois ci est 0.0");
					}
					
					//System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interet);				
				}
			}
		}
	
		return retour;
	}
	
	//----------------------------------------------------------------------------------------------------------
	
  /***
  * Rapport transaction d'épargne
  * ***/
	@Override
	public List<TransactionEpargne> rapportTransactions(String agence, String client, String type,
			String idProd, String trans, String dateDeb, String dateFin) {
		List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
		
		//si les critères sont null
		String sql = "SELECT t FROM TransactionEpargne t ";		
		
		//si les critères ne sont pas null
		if(!agence.equals("") || !client.equals("") || !type.equals("") || !idProd.equals("") 
				|| !trans.equals("") ||	!dateDeb.equals("") || !dateFin.equals("") ){
		
			String ag = agence;

			String ind = "";
			String grp = "";
			if (client.equalsIgnoreCase("individuel")) {
				ind = client;
				grp = "";
			} else if (client.equalsIgnoreCase("groupe")) {
				grp = client;
				ind = "";
			}

			sql += "join t.compteEpargne e join e.produitEpargne p join p.typeEpargne tp ";

			// Test agence, client, produit , dateDeb, dateFin non vide
			if (!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null ";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null ";
				}

				sql += "and e.numCompteEp like '" + ag
						+ "%' and  p.idProdEpargne = '" + idProd
						+ "' and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'" + " and tp.abrev = '"
						+ type + "'";
			}
			

			//Agence , client , produit
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")){
					
					if(!ind.equals("") && grp.equals("")){
						sql += "join e.individuel i where e.individuel is not null ";					
					}
					if(ind.equals("") && !grp.equals("")){
						sql += "join e.groupe g where e.groupe is not null ";			
					}
					
					sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' "
							+ " and tp.abrev = '"+ type +"'";
				}
			
			
			// agence, client, produit , dateDeb, dateFin, numCompte non vide
			if (!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null ";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null ";
				}

				sql += "and e.numCompteEp like '" + ag
						+ "%' and  p.idProdEpargne = '" + idProd
						+ "' and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'" + " and tp.abrev = '"
						+ type + "' and t.typeTransEp = '" + trans + "'";
			}

			// Agence non vide
			if (!agence.equals("") && client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {
				// join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '" + ag + "%' "
						+ "and tp.abrev = '" + type + "'";
			}
			
			// Agence et Typr transaction non vide
			if (!agence.equals("") && client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {
				// join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '" + ag + "%' "
						+ "and tp.abrev = '" + type + "' and t.typeTransEp = '" + trans + "'";
			}

			// Agence et client non vide
			if (!agence.equals("") && !client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"
							+ type + "'";
				}
				sql += " and e.numCompteEp like '" + ag + "%'";
			}
			
			// Agence et client et numCompte non vide
			if (!agence.equals("") && !client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"
							+ type + "'";
				}
				sql += " and e.numCompteEp like '" + ag + "%' and t.typeTransEp = '" + trans + "'";
			}

			// Agence et client , dateDeb et dateFin non vide
			if (!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.numCompteEp like '"
							+ ag + "%' and e.individuel is not null "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'" + " and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.numCompteEp like '" + ag
							+ "%' and e.groupe is not null "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'" + " and tp.abrev = '"
							+ type + "'";
				}
			}
			
			// Agence et client , dateDeb et dateFin et numCompte non vide
			if (!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.numCompteEp like '"
							+ ag + "%' and e.individuel is not null "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'" + " and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.numCompteEp like '" + ag
							+ "%' and e.groupe is not null "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'" + " and tp.abrev = '"
							+ type + "' and t.typeTransEp = '" + trans + "'";
				}
			}

			// agence , produit non vide
			if (!agence.equals("") && client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {

				sql += "where e.numCompteEp like '"
						+ ag
						+ "%' "
						+ "and  p.idProdEpargne = '"
						+ idProd
						+ "' and tp.abrev = '" + type + "'";
			}
			
			// agence , produit et numCompte non vide
			if (!agence.equals("") && client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {

				sql += "where e.numCompteEp like '"
						+ ag
						+ "%' "
						+ "and  p.idProdEpargne = '"
						+ idProd
						+ "' and tp.abrev = '" + type + "' and t.typeTransEp = '" + trans + "'";
			}

			// agence, produit, periode non vide
			if (!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {

				sql += "where e.numCompteEp like '"
						+ ag + "%' " + "and  p.idProdEpargne = '"+ idProd + "' and t.dateTransaction between '"
						+ dateDeb+ "' and '"+ dateFin+ "'"+ " and tp.abrev = '"
						+ type + "'";
			}
			
			// agence, produit, periode et numCompte non vide
			if (!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {

				sql += "where e.numCompteEp like '"
						+ ag + "%' " + "and  p.idProdEpargne = '"+ idProd + "' and t.dateTransaction between '"
						+ dateDeb+ "' and '"+ dateFin+ "'"+ " and tp.abrev = '"
						+ type + "' and t.typeTransEp = '" + trans + "'";
			}

			// Client non vide
			if (!client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"
							+ type + "'";
				}
			}
			// Client et numCompte non vide
			if (!client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"
							+ type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"
							+ type + "' and t.typeTransEp = '" + trans + "'";
				}
			}

			// Client , produit non vide
			if (!client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"
							+ idProd
							+ "' and tp.abrev = '" + type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and  p.idProdEpargne = '" + idProd
							+ "' and tp.abrev = '" + type + "'";
				}
			}
			
			// Client , produit et numCompte non vide
			if (!client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"
							+ idProd
							+ "' and tp.abrev = '" + type + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and  p.idProdEpargne = '" + idProd
							+ "' and tp.abrev = '" + type + "' and t.typeTransEp = '" + trans + "'";
				}
			}

			// Client , produit et dates non vide
			if (!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"
							+ idProd
							+ "' and tp.abrev = '"
							+ type
							+ "' "
							+ "and t.dateTransaction between '"
							+ dateDeb
							+ "' and '" + dateFin + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and p.idProdEpargne = '" + idProd
							+ "' and tp.abrev = '" + type + "' "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'";
				}
			}
			
			// Client , produit et dates et numCompte non vide
			if (!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"
							+ idProd
							+ "' and tp.abrev = '"
							+ type
							+ "' "
							+ "and t.dateTransaction between '"
							+ dateDeb
							+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and p.idProdEpargne = '" + idProd
							+ "' and tp.abrev = '" + type + "' "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
				}
			}

			// Client et dates non vide
			if (!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and tp.abrev = '"
							+ type
							+ "' "
							+ "and t.dateTransaction between '"
							+ dateDeb
							+ "' and '" + dateFin + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and tp.abrev = '" + type + "' "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "'";
				}
			}
			
			// Client et dates et numCompte non vide
			if (!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {

				if (!ind.equals("") && grp.equals("")) {
					sql += "join e.individuel i where e.individuel is not null "
							+ "and tp.abrev = '"
							+ type
							+ "' "
							+ "and t.dateTransaction between '"
							+ dateDeb
							+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
				}
				if (ind.equals("") && !grp.equals("")) {
					sql += "join e.groupe g where e.groupe is not null "
							+ "and tp.abrev = '" + type + "' "
							+ "and t.dateTransaction between '" + dateDeb
							+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
				}
			}


			// produit non vide
			if (client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && trans.equals("")) {

				sql += "where p.idProdEpargne = '" + idProd
						+ "' and tp.abrev = '" + type + "'";
			}
			
			// produit et numCompte non vide
			if (client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {
	
				sql += "where p.idProdEpargne = '" + idProd
						+ "' and tp.abrev = '" + type + "' and t.typeTransEp = '" + trans + "'";
			}

			// produit et dates non vide
			if (client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {
				sql += "where p.idProdEpargne = '" + idProd
						+ "' and tp.abrev = '" + type + "' "
						+ "and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'";
			}
			
			// produit et dates et numCompte non vide
			if (client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {
				sql += "where p.idProdEpargne = '" + idProd
						+ "' and tp.abrev = '" + type + "' "
						+ "and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
			}
			
			// numCompte non vide
			if (client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("") && !trans.equals("")) {
				sql += "where tp.abrev = '" + type + "' and t.typeTransEp = '" + trans + "'";
			}
			
			//dates et numCompte non vide
			if (client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && !trans.equals("")) {
				sql += "where tp.abrev = '" + type + "' "
						+ "and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "' and t.typeTransEp = '" + trans + "'";
			}

			// dates non vide
			if (client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("") && trans.equals("")) {
				sql += "where tp.abrev = '" + type + "' "
						+ "and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'";
			}

			if (agence.equals("") && client.equals("") && !type.equals("")
					&& idProd.equals("") && dateDeb.equals("")
					&& dateFin.equals("") && trans.equals("")) {

				sql += "where tp.abrev = '" + type + "'";
			}
			
		} 
		
		System.out.println("Sql transanction = "+sql);
		
		TypedQuery<TransactionEpargne> q = em.createQuery(sql,TransactionEpargne.class);
		if(!q.getResultList().isEmpty()){
			result = q.getResultList();
		}	
		return result;
	}
	
	//Rapport transaction supprimé
	@Override
	public List<TransactionEpargneSupp> getTransactionSupprimer(String agence, String idProd,
			String dateDeb, String dateFin) {
		List<TransactionEpargneSupp> result = new ArrayList<TransactionEpargneSupp>();
		
		//si les critères sont null 
		String sql = "SELECT t FROM TransactionEpargneSupp t ";		
		
		//si les critères ne sont pas null
		if(!agence.equals("") || !idProd.equals("") ||	!dateDeb.equals("") || !dateFin.equals("") ){
		
			String ag = agence;

			sql += "where ";

			// Test agence, client, produit , dateDeb, dateFin non vide
			if (!agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")) {

				sql += " t.codeAgence = '" + ag + "' and  t.idProduit = '" + idProd
						+ "' and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'";
			}
			
			//Agence
			if(!agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
					sql += " t.codeAgence = '" + ag + "'";
				}

			//Agence , client , produit
			if(!agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
					sql += " t.codeAgence = '" + ag + "' and  t.idProduit = '" + idProd	+ "'";
				}
			
			
			// agence, produit , dateDeb, dateFin non vide
			if (!agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")) {

				sql += "t.codeAgence = '" + ag + "' and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'";
			}

			// Produit
			if (agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")) {
				
				sql += "t.idProduit = '" + idProd	+ "'";
			}
			
			// Agence te numCompte non vide
			if (agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")) {
				
				sql += "t.idProduit = '" + idProd	+ "' and t.dateTransaction between '" + dateDeb
						+ "' and '" + dateFin + "'";
			}

			// Agence et client non vide
			if (agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")) {

				sql += " t.dateTransaction between '" + dateDeb	+ "' and '" + dateFin + "'";
			}
						
		} 
		
		System.out.println("Sql transanction supprimé = "+sql);
		
		TypedQuery<TransactionEpargneSupp> q = em.createQuery(sql, TransactionEpargneSupp.class);
		if(!q.getResultList().isEmpty()){
			result = q.getResultList();
		}	
		return result;
	}


	/***
	 * CHERCHER COMPTE PAR CODE CLIENT
	 * ***/
	@Override
	public List<CompteEpargne> findByCodeCli(String codeInd, String codeGrp) {
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		
		if(codeInd.equals("")){
			TypedQuery<CompteEpargne> q =em.createQuery("SELECT c FROM CompteEpargne c "
					+ "JOIN c.groupe g WHERE g.codeGrp= :code",CompteEpargne.class);
			q.setParameter("code", codeGrp);			
			if(!q.getResultList().isEmpty()){
				result = q.getResultList();
			}
		}else{
			TypedQuery<CompteEpargne> q =em.createQuery("SELECT c FROM CompteEpargne c "
					+ "JOIN c.individuel i WHERE i.codeInd= :code",CompteEpargne.class);
			q.setParameter("code", codeInd);			
			if(!q.getResultList().isEmpty()){
				result = q.getResultList();
			}
		}
		
		return result;
	}

	/***
	 * FERMER COMPTE
	 * ***/	 
	@Override
	public boolean fermerCompte(CompteFerme compFerme, String numCmpt) {
		CompteEpargne cmpt = em.find(CompteEpargne.class, numCmpt);
		
		try {	
			cmpt.setFermer(true);
			cmpt.setActif(false);
			compFerme.setCompteEpargne(cmpt);
			cmpt.setPasRetrait(true); 
			transaction.begin();
			em.flush();
			em.persist(compFerme);
			transaction.commit();
			em.refresh(cmpt);
			em.refresh(compFerme); 
			System.out.println("Enregistrement reussit");
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur enregistrement");
			return false;
		}
	}   
	
	//Supprimer compte épargne
	@Override
	public boolean deleteCompte(String numCmpt, int user) {
	
		CompteEpargne c = em.find(CompteEpargne.class, numCmpt);
		Utilisateur ut = em.find(Utilisateur.class, user);
		CompteEpargneSupp cs = new 
				CompteEpargneSupp(c.getNumCompteEp(), c.getDateEcheance(), c.getDateOuverture(), 
						c.getIsActif(), c.getSolde(), c.isFermer(), c.isComptGeler(), c.isPasRetrait(),
						c.isPrioritaire(), c.getProduitEpargne().getIdProdEpargne(), ut.getNomUtilisateur());
		if(c.getIndividuel() != null){
			cs.setCodeClient(c.getIndividuel().getCodeInd());
			cs.setNomClient(c.getIndividuel().getNomClient() +" "+ c.getIndividuel().getPrenomClient());
		}
		if(c.getGroupe() != null){
			cs.setCodeClient(c.getGroupe().getCodeGrp());
			cs.setNomClient(c.getGroupe().getNomGroupe()); 
		}
		try {
			transaction.begin();
			em.persist(cs);
			em.remove(c);
			transaction.commit();
			em.refresh(cs); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}

	//Liste comptes épargne supprimé
	@Override
	public List<CompteEpargneSupp> getCompteSupprimer() {
		String sql = "select c from CompteEpargneSupp c";
		TypedQuery<CompteEpargneSupp> q = em.createQuery(sql, CompteEpargneSupp.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}
	
	//Get max date transaction
	public static String getMaxDateTransaction(String numCompte){
		Query q = em.createQuery("SELECT MAX(t.dateTransaction) FROM TransactionEpargne t JOIN t.compteEpargne cp "
				+ " WHERE cp.numCompteEp = :x ");
		q.setParameter("x", numCompte);		
		
		String dateMax = "";
		if(q.getSingleResult() != null){
			dateMax = (String) q.getSingleResult();
		}else
			dateMax = "";
		return dateMax;
	}
	
	//Mettre comptes en inactif
		@Override
		public List<CompteEpargne> desactiverCompte(String date) {
			List<CompteEpargne> compteEp = new ArrayList<CompteEpargne>();
			
			for (CompteEpargne c : getCompteDistinc("", "", "DAV", "", "", "")) {
				String numCmpt = c.getNumCompteEp();
				System.out.println("Numéro compte épargne : "+numCmpt);
				
				//Récuperation date du dernière transaction
				String dateTrans = getMaxDateTransaction(numCmpt);
				//Calcul difference entre date dernière transaction et date aujourd'hui
				int diffDate = (int) calculDiffDate(dateTrans, date);
				
				//Nombre de jour maximum pour mettre un compte comme inactif
				int nbJrMax = c.getProduitEpargne().getConfigProdEp().getNbrJrIn();
				
				if(diffDate > nbJrMax){
					try {
						c.setActif(false);
						transaction.begin();
						em.flush();
						transaction.commit();
						em.refresh(c);
						compteEp.add(c);
					} catch (Exception e) {
						e.printStackTrace();
						compteEp = null;
					}
				}
				
			}
			
			System.out.println("Liste des comptes inactif le : "+date);
			for (CompteEpargne compteEpargne : compteEp) {
				System.out.println("Numéro compte : "+compteEpargne.getNumCompteEp());				
			}
			
			return compteEp;
		}

		//Reactiver un compte
		@Override
		public boolean activerCompte(String numCompte, String date, double frais,
				int user, String pieceCompta, String typPaie, String numTel,
				String compteCaisse) {
			
			CompteEpargne c = em.find(CompteEpargne.class, numCompte);
			Utilisateur ut = em.find(Utilisateur.class, user);
			
			Individuel ind = null;
			Groupe grp = null;
			
			if(c.getIndividuel() != null)
				ind = c.getIndividuel();
			if(c.getGroupe() != null)
				grp = c.getGroupe();

			try {
				
				/************ Imputation compta *************************/
				String indexTcode = CodeIncrement.genTcode(em);
				String desc = "Activation du compte "+c.getNumCompteEp();
				
				String numAccountCredit = c.getProduitEpargne().getConfigGlEpargne().getActivationCompte();
				
				Account cred = CodeIncrement.getAcount(em, numAccountCredit);
				
				String vp = "";
				Caisse caisse = null;
				if(typPaie.equalsIgnoreCase("cash")){
					caisse = em.find(Caisse.class, compteCaisse);
					Account ac = caisse.getAccount();				
					//Débit
					ComptabliteServiceImpl.saveImputationEpargne(indexTcode, date, desc, pieceCompta, 
							0, frais, ut, ind, grp, c, null, ac);
					
					vp = String.valueOf(ac.getNumCpt());

				}else if(typPaie.equalsIgnoreCase("mobile")){
					String cmptMobil = c.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt();
					Account ac = CodeIncrement.getAcount(em, cmptMobil);
					
					//Débit
					ComptabliteServiceImpl.saveImputationEpargne(indexTcode, date, desc, pieceCompta, 
							0, frais, ut, ind, grp, c, null, ac);
					
					vp = numTel;
				}
				
				//Crédit
				ComptabliteServiceImpl.saveImputationEpargne(indexTcode, date, desc, pieceCompta, 
						frais, 0, ut, ind, grp, c, null, cred);
				
				/***************** Enregistrement transaction si solde du compte supérieur à 0 *************/
				
				if(c.getSolde() > frais){
					//Enregistrement de la transaction
					
					double sd = c.getSolde() - frais;
					
					TransactionEpargne trans = new TransactionEpargne(indexTcode, date,
							"Frais activation compte", frais, pieceCompta, "RE", typPaie, vp, 0, 0, 0, c, ut);
					trans.setCaisse(caisse);
					c.setSolde(sd);
					trans.setSolde(sd); 
					try {
						transaction.begin();
						em.flush();
						em.persist(trans);
						transaction.commit();
						em.refresh(trans); 
						System.out.println("transaction enregistré");
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Erreur enregistrement transaction");
					}
				}
				
				/************ Mise à jour du compte épargne *******************/
				c.setActif(true);
				transaction.begin();
				em.flush();
				transaction.commit();
				System.out.println("Compte "+ c.getNumCompteEp() +" activé");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erreur activation compte");
				return false;				
			}
		}
		
		
		//Recuperer tous les comptes inactif
		@Override
		public List<CompteEpargne> getCompteEpargne(boolean etat) {
			String sql ="select c from CompteEpargne c where c.isActif='"+ etat +"'";
			TypedQuery<CompteEpargne> q = em.createQuery(sql, CompteEpargne.class);
			if(!q.getResultList().isEmpty())
				return q.getResultList();
			return null;
		}
		
		
		//Chercher compte épargne par code client et type d'épargne
		@Override
		public List<CompteEpargne> findCompteEpByType(String typeEp,
				String codeInd, String codeGrp) {
			System.out.println("Type épargne : "+ typeEp);
			System.out.println("codeInd : "+ codeInd);
			System.out.println("codeGrp : "+ codeGrp);
			
			String sql = "SELECT c FROM CompteEpargne c  ";
			
			if(!codeInd.equals("") && codeGrp.equals("")){
				sql += "JOIN c.produitEpargne p JOIN p.typeEpargne t JOIN c.individuel i WHERE "
						+ "c.isActif = '1' and c.fermer='0' and t.abrev ='"+ typeEp +"' AND i.codeInd ='"+ codeInd +"'";
			}
			
			if(!codeGrp.equals("") && codeInd.equals("")){
				sql += "JOIN c.produitEpargne p JOIN p.typeEpargne t JOIN c.groupe g WHERE"
						+ " c.isActif = '1' and c.fermer='0' and t.abrev ='"+ typeEp +"' AND g.codeGrp = '"+ codeGrp +"'";
			}
			
			System.out.println("requete : "+ sql);
			
			TypedQuery<CompteEpargne> q = em.createQuery(sql, CompteEpargne.class);
			
			if(!q.getResultList().isEmpty()){
				System.out.println("Match result");
				List<CompteEpargne> c = q.getResultList();
				for (CompteEpargne compteEpargne : c) {
					System.out.println("Num compte : "+ compteEpargne.getNumCompteEp());
				}
				return q.getResultList();
			}
			System.out.println("No result");
			return null;
		}


	/***
	 * Rapport par produit
	 * ***/
	@Override
	public List<CompteEpargne> rapportParProduit(String idProd) {
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		
//		ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
//		
//		List<CompteEpargne> cp = p.getCompteEpargnes();
//		
//		for (int i = 0; i < cp.size(); i++) {
//			
//			System.out.println(cp.get(i).getNumCompteEp() +" solde :"+cp.get(i).getSolde());
//			
//		}
//			
		if(idProd.equalsIgnoreCase("tout")){
			TypedQuery<CompteEpargne> q = em.createQuery("SELECT c FROM CompteEpargne c",CompteEpargne.class);
			if(!q.getResultList().isEmpty()){
				result = q.getResultList();
				return result;
			}
		}else{
			TypedQuery<CompteEpargne> q = em.createQuery("SELECT c FROM CompteEpargne c JOIN c.produitEpargne p "
					+ "WHERE p.idProdEpargne = :x",CompteEpargne.class);
			q.setParameter("x", idProd);
			if(!q.getResultList().isEmpty()){
				result = q.getResultList();
				return result;
			}
		}
		
		return null;
	}
	
	
	// selection des comptes
	static List<CompteEpargne> getCompteDistinc(String agence,String client,String type , String idProd,
			String dateDeb, String dateFin){
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		String sql="select distinct t.compteEpargne from TransactionEpargne t ";
		
		
		if(!agence.equals("") || !client.equals("") || !type.equals("") || !idProd.equals("")
				|| !dateDeb.equals("") || !dateFin.equals("")){
			
			String ag = agence;
			
			String ind = "";
			String grp = "";
			if(client.equalsIgnoreCase("individuel")){
				ind = client;
				grp = "";
			}else if(client.equalsIgnoreCase("groupe")){
				grp = client;
				ind = "";
			}
			
			sql += "join t.compteEpargne e join e.produitEpargne p join p.typeEpargne tp ";
					
			
			//Test agence, client, produit , dateDeb, dateFin non vide
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
				&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null ";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null ";			
				}
				
				sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";
			}
			
			//Agence , client , produit
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
					if(!ind.equals("") && grp.equals("")){
						sql += "join e.individuel i where e.individuel is not null ";					
					}
					if(ind.equals("") && !grp.equals("")){
						sql += "join e.groupe g where e.groupe is not null ";			
					}
					
					sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' "
							+ " and tp.abrev = '"+ type +"'";
				}
			
			//Agence , client , produit , une date
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
					
					if(!ind.equals("") && grp.equals("")){
						sql += "join e.individuel i where e.individuel is not null ";					
					}
					if(ind.equals("") && !grp.equals("")){
						sql += "join e.groupe g where e.groupe is not null ";			
					}
					//List<E>
					sql += "and e.numCompteEp like '"+ ag +"%' and t.dateTransaction <= '"+dateDeb+"' "
							+ " and  p.idProdEpargne = '"+idProd+"' "
							+ " and tp.abrev = '"+ type +"'";
				}
			
			//Agence non vide
			if(!agence.equals("") && client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				//join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '"+ ag +"%' "
						+ "and tp.abrev = '"+ type +"'";
			}
			
			//Agence, une date non vide
			if(!agence.equals("") && client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				//join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '"+ ag +"%' "
						+ " and t.dateTransaction <= '"+dateDeb+"' and tp.abrev = '"+ type +"'";
			}
			
			//Agence et client non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
				sql +=" and e.numCompteEp like '"+ ag +"%'";
			}
			
			//Agence et client, une date non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
				sql +=" and t.dateTransaction <= '"+dateDeb+"' and e.numCompteEp like '"+ ag +"%'";
			}
			
			//Agence et client , dateDeb et dateFin non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.numCompteEp like '"+ ag +"%' and e.individuel is not null "
							+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.numCompteEp like '"+ ag +"%' and e.groupe is not null "
							+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";			
				}
			}
			
			//agence , produit non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//agence , produit, une date non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ " and t.dateTransaction <= '"+dateDeb+"' and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//agence, produit, periode non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'"
								+ " and tp.abrev = '"+ type +"'";
			}
			
			//Client non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
			}
			
			//Client, une date non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and t.dateTransaction <= '"+dateDeb+"' and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and t.dateTransaction <= '"+dateDeb+"' and tp.abrev = '"+ type +"'";			
				}
			}
			
			//Client , produit non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";			
				}	
			}
			
			//Client , produit, une date non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and t.dateTransaction <= '"+dateDeb+"' and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and t.dateTransaction <= '"+dateDeb+"' and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";			
				}	
			}
			
			//Client , produit et dates non vide non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//Client et dates non vide non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//produit non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				
					sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//produit, une date non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				
				
				sql += "where p.idProdEpargne = '"+idProd+"' and t.dateTransaction <= '"+dateDeb+"' and tp.abrev = '"+ type +"'";
			}
			
			//produit et dates non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
						+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			//dates non vide
			if(client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where tp.abrev = '"+ type +"' "
						+ "and t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			//une date non vide
			if(client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && dateFin.equals("")){
				sql += "where tp.abrev = '"+ type +"' "
						+ "and t.dateTransaction <= '"+dateDeb+"'";
			}
			
			if(agence.equals("") && client.equals("") && !type.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				sql +="where tp.abrev = '"+ type +"'";
			}
			
		}
		
		System.out.println("Sql compte distinct = "+sql);
		
		TypedQuery<CompteEpargne> query = em.createQuery(sql,CompteEpargne.class);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
		}
		
		for (CompteEpargne compteEpargne : result) {
			System.out.println("compte: "+compteEpargne.getNumCompteEp());
		}
		return result;
	}
	
	/***
	 * Recuperer les soldes de chaques comptes épargne
	 * ***/
	@Override
	public List<SoldeEpargne> getSoldeEpargne(String agence, String client, String type,
			String idProd, String dateDeb) {
		List<SoldeEpargne> result = new ArrayList<SoldeEpargne>();
		
		List<CompteEpargne> compteEp = getCompteDistinc(agence, client, type, idProd, dateDeb, "");
		
		for (CompteEpargne compte : compteEp) {
			String numCmpt = compte.getNumCompteEp();
			System.out.println(numCmpt);
			SoldeEpargne donnee = new SoldeEpargne();
			
			String sql = "SELECT MAX(t.dateTransaction) FROM TransactionEpargne t JOIN t.compteEpargne cp "
					+ "WHERE cp.numCompteEp = '"+ numCmpt +"'";
			
			if(!dateDeb.equals("")){
				sql += " and t.dateTransaction <= '"+dateDeb+"'";
			}
			
			Query q = em.createQuery(sql);
			
			String dateMax = "";
			double montant = 0;
			double solde = 0;
			String trans = "";
			
			if(q.getSingleResult() != null){
				dateMax = (String) q.getSingleResult();//sum(t.montant)
				Query query = em.createQuery("select t from TransactionEpargne t join t.compteEpargne e"
						+ " where t.dateTransaction = '"+dateMax+"' and e.numCompteEp='"+compte.getNumCompteEp()+"'" );
				if(query.getSingleResult() != null){
					TransactionEpargne tr = (TransactionEpargne) query.getSingleResult();
					montant = tr.getMontant();
					solde = tr.getSolde();
					if(tr.getTypeTransEp().equalsIgnoreCase("DE"))
						trans = "Dépôt";
					if(tr.getTypeTransEp().equalsIgnoreCase("RE"))
						trans = "Retrait";
					
				}
				
				/*Query query2 = em.createQuery("select t.solde from TransactionEpargne t join t.compteEpargne e"
						+ " where t.dateTransaction = '"+dateMax+"' and e.numCompteEp='"+compte.getNumCompteEp()+"'" );
				if(query.getSingleResult() != null)//sum(t.solde)
					solde = (double) query2.getSingleResult();*/
			}
			
			String code= "";
			String nom = "";
			String fermer = "";
			
			if(compte.getIndividuel() != null){
				code = compte.getIndividuel().getCodeInd();
				nom = compte.getIndividuel().getNomClient() +" "+ compte.getIndividuel().getPrenomClient();
			}else if(compte.getGroupe() != null){
				code = compte.getGroupe().getCodeGrp();
				nom = compte.getGroupe().getNomGroupe();
			}
			String devise = "";
			if(compte.getProduitEpargne().getConfigProdEp().getDevise() != null)
				devise = compte.getProduitEpargne().getConfigProdEp().getDevise();
			
			if(compte.isFermer() == true){
				fermer = "Oui";
			}else{
				fermer = "Non";
			}
			
			
			donnee.setNumCompte(compte.getNumCompteEp());
			donnee.setCodeProd(compte.getProduitEpargne().getIdProdEpargne());
			donnee.setNomPro(compte.getProduitEpargne().getNomProdEpargne());
			donnee.setDevise(devise);
			donnee.setCodeClient(code);
			donnee.setNomClient(nom);
			donnee.setMontant(montant);
			donnee.setSolde(solde);
			donnee.setDateTrans(dateMax);
			donnee.setTypeTrans(trans); 
			donnee.setFermer(fermer);
			donnee.setSoldeFinPeriode(0);
			
			result.add(donnee);
		}
		
		return result;		
	}
	
	//Solde Min/Max
	@Override
	public List<SoldeEpargne> getSoldeMinMax(String agence, String client, String type,
			String idProd, String dateDeb, String dateFin) {
		//retour
		List<SoldeEpargne> result = new ArrayList<SoldeEpargne>();
		
		//recupération des comptes qui avait une transaction
		List<CompteEpargne> compteEp = getCompteDistinc(agence, client, type, idProd, dateDeb, dateFin);
		
		for (CompteEpargne compte : compteEp) {
			String numCmpt = compte.getNumCompteEp();
			SoldeEpargne donnee = new SoldeEpargne();
			
			double soldMin = 0;
			double soldMax = 0;
			
			String sql = "select min(t.solde) from TransactionEpargne t join t.compteEpargne cp "
					+ " where cp.numCompteEp = '"+numCmpt+"'";
			
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql += " and t.dateTransaction between '"+ dateDeb +"' and '"+ dateFin +"'";
			}		
			
			Query q = em.createQuery(sql);
			
			if(q.getSingleResult() != null)
				soldMin = (double) q.getSingleResult();
			
			String sql2 = "select max(t.solde) from TransactionEpargne t join t.compteEpargne cp "
					+ " where cp.numCompteEp = '"+numCmpt+"'";
			
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql2 += " and t.dateTransaction between '"+ dateDeb +"' and '"+ dateFin +"'";
			}	
			
			Query query = em.createQuery(sql2);
			
			if(query.getSingleResult() != null)
				soldMax = (double) query.getSingleResult();	
			
			String code= "";
			String nom = "";
			String fermer = "";
			
			if(compte.getIndividuel() != null){
				code = compte.getIndividuel().getCodeInd();
				nom = compte.getIndividuel().getNomClient() +" "+ compte.getIndividuel().getPrenomClient();
			}else if(compte.getGroupe() != null){
				code = compte.getGroupe().getCodeGrp();
				nom = compte.getGroupe().getNomGroupe();
			}
			String devise = "";
			if(compte.getProduitEpargne().getConfigProdEp().getDevise() != null)
				devise = compte.getProduitEpargne().getConfigProdEp().getDevise();
			
			donnee.setNumCompte(compte.getNumCompteEp());
			donnee.setCodeProd(compte.getProduitEpargne().getIdProdEpargne());
			donnee.setNomPro(compte.getProduitEpargne().getNomProdEpargne());
			donnee.setDevise(devise);
			donnee.setCodeClient(code);
			donnee.setNomClient(nom);
			donnee.setMontant(soldMin);
			donnee.setSolde(soldMax);
			donnee.setSoldeFinPeriode(compte.getSolde());
			donnee.setFermer(fermer);
			donnee.setDateTrans("");
			result.add(donnee);
		}
		return result;
	}

	/***
	 * Rapport intérêt d'épargne
	 * ***/
	@Override
	public List<InteretEpargne> rapportInteret(String idProd, String date1,
			String date2) {
		//valeur de retour
		List<InteretEpargne> retour = new ArrayList<InteretEpargne>();
		
		String sql = "select i from InteretEpargne i ";
		
		if(!idProd.equals("") || !date1.equals("") || !date2.equals("")){
			
			if(!idProd.equals("")){
				sql +=" join i.compte c join c.produitEpargne p where p.idProdEpargne = '"+idProd+"'";
				
				if(!date1.equals("") && !date2.equals("")){
					sql +=" and i.date between '"+date1+"' and '"+date2+"'";
				}
			}
			
			if(!date1.equals("") && !date2.equals("") && idProd.equals("")){
				sql +=" where i.date between '"+date1+"' and '"+date2+"'";
			}			
		}
		
		TypedQuery<InteretEpargne> query = em.createQuery(sql, InteretEpargne.class);
		
		if(!query.getResultList().isEmpty()){
			retour = query.getResultList();
			return retour;
		}
				
		return null;
	}

	@Override
	public List<TransactionEpargne> getReleverCompte(String numCompte,String dateDeb,String dateFin) {
		List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
		
		String sql = "select t from TransactionEpargne t join t.compteEpargne c where c.numCompteEp ='"+numCompte+"'";
		String sel = " order by t.dateTransaction desc";
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql += " and t.dateTransaction<='"+dateDeb+"'";
			sel = " ";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql += " and t.dateTransaction  between '"+dateDeb+"' and '"+dateFin+"'";
			sel = " ";
		}
		sql +=" "+sel;
		TypedQuery<TransactionEpargne> query = em.createQuery(sql,TransactionEpargne.class);
		
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
		}
				
		return result;
	}
	
	@Override
	public List<TransactionEpargne> getDemandeReleverCompte(String numCompte,
			String dateDeb, String dateFin, String dateDem, double frais, int user,
			String pieceCompta, String typPaie, String numTel,
			String compteCaisse) {
		
		CompteEpargne c = em.find(CompteEpargne.class, numCompte);
		Utilisateur ut = em.find(Utilisateur.class, user);
		
		Individuel ind = null;
		Groupe grp = null;
		
		if(c.getIndividuel() != null)
			ind = c.getIndividuel(); 
		if(c.getGroupe() != null)
			grp = c.getGroupe();

		try {
			
			/************ Imputation compta *************************/
			String indexTcode = CodeIncrement.genTcode(em);
			String desc = "Frais demande relevé "+c.getNumCompteEp();
			
			String numAccountCredit = c.getProduitEpargne().getConfigGlEpargne().getDemandRelever();
			
			Account cred = CodeIncrement.getAcount(em, numAccountCredit);
			
			String vp = "";
			Caisse caisse = null;
			if(typPaie.equalsIgnoreCase("cash")){
				caisse = em.find(Caisse.class, compteCaisse);
				Account ac = caisse.getAccount();				
				//Débit
				ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateDem, desc, pieceCompta, 
						0, frais, ut, ind, grp, c, null, ac);
				
				vp = String.valueOf(ac.getNumCpt());

			}else if(typPaie.equalsIgnoreCase("mobile")){
				String cmptMobil = c.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt();
				Account ac = CodeIncrement.getAcount(em, cmptMobil);
				
				//Débit
				ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateDem, desc, pieceCompta, 
						0, frais, ut, ind, grp, c, null, ac);
				
				vp = numTel;
			}
			
			//Crédit
			ComptabliteServiceImpl.saveImputationEpargne(indexTcode, dateDem, desc, pieceCompta, 
					frais, 0, ut, ind, grp, c, null, cred);
			
			/***************** Enregistrement transaction si solde du compte supérieur à 0 *************/
			
			if(c.getSolde() > frais){
				//Enregistrement de la transaction
				
				double sd = c.getSolde() - frais;
				
				TransactionEpargne trans = new TransactionEpargne(indexTcode, dateDem,
						"Frais demande relevé", frais, pieceCompta, "RE", typPaie, vp, 0, 0, 0, c, ut);
				trans.setCaisse(caisse);
				c.setSolde(sd);
				trans.setSolde(sd); 
				try {
					transaction.begin();
					em.flush(); 
					em.persist(trans);
					transaction.commit();
					em.refresh(trans); 
					System.out.println("transaction enregistré");
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur enregistrement transaction");
				}
			}
			
			return getReleverCompte(numCompte, dateDeb, dateFin);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur activation compte");
			return null;
		}
		
	}
	
	//Selection distinct de compte caisse dans transction epargne 
	static List<Caisse> getCompteCaisseDistinct(){
		
		List<Caisse> result = new ArrayList<Caisse>();
		
		TypedQuery<Caisse> query = em.createQuery("select distinct t.compteCaisse from TransactionEpargne t"
				,Caisse.class);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		
		return null;
	}

	//Rapport nouveau compte
	@Override
	public List<CompteEpargne> rapportNouveauCompte(String agence, String client, String type,
			String idProd, String dateDeb, String dateFin) {
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		
		String sql = "select e from CompteEpargne e ";
		
		if(!agence.equals("") || !client.equals("") || !type.equals("") || !idProd.equals("")
				|| !dateDeb.equals("") || !dateFin.equals("")){
			
			String ag = agence;
			
			String ind = "";
			String grp = "";
			if(client.equalsIgnoreCase("individuel")){
				ind = client;
				grp = "";
			}else if(client.equalsIgnoreCase("groupe")){
				grp = client;
				ind = "";
			}
			
			sql += "join e.produitEpargne p join p.typeEpargne tp ";					
			
			//Test agence, client, produit , dateDeb, dateFin non vide
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
				&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null ";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null ";			
				}
				
				sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";
			}
			
			//Agence , client , produit
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
					if(!ind.equals("") && grp.equals("")){
						sql += "join e.individuel i where e.individuel is not null ";					
					}
					if(ind.equals("") && !grp.equals("")){
						sql += "join e.groupe g where e.groupe is not null ";			
					}
					
					sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' "
							+ " and tp.abrev = '"+ type +"'";
				}
			
			
			//Agence non vide
			if(!agence.equals("") && client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				//join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '"+ ag +"%' "
						+ "and tp.abrev = '"+ type +"'";
			}
			
			//Agence et client non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
				sql +=" and e.numCompteEp like '"+ ag +"%'";
			}
			
			//Agence et client , dateDeb et dateFin non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.numCompteEp like '"+ ag +"%' and e.individuel is not null "
							+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.numCompteEp like '"+ ag +"%' and e.groupe is not null "
							+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";			
				}
			}
			
			//agence , produit non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//agence, produit, periode non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'"
								+ " and tp.abrev = '"+ type +"'";
			}
			
			//Client non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
			}
			
			//Client , produit non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";			
				}	
			}
			
			//Client , produit et dates non vide non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//Client et dates non vide non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//produit non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){				
				
					sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//produit et dates non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
						+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			//dates non vide
			if(client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where tp.abrev = '"+ type +"' "
						+ "and e.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			if(agence.equals("") && client.equals("") && !type.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				sql +="where tp.abrev = '"+ type +"'";
			}
			
		}
		
		sql +=" order by e.dateOuverture asc";
		
		System.out.println("Sql rapport nouveaux compte = "+sql);
		
		TypedQuery<CompteEpargne> query = em.createQuery(sql,CompteEpargne.class);
		
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		
		return null;
	}

	//Liste compte fermer
	@Override
	public List<CompteFerme> getCompteFermer(String agence, String client, String type,
			String idProd, String dateDeb, String dateFin) {
		List<CompteFerme> result = new ArrayList<CompteFerme>();
		
		String sql = "select c from CompteFerme c ";
		
		if(!agence.equals("") || !client.equals("") || !type.equals("") || !idProd.equals("")
				|| !dateDeb.equals("") || !dateFin.equals("")){
			
			String ag = agence;
			
			String ind = "";
			String grp = "";
			if(client.equalsIgnoreCase("individuel")){
				ind = client;
				grp = "";
			}else if(client.equalsIgnoreCase("groupe")){
				grp = client;
				ind = "";
			}
			
			sql += "join c.compteEpargne e join e.produitEpargne p join p.typeEpargne tp ";					
			
			//Test agence, client, produit , dateDeb, dateFin non vide
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
				&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null ";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null ";			
				}
				
				sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";
			}
			
			//Agence , client , produit
			if(!agence.equals("") && !client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
					if(!ind.equals("") && grp.equals("")){
						sql += "join e.individuel i where e.individuel is not null ";					
					}
					if(ind.equals("") && !grp.equals("")){
						sql += "join e.groupe g where e.groupe is not null ";			
					}
					
					sql += "and e.numCompteEp like '"+ ag +"%' and  p.idProdEpargne = '"+idProd+"' "
							+ " and tp.abrev = '"+ type +"'";
				}
			
			
			//Agence non vide
			if(!agence.equals("") && client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				//join e.individuel i join e.groupe g
				sql += " where e.numCompteEp like '"+ ag +"%' "
						+ "and tp.abrev = '"+ type +"'";
			}
			
			//Agence et client non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
				sql +=" and e.numCompteEp like '"+ ag +"%'";
			}
			
			//Agence et client , dateDeb et dateFin non vide
			if(!agence.equals("") && !client.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.numCompteEp like '"+ ag +"%' and e.individuel is not null "
							+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.numCompteEp like '"+ ag +"%' and e.groupe is not null "
							+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'"
						+ " and tp.abrev = '"+ type +"'";			
				}
			}
			
			//agence , produit non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
					
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//agence, produit, periode non vide
			if(!agence.equals("") && client.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				
				sql += "where e.numCompteEp like '"+ ag +"%' "
						+ "and  p.idProdEpargne = '"+idProd+"' and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'"
								+ " and tp.abrev = '"+ type +"'";
			}
			
			//Client non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null and tp.abrev = '"+ type +"'";			
				}
			}
			
			//Client , produit non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";			
				}	
			}
			
			//Client , produit et dates non vide non vide
			if(!client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and  p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
									+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//Client et dates non vide non vide
			if(!client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
					
				if(!ind.equals("") && grp.equals("")){
					sql += "join e.individuel i where e.individuel is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";					
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "join e.groupe g where e.groupe is not null "
							+ "and tp.abrev = '"+ type +"' "
									+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";			
				}
			}
			
			//produit non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){				
				
					sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"'";
			}
			
			//produit et dates non vide
			if(client.equals("") && agence.equals("") && !idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where p.idProdEpargne = '"+idProd+"' and tp.abrev = '"+ type +"' "
						+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			//dates non vide
			if(client.equals("") && agence.equals("") && idProd.equals("")
					&& !dateDeb.equals("") && !dateFin.equals("")){
				sql += "where tp.abrev = '"+ type +"' "
						+ "and c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";
			}
			
			if(agence.equals("") && client.equals("") && !type.equals("") && idProd.equals("")
					&& dateDeb.equals("") && dateFin.equals("")){
				
				sql +="where tp.abrev = '"+ type +"'";
			}
			
		}
		
		sql +=" order by e.dateOuverture asc";
		
		System.out.println("Sql rapport nouveaux compte = "+sql);
		
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql += " where c.dateCloture='"+dateDeb+"'";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql += " where c.dateCloture between '"+dateDeb+"' and '"+dateFin+"'";
		}
		
		TypedQuery<CompteFerme> query = em.createQuery(sql,CompteFerme.class);
		
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		
		return null;
	}

	//Fiche journalier
	@Override
	public List<TransactionEpargne> getFicheJournalier(String agence, String type, String date) {
		
		String sql = "select t from TransactionEpargne t ";
		
		if(!agence.equals("") || !type.equals("") || !date.equals("")){
			
			sql += "join t.compteEpargne e join e.produitEpargne p join p.typeEpargne tp ";	
			
			if(!agence.equals("") && !date.equals("")){
				sql +="where e.numCompteEp like '"+ agence +"%' and t.dateTransaction = '"+ date +"'"
						+ " and tp.abrev = '"+ type +"'";
			}

			if(!agence.equals("") && date.equals("")){
				sql +="where e.numCompteEp like '"+ agence +"%' and tp.abrev = '"+ type +"'";
			}
			
			if(agence.equals("") && !date.equals("")){
				sql +="where t.dateTransaction = '"+ date +"' and tp.abrev = '"+ type +"'";
			}
			
			/*if(agence.equals("") && date.equals("")){
				sql +="where tp.abrev = '"+ type +"'";
			}*/
			
		}
		
		TypedQuery<TransactionEpargne> query = em.createQuery(sql,TransactionEpargne.class);
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}
		return null;
	}

	//Etat de compte
	@Override
	public List<String> getEtatCompte(String dateDeb, String dateFin, int cat1,
			int cat2, int cat3) {
		
		List<String> result = new ArrayList<String>();
		
		String sql = "select count(c) from CompteEpargne c";
		
		//requete pour savoir le total de compte au début de période
		Query q = em.createQuery(sql+" where c.dateOuverture <= :dateDeb");
		q.setParameter("dateDeb", dateDeb);
		
		//requete pour savoir le total de compte à la fin de période
		Query der = em.createQuery(sql + " where c.dateOuverture <= :dateFin");
		der.setParameter("dateFin", dateFin);
		
		//recupération des requetes
		
		long totDeb = (long) q.getSingleResult();		
		long totFin = (long) der.getSingleResult();		
		long diff = totFin - totDeb;		
		
		System.out.println("Total compte au début de periode : "+ totDeb);
		System.out.println("Total compte fin de periode : "+ totFin);
		System.out.println("Nouveau compte : "+ diff);
		
		/** Debut de requete pour la date début période **/
		
		Query qsm = em.createQuery(sql+" where c.solde <= :sdMin and c.dateOuverture <= :dateDeb");
		qsm.setParameter("sdMin", cat1);
		qsm.setParameter("dateDeb", dateDeb);
		
		long soldeMin = (long) qsm.getSingleResult();
		
		Query qCat1 = em.createQuery(sql+" where c.solde between :sold1 and :sold2 and c.dateOuverture <= :dateDeb");
		qCat1.setParameter("sold1", cat1+1);
		qCat1.setParameter("sold2", cat2);
		qCat1.setParameter("dateDeb", dateDeb);
		
		long soldeCat1 = (long) qCat1.getSingleResult();
		
		Query qCat2 = em.createQuery(sql+" where c.solde between :sold1 and :sold2 and c.dateOuverture <= :dateDeb");
		qCat2.setParameter("sold1", cat2+1);
		qCat2.setParameter("sold2", cat3);
		qCat2.setParameter("dateDeb", dateDeb);
		
		long soldeCat2 = (long) qCat2.getSingleResult();
		
		Query qsm2 = em.createQuery(sql+" where c.solde > :sdMin and c.dateOuverture <= :dateDeb");
		qsm2.setParameter("sdMin", cat3);
		qsm2.setParameter("dateDeb", dateDeb);
		
		long soldeMax = (long) qsm2.getSingleResult();
		
		System.out.println("total de compte dont le solde est inférieur à "+cat1+" est "+ soldeMin);
		
		System.out.println("total de compte dont le solde est compris entre  "+cat1+1+" et "+cat2+" est "+soldeCat1);
		
		System.out.println("total de compte dont le solde est compris entre  "+cat2+1+" et "+cat3+" est "+soldeCat2);
		
		System.out.println("total de compte dont le solde est supérieur à "+cat3+" est "+ soldeMax);
		
		/** Debut de requete pour la date Fin période **/
		Query solMinFin = em.createQuery(sql+" where c.solde <= :sdMin and c.dateOuverture <= :dateFin");
		solMinFin.setParameter("sdMin", cat1);
		solMinFin.setParameter("dateFin", dateFin);
		
		long soldeMinFin = (long) solMinFin.getSingleResult();
		
		Query qCat1Fin = em.createQuery(sql+" where c.solde between :sold1 and :sold2 and c.dateOuverture <= :dateFin");
		qCat1Fin.setParameter("sold1", cat1+1);
		qCat1Fin.setParameter("sold2", cat2);
		qCat1Fin.setParameter("dateFin", dateFin);
		
		long soldeCat1Fin = (long) qCat1Fin.getSingleResult();
		
		Query qCat2Fin = em.createQuery(sql+" where c.solde between :sold1 and :sold2 and c.dateOuverture <= :dateFin");
		qCat2Fin.setParameter("sold1", cat2+1);
		qCat2Fin.setParameter("sold2", cat3);
		qCat2Fin.setParameter("dateFin", dateFin);
		
		long soldeCat2Fin = (long) qCat2Fin.getSingleResult();
		
		Query qsm2Fin = em.createQuery(sql+" where c.solde > :sdMin and c.dateOuverture <= :dateFin");
		qsm2Fin.setParameter("sdMin", cat3);
		qsm2Fin.setParameter("dateFin", dateFin);
		
		long soldeMaxFin = (long) qsm2Fin.getSingleResult();
		
		result.add(String.valueOf(totDeb));
		result.add(String.valueOf(totFin));
		result.add(String.valueOf(diff));
		
		result.add(String.valueOf(soldeMin));
		result.add(String.valueOf(soldeCat1));
		result.add(String.valueOf(soldeCat2));
		result.add(String.valueOf(soldeMax));
		
		result.add(String.valueOf(soldeMinFin));
		result.add(String.valueOf(soldeCat1Fin));
		result.add(String.valueOf(soldeCat2Fin));
		result.add(String.valueOf(soldeMaxFin));
		
		return result;
	}

	//Compte caisse dans grand livre
	static List<Caisse> getDistinctCaisse(String date){
		String sql = "select distinct t.compteCaisse from TransactionEpargne t where t.dateTransaction ='"+date+"'";
		TypedQuery<Caisse> query = em.createQuery(sql,Caisse.class);
		if(!query.getResultList().isEmpty()){
			return query.getResultList();
		}
		return null;
	}
	
	//Fiche caisse d'épargne
	@Override
	public List<FicheCaisseEpargne> getFicheCaisse(String date) {
		/*List<FicheCaisseEpargne> result = new ArrayList<FicheCaisseEpargne>();
		
		List<CompteCaisse> cmpCaisse = getDistinctCaisse(date);
		
		for (CompteCaisse compteCaisse : cmpCaisse) {
			
		}
		*/
		return null;
	}

	
	static double interetDAT(double montant,int taux,int periode){
		double interet = (montant * periode) * taux/100;		
		return interet;
	}
	

	@Override
	public String ouvrirDAT(String idProduitEp, String individuelId,String groupeId, int userId,CompteDAT compte
	, String piece,String typePaie, String numTel, String numCheq,String nomCptCaisse, String numCptEp) {
		String retour="";
		
		//Instance des classes necessaire		
		ProduitEpargne produit = em.find(ProduitEpargne.class, idProduitEp);
		Individuel ind = em.find(Individuel.class, individuelId);
		Groupe grp = em.find(Groupe.class, groupeId);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		
		//Configuration
		ConfigGeneralDAT confGn = produit.getConfigGeneralDat();
		ConfigGLDAT confGL = produit.getConfigGlDat();
		
		//Vérifié l'existance de compte
		
		String sqli = "select c from CompteDAT c join c.produitEpargne p join c.individuel i "
				+ " join c.groupe g";
		if(ind != null){
			sqli+=" where p.idProdEpargne='"+produit.getIdProdEpargne()+"' and i.codeInd='"+ind.getCodeInd()+"'";
		}
		if(grp != null){
			sqli+=" where p.idProdEpargne='"+produit.getIdProdEpargne()+"' and g.codeGrp='"+grp.getCodeGrp()+"'";
		}
		
		Query verif = em.createQuery(sqli);
		
		if(verif.getResultList().isEmpty()){
			System.out.println("nouveau compte DAT");
			//Interet
			double interet = interetDAT(compte.getMontant(), compte.getTauxInt(), compte.getPeriode());
			if(compte.getMontant() < confGn.getDepotMin() || compte.getMontant() > confGn.getDepotMax()){
				retour = "montant déposé doit compris entre "+confGn.getDepotMin() +" et "+ confGn.getDepotMax();
			}else{
				if(compte.getTauxInt() < confGn.getInterMin() || compte.getTauxInt() > confGn.getInterMax()){
					retour = "taux d'intérêt doit compris entre "+confGn.getInterMin() +" et "+ confGn.getDepotMax();
				}else{
					if(compte.getPeriode() < confGn.getPeriodeMinInt() || compte.getPeriode() > confGn.getPeriodeMaxInt()){
						retour = "période d'intérêt doit compris entre "+confGn.getPeriodeMinInt() +" et "+ confGn.getPeriodeMaxInt();
					}else{
						String tcode = CodeIncrement.genTcode(em);						
						
						Individuel i = null;
						Groupe g = null;
						//Information dépôt à terme
						compte.setInteret(interet); 
						if(ind != null){
							System.out.println("Dépôt à terme pour client individuel");
							compte.setNumCompteDAT(ind.getCodeInd() +"/"+produit.getIdProdEpargne());
							compte.setIndividuel(ind); 
							
							i = ind;
						}
						if(grp != null){
							System.out.println("Dépôt à terme pour client groupe");
							compte.setNumCompteDAT(grp.getCodeGrp() +"/"+produit.getIdProdEpargne());
							compte.setGroupe(grp); 
							
							g = grp;
						}
												
						Account accDeb = null;
						
						switch (typePaie) {
						case "cash":
							//Compte caisse
							Caisse cpt = em.find(Caisse.class, nomCptCaisse);
							String c = String.valueOf(cpt.getAccount().getNumCpt());
							
						    accDeb = CodeIncrement.getAcount(em, c);
							break;
						case "mobile":
							//Compte à la configuration
							accDeb = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
							break;
						case "cheque":
							//Compte à la configuration
							accDeb = CodeIncrement.getAcount(em, confGL.getCmptCheque());
							break;
						case "epargne":
							//Compte à la configuration
							CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
							
							System.out.println("compte "+cptEp.getNumCompteEp());
							
							
							if(cptEp.getSolde() > compte.getMontant() ){
								
								if(cptEp.isPasRetrait() == true){
									retour = "ce compte n'est pas autorisé à faire de retrait ";
								}else{
									
									double tr = cptEp.getSolde() - compte.getMontant();
									if(cptEp.getProduitEpargne().getConfigInteretProdEp().getSoldeMinInd() < tr ){
										accDeb = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
																				
										//Initialisation des informations de transaction
										TransactionEpargne trans = new 
												TransactionEpargne(tcode, compte.getDateDepot(),"Transferer au DAT du compte "+compte.getNumCompteDAT()
														, compte.getMontant(), piece, "RE", 
														"transfer", compte.getNumCompteDAT(), 0, 0, 0, cptEp, ut);
									
										cptEp.setSolde(tr);
										
										try {
											transaction.begin();
											em.persist(trans);
											em.merge(cptEp);
											transaction.commit();
											em.refresh(cptEp);
											em.refresh(trans); 
											System.out.println("Retrait du compte "+cptEp.getNumCompteEp() +"est effectué");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}else{
										retour = "montant non accepter";
									}
								}
															
							}else{
								retour = "Solde du compte "+cptEp.getNumCompteEp() +" est insuffisant";
							}
						
							break;				

						default:
							break;
						}
						
						compte.setProduitEpargne(produit); 	
						compte.setUtilisateur(ut); 
						compte.setRet(false); 	
						compte.setFermer(false); 
						
						try {
							//Enregistrement dans grand livre des opérations sur dépôt à terme	
							//Information debit
							
							ComptabliteServiceImpl.saveImputationDAT(tcode, compte.getDateDepot(), "Dépôt à terme du compte "+compte.getNumCompteDAT(),
									piece, 0, compte.getMontant(), ut, i, g, compte, null, accDeb);
							
							//Information crédit
							Account accCredit = CodeIncrement.getAcount(em, confGL.getCmptDAT());							
							ComptabliteServiceImpl.saveImputationDAT(tcode, compte.getDateDepot(), "Dépôt à terme du compte "+compte.getNumCompteDAT(),
									piece, compte.getMontant(), 0, ut, i, g, compte, null, accCredit);
							
							transaction.begin();
							em.persist(compte); 
							transaction.commit();
							em.refresh(compte);
							System.out.println("Enregistrement reussit");
							retour = "Enregistrement reussit";
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Erreur enregistrement");
							retour = "Erreur enregistrement";
						}
						
					}
				}
			}
			
		}else{
			System.out.println("Compte déjà existé");
			retour = "ce client a déjà un compte avec ce produit";
			
		}
			
		return retour;
	}
	
	//Retrait dépôt à terme
	@Override
	public String retaraitDAT(String compte,String date, int userId, String piece,
			String typePaie, String numTel, String numCheq,
			String nomCptCaisse, String numCptEp) {
		
		String retour = "";
		
		//Calcult montant total à retirer		
		CalculDAT calcul = calculMontantDAT(date, compte);
		
		double montant = calcul.getMontant();
		double interet = calcul.getInteret();
		double penalite = calcul.getPenalite();
		double taxe = calcul.getTaxe();
		
		Utilisateur ut = em.find(Utilisateur.class, userId);
		
		CompteDAT dat = em.find(CompteDAT.class, compte);
				
		LocalDate datRet = LocalDate.parse(date);
		LocalDate dateDepot = LocalDate.parse(dat.getDateDepot());
		Long av = ChronoUnit.DAYS.between(dateDepot, datRet);
		
		//List<E>	
		//Si date rétrait est inferieur au date dépôt
		if(av < 0 ){
			retour = "Date retrait ne doit pas inferieur au date de dépôt";
		}else{
			
			//Incrementation tcode grand livre
			String tcode = CodeIncrement.genTcode(em);
						
			//Imputation penalité sur grand livre
			ConfigGLDAT confGL = dat.getProduitEpargne().getConfigGlDat();
					
			//Imputation retrait sur dépôt à terme
			dat.setDateRetrait(date);
			dat.setTotalIntRetrait(interet);
			dat.setTaxe(taxe);
			dat.setPenalite(penalite);
			dat.setTotal(montant); 
			dat.setRet(true); 
				
			Account accCredit = null;
			switch (typePaie) {
			case "cash":
				//Compte caisse
				Caisse cpt = em.find(Caisse.class, nomCptCaisse);
				String c = String.valueOf(cpt.getAccount().getNumCpt());
				
			    accCredit = CodeIncrement.getAcount(em, c);
						
				break;
			case "mobile":
				//Compte à la configuration
				accCredit = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
				
				break;
			case "cheque":
				//Compte à la configuration
				accCredit = CodeIncrement.getAcount(em, confGL.getCmptCheque());
				
				break;
			case "epargne":
				//Compte à la configuration
				CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
				
				TransactionEpargne trans = new TransactionEpargne();	
	
					accCredit = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
										
					//Initialisation des informations de transaction
					trans.setDateTransaction(date);
					trans.setTypeTransEp("DE");
					trans.setMontant(montant);
					trans.setDescription("Dépôt via DAT du compte "+dat.getNumCompteDAT());
					trans.setPieceCompta(piece);
					trans.setIdTransactionEp(tcode);
					trans.setCompteEpargne(cptEp);
					trans.setTypePaie("transfer");
					trans.setValPaie(dat.getNumCompteDAT());
					
					double tr = cptEp.getSolde() + montant;
					cptEp.setSolde(tr);
					
					try {
						transaction.begin();
						em.persist(trans);
						em.merge(cptEp);
						transaction.commit();
						em.refresh(cptEp); 
						em.refresh(trans); 
						System.out.println("Retrait du compte "+cptEp.getNumCompteEp() +"est effectué");
					} catch (Exception e) {
						e.printStackTrace();
					}
				break;				

			default:
				break;
			}
			
			Individuel ind = null;
			Groupe grp = null;
					
			if(dat.getIndividuel() != null){
				ind = dat.getIndividuel();
			}
			if(dat.getGroupe() != null){
				grp = dat.getGroupe();
			}
			
			//Imputation DAT
			
			//Frais de dépôt à terme			
			if(taxe != 0){									
				//Compte taxe
				Account cmptTax = CodeIncrement.getAcount(em, confGL.getCmptTaxeRetenu());
				ComptabliteServiceImpl.
				saveImputationDAT(tcode, date, "Taxe retenu DAT du compt.DAT"+dat.getNumCompteDAT(),
						tcode, 0, taxe, ut, ind, grp, dat, null, cmptTax);
			}
			//Intérêt
			if(interet != 0){
				//Compte interet
				Account cmptInt = CodeIncrement.getAcount(em, confGL.getCmptIntPayeDAT());
				ComptabliteServiceImpl.
				saveImputationDAT(tcode, date, "Intérêt payé DAT du compt.DAT"+dat.getNumCompteDAT(),
						tcode, 0, interet, ut, ind, grp, dat, null, cmptInt);
			}
			
			//Penalité
			if(penalite != 0){
				//Compte penalité
				Account cmptInt = CodeIncrement.getAcount(em, confGL.getCmptPenalDAT());
				ComptabliteServiceImpl.
				saveImputationDAT(tcode, date, "Penalité sur DAT du compt.DAT"+dat.getNumCompteDAT(),
						tcode, 0, penalite, ut, ind, grp, dat, null, cmptInt);
			}
			
			//Compte DAT à débité
			Account debitDAT = CodeIncrement.getAcount(em, confGL.getCmptDAT());
			double montDeb = montant - (interet + taxe + penalite);
			ComptabliteServiceImpl.
			saveImputationDAT(tcode, date, "Transfert échéance DAT du compt.DAT"+dat.getNumCompteDAT(),
					tcode, 0, montDeb, ut, ind, grp, dat, null, debitDAT);
			
			//Compte à crédité
			ComptabliteServiceImpl.
			saveImputationDAT(tcode, date, "Transfert échéance DAT du compt.DAT"+dat.getNumCompteDAT(),
					tcode, montant, 0, ut, ind, grp, dat, null, accCredit);
			
			
							
			try {
				transaction.begin();
				em.flush();
				transaction.commit();
				em.refresh(dat); 
				System.out.println("Enregistrement reussit");
				retour = "Enregistrement reussit";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur enregistrement");
				retour = "Erreur enregistrement";
			}		
			
		}
		
		return retour;
		
	}
	
	//Retrait automatique sur DAT
	@Override
	public List<CompteDAT> retraitDATAuto(String dateRetrait, int user) {
		
		List<CompteDAT> result = new ArrayList<CompteDAT>();
			
		Utilisateur ut = em.find(Utilisateur.class, user);
		
		//Chercher tous les comptes DAT
		TypedQuery<CompteDAT> query = em.createQuery("select d from CompteDAT d where d.ret = 'false'", CompteDAT.class);
		if(!query.getResultList().isEmpty()){
			try {
				CounterExecution ct = new CounterExecution();
				ct.setDate(dateRetrait);
				ct.setExec(true);
				transaction.begin();
				em.persist(ct);
				transaction.commit();
				em.refresh(ct); 
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			//parcourir les comptes DAT
			for (CompteDAT compte : query.getResultList()) {
				System.out.println("Compte DAT n° :"+ compte.getNumCompteDAT() + " echeance :"+ compte.getDateEcheance());
				//Si date aujourd'hui est égal à date échéance du compte on entre
				if(dateRetrait.equalsIgnoreCase(compte.getDateEcheance()) && compte.isRet() == false){
					//Test du code client, si individuel non null codeCli est le code du client ind
					//Si non code du groupe
					String codeCli = "";
					Individuel ind = null;
					Groupe grp = null;
					
					String sql = "select c from CompteEpargne c ";
					if(compte.getIndividuel() != null){
						ind = compte.getIndividuel();
						codeCli = compte.getIndividuel().getCodeInd();
						sql += " join c.individuel i where i.codeInd = '"+ codeCli +"'";
					}
					if(compte.getGroupe() != null){
						grp = compte.getGroupe();
						codeCli = compte.getGroupe().getCodeGrp();
						sql += " join c.groupe g where g.codeGrp  = '"+ codeCli +"'";
					}
					
					System.out.println("Code client :"+ codeCli);
					//Cherche tous les comptes épargne du client
					 
					TypedQuery<CompteEpargne> q = em.createQuery(sql, CompteEpargne.class);
					
					if(!q.getResultList().isEmpty()){
						//Parcourir les comptes éparnge du client
						for (CompteEpargne c : q.getResultList()) {
							System.out.println("Compte epargne n° :"+ c.getNumCompteEp() + " du client :"+ codeCli +" prioritaire :"+ c.isPrioritaire());
							
							//Trouver le compte épargne prioritaire
							if(c.isPrioritaire() == true){
								
								//Calcul Intérêt
								CalculDAT calcul = calculMontantDAT(dateRetrait, compte.getNumCompteDAT());
								
								double montant = calcul.getMontant();
								double interet = calcul.getInteret();
								double penalite = calcul.getPenalite();
								double taxe = calcul.getTaxe();
								
								//Incrementation tcode grand livre
								String tcode = CodeIncrement.genTcode(em);
								
								//Imputation DAT
								ConfigGLDAT confGL = compte.getProduitEpargne().getConfigGlDat();
								
								//Frais de dépôt à terme			
								if(taxe != 0){									
									//Compte taxe
									Account cmptTax = CodeIncrement.getAcount(em, confGL.getCmptTaxeRetenu());
									ComptabliteServiceImpl.
									saveImputationDAT(tcode, dateRetrait, "Taxe retenu DAT du compt.DAT"+compte.getNumCompteDAT(),
											tcode, 0, taxe, ut, ind, grp, compte, null, cmptTax);
								}
								//Intérêt
								if(interet != 0){
									//Compte interet
									Account cmptInt = CodeIncrement.getAcount(em, confGL.getCmptIntPayeDAT());
									ComptabliteServiceImpl.
									saveImputationDAT(tcode, dateRetrait, "Intérêt payé DAT du compt.DAT"+compte.getNumCompteDAT(),
											tcode, 0, interet, ut, ind, grp, compte, null, cmptInt);
								}
								
								//Penalité
								if(penalite != 0){
									//Compte penalité
									Account cmptInt = CodeIncrement.getAcount(em, confGL.getCmptPenalDAT());
									ComptabliteServiceImpl.
									saveImputationDAT(tcode, dateRetrait, "Penalité sur DAT du compt.DAT"+compte.getNumCompteDAT(),
											tcode, 0, penalite, ut, ind, grp, compte, null, cmptInt);
								}
								
								//Compte DAT à débité
								Account debitDAT = CodeIncrement.getAcount(em, confGL.getCmptDAT());
								double montDeb = montant - (interet + taxe + penalite);
								ComptabliteServiceImpl.
								saveImputationDAT(tcode, dateRetrait, "Transfert échéance DAT du compt.DAT"+compte.getNumCompteDAT(),
										tcode, 0, montDeb, ut, ind, grp, compte, null, debitDAT);
								
								//Compte à crédité
								Account creditDAT = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
								ComptabliteServiceImpl.
								saveImputationDAT(tcode, dateRetrait, "Transfert échéance DAT du compt.DAT"+compte.getNumCompteDAT(),
										tcode, montant, 0, ut, ind, grp, compte, null, creditDAT);
								
								
								
								//Mise à jour DAT
								compte.setDateRetrait(dateRetrait);
								compte.setTotalIntRetrait(interet);
								compte.setTaxe(taxe);
								compte.setPenalite(penalite);
								compte.setTotal(montant); 
								compte.setRet(true); 
						
								//Transfert sur Compte DAV
								TransactionEpargne trans = new 
										TransactionEpargne(tcode, dateRetrait, "Transfert échéance DAT du compte "+compte.getNumCompteDAT(), 
												montant, tcode, "DE", "transfer", compte.getNumCompteDAT(), 0, 0,
												0, c, ut);	
																
								double tr = c.getSolde() + montant;
								c.setSolde(tr);
								
								try {
									transaction.begin();
									em.persist(trans);
									em.merge(compte);
									em.merge(c);
									transaction.commit();
									em.refresh(trans); 
									em.refresh(compte); 
									em.refresh(c); 
									
									System.out.println("Retrait du compte "+c.getNumCompteEp() +" est effectué");
									result.add(compte);
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Erreur calcul DAT");
								}
							}
						}
					}
					
				}
			}
		}
		
		return result;
	}

	//Annuler DAT
	@Override
	public boolean annulerDAT(String compte, String raison) {
		CompteDAT dat = em.find(CompteDAT.class, compte);		
		try {
			dat.setFermer(true);
			dat.setRaison(raison);
			transaction.begin();
			em.flush();
			transaction.commit();
			em.refresh(dat);
			System.out.println("Dépôt à terme n° "+dat.getNumCompteDAT() +" a été annulé");
			return true;
		} catch (Exception e) {    
			e.printStackTrace();
			return false;
		}
	}
	
	//Supprimer compte DAT
	@Override
	public boolean deleteCompteDAT(String compte, int user) {
		CompteDAT c = em.find(CompteDAT.class, compte);
		Utilisateur ut = em.find(Utilisateur.class, user);
		CompteDATSupp cd = new 
				CompteDATSupp(c.getNumCompteDAT(), c.getDateDepot(), c.getDateEcheance(), 
						c.getMontant(), c.getInteret(), c.getTauxInt(), c.getPeriode(), c.getModePayeInteret(),
						c.isRet(), c.getDateRetrait(), c.getTotalIntRetrait(), c.getTaxe(), c.getPenalite(),
						c.getTotal(), c.getPayeInt(), c.getRaison(), c.isFermer(), c.getProduitEpargne().getIdProdEpargne(),
						ut.getNomUtilisateur());
		
		if(c.getIndividuel() != null)
		{
			cd.setCodeClient(c.getIndividuel().getCodeInd());
			cd.setNomClient(c.getIndividuel().getNomClient() +" "+ c.getIndividuel().getPrenomClient());
		}
		if(c.getGroupe() != null){
			cd.setCodeClient(c.getGroupe().getCodeGrp());
			cd.setNomClient(c.getGroupe().getNomGroupe());
		}
		try {
			transaction.begin();
			em.persist(cd);
			em.remove(c);
			transaction.commit();
			em.refresh(cd); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Liste compte DAT supprimé
	@Override
	public List<CompteDATSupp> getCompteDATSupprimer() {
		String sql = "select c from CompteDATSupp c";
		TypedQuery<CompteDATSupp> q = em.createQuery(sql, CompteDATSupp.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Rapport compte DAT   
	@Override
	public List<CompteDAT> rapportDAT(String dateDeb, String dateFin,boolean payer,boolean fermer) {
	
		String sql = "select d from CompteDAT d ";
				
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql+=" where d.fermer = '"+fermer+"' and d.ret='"+payer+"'  and d.dateDepot < '"+dateDeb+"'";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql+="  where d.fermer = '"+fermer+"' and d.ret='"+payer+"' and d.dateDepot between '"+dateDeb+"' and '"+dateFin+"'";
		}
		
		TypedQuery<CompteDAT> requete = em.createQuery(sql,CompteDAT.class);
		
		if(!requete.getResultList().isEmpty())
			return requete.getResultList();
		
		return null;
	}
	
	@Override
	public List<CompteDAT> rapportEcheanceDAT(String dateDeb, String dateFin) {
		String sql = "select d from CompteDAT d ";
		
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql+=" where d.ret='"+true+"'  and d.dateDepot < '"+dateDeb+"'";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql+="  where d.ret='"+true+"' and d.dateDepot between '"+dateDeb+"' and '"+dateFin+"'";
		}
		//d.fermer = '"+false+"' and
		TypedQuery<CompteDAT> requete = em.createQuery(sql,CompteDAT.class);    
		
		if(!requete.getResultList().isEmpty())
			return requete.getResultList();
		
		return null;
	}

	
	//Selection produit épargne
	@Override
	public List<ProduitEpargne> getProduit(String debut, int val) {
		String sql = "select p from ProduitEpargne p ";
		
		if(val == 1 && !debut.equals("")){
			sql+=" where p.idProdEpargne like '"+debut+"%'";
		}else if(val == 0 && !debut.equals("")){
			sql+=" where p.idProdEpargne not like '"+debut+"%'";
		}
		
		TypedQuery<ProduitEpargne> query = em.createQuery(sql,ProduitEpargne.class);
		
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		
		return null;
	}

	//Calcul date echeance
	@Override
	public String getDateEcheanche(String date,int periode) {		
		LocalDate converse = LocalDate.parse(date);		
		String result = converse.plusMonths(periode).toString();		
		return result;
	}

	//Checher un compte DAT
	@Override
	public CompteDAT getCompteDAT(String compte, boolean payer, boolean fermer) {
		
		String sql = "select c from CompteDAT c where c.numCompteDAT like '"+compte+"' "
				+ " and c.fermer = '"+fermer+"' and c.ret='"+payer+"'";
		Query query = em.createQuery(sql);
		CompteDAT result = (CompteDAT) query.getSingleResult();		
		return result;
	}

	//Chercher tout compte DAT
	@Override
	public List<CompteDAT> findCompteDAT(String compte, boolean payer,
			boolean fermer) {
		String sql = "select c from CompteDAT c where c.numCompteDAT like '"+compte+"%' "
				+ " and c.fermer = '"+fermer+"' and c.ret='"+payer+"'";		
		TypedQuery<CompteDAT> query = em.createQuery(sql,CompteDAT.class);
		if(!query.getResultList().isEmpty())
			return query.getResultList();
		return null;
	}

	//Calcul montant retrait DAT
	@Override
	public CalculDAT calculMontantDAT(String date, String numCompte) {
		CompteDAT compte = em.find(CompteDAT.class, numCompte);
		CalculDAT result = new CalculDAT();
		LocalDate dateDepot = LocalDate.parse(compte.getDateDepot());
		LocalDate dateRetrait = LocalDate.parse(date);		
		Long periode = ChronoUnit.MONTHS.between(dateDepot, dateRetrait);
		
		double montant  = compte.getMontant();
		int taux = compte.getTauxInt();
		int taxe = compte.getProduitEpargne().getConfigGeneralDat().getTaxe();
		
		double interet = (montant * periode) * taux/100;
		double retenue = interet * taxe /100;
		
		Long prema = ChronoUnit.DAYS.between(LocalDate.parse(compte.getDateEcheance()), dateRetrait);
		
		double penalite=0;
		if(prema<0){
			//Rétrait prématurée
			if(compte.getProduitEpargne().getConfigGeneralDat().getMontantPremature() != 0){
				penalite = compte.getProduitEpargne().getConfigGeneralDat().getMontantPremature();					
			}else if(compte.getProduitEpargne().getConfigGeneralDat().getFinPremature() != 0){
				penalite = (montant * compte.getProduitEpargne().getConfigGeneralDat().getFinPremature())/100 ;
			}else if(compte.getProduitEpargne().getConfigGeneralDat().isAuccunInteret() == true){
				interet = 0;
				penalite = 0;
			}
		}
		
		double total = montant + (interet-retenue) - penalite;

		result.setInteret(interet);
		result.setTaxe(retenue);
		result.setMontant(total);
		result.setPeriode(periode); 
		result.setPenalite(penalite); 
		return result;
	}

	//-------------------------------------------------------------------------------------------------------
	//Ouverture compte épargne
	/*@Override
	public String ouvrirComptePep(ComptePep compte, String idProduit,
			String codeInd, String codeGrp, int userId) {
		
	}
	*/

	@Override
	public String ouvrirComptePep(ComptePep compte, String idProduit,
			String codeInd, String codeGrp, int userId, String trans,
			String piece, String typePaie, String numTel, String numCheq,
			String CompteCaisse, double montant) {
		
		String result = "";
		
		ProduitEpargne produit = em.find(ProduitEpargne.class, idProduit);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		Individuel ind = null;
		Groupe grp = null;
		
		if(!codeInd.equals("")){
			ind = em.find(Individuel.class, codeInd);
			compte.setNumCompte(ind.getCodeInd()+"/"+produit.getIdProdEpargne());
		}
		if(!codeGrp.equals("")){
			grp = em.find(Groupe.class, codeGrp);
			compte.setNumCompte(grp.getCodeGrp()+"/"+produit.getIdProdEpargne());
		}
		
		String dateFin = CodeIncrement.getDateFinDepot(compte.getPeriode(), compte.getFrequence(), compte.getDateDebutDepot());
		System.out.println("Date fin :"+ dateFin);
		
		compte.setProduitEpargne(produit);
		compte.setIndividuel(ind); 
		compte.setGroupe(grp);
		compte.setUtilisateur(ut); 
		compte.setTotPeriode(compte.getPeriode());
		compte.setDateFinDepot(dateFin); 
		
		List<CalendrierPepView> calView = 
				CodeIncrement.getCalPrevuPep(compte.getNumCompte(), 
						compte.getPeriode(), compte.getFrequence(), montant, compte.getDateDebutDepot(), compte.getTauxInt());
		try {
			transaction.begin();
			em.persist(compte);
			for (CalendrierPepView cal : calView) {
				CalendrierPep cp = new CalendrierPep(cal.getDate(), cal.getCapital(), cal.getDuree(), cal.getInteret(), compte);
				em.persist(cp); 
			}
			transaction.commit();
			em.refresh(compte); 
			
			TransactionPep transc = new TransactionPep();
			transc.setDateTransaction(compte.getDateDebutDepot());
			transc.setDescription("1er dépôt");
			transc.setMontant(montant);
			transc.setPeriode(compte.getPeriode());
			transc.setPieceCompta(piece);
			transc.setTypePaie(typePaie);
			transc.setTypeTrans(trans); 
			
			boolean s = saveTransPep(transc, compte.getNumCompte(), userId, numTel, numCheq, CompteCaisse);
			
			if(s == true){
				System.out.println("Ouverture compte pep reussi");
				result = "Ouverture compte réussi";
			}else{
				System.out.println("Erreur ouverture compte pep");
				result = "Erreur ouverture compte pep";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur Ouverture compte pep");
			result = "Erreur ouverture compte";
		}
		
		return result;
	}
	

	@Override
	public boolean modifierComptePep(String idCompte, ComptePep compte,
			int userId) {
		ComptePep c = getUniqueComptePep(idCompte);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		c = compte;
		c.setUserUpdate(ut); 
		try {
			transaction.begin();
			em.merge(c);
			transaction.commit();
			em.refresh(c); 
			System.out.println("Modification pep reussi");
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
			System.err.println("Erreur modification pep");
			return false;
		}
	}

	@Override
	public ComptePep getUniqueComptePep(String idCompte) {
		return em.find(ComptePep.class, idCompte);
	}

	@Override
	public boolean deleteComptePep(String idCompte, int userId) {
		ComptePep c = getUniqueComptePep(idCompte);
		Utilisateur ut = em.find(Utilisateur.class, userId);
		ComptePepSupp cs = new 
				ComptePepSupp(c.getNumCompte(), c.getDateDebutDepot(), c.getDateFinDepot(),
						c.getPeriode(), c.getFrequence(), c.getSolde(), c.getTotalInteret(),
						c.getTauxInt(), c.getProduitEpargne().getIdProdEpargne(), "", ut.getNomUtilisateur());
		if(c.getIndividuel() != null){
			cs.setCodeClient(c.getIndividuel().getCodeInd());
			cs.setNomClient(c.getIndividuel().getNomClient() +" "+ c.getIndividuel().getPrenomClient());
			cs.setAgence(c.getIndividuel().getAgence().getCodeAgence());
		}if(c.getGroupe() != null){
			cs.setCodeClient(c.getGroupe().getCodeGrp());
			cs.setNomClient(c.getGroupe().getNomGroupe());
			cs.setAgence(c.getGroupe().getAgence().getCodeAgence()); 
		}
		try {
			transaction.begin();
			em.persist(cs);
			em.refresh(c);
			transaction.commit();
			em.refresh(cs); 
			System.out.println("Suppression compte pep reussi");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur suppression pep");
			return false;
		}
	}

	@Override
	public List<ComptePep> findComptePep(String idCompte) {
		String sql = "select c from ComptePep c where c.numCompte like '"+ idCompte +"%'";
		TypedQuery<ComptePep> q = em.createQuery(sql, ComptePep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	@Override
	public List<ComptePep> getAllComptePep() {
		String sql = "select c from ComptePep c order by c.numCompte asc";
		TypedQuery<ComptePep> q = em.createQuery(sql, ComptePep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	@Override
	public List<ComptePepSupp> getComptePepSupp() {
		String sql = "select c from ComptePepSupp c order by c.numCompte asc";
		TypedQuery<ComptePepSupp> q = em.createQuery(sql, ComptePepSupp.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}
	
	
	@Override
	public List<ComptePep> getComptePepByCli(String codeInd, String codeGrp) {
		if(!codeInd.equals("")){
			Individuel ind = em.find(Individuel.class, codeInd);
			return ind.getComptePep();
		}
		if(!codeGrp.equals("")){
			Groupe grp = em.find(Groupe.class, codeGrp);
			return grp.getComptePep();
		}
		return null;
	}


	//--------------------------------------------------------------------------------------------------
	/****************************** Transaction plan d'épargne *****************************************/

	@Override
	public boolean saveTransPep(TransactionPep trans, String numCptEp,
			int idUser, String numTel, String numCheq, String CompteCaisse) {
		
		Utilisateur ut = em.find(Utilisateur.class, idUser);
		ComptePep cp = em.find(ComptePep.class, numCptEp);
		
		if(trans.getTypeTrans().equalsIgnoreCase("DE")){
			
			///incrémenter le tcode dans la table grandlivre
			String indexTcode = CodeIncrement.genTcode(em);
			
			Individuel ind = null;
			Groupe grp = null;
		
			if(cp.getIndividuel() != null) 
				ind = cp.getIndividuel();
			if(cp.getGroupe() != null)
				grp = cp.getGroupe();
			
			ConfigGLPEP confGl = cp.getProduitEpargne().getConfigGlPep();
			
			//Compte comptable à débiter
			Account accDeb = null;		
			Caisse cptCaisse = null;
			String vp = "";			
			if(trans.getTypePaie().equalsIgnoreCase("cash")){	
				
				cptCaisse = em.find(Caisse.class, CompteCaisse);			
				vp = String.valueOf(cptCaisse.getAccount().getNumCpt());					
				accDeb = cptCaisse.getAccount();
				
			}else if(trans.getTypePaie().equalsIgnoreCase("cheque")){
				accDeb = CodeIncrement.getAcount(em, confGl.getCmptCheque());
				vp = numCheq;
			}else if(trans.getTypePaie().equalsIgnoreCase("mobile")){
				accDeb = CodeIncrement.getAcount(em, confGl.getCmptDiffCash());
				vp = numTel;
			}
			
			double inter = ((trans.getMontant() * cp.getTauxInt()) / 100) * cp.getPeriode();
			
			trans.setPeriode(cp.getPeriode());
			cp.setSolde(cp.getSolde() + trans.getMontant());
			cp.setTotalInteret(cp.getTotalInteret() + inter);
			cp.setPeriode(cp.getPeriode() - 1);
			
			trans.setValPaie(vp); 
			trans.setInteret(inter);
			trans.setSoldeProgressif(trans.getMontant() + inter);
			trans.setIdTransaction(indexTcode); 
			trans.setUtilisateur(ut);
			trans.setComptePep(cp); 
			
			try {
				transaction.begin();
				em.persist(trans);
				em.merge(cp);
				transaction.commit();
				em.refresh(trans);
				em.refresh(cp); 
				String desc = "Dépôt pep du compte "+numCptEp;
				
				ComptabliteServiceImpl.
				saveImputationPEP(indexTcode, trans.getDateTransaction(), desc, trans.getPieceCompta(),
						0, trans.getMontant(), ut, ind, grp, cp, null, accDeb);

				Account accCred = CodeIncrement.getAcount(em, confGl.getCmptPep());
				ComptabliteServiceImpl.
				saveImputationPEP(indexTcode, trans.getDateTransaction(), desc, trans.getPieceCompta(),
						trans.getMontant(), 0, ut, ind, grp, cp, null, accCred);
				
				System.out.println("Transaction pep enregistré");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur transaction pep");
				return false;
			}
			
		}
		
		return false;
	}
	
	@Override
	public boolean saveRetraitPep(TransactionPep trans, String numCptEp,
			int idUser, String numTel, String numCheq, String CompteCaisse) {
		Utilisateur ut = em.find(Utilisateur.class, idUser);
		ComptePep cp = em.find(ComptePep.class, numCptEp);
		
		if(trans.getTypeTrans().equalsIgnoreCase("RE")){
			
			///incrémenter le tcode dans la table grandlivre
			String indexTcode = CodeIncrement.genTcode(em);
			
			Individuel ind = null;
			Groupe grp = null;
		
			if(cp.getIndividuel() != null) 
				ind = cp.getIndividuel();
			if(cp.getGroupe() != null)
				grp = cp.getGroupe();
			
			ConfigGLPEP confGl = cp.getProduitEpargne().getConfigGlPep();
			
			//Compte comptable à débiter
			Account accCred = null;		
			Caisse cptCaisse = null;
			String vp = "";			
			if(trans.getTypePaie().equalsIgnoreCase("cash")){	
				
				cptCaisse = em.find(Caisse.class, CompteCaisse);			
				vp = String.valueOf(cptCaisse.getAccount().getNumCpt());					
				accCred = cptCaisse.getAccount();
				
			}else if(trans.getTypePaie().equalsIgnoreCase("cheque")){
				accCred = CodeIncrement.getAcount(em, confGl.getCmptCheque());
				vp = numCheq;
			}else if(trans.getTypePaie().equalsIgnoreCase("mobile")){
				accCred = CodeIncrement.getAcount(em, confGl.getCmptDiffCash());
				vp = numTel;
			}
			
			double montant = cp.getSolde() + cp.getTotalInteret();
			
			cp.setRet(true); 
			
			trans.setPeriode(cp.getPeriode());
			trans.setValPaie(vp); 
			trans.setInteret(cp.getTotalInteret());
			trans.setSoldeProgressif(montant);
			trans.setMontant(montant); 
			trans.setIdTransaction(indexTcode); 
			trans.setUtilisateur(ut);
			trans.setComptePep(cp); 
			
			try {
				transaction.begin();
				em.persist(trans);
				em.merge(cp);
				transaction.commit();
				em.refresh(trans);
				em.refresh(cp); 
				String desc = "Total retrait pep du compte "+numCptEp;
				Account accDeb = CodeIncrement.getAcount(em, confGl.getCmptPep());
				ComptabliteServiceImpl.
				saveImputationPEP(indexTcode, trans.getDateTransaction(), desc, trans.getPieceCompta(),
						0, montant, ut, ind, grp, cp, null, accDeb);

				String desc1 = "Retrait capital pep du compte "+numCptEp;
				ComptabliteServiceImpl.
				saveImputationPEP(indexTcode, trans.getDateTransaction(), desc1, trans.getPieceCompta(),
						cp.getSolde(), 0, ut, ind, grp, cp, null, accCred);
				
				String desc2 = "Retrait intérêt pep du compte "+numCptEp;
				Account accCred2 = CodeIncrement.getAcount(em, confGl.getCmptIntPayePep());
				ComptabliteServiceImpl.
				saveImputationPEP(indexTcode, trans.getDateTransaction(), desc2, trans.getPieceCompta(),
						cp.getTotalInteret(), 0, ut, ind, grp, cp, null, accCred2);
				
				System.out.println("Transaction pep enregistré");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur transaction pep");
				return false;
			}
			
		}
		
		return false;
	}
	
	
	//Get unique transaction
	@Override
	public TransactionPep getUniqueTransPep(String idTrans) {
		return em.find(TransactionPep.class, idTrans);
	}

	@Override
	public List<TransactionPep> getAllTransPep() {
		String sql = "select c from TransactionPep c order by c.idTransaction";
		TypedQuery<TransactionPep> q = em.createQuery(sql, TransactionPep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}
	
	@Override
	public List<TransactionPep> getTransPepByCompte(String idCompte) {
		ComptePep c = em.find(ComptePep.class, idCompte);
		return c.getTransaction();
	}

	@Override
	public boolean updateTransPep(String idTrans, TransactionPep trans,
			int idUser) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteTransPep(String idTrans, int idUser) {
		TransactionPep c = getUniqueTransPep(idTrans);
		//Utilisateur ut = em.find(Utilisateur.class, idUser);
		try {
			transaction.begin();
			em.refresh(c);
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;			
		}
	}

	//Verification excecution
	@Override
	public boolean verifierExecution(String date) {
		String sql = "select c from CounterExecution c where c.date='"+date+"' and c.exec='true'";
		TypedQuery<CounterExecution> q = em.createQuery(sql, CounterExecution.class);
		if(!q.getResultList().isEmpty())
			return true;
		return false;
	}

	@Override
	public List<CalendrierPepView> getCalendrierViewPep(String produit,
			String codInd, String codGroupe, String dateOverture, int duree,
			String frequence, double montant, double taux) {
				
		ProduitEpargne p = em.find(ProduitEpargne.class, produit);
		Individuel ind = null;
		Groupe grp = null;
		String numCompte = "";
		if(!codInd.equals("")){
			ind = em.find(Individuel.class, codInd);
			 numCompte = ind.getCodeInd()+"/"+p.getIdProdEpargne();
		}
		if(!codGroupe.equals("")){
			grp = em.find(Groupe.class, codGroupe);
			numCompte = grp.getCodeGrp()+"/"+p.getIdProdEpargne();
		}	
		String dateFin = CodeIncrement.getDateFinDepot(duree, frequence, dateOverture);
		System.out.println("Date fin :"+ dateFin);
		
		List<CalendrierPepView> result = CodeIncrement.
				getCalPrevuPep(numCompte, duree, frequence, montant, dateOverture, taux);	
		return result;
	}

	//Vider calendrier view Pep
	@Override
	public boolean viderCalendrierPepView() {
		Query query = em.createNativeQuery("TRUNCATE TABLE calendrierPepView");
		try {
			transaction.begin();
			query.executeUpdate();
			transaction.commit();
			System.out.println("Table calendrier pep view"
					+ " vide");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Chercher compte plan épargne par client 
	@Override
	public List<ComptePep> findComptePepByCli(String codeInd, String codeGrp) {
	
		System.out.println("codeInd : "+ codeInd);
		System.out.println("codeGrp : "+ codeGrp);
		
		String sql = "SELECT c FROM ComptePep c  ";
		
		if(!codeInd.equals("") && codeGrp.equals("")){
			sql += "JOIN c.individuel i WHERE "
					+ " i.codeInd ='"+ codeInd +"'";
		}
		
		if(!codeGrp.equals("") && codeInd.equals("")){
			sql += " JOIN c.groupe g WHERE"
					+ " g.codeGrp = '"+ codeGrp +"'";
		}
		
		System.out.println("requete : "+ sql);
		
		TypedQuery<ComptePep> q = em.createQuery(sql, ComptePep.class);
		
		if(!q.getResultList().isEmpty()){
			System.out.println("Match result");
			List<ComptePep> c = q.getResultList();
			for (ComptePep compteEpargne : c) {
				System.out.println("Num compte : "+ compteEpargne.getNumCompte());
			}
			return q.getResultList();
		}
		System.out.println("No result");
		return null;
	}

	@Override
	public List<CalendrierPep> getCalPepByCompte(String idCompte) {
		
		String sql = "select c from CalendrierPep c join c.comptePep p where p.numCompte = '"+idCompte+"' "
				+ "order by c.date";
		TypedQuery<CalendrierPep> q = em.createQuery(sql, CalendrierPep.class);
		return q.getResultList();
	}

	@Override
	public long calculDate(String idCompte, String date) {
		ComptePep c = em.find(ComptePep.class, idCompte);
		String dateCal = c.getDateFinDepot();
		try { 
			
			LocalDate dateparm = LocalDate.parse(date);
			LocalDate dateDu = LocalDate.parse(dateCal);
			System.out.println("date aujourd'hui : "+dateparm);
			System.out.println("date retrait autorisé : "+dateDu);
	
			Long val = ChronoUnit.DAYS.between(dateDu, dateparm);			
			System.out.println("valeur calcul date : "+val + "\n");			
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	//Rapport nouveaux comptes plan d'épargne
	@Override
	public List<ComptePep> rapportNouveauPEP(String agence, String client,
			String produit, String dateDeb, String dateFin) {
		String sql = "select c from ComptePep c ";
		
		if(!agence.equals("") || !client.equals("") || !produit.equals("") || !dateDeb.equals("") || !dateFin.equals("")){
			
			String ind = "";
			String grp = "";
			
			if(!client.equals("")){
				
				if (client.equalsIgnoreCase("individuel")) {
					ind = client;
					grp = "";
					sql +="join c.individuel i ";
				} else if (client.equalsIgnoreCase("groupe")) {
					grp = client;
					ind = "";
					sql += "join c.groupe g ";
				}
				
			}
			
			if(!produit.equals("")){
				sql += "join c.produitEpargne p ";
			}
			sql += "where ";
				
			if(!agence.equals("")){
				sql+=" c.numCompte like '"+agence+"%' ";
				if(!client.equals("") || !produit.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
					sql +="and ";
			}
			if(!client.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += " c.individuel is not null ";
					if(!produit.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
						sql +="and ";
				}
				if(ind.equals("") && !grp.equals("")){
					sql += " c.groupe is not null ";
					if(!produit.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
						sql +="and ";
				}
			}
			if(!produit.equals("")){
				sql +=" p.idProdEpargne = '"+ produit +"' ";
				if((!dateDeb.equals("") && !dateFin.equals("")))
					sql +="and ";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql +=" c.dateDebutDepot between '"+dateDeb+"' and '"+dateFin+"'";
			}
		}
		System.out.println("sql nouveaux compte PEP: "+ sql);
		TypedQuery<ComptePep> q = em.createQuery(sql, ComptePep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	@Override
	public List<TransactionPep> rapportTransactionPEP(String agence,
			String client, String produit, String trans, String dateDeb,
			String dateFin) {
		String sql = "select t from TransactionPep t ";
		if(!agence.equals("") || !client.equals("") || !produit.equals("") || !trans.equals("")
				|| !dateDeb.equals("") || !dateFin.equals("")){
			
			String ind = "";
			String grp = "";
			
			if(!client.equals("")){
				
				if (client.equalsIgnoreCase("individuel")) {
					ind = client;
					grp = "";
					sql +="join t.comptePep c join c.individuel i ";
				} else if (client.equalsIgnoreCase("groupe")) {
					grp = client;
					ind = "";
					sql += "join t.comptePep c join c.groupe g ";
				}
				
			}
			
			if(!produit.equals("")){
				sql += "join t.comptePep c join c.produitEpargne p ";
			}
			sql += "where ";
				
			if(!agence.equals("")){
				sql+=" c.numCompte like '"+agence+"%' ";
				if(!client.equals("") || !produit.equals("") || !trans.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
					sql +="and ";
			}
			if(!client.equals("")){
				
				if(!ind.equals("") && grp.equals("")){
					sql += "c.individuel is not null ";
					if(!produit.equals("") || !trans.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
						sql +="and ";
				}
				if(ind.equals("") && !grp.equals("")){
					sql += "c.groupe is not null ";
					if(!produit.equals("") || !trans.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
						sql +="and ";
				}
			}
			if(!produit.equals("")){
				sql +="p.idProdEpargne = '"+ produit +"' ";
				if(!trans.equals("") || (!dateDeb.equals("") && !dateFin.equals("")))
					sql +="and ";
			}
			
			if(!trans.equals("")){
				sql += "t.typeTrans ='"+trans+"' ";
				if((!dateDeb.equals("") && !dateFin.equals("")))
					sql +="and ";
			}
			
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql +=" t.dateTransaction between '"+dateDeb+"' and '"+dateFin+"'";
			}
		}
		System.out.println("sql transaction compte PEP: "+ sql);
		TypedQuery<TransactionPep> q = em.createQuery(sql, TransactionPep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

	//Rapport relevés de compte
	@Override
	public List<TransactionPep> getReleverComptePEP(String numCompte) {
		String sql = "select t from TransactionPep t join t.comptePep c where "
				+ "c.numCompte = '"+numCompte+"' order by t.dateTransaction asc";
		TypedQuery<TransactionPep> q = em.createQuery(sql, TransactionPep.class);
		if(!q.getResultList().isEmpty())
			return q.getResultList();
		return null;
	}

}

