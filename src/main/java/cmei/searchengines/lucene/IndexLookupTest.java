package cmei.searchengines.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexLookupTest {
	
	public static void main(String[] args) throws Exception{
		File f=new File(IndexCreationTest.indexRoot);
		Directory indexDir = FSDirectory.open(f);
		IndexReader indexReader=IndexReader.open(indexDir);
		System.out.println("maxDoc:"+indexReader.maxDoc()+" numDoc:"+indexReader.numDocs());
		
	    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//	    QueryParser parser = new QueryParser(IndexCreationTest.matchVersion,"name",IndexCreationTest.analyzer); 
//	    Query query = parser.parse("1112222222200033");  
	    // Parse a simple query that searches for "text":
	    Term t=new Term("name","test");
	    Query query=new TermQuery(t);
	    TopDocs topDocs=indexSearcher.search(query,5);
	    ScoreDoc[] scoreDocs=topDocs.scoreDocs;
	    System.out.println("hit num:"+scoreDocs.length);
	    for (int i = 0; i < scoreDocs.length; i++) {
	      Document hitDoc =indexSearcher.doc(scoreDocs[i].doc);
	      System.out.println("hit doc"+i+":"+hitDoc.toString());
	    }
	    indexReader.close();
	    indexDir.close();
	    
   }

}
