package mg.fidev.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mg.fidev.model.CompteEpargne;
import mg.fidev.model.ConfigGlEpargne;
import mg.fidev.model.ConfigInteretProdEp;
import mg.fidev.model.ConfigProdEp;
import mg.fidev.model.ProduitEpargne;
import mg.fidev.model.TypeEpargne;
import mg.fidev.service.ProduitEpargneService;

@WebService(name = "epargneService", targetNamespace = "http://fidev.mg.epargneService", serviceName = "epargneService", portName = "epargneServicePort", endpointInterface = "mg.fidev.service.ProduitEpargneService")
public class ProduitEpargneServiceImpl implements ProduitEpargneService {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();

	/*
	 * Method recupere le dernier index
	 */
	static int getLastIndex(String typeEp) {
		String sql = "select count(*) from produit_epargne where Type_epargnenom_type_epargne = '"+typeEp+"' ";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}

	@Override
	public boolean saveProduit(String nomProdEp, String nomTypeEp, boolean isActive) {
		try {
			ProduitEpargne pro = new ProduitEpargne();
			pro.setNomProdEpargne(nomProdEp);
			TypeEpargne typeEp = em.find(TypeEpargne.class, nomTypeEp);
			pro.setTypeEpargne(typeEp);
			pro.setEtat(isActive);

			int lastIndex = getLastIndex(pro.getTypeEpargne().getNomTypeEpargne());
			String index = String.format("%03d", ++lastIndex);
			
			if(pro.getTypeEpargne().getNomTypeEpargne().equals("DAT (Dépôt à terme)"))
				pro.setIdProdEpargne("AT" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("DAV (Dépôt à vue)"))
				pro.setIdProdEpargne("AV" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("Dépôt de garantie"))
				pro.setIdProdEpargne("Ga" + index);
			else if(pro.getTypeEpargne().getNomTypeEpargne().equals("Plan d'épargne"))
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

	@Override
	public List<ProduitEpargne> findAllProduit() {
		try {
			String sql = "select p from ProduitEpargne p";
			TypedQuery<ProduitEpargne> q = em.createQuery(sql, ProduitEpargne.class);
			
			List<ProduitEpargne> results = q.getResultList();
			System.out.println("Nety le izy");
			return results;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean desactiverProduit(String idProd) {
		try {
			ProduitEpargne p = em.find(ProduitEpargne.class, idProd);
			p.setEtat(false);
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
	
	@Override
	public boolean updateConfigProduit(int nbrJrIn, int nbrJrMinRetrait,
			int nbrJrMaxDepot, int ageMinCpt, float fraisTenuCpt, List<String> idProduit) {
		try {
			List<ProduitEpargne> listProdEp = new ArrayList<ProduitEpargne>();
			ConfigProdEp confProd = new ConfigProdEp();
			confProd.setNbrJrIn(nbrJrIn);
			confProd.setNbrJrMinRet(nbrJrMinRetrait);
			confProd.setNbrJrMaxDep(nbrJrMaxDepot);
			confProd.setAgeMinCpt(ageMinCpt);
			confProd.setFraisTenuCpt(fraisTenuCpt);
			for(String pro : idProduit){
				listProdEp.add(em
						.find(ProduitEpargne.class, pro));
			}
			confProd.setProduitEpargnes(listProdEp);
			for(ProduitEpargne prodEp : listProdEp){
				prodEp.setConfigProdEp(confProd);
			}
			
			em.getTransaction().begin();

			em.flush();
			em.persist(confProd);
			em.getTransaction().commit();
			em.refresh(confProd);
			
			System.out.println("Update successful");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return false;
		}
	}
	
	@Override
	public boolean updateConfigIntProduit(int periodeInteret, String dateCalcul, float tauxInteret,
			int nbrJrInteret, int nbrSemInteret, double soldeMinInd,
			double soldeMinGrp, double interetMinInd, double interetMinGrp,
			String modeCalcul, List<String> idProduit) {
		List<ProduitEpargne> listProdEp = new ArrayList<ProduitEpargne>();
		ConfigInteretProdEp confIntProd = new ConfigInteretProdEp();
		confIntProd.setPeriodeInteret(periodeInteret);
		confIntProd.setDateCalcul(dateCalcul);
		confIntProd.setTauxInteret(tauxInteret);
		confIntProd.setNbrJrInt(nbrJrInteret);
		confIntProd.setNbrSemaineInt(nbrSemInteret);
		confIntProd.setSoldeMinInd(soldeMinInd);
		confIntProd.setSoldeMinGrp(soldeMinGrp);
		confIntProd.setInteretMinInd(interetMinInd);
		confIntProd.setInteretMinGrp(interetMinGrp);
		confIntProd.setModeCalcul(modeCalcul);
		for(String pro : idProduit){
			listProdEp.add(em
					.find(ProduitEpargne.class, pro));
		}
		confIntProd.setProduitEpargnes(listProdEp);
		for(ProduitEpargne pro : listProdEp)
			pro.setConfigInteretProdEp(confIntProd);
		try {
			
			em.getTransaction().begin();

			em.flush();
			em.persist(confIntProd);
			em.getTransaction().commit();
			em.refresh(confIntProd);
			
			System.out.println("Update successful");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("We have a problem");
			return false;
		}
	}

	@Override
	public void calculInteret(List<String> listNumCpt) {
		List<CompteEpargne> comptes = new ArrayList<CompteEpargne>();
		for(String num : listNumCpt){
			comptes.add(em.find(CompteEpargne.class, num));
		}
		double interet;
		///DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		ConfigInteretProdEp confInt = new ConfigInteretProdEp();
		for(CompteEpargne cpt : comptes){
			confInt = cpt.getProduitEpargne().getConfigInteretProdEp();
			///Month mois = LocalDateTime.parse(cpt.getTransactionEpargnes().get(0).getDateTransaction(), formatter).getMonth();
			/*for(int i=0; i< cpt.getTransactionEpargnes().size(); i++){
				while(LocalDateTime.parse(cpt.getTransactionEpargnes().get(i).getDateTransaction(), formatter).getMonth().equals(mois)){
					if(i < (cpt.getTransactionEpargnes().size()) - 1){
					switch (confInt.getModeCalcul()) {
						case "Solde moyen mensuel":
							int p = Period.between(LocalDateTime.parse(cpt.getTransactionEpargnes().
						get(i).getDateTransaction(), formatter).toLocalDate(), LocalDateTime.parse(cpt.getTransactionEpargnes().
						get(i++).getDateTransaction(), formatter).toLocalDate()).getDays();
							System.out.println("Solde sans intérêt : "+cpt.getSolde());
							interet = ((cpt.getTransactionEpargnes().get(i).getSolde()*p)/confInt.getNbrJrInt() + (cpt.getTransactionEpargnes().get(i++).getSolde()*p)/confInt.getNbrJrInt()) * 10 / 100;
							cpt.setSolde(cpt.getSolde() + interet);
							System.out.println("Solde avec intérêt : "+cpt.getSolde());
							String sql = "SELECT MIN(solde) FROM `transaction_epargne` WHERE date_transaction BETWEEN '2019-06-01' and '2019-06-30' and Compte_epargnenum_compte_ep = '"+cpt.getNumCompteEp()+"' ";
							Query q = em.createNativeQuery(sql);
							double solde = (double) q.getSingleResult();
							System.out.println("Solde sans intérêt : "+cpt.getSolde());
							interet = (solde * confInt.getTauxInteret() / 100) / 12;
							System.out.println(interet);
							break;
			
						default:
							break;
					}
					}
					
				}
			}*/
			switch (confInt.getModeCalcul()) {
				case "Solde moyen mensuel":
					/*int p = Period.between(LocalDateTime.parse(cpt.getTransactionEpargnes().
				get(i).getDateTransaction(), formatter).toLocalDate(), LocalDateTime.parse(cpt.getTransactionEpargnes().
				get(i++).getDateTransaction(), formatter).toLocalDate()).getDays();
					System.out.println("Solde sans intérêt : "+cpt.getSolde());
					interet = ((cpt.getTransactionEpargnes().get(i).getSolde()*p)/confInt.getNbrJrInt() + (cpt.getTransactionEpargnes().get(i++).getSolde()*p)/confInt.getNbrJrInt()) * 10 / 100;
					cpt.setSolde(cpt.getSolde() + interet);
					System.out.println("Solde avec intérêt : "+cpt.getSolde());*/
					String sql = "SELECT MIN(solde) FROM `transaction_epargne` WHERE date_transaction BETWEEN '2019-06-01' and '2019-06-30' and Compte_epargnenum_compte_ep = '"+cpt.getNumCompteEp()+"' ";
					Query q = em.createNativeQuery(sql);
					double solde = (double) q.getSingleResult();
					System.out.println("Solde sans intérêt : "+cpt.getSolde());
					interet = (solde * confInt.getTauxInteret() / 100) / confInt.getPeriodeInteret();
					System.out.println(interet);
					break;
	
				default:
					break;
			}
		}
	}

	///	Configuration GL épargne
	@Override
	public void configGLepargne(ConfigGlEpargne configGlEpargne,
			String idProduit) {
		ProduitEpargne pdtEp = em.find(ProduitEpargne.class, idProduit);
		List<ProduitEpargne> listProd = new ArrayList<ProduitEpargne>();
		listProd.add(pdtEp);
		configGlEpargne.setProduitEpargnes(listProd);
		try {
			em.getTransaction().begin();
			em.persist(configGlEpargne);
			em.getTransaction().commit();
			System.out.println("Configuration GL épargne succes");
		} catch (Exception e) {
			System.err.println("");
		}
	}

}
