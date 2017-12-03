package business;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import business.PurchaseRequestLineItem;

@Entity
public class PurchaseRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;
	private String description;
	private String justification;
	private Timestamp dateNeeded;
	private String deliveryMode;
	@ManyToOne
	@JoinColumn(name="statusid")
	private Status status;
	private double total;
	private Timestamp submittedDate;
	private String reasonForRejection;
	private int updatedByUser;
	@OneToMany (fetch=FetchType.EAGER, cascade=CascadeType.ALL) 
	@JoinColumn(name="purchaserequestid")
	private ArrayList<PurchaseRequestLineItem> prli;

	public PurchaseRequest() {
		id = 0;
		description = "";
		justification = "";
		dateNeeded = Timestamp.valueOf(LocalDateTime.now());
		deliveryMode = "";
		total = 0.0;
		submittedDate = null;
		reasonForRejection = "";
		prli = new ArrayList<PurchaseRequestLineItem>();
	}	
	
	public PurchaseRequest(int id, int userID, String description, String justification, Timestamp dateNeeded, String deliveryMode, int statusID,
		                   double total, Timestamp submittedDate, String reasonForRejection//DO NOT NEED ArrayList YET; 
		                   ) {
		super();
		this.id = id;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		this.total = total;
		this.submittedDate = submittedDate;
		this.reasonForRejection = reasonForRejection;
		this.prli =  new ArrayList<PurchaseRequestLineItem>();	//Instantiate so prli can be populated later	
	}

	public PurchaseRequest(User usr, String description, String justification, Timestamp dateNeeded, 
			               String deliveryMode, Status s, double total) {
		id = 0;
		user = usr;
		this.description = description;
		this.justification = justification;
		this.dateNeeded = dateNeeded;
		this.deliveryMode = deliveryMode;
		status = s; 
		this.total = total;	
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}
	
	public User getUser(User user) {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public Timestamp getDateNeeded() {
		return dateNeeded;
	}

	public void setDateNeeded(Timestamp dateNeeded) {
		this.dateNeeded = dateNeeded;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public Status getStatus() {
		return status;
	}
	
	public Status getStatus(String description) {
		return status;
	}	

	public Status getStatus(int statusID) {
		return status;
	}
	
	public Status getStatus(Status status) {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Timestamp getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Timestamp submittedDate) {
		this.submittedDate = submittedDate;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}	

	public int getUpdatedByUser() {
		return updatedByUser;
	}
	public void setUpdatedByUser(int updatedByUser) {
		this.updatedByUser = updatedByUser;
	}	
	
	public ArrayList<PurchaseRequestLineItem> getPrli() {
		return prli;
	}

	public void setPrli(ArrayList<PurchaseRequestLineItem> prli) {
		this.prli = prli;
	}
	
	public void addPRLineItem(PurchaseRequestLineItem li) {
		prli.add(li);
	}
	 	
	@Override
	public String toString() {
		return "\nPurchaseRequest= [" + 
	              "\nid=" + id +  ", description=" + description + ", " + 
	              "justification=" + justification + 
	              ", dateNeeded= " + dateNeeded +
	              ", deliveryMode=" + deliveryMode +  
	              ", total=" + total + 
	              ", submittedDate=" + submittedDate +
	              ", reasonForRejection=" + reasonForRejection +
	              ",\n" + getUser() +  
	              ",\n" + getStatus() + 
	              "\nLineItems: [" +  prli +"\n" + "]\n";
	}
}