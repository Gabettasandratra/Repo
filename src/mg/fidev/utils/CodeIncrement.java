package mg.fidev.utils;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CodeIncrement {
	
	private static int getLastIndex(EntityManager em, String agence) {
		String sql = "select count(*) from individuel where left(codeInd, 2) = '"+agence+"'";
		Query q = em.createNativeQuery(sql);
		int result = Integer.parseInt(q.getSingleResult().toString());
		return result;
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
