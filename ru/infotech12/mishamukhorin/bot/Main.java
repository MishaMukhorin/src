package ru.infotech12.mishamukhorin.bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) throws Exception {

		//�������� ���� (�� ���������� ������ � ��������)
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		
		//������� ��������� ����
		try {
			telegramBotsApi.registerBot(new Bot());
			System.out.println("Bot is online!");
		} 
		catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}
	
}
