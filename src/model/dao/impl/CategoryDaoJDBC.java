package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CategoryDao;
import model.entities.Category;

public class CategoryDaoJDBC implements CategoryDao {

	private Connection conn;
	
	public CategoryDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	private Category instanciateCategory(ResultSet rs) throws SQLException{
		Category cat = new Category();
		cat.setId(rs.getInt("id"));
		cat.setDescription(rs.getString("description"));
		return cat;
	}
	
	@Override
	public void insert(Category obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO category "
					+ "(id, description) "
					+ "VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getId());
			st.setString(2, obj.getDescription());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(Category obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE category "
					+ "SET id = ?, description = ? "
					+ "WHERE id = ?");
			
			st.setInt(1, obj.getId());
			st.setString(2, obj.getDescription());
			st.setInt(3, obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM category WHERE id = ?");
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
	public Category finById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM category "
					+ "WHERE id IN (?)");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Category cat = instanciateCategory(rs);
				return cat;
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
	public List<Category> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM category "
					+ "ORDER BY id");
			
			rs = st.executeQuery();
			List<Category> list = new ArrayList<>();
			
			while(rs.next()) {
				Category cat = instanciateCategory(rs);
				list.add(cat);
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
