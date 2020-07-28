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
import mg.fidev.model.CatEpargne;
import mg.fidev.model.CompteDAT;
import mg.fidev.model.CompteEpargne;
import mg.fidev.model.CompteFerme;
import mg.fidev.model.ConfigGLDAT;
import mg.fidev.model.ConfigGeneralDAT;
import mg.fidev.model.ConfigGlEpargne;
import mg.fidev.model.ConfigInteretProdEp;
import mg.fidev.model.ConfigProdEp;
import mg.fidev.model.Grandlivre;
import mg.fidev.model.Groupe;
import mg.fidev.model.Individuel;
import mg.fidev.model.InteretEpargne;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TransactionEpargne;
import mg.fidev.model.TypeEpargne;
import mg.fidev.model.Utilisateur;
import mg.fidev.service.ProduitEpargneService;
import mg.fidev.utils.CalculDAT;
import mg.fidev.utils.CodeIncrement;
import mg.fidev.utils.FicheCaisseEpargne;
import mg.fidev.utils.SoldeEpargne;

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
	public List<ProduitEpargne> findAllProduit() {
		try {
			String sql = "select p from ProduitEpargne p";
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
			em.getTransaction().begin();
			em.remove(p); 
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
	 * CHERCHER COMPTE PAR NUM COMPTE
	 * ***/
	@Override
	public List<CompteEpargne> findCompteByCode(String numCmpt) {
		TypedQuery<CompteEpargne> query = em.createQuery("SELECT c FROM CompteEpargne c WHERE c.fermer = :x AND c.numCompteEp LIKE :code"
				,CompteEpargne.class);
		query.setParameter("x", false);
		query.setParameter("code", numCmpt+"%");
		
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
	public CompteEpargne ouvrirCompte(String dateOuverture,boolean geler,boolean pasRetrait
			,String dateRetirer, String idProduitEp,String individuelId, String groupeId, int userId) {
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
				cpt_ep.setDateEcheance(dateRetirer);
				cpt_ep.setUtilisateur(ut);
				cpt_ep.setProduitEpargne(pdt_ep);
				cpt_ep.setIsActif(true);
				if(ind != null){
					System.out.println("Compte individuel");
					String dateNais = ind.getDateNaissance();				
					LocalDate dateNow = LocalDate.now();
					
					int age = Period.between(LocalDate.parse(dateNais), dateNow).getYears();
					System.out.println("âge = "+age);
					if(age>=confProduit.getAgeMinCpt()){					
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
		return em.find(CompteEpargne.class, numCompte);
	}
	
	
	/***
	 * TRASACTION EPARGNE
	 * ***/
	@SuppressWarnings("unused")
	@Override
	public boolean trasactionEpargne(String typeTransac, String dateTransac,
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
			
			//Test des configurations
			if(cptEp != null && cptEp.getIsActif() == true && cptEp.isFermer() == false){
				System.out.println("Informations sur la transaction prêtes");
				try {
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
								System.out.println("Le montant est supérieur au solde d'ouverture ("+confProd.getSoldeOverture()+")");
								result = "Le montant est supérieur au solde d'ouverture ("+confProd.getSoldeOverture()+")";
								return false;
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
								System.out.println("Transaction réussie");
								result = "Transaction réussie";
								return true;
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
							System.out.println("Transaction réussie");
							result = "Transaction réussie";
							return true;							
						}	
					
					}//Si transaction est retrait d'épargne
					else if(typeTransac.equalsIgnoreCase("RE")){
						//Vérifier si le compte est autorisé à faire de rétrait
						if(cptEp.isPasRetrait() == true){
							
							System.out.println("Désolé, ce compte n'est pas autorisé à faire de retrait");
							result = "Désolé, ce compte n'est pas autorisé à faire de retrait";
							return false;
							
						}else{
							
							double sd = cptEp.getSolde() - montant;
							//Pour vérifier le solde minimum d'un compte
							if(sd < confInter.getSoldeMinInd() ){
								System.out.println("Montant superieur au montant autorisé");
								result = "Montant superieur au montant autorisé";
								return false;
							}else{		
								//Pour vérifier le nombre de jours entre deux retrait
								int diff = confProd.getNbrJrMinRet(); 
								String rq = "select max(t.dateTransaction) from TransactionEpargne t join "
										+ " t.compteEpargne c where c.numCompteEp='"+numCptEp+"' and t.typeTransEp ='RE'";
								Query qs = em.createQuery(rq);
								int v = confProd.getNbrJrMinRet();
								if(qs.getSingleResult() != null){
									String dernierTransaction = (String)qs.getSingleResult();
									long difDate = calcDate(dernierTransaction, dateTransac);
									v = (int)difDate;
								}
								
								System.out.println("valeur = "+ v);
								if(diff > v){
									int a = diff - v;
									System.out.println("Le retrait est autorisé après "+ a + " jours");
									result = "Le retrait est autorisé après "+ a + " jours";
									return false;
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
									System.out.println("Transaction réussie");							
									result = "Transaction réussie";
									return true;						
									
								}												
							}
						}					
						
					}
					
					return false;
				} catch (Exception e) {
					System.err.println(e.getMessage());
					result = "Erreur enregistrement";
					return false;
				}
			}
			else{
				System.out.println("Pas de transaction autorisé, car ce compte est inactif");
				result = "Pas de transaction autorisé, car ce compte est inactif";
				return false;
			}
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
			if(tr.getCompteEpargne().getIndividuel() != null) 
				ind = tr.getCompteEpargne().getIndividuel();
			if(tr.getCompteEpargne().getGroupe() != null)
				grp = tr.getCompteEpargne().getGroupe();
			
			double ancienMontant = tr.getMontant();
			
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
			
			if(tr.getTypeTransEp().equalsIgnoreCase("DE")){
				String desc = "Modification trans. du compte "+ce.getNumCompteEp();							
				
				ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, desc,
						pieceCompta, 0, (ancienMontant * -1), ut, ind, grp, ce, null, Deb);
				
				ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, desc,
						pieceCompta, (ancienMontant * -1), 0, ut, ind, grp, ce, null, Cred);
				
			}else if(tr.getTypeTransEp().equalsIgnoreCase("RE")){
				String desc = "Modification trans. du compte "+ce.getNumCompteEp();							
				ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, desc,
						pieceCompta, (ancienMontant * -1), 0, ut, ind, grp, ce, null, Deb);
				
				ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, desc,
						pieceCompta, 0, (ancienMontant * -1), ut, ind, grp, ce, null, Cred);

			}		
			
			String typ = "";
			String vp = "";
			
			Caisse cptCaisse = null;
			if(numTel.equals("") && numCheq.equals("") && nomCptCaisse.equals("")){
				typ = tr.getTypePaie();
				vp = tr.getValPaie();
				cptCaisse = tr.getCaisse();
			}else{
				if(typPaie.equalsIgnoreCase("cash")){
					cptCaisse = em.find(Caisse.class, nomCptCaisse);
					typ = "cash";							
					vp = String.valueOf(cptCaisse.getAccount().getNumCpt());		
				}else if(typPaie.equalsIgnoreCase("cheque")){
					vp = numCheq;
					typ = "cheque";		
				}else if(typPaie.equalsIgnoreCase("mobile")){
					vp = numTel;
					typ = "mobile";
				}
			}
			
			if(typeTrans.equals(""))
				typeTrans = tr.getTypeTransEp();
			
			tr.setTypeTransEp(typeTrans);
			tr.setDateTransaction(dateTrans);
			tr.setDescription(description);
			tr.setPieceCompta(pieceCompta);
			tr.setMontant(montant);
			tr.setTypePaie(typ);
			tr.setValPaie(vp); 
			tr.setUserUpdate(ut); 
			

			//Compte comptable à débiter
			Account accDeb = null;
			
			if(typ.equalsIgnoreCase("cash")){							
				accDeb = cptCaisse.getAccount();							
			}else if(typ.equalsIgnoreCase("cheque")){
				accDeb = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getCptCheque());
			}else if(typ.equalsIgnoreCase("mobile")){							
				accDeb = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
			}				
			//Compte comptable à créditer
			Account accCred = CodeIncrement.getAcount(em, ce.getProduitEpargne().getConfigGlEpargne().getEpargneInd());
			
			if(typeTrans.equalsIgnoreCase("DE")){
				
				ce.setSolde((ce.getSolde() + montant));				
				try {
					String nouvDesc = "Ressaisie trans. du compte "+ce.getNumCompteEp();							
					transaction.begin();
					em.flush();
					em.flush();
					transaction.commit();
					em.refresh(tr);
					em.refresh(ce);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, nouvDesc,
							pieceCompta, 0, montant, ut, ind, grp, ce, null, accDeb);
				
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, nouvDesc,
							pieceCompta, montant, 0, ut, ind, grp, ce, null, accCred);			
					
					System.out.println("Modification transaction réussie");
					return true;		
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur modification transaction");
				}
				
			}else if(typeTrans.equalsIgnoreCase("RE")){
				ce.setSolde((ce.getSolde() - montant));
				
				try {
					String nouvDesc = "Ressaisie trans. du compte "+ce.getNumCompteEp();							
					transaction.begin();
					em.flush();
					em.flush();
					transaction.commit();
					em.refresh(tr);
					em.refresh(ce);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, nouvDesc,
							pieceCompta, montant, 0, ut, ind, grp, ce, null, accDeb);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dateTrans, nouvDesc,
							pieceCompta, 0, montant, ut, ind, grp, ce, null, accCred);			
					
					System.out.println("Modification transaction réussie");
					return true;		
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur modification transaction");
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
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, 0, (tr.getMontant() * -1), ut, ind, grp, ce, null, Deb);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, (tr.getMontant() * -1), 0, ut, ind, grp, ce, null, Cred);
					
				}else if(tr.getTypeTransEp().equalsIgnoreCase("RE")){
					ce.setSolde((ce.getSolde() + tr.getMontant()));		
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, (tr.getMontant() * -1), 0, ut, ind, grp, ce, null, Deb);
					
					ComptabliteServiceImpl.saveImputationEpargne(tr.getIdTransactionEp(), dt.toString(), desc,
							piece, 0, (tr.getMontant() * -1), ut, ind, grp, ce, null, Cred);
					
				}		
				transaction.begin();
				em.flush();
				em.remove(tr);
				transaction.commit();
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
	static long calcDate(String dateDeb, String dateFin) {
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
							
							String indexTcode = CodeIncrement.genTcode(em);
							retirer.setIdTransactionEp(indexTcode);
							
//							Mouvement comptabilité
							Grandlivre lDebit = new Grandlivre();
							Grandlivre lCredit = new Grandlivre();
							
							//INFORMATION DEBIT
							lDebit.setPiece(pieceCompta);
							lDebit.setTcode(indexTcode);
							lDebit.setDate(dateTransac);
							
							lDebit.setUtilisateur(ut);
							lDebit.setCompteEpargne(cp1); 
							lDebit.setCodeInd(cp1.getIndividuel()); 
							
							lDebit.setUserId(ut.getNomUtilisateur());
							
							//INFORMATION CREDIT
							lCredit.setPiece(pieceCompta);
							lCredit.setTcode(indexTcode);
							lCredit.setDate(dateTransac);
							
							lCredit.setUtilisateur(ut);
							lCredit.setCompteEpargne(cp1);
							lCredit.setCodeInd(cp1.getIndividuel()); 
							
							lCredit.setUserId(ut.getNomUtilisateur());
							
							//Ajout compte comptable au grand Livre
							//Débit
							Account AcDebit = CodeIncrement.getAcount(em,
									cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							double solProgressifDebit = AcDebit.getSoldeProgressif() + montant;
							
							lDebit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							lDebit.setAccount(AcDebit);
							lDebit.setDebit(montant);
							lDebit.setDescr("Virement de compte "+cmpt1+" vers "+cmpt2);
							
							lDebit.setSolde(solProgressifDebit); 
							AcDebit.setSoldeProgressif(solProgressifDebit);
							
							//Crédit
							lCredit.setCredit(montant);
							lCredit.setDescr("Virement de compte "+cmpt1+" vers "+cmpt2);
							
							Account accCredit;
							
							//Ajout compte comptable au grand livre
							if(typPaie.equalsIgnoreCase("cash")){
								accCredit = cptCaisse.getAccount();
								double soldCredit = accCredit.getSoldeProgressif() - montant;
								lCredit.setCompte(cptC);								
								lCredit.setAccount(accCredit);
								
								lCredit.setSolde(soldCredit); 
								accCredit.setSoldeProgressif(soldCredit);
								
							}else if(typPaie.equalsIgnoreCase("cheque")){
								accCredit = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());
								
								double soldCredit = accCredit.getSoldeProgressif() - montant;
								
								lCredit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());
								lCredit.setAccount(accCredit);
								
								lCredit.setSolde(soldCredit); 
								accCredit.setSoldeProgressif(soldCredit);
								
							}else if(typPaie.equalsIgnoreCase("mobile")){
								
								accCredit = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
								
								double soldCredit = accCredit.getSoldeProgressif() - montant;
								
								lCredit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());	
								lCredit.setAccount(accCredit);
								
								lCredit.setSolde(soldCredit); 
								accCredit.setSoldeProgressif(soldCredit);
								
							}
							//Enregistrement au grand livre s'il y a une commission lors d'une transfer.
							//Si montant de commission lors de la configuration du produit est different de 0 
							if(cp1.getProduitEpargne().getConfigProdEp().getCommTransfer() != 0){
								Grandlivre gl = new Grandlivre();
								Account accGl = CodeIncrement.getAcount(em, 
										cp1.getProduitEpargne().getConfigGlEpargne().getCommEpargne());
								
								double sdl = accGl.getSoldeProgressif() + cp1.getProduitEpargne().getConfigProdEp().getCommTransfer();
								
								gl.setPiece(pieceCompta);
								gl.setTcode(indexTcode);
								gl.setDate(dateTransac);
								gl.setUserId(ut.getNomUtilisateur());
								gl.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getCommEpargne());
								gl.setDebit(cp1.getProduitEpargne().getConfigProdEp().getCommTransfer());
								gl.setDescr("Commission sur transfer de compte "+cmpt1+" vers "+cmpt2);
															
								gl.setUtilisateur(ut);
								gl.setCompteEpargne(cp1);
								gl.setCodeInd(cp1.getIndividuel());
								gl.setAccount(accGl);
								
								gl.setSolde(sdl); 
								accGl.setSoldeProgressif(sdl);							
								
								transaction.begin();
								em.flush();
								em.persist(gl);
								transaction.commit();
							}
							
							transaction.begin();
							em.flush();
							em.flush();
							em.flush();
							em.persist(retirer);
							em.persist(lDebit);
							em.persist(lCredit);
							transaction.commit();
							em.refresh(retirer);
							
						}
						
						if(depot.getTypeTransEp().equals("DE")){
							
							String indexTcode = CodeIncrement.genTcode(em);
							
//							Mouvement comptabilité
							Grandlivre lDebit = new Grandlivre();
							Grandlivre lCredit = new Grandlivre();
							
							//INFORMATION DEBIT
							lDebit.setPiece(pieceCompta);
							lDebit.setTcode(indexTcode);
							lDebit.setDate(dateTransac);
							lDebit.setUserId(ut.getNomUtilisateur());
							
							lDebit.setUtilisateur(ut);
							lDebit.setCompteEpargne(cp2); 
							lDebit.setCodeInd(cp1.getIndividuel()); 
							
							//INFORMATION CREDIT
							lCredit.setPiece(pieceCompta);
							lCredit.setTcode(indexTcode);
							lCredit.setDate(dateTransac);
							lCredit.setUserId(ut.getNomUtilisateur());
							
							lCredit.setUtilisateur(ut);
							lCredit.setCompteEpargne(cp2); 
							lCredit.setCodeInd(cp1.getIndividuel()); 
							
							//Débit
							lDebit.setDebit(montant);
							lDebit.setDescr("Virement de compte "+cmpt1+" vers "+cmpt2);
							
							//Compte comptable à débiter							
							Account accDb;
							
							//Ajout compte comptable en fonction de mode de payment
							//si mode de payement est cash
							if(typPaie.equalsIgnoreCase("cash")){
								accDb = cptCaisse.getAccount();
								
								lDebit.setCompte(cptC);
								lDebit.setAccount(accDb);
								
								double sdDeb = accDb.getSoldeProgressif() + montant;
								lDebit.setSolde(sdDeb); 
								accDb.setSoldeProgressif(sdDeb);
							
								//Si mode payement est chèque
							}else if(typPaie.equalsIgnoreCase("cheque")){
								accDb = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());
								
								lDebit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getCptVireCheque());
								lDebit.setAccount(accDb);
								
								double sdDeb = accDb.getSoldeProgressif() + montant;
								lDebit.setSolde(sdDeb); 
								accDb.setSoldeProgressif(sdDeb);
								
								//si mode de payement est mobile
							}else if(typPaie.equalsIgnoreCase("mobile")){
								accDb = CodeIncrement.getAcount(em,
										cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
								
								
								lDebit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getChargeSMScpt());
								lDebit.setAccount(accDb);
								
								double sdDeb = accDb.getSoldeProgressif() + montant;
								lDebit.setSolde(sdDeb); 
								accDb.setSoldeProgressif(sdDeb);							
								
							}
							
							//Compte comptable à créditer
							
							Account accCredits=CodeIncrement.getAcount(em,
									cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							
							double sdcrd = accCredits.getSoldeProgressif() - montant;
							
							lCredit.setCompte(cp1.getProduitEpargne().getConfigGlEpargne().getVirePermCptTit());
							lCredit.setAccount(accCredits);
							lCredit.setCredit(montant);
							lCredit.setDescr("Virement de compte "+cmpt1+" vers "+cmpt2);
							
							lCredit.setSolde(sdcrd); 
							accCredits.setSoldeProgressif(sdcrd);
							
							/*String indexTcodeDep = CodeIncrement.genTcode(em);							
							int cod = Integer.parseInt(indexTcodeDep)+1;*/
							depot.setIdTransactionEp(indexTcode);
							
							transaction.begin();
							em.flush();
							em.flush();
							em.flush();
							em.persist(depot);
							em.persist(lCredit);
							em.persist(lDebit);
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
	
	/***
	 * CALCUL INTERET
	 * ***/
	@Override
	public List<InteretEpargne> calcInteret(String idProd, String date1, String date2) { 
		//Instance des classes necessaire		
		//seléction du produit entrée au parametre et liste des comptes de ce produit
		ProduitEpargne produit = em.find(ProduitEpargne.class, idProd);		
		List<CompteEpargne> listComp = produit.getCompteEpargnes();
			
		//valeur de retour de la methode
		List<InteretEpargne> retour = new ArrayList<InteretEpargne>();
		//Instance de configuration intérêt du produit  
		ConfigInteretProdEp confInteret = produit.getConfigInteretProdEp();
		//ConfigGlEpargne confGL = produit.getConfigGlEpargne();
		
		String datdeb = date1;
		String datfin = date2;	
			
			switch (confInteret.getModeCalcul()) {		
			case "Solde actuel":
				for (int k = 0; k < listComp.size(); k++) {	
					
					InteretEpargne saveInt = new InteretEpargne();
					
					String numCpt = listComp.get(k).getNumCompteEp();
					CompteEpargne cmpte = listComp.get(k);					
					
					//double interet = 0.0;
					
					LocalDate dateFinMois = LocalDate.parse(datfin); 
					
					System.out.println(datdeb);
					System.out.println(dateFinMois+"\n");
					//recuperer tout transaction du mois en cours
					List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
					
					TypedQuery<TransactionEpargne> q = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
							+ "WHERE c.numCompteEp=:id AND"
							+ " t.dateTransaction BETWEEN :dateDeb AND :dateFin ORDER BY t.dateTransaction ASC",TransactionEpargne.class);
					q.setParameter("id", numCpt);
					q.setParameter("dateDeb", datdeb);
					q.setParameter("dateFin", datfin);
					if(!q.getResultList().isEmpty()){
						result = q.getResultList();
					}
				
					// intitialisation des variable utilisé, val = difference du date
					// dépuis le dernier transaction
					// somme = somme solde progressif de ce compte
					int val = 0;
					double somme = 0.0;
					double derSome = 0.0;
	
					for (int i = 0; i < result.size() - 1; i++) {
						val = Period.between(LocalDate.parse(result.get(i).getDateTransaction()),
										LocalDate.parse(result.get(i + 1).getDateTransaction())).getDays();
	
						System.out.println("premier transaction : "+result.get(i).getDateTransaction()
								+" transaction suivant : "+result.get(i + 1).getDateTransaction()+" difference de date de transaction :"+val);
	
						somme += (result.get(i).getSolde() * val / produit.getConfigInteretProdEp().getNbrJrInt());
	
						System.out.println("somme=" + result.get(i).getSolde() + "*"+ 
						val + "/" + produit.getConfigInteretProdEp().getNbrJrInt());
	
						if (i == result.size() - 2) {
							int v = Period.between(	LocalDate.parse(result.get(result.size() - 1)
											.getDateTransaction()), dateFinMois).getDays();
							int b = v + 1;
	
							derSome = result.get(result.size() - 1).getSolde() * b/ produit.getConfigInteretProdEp().getNbrJrInt();
	
							System.out.println(v);
							System.out.println(b);
							System.out.println("derSome="+ result.get(result.size() - 1).getSolde() + "*"+ b + "/365 \n");
						}
					}
	
					double Interet = (int) (somme + derSome) * produit.getConfigInteretProdEp().getTauxInteret() / 100;
					System.out.println(Interet);
					
					System.out.println(listComp.get(k).getNumCompteEp() + " solde actuel :"
							+ listComp.get(k).getSolde() +" Intérêt: "+Interet);
	
					saveInt.setDate(datfin);
					saveInt.setMontant(Interet);
					saveInt.setSolde(cmpte.getSolde());
					saveInt.setCompte(cmpte);
					cmpte.setSolde(cmpte.getSolde() + Interet);
	
					try {
						transaction.begin();
						em.merge(cmpte);
						em.persist(saveInt);
						transaction.commit();
						System.out.println("Enregistrement d'intérêt du compte "+numCpt);
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
				break;
			case "Solde minimum mensuel":
				for (int k = 0; k < listComp.size(); k++) {
					
					//Classe interetEpargne pour historiser l'intérêt d'une compte après le calcul
					InteretEpargne saveInt = new InteretEpargne();
										
					String numCpt = listComp.get(k).getNumCompteEp();
					CompteEpargne cmpte = listComp.get(k);					
					
					double interet = 0.0;
					
					TypedQuery<TransactionEpargne> query = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
							+ "WHERE c.numCompteEp=:id AND t.dateTransaction < :dateDeb ",TransactionEpargne.class);
					query.setParameter("id", numCpt);
					query.setParameter("dateDeb", datdeb);
					
					if(!query.getResultList().isEmpty()){
						
						Query query1 = em.createQuery("SELECT MIN(tr.solde) FROM TransactionEpargne tr JOIN tr.compteEpargne cpe "
								+ "WHERE cpe.numCompteEp=:id AND tr.dateTransaction BETWEEN :dateDeb1 AND :dateFin1 ");
						query1.setParameter("id", numCpt);
						query1.setParameter("dateDeb1", datdeb);
						query1.setParameter("dateFin1", datfin);
						
						double soldMin = (double)query1.getSingleResult();
						
						System.out.println(listComp.get(k).getNumCompteEp() + " solde actuel :"
								+ listComp.get(k).getSolde() +" solde minimum du mois: "+soldMin);
						
						interet = (int) (soldMin*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
//						DecimalFormat df = new DecimalFormat("0.00");
//						String s = df.format(mo);
						
					}else{
						interet = 0.0;
						System.out.println("L'intérêt de ce compte pour mois ci est 0.0");
					}
					
					System.out.println("l'intérêt entre "+datdeb+" et "+datfin +" est de: "+ interet);
	
					saveInt.setDate(datfin);
					saveInt.setMontant(interet);
					saveInt.setSolde(cmpte.getSolde());
					saveInt.setCompte(cmpte);
					cmpte.setSolde(cmpte.getSolde() + interet);
	
					try {
						transaction.begin();
						em.flush();
						em.persist(saveInt);
						transaction.commit();
						System.out.println("Enregistrement d'intérêt du compte "+numCpt);	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "Solde moyen mensuel":
				for (int k = 0; k < listComp.size(); k++) {
					
					//Classe interetEpargne pour historiser l'intérêt d'une compte après le calcul
					InteretEpargne saveInt = new InteretEpargne();
										
					String numCpt = listComp.get(k).getNumCompteEp();
					CompteEpargne cmpte = listComp.get(k);	
					
					//double interet = 0.0;
					//double interMensuel = 0.0;
					
					TypedQuery<TransactionEpargne> quer2 = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
							+ "WHERE c.numCompteEp=:id AND t.dateTransaction < :dateDeb ",TransactionEpargne.class);
					quer2.setParameter("id", numCpt);
					quer2.setParameter("dateDeb", datdeb);
					
					if(!quer2.getResultList().isEmpty()){
						
						TypedQuery<TransactionEpargne> query1 = em.createQuery("SELECT t FROM TransactionEpargne t JOIN t.compteEpargne c "
								+ "WHERE c.numCompteEp=:id AND"
								+ " t.dateTransaction BETWEEN :dateDeb AND :dateFin ",TransactionEpargne.class);
						
						
						query1.setParameter("id", numCpt);
						query1.setParameter("dateDeb", datdeb);
						query1.setParameter("dateFin", datfin);
						
						//double soldMin = (double)query1.getSingleResult();
						
						//interet = (soldMin*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
						
					}else{
						//double interet = 0.0;
						System.out.println("L'intérêt de ce compte pour mois ci est 0.0");
					}
					
					//System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interet);
	
					saveInt.setDate(datfin);
					//saveInt.setMontant(interet);
					saveInt.setCompte(cmpte);
					//cmpte.setSolde(cmpte.getSolde() + interet);
					
					/*((Solde au début du mois + Solde à la fin du mois) / 2) x Taux d’intérêt annuel / 100) 
					  Nombre de mois dans l’année) */ 
	
					try {
						transaction.begin();
						em.flush();
						em.persist(saveInt);
						transaction.commit();
	
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}
				break;
				
			case "Solde fin periode":
				
				for (int k = 0; k < listComp.size(); k++) {
					
					//Classe interetEpargne pour historiser l'intérêt d'une compte après le calcul
					InteretEpargne saveInt = new InteretEpargne();
										
					String numCpt = listComp.get(k).getNumCompteEp();
					CompteEpargne cmpte = listComp.get(k);	
				
					Query quers = em.createQuery("SELECT t FROM TransactionEpargne t "
							+ "WHERE t.dateTransaction = (SELECT MAX(tr.dateTransaction) FROM TransactionEpargne tr "
							+ "JOIN tr.compteEpargne c WHERE c.numCompteEp=:id AND "
							+ "tr.dateTransaction BETWEEN :dateDeb AND :dateFin )");
					quers.setParameter("id", numCpt);
					quers.setParameter("dateDeb", datdeb);
					quers.setParameter("dateFin", datfin);
					
					TransactionEpargne te = (TransactionEpargne) quers.getSingleResult();
					
					double mont = te.getSolde();
					
					double interetFinPeriode = 
							(int)((mont*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret())*Double.parseDouble(datfin.substring(5,7));
					
					System.out.println("l'intérêt du mois de "+datfin +"est de: "+ interetFinPeriode);
	
					saveInt.setDate(datfin);
					saveInt.setMontant(interetFinPeriode);
					saveInt.setSolde(cmpte.getSolde());
					saveInt.setCompte(cmpte);
					cmpte.setSolde(cmpte.getSolde() + interetFinPeriode);
	
					try {
						transaction.begin();
						em.flush();
						em.persist(saveInt);
						transaction.commit();
						System.out.println("Enregistrement d'intérêt du compte "+numCpt);
	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
				
			case "Solde fin mois":
				
				for (int k = 0; k < listComp.size(); k++) {
					
					//Classe interetEpargne pour historiser l'intérêt d'une compte après le calcul
					InteretEpargne saveInt = new InteretEpargne();
										
					String numCpt = listComp.get(k).getNumCompteEp();
					CompteEpargne cmpte = listComp.get(k);	
				
					Query quers2 = em.createQuery("SELECT t FROM TransactionEpargne t "
							+ "WHERE t.dateTransaction = (SELECT MAX(tr.dateTransaction) FROM TransactionEpargne tr "
							+ "JOIN tr.compteEpargne c WHERE c.numCompteEp=:id AND "
							+ "tr.dateTransaction BETWEEN :dateDeb AND :dateFin )");
					quers2.setParameter("id", numCpt);
					quers2.setParameter("dateDeb", datdeb);
					quers2.setParameter("dateFin", datfin);
					
					TransactionEpargne te2 = (TransactionEpargne) quers2.getSingleResult();
					
					double mont2 = te2.getSolde();
					
					double interetFinPeriode2 = (int) (mont2*confInteret.getTauxInteret()/100)/confInteret.getPeriodeInteret();
					
					System.out.println("l'intérêt du mois de "+datfin +" est de: "+ interetFinPeriode2);
	
					saveInt.setDate(datfin);
					saveInt.setMontant(interetFinPeriode2);
					saveInt.setSolde(cmpte.getSolde());;
					saveInt.setCompte(cmpte);
					cmpte.setSolde(cmpte.getSolde() + interetFinPeriode2);
	
					try {
						transaction.begin();
						em.flush();
						em.persist(saveInt);
						transaction.commit();
						System.out.println("Enregistrement d'intérêt du compte "+numCpt);;
	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
				break;
				
			case "Solde courant":
				
				break;
				
			default:
				break;
			
		}
		TypedQuery<InteretEpargne> qrs = em.createQuery("SELECT i FROM InteretEpargne i",InteretEpargne.class);
		retour = qrs.getResultList();
		return retour;
	}
	
	//----------------------------------------------------------------------------------------------------------
	
  /***
  * Rapport transaction d'épargne
  * ***/
	@Override
	public List<TransactionEpargne> rapportTransactions(String dateDeb, String dateFin) {
		List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
		
		//si les critères sont null
		String sql = "SELECT t FROM TransactionEpargne t ";
		
		//si date debut ou date fin ne sont pas null
		if(!dateDeb.equals("") || !dateFin.equals("") ){
		
			//rapport dans une date
			if(!dateDeb.equals("") && dateFin.equals("")){
				sql += " WHERE t.dateTransaction ='"+dateDeb+"' ORDER BY t.dateTransaction ASC";
			}
			//rapport entre deux date
			if(!dateDeb.equals("") && !dateFin.equals("")){
				sql +=" WHERE t.dateTransaction BETWEEN '"+dateDeb+"' AND '"+dateFin+"' ORDER BY t.dateTransaction ASC";
			}
			
		}
		TypedQuery<TransactionEpargne> q = em.createQuery(sql,TransactionEpargne.class);
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
	static List<CompteEpargne> getCompteDistinc(String idProd){
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		String sql="select distinct c.compteEpargne from TransactionEpargne c ";
		if(!idProd.equals("")){
			sql+=" join c.compteEpargne e join e.produitEpargne p where p.idProdEpargne = '"+idProd+"'";
		}
		
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
	public List<SoldeEpargne> getSoldeEpargne(String idProd) {
		List<SoldeEpargne> result = new ArrayList<SoldeEpargne>();
		
		List<CompteEpargne> compteEp = getCompteDistinc(idProd);
		
		//TransactionEpargne t = new TransactionEpargne();
		
		for (CompteEpargne compte : compteEp) {
			String numCmpt = compte.getNumCompteEp();
			System.out.println(numCmpt);
			SoldeEpargne donnee = new SoldeEpargne();
			
			Query q = em.createQuery("SELECT MAX(t.dateTransaction) FROM TransactionEpargne t JOIN t.compteEpargne cp "
					+ "WHERE cp.numCompteEp = :x ");
			q.setParameter("x", numCmpt);
			
			String dateMax = "";
			double montant = 0;
			
			if(q.getSingleResult() != null){
				dateMax = (String) q.getSingleResult();
				Query query = em.createQuery("select sum(t.montant) from TransactionEpargne t join t.compteEpargne e"
						+ " where t.dateTransaction = '"+dateMax+"' and e.numCompteEp='"+compte.getNumCompteEp()+"'" );
				if(query.getSingleResult() != null)
					montant = (double) query.getSingleResult();
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
			donnee.setSolde(compte.getSolde());
			donnee.setDateTrans(dateMax);
			donnee.setFermer(fermer);
			donnee.setSoldeFinPeriode(0);
			
			result.add(donnee);
			
		}
		
		return result;
		
	}
	
	//Solde Min/Max
	@Override
	public List<SoldeEpargne> getSoldeMinMax(String date1, String date2) {
		//retour
		List<SoldeEpargne> result = new ArrayList<SoldeEpargne>();
		String idProd = "";
		
		//recupération des comptes qui avait une transaction
		List<CompteEpargne> compteEp = getCompteDistinc(idProd);
		
		for (CompteEpargne compte : compteEp) {
			String numCmpt = compte.getNumCompteEp();
			SoldeEpargne donnee = new SoldeEpargne();
			
			double soldMin = 0;
			double soldMax = 0;
			
			String sql = "select min(t.solde) from TransactionEpargne t join t.compteEpargne cp "
					+ " where cp.numCompteEp = '"+numCmpt+"' and t.dateTransaction between '"+date1+"' and '"+date2+"'";
			
			Query q = em.createQuery(sql);
			
			if(q.getSingleResult() != null)
				soldMin = (double) q.getSingleResult();
			
			String sql2 = "select max(t.solde) from TransactionEpargne t join t.compteEpargne cp "
					+ " where cp.numCompteEp = '"+numCmpt+"' and t.dateTransaction between '"+date1+"' and '"+date2+"'";
			
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
	public List<CompteEpargne> rapportNouveauCompte(String dateDeb,
			String dateFin) {
		List<CompteEpargne> result = new ArrayList<CompteEpargne>();
		
		String sql = "select c from CompteEpargne c";
		
		if(!dateDeb.equals("") && dateFin.equals("")){
			sql += " where c.dateOuverture='"+dateDeb+"'";
		}
		if(!dateDeb.equals("") && !dateFin.equals("")){
			sql += " where c.dateOuverture between '"+dateDeb+"' and '"+dateFin+"'";
		}
		
		TypedQuery<CompteEpargne> query = em.createQuery(sql,CompteEpargne.class);
		
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
		}
		
		return null;
	}

	//Liste compte fermer
	@Override
	public List<CompteFerme> getCompteFermer(String dateDeb, String dateFin) {
		List<CompteFerme> result = new ArrayList<CompteFerme>();
		
		String sql = "select c from CompteFerme c";
		
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
	public List<TransactionEpargne> getFicheJournalier(String date) {
		List<TransactionEpargne> result = new ArrayList<TransactionEpargne>();
		TypedQuery<TransactionEpargne> query = em.createQuery(
		"select t from TransactionEpargne t where t.dateTransaction =:dat",TransactionEpargne.class);
		query.setParameter("dat", date);
		if(!query.getResultList().isEmpty()){
			result = query.getResultList();
			return result;
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
						Grandlivre credit = new Grandlivre();
						Grandlivre debit = new Grandlivre();
						String tcode = CodeIncrement.genTcode(em);
						
						//Information dépôt à terme
						compte.setInteret(interet); 
						if(ind != null){
							System.out.println("Dépôt à terme pour client individuel");
							compte.setNumCompteDAT(ind.getCodeInd() +"/"+produit.getIdProdEpargne());
							compte.setIndividuel(ind); 
							credit.setCodeInd(ind); 
							debit.setCodeInd(ind); 
						}
						if(grp != null){
							System.out.println("Dépôt à terme pour client groupe");
							compte.setNumCompteDAT(grp.getCodeGrp() +"/"+produit.getIdProdEpargne());
							compte.setGroupe(grp); 
							credit.setGroupe(grp); 
							debit.setGroupe(grp); 
						}
						
						//Enregistrement dans grand livre des opérations sur dépôt à terme	
						//Information crédit
						Account accCredit = CodeIncrement.getAcount(em, confGL.getCmptDAT());
						double sdCred = accCredit.getSoldeProgressif() - compte.getMontant();
						
						credit.setTcode(tcode);
						credit.setDate(compte.getDateDepot());
						credit.setPiece(piece);
						credit.setDescr("Dépôt à terme du compte "+compte.getNumCompteDAT());
						credit.setUtilisateur(ut); 
						credit.setCompteDat(compte);
						credit.setCredit(compte.getMontant());
						
						credit.setCompte(accCredit.getNumCpt());
						credit.setAccount(accCredit); 
						credit.setSolde(sdCred); 
						accCredit.setSoldeProgressif(sdCred); 
						
						//Information débit
						debit.setTcode(tcode);
						debit.setPiece(piece); 
						debit.setDate(compte.getDateDepot());
						debit.setDescr("Dépôt à terme du compte "+compte.getNumCompteDAT());
						debit.setUtilisateur(ut); 
						debit.setCompteDat(compte);
						debit.setDebit(compte.getMontant());
						
						Account accDeb;
						double soldDeb;
						switch (typePaie) {
						case "cash":
							//Compte caisse
							Caisse cpt = em.find(Caisse.class, nomCptCaisse);
							String c = String.valueOf(cpt.getAccount().getNumCpt());
							
						    accDeb = CodeIncrement.getAcount(em, c);
							soldDeb = accDeb.getSoldeProgressif() + compte.getMontant();
							
							debit.setCompte(accDeb.getNumCpt());
							debit.setAccount(accDeb); 
							debit.setSolde(soldDeb);
							accDeb.setSoldeProgressif(soldDeb); 
							compte.setProduitEpargne(produit); 	
							compte.setUtilisateur(ut); 
							compte.setRet(false); 	
							compte.setFermer(false); 
							try {
								transaction.begin();
								em.flush();
								em.flush();
								em.flush();
								em.persist(debit);
								em.persist(credit);
								em.persist(compte); 
								transaction.commit();
								em.refresh(debit);
								em.refresh(credit);
								em.refresh(compte);
								System.out.println("Enregistrement reussit");
								retour = "Enregistrement reussit";
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Erreur enregistrement");
								retour = "Erreur enregistrement";
							}
							
							break;
						case "mobile":
							//Compte à la configuration
							accDeb = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
							soldDeb =  accDeb.getSoldeProgressif() + compte.getMontant();
							
							debit.setCompte(accDeb.getNumCpt());
							debit.setAccount(accDeb); 
							debit.setSolde(soldDeb);
							accDeb.setSoldeProgressif(soldDeb); 
							compte.setProduitEpargne(produit); 	
							compte.setUtilisateur(ut); 
							compte.setRet(false); 	
							compte.setFermer(false); 
							try {
								transaction.begin();
								em.flush();
								em.flush();
								em.flush();
								em.persist(debit);
								em.persist(credit);
								em.persist(compte); 
								transaction.commit();
								em.refresh(debit);
								em.refresh(credit);
								em.refresh(compte);
								System.out.println("Enregistrement reussit");
								retour = "Enregistrement reussit";
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Erreur enregistrement");
								retour = "Erreur enregistrement";
							}
							break;
						case "cheque":
							//Compte à la configuration
							accDeb = CodeIncrement.getAcount(em, confGL.getCmptCheque());
							soldDeb =  accDeb.getSoldeProgressif() + compte.getMontant();
							
							debit.setCompte(accDeb.getNumCpt());
							debit.setAccount(accDeb); 
							debit.setSolde(soldDeb);
							accDeb.setSoldeProgressif(soldDeb); 
							compte.setProduitEpargne(produit); 	
							compte.setUtilisateur(ut); 
							compte.setRet(false); 	
							compte.setFermer(false); 
							try {
								transaction.begin();
								em.flush();
								em.flush();
								em.flush();
								em.persist(debit);
								em.persist(credit);
								em.persist(compte); 
								transaction.commit();
								em.refresh(debit);
								em.refresh(credit);
								em.refresh(compte);
								System.out.println("Enregistrement reussit");
								retour = "Enregistrement reussit";
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Erreur enregistrement");
								retour = "Erreur enregistrement";
							}
							break;
						case "epargne":
							//Compte à la configuration
							CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
							
							System.out.println("compte "+cptEp.getNumCompteEp());
							
							TransactionEpargne trans = new TransactionEpargne();
							if(cptEp.getSolde() > compte.getMontant() ){
								
								if(cptEp.isPasRetrait() == true){
									retour = "ce compte n'est pas autorisé à faire de retrait ";
								}else{
									
									double tr = cptEp.getSolde() - compte.getMontant();
									if(cptEp.getProduitEpargne().getConfigInteretProdEp().getSoldeMinInd() < tr ){
										accDeb = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
										soldDeb =  accDeb.getSoldeProgressif() + compte.getMontant();
										
										debit.setCompte(accDeb.getNumCpt());
										debit.setAccount(accDeb); 
										debit.setSolde(soldDeb);
										accDeb.setSoldeProgressif(soldDeb); 
										
										//Initialisation des informations de transaction
										trans.setDateTransaction(compte.getDateDepot());
										trans.setTypeTransEp("RE");
										trans.setMontant(compte.getMontant());
										trans.setDescription("Transferer au DAT du compte "+compte.getNumCompteDAT());
										trans.setPieceCompta(piece);
										trans.setIdTransactionEp(tcode);
										trans.setCompteEpargne(cptEp);
										trans.setTypePaie("transfer");
										trans.setValPaie(compte.getNumCompteDAT());
										
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
										
										compte.setProduitEpargne(produit); 	
										compte.setUtilisateur(ut); 
										compte.setRet(false); 	
										compte.setFermer(false); 
										try {
											transaction.begin();
											em.flush();
											em.flush();
											em.flush();
											em.persist(debit);
											em.persist(credit);
											em.persist(compte); 
											transaction.commit();
											em.refresh(debit);
											em.refresh(credit);
											em.refresh(compte);
											System.out.println("Enregistrement reussit");
											retour = "Enregistrement reussit";
										} catch (Exception e) {
											e.printStackTrace();
											System.out.println("Erreur enregistrement");
											retour = "Erreur enregistrement";
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
			
			//Frais de dépôt à terme			
			if(taxe != 0 && interet != 0){
				Grandlivre g1 = new Grandlivre();
				Grandlivre g2 = new Grandlivre();
		
				ConfigGLDAT confGL = dat.getProduitEpargne().getConfigGlDat();
				//Information crédit
				Account taxCredit = CodeIncrement.getAcount(em, confGL.getCmptDAT());
				double sdC = taxCredit.getSoldeProgressif() - taxe;
				
				g1.setTcode(tcode);
				g1.setDate(date);
				g1.setPiece(piece); 
				g1.setDescr("Taxe retenue dépôt à terme du compte "+dat.getNumCompteDAT());
				g1.setUtilisateur(ut); 
				g1.setCompteDat(dat);
				g1.setCredit(taxe);
				
				g1.setCompte(taxCredit.getNumCpt());
				g1.setAccount(taxCredit); 
				g1.setSolde(sdC); 
				taxCredit.setSoldeProgressif(sdC); 
				
				//Information débit
				
				Account taxdebit = CodeIncrement.getAcount(em, confGL.getCmptTaxeRetenu());
				double sdD = taxdebit.getSoldeProgressif() + taxe;
				
				g2.setTcode(tcode);
				g2.setDate(date);
				g2.setPiece(piece); 
				g2.setDescr("Taxe retenue dépôt à terme du compte "+dat.getNumCompteDAT());
				g2.setUtilisateur(ut); 
				g2.setCompteDat(dat);
				g2.setDebit(taxe);
				
				g2.setCompte(taxdebit.getNumCpt());
				g2.setAccount(taxdebit); 
				g2.setSolde(sdD); 
				taxdebit.setSoldeProgressif(sdD); 
				
				try {
					transaction.begin();
					em.flush();
					em.flush();
					em.persist(g1);
					em.persist(g2);
					transaction.commit();
					em.refresh(g1);
					em.refresh(g2); 
					System.out.println("Enregistrement taxe au grand livre");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//Imputation penalité sur grand livre
			ConfigGLDAT confGL = dat.getProduitEpargne().getConfigGlDat();
			if(penalite != 0){
				Grandlivre g1 = new Grandlivre();
				Grandlivre g2 = new Grandlivre();
			
				//Information crédit
				Account penalCredit = CodeIncrement.getAcount(em, confGL.getCmptDAT());
				double sdC = penalCredit.getSoldeProgressif() - penalite;
				
				g1.setTcode(tcode);
				g1.setDate(date);
				g1.setPiece(piece); 
				g1.setDescr("Penalité dépôt à terme du compte "+dat.getNumCompteDAT());
				g1.setUtilisateur(ut); 
				g1.setCompteDat(dat);
				g1.setCredit(penalite);
				
				g1.setCompte(penalCredit.getNumCpt());
				g1.setAccount(penalCredit); 
				g1.setSolde(sdC); 
				penalCredit.setSoldeProgressif(sdC); 
				
				//Information débit				
				Account penalitedebit = CodeIncrement.getAcount(em, confGL.getCmptPenalDAT());
				double sdD = penalitedebit.getSoldeProgressif() + penalite;
				
				g2.setTcode(tcode);
				g2.setDate(date);
				g2.setPiece(piece); 
				g2.setDescr("Penalité dépôt à terme du compte "+dat.getNumCompteDAT());
				g2.setUtilisateur(ut); 
				g2.setCompteDat(dat);
				g2.setDebit(penalite);
				
				g2.setCompte(penalitedebit.getNumCpt());
				g2.setAccount(penalitedebit); 
				g2.setSolde(sdD); 
				penalitedebit.setSoldeProgressif(sdD); 
				
				try {
					transaction.begin();
					em.flush();
					em.flush();
					em.persist(g1);
					em.persist(g2);
					transaction.commit(); 
					em.refresh(g1);
					em.refresh(g2); 
					System.out.println("Enregistrement taxe au grand livre");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//Imputation retrait sur dépôt à terme
			dat.setDateRetrait(date);
			dat.setTotalIntRetrait(interet);
			dat.setTaxe(taxe);
			dat.setPenalite(penalite);
			dat.setTotal(montant); 
			dat.setRet(true); 
	
			//Enregistrement dans grand livre des opérations sur dépôt à terme	
			//Information Débit
			Grandlivre debit = new Grandlivre();
			Grandlivre credit = new Grandlivre();
			
			Account accDebit = CodeIncrement.getAcount(em, confGL.getCmptDAT());
			double sdDebit = accDebit.getSoldeProgressif() + montant;
			
			debit.setTcode(tcode);
			debit.setDate(date);
			debit.setPiece(piece); 
			debit.setDescr("Retrait dépôt à terme du compte "+dat.getNumCompteDAT());
			debit.setUtilisateur(ut); 
			debit.setCompteDat(dat);
			debit.setDebit(montant);
			
			debit.setCompte(accDebit.getNumCpt());
			debit.setAccount(accDebit); 
			debit.setSolde(sdDebit); 
			accDebit.setSoldeProgressif(sdDebit); 
			
			//Information Crédit
			credit.setTcode(tcode);
			credit.setDate(date);
			credit.setPiece(piece); 
			credit.setDescr("Retrait dépôt à terme du compte "+dat.getNumCompteDAT());
			credit.setUtilisateur(ut); 
			credit.setCompteDat(dat);
			credit.setCredit(montant);
			
			Account accCredit;
			double soldCredit;
			switch (typePaie) {
			case "cash":
				//Compte caisse
				Caisse cpt = em.find(Caisse.class, nomCptCaisse);
				String c = String.valueOf(cpt.getAccount().getNumCpt());
				
			    accCredit = CodeIncrement.getAcount(em, c);
				soldCredit = accCredit.getSoldeProgressif() - montant;
				
				credit.setCompte(accCredit.getNumCpt());
				credit.setAccount(accCredit); 
				credit.setSolde(soldCredit);
				accCredit.setSoldeProgressif(soldCredit); 
				
				break;
			case "mobile":
				//Compte à la configuration
				accCredit = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
				soldCredit = accCredit.getSoldeProgressif() - montant;
				
				credit.setCompte(accCredit.getNumCpt());
				credit.setAccount(accCredit); 
				credit.setSolde(soldCredit);
				accCredit.setSoldeProgressif(soldCredit); 
				break;
			case "cheque":
				//Compte à la configuration
				accCredit = CodeIncrement.getAcount(em, confGL.getCmptCheque());
				soldCredit = accCredit.getSoldeProgressif() - montant;
				
				credit.setCompte(accCredit.getNumCpt());
				credit.setAccount(accCredit); 
				credit.setSolde(soldCredit);
				accCredit.setSoldeProgressif(soldCredit); 
				break;
			case "epargne":
				//Compte à la configuration
				CompteEpargne cptEp = em.find(CompteEpargne.class, numCptEp);
				
				TransactionEpargne trans = new TransactionEpargne();	
	
					accCredit = CodeIncrement.getAcount(em, confGL.getCmptDiffCash());
					soldCredit = accCredit.getSoldeProgressif() - montant;
					
					credit.setCompte(accCredit.getNumCpt());
					credit.setAccount(accCredit); 
					credit.setSolde(soldCredit);
					accCredit.setSoldeProgressif(soldCredit); 
					
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
							
			try {
				transaction.begin();
				em.flush();
				em.flush();
				em.flush();
				em.flush();
				em.persist(debit);
				em.persist(credit);
				transaction.commit();
				em.refresh(debit);
				em.refresh(credit);
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
		
		double interet = (montant*periode)*taux/100;
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

}

