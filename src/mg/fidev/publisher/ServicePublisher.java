package mg.fidev.publisher;

import javax.xml.ws.Endpoint;

import mg.fidev.service.impl.GroupeServiceImpl;
import mg.fidev.service.impl.IndividuelServiceImpl;

public class ServicePublisher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Initiation de la serviceV2 individuel.....");
		Endpoint.publish("http://127.0.0.1:8080/service2.0/client/individuel", new IndividuelServiceImpl());
		Endpoint.publish("http://127.0.0.1:8080/service2.0/client/groupe", new GroupeServiceImpl());
		System.out.println("-- Service Online --");
	}

}
