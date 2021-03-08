package com.example.final_project.web.dto;

import com.example.final_project.model.User;

import javax.persistence.Column;
import java.util.List;

public class AnswerDto {
    private float statUnknown;
    private Long id;
    private String value;
    private float rate;
    private boolean checked;
    private float statMale;
    private float statFemale;
    private float statNonbinary;
    private List<String> answeredUsersEmailList;

    public AnswerDto(Long id, String value, float rate, boolean checked) {
        this.id = id;
        this.value = value;
        this.rate = rate;
        this.checked = checked;
    }

    public AnswerDto(Long id, String value, float rate, List<String> answeredUserList) {
        this.id = id;
        this.value = value;
        this.rate = rate;
        this.answeredUsersEmailList = answeredUserList;
    }

    public AnswerDto(Long id, String value, List<String> answeredUserList) {
        this.id = id;
        this.value = value;
        this.answeredUsersEmailList = answeredUserList;
    }

    public AnswerDto(Long id, String value, float statMale, float statFemale, float statNonbinary, float statUnknown, float rate) {
        this.id = id;
        this.value= value;
        this.statMale = statMale;
        this.statFemale = statFemale;
        this.statNonbinary = statNonbinary;
        this.statUnknown = statUnknown;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<String> getAnsweredUsersEmailList() {
        return answeredUsersEmailList;
    }

    public void setAnsweredUsersEmailList(List<String> answeredUsersEmailList) {
        this.answeredUsersEmailList = answeredUsersEmailList;
    }

    public float getStatMale() {
        return statMale;
    }

    public void setStatMale(float statMale) {
        this.statMale = statMale;
    }

    public float getStatFemale() {
        return statFemale;
    }

    public void setStatFemale(float statFemale) {
        this.statFemale = statFemale;
    }

    public float getStatNonbinary() {
        return statNonbinary;
    }

    public void setStatNonbinary(float statNonbinary) {
        this.statNonbinary = statNonbinary;
    }

    public float getStatUnknown() {
        return statUnknown;
    }

    public void setStatUnknown(float statUnknown) {
        this.statUnknown = statUnknown;
    }
}
