/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import entity.Customer;
import entity.User;
import java.util.List;
import java.util.Scanner;
import security.SecManager;

/**
 *
 * @author User
 */
public class UserManager {
    private Scanner scan = new Scanner(System.in);
    
    public User createUser(){
        CustomerManager customerManager = new CustomerManager();
        Customer customer = customerManager.createCustomer();
        User user = new User();
        System.out.println(" --- Добавить пользователя --- ");
        System.out.printf("Логин : ");
        user.setLogin(scan.nextLine());
        System.out.printf("Введите пароль: ");
        user.setPassword(scan.nextLine());
        int numRole;
        do {            
            System.out.println("Список ролей: ");
            for (int i = 0; i < SecManager.role.values().length; i++) {
                System.out.printf("%d. %s%n"
                    ,i+1
                    ,SecManager.role.values()[i].toString()
                );
            }
            System.out.printf("Введите номер роли: ");
            String numRoleStr = scan.nextLine();
            try {
                numRole = Integer.parseInt(numRoleStr);
                break;
            } catch (Exception e) {
                System.out.println("Вводите цифрами!");
            }
        } while (true);
        user.setRole(SecManager.role.values()[numRole-1].toString());
        user.setCustomer(customer);
        return user;
    }
    
    public void addUserToArray(User user, List<User> listUsers) {
        listUsers.add(user);
    }
    
    public void printListUsers(List<User> listUsers) {
        int n = 0;
        for (User b : listUsers) {
            if(b != null){
                System.out.println(n+1+". "+b.toString());
                n++;
            }
        }
    }
    
    public User getCheckInUser(List<User> listUsers) {
        System.out.println("===== Вход в систему =====");
        System.out.print("Логин: ");
        String login = scan.nextLine();
        System.out.print("Пароль: ");
        String password = scan.nextLine();
        for (int i = 0; i < listUsers.size(); i++) {
            if(listUsers.get(i) != null && listUsers.get(i).getLogin().equals(login)){
                for (int j = 0; j < 2; j++) {
                    if(listUsers.get(i).getPassword().equals(password)){
                        return listUsers.get(i);
                    }else{
                        System.out.println("Попробуй еще раз");
                        password = scan.nextLine();
                    }
                }
                System.out.println("У вас нет прав!");
                return null;
            }
        }
        System.out.println("У вас нет прав! Зарегистрируйтесь");
        return null;
    }
}