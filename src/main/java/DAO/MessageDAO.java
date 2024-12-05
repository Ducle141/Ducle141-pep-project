package DAO;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLClientInfoException;
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
}
