package com.georgen.hrbot.model.birthdays;

import com.georgen.hrbot.model.constants.Placeholder;

public class Person {
    private String name;
    private String birthday;
    private String nickname;

    public Person(String name, String birthday, String nickname) {
        this.name = name;
        this.birthday = birthday;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isEqualBirthday(Person person){
        if (this.birthday == null || person.getBirthday() == null) return false;
        return this.birthday.equals(person.getBirthday());
    }

    public String adaptGreetings(String greetingsText){
        return greetingsText.replace(Placeholder.NAME, this.name);
    }
}
