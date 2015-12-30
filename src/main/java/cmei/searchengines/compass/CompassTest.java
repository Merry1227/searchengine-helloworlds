package cmei.searchengines.compass;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CompassTest {
	
	public static void main(String[] args){
		ApplicationContext context=new ClassPathXmlApplicationContext("compass.xml");
		IAccountDAO accountDAO=(IAccountDAO)context.getBean("accountDAO");
//		List<Account> accounts=new ArrayList<Account>();
//		Account account1=new Account(1101);
//		account1.setOwner("owner1");
//		account1.setBalance(11.01f);
//		Account account2=new Account(1102);
//		account2.setOwner("owner2");
//		account2.setBalance(11.02f);
//		accounts.add(account1);
//		accounts.add(account2);
//		
//		accountDAO.buildIndex(accounts);
		
//		account2.setBalance(110.2f);
//		accountDAO.updateIndex(account2);
		
		List<Account> accounts1=accountDAO.queryIndex("1101");
		System.out.println(accounts1.get(accounts1.size()-1).getBalance());
		List<Account> accounts2=accountDAO.queryIndex("1102");
		System.out.println(accounts2.get(0).getBalance());

	}	

}
