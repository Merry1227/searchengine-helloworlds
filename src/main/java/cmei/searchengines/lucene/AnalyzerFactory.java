package cmei.searchengines.lucene;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
//import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * 
 * @author cmei This class return different Analyzer according to given language
 *         code.
 * 
 */
public class AnalyzerFactory {
    
    /**
     * 1={"id":1,"name":"English","code":"en_US"
     * ,"image":null,"sort_order":1,"is_deleted":0}
     * 2={"id":2,"name":"Chinese","code"
     * :"zh_CN","image":null,"sort_order":2,"is_deleted":0}
     * 3={"id":3,"name":"Spanish"
     * ,"code":"es_ES","image":null,"sort_order":3,"is_deleted":0}
     * 4={"id":4,"name"
     * :"Catalan","code":"ca_ES","image":null,"sort_order":4,"is_deleted":0}
     * 5={"id"
     * :5,"name":"French","code":"fr","image":null,"sort_order":5,"is_deleted"
     * :0}
     * 6={"id":6,"name":"Portuguese","code":"pt","image":null,"sort_order":6,
     * "is_deleted":0}
     * 7={"id":7,"name":"Russian","code":"ru","image":null,"sort_order"
     * :7,"is_deleted":0}
     * 8={"id":8,"name":"Italian","code":"it","image":null,"sort_order"
     * :8,"is_deleted":0}
     * 9={"id":9,"name":"Japanese","code":"ja","image":null,"sort_order"
     * :9,"is_deleted":0}
     * 10={"id":10,"name":"Korean","code":"ko","image":null,"sort_order"
     * :10,"is_deleted":0}
     * 11={"id":11,"name":"Latin","code":"la","image":null,"sort_order"
     * :11,"is_deleted":0}
     * 12={"id":12,"name":"Polish","code":"pl","image":null,"sort_order"
     * :12,"is_deleted":0}
     * 13={"id":13,"name":"Arabic","code":"ar","image":null,"sort_order"
     * :13,"is_deleted":0}
     */

    public static Analyzer getAnalysizer(String langCode) {
	Analyzer analyzer = new StandardAnalyzer(Constants.LUCENE_CURRENT);
	if (langCode == null||Constants.LANGUAGES_ALL.equalsIgnoreCase(langCode)) {
	    analyzer = new StandardAnalyzer(Constants.LUCENE_CURRENT);
	}
	if(langCode.equals("en_US")){
	    return new EnglishAnalyzer(Constants.LUCENE_CURRENT);
	}
	// if (langCode.equals("es_ES")||langCode.equals("ca_ES")) {
	// analyzer = new StandardAnalyzer(Constants.LUCENE_CURRENT);
	// }

	if (langCode.equals("fr")) {
	    analyzer = new FrenchAnalyzer(Constants.LUCENE_CURRENT);
	}
	if (langCode.equals("pt")) {
	    analyzer = new PortugueseAnalyzer(Constants.LUCENE_CURRENT);
	}
	if (langCode.equals("ru")) {
	    analyzer = new RussianAnalyzer(Constants.LUCENE_CURRENT);
	}
	if (langCode.equals("it")) {
	    analyzer = new ItalianAnalyzer(Constants.LUCENE_CURRENT);
	}

	// if(langCode.equals("ja")){
	// //Analyzer for Japanese that uses morphological analysis.
	// analyzer=new JapaneseAnalyzer(LuceneConstants.matchVersion);
	// }

	// if(langCode.equals("la")){
	// //No special Analyzer
	// }

//	if (langCode.equals("pl")) {
//	    // Analyzer using Morfologik library.
//	    // analyzer=new MorfologikAnalyzer(LuceneConstants.matchVersion);
//	    analyzer = new PolishAnalyzer(Constants.LUCENE_CURRENT);
//	}

	if (langCode.equals("ar")) {
	    analyzer = new ArabicAnalyzer(Constants.LUCENE_CURRENT);
	}

	// if(langCode.equals("ja")||langCode.equals("ko")){
	// //Analyzer for Chinese, Japanese, and Korean, which indexes bigrams
	// maybe need NGramPhraseQuery TODO
	// 我是/是中/中国/国人
	// analyzer=new CJKAnalyzer(matchVersion);

	// }

	// Basically,StardardAnaylyzer can work at most of languages.
	// if (langCode.equals("zh_CN") || langCode.equals("en_US") ||
	// langCode.equals("la") || langCode.equals("ja") ||
	// langCode.equals("ko")) {
	// analyzer = new StandardAnalyzer(Constants.LUCENE_CURRENT);
	// }
	
	System.out.println("For langCode:" + langCode + " Get analyzer:" + analyzer.getClass().getName());
	
	return analyzer;

    }

}
