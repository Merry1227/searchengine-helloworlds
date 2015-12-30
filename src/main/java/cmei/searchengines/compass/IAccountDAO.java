package cmei.searchengines.compass;

import java.util.List;

public interface IAccountDAO {
	public void buildIndex(List<Account> accounts);
	
	public List<Account> queryIndex(String keyword);
	
	public void updateIndex(Account account);
	
	public void deleteIndex(long aid);
	
}
