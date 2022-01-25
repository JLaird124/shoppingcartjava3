import java.io.Serializable;

public class Food extends Product implements Serializable {

	
	
	public int foodID;
	public String foodType;
	
	private static final long serialVersionUID =1L;
	
	public Food() {
		
	}

	public Food(int productID, int productQty, String productName, int productPrice, int foodID, String foodType) {
		super(productID,productQty,productName,productPrice);
		this.foodID = foodID;
		this.foodType = foodType;
	}

	public int getFoodID() {
		return foodID;
	}

	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}
	
}
