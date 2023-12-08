package com.StepLife.steplifeapp.other;

import java.io.Serializable;

public class Message implements Serializable {
    public String Date;
    public String Name;
    public String Telephone;
    public String messege;
    public Message(){}
    public Message(String Name, String Telephone, String messege, String Date){
        this.Name = Name;
        this.Telephone = Telephone;
        this.messege = messege;
        this.Date = Date;
    }
}
