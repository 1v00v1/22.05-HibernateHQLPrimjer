import model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Insert product using HQL
        insertProduct("Laptop", 1200);
        insertProduct("Smartphone", 800);

        // Select products using HQL
        selectProduct();

        // Update product using HQL
        updateProductPrice("Laptop", 1300);

        // Select products again to see the update
        selectProduct();

        // Delete product using HQL
        deleteProduct("Smartphone");

        // Select products again to see the deletion
        selectProduct();

        HibernateUtil.shutdown();

    }

    private static void insertProduct(String name, double price) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            Product product = new Product(name, price);
            session.save(product);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    public static void selectProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("from Product", Product.class).list();

            for (Product p : products) {
                System.out.println(" - " + p.getName() + ": $" + p.getPrice());
            }
        }
    }

    public static void updateProductPrice(String name, double newPrice){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         Transaction transaction = session.beginTransaction();
         String hql = "UPDATE Product set price = :newPrice WHERE name = :name";
         session.createQuery(hql)
                 .setParameter("newPrice",newPrice)
                 .setParameter("name",name)
                 .executeUpdate();
         transaction.commit();

        }
    }
    public static void deleteProduct(String name){
        try(Session session= HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            String hql= "DELETE FROM Product WHERE name = :name";
            session.createQuery(hql)
                    .setParameter("name",name)
                    .executeUpdate();
            transaction.commit();

        }
    }
}
