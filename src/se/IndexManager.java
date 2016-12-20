package se;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexManager {

	private static String File_Dir = ".\\text";
	private static String Index_Dir = ".\\index";
	private IndexWriter indexWriter;
	private Analyzer luceneAnalyzer;
	private IndexWriterConfig indexWriterConfig;
	private List<File> fileList;

	// ��������
	public boolean createIndex(String path) throws IOException {

		if ((fileList = getFileList(path)) == null)
			return false;

		Directory directory = FSDirectory.open(new File(Index_Dir));

		luceneAnalyzer = new ICTCLASAnalyzer();
		indexWriterConfig = new IndexWriterConfig(Version.LUCENE_44,
				luceneAnalyzer);
		indexWriter = new IndexWriter(directory, indexWriterConfig);
		// Ϊÿһ���ļ��е�doc��������
		for (File file : fileList) {
			System.out.println(file.getName());
			indexWrite(file);
		}
		if (indexWriter != null) {
			indexWriter.close();
			return true;
		} else {
			return false;
		}
	}

	// ��һ��doc������ת����һ��document
	public Document getDoc(String docContent) throws IOException {
		String s, url, publishid, subjectid, title, keywords, description, content;
		BufferedReader bf = new BufferedReader(new StringReader(docContent));
		Document doc = new Document();

		// �ֹ�����doc
		while ((s = bf.readLine()) != null) {
			if (s.equals("<doc>")) {
				url = publishid = subjectid = title = keywords = description = content = "";

				// ��url
				s = bf.readLine();
				url = s.substring(5, s.lastIndexOf('<'));
				doc.add(new StringField("url", url, Store.YES));
				s = bf.readLine();
				while (!s.substring(1, 6).equals("title")) {
					s = bf.readLine(); // <meta http-equiv...
				}

				// ��title
				title = s.substring(7, s.lastIndexOf('<'));
				doc.add(new TextField("title", title, Store.YES));
				s = bf.readLine();

				while (s.length() > 5 && s.substring(1, 5).equals("meta")) {
					// ��ȡkeywords
					if (s.substring(11, 19).equals("keywords")) {
						keywords = s.substring(s.indexOf('"') + 1,
								s.lastIndexOf('"'));
						doc.add(new TextField("keywords", keywords, Store.YES));
					}
					// ��ȡdescription
					else if (s.substring(11, 22).equals("description")) {
						description = s.substring(s.indexOf('"') + 1,
								s.lastIndexOf('"'));
						doc.add(new TextField("description", description,
								Store.YES));
					}
					// ��ȡpublishid
					else if (s.substring(12, 21).equals("publishid")) {
						publishid = s.substring(22);
						publishid = publishid.substring(
								publishid.indexOf('"') + 1,
								publishid.lastIndexOf('"'));
						doc.add(new StringField("publishid", publishid,
								Store.YES));
					}
					// ��ȡsubjectid
					else if (s.substring(12, 21).equals("subjectid")) {
						subjectid = s.substring(22);
						subjectid = subjectid.substring(
								subjectid.indexOf('"') + 1,
								subjectid.lastIndexOf('"'));
						doc.add(new StringField("subjectid", subjectid,
								Store.YES));
					}
					s = bf.readLine();
				}

				// ȥ�������е����ñ�ǩ,ʹ��������ʽ
				while (!s.equals("</doc>")) {
					Pattern p_html, p_mod;
					Matcher m_html, m_mod;
					String regEx_html = "<.*?>";
					p_html = Pattern.compile(regEx_html,
							Pattern.CASE_INSENSITIVE);
					m_html = p_html.matcher(s);
					s = m_html.replaceAll("");

					String regEx_mod = "&.*?;";
					p_mod = Pattern
							.compile(regEx_mod, Pattern.CASE_INSENSITIVE);
					m_mod = p_mod.matcher(s);
					s = m_mod.replaceAll("");

					content = content + s + "\n";
					s = bf.readLine();
				}
				doc.add(new TextField("content", content, Store.YES));
			}
		}
		bf.close();
		return doc;
	}

	// ���ļ����ݼ��л��documentList

	private void indexWrite(File file) throws IOException {

		Document doc;
		int count = 0;
		String result = "";
		BufferedReader bf = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		String s = bf.readLine();
		while (s != null) {
			result = "";
			// ��һƪDoc��ȡ���������н���
			if (s.equals("<doc>")) {
				while (!s.equals("</doc>")) {
					result = result + s + "\n";
					s = bf.readLine();
				}
				result = result + s + "\n";
				doc = getDoc(result);
				indexWriter.addDocument(doc);
				count++;
			}
			s = bf.readLine();
		}
		bf.close();
		System.out.println(count);
	}

	// ��ȡ�ļ��б�
	private List<File> getFileList(String path) {

		File[] filenames = new File(path).listFiles();
		if (filenames == null) {
			System.out.println("Cannot find data files!");
			return null;
		}

		List<File> fileList = new ArrayList<File>();

		for (File filename : filenames) {
			// ��txt�ļ�����fileList
			if (filename.getName().lastIndexOf(".txt") > 0) {
				fileList.add(filename);
			}
		}
		return fileList;

	}

	// ��������
	public static void main(String[] args) throws IOException {
		IndexManager se = new IndexManager();
		if (!se.createIndex(File_Dir))
			System.out.println("Cannot creat index!");
	}

}
