package model.dao;

import db.DB;
import model.dao.impl.CategoryDaoJDBC;
import model.dao.impl.ProductDaoJDBC;

public class DaoFactory {

	public static CategoryDao createCategoryDao() {
		return new CategoryDaoJDBC(DB.getConnection());
	}
	
	public static ProductDao createProductDao() {
		return new ProductDaoJDBC(DB.getConnection());
	}
}
