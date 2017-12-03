package presentation;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import business.User;
import business.UserDB;
import business.PurchaseRequest;
import business.PurchaseRequestDB;
import business.PurchaseRequestLineItem;
import business.Product;
import business.ProductDB;
import business.Status;
//import business.Vendor;

import util.Console;

public class PRSConsole {
	private static User currentUser = null;
	private static UserDB uDB = null;
	private static ProductDB prodDB = null;
	private static PurchaseRequest currentPR = null;
    private static PurchaseRequestDB preqDB = null;
	private static HashMap<Integer, String> statusMapIDToDesc = null;//Gets status desc from ID
	private static HashMap<String, Integer> statusMapDescToID = null;//Gets status ID from desc

	private static boolean isLoggedIn; 
	
	public static void main(String[] args) {
		Console.printToConsole("Welcome to PRS Console");
		Console.printToConsole("");
		
		uDB = new UserDB();//Is this like the DB session? 
		prodDB = new ProductDB();				
		preqDB = new PurchaseRequestDB();
//		status = new Status();
		statusMapIDToDesc = new HashMap<>();
		statusMapDescToID = new HashMap<>();
				
		displayPreLoginMenu();

		String choice = "y";
		isLoggedIn = false; 

		loadStatusMaps();
		
		while(choice.equalsIgnoreCase("y")){
			currentPR = null;
			Console.printToConsole("");
			
			if (!isLoggedIn) {
				
				String loginOpt = Console.getString("Select login options below:\t", "login", "exit");
				if (loginOpt.equalsIgnoreCase("login")) {
					while (!isLoggedIn) {
						login();									
					}
				} else {
					Console.printToConsole("Exit... ");
					break;				
				}
			}							

			//Set display menu based on user settings
			displayMenuForUser();
			
			String menuOption = Console.getString("Select menu option:\t");
			
			if (menuOption.equalsIgnoreCase("all")) {				
				listAll();
				
			} else if (menuOption.equalsIgnoreCase("exit")) {				
				Console.printToConsole("Exit... ");
				break;	
								
			} else if (menuOption.equalsIgnoreCase("mypr")) {
				displayMyPurchaseRequests();
				
			} else if (menuOption.equalsIgnoreCase("review")){
				displayReviewPurchaseRequests();					
				
			} else if (menuOption.equalsIgnoreCase("newpr")) {
				generateNewPR();
				
			} else if (menuOption.equalsIgnoreCase("adduser")) {
				addUser();
				
			} else if (menuOption.equalsIgnoreCase("deluser")) {
				deleteUser();
				
			} else if (menuOption.equalsIgnoreCase("upduser")) {
				updateUser();							
			}	
			choice = Console.getString("Continue? (y/n) ", "y", "n");
		}
		
		Console.printToConsole("");
		Console.printToConsole("Bye!");	
	}

	public static void displayPreLoginMenu() {
		Console.printToConsole("");
		Console.printToConsole("Login Menu\n" + 
	                           "==============================\n" +
							   "LOGIN 	- Login to application\n" +  
				               "EXIT	- Exit application\n");
	}
	
	public static void login () {
		Console.printToConsole("");
		Console.printToConsole("Enter your login credentials...");
		String u = Console.getString("Enter username:\t"); 
		String p = Console.getString("Enter password:\t"); 
		
		currentUser = uDB.authenticateUser(u, p);
		if (currentUser != null) {
			Console.printToConsole("Successful login - " + currentUser.getFirstName() + " " + currentUser.getLastName());
			System.out.println(currentUser);
			isLoggedIn = true;
		} else {
			Console.printToConsole("Unsuccessful login. Try again.");
		}
		Console.printToConsole("");
	}

    public static void displayMenuForUser() {
		boolean isReviewer = checkIfReviewer();
		boolean isAdmin = checkIfAdmin();
		if (!isReviewer) {
			if (!isAdmin) {
				displayPRSMenuOptions();									
			} else {
				displayAdminMenuOptions();
			}
		} else {
			if (!isAdmin) {
				displayReviewerMenuOptions();
			} else {
				displayAdminReviewerMenuOptions();									
			}
		}
    }
	
	public static boolean checkIfReviewer() {
		return  currentUser.isReviewer();
	}
	
	public static boolean checkIfAdmin() {
		return currentUser.isAdmin();
	}

	public static void displayPRSMenuOptions() {
		Console.printToConsole("");
		Console.printToConsole("Menu:\n" + 
	                           "==============================\n" +
				               "ALL  	 - Display all users\n" + 
							   "NEWPR    - Enter new purchase request\n" +
							   "MYPR     - Review my purchase requests\n" +				               
				               "EXIT	 - Exit application\n");
	}

	public static void displayReviewerMenuOptions() {
		Console.printToConsole("\nMenu:\n" + 
	                 		   "==============================\n" +
	                 		   "ALL     - Display all users\n" + 
	                 		   "NEWPR   - Enter new purchase request\n" + 
	                 		   "MYPR    - Review my purchase requests\n" +				               
	                 		   "REVIEW  - Review pending requests\n" +				               
	                 		   "EXIT    - Exit application\n");
	}

	public static void displayAdminMenuOptions() {
		Console.printToConsole("\nMenu:\n" + 
	                 		   "==============================\n" +
	                 		   "ALL     - Display all users\n" + 
	                 		   "NEWPR   - Enter new purchase request\n" + 
	                 		   "MYPR    - Review my purchase requests\n" +			
	                 		   "ADDUSER - Add new user\n" +
	                 		   "UPDUSER - Update user\n" +	                 		   
	                 		   "DELUSER - Delete user\n" +	                 		   
	                 		   "EXIT    - Exit application\n");
	}
	
	public static void displayAdminReviewerMenuOptions() {
		Console.printToConsole("\nMenu:\n" + 
	                 		   "==============================\n" +
	                 		   "ALL     - Display all users\n" + 
	                 		   "NEWPR   - Enter new purchase request\n" + 
	                 		   "MYPR    - Review my purchase requests\n" +				               
	                 		   "REVIEW  - Review pending requests\n" +				
	                 		   "ADDUSER - Add new user\n" +
	                 		   "UPDUSER - Update user\n" +	                 		   
	                 		   "DELUSER - Delete user\n" +	                 		   
	                 		   "EXIT    - Exit application\n");
	}
	
	public static void listAll () {				
		for (User usr : uDB.getAllUsers()) {
			System.out.println(usr);		
		}
	}
	
	public static void generateNewPR() {
		boolean success = false;
		success = createPurchRequest();	
		if (success) {
			Console.printToConsole("Successful insert of purchase request header ("+ currentPR.getId() + ").\n");
			displayProductList();
			String choice = Console.getString("\nEnter line item? (y/n):\t","y","n");	
			boolean liSuccess = false;
			ArrayList<PurchaseRequestLineItem> lineItemAL = new ArrayList<>();
			while (choice.equalsIgnoreCase("y")) {			
				int productID = Console.getInt("Enter product nbr from product list:\t", 1, prodDB.getProducts().size());
				int qty = Console.getInt("Enter quantity:\t");  			
				PurchaseRequestLineItem li = new PurchaseRequestLineItem (); 				
				li.setPurchaseRequestID(currentPR.getId());
				li.setProductID(productID);
				li.setQuantity(qty);
				li.setupdatedByUser(currentUser.getId());
				lineItemAL.add(li);
				currentPR.setPrli(lineItemAL);
				double liTotal = calculateLineItemPrice(li);
				currentPR.setTotal(currentPR.getTotal() + liTotal);
				Console.printToConsole("\nLine item price: " + formatCurrency(liTotal) + "; PR subtotal: " + formatCurrency(currentPR.getTotal()));
				Console.printToConsole("");
				
				liSuccess = preqDB.insertPurchRequestLISingle(li);
				boolean updSuccess = false;
				if (liSuccess) {
					updSuccess = preqDB.updatePRTotal(currentPR);
					if (!updSuccess) {
						Console.printToConsole("PR total update failed.\n");
						break;
					} else {
						Console.printToConsole("PR total update successful.\n");
					}
				}
				
				choice = Console.getString("Enter line item? (y/n):\t","y","n");	
			}	
//			liSuccess = preqDB.insertPurchRequestLI(lineItemAL); //Insert all line items at one time 
//			if (liSuccess) {
//				Console.printToConsole("\nLine items successfully inserted.\n");
//				autoApproveUpTo50();
//			} else {
//				Console.printToConsole("Error inserting line items.\n");
//			}
		} else {
			Console.printToConsole("Purchase request insert failed.");
		}
	}
	
	public static boolean createPurchRequest() {
		boolean isPRCreated = false;
		Console.printToConsole("Enter attributes for new purchase request...");
		
		String description = Console.getString("Enter description:\t", 100, true);
		String justification  = Console.getString("Enter justification:\t", 255, true);
		String dateNeededStr = Console.getString("Enter date needed (YYYY-MM-DD):\t", 10);
		String deliveryMode  = Console.getString("Enter delivery mode:\t", 25);		
        Timestamp tsd = Console.getDateFromStringTS(dateNeededStr);
		
		int newPRStatusID = statusMapDescToID.get("New");
		String newPRStatusDesc= statusMapIDToDesc.get(newPRStatusID);
		Status newStatus = new Status(newPRStatusID, newPRStatusDesc);

		//To dos !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//Update PR total after line items 
		//PROMPT TO EDIT PR HDR
		//LET USER EDIT LI THAT UPDATES TOTAL
		//Add vendor - set up in jpa and join w product
		//Update status to remove the jpql and simply call status like userDB gets all users
		
		currentPR = new PurchaseRequest(currentUser, description, justification, tsd, deliveryMode, newStatus, 0.0);
		
		String strg = "Purchase Request Header Review\n" +  
	                   "==============================\n" +
	                   "Purchaser Request ID:\t\t\n" +  
			           "User:\t\t\t\t" + currentPR.getUser().getFirstName() + " " + currentPR.getUser().getLastName() + "\n" +	                   
				       "Description:\t\t\t" + currentPR.getDescription() + "\n" +
				       "Justification:\t\t\t" + currentPR.getJustification() + "\n" + 
	 			       "Date Needed:\t\t\t" +  currentPR.getDateNeeded() + "\n" + 
				       "DeliveryMode:\t\t\t" + currentPR.getDeliveryMode() + "\n" + 
				       "Status:\t\t\t\t" + currentPR.getStatus().getDescription() + "\n" +
				       "Total:\t\t\t\t" + formatCurrency(currentPR.getTotal()) + "\n" +  
				       "Submitted Date:\t\t\t" + currentPR.getSubmittedDate();
		
		String choice = Console.getString("\n" + strg + "\nSubmit Purchase Request Header? (Submit or Cancel):\t", "submit", "cancel"); 

		if (choice.equalsIgnoreCase("submit")) {
			updatePR();
			isPRCreated = preqDB.insertPurchRequest(currentPR);
//			Console.printToConsole("Purchase request header submitted.\n"); 				
		} else {
			Console.printToConsole("Purchase request header cancelled\n"); 				
		}
		
		return isPRCreated;
	}
		
	public static void displayProductList() {
		for (Product p : prodDB.getProducts()) {
			System.out.println(p);			
		}
	}
	
	public static void updatePR() {
		currentPR.setSubmittedDate(Timestamp.valueOf(LocalDateTime.now()));
		currentUser.setUpdatedByUser(currentUser.getId());
		currentPR.setUpdatedByUser(currentUser.getUpdatedByUser());
		System.out.println("pr updated: " + currentPR);
	}
	
	public static String formatCurrency(double dollarAmt) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return nf.format(dollarAmt);
	}
	
	public static double calculateLineItemPrice(PurchaseRequestLineItem prli) {
		Product product = prodDB.getPriceForProductID(prli.getProductID());
		return product.getPrice() * prli.getQuantity();
	}

	public static void displayMyPurchaseRequests() {
		String s = "";
		Console.printToConsole("\nMy Purchase Requests...");
		Console.printToConsole("");
		for (PurchaseRequest pr : preqDB.getMyPurchaseRequests(currentUser)){
			
			s += "Purchase Request ID:\t" + pr.getId() + "\n" +
		         "  User:\t\t\t\t" + pr.getUser().getFirstName() + " " + pr.getUser().getLastName() + "\n" +					
				 "  Description:\t\t" + pr.getDescription() + "\n" +
		         "  Justification:\t" + pr.getJustification() + "\n" +
				 "  Date Needed:\t\t" +  pr.getDateNeeded() + "\n" +
		         "  DeliveryMode:\t\t" + pr.getDeliveryMode() + "\n" +
			     "  Status:\t\t" + pr.getStatus().getDescription() + "\n" +
				 "  Total:\t\t" + formatCurrency(pr.getTotal()) + "\n" +
				 "  Submitted Date:\t" + pr.getSubmittedDate() + "\n" + 
				 "  Updated by user: " + pr.getUpdatedByUser() + "\n" + 
				 "  PRLI:\n" + pr.toString(); 			
		}
		if (s.equalsIgnoreCase("")) {
			Console.printToConsole("No purchase requests were found for user: " + currentUser.getFirstName() + " " + currentUser.getLastName());
		} else {
			Console.printToConsole(s);					
		}
	}
	
	public static void displayReviewPurchaseRequests() {
		String s = "";
		Console.printToConsole("\nPurchase Requests to Review...");
		Console.printToConsole("");
		int rvwStatusID = statusMapDescToID.get("New");
		String statusDesc= statusMapIDToDesc.get(rvwStatusID);
		Status newStatus = new Status(rvwStatusID, statusDesc);
		
		for (PurchaseRequest pr : preqDB.getPurchaseRequestsToReview(currentUser, newStatus)){
			s += "Purchase Request ID:\t" + pr.getId() + "\n" +
		         "  User:\t\t\t\t" + pr.getUser().getFirstName() + " " + pr.getUser().getLastName() + "\n" +
			     "  Description:\t\t\t" + pr.getDescription() + "\n" +
		         "  Justification:\t\t" + pr.getJustification() + "\n" +
				 "  Date Needed:\t\t\t" +  pr.getDateNeeded() + "\n" +
		         "  DeliveryMode:\t\t\t" + pr.getDeliveryMode() + "\n" +
				 "  Status:\t\t\t" + pr.getStatus().getDescription() + "\n" + 
				 "  Total:\t\t\t" + formatCurrency(pr.getTotal()) + "\n" +
				 "  Submitted Date:\t\t" + pr.getSubmittedDate() + "\n" +
				 "  Updated by user: " + pr.getUpdatedByUser() + "\n" + 
				 "  PRLI:\n" + pr.toString();
		}
		if (s.equalsIgnoreCase("")) {
			Console.printToConsole("No purchase requests were found to review.");
		} else {
			Console.printToConsole(s);					
		}
	}
	
	public static void loadStatusMaps() {
		statusMapIDToDesc = Status.getstatusMapIDToDesc();
		statusMapDescToID = Status.getstatusMapDescToID();
	}
	
	public static void autoApproveUpTo50() {
		if (currentPR.getTotal() <= 50) {
			int approvedStatusID = statusMapDescToID.get("Approved");
			String approvedStatusDesc= statusMapIDToDesc.get(approvedStatusID);
			Status approvedStatus = new Status(approvedStatusID, approvedStatusDesc);
			currentPR.setStatus(approvedStatus);
		}		
	}
	
	public static void addUser() {
		User newUser = new User();
		newUser.setUsername(Console.getString("Enter username:\t"));
		newUser.setPassword(Console.getString("Enter password:\t"));
		newUser.setFirstName(Console.getString("Enter firstName:\t"));
		newUser.setLastName(Console.getString("Enter lastName:\t"));
		newUser.setPhone(Console.getString("Enter phone:\t"));
		newUser.setEmail(Console.getString("Enter email:\t"));
		newUser.setReviewer(Console.getBoolean("Enter reviewer:\t"));
		newUser.setAdmin(Console.getBoolean("Enter admin:\t"));	
		newUser.setUpdatedByUser(currentUser.getId());
		
		if (uDB.insertUser(newUser)) {
			Console.printToConsole("User " + newUser.getFirstName() + " " + newUser.getLastName() + " added successfully.");
		} else {
			Console.printToConsole("User " + newUser.getFirstName() + " " + newUser.getLastName() + " not added."); 
		}
	}
	
	public static void deleteUser() {
		int deleteUserID = Console.getInt("Enter user ID to delete:\t");
		boolean success = false;
		User userToDelete = null;
		userToDelete = uDB.getUserByUserID(deleteUserID);
		if (userToDelete!=null) {
			success = uDB.deleteUser(userToDelete);				
		}
		if (success){
			Console.printToConsole("User successfully deleted for userID (" + deleteUserID + ").");
		} else {
			Console.printToConsole("No user deleted for user ID (" + deleteUserID + ").");
		}
	}
	
	public static void updateUser() {
		int updateUserID = Console.getInt("Enter user ID to update:\t");
		User userToUpdate = uDB.getUserByUserID(updateUserID);
		System.out.println("User to update: " + userToUpdate);
		userToUpdate.setUsername(Console.getString("Enter username:\t"));
		userToUpdate.setPassword(Console.getString("Enter password:\t"));
		userToUpdate.setFirstName(Console.getString("Enter firstName:\t"));
		userToUpdate.setLastName(Console.getString("Enter lastName:\t"));
		userToUpdate.setPhone(Console.getString("Enter phone:\t"));
		userToUpdate.setEmail(Console.getString("Enter email:\t"));
		userToUpdate.setReviewer(Console.getBoolean("Enter reviewer:\t"));
		userToUpdate.setAdmin(Console.getBoolean("Enter admin:\t"));	
		userToUpdate.setUpdatedByUser(currentUser.getId());

		boolean success = false;
		success = uDB.updateUser(userToUpdate);				
		if (success){
			Console.printToConsole("User successfully updated for userID (" + updateUserID + ").");
		} else {
			Console.printToConsole("No user updated for user ID (" + updateUserID + ").");
		}
	}
	
}
