package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@XmlRootElement
public class Student {
    private int index;
    private String firstname,lastname;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date birthday;
    private List<Grade> grades;
    private static int indexCounter = 0;


    public Student() {
        setIndex();
        grades = new ArrayList<Grade>();
    }

    public Student(String firstname, String lastname, Date birthday) {
        setIndex();
        grades = new ArrayList<Grade>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }

    public boolean addGrade(Grade grade){
        return grades.add(grade);
    }

    public Grade findGradeById(int id){
        for(Grade grade: grades){
            if(grade.getId() == id){
                return grade;
            }
        }
        return null;
    }

    public void updateGrade(int id, Grade grade) throws Exception {
        Grade g = findGradeById(id);
        if(g == null){
            throw new Exception("Grade with id="+id+" for student with index="+index+" don't exist");
        }
        grades.set(grades.indexOf(g),grade);
    }

    public boolean deleteGrade(int id){
        return grades.remove(findGradeById(id));
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

    public void setIndex() {
        indexCounter++;
        this.index = indexCounter;
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
