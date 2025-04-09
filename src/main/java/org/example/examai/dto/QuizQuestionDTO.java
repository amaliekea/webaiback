package org.example.examai.dto;

import java.util.Map;

public class QuizQuestionDTO {
    private String question;
    private Map<String, String> answers;
    private Map<String, String> correctAnswers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public Map<String, String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Map<String, String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
