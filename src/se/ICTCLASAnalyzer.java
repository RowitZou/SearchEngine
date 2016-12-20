package se;

import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import kevin.zhang.NLPIR;

public class ICTCLASAnalyzer extends Analyzer {

	public ICTCLASAnalyzer() throws UnsupportedEncodingException {

		String initPath = "G:\\javawork32\\SearchEngine";

		// ≥ı ºªØNLPIR
		if (NLPIR.NLPIR_Init(initPath.getBytes("GB2312"), 0) == false) {
			System.out.println("Init Fail!");
			return;
		}
	}

	@Override
	public TokenStreamComponents createComponents(String fieldname,
			Reader reader) {
		Tokenizer ictclastokenizer = new ICTCLASTokenizer(reader);
		TokenStreamComponents tokenstreamcomponents = new TokenStreamComponents(
				ictclastokenizer);
		return tokenstreamcomponents;
	}

}
