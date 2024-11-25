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

    // public AccountDAO getAccountByUsername(Account account) {
    //     return AccountDAO.getAccountBy
    // }

    public Account insertAccount(Account account) {
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

    public Account loginAccount(Account account) {
        String currentAccountUsername = account.getUsername();
        String currentAccountPassword = account.getPassword();

        if (accountDAO.getAllAccounts().contains(account)) {
            
        }
    }
}
