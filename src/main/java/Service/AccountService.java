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
        // System.out.println("START");
        // System.out.println(listAccounts);
        // System.out.println("END");
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
