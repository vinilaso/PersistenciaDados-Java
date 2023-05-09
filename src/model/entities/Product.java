package model.entities;

public class Product {

	// attributes
	private Integer id;
	private String name;
	private Double unitPrice;
	private Integer qtInStock;
	private Category category;
	
	// constructors
	public Product() {
	}

	public Product(Integer id, String name, Double unitPrice, Integer qtInStock, Category category) {
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
		this.qtInStock = qtInStock;
		this.category = category;
	}

	// methods
	@Override
	public String toString() {
		return "id - " + id
				+ " | name -" + name
				+ " | unit_price - " + unitPrice
				+ " | qt_in_stock - " + qtInStock
				+ " | category_id - " + category.getId();
	}
	
	// getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQtInStock() {
		return qtInStock;
	}

	public void setQtInStock(Integer qtInStock) {
		this.qtInStock = qtInStock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
