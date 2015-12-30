package cmei.searchengines.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cmei.searchengines.compass.IAccountDAO;
import cmei.searchengines.lucene.helper.Coupon;
import cmei.searchengines.lucene.helper.FakeDBDataFetcher;

public class IndexCreationTest {
	public static Version matchVersion = Version.LUCENE_36;
	public static String indexRoot="target/index/coupon";
	public static  Analyzer analyzer = new StandardAnalyzer(matchVersion);
	
	

	public static void main(String[] args) throws Exception{
			IndexWriterConfig config=new IndexWriterConfig(matchVersion,analyzer);
			File f=new File(indexRoot);
			Directory indexDir = FSDirectory.open(f);
	        IndexWriter indexWriter = new IndexWriter(indexDir,config);
	        
	        ApplicationContext context=new ClassPathXmlApplicationContext("contexts/localdb.xml");
	        
	        List<Long> orgIds=new ArrayList<Long>();
	        orgIds.add(197394889304L);//6730537575
	        List<Coupon> coupons=FakeDBDataFetcher.getCouponsByOrgIds(orgIds,1,5);
	         
	        for(Coupon coupon:coupons){
	        	System.out.println(coupon.getCoupon_id()+" "+coupon.getName());
	        	Document doc = new Document();
	        	doc.add(new Field("id",String.valueOf(coupon.getCoupon_id()),Field.Store.YES,Field.Index.NOT_ANALYZED));
	        	doc.add(new Field("org_id",String.valueOf(coupon.getOrg_id()),Field.Store.YES,Field.Index.NOT_ANALYZED));
	        	doc.add(new Field("quantity",String.valueOf(coupon.getQuantity()),Field.Store.YES,Field.Index.NOT_ANALYZED));
	        	doc.add(new Field("start_time",String.valueOf(coupon.getStart_time()),Field.Store.YES,Field.Index.NOT_ANALYZED));
	        	doc.add(new Field("expire_time",String.valueOf(coupon.getExpire_time()),Field.Store.YES,Field.Index.NOT_ANALYZED));
	        	doc.add(new Field("name",String.valueOf(coupon.getName()),Field.Store.YES,Field.Index.ANALYZED));
	        	doc.add(new Field("short_desc",String.valueOf(coupon.getShort_desc()),Field.Store.YES,Field.Index.ANALYZED));
	        	doc.add(new Field("description",String.valueOf(coupon.getDescription()),Field.Store.YES,Field.Index.ANALYZED));
	        	indexWriter.addDocument(doc);
	        }
	        indexWriter.close();
	        indexDir.close();
	             
	}


}
