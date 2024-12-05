package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
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
        
        app.post("/register", this::postUserHandler);
        app.post("/login", this::loginUserHandler);
        app.post("/messages", this::postMessageHandler);

        return app;
    }
    
    private void postUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }
    private void loginUserHandler(Context ctx) throws JsonProcessingException {
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
}