package ru.infotech12.mishamukhorin.parser;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Parser {

	public static void main(String[] args) {
		String arg = args[0];
		ParseHabr(arg);
	}
	
	private static void SavePostToFile(String articleTitle, String articleText, String filePath) {
		//записываем в файл
		try(FileWriter writer = new FileWriter(filePath, false))
        {
            writer.write(articleTitle);
            writer.write("\n\n\n");
			writer.write(articleText);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        } 
	}
	
	public static String Search(String arg) {
		int maxRes = 5;
		
		//https://habr.com/ru/search/?target_type=posts&q=     &order_by=date
		String searchLink = "https://habr.com/ru/search/?target_type=posts&q=" + arg + "&order_by=relevance";
		String articleList = "";
		//String postId = "";  
		Document document;
		Elements liList;
		ArrayList<String> results = new ArrayList<String>();
		
		//String postBodySelector = "ul:nth-child(1)"; 
		
		try 
		{
			document = Jsoup.connect(searchLink).get();			
			liList = document.select("li.content-list__item");
			for (Element list : liList) {
				if (list.hasAttr("id")) {
					results.add(list.attr("id").substring(5));
				}
	        }
			
			if (results.size() < maxRes) {
				maxRes = results.size();
			}
			if (maxRes > 0) 
			{
				for(int x = 0; x < maxRes; x = x + 1)
				{
					String tmpArticle = Parse("https://habr.com/ru/post/" + results.get(x) + "/");
					articleList = articleList  +  GetTitleFromFile(tmpArticle) + " /dlh" + results.get(x) + " \n \n";
				}
			} else { articleList = "Такого не нашлось("; }
		
		} catch (IOException e) {
			e.printStackTrace();
		};
		return articleList;
	}
	public static String Parse(String arg) {
		if (arg.contains("habr.com/")) {
			//TO DO Habr
			return ParseHabr(arg);
			
		}
		if (arg.contains("tproger.ru/")) {
			//TO DO TPR
			return ParseTPRoger(arg);
			
		}
		return "";
	}
	public static String ParseHabr(String arg) {
       	System.out.print("Парсим Хабр, URL = ");
       	System.out.println(arg);
       	//получаем ID статьи
        String postId = arg.substring(arg.lastIndexOf('/', arg.length() - 2) + 1, arg.length() - 1);
        //получаем текст самой статьи и заголовка
        String filePathStr = "DB/habr/" + postId + ".txt";
        Path filePath = Path.of(filePathStr);
        try {
        	 if (Files.notExists(filePath)) 
        	 {
        		 String postTitleSelector = "#post_" + postId + " > div.post__wrapper > h1 > span";
        	     String postBodySelector = "#post-content-body:nth-child(1)";                 //"#post-content-body";
        	     String articleTitle = "";
        	     String articleText = "";
        	     Document document;
        	     try 
        	     {
        	    	 document = Jsoup.connect(arg).get();
        	    	 int i = 1;
        	    	 while (document.select(postBodySelector).text() != "" )
        	    	 {
        	    		 if (articleText + document.select(postBodySelector).text() != "")  
        	    		 {
        	    			 articleText = articleText + document.select(postBodySelector).text();
        	    		 }
        	    		 i = i + 1;
        	    		 postBodySelector = "#post-content-body:nth-child(" + i + ")";
        	    	 }
        	    	 articleTitle = document.select(postTitleSelector).text();
        	     } catch (IOException e) {
        	    	 e.printStackTrace();
        	     }
        		SavePostToFile(articleTitle, articleText, filePathStr);
             } 
        	 return filePathStr;	
		} catch (Exception e) 
		{
			e.printStackTrace();
		} 
        return "Как бы и нету такого ничего...";
    }
	public static String ParseTPRoger(String arg) {
       	System.out.print("Парсим ТПРоджер, URL = ");
       	System.out.println(arg);
       	//получаем ID статьи
       	String postId = "";        
        //получаем текст самой статьи и заголовка
        
        String postBodySelector = "";
        String filePath = "";
        String postTitleSelector = "";
        String articleTitle = "";
    	String articleText = "";
       	Document document;
       	try {
			document = Jsoup.connect(arg).get();
			postId = document.select("article").attr("id");
			document.select("#" + postId + " > div.entry-container > div > div > a").remove();
			postTitleSelector = "#" + postId + " > div.post-title > h1";
			postBodySelector = "#" + postId + " > div.entry-container > div";
			articleTitle = document.select(postTitleSelector).text();
			articleText = document.select(postBodySelector).text();
			filePath = "DB/TPR/" + postId + ".txt";
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Распарсили тпрогер");
        
		SavePostToFile(articleTitle, articleText, filePath);
		System.out.println("Записали в файл.");
		return filePath;
		
    }
	public static String GetArticleFromFile(String filePath) { //получаем статью из файла (для телеги)
		Path fileName = Path.of(filePath);
		String result = null;
		try {
			result = new String(Files.readAllBytes(fileName));
		} catch (IOException e) 
		{
			e.printStackTrace();
		} 
       
		return result;
	}
	public static String GetTitleFromFile(String filePath) { //получаем название статьи из файла (для телеги)
		String result = null;
		BufferedReader buff;
		try {
			buff = new BufferedReader(new FileReader(filePath)); 
			result = buff.readLine();
			buff.close();

		} catch (IOException e) 
		{
			e.printStackTrace();
		} 
       
		return result;
	}
	
}