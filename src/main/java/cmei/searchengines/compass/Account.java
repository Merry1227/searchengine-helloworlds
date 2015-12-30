package cmei.searchengines.compass;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

@Searchable
public class Account {
	private long aid;
	private String owner;
	private float balance;
	
	public Account(){
		
	}
	
	public Account(long aid){
		this.aid=aid;
	}
	
	public void setAid(long aid){
		this.aid=aid;
	}
	
	@SearchableId(name="aid")
	public long getAid(){
		return this.aid;
	}
	
	@SearchableProperty(index=Index.ANALYZED,store=Store.YES)
	public String getOwner(){
		return this.owner;
	}
	
	
	public void setOwner(String owner){
		this.owner=owner;
	}
	
	@SearchableProperty(index=Index.NOT_ANALYZED,store=Store.YES)
	public float getBalance(){
		return this.balance;
	}
	
	public void setBalance(float balance){
		this.balance=balance;
	}
}
