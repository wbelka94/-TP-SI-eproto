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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) throws Exception {
        if(value >= 2 && value <= 5 && value%0.5 == 0){
            this.value = value;
            return;
        }
        throw new Exception("Incorrect grade value");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
