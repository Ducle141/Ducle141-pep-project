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

    

}
