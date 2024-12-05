package DAO;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

import Model.Message;

public class MessageDAO {
    public static Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), 
                    message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.print(e.getMessage());
        } 
        return null;
    }    

    public static List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message (rs.getInt("message_id"), 
                    rs.getInt("message.posted_by"),        
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public static Message getMessageByID(int id)  {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message where message_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }

        }   catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<Message> getMessagesByAccountID(int id)  {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message where posted_by=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages; 
    }

    public static Message deleteMessageByID(int id)  {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "delete from message where message_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            Message deletedMessage = getMessageByID(id); //get deleted message before executing delete
            // ^ this may not be necessary if pkeyresultset returns the deleted fields
            //                 !!!!! ^^^^
            preparedStatement.executeUpdate(); //execute delete

            //ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            /**
             * if(pkeyResultSet.next()){
                /**
                 * int deleted_message_id = (int) pkeyResultSet.getInt(1);
                int deleted_posted_by = pkeyResultSet.getInt(2);
                String deleted_message_text = pkeyResultSet.getString(3);
                long deleted_time_posted_epoch = pkeyResultSet.getLong(4);
                return new Message(deleted_message_id, deleted_posted_by, deleted_message_text, 
                    deleted_time_posted_epoch);
             }
             */
            return deletedMessage;

        }   catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
