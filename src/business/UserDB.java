package business;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import db.DBUtil;
import util.Console;

public class UserDB {
	
	public UserDB() {		
	}

	public ArrayList<User> getAllUsers() {//Finds the DB connection using persistence and User annotated as entity
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
			ArrayList<User> allUsers = new ArrayList<>(query.getResultList());
			return allUsers;
		} finally {
			em.close();			
		}		
	}

	public User getUserByUserID(int userID) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		User usr = null;
		try {
			usr = em.find(User.class, userID);
		} catch (Exception e) {
			Console.printToConsole("Error getting user for userID (" + userID + "): " + e);
		}
		finally {
			em.close();
		}
		return usr;
	}	
	
	public User authenticateUser(String un, String pwd) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String qStr = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
		TypedQuery<User> q = em.createQuery(qStr, User.class);
		q.setParameter("username", un);
		q.setParameter("password", pwd);
		
		User authUser = null;		
		try {
			authUser = q.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Authenticated user error for username(" + un + ") and pwd (" + pwd + "): " + e);
		} finally {
			em.close();
		}
		return authUser;
	}		

	public boolean insertUser (User u) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		boolean success = false;
		try {
			trans.begin();
			em.persist(u);
			trans.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Insert user error: " + e);
			trans.rollback();
		} finally {
			em.close();
		}
		return success;
	}
	
	public boolean deleteUser (User u) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		boolean success = false;
		try {
			trans.begin();
			em.remove(em.merge(u));
			trans.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Delete user error: " + e);
			trans.rollback();
		} finally {
			em.close();
		}
		return success;
	}

	public boolean updateUser (User u) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		boolean success = false;
		try {
			trans.begin();
			em.merge(u)	;
			trans.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Update user error: " + e);
			trans.rollback();
		} finally {
			em.close();
		}
		return success;
	}	
}
