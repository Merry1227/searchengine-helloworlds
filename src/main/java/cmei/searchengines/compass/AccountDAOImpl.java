package cmei.searchengines.compass;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.CompassCallback;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("accountDAO")
public class AccountDAOImpl implements IAccountDAO{
	
	@Autowired
	private CompassTemplate compassTemplate;
	
	public void setCompassTemplate(CompassTemplate compassTemplate){
		this.compassTemplate=compassTemplate;
	}
	
	public CompassTemplate getCompassTemplate(){
		return this.compassTemplate;
	}

	@Override
	public void buildIndex(final List<Account> accounts) {
		compassTemplate.execute(new CompassCallback<Object>(){
			public Object doInCompass(CompassSession session){
				  for(Account account:accounts){
		            	session.create(account);
		            }
				  return null;
			}
		});
//		CompassSession session = compassTemplate.getCompass().openSession();
	
//        try {
//            session.beginTransaction();
//            for(Account account:accounts){
//            	session.create(account);
//            }
//            session.commit();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	session.rollback();  
//        } finally {
//        	if(session!=null){
//        		session.close();
//        	}  
//        }
	}

	@Override
	public List<Account> queryIndex(String keyword) {
		CompassDetachedHits compassHits=compassTemplate.findWithDetach(String.valueOf(keyword));
		List<Account> accounts=new ArrayList<Account>(compassHits.length());
		for(int i=0;i<compassHits.getLength();i++){
			accounts.add((Account)compassHits.data(i));
		}
		return accounts;
	}

	@Override
	public void updateIndex(final Account account) {
		compassTemplate.execute(new CompassCallback<Object>(){

			@Override
			public Object doInCompass(CompassSession session)
					throws CompassException {
				session.delete(account);
				session.create(account);
				return null;
			}
		});	
	}

	@Override
	public void deleteIndex(long aid) {
		compassTemplate.delete(new Account(aid));
	}

}
