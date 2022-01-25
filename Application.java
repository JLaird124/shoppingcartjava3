import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

	static int numTransactions = 0;
	static double statTakings = 0;
	static DecimalFormat currency = new DecimalFormat("0.00");//for money values
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	static Scanner input = new Scanner(System.in);

	static int centralID = 1001;
	static int drinkID = 1;
	static int foodID = 10;
	static ArrayList<Product> allProducts = new ArrayList<Product>();
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Food> allFoods = new ArrayList<Food>();
	static ArrayList<Product> cart = new ArrayList<Product>();
	static String[] discountCodes = { "AE5"};//only working discoutn code 

	static double transactionPrice = 0;//for stats and checkotu 
	static double totalTakings = 0;

	public static void main(String[] args) {//mainmethod 

		File f = new File("data.ser");

		if (f.exists()) {//if file exists then proceed
			try {
				loadData();
			} catch (Exception e) {
				System.out.println("There are currently no products in the system");
			}
			menu();

		} else {

			menu();
		}
	}
/**
 * User picks number 1-8, or saves or exits
 * choice = user input
 * choice sends user to relevant method 
 * default set up to apply for user mistakes
 * 
 * 
 */
	public static void menu() {//brain of my entire system 

		System.out.println("Press 1 to create a product");
		System.out.println("press 2 to view all products");
		System.out.println("press 3 to add product to cart");
		System.out.println("Press 4 to view cart");
		System.out.println("Press 5 to checkout");
		System.out.println("Press 6 to view stats");
		System.out.println("Press 7 to view all drinks");
		System.out.println("Press 8 to view all foods");
		System.out.println("Press S to Save");
		System.out.println("Press x to exit the program");

		String choice = input.next();
		choice = choice.toLowerCase();

		switch (choice) {
		case "1": {
			try {
				createProduct();
			} catch (IOException e) {
				System.out.println("Error Logged");
			}
			break;
		}
		case "2": {
			viewProducts();
			break;
		}
		case "3": {
			try {
				addToCart();
				
			} catch (Exception e) {
				System.out.println("Please start again and enter a valid product ID");
			}
			break;
		}
		case "4": {
			viewCart();
			break;
		}
		case "5": {
			checkOut();
			break;
		}
		case "6": {
			stats();
			break;
		}
		case "7": {
			viewDrinks();
			break;
		}
		case "8": {
			viewFoods();
			break;
		}
		case "s": {
			try {
				saveData();
			} catch (Exception e) {
				System.out.println("Error");
			}
			break;
		}
		case "x":{
			System.out.println("You have exited the program");
			System.exit(0);
		}
		default: {
			System.out.println("Invalid choice try again");
			break;
		}
		}

		menu();
	}
/*
 * loading and importing any products that I had added during testing and 
 * any future projects that may arise 
 */
	private static void loadData() throws IOException {

		FileInputStream importFile;
		try {
			importFile = new FileInputStream("data.ser");

			ObjectInputStream reader = new ObjectInputStream(importFile);

			allProducts = (ArrayList<Product>) reader.readObject();
			centralID = allProducts.size() + centralID;
			System.out.println("File Loaded");
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Data.ser was not found");
		} catch (ClassNotFoundException e) {
			System.out.println("Error reading file");
		}
	}

	private static void saveData() throws Exception {
		FileOutputStream exportFile = new FileOutputStream("data.ser");// creates
		ObjectOutputStream writer = new ObjectOutputStream(exportFile); // writes

		writer.writeObject(allProducts);// writes the arraylist object
		System.out.println("File Successfully Saved");
		writer.close();

	}
/**
 * Enter ID 
 * is ID = to product?
 * Yes, then print name
 * choose quantity is valid number?
 * if yes add to product to basket
 * if not then let user pick product another product
 * 
 * is ID != product
 * then send user back to menu and try again 
 */
	private static void addToCart() throws Exception {

		viewProducts();
		System.out.println("Enter Product by ID number to add to cart");
		int chosenProduct = input.nextInt();

		for (Product p : allProducts) {
			if (chosenProduct == p.getProductID()) {
				System.out.println("You have chosen " + p.getProductName());
				System.out.println("There are currently " + p.getProductQty() + " in stock");
				System.out.println("Enter quantity to add to cart");
				int chosenQty = input.nextInt();//watching for my users input 

				if (chosenQty > p.getProductQty()) {
					System.out.println("Not enough item in stock transaction cancelled please try again");
					addToCart();//looping back to cart
				} else {
					Product cartItem = new Product();
					cartItem.setProductID(p.getProductID());
					cartItem.setProductName(p.getProductName());
					cartItem.setProductPrice(p.getProductPrice());
					cartItem.setProductQty(chosenQty);
					p.setProductQty(p.getProductQty() - chosenQty);
					cart.add(cartItem);
				}
			}
		}
		menu(); //making program recursive 
	}

	private static void viewCart() {
		transactionPrice = 1;
		for (Product p : cart) {
			System.out.println("************************");
			System.out.println("ID: \t" + p.getProductID());
			System.out.println("Name: \t" + p.getProductName());
			System.out.println("Price: \t" + p.getProductPrice());
			System.out.println("Quantity: \t" + p.getProductQty());
			System.out.println("************************");
			transactionPrice = transactionPrice + (p.getProductPrice() * p.getProductQty());//maths to add up the total 
		}
		System.out.println("Cart Total is " + transactionPrice);

	}
/**
 * Yes or no for Code
 * if yes then 3 attempts to enter discount code
 * is discount code correct?
 * if yes then foundvalid=true
 * if not then print message
 * if foundvalid = true discount code is applied
 * if foundvalid = false discount code not applied 2 more tries
 * after 3 tries user automatically sent on to next step
 * 
 */
	private static void checkOut() {

		viewCart();
		System.out.println("Do you have a discount code,type yes or no");
		String choice = input.next();//using scanner to avoid IO error from reader
		double discountPercentage = 0;

		if (choice.equals("yes")) {
			for (int attempt = 0; attempt < 3; attempt++) {// gives user 3 attempts to get correct code 
				System.out.println("Please enter your discount code");
				choice = input.next();
				boolean foundValid = false;//initialize my boolean 
				for (int i = 0; i < discountCodes.length; i++) {
					if (choice.equalsIgnoreCase(discountCodes[i])) {
						discountPercentage = 0.1;
						foundValid = true;//changes boolean if correct code 
						break;
					}

				}

				if (!foundValid) {
					System.out.println("The discount code you entered is incorrect");
				} else {
					System.out.println("Discount code has been applied! " + discountPercentage * 100 + "%");//maths to show percentage discount
					break;
				}
			}
		}

		totalTakings = (transactionPrice * (1 - discountPercentage)) + totalTakings;//maths to calculate price is uneffected if no discount code

		System.out.println("Cart Total is now " + currency.format(totalTakings));
		
		System.out.print("Please enter payment");

		double moneyEntered = input.nextDouble();

		while (moneyEntered < totalTakings) {
			System.out.println("Balance remaining: " + currency.format((totalTakings - moneyEntered)));
			System.out.println("Enter Remaining Balance");
			moneyEntered = moneyEntered + input.nextDouble();

		}

		// When we reach this point, the transaction has been successful
		if ((moneyEntered - totalTakings) > 0.00000001)// if our change is not 0
		{
			System.out.println("***********************************************");
			System.out.println("Transaction Sucessful. Please take your receipt");
			System.out.println("Change is " + currency.format((moneyEntered - totalTakings)));
			System.out.println("***********************************************");
			numTransactions++;
		}

		if ((moneyEntered - totalTakings) < 0.000000001)// if our change is = to 0
		{
			System.out.println("***********************************************");
			System.out.println("Transaction Sucessful. Please take your receipt");
			System.out.println("***********************************************");
			numTransactions++;

		}
		// Adjust Stats

		numTransactions++;
		statTakings = statTakings + totalTakings;
		

	}
	/*
	 * basic view all methods down here 
	 */
	
	
	private static void viewProducts() {
		for (Product p : allProducts) {
			System.out.println("************************");
			System.out.println("ID: \t" + p.getProductID());
			System.out.println("Name: \t" + p.getProductName());
			System.out.println("Price: \t" + p.getProductPrice());
			System.out.println("Quantity: \t" + p.getProductQty());
			System.out.println("************************");
		}

	}
	
	private static void viewDrinks() {
		for (Drink p : allDrinks) {
			System.out.println("************************");
			System.out.println("ID: \t" + p.getProductID());
			System.out.println("Name: \t" + p.getProductName());
			System.out.println("Price: \t" + p.getProductPrice());
			System.out.println("Quantity: \t" + p.getProductQty());
			System.out.println("Drink ID: \t" + p.getDrinkID());
			System.out.println("Drink Type: \t" + p.getDrinkType());
			System.out.println("************************");
		}
	}
		
		private static void viewFoods() {
			for (Food p : allFoods) {
				System.out.println("************************");
				System.out.println("ID: \t" + p.getProductID());
				System.out.println("Name: \t" + p.getProductName());
				System.out.println("Price: \t" + p.getProductPrice());
				System.out.println("Quantity: \t" + p.getProductQty());
				System.out.println("Food ID: \t" + p.getFoodID());
				System.out.println("Food Type: \t" + p.getFoodType());
				System.out.println("************************");
			}

	}

	private static void createProduct() throws IOException {

		/*
		 * watching for input then proceeding with creating product
		 * depending on whether is a food or drink 
		 */
		System.out.println("Is the product a drink or food?");
		System.out.println("Please enter d for drink and f for food");

		String choice = input.next();
		choice = choice.toLowerCase();

		switch (choice) {
		case "f": {
			try {
				createFood();
			} catch (Exception e) {
				System.out.println("Error Logged");
			}
			break;
		}
		case "d": {

			try {
				createDrink();

			} catch (Exception e) {
				System.out.println("Error Logged");
			}
			break;

		}
		default: {
			System.out.println("Invalid choice try again");
			break;
		}

		}
		menu();
	}
/*
 * creating food or drink methods 
 */
	private static void createDrink() throws Exception {
		Drink p = new Drink();
		p.setProductID(centralID);
		centralID++;
		System.out.println("Enter a product title");
		p.setProductName(reader.readLine());
		System.out.println("Enter Product Price");
		p.setProductPrice(input.nextDouble());
		System.out.println("Please enter Product quantity");
		p.setProductQty(input.nextInt());
		System.out.println("Is it a soft or still drink?");
		p.setDrinkType(input.next());
		p.setDrinkID(drinkID);
		drinkID++;

		allProducts.add(p);
		allDrinks.add(p);
	}

	private static void createFood() throws Exception {
		Food p = new Food();
		p.setProductID(centralID);
		centralID++;
		System.out.println("Enter a product title");
		p.setProductName(reader.readLine());
		System.out.println("Enter Product Price");
		p.setProductPrice(input.nextDouble());
		System.out.println("Please enter Product quantity");
		p.setProductQty(input.nextInt());
		System.out.println("Is it served hot or cold?");
		p.setFoodType(input.next());
		p.setFoodID(foodID);
		foodID++;

		allProducts.add(p);
		allFoods.add(p);
	}
/*
 * basic stats method with all relavant maths 
 */
	public static void stats()// stats for average and total.
	{
		System.out.println("***********************************");
		System.out.println("Number of Transactions: " + numTransactions);
		System.out.println("Total Takings: \t" + currency.format(statTakings));
		double average = statTakings / numTransactions;
		System.out.println("Average Transaction Value :" + currency.format(average));
		System.out.println("***********************************");
		menu();

	}
}
