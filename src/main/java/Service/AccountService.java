package Service;
import DAO.AccountDAO;
import Model.Account;
import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }   

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /***
     * The business Login here is username is not blank, password is at least 4 characters long, and an Account with that username
     * does not already exist.If these conditions are met, the response body should contain a JSON of the posted account, including account_id.
     * The response status is 200 if successful and 400 (client error) if unsuccessful.
     * @param account
     * @return
     */
    public Account addAccount(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;   
        }
        List<Account> listAccounts = getAllAccounts();
        for (Account acc : listAccounts) {
            if (acc.getAccount_id() == account.getAccount_id()) {
                return null;
            }
        }
        Account account1 = accountDAO.insertAccount(account);
        return account1;
    }

    public Account getAccountByLogin(String username, String password) {
        return AccountDAO.getAccountByLogin(username, password);
    }

    public Account getAccountByID(int id) {
        return AccountDAO.getAccountByID(id);
    }
}
