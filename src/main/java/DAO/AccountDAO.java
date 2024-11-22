package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
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

    /**
     * TODO: 
     * Unlike some of the other insert problems, the primary key here will be provided by the client as part of the
     * Book object. Given the specific nature of an ISBN as both a numerical organization of books outside of this
     * database, and as a primary key, it would make sense for the client to submit an ISBN when submitting a book.
     * You only need to change the sql String and leverage PreparedStatement's setString and setInt methods.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "insert into account (username, password) values (?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.setString(3, account.getPassword());

            preparedStatement.executeUpdate();
            return account;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

        // /**
    //  * TODO: 
    //  * You only need to change the sql String and leverage PreparedStatement's setString and setInt methods.
    //  * @return a book identified by isbn.
    //  */
    // public Account getAccountByUsername(int account_id){
    //     Connection connection = ConnectionUtil.getConnection();
    //     try {
    //         //Write SQL logic here
    //         String sql = "select * from account where account_id = ?";
    //         PreparedStatement preparedStatement = connection.prepareStatement(sql);

    //         //write preparedStatement's setInt method here.

    //         ResultSet rs = preparedStatement.executeQuery();
    //         while(rs.next()){
    //             Account account = new Account(rs.getInt("account_id"),
    //                     rs.getString("username"),
    //                     rs.getString("password"));
    //             return account;
    //         }
    //     }catch(SQLException e){
    //         System.out.println(e.getMessage());
    //     }
    //     return null;
    // }
}
