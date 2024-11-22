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

    // public AccountDAO getAccountWithUsername(Account account) {
    //     return AccountDAO.get
    // }

    public Account insertAccount(Account account) {
        if (account.getUsername() != null && !accountDAO.getAllAccounts().contains(account) && account.getPassword().length() > 4) {
            return accountDAO.insertAccount(account);
        }
        return null;
    }

}
