package com.example.final_project.web.dto;

import javax.persistence.Column;

public class AnswerDto {
    private Long id;
    private String value;
    private int rate;
    private boolean checked;

    public AnswerDto() {
    }

    public AnswerDto(Long id, String value, int rate, boolean checked) {
        this.id = id;
        this.value = value;
        this.rate = rate;
        this.checked = checked;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
