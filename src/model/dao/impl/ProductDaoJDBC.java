package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

public class ProductDaoJDBC implements ProductDao {
	
	private Connection conn;
	
	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	private Category instanciateCategory(ResultSet rs) throws SQLException{
		Category cat = new Category();
		cat.setId(rs.getInt("id"));
		cat.setDescription(rs.getString("cat_desc"));
		return cat;
	}
	
	private Product instanciateProduct(ResultSet st, Category cat) throws SQLException{
		Product obj = new Product();
		obj.setId(st.getInt("id"));
		obj.setName(st.getString("name"));
		obj.setUnitPrice(st.getDouble("unit_price"));
		obj.setQtInStock(st.getInt("qt_in_stock"));
		obj.setCategory(cat);
		return obj;
	}

	public void remove(Integer quantity, Product product) {
		
	}
	
	@Override
	public void insert(Product product) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO product "
					+ "(id, name, unit_price, qt_in_stock, category_id) "
					+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, product.getId());
			st.setString(2, product.getName());
			st.setDouble(3, product.getUnitPrice());
			st.setInt(4, product.getQtInStock());
			st.setInt(5, product.getCategory().getId());
			
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					product.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Product product) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE product "
					+ "SET id=?, name=?, unit_price=?, qt_in_stock=?, category_id=? "
					+ "WHERE id = ?");
			
			st.setInt(1, product.getId());
			st.setString(2, product.getName());
			st.setDouble(3, product.getUnitPrice());
			st.setInt(4, product.getQtInStock());
			st.setInt(5, product.getCategory().getId());
			st.setInt(6, product.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM product WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Product findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT product.*,category.description as cat_desc "
					+ "FROM product INNER JOIN category "
					+ "ON product.category_id = category.id "
					+ "WHERE product.id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Category cat = instanciateCategory(rs);
				Product prod = instanciateProduct(rs, cat);
				return prod;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Product> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT product.*,category.description as cat_desc "
					+ "FROM product INNER JOIN category "
					+ "ON product.category_id = category.id "
					+ "ORDER BY id");
			
			rs = st.executeQuery();
			List<Product> list = new ArrayList<>();
			Map<Integer, Category> map = new HashMap<>();
			
			while(rs.next()) {
				Category cat = map.get(rs.getInt("category_id"));
				if(cat == null) {
					cat = instanciateCategory(rs);
					map.put(rs.getInt("category_id"), cat);
				}
				Product obj = instanciateProduct(rs, cat);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Product> findByCategory(Category cat) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT product.*,category.description as cat_desc "
					+ "FROM product INNER JOIN category "
					+ "ON product.category_id = category.id "
					+ "WHERE category_id = ? "
					+ "ORDER BY name");
			
			st.setInt(1, cat.getId());
			rs = st.executeQuery();
			List<Product> list = new ArrayList<>();
			Map<Integer, Category> map = new HashMap<>();
			
			while(rs.next()) {
				Category category = map.get(rs.getInt("category_id"));
				if(category == null) {
					category = instanciateCategory(rs);
					map.put(rs.getInt("category_id"), category);
				}
				Product obj = instanciateProduct(rs, category);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
