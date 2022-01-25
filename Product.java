import java.io.Serializable;

public class Product implements Serializable {

	public int productID;
	public int productQty;
	public String productName;
	public double productPrice;
	
	private static final long serialVersionUID =1L;
	
	public Product() {
		
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public Product(int productID, int productQty, String productName, double productPrice) {
		this.productID = productID;
		this.productQty = productQty;
		this.productName = productName;
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	
}
