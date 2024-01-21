package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.birthdays.Person;
import com.fatemorgan.hrbot.model.exceptions.ChatException;
import com.fatemorgan.hrbot.storage.EventsStorage;
import com.fatemorgan.hrbot.tools.TelegramApi;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.telegram.response.TelegramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fatemorgan.hrbot.model.constants.ChatMessage.EMPTY_CHAT_REPLIES;

@Service
public class TelegramBotService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotService.class);
    @Value("${telegram.url}")
    private String url;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.chat-id}")
    private Long chatID;

    private TelegramApi api;
    private ChatService chatService;
    private BirthdaysService birthdaysService;
    private EventsStorage eventsStorage;

    public TelegramBotService(TelegramApi api,
                              ChatService chatService,
                              BirthdaysService birthdaysService,
                              EventsStorage eventsStorage) {
        this.api = api;
        this.chatService = chatService;
        this.birthdaysService = birthdaysService;
        this.eventsStorage = eventsStorage;
    }

    public String sendMessage(String message) throws Exception {
        return api.sendMessage(message);
    }

    public String reply(String message, Long repliedMessageID) throws Exception {
        return api.reply(message, repliedMessageID, null);
    }

    public TelegramResponse getUpdates() {
        return api.getUpdates();
    }

    public String replyUnanswered() throws Exception {
        ChatReplies chatReplies = chatService.getChatReplies();
        if (chatReplies == null || chatReplies.isEmpty()) throw new ChatException(EMPTY_CHAT_REPLIES);
        return api.replyUnanswered(chatReplies);
    }

    public String processCurrentBirthdays() throws Exception {
        List<Person> currentBirthdays = birthdaysService.getCurrentBirthdays();
        if (currentBirthdays == null || currentBirthdays.isEmpty()) return new HashSet().toString();

        currentBirthdays = filterUnprocessed(currentBirthdays);
        Set<String> eventNames = birthdaysService.extractEventNames(currentBirthdays);

        List<String> greetings = birthdaysService.getCurrentBirthdayWishes(currentBirthdays);
        if (!greetings.isEmpty()){
            for (String greeting : greetings){
                sendMessage(greeting);
            }
        }

        eventsStorage.saveProcessedEvents(eventNames);

        return greetings.toString();
    }

    private List<Person> filterUnprocessed(List<Person> people){
        return people
                .stream()
                .filter(person -> !eventsStorage.isProcessed(person.getName()))
                .collect(Collectors.toList());
    }
}
