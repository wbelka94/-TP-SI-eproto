package models;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlRootElement
public class Course {
    private int id;
    private String name,lecturer;
    private static int idCounter = 0;

    @InjectLinks({
            @InjectLink(resource = CourseService.class, rel = "parent"),
            @InjectLink(value="courses/${instance.id}", rel="self"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    public Course(){
        setId();
    }

    public Course(String name, String lecturer) {
        setId();
        this.name = name;
        this.lecturer = lecturer;
    }

    public int getId() {
        return id;
    }

    public void setId() {
        idCounter++;
        this.id = idCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}
