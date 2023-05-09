package application;

import java.util.Locale;
import java.util.Scanner;

import model.dao.CategoryDao;
import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

public class Program {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		ProductDao productDao = DaoFactory.createProductDao();
		CategoryDao categoryDao = DaoFactory.createCategoryDao();
		
		System.out.print("Enter your name: ");
		String name = sc.nextLine();
		
		char exit;
		do {
			System.out.print(name+ ", would you like to register products [a], "
					+ "add them to inventory [b], "
					+ "or remove them from inventory [c]?");
			char option = sc.nextLine().toLowerCase().charAt(0);
			while (option != 'a' && option != 'b' && option != 'c') {
				System.out.print("Invalid option! Try again: ");
				option = sc.nextLine().toLowerCase().charAt(0);
			}
			
			switch (option) {
			case 'a':
				System.out.print("Enter the product ID: ");
				int id = sc.nextInt();
				while(productDao.findById(id) != null) {
					System.out.print("ID has already been registered. Please select another one: ");
					id = sc.nextInt();
				}
				
				System.out.print("Enter the name of the product: ");
				sc.nextLine();
				String pName = sc.nextLine();
				System.out.print("Enter the unit price of the product ");
				double price = sc.nextDouble();
				System.out.print("How many units to add to inventory? ");
				int quantity = sc.nextInt();
				System.out.println("Enter the category of the product:"
						          + "\nAvailable categories:"
						          + "\n1 - Electronics"
						          + "\n2 - Furniture"
						          + "\n3 - Clothing"
						          + "\n4 - Jewlery"
						          + "\n5 - Food / Drinks"
						          + "\n6 - Tools");
				int categoryId = sc.nextInt();
				Category category = categoryDao.finById(categoryId);
				Product product = new Product(id, pName, price, quantity, category);
				productDao.insert(product);
				System.out.print(quantity+ " units of " + product.getName() + " added to inventory! "
						         + "Total value: $" + (product.getUnitPrice() * quantity));
				break;
			case 'i':
				System.out.print("Enter product ID: ");
				id = sc.nextInt();
				while (productDao.findById(id) == null) {
					System.out.print("Product not registered! Please select another ID: ");
					id = sc.nextInt();
				}
				product = productDao.findById(id);
				System.out.print("How many units of " +product.getName()+ " will be added to inventory? ");
				int insert = sc.nextInt();
				quantity = insert + product.getQtInStock();
				product.setQtInStock(quantity);
				System.out.println(insert+ " units of " +product.getName()+ " added to inventory!"
						          +"\nTotal quantity in inventory: " +product.getQtInStock()+ " units!");
				productDao.update(product);
				break;
			case 'r':
				System.out.print("Enter product ID: ");
				id = sc.nextInt();
				while(productDao.findById(id) == null) {
					System.out.print("Product not registered! Please select another ID: ");
					id = sc.nextInt();
				}
				product = productDao.findById(id);
				System.out.print("How many units of " +product.getName()+ " will be removed from inventory? ");
				quantity = sc.nextInt();
				while(quantity > product.getQtInStock()) {
					System.out.print("The quantity entered is greater than the quantity available in the inventory!"
							+ "\nSelect another quantity: ");
					quantity = sc.nextInt();
				}
				int remove = product.getQtInStock() - quantity;
				product.setQtInStock(remove);
				productDao.update(product);
				System.out.println(quantity+ " units of " +product.getName()+ " removed from stock!"
						          + "\nTotal quantity in inventory: " +product.getQtInStock()+ " units!");
				break;
	
			default:
				System.out.print("Invalid option");
				break;
			}
			System.out.println();
			System.out.print("Leave the program or continue? ");
			sc.nextLine();
			exit = sc.nextLine().toLowerCase().charAt(0);
		}while(exit == 'c');
		sc.close();

	}

}
