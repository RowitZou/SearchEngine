package se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import kevin.zhang.NLPIR;

public class ICTCLASTokenizer extends Tokenizer {
	private List<String> tokens;
	private Iterator<String> tokenIter;
	private CharTermAttribute termAtt;
	private TypeAttribute typeAtt;
	private NLPIR nlpir;

	public ICTCLASTokenizer(Reader reader) {
		super(reader);
		nlpir = new NLPIR();
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
	}

	protected List<String> tokenizerReader() {
		List<String> result = new ArrayList<String>();
		BufferedReader bf = new BufferedReader(input);
		String s, content = "";
		try {
			while ((s = bf.readLine()) != null) {
				content = content + s + "\n";
			}
			byte[] contentByte = nlpir.NLPIR_ParagraphProcess(
					content.getBytes("GB2312"), 1);
			String contentStr = new String(contentByte, "GB2312");
			String[] terms = contentStr.split("\\s+");
			for (String string : terms) {
				result.add(string);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean incrementToken() {//得到下一个词
		clearAttributes();
		if (tokenIter.hasNext()) {
			String tokenstr = tokenIter.next();
			int pos = tokenstr.lastIndexOf('/');
			if (pos != -1) {
				termAtt.append(tokenstr.substring(0, pos));
				termAtt.setLength(pos);
				typeAtt.setType(tokenstr.substring(pos, tokenstr.length()));
				return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		tokens = tokenizerReader();
		tokenIter = tokens.iterator();
	}

}
