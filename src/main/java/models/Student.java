package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@XmlRootElement
@Entity("students")
public class Student {
    @Id
    @XmlTransient
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId id;
    private int index;
    private String firstname, lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date birthday;
    @Reference
    private List<Grade> grades;

    @InjectLinks({
            @InjectLink(resource = StudentService.class, rel = "parent"),
            @InjectLink(value="students/${instance.index}", rel="self"),
            @InjectLink(value="students/${instance.index}/grades", rel="grades"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;


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

    public boolean addGrade(Grade grade) {
        return grades.add(grade);
    }

    /*public Grade findGradeById(int id) {
        for (Grade grade : grades) {
            if (grade.getId() == id) {
                return grade;
            }
        }
        return null;
    }*/

    /*public void updateGrade(int id, Grade grade) throws Exception {
        Grade g = findGradeById(id);
        if (g == null) {
            throw new Exception("Grade with id=" + id + " for student with index=" + index + " don't exist");
        }
        grades.set(grades.indexOf(g), grade);
    }*/

    /*public boolean deleteGrade(int id) {
        return grades.remove(findGradeById(id));
    }*/

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
        try {
            Query<AutoIncrement> query = MongoDB.getDatastore().createQuery(AutoIncrement.class);
            UpdateOperations<AutoIncrement> updateOperations = MongoDB.getDatastore().createUpdateOperations(AutoIncrement.class).inc("actual");
            AutoIncrement autoIncrement = MongoDB.getDatastore().findAndModify(query, updateOperations);
            System.out.println(autoIncrement.getActual());
            if (autoIncrement.equals(null)) {
                autoIncrement = new AutoIncrement(1);
                MongoDB.getDatastore().save(autoIncrement);
            }
            index = autoIncrement.getActual();
        }catch (Exception e){
            e.printStackTrace();
        }


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


    @XmlTransient
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
