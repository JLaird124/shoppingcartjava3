import java.io.Serializable;

public class Drink extends Product implements Serializable {

	public int drinkID;
	public String drinkType;
	
	private static final long serialVersionUID =1L;
	
	public Drink() {
		
	}

	public Drink(int productID, int productQty, String productName, int productPrice, int drinkID, String drinkType) {
		super(productID,productQty,productName,productPrice);
		this.drinkID = drinkID;
		this.drinkType = drinkType;
	}

	public int getDrinkID() {
		return drinkID;
	}

	public void setDrinkID(int drinkID) {
		this.drinkID = drinkID;
	}

	public String getDrinkType() {
		return drinkType;
	}

	public void setDrinkType(String drinkType) {
		this.drinkType = drinkType;
	}
	
}
