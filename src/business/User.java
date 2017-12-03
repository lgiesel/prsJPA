package business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	@Column(name="isreviewer")
	private boolean reviewer;
	@Column(name="isadmin")
	private boolean admin;
	private int updatedByUser;
	
	public User() {
	id = 0;
	username = "";
	password = "";
	firstName = "";
	lastName = "";
	phone = "";
	email = "";
	reviewer = false;
	admin = false;
	updatedByUser = 1;
}
	public User(int id, String username, String password, String firstName, String lastName, 
			    String phone, String email, boolean reviewer, boolean admin, int updatedByUser) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.reviewer = reviewer;
		this.admin = admin;		
		this.updatedByUser = updatedByUser;
	}
	
	public User (String username, String password, String firstName, String lastName,
                 String phone, String email, boolean reviewer, boolean admin, int updatedByUser){
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setPhone(phone);
		setEmail(email);
		setReviewer(reviewer);
		setAdmin(admin);	
	}
	
	public User (String username, String password) {
		this.username = username;
		this.password = password;		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isReviewer() {
		return reviewer;
	}

	public void setReviewer(boolean reviewer) {
		this.reviewer = reviewer;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public int getUpdatedByUser() {
		return updatedByUser;
	}
	public void setUpdatedByUser(int updatedByUser) {
		this.updatedByUser = updatedByUser;
	}
	@Override
	public String toString() {
		return "User= [id=" + id + ", username=" + username + ", password=" + password + 
			        ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + 
				    ", email=" + email + ", reviewer=" + reviewer + ", admin=" + admin + ", updatedbyuser=" + updatedByUser +"]";
	}
	
	public String buildDisplayText() {//Just shows that either the variable or the getX method works
		return "id:\t\t" + id + "\n" + 
               "username:\t" + getUsername() + "\n" + 
	           "password:\t" + password + "\n" +
               "firstname:\t" + getFirstName() + "\n" +
	           "lastname:\t" + lastName + "\n" +
               "phone:\t\t" + getPhone() + "\n" +
	           "email:\t\t" + getEmail() + "\n" +
               "reviewer:\t" + isReviewer() + "\n" +
	           "admin:\t\t" + isAdmin() + "\n";
	}
}
