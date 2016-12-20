<%@ page language="java" contentType="text/html; charset=GB2312"
	pageEncoding="GB2312" import="se.Searcher"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>大新闻</title>
</head>
<body>
	<%!String searchText = "新浪";%>
	<%!Integer currPage = 1;%>
	<%!String newIndex = "0";%>
	<%
		request.setCharacterEncoding("GB2312");
		newIndex = request.getParameter("newindex");
		if (newIndex.equals("1"))
			searchText = request.getParameter("Index");
		else
			searchText = Searcher.searchIndex;
		if (request.getParameter("page") == null)
			out.println("页面发生错误");
		else
			currPage = Integer.parseInt(request.getParameter("page"));
	%>
	<img src=".\\news.jpg" alt="大新闻" width="300"><br>
	<form method="POST" action="searchResult.jsp?newindex=1&page=1">
		<p align="left">
			<input type="text" name="Index" size="35"
				value=<%out.print("\"" + searchText + "\"");%>> <input
				type="submit" value="Search"> <a href="searchinput.jsp"><font
				color="blue">返回首页</font></a> <br> <br>
	</form>

	<%
		if (newIndex.equals("1")) {
			if (searchText != null && searchText != "") {

				Searcher searcher = new Searcher();
				searcher.searchIndex(searchText);

				if (Searcher.hits == 10000)
					out.println("<font color=\"green\" size=\"2\">共有多于"
							+ Searcher.hits + "条搜索结果</font><br><br>");
				else
					out.println("<font color=\"green\" size=\"2\">共有"
							+ Searcher.hits + "条搜索结果</font><br><br>");
				if (Searcher.hits > 0) {
					int min = (Searcher.hits > ((currPage - 1) * 20 + 20)) ? ((currPage - 1) * 20 + 20)
							: Searcher.hits;
					for (int i = (currPage - 1) * 20; i < min; ++i) {
						out.println("<a href = \"" + Searcher.url[i]
								+ "\" target=\"_blank\">");
						for (int j = 0; j < Searcher.title[i].length(); j++) {
							if (searchText.contains(Searcher.title[i]
									.charAt(j) + ""))
								out.print("<font color=\"red\" size = \"4\">"
										+ Searcher.title[i].charAt(j)
										+ "</font>");
							else
								out.print("<font color=\"blue\" size = \"4\">"
										+ Searcher.title[i].charAt(j)
										+ "</font>");
						}
						out.println("</a><br>");
						out.println("<font color = \"green\" size = \"3\">");
						out.println("Source: " + Searcher.publishid[i]);
						out.println("</font><br><br>");
						for (int j = 0; j < Searcher.content[i].length(); j++) {
							if (searchText.contains(Searcher.content[i]
									.charAt(j) + ""))
								out.print("<font color=\"red\">"
										+ Searcher.content[i].charAt(j)
										+ "</font>");
							else
								out.print(Searcher.content[i].charAt(j));
							if (j > 180) {
								out.print("...<br>");
								break;
							}
							if (j != 0 && j % 50 == 0)
								out.print("<br>");
						}
						out.println("<br><br><br>");
					}
				} else {
					out.println("<br></font color = \"black\" size = \"5\">没有找到匹配内容<br>");
				}
			} else {
				out.println("<br></font color = \"black\" size = \"5\">请重新输入<br>");
			}
		} else {
			if (Searcher.hits == 10000)
				out.println("<font color=\"green\" size=\"2\">共有多于"
						+ Searcher.hits + "条搜索结果</font><br><br>");
			else
				out.println("<font color=\"green\" size=\"2\">共有"
						+ Searcher.hits + "条搜索结果</font><br><br>");
			if (Searcher.hits > 0) {
				int min = (Searcher.hits > ((currPage - 1) * 20 + 20)) ? ((currPage - 1) * 20 + 20)
						: Searcher.hits;
				for (int i = (currPage - 1) * 20; i < min; ++i) {
					out.println("<a href = \"" + Searcher.url[i]
							+ "\" target=\"_blank\">");
					for (int j = 0; j < Searcher.title[i].length(); j++) {
						if (Searcher.searchIndex.contains(Searcher.title[i]
								.charAt(j) + ""))
							out.print("<font color=\"red\" size = \"4\">"
									+ Searcher.title[i].charAt(j)
									+ "</font>");
						else
							out.print("<font color=\"blue\" size = \"4\">"
									+ Searcher.title[i].charAt(j)
									+ "</font>");
					}
					out.println("</a><br>");
					out.println("<font color = \"green\" size = \"3\">");
					out.println("Source: " + Searcher.publishid[i]);
					out.println("</font><br><br>");
					for (int j = 0; j < Searcher.content[i].length(); j++) {
						if (Searcher.searchIndex
								.contains(Searcher.content[i].charAt(j)
										+ ""))
							out.print("<font color=\"red\">"
									+ Searcher.content[i].charAt(j)
									+ "</font>");
						else
							out.print(Searcher.content[i].charAt(j));
						if (j > 180) {
							out.print("...<br>");
							break;
						}
						if (j != 0 && j % 50 == 0)
							out.print("<br>");
					}
					out.println("<br><br><br>");
				}
			} else {
				out.println("<br></font color = \"black\" size = \"5\">没有找到匹配内容<br>");
			}
		}
	%>
	<%
		out.println("<p align=\"center\">");
		out.println("当前是第 " + currPage + " 页<br><br>");
		out.println("<a href=\"searchResult.jsp?newindex=0&page=0\">首页</a>");
		if (currPage == 1)
			out.println("上一页");
		else
			out.println("<a href=\"searchResult.jsp?newindex=0&page="
					+ (currPage - 1) + "\">上一页</a>");
		if (20 * currPage >= Searcher.hits)
			out.println("下一页");
		else
			out.println("<a href=\"searchResult.jsp?newindex=0&page="
					+ (currPage + 1) + "\">下一页</a>");
		if (Searcher.hits % 20 == 0)
			out.println("<a href=\"searchResult.jsp?newindex=0&page="
					+ (Searcher.hits / 20) + "\">尾页</a>");
		else
			out.println("<a href=\"searchResult.jsp?newindex=0&page="
					+ (Searcher.hits / 20 + 1) + "\">尾页</a>");
		out.println("</p>");
	%>
	<form method="POST" action="searchResult.jsp?newindex=0">
		<p align="center">跳至
			<input type="text" name="page" size="5" value=0>页 <input
				type="submit" value="Go"> <br>
	</form>
</body>
</html>