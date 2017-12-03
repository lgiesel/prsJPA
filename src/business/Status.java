package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import db.DBUtil;

@Entity
public class Status {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String description;

	public Status() {
		id = 0;
		description = "";
	}
	
	public Status(int id, String description) {
			this.id = id;
			this.description = description;
		}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
	public static HashMap<Integer,String> getstatusMapIDToDesc(){
		HashMap<Integer, String> statusMap = new HashMap<>();
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String jpql = "SELECT s FROM Status s";
		try {
			TypedQuery<Status> query = em.createQuery(jpql, Status.class);
			ArrayList<Status> allStatus = new ArrayList<>(query.getResultList());
			for (Status s : allStatus) {
				statusMap.put(s.getId(), s.getDescription());
			}
		} finally {
			em.close();			
		}	
		return statusMap;
	}
	
	public static HashMap<String, Integer>getstatusMapDescToID(){
		HashMap<String, Integer> statusMap = new HashMap<>();
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String jpql = "SELECT s FROM Status s";
		try {
			TypedQuery<Status> query = em.createQuery(jpql, Status.class);
			ArrayList<Status> allStatus = new ArrayList<>(query.getResultList());
			for (Status s : allStatus) {
				statusMap.put(s.getDescription(), s.getId());
			}
		} finally {
			em.close();			
		}	
		return statusMap;
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", description=" + description + "]";
	}			
	
}
