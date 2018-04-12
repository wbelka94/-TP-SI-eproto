package models;

import java.util.Date;

public class Grade {
    private int id;
    private float value;
    private Date date;
    private Course course;

    public Grade(int id, float value, Date date, Course course) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.course = course;
    }
}
