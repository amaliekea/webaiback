package org.example.examai.dto;

import java.util.Map;

public class QuizQuestionDTO {
        private String question;
        private Map<String, String> answers; // answer_a, answer_b...
        private Map<String, String> correct_answers; // answer_a_correct: "false", etc.

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

        public Map<String, String> getCorrect_answers() {
            return correct_answers;
        }

        public void setCorrect_answers(Map<String, String> correct_answers) {
            this.correct_answers = correct_answers;
        }
    }

