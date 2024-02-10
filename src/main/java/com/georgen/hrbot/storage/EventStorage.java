package com.georgen.hrbot.storage;

import com.georgen.hrbot.model.serializers.StringSetSerializer;
import com.georgen.hrbot.model.settings.DateParser;
import com.georgen.hrbot.tools.datetime.Today;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class EventStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventStorage.class);

    private DateParser dateParser;
    private File eventsFile;
    private Set<String> eventsSet;

    public EventStorage(@Qualifier("storageDateParser") DateParser dateParser,
                        @Qualifier("eventsFile") File eventsFile) {
        this.dateParser = dateParser;
        this.eventsFile = eventsFile;
    }

    public void saveProcessedEvents(Set<String> eventNames) {
        if (eventNames == null || eventNames.isEmpty()) return;

        try {
            updateStorage();
            eventsSet.addAll(eventNames);
            eventsSet.add(Today.getString(dateParser));

            String json = StringSetSerializer.serialize(eventsSet);
            FileManager.write(eventsFile, json);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public boolean isProcessed(String eventName){
        try {
            updateStorage();
            return eventsSet.contains(eventName);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private void updateStorage(){
        try {
            updateEventsBuffer();
            if (!isEventsStorageValid()) cleanUp();
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    private void cleanUp() throws IOException {
        eventsSet = new HashSet<String>(){{ add(Today.getString(dateParser)); }};
        FileManager.write(
                eventsFile,
                StringSetSerializer.serialize(eventsSet)
        );
    }

    private boolean isEventsStorageValid() throws Exception {
        if (!isStorageInit()) updateEventsBuffer();
        String todaySting = Today.getString(dateParser);
        return eventsSet.contains(todaySting);
    }

    private void updateEventsBuffer() throws Exception {
        String eventsJson = FileManager.read(eventsFile);
        this.eventsSet = StringSetSerializer.deserialize(eventsJson);
    }

    private boolean isStorageInit(){
        return eventsFile != null && eventsSet != null;
    }
}
