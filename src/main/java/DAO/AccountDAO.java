package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (username, password) values (?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                int i = (int) rs.getLong(1);
                return new Account(i, account.getUsername(), account.getPassword());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /***
     * The login will be sucessful if the username and password provided from user in the request body JSON match
     * a real account existing in the database.If successful, the response body should contain a JSON of the account in the response body, including its account_id.
     * The response status will be 200.
     * If unsuccessful, the response status is 401 (unauthorized)
     * @param username
     * @param password
     * @return
     */
    public static Account getAccountByLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Account result_account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return result_account; 
            } else {
                return null;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public static Account getAccountByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                rs.getString("password"));
                return account;
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
