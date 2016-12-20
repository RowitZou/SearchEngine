package se;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	private String Index_Dir = "G:\\javawork32\\SearchEngine\\index";
	public static int hits = 0;
	public static String searchIndex = null;
	public static String[] title = null, content = null, publishid = null, url = null;

	public Searcher() {
	}

	/*
	 * ²éÑ¯Ë÷Òý
	 */
	public void searchIndex(String querystr) {
		searchIndex = querystr;
		try {
			Directory directory = FSDirectory.open(new File(Index_Dir));
			IndexReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer ictclasAnalyzer = new ICTCLASAnalyzer();
			String[] fields = { "title", "description", "keywords", "content" };
			MultiFieldQueryParser mp = new MultiFieldQueryParser(
					Version.LUCENE_44, fields, ictclasAnalyzer);
			Query q = mp.parse(querystr);

			TopDocs topDocs = searcher.search(q, 10000);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			hits = scoreDocs.length;
			if (hits > 0) {
				title = new String[hits];
				content = new String[hits];
				publishid = new String[hits];
				url = new String[hits];
			}
			int i = 0;
			for (ScoreDoc scdoc : scoreDocs) {
				Document doc = searcher.doc(scdoc.doc);
				title[i] = doc.get("title");
				content[i] = doc.get("content");
				publishid[i] = doc.get("publishid");
				url[i] = doc.get("url");
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
