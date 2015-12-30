package cmei.searchengines.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import cmei.searchengines.lucene.helper.FakeDBDataFetcher;



@Service("couponSearch")
public class CouponSearcher {
	
	public static String indexRoot=new File(System.getProperty("user.home"))+File.separator+"luceneindex";
	
	public static String couponRoot=indexRoot+File.separator+"coupon";
	
	private Directory indexDir =null;

	
	/**
	 *hardcode part just for demo
	 */
	List<Long> orgs=Arrays.asList(276151195749903L,6730537575L);
	//int pageSize=100;
	String[] langCodes={"en_US","zh_CN","es_ES","ca_ES","fr","pt","ru","it","ja","ko","la","pl","ar"};
	String[] values={"description",
			 "中文",
			 "habláis",
			 "fünfzehnte",
			 "A tout à l'heure",
			 "Ele está a dormir",
			 "Миллион",
			 "Sì",
			 "にほんごニホンゴ",
			 "오영지",
			 "mecum omnes plangite",
			 "łódka Ą",
			 "نا سعيد بلقائ"	
	};

	String searchField="name";

	
	
	public CouponSearcher(){
		
		
	}

	public List<Map<String,Object>> searchName(String searchField,String searchValue,String langCode,int pageSize) throws IOException, ParseException{
		String sortField=available_to;
		boolean isDES=false;
		File f=new File(couponRoot);
		indexDir = FSDirectory.open(f);
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		System.out.println(" index file:"+f.getAbsolutePath()+" maxDoc:"+indexReader.maxDoc()+" numDoc:"+indexReader.numDocs());
		
	    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	    QueryParser parser = new QueryParser(Constants.LUCENE_CURRENT,MLAnalyzerWrapperUtil.getLuceneField(searchField,langCode),AnalyzerFactory.getAnalysizer(langCode)); 
	    parser.setAllowLeadingWildcard(true);
	    Query query = parser.parse(searchValue);  
	    TopDocs topDocs=indexSearcher.search(query,pageSize,new Sort(new SortField(sortField,
	    		SortField.Type.LONG,isDES)));
	    ScoreDoc[] scoreDocs=topDocs.scoreDocs;
	    System.out.println("hit num:"+scoreDocs.length);
	    
	    List<Map<String,Object>> coupons=new ArrayList<Map<String,Object>>();
	    for (int i = 0; i < scoreDocs.length; i++) {
	      Document hitDoc =indexSearcher.doc(scoreDocs[i].doc);
	      Map<String,Object> couponMap=new HashMap<String,Object>();
	      couponMap.put(id,Long.valueOf(hitDoc.get(id)));
	      couponMap.put(org_id,Long.valueOf(hitDoc.get(org_id)));
	      couponMap.put(available_from,Long.valueOf(hitDoc.get(available_from)));
	      couponMap.put(quantity,Integer.valueOf(hitDoc.get(quantity)));
	      couponMap.put(available_to,Long.valueOf(hitDoc.get(available_to)));
	      couponMap.put(name,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(name,langCode)));
	      couponMap.put(short_desc,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(short_desc,langCode)));
	      couponMap.put(description,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(description,langCode)));      
	      coupons.add(couponMap);
	    }
	    indexReader.close();
	    indexDir.close();
	    return coupons;
	}
	
	public List<Map<String,Object>> searchDescs(long orgID,String searchValue,String langCode,int pageSize) throws IOException, ParseException{
		String sortField=available_from;
		String[] searchFields={MLAnalyzerWrapperUtil.getLuceneField(name,langCode),MLAnalyzerWrapperUtil.getLuceneField(short_desc,langCode),MLAnalyzerWrapperUtil.getLuceneField(description,langCode)};
		boolean isDES=true;
		File f=new File(couponRoot);
		indexDir = FSDirectory.open(f);
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		System.out.println(" index file:"+f.getAbsolutePath()+" maxDoc:"+indexReader.maxDoc()+" numDoc:"+indexReader.numDocs());
		
	    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	    QueryParser parser = new MultiFieldQueryParser(Constants.LUCENE_CURRENT,searchFields,AnalyzerFactory.getAnalysizer(langCode)); 
	    parser.setAllowLeadingWildcard(true);
	    Query query = parser.parse(searchValue);  
	    TopDocs topDocs=null;
	    if(orgID==0L){
	    	topDocs=indexSearcher.search(query,pageSize,new Sort(new SortField(sortField,
		    		SortField.Type.LONG,isDES)));
	    }else{
	    	BooleanQuery booleanQuery=new BooleanQuery();
	    	//BytesRef bf=new BytesRef();
	    	//NumericUtils.longToPrefixCoded(orgID,0,bf);
	    	//TermQuery query1=new TermQuery(new Term(WebServiceConstants.EXTRAFLD_ORGID, bf));
	    	NumericRangeQuery<Long> query1=NumericRangeQuery.newLongRange(org_id, orgID, orgID, true,true);
	    	booleanQuery.add(query1,BooleanClause.Occur.MUST);
	    	//booleanQuery.add(query,BooleanClause.Occur.MUST);
	    	topDocs=indexSearcher.search(booleanQuery,pageSize,new Sort(new SortField(sortField,
		    		SortField.Type.LONG,isDES)));
	    } 
	   
	    ScoreDoc[] scoreDocs=topDocs.scoreDocs;
	    System.out.println("hit num:"+scoreDocs.length);
	    
	    List<Map<String,Object>> coupons=new ArrayList<Map<String,Object>>();
	    for (int i = 0; i < scoreDocs.length; i++) {
	      Document hitDoc =indexSearcher.doc(scoreDocs[i].doc);
	      Map<String,Object> couponMap=new HashMap<String,Object>();
	      couponMap.put(id,Long.valueOf(hitDoc.get(id)));
	      couponMap.put(org_id,Long.valueOf(hitDoc.get(org_id)));
	      couponMap.put(quantity,Integer.valueOf(hitDoc.get(quantity)));
	      couponMap.put(available_from,Long.valueOf(hitDoc.get(available_from)));
	      couponMap.put(available_to,Long.valueOf(hitDoc.get(available_to)));
	      couponMap.put(name,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(name,langCode)));
	      couponMap.put(short_desc,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(short_desc,langCode)));
	      couponMap.put(description,hitDoc.get(MLAnalyzerWrapperUtil.getLuceneField(description,langCode)));   
	      coupons.add(couponMap);
	    }
	    indexReader.close();
	    indexDir.close();
	    return coupons;
	}
	

	
	/******offer fields*****/
	public static String id="id";
	public static String org_id="org_id";
	public static String quantity="quantity";
	public static String available_from="available_from";
	public static String available_to="available_to";
	public static String name="name";
	public static String short_desc="short_desc";
	public static String description="description";
	public static String mlPrefix="ml_";
	
	
	/**************create Index
	 * @throws DataAccessException 
	 * @throws SQLException ********/
	public Collection<Map<String, Object>> getSourceData(long orgID,int eachlangSize){
	  
		Map<Long,Map<String,Object>> mlCoupons=new HashMap<Long,Map<String,Object>>();
		for(int langID=1;langID<=langCodes.length;langID++){
			StringBuilder sql=new StringBuilder();
			String searchValue=values[langID-1];
			if(langID==1){
				sql.append(
				"select distinct coupons.id,quantity,start_time,expire_time,coupons.name,coupons.short_desc ,coupons.description from coupons where 1=1 and ((coupons.name like '%") 
				.append(searchValue).append("%') or (coupons.short_desc like '%").append(searchValue).append("%') or (coupons.description like '%").append(searchValue).append("%')) and coupons.is_deleted=0 order by id limit 0,").append(eachlangSize);
			}else{
				sql.append("select distinct coupons.id as id,quantity,start_time,expire_time,coupon_descriptions.name,coupon_descriptions.short_desc,coupon_descriptions.description from coupons left join coupon_descriptions on coupons.id = coupon_descriptions.coupon_id where 1=1 and ((coupon_descriptions.name like '%")
				.append(searchValue).append("%') or (coupon_descriptions.short_desc like '%").append(searchValue).append("%') or (coupon_descriptions.description like '%").append(searchValue)
				.append("%')) and coupon_descriptions.language_id=").append(langID).append(" and coupon_descriptions.is_deleted=0 and coupons.is_deleted=0 order by id limit 0,").append(eachlangSize);	
			}
			System.out.println("ml sql:"+sql.toString());
			List<Map<String, Object>> resultMaps =FakeDBDataFetcher.getRawDataFromDB(orgID,sql.toString());
			System.out.println("langID:"+langID+" "+resultMaps.size());
			for(Map<String,Object> resultMap:resultMaps){
				Long couponid=(Long)resultMap.get("id");
				Set ids=mlCoupons.keySet();
				if(!ids.contains(couponid)){
					Map<String,Object> mlCoupon=new HashMap<String,Object>();
					mlCoupon.put(id,couponid);
					mlCoupon.put(org_id,orgID);
					mlCoupon.put(quantity,(Integer)resultMap.get(quantity));
					mlCoupon.put(available_from,String2UnixTime((String)resultMap.get("start_time")));
					mlCoupon.put(available_to,String2UnixTime((String)resultMap.get("expire_time")));
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(name,langCodes[langID-1]),resultMap.get(name));
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(short_desc,langCodes[langID-1]),resultMap.get(short_desc));
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(description,langCodes[langID-1]),resultMap.get(description));
					mlCoupons.put(couponid,mlCoupon);
				}else{
					Map<String,Object> mlCoupon=mlCoupons.get(couponid);
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(name,langCodes[langID-1]),resultMap.get(name));
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(short_desc,langCodes[langID-1]),resultMap.get(short_desc));
					mlCoupon.put(MLAnalyzerWrapperUtil.getLuceneField(description,langCodes[langID-1]),resultMap.get(description));
				}
			}		
		}
		
		return  mlCoupons.values();
	}
	
	
	public String indexOffers(int eachlangSize) throws Exception {
		IndexWriterConfig config=new IndexWriterConfig(Constants.LUCENE_CURRENT, null);
        File f=new File(couponRoot);
		try{
			indexDir = FSDirectory.open(f);  
	        IndexWriter indexWriter = new IndexWriter(indexDir,config);
			indexWriter.deleteAll();
			for(Long orgID:orgs){
	           Collection<Map<String,Object>> coupons=this.getSourceData(orgID, eachlangSize);
	        	   //adminOfferServiceEntry.searchOfferList(orgID,searchField,values[langID-1],langID,1,pageSize, false);
	           for(Map<String,Object> coupon:coupons){
	           //	System.out.println(String.valueOf(coupon.get(TBLCouponsFields.id))+":"+String.valueOf(coupon.get(TBLCouponsFields.name)));
	           	
	           	Document doc = new Document(); 
	           	for(Entry<String,Object> entry:coupon.entrySet()){
	           		String key=entry.getKey();
	           		Object value=entry.getValue();
	           		if(key.equals(id)){
	           			doc.add(new LongField(key,(Long)value,Store.YES));
	           		}
	           		if(key.equals(org_id)){
	           			doc.add(new LongField(key,(Long)value,Store.YES));
	           		}
	           		
	           		if(key.equals(quantity)){
	           			doc.add(new IntField(key,(Integer)value,Store.YES));
	           		}
	           		
	           		if(key.equals(available_from)||entry.getKey().equals(available_to)){
	           			doc.add(new LongField(key,(Long)value,Store.YES));
	           		}
	           	
	           		if(key.startsWith(mlPrefix)){
	           			if(value!=null){
	           				doc.add(new TextField(key,(String)value,Store.YES));
	           			}
	           		}
	           	}
	           	indexWriter.addDocument(doc,MLAnalyzerWrapperUtil.getAnalyzerWrapper());
	           	System.out.println("orgID:"+orgID+" size:"+coupons.size()+" have been indexed.");
	           }
       }
	  System.out.println("maxDoc:"+indexWriter.maxDoc()+" numDoc:"+indexWriter.numDocs());
       
       indexWriter.close();
       indexDir.close();
		}catch(Exception e){
			throw e;
		}
       return f.getAbsolutePath();
	}
	
	private static long String2UnixTime(String time) {
		Date date=null;
		try {
			SimpleDateFormat  format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date=format.parse(time);
		} catch (Exception e) {
				//e.printStackTrace();
				//logger.warn("time "+time+" parse exception",e);
		}
		if(date!=null){
			return date.getTime()/1000;
		}	
		return 0;
	}
}
