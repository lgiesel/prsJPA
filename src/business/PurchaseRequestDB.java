package business;

import db.DBUtil;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class PurchaseRequestDB {
	
	public static PurchaseRequest getPurchaseRequestById(int prID) {//Finds the DB connection using persistence and User annotated as entity
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		try {
			PurchaseRequest pr = em.find(PurchaseRequest.class, prID);
			return pr;			
		} finally {
			em.close();			
		}		
	}
	
	public ArrayList<PurchaseRequest> getMyPurchaseRequests(User u) {		
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String jpql = "Select p FROM PurchaseRequest p WHERE p.user = :user";
		try {
			TypedQuery<PurchaseRequest> q = em.createQuery(jpql, PurchaseRequest.class);
			q.setParameter("user", u);		
			ArrayList<PurchaseRequest> pr = new ArrayList<>(q.getResultList());
			return pr;				
		} finally {
			em.close();
		}
	}

	public ArrayList<PurchaseRequest> getPurchaseRequestsToReview(User u, Status s) {					
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String jpql = "SELECT p FROM PurchaseRequest p WHERE p.status = :status " + 
				      " AND p.user <> :user ORDER BY p.dateNeeded, p.id";
		try {
			TypedQuery<PurchaseRequest> q = em.createQuery(jpql, PurchaseRequest.class);
			q.setParameter("user", u);		
			q.setParameter("status", s);		
			ArrayList<PurchaseRequest> pr = new ArrayList<>(q.getResultList());
			return pr;				
		} finally {
			em.close();
		}
	}	
	
	public boolean insertPurchRequest(PurchaseRequest pr) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		boolean success = false;
		try {
			trans.begin();
			em.persist(pr);
			trans.commit();
			success = true;			
		} catch (Exception e) {
			System.out.println("Insert PR header error: " + e);
			trans.rollback();
		} finally {
			em.close();
		}
		return success;		
	}
	
	public boolean insertPurchRequestLI(ArrayList<PurchaseRequestLineItem> prLI) {
		boolean success = false;		
		for (PurchaseRequestLineItem li : prLI) {
			EntityManager em = DBUtil.getEmFactory().createEntityManager();
			EntityTransaction trans = em.getTransaction();
			try {
				trans.begin();
				em.persist(li);
				trans.commit();
				success = true;			
			} catch (Exception e) {
				System.out.println("Insert PR line item error: " + e);
				trans.rollback();
			} finally {
				em.close();
			}			
		}
		return success;		
	}
	
	public boolean insertPurchRequestLISingle(PurchaseRequestLineItem prlI) {
		boolean success = false;		
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(prlI);
			trans.commit();
			success = true;			
		} catch (Exception e) {
			System.out.println("Insert PR line item error: " + e);
			trans.rollback();
		} finally {
			em.close();
		}	
		return success;		
	}
	
	public boolean updatePRTotal(PurchaseRequest pr) {
		boolean success = false;
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(pr);
			trans.commit();
			success = true;			
		} catch (Exception e) {
			trans.rollback();
		} finally {
			em.close();
		}
		return success;
	}
}