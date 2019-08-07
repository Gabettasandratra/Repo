package mg.fidev.utils;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CodeIncrement {
	
	private static int getLastIndex(EntityManager em, String agence) {
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'"
				+ "and estClientIndividuel = true";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	private static int getLastIndexGar(EntityManager em, String agence) {
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'"
				+ "and estGarant = true ";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
	}
	
	public static String genTcode(EntityManager em) {
		String sql = "select MAX(tcode) from grandlivre";
		Query q = em.createNativeQuery(sql);
		if((q.getSingleResult()) == null)
			return "00000001";
		else{
			int result = Integer.parseInt(q.getSingleResult().toString());
			String index = String.format("%08d", ++result);
			return index;
		}
	}
	
	public static String getCodeGar(EntityManager em, String agence){
		int lastIndex = getLastIndexGar(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence + "/" + index;
	}
	
	public static String getCodeInd(EntityManager em, String agence){
		int lastIndex = getLastIndex(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence+"/I/" + index;
	}
	
	private static int getLastIndexGrp(EntityManager em, String agence) {
		String sql = "select count(*) from groupe where left(codeGrp, 2) = '"+agence+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
		}

	public static String getCodeGrp(EntityManager em, String agence){
		int lastIndex = getLastIndexGrp(em, agence);
		String index = String.format("%06d", ++lastIndex);
		return agence+"/G/" + index;
	}
}
