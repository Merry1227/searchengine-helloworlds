package cmei.searchengines.lucene.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeDBDataFetcher {

	public static List<Map<String, Object>>  getRawDataFromDB(Long orgID,String sql){
		Map<String,Object> data1=new HashMap<String,Object>();
		data1.put("name","abab");
		List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
		dataList.add(data1);
		return dataList;
	}
	
	public static List<Coupon> getCouponsByOrgIds(List ids,int page,int pageSize){
		List<Coupon> coupons=new ArrayList<Coupon>();
		Coupon coupon=new Coupon();
		return coupons;
	}
}
