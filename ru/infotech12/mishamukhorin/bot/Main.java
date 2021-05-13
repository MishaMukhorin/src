package ru.infotech12.mishamukhorin.bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) throws Exception {

		//создание бота (по известному токену и названию)
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		
		//попытка запустить бота
		try {
			telegramBotsApi.registerBot(new Bot());
			System.out.println("Bot is online!");
		} 
		catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}
	
}
