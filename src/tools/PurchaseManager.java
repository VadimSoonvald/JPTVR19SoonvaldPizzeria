/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import entity.Customer;
import entity.Product;
import entity.Purchase;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import jptvr19soonvaldpizzeria.App;
import security.SecManager;

/**
 *
 * @author User
 */
public class PurchaseManager {
    private Scanner scan = new Scanner(System.in);
    private CustomerManager customerManager = new CustomerManager();
    private ProductManager productManager = new ProductManager();
    private StorageManager storageManager = new StorageManager();
    
    public void makeDeal(List<Customer> listCustomers, List<Product> listProducts, List<Purchase> listPurchases){
        System.out.println(" ===== СПИСОК ПИЦЦ ===== ");
        int productNum;
        do {            
            if(!productManager.printListProducts(listProducts)){
            return;
            }
            System.out.printf("Выберите номер пиццы: ");
                String productNumStr = scan.nextLine();
            try {
                productNum = Integer.parseInt(productNumStr);
                if(productNum < 1 && productNum >= listProducts.size()){
                    throw new Exception("Выход за диапазон доступных пицц");
                }
                break;
            } catch (Exception e) {
                System.out.println("Выберите номер из указанного выше списка");
                productNumStr = scan.nextLine();
            }
        } while (true);
        Product product = listProducts.get(productNum-1);
        Customer customer = null;
        if(SecManager.role.MANAGER.toString().equals(App.loggedInUser.getRole())){
            int customerNum;
            do {            
                System.out.println(" ===== СПИСОК КЛИЕНТОВ ===== ");
                customerManager.printListCustomers(listCustomers);
                System.out.printf("Выберите номер клиента: ");
                String productNumStr = scan.nextLine();
                try {
                    customerNum = Integer.parseInt(productNumStr);
                    if(customerNum < 1 && customerNum >= listCustomers.size()){
                        throw new Exception();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Выберите номер из указанного выше списка");
                }
            } while (true);
            customer = listCustomers.get(productNum-1);
        }else if (SecManager.role.CUSTOMER.toString().equals(App.loggedInUser.getRole())){
            customer = App.loggedInUser.getCustomer();
        }
        Calendar calendar = new GregorianCalendar();
        
        double residual;
        residual = customer.getBalance() - product.getPrice();
        if (residual < 0){
            System.out.println("Недостаточно средств для покупки");
            System.out.println("Баланс: "+customer.getBalance()+"€");
            return;
        }else{
            customer.setBalance(residual);
            Purchase purchase =  new Purchase(customer, product, calendar.getTime());
            this.addPurchaseToArray(purchase, listPurchases);
        }
    }
    public void addPurchaseToArray(Purchase purchase, List<Purchase> listPurchases){
        listPurchases.add(purchase);
        storageManager.save(listPurchases, App.storageFile.PURCHASES.toString());
    }
    
    public boolean printListPurchases(List<Purchase> listPurchases){
        boolean notDeals = true;
        if(SecManager.role.MANAGER.toString().equals(App.loggedInUser.getRole())){
            for (int i = 0; i < listPurchases.size(); i++) {
                if(listPurchases.get(i)!=null)
                    System.out.printf("%d. Клиент %s %s купил \"%s\"%n"
                    ,i+1
                    ,listPurchases.get(i).getCustomer().getFirstName()
                    ,listPurchases.get(i).getCustomer().getLastName()
                    ,listPurchases.get(i).getProduct().getName()
                );
                notDeals = false;
            }
            if(notDeals){
                System.out.println("Журнал покупок пуст");
                return false;
            }
        }else if (SecManager.role.CUSTOMER.toString().equals(App.loggedInUser.getRole())){
            for (int i = 0; i < listPurchases.size(); i++) {
                if(listPurchases.get(i)!=null)
                    System.out.printf("%d. Клиент %s %s купил \"%s\"%n"
                    ,i+1
                    ,listPurchases.get(i).getCustomer().getFirstName()
                    ,listPurchases.get(i).getCustomer().getLastName()
                    ,listPurchases.get(i).getProduct().getName()
                );
                notDeals = false;
            }
            if(notDeals){
                System.out.println("Журнал покупок пуст");
                return false;
            }
        }
    return true;
    }
}
