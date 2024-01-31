package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.birthdays.Person;
import com.fatemorgan.hrbot.model.events.Event;
import com.fatemorgan.hrbot.model.exceptions.ChatException;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessage;
import com.fatemorgan.hrbot.storage.EventsStorage;
import com.fatemorgan.hrbot.storage.MessageStorage;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessageResponse;
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
    private EventsService eventsService;
    private MessageStorage messageStorage;
    private EventsStorage eventsStorage;

    public TelegramBotService(TelegramApi api,
                              ChatService chatService,
                              BirthdaysService birthdaysService,
                              EventsService eventsService,
                              MessageStorage messageStorage,
                              EventsStorage eventsStorage) {

        this.api = api;
        this.chatService = chatService;
        this.birthdaysService = birthdaysService;
        this.eventsService = eventsService;
        this.messageStorage = messageStorage;
        this.eventsStorage = eventsStorage;
    }

    public String sendMessage(String message) throws Exception {
        return api.sendMessage(message);
    }

    public String sendMessage(String message, Long chatID) throws Exception {
        return api.sendMessage(message, chatID);
    }

    public String reply(String message, Long repliedMessageID) throws Exception {
        return api.reply(message, repliedMessageID, null);
    }

    public TelegramMessageResponse getUpdates() {
        return api.getUpdates();
    }

    public String replyUnanswered() throws Exception {
        ChatReplies chatReplies = chatService.getChatReplies();
        if (chatReplies == null || chatReplies.isEmpty()) throw new ChatException(EMPTY_CHAT_REPLIES);

        List<TelegramMessage> unansweredMessages = api.getUnansweredMessages(chatReplies);
        TelegramMessage birthdayRequest = chatReplies.extractBirthdayRequest(unansweredMessages);
        if (birthdayRequest != null) processNextBirthdays(birthdayRequest);

        return api.processUnansweredMessages(unansweredMessages, chatReplies);
    }

    public String processNextBirthdays(TelegramMessage birthdayRequest) throws Exception {
        List<Person> nextBirthdays = birthdaysService.getNextBirthdays();
        for (Person person : nextBirthdays){
            String birthdayInfo = String.format("%s | %s", person.getBirthday(), person.getName());
            api.sendMessage(birthdayInfo, birthdayRequest.getChatID());
        }
        messageStorage.saveReply(birthdayRequest.getMessageID());
        return nextBirthdays.toString();
    }

    public String processCurrentBirthdays() throws Exception {
        List<Person> currentBirthdays = birthdaysService.getCurrentBirthdays();
        if (currentBirthdays == null || currentBirthdays.isEmpty()) return new HashSet().toString();

        currentBirthdays = filterUnprocessedBirthdays(currentBirthdays);
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

    public String processTomorrowEvents() throws Exception {
        List<Event> events = eventsService.getTomorrowEvents();
        if (events == null || events.isEmpty()) return new HashSet().toString();

        events = filterUnprocessedEvents(events);
        Set<String> eventNames = eventsService.extractEventNames(events);

        for (Event event : events){
            sendMessage(event.getAnnouncementText());
        }

        eventsStorage.saveProcessedEvents(eventNames);

        return JsonMaker.serialize(events);
    }

    private List<Person> filterUnprocessedBirthdays(List<Person> people){
        return people
                .stream()
                .filter(person -> !eventsStorage.isProcessed(person.getName()))
                .collect(Collectors.toList());
    }

    private List<Event> filterUnprocessedEvents(List<Event> events){
        return events
                .stream()
                .filter(event -> !eventsStorage.isProcessed(event.getDate()))
                .collect(Collectors.toList());
    }
}
