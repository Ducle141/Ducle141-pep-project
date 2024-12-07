package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    
    public SocialMediaController(){
        this.accountService = new AccountService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);  
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);

        return app;
    }
    
    /**
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into Account object.
     * If accountService post account successfully, it will return a json of the posted account.
     * If accountService returns a null account (meaning posting account was failed, the API will return 400 message (client error))
     * @param ctx
     * @throws JsonProcessingException
     */
    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    /**
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
     * The request body will contain a JSON representation of an Account, not containing an account_id. 
     * 
     * 
     */
    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        String username = account.getUsername();
        String password = account.getPassword();

        Account foundAccount = accountService.getAccountByLogin(username, password);

        if (foundAccount != null) {
            ctx.json(mapper.writeValueAsString(foundAccount));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /***
     * The request body will contain a JSON representation of the message, which should be persisted to the database,
     * but will not contain the message_id.
     * The creation of message will be successful of the message_text is not black, not over 255 character long, and posted_by 
     * refers to an existing user in database. If successful, the response body will contain a JSON of the message, including auto_generate 
     * message_id with a response status of 200. If unsucessful, the response status will be 400.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        //Message Object contains text body, account_id of sender and time posted
        int foundAccountID = message.getPosted_by();
        Account foundAccount = accountService.getAccountByID(foundAccountID);
        String messageText = message.getMessage_text();
        Long messageTimePosted = message.getTime_posted_epoch();

        if (!messageText.isBlank() && messageText.length() < 255 && foundAccount != null) {
            Message messageToAdd = new Message(foundAccountID, messageText, messageTimePosted);
            Message messageAdded = MessageService.addMessage(messageToAdd);
            //messageAdded in database automatically has message_id
            ctx.json(mapper.writeValueAsString(messageAdded));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = MessageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messages));
        ctx.status(200);
    }

    /*
     * It is expected for the response body to simply be empty if there is no such message, no such message_id.
     */
    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = MessageService.getMessageByID(id);
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
            ctx.status(200);
        } else {
            ctx.status(200);
        }
    }

    /*
     * The returning response body should contain a JSON representation of a list of all messages posted by a particular user.
     * The empty list is expected if that user does not have any message.
     */
    private void getAllMessagesByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int accountID = Integer.parseInt(ctx.pathParam("account_id")); 
        List<Message> messages = MessageService.getMessagesByAccountID(accountID);
        ctx.json(mapper.writeValueAsString(messages));
        ctx.status(200);
    }    

    /*
     *  The deletion of an existing message should remove an existing message from the database. If the message existed, 
     * the response body should contain the now-deleted message. The response status should be 200, which is the default.
     */
    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = MessageService.deleteMessageByID(id);
        
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
        }
        ctx.status(200);
    }

    /*
     * The request body should contain a new message_text values to replace the message identified by message_id.
     *  The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
     * If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), 
     * and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
     */
    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // System.out.println("START--------------");
        // System.err.println(ctx.body());
        int messageIDToUpdate = Integer.parseInt(ctx.pathParam("message_id")); 
        // System.out.println(messageIDToUpdate);
        Message newMessageTextToUpdate = mapper.readValue(ctx.body(),Message.class); 
        String newMessageText = newMessageTextToUpdate.getMessage_text();

        if(!newMessageText.isBlank() && 
                newMessageText.length() < 255 && MessageService.getMessageByID(messageIDToUpdate) != null) {
            Message updatedMessage = MessageService.updateMessage(messageIDToUpdate, newMessageTextToUpdate);
            updatedMessage.setMessage_text(newMessageText);

            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);

        } else {
            ctx.status(400);
        }        
    }
}