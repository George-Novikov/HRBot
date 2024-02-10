package com.georgen.hrbot.model.events;

public class Event {
    private String date;
    private String announcementText;

    public Event() {}
    public Event(String date, String announcementText) {
        this.date = date;
        this.announcementText = announcementText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnnouncementText() {
        return announcementText;
    }

    public void setAnnouncementText(String announcementText) {
        this.announcementText = announcementText;
    }

    public boolean isEqualDate(Event event){
        if (this.date == null || event.getDate() == null) return false;
        return this.date.equals(event.getDate());
    }
}
