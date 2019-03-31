package com.example.quizapp;

public class Comment {
    String comment,datec,userc,u_id;
    public Comment(){

    }

    public Comment(String comment, String datec, String userc, String u_id) {
        this.comment = comment;
        this.datec = datec;
        this.userc = userc;
        this.u_id = u_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatec() {
        return datec;
    }

    public void setDatec(String datec) {
        this.datec = datec;
    }

    public String getUserc() {
        return userc;
    }

    public void setUserc(String userc) {
        this.userc = userc;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
}
