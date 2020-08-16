package mg.fidev.publisher;

import javax.xml.ws.Endpoint;

import mg.fidev.service.impl.ComptabliteServiceImpl;
import mg.fidev.service.impl.CreditServiceImpl;
import mg.fidev.service.impl.GroupeServiceImpl;
import mg.fidev.service.impl.IndividuelServiceImpl;
import mg.fidev.service.impl.ProduitEpargneServiceImpl;
import mg.fidev.service.impl.UserServiceImpl; 

public class ServicePublisher {

	public static void main(String[] args) {
		System.out.println("Initiation de la serviceV2 individuel.....");
		Endpoint.publish("http://127.0.0.1:8082/service2.0/client/individuel", new IndividuelServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/client/groupe", new GroupeServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/user", new UserServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/produit", new ProduitEpargneServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/credit", new CreditServiceImpl());
		Endpoint.publish("http://127.0.0.1:8082/service2.0/comptabilite", new ComptabliteServiceImpl());
		System.out.println("-- Service Online --");    
		/*LocalDate localDate = LocalDate.now();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//String d = "2020-04-12";
		//System.out.println(d.substring(8, 10)); 
		try {   
			System.out.println("Local date = "+ localDate);
			System.out.println("Local date to string = "+ localDate.toString());
			java.util.Date d = simpleDateFormat.parse(localDate.toString());
			System.out.println("Date = "+d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		double montant = 500;
		System.out.println("Montant à rembourser = "+montant);
		double interet = 500 - 200;
		montant = interet;
		System.out.println("rest paye int = "+interet);
		double principal = montant - 300;		
		System.out.println("rest paye principal = "+principal);*/
	}
}



/*double montant = 8500;
int echeance =1;

//List<E>
while(montant>0){
	double princ = 900;
	double inter = 100;
	//echeance = 1;
	double restInt = montant - inter;
	
	System.out.println("echeance ="+echeance);
	System.out.println("rest paie intéret: "+montant+" - "+inter+" ="+restInt);
	
	double restPrinc = restInt - princ;
	
	System.out.println("rest paie principal: "+restInt+" - "+princ+" ="+restPrinc);
	
	echeance++;
	montant = restPrinc;
	System.out.println("montant ="+montant +"\n");
	//i = i-1;
}

double a = (300000*14/365 + 200000*6/365 + 100000*10/365) * 10/100;
System.out.println(a);*/