package mg.fidev.utils;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="agentInstitution")
public class Agent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "codeAgent", required = false, nillable = true)
	private int codeAgent;	
	@XmlElement(name = "nom", required = false, nillable = true)
	private String noomAgent;
	@XmlElement(name = "tel", required = false, nillable = true)
	private int tel;
	@XmlElement(name = "fonction", required = false, nillable = true)
	private String fonction;
	@XmlElement(name = "tauxRemb", required = false, nillable = true)
	private int tauxRemb;
	@XmlElement(name = "tauxArrieres", required = false, nillable = true)
	private int tauxArrieres;
	@XmlElement(name = "nbrClient", required = false, nillable = true)
	private int nbrClient;
	@XmlElement(name = "clients", required = false, nillable = true)
	private List<ClientAgent> clients;
	
	public Agent() {
		super();
	}
	public int getCodeAgent() {
		return codeAgent;
	}
	public void setCodeAgent(int codeAgent) {
		this.codeAgent = codeAgent;
	}
	public String getNoomAgent() {
		return noomAgent;
	}
	public void setNoomAgent(String noomAgent) {
		this.noomAgent = noomAgent;
	}
	public int getTel() {
		return tel;
	}
	public void setTel(int tel) {
		this.tel = tel;
	}
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}	
	public int getTauxRemb() {
		return tauxRemb;
	}
	public void setTauxRemb(int tauxRemb) {
		this.tauxRemb = tauxRemb;
	}
	public int getTauxArrieres() {
		return tauxArrieres;
	}
	public void setTauxArrieres(int tauxArrieres) {
		this.tauxArrieres = tauxArrieres;
	}
	public int getNbrClient() {
		return nbrClient;
	}
	public void setNbrClient(int nbrClient) {
		this.nbrClient = nbrClient;
	}
	public List<ClientAgent> getClients() {
		return clients;
	}
	public void setClients(List<ClientAgent> clients) {
		this.clients = clients;
	}
}
