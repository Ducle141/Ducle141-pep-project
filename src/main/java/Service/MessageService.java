package Service;
import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    MessageDAO messageDAO;
    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public static List<Message> getAllMessages() {
        return MessageDAO.getAllMessages();
    }
    public static Message addMessage(Message message) {
        return MessageDAO.insertMessage(message);
    }

    public static Message getMessageByID(int id) {
        return MessageDAO.getMessageByID(id);
    }
    
    public static List<Message> getMessagesByAccountID(int id) {
        return MessageDAO.getMessagesByAccountID(id);
    }

    public static Message deleteMessageByID(int id) {
        return MessageDAO.deleteMessageByID(id);
    }

    public static Message updateMessage(int Message_id, Message Message){
        return MessageDAO.getMessageByID(Message_id);
    }

}

