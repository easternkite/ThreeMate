package org.techtown.ThreeMate;

import java.util.ArrayList;

public class AnswerSetGetter {
    public static ArrayList<String> answers = new ArrayList<String>();

    public static void setAnswers(String answer){
        answers.add(answer);
    }

    public static ArrayList<String> getAnswers(){
        return answers;
    }
    public static void setClear(){
        answers.clear();
    }
}