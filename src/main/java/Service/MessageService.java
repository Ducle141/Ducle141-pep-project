package Service;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    
    public Message addMessage(Message message) {
        return MessageDAO.insertMessage(message);
    }
}
