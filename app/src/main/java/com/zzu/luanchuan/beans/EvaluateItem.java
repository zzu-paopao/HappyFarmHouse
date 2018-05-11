package com.zzu.luanchuan.beans;

import android.net.Uri;

import java.util.ArrayList;

@SuppressWarnings("all")
public class EvaluateItem {
    private String head_img_url;
    private String nick_name;
    private String time_stamp;

    private int evaluate_score;
    private String evaluate_content;
    private ArrayList<String> evaluate_img_urls;
    private ArrayList<Uri> evaluate_img_uris;

    public String getHead_img_url() {
        return head_img_url;
    }

    public void setHead_img_url(String head_img_url) {
        this.head_img_url = head_img_url;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public int getEvaluate_score() {
        return evaluate_score;
    }

    public void setEvaluate_score(int evaluate_score) {
        this.evaluate_score = evaluate_score;
    }

    public String getEvaluate_content() {
        return evaluate_content;
    }

    public void setEvaluate_content(String evaluate_content) {
        this.evaluate_content = evaluate_content;
    }

    public ArrayList<String> getEvaluate_img_urls() {
        return evaluate_img_urls;
    }

    public void setEvaluate_img_urls(ArrayList<String> evaluate_img_urls) {
        this.evaluate_img_urls = evaluate_img_urls;
    }

    public ArrayList<Uri> getEvaluate_img_uris() {
        return evaluate_img_uris;
    }

    public void setEvaluate_img_uris(ArrayList<Uri> evaluate_img_uris) {
        this.evaluate_img_uris = evaluate_img_uris;
    }

}
