package mg.fidev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="counterExecution")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CounterExecution implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String date;
	
	private boolean exec;

	public CounterExecution() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isExec() {
		return exec;
	}

	public void setExec(boolean exec) {
		this.exec = exec;
	}

}
