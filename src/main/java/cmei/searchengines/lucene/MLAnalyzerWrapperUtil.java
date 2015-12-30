package cmei.searchengines.lucene;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/***
 * 
 * @author cmei
 * This util return different Analyzer Wrapper for different fields in same document.
 * It is depend on business you designed.
 *
 */
public class MLAnalyzerWrapperUtil {
	
	public static String mlPrefix="ml_";
	public static String[] langCodes={"en_US","zh_CN","es_ES","ca_ES","fr","pt","ru","it","ja","ko","la","pl","ar"};
	public static String[] mlFields={"ml_name","ml_description"};
	public static Analyzer getAnalyzerWrapper(){
		return getAnalyzerWrapper(langCodes,mlFields);	
	}
	
	public static Analyzer getAnalyzerWrapper(String[] langCodes,String mlFields[]){
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		for(String mlField:mlFields){
			for(String langCode:langCodes){
				analyzerPerField.put(getLuceneField(mlField,langCode), AnalyzerFactory.getAnalysizer(langCode));
			}
		}	
		return new PerFieldAnalyzerWrapper(new StandardAnalyzer(Constants.LUCENE_CURRENT),analyzerPerField); 
	}
	
	/**
	 * pls use the @link {@link FieldsHelper#getLuceneField(String, String)}<p>
	 * get the field name in lucence. If the fieldName passed in is a mlFields, like "name","short_desc","description", the return would be
	 * mlPrefix+fieldName+"_"+langCode; Otherwise, the fieldName would be just the fieldName
	 * @param fieldName
	 * @param langCode
	 * @return
	 */
	public static String getLuceneField(String fieldName,String langCode){
    	    	boolean isMLField =false;
            	for(String mlF:mlFields){
            	    if(mlF.equalsIgnoreCase(fieldName)){
            		isMLField=true;
            	    }
            	}
            	if(isMLField){
            	    return mlPrefix+fieldName+"_"+langCode;
            	}else{
            	    return fieldName;
            	}
	}

//	public static String getLangeCodeByLangId(Long langId){
//		if(langId<1||langId>langCodes.length){
//			return null;
//		}else{
//			return langCodes[langId.intValue()-1];
//		}
//	}
}
