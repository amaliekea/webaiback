package org.example.examai.dto;

public class StudyQuestion {
    private String topic;
    private String level; // fx "10-årig", "ekspert", "mellemniveau"
    private boolean includeQuiz;

    public StudyQuestion(String topic, String level, boolean includeQuiz) {
        this.topic = topic;
        this.level = level;
        this.includeQuiz = includeQuiz;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isIncludeQuiz() {
        return includeQuiz;
    }

    public void setIncludeQuiz(boolean includeQuiz) {
        this.includeQuiz = includeQuiz;
    }

    @Override
    public String toString() {
        return "StudyQuestion{" +
                "topic='" + topic + '\'' +
                ", level='" + level + '\'' +
                ", includeQuiz=" + includeQuiz +
                '}';
    }
}
