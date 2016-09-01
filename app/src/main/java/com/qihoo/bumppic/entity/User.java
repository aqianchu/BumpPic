package com.qihoo.bumppic.entity;

/**
 * Created by hacker on 16/9/1.
 */
public class User {
    String id;
    String name;
    String age;
    String gender;
    String laber;
    String portait;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLaber() {
        return laber;
    }

    public void setLaber(String laber) {
        this.laber = laber;
    }

    public String getPortait() {
        return portait;
    }

    public void setPortait(String portait) {
        this.portait = portait;
    }
}
