package com.fatemorgan.hrbot.model;

import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.settings.Settings;

public class GlobalDataContainer {
    private Settings settings;
    private BirthdaysSchedule birthdays;
    private EventsSchedule events;
    private ChatReplies chatReplies;

    public GlobalDataContainer() {}

    public GlobalDataContainer(Settings settings,
                               BirthdaysSchedule birthdays,
                               EventsSchedule events,
                               ChatReplies chatReplies) {
        this.settings = settings;
        this.birthdays = birthdays;
        this.events = events;
        this.chatReplies = chatReplies;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
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

    public void load(Settings settings,
                     BirthdaysSchedule birthdays,
                     EventsSchedule events,
                     ChatReplies chatReplies) {
        if (settings != null) this.settings = settings;
        if (birthdays != null) this.birthdays = birthdays;
        if (events != null) this.events = events;
        if (chatReplies != null) this.chatReplies = chatReplies;
    }

    public boolean hasSettings(){
        return this.settings != null && !this.settings.isEmpty();
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
