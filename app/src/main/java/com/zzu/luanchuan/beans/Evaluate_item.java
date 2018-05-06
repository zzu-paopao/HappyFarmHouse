package com.zzu.luanchuan.beans;

import java.util.ArrayList;

@SuppressWarnings("all")
public class Evaluate_item {
    private int evaluate_score;
    private String evaluate_content;
    private ArrayList<String> evaluate_img_urls;

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
}
