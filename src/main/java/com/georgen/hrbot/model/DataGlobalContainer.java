package com.georgen.hrbot.model;

import com.georgen.hrbot.model.birthdays.BirthdaysSchedule;
import com.georgen.hrbot.model.chat.ChatReplies;
import com.georgen.hrbot.model.events.EventsSchedule;
import com.georgen.hrbot.model.settings.DataSettings;

public class DataGlobalContainer {
    private DataSettings dataSettings;
    private BirthdaysSchedule birthdays;
    private EventsSchedule events;
    private ChatReplies chatReplies;

    public DataGlobalContainer() {}

    public DataGlobalContainer(DataSettings dataSettings,
                               BirthdaysSchedule birthdays,
                               EventsSchedule events,
                               ChatReplies chatReplies) {
        this.dataSettings = dataSettings;
        this.birthdays = birthdays;
        this.events = events;
        this.chatReplies = chatReplies;
    }

    public DataSettings getSettings() {
        return dataSettings;
    }

    public void setSettings(DataSettings dataSettings) {
        this.dataSettings = dataSettings;
    }

    public BirthdaysSchedule getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(BirthdaysSchedule birthdays) {
        this.birthdays = birthdays;
    }

    public EventsSchedule getEvents() {
        return events;
    }

    public void setEvents(EventsSchedule events) {
        this.events = events;
    }

    public ChatReplies getChatReplies() {
        return chatReplies;
    }

    public void setChatReplies(ChatReplies chatReplies) {
        this.chatReplies = chatReplies;
    }

    public void load(DataSettings dataSettings,
                     BirthdaysSchedule birthdays,
                     EventsSchedule events,
                     ChatReplies chatReplies) {
        if (dataSettings != null) this.dataSettings = dataSettings;
        if (birthdays != null) this.birthdays = birthdays;
        if (events != null) this.events = events;
        if (chatReplies != null) this.chatReplies = chatReplies;
    }

    public boolean hasSettings(){
        return this.dataSettings != null && !this.dataSettings.isEmpty();
    }

    public boolean hasBirthdays(){
        return this.birthdays != null && !this.birthdays.isEmpty();
    }

    public boolean hasEvents(){
        return this.events != null && !this.events.isEmpty();
    }

    public boolean hasChatReplies(){
        return this.chatReplies != null && !this.chatReplies.isEmpty();
    }
}
