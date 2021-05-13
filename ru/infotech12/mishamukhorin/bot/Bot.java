package ru.infotech12.mishamukhorin.bot;

import ru.infotech12.mishamukhorin.parser.Parser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {	

	@Override
	public String getBotUsername() { return "MrLogicDevBot"; }
	
	@Override
	public String getBotToken() { return /*bot token*/; }
	
	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage()) {
			
			//записываем полученное сообщение в объект Message
			Message message = update.getMessage();
			
			//провер€ем на пустоту
			if (message != null && message.hasText()) {
				
				//все переводим в нижний регистр
				String msg = message.getText().toLowerCase();
				
				//отвечаем на определенные команды
				if (msg.startsWith("/hello")) {
					SendMsg(message, "Hello World!");
				}				
				else if (msg.startsWith("/parse")) {
					SendMsg(message, "«апускаем парсер!");
					String urlToParse = msg.substring(7).trim();
					String articlePath = null;
					if (urlToParse != null) 
					{
						articlePath = Parser.Parse(urlToParse);
					}
					SendMsg(message, "ѕарсер закончил работу! ¬от что он получил:");
					SendArticle(message, articlePath);				
					
				}
				else if (msg.startsWith("/search")) {
					SendMsg(message, "«апускаем поиск!");
					String nameToSearch = msg.substring(8).trim();
			                       	        //SendMsg(message, nameToSearch);
					String articlesFound = null;
			        if (nameToSearch != null) 
			        {
			        	articlesFound = Parser.Search(nameToSearch);
			        } 
			        SendMsg(message, "ѕоиск завершен! ¬от что нашлось:");
			        if (articlesFound != null) {
			        	SendMsg(message, articlesFound);
					}					
			        
				}
				else if (msg.startsWith("/dlh")) {
					//возвращаем текст статьи с диска
					String articlePath = "DB/habr/" + msg.substring(4).trim() + ".txt";
					SendArticle(message, articlePath);	
				}
				//отвечаем на все остальные сообщени€ тем же сообщением (уже не в нижнем регистре)
				else {
					SendMsg(message, message.getText());					
				}
			}	
		}
	
	}

	private void SendArticle(Message message, String articlePath) {
		String articleFileText = Parser.GetArticleFromFile(articlePath);
		for (int i = 0; i <= Math.ceil(articleFileText.length() / 4000); i = i + 1 )
		{ 
			if (4000 * (i + 1) >= articleFileText.length()) 
			{ 
				SendMsg(message, articleFileText.substring(4000 * i, articleFileText.length()) + "\n" + Integer.toString(i +  1) + " из " + Integer.toString(Math.round(articleFileText.length() / 4000) + 1) ); 
			}
			else
			{
				SendMsg(message, articleFileText.substring(4000 * i, 4000 * (i + 1)) + "\n" + Integer.toString(i +  1) + " из " + Integer.toString(Math.round(articleFileText.length() / 4000) + 1));
			}
		};
	}
	
	
	//отправка строки в сообщении
	public void SendMsg(Message message, String s) {
		SendMessage sendMessage = new SendMessage();
		
		//куда и что отправить
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText(s);
		
		//попытка отправить
		try {
			execute(sendMessage);
		}
		catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
		
}