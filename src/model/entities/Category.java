package model.entities;

public class Category {

	// attributes
	private Integer id;
	private String description;
	
	// constructors
	public Category() {
	}
	
	public Category(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	// methods
	@Override
	public String toString() {
		return "id - " + id + " | description - " + description;
	}
	
	// getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
