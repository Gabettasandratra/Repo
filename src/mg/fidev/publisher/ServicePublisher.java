package mg.fidev.publisher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.ws.Endpoint;

import mg.fidev.service.impl.GroupeServiceImpl;
import mg.fidev.service.impl.IndividuelServiceImpl;
import mg.fidev.service.impl.ProduitEpargneServiceImpl;
import mg.fidev.service.impl.UserServiceImpl;

public class ServicePublisher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Initiation de la serviceV2 individuel.....");
		Endpoint.publish("http://127.0.0.1:8082/service2.0/client/individuel", new IndividuelServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/client/groupe", new GroupeServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/user", new UserServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/produit", new ProduitEpargneServiceImpl());
		System.out.println("-- Service Online --");
	}

}