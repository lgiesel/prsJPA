package business;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import db.DBUtil;

public class ProductDB {
	
	public ProductDB() {
		// initialize list of products - called from DB now
	}

	public ArrayList<Product> getProducts() {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
			ArrayList<Product> products = new ArrayList<>(query.getResultList());
			return products;
		} finally {
			em.close();			
		}		
	}

	public Product getPriceForProductID (int productID) {
		Product prod = null;
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String qStr = "SELECT p FROM Product p WHERE p.id = :productid";
		TypedQuery<Product> prdt = em.createQuery(qStr, Product.class);
		prdt.setParameter("productid", productID);
		try {
			prod= prdt.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Price for product (" + productID + ") error " + e);
		} finally {
			em.close();
		}
		return prod;
	}
}