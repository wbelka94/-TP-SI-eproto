package models;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


@XmlRootElement
public class Student {
    private int index;
    private String firstname,lastname;
    private Date birthday;
    private List<models.Grade> grades;


    public Student() {
    }

    public Student(int index, String firstname, String lastname, Date birthday) {
        this.index = index;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }

    public boolean addGrade(Grade grade){
        return grades.add(grade);
    }

    public int getIndex() {
        return index;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
