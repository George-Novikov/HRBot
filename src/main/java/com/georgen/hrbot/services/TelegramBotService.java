package com.georgen.hrbot.services;

import com.georgen.hrbot.model.birthdays.Person;
import com.georgen.hrbot.model.events.Event;
import com.georgen.hrbot.model.exceptions.ChatException;
import com.georgen.hrbot.model.serializers.JsonMaker;
import com.georgen.hrbot.model.telegram.response.TelegramInfoResponse;
import com.georgen.hrbot.model.telegram.response.messages.TelegramMessage;
import com.georgen.hrbot.storage.EventStorage;
import com.georgen.hrbot.storage.MessageStorage;
import com.georgen.hrbot.model.chat.ChatReplies;
import com.georgen.hrbot.model.telegram.response.messages.TelegramMessageResponse;
import com.georgen.hrbot.tools.SafeReader;
import com.georgen.hrbot.model.constants.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotService.class);
    private TelegramApi api;
    private ChatService chatService;
    private BirthdaysService birthdaysService;
    private EventsService eventsService;
    private MessageStorage messageStorage;
    private EventStorage eventStorage;

    public TelegramBotService(TelegramApi api,
                              ChatService chatService,
                              BirthdaysService birthdaysService,
                              EventsService eventsService,
                              MessageStorage messageStorage,
                              EventStorage eventStorage) {

        this.api = api;
        this.chatService = chatService;
        this.birthdaysService = birthdaysService;
        this.eventsService = eventsService;
        this.messageStorage = messageStorage;
        this.eventStorage = eventStorage;
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
        List<TelegramMessage> unansweredMessages = api.getUnansweredMessages();
        if (!SafeReader.isValid(unansweredMessages)) return "[]";

        ChatReplies chatReplies = chatService.getChatReplies();
        if (chatReplies == null || chatReplies.isEmpty()) throw new ChatException(ChatMessage.EMPTY_CHAT_REPLIES);

        TelegramMessage birthdayRequest = chatReplies.extractBirthdayRequest(unansweredMessages);
        if (birthdayRequest != null) processNextBirthdays(birthdayRequest);

        return api.processUnansweredMessages(unansweredMessages, chatReplies);
    }

    public TelegramInfoResponse getBotInfo(){
        return api.getBotInfoResponse();
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

        eventStorage.saveProcessedEvents(eventNames);

        return greetings.toString();
    }

    public String processTodayEvents() throws Exception {
        List<Event> events = eventsService.getTodayEvents();
        if (events == null || events.isEmpty()) return new HashSet().toString();

        events = filterUnprocessedEvents(events);
        Set<String> eventNames = eventsService.extractEventNames(events);

        for (Event event : events){
            sendMessage(event.getAnnouncementText());
        }

        eventStorage.saveProcessedEvents(eventNames);

        return JsonMaker.serialize(events);
    }

    private List<Person> filterUnprocessedBirthdays(List<Person> people){
        return people
                .stream()
                .filter(person -> !eventStorage.isProcessed(person.getName()))
                .collect(Collectors.toList());
    }

    private List<Event> filterUnprocessedEvents(List<Event> events){
        return events
                .stream()
                .filter(event -> !eventStorage.isProcessed(event.getDate()))
                .collect(Collectors.toList());
    }
}
