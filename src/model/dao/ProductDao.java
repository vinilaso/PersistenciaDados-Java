package model.dao;

import java.util.List;

import model.entities.Category;
import model.entities.Product;

public interface ProductDao {

	void insert(Product product);
	void update(Product product);
	void deleteById(Integer id);
	Product findById(Integer id);
	List<Product> findAll();
	List<Product> findByCategory(Category cat);
}
