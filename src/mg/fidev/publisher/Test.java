package mg.fidev.publisher;

import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import mg.fidev.model.Individuel;

public class Test {
	private static final String PERSISTENCE_UNIT_NAME = "FIDEV-Repository";
	private static EntityManager em = Persistence.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME).createEntityManager();
	public static void main(String[] args) throws ParseException, JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Individuel.class);
		System.out.println("jaxbContext is=" +jaxbContext.toString());
	}

}
