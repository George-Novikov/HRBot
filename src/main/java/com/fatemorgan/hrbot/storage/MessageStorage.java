package com.fatemorgan.hrbot.storage;

import com.fatemorgan.hrbot.model.serializers.LongSetSerializer;
import com.fatemorgan.hrbot.model.serializers.StringSetSerializer;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.tools.datetime.Today;
import com.fatemorgan.hrbot.tools.datetime.Yesterday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageStorage.class);
    private DateParser dateParser;
    private File managementFile;
    private File todayFile;
    private File yesterdayFile;
    private Set<Long> todayMessageIDs;
    private Set<Long> yesterdayMessageIDs;
    private Date storageStateDate;

    public MessageStorage(@Qualifier("storageDateParser") DateParser dateParser,
                          @Qualifier("managementFile") File managementFile) {
        this.dateParser = dateParser;
        this.managementFile = managementFile;
        updateStorage();
    }

    public void saveReplies(Set<Long> messageIDs) {
        if (messageIDs == null || messageIDs.isEmpty()) return;

        try {
            updateStorage();
            updateMessageIDBuffer();
            todayMessageIDs.addAll(messageIDs);

            String json = LongSetSerializer.serialize(todayMessageIDs);
            FileManager.write(todayFile, json);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public boolean isAnswered(Long messageID){
        try {
            updateStorage();
            updateMessageIDBuffer();
            return yesterdayMessageIDs.contains(messageID) || todayMessageIDs.contains(messageID);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private void updateStorage() {
        try {
            if (isStorageStateUpToDate()) return;

            updateStorageFiles();
            updateStorageMemory();
            this.storageStateDate = Today.get(dateParser);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void updateStorageMemory() throws IOException {
        Set<String> inMemoryFileNames = getInMemoryFileNames();

        Set<String> outdatedFileNames = inMemoryFileNames
                .stream()
                .filter(name -> !isValidFileName(name))
                .collect(Collectors.toSet());

        inMemoryFileNames.removeAll(outdatedFileNames);
        saveCurrentFileNames(inMemoryFileNames);

        cleanUp(outdatedFileNames);
    }

    private Set<String> getInMemoryFileNames() {
        try {
            String managementJson = FileManager.read(managementFile);

            Set<String> inMemoryFileNames = StringSetSerializer.deserialize(managementJson);
            if (!isStorageInit()) updateStorageFiles();
            inMemoryFileNames.add(todayFile.getName());
            inMemoryFileNames.add(yesterdayFile.getName());

            return inMemoryFileNames;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new HashSet<>();
        }
    }

    private void saveCurrentFileNames(Set<String> fileNames) {
        try {
            String jsonFileNames = StringSetSerializer.serialize(fileNames);
            FileManager.write(managementFile, jsonFileNames);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void updateStorageFiles(){
        try {
            this.todayFile = getTodayFile();
            this.yesterdayFile = getYesterdayFile();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private boolean isStorageStateUpToDate(){
        if (storageStateDate == null || !isStorageInit() || !isMessageIDBufferInit()) return false;
        Date today = Today.get(dateParser);
        return storageStateDate.equals(today);
    }

    public File getTodayFile() throws IOException {
        File todayFile = new File(getTodayFileName());
        FileManager.createOrBypass(todayFile);
        return todayFile;
    }

    public File getYesterdayFile() throws IOException {
        File yesterdayFile = new File(getYesterdayFileName());
        FileManager.createOrBypass(yesterdayFile);
        return yesterdayFile;
    }

    private String getTodayFileName(){
        return String.format("%s.json", Today.getString(dateParser));
    }

    private String getYesterdayFileName(){
        return String.format("%s.json", Yesterday.getString(dateParser));
    }

    private void updateMessageIDBuffer() throws Exception {
        if (!isStorageInit()) updateStorage();
        loadTodayMessageIDBuffer();
        loadYesterdayMessageIDBuffer();
    }
    private void loadTodayMessageIDBuffer() throws Exception {
        String todayJson = FileManager.read(todayFile);
        this.todayMessageIDs = LongSetSerializer.deserialize(todayJson);
    }

    private void loadYesterdayMessageIDBuffer() throws Exception {
        String yesterdayJson = FileManager.read(yesterdayFile);
        this.yesterdayMessageIDs = LongSetSerializer.deserialize(yesterdayJson);
    }

    private boolean isValidFileName(String fileName){
        return fileName.equals(getTodayFileName()) || fileName.equals(getYesterdayFileName());
    }

    private boolean isStorageInit(){
        return todayFile != null && yesterdayFile != null;
    }

    private boolean isMessageIDBufferInit(){
        return todayMessageIDs != null && this.yesterdayMessageIDs != null;
    }

    private void cleanUp(Set<String> outdatedFileNames){
        outdatedFileNames
                .stream()
                .forEach(fileName -> {
                    File file = new File(fileName);
                    file.delete();
        });
    }
}
