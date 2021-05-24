package com.example.fightcovid.models;



import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {
    String Name;
    String email;
    String location;
    String uid;
    ArrayList<String> tags;
    public Users (String name,String email, String location,String uid,ArrayList<String> tags){
        this.Name=name;
        this.email=email;
        this.location=location;
        this.uid=uid;
        this.tags=tags;
    }
    public Users(){

    }
    public String getUid(){
        return this.uid;
    }
    public String getName(){
        return this.Name;
    }
    public String getLocation(){
        return this.location;
    }
    public String getEmail(){
        return this.email;
    }
    public ArrayList<String> getTags(){
        return this.tags;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
    public void setName(String name){
        this.Name=name;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setTags(ArrayList<String> tags){
        this.tags=tags;
    }

}
