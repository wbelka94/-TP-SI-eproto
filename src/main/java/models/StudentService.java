package models;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/students")
public class StudentService {



    private static List<Student> List = new ArrayList<>();;

    /*static {
        try {
            List.add(
                    new Student(
                            "Jan",
                            "Kowalski",
                            new SimpleDateFormat("dd-MM-yyyy").parse("22-02-1994")
                    )
            );

            List.add(
                    new Student(
                            "Anna",
                            "Nowak",
                            new SimpleDateFormat("dd-MM-yyyy").parse("12-05-1996")
                    )
            );

            List.add(
                    new Student(
                            "Piotr",
                            "Kowalczyk",
                            new SimpleDateFormat("dd-MM-yyyy").parse("28-07-1997")
                    )
            );
        } catch (Exception ignored) {
        }
    }*/

    Student findStudentByIndex(int index){
        for(Student student : List){
            if(student.getIndex() == index){
                return student;
            }
        }
        return null;
    }


    //[GET, POST] /students

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getAll() {
        try {
            Query<Student> query = MongoDB.getDatastore().createQuery(Student.class);
            return query.asList();
        }catch (Exception e){
            e.printStackTrace();
        }
//        if(List != null) {
//            return new GenericEntity<List<Student>>(List){};
//        }else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response addStudent(Student student){
        try {
            MongoDB.getDatastore().save(student);
        }catch (Exception e){
            e.printStackTrace();
        }

        //if(List.add(student)){
            return Response.created(URI.create("/myapp/students/"+student.getIndex())).build();
       /* }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }*/
    }


    //[GET, PUT, DELETE] /students/{index}
    @GET
    @Path("/{index}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getStudent(@PathParam("index") int index) {
        return MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get();
        /*Student s = findStudentByIndex(index);
        if (s != null) {
            return s;
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }*/
    }

    @PUT
    @Path("/{index}")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response updateStudent(@PathParam("index") int index, Student student) {
        Student s = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get();
        if(s != null) {
            s.setBirthday(student.getBirthday());
            s.setFirstname(student.getFirstname());
            s.setLastname(student.getLastname());
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{index}")
    public Response deleteStudent(@PathParam("index") int index) {
        Query<Student> query = MongoDB.getDatastore().createQuery(Student.class).filter("index",index);
        Student student = MongoDB.getDatastore().findAndDelete(query);
        if(student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    //[GET, POST] /students/{index}/grades

    @GET
    @Path("/{index}/grades")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getGradesOfStudent(@PathParam("index") int index) {
        List<Grade> grades = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get().getGrades();
        if(grades != null){
            return new GenericEntity<List<Grade>>(grades){};
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{index}/grades")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response addGrade(@PathParam("index") int index, Grade grade){
        try {
            Student student = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get();
            if(student != null && student.addGrade(grade)){
                MongoDB.getDatastore().save(student);
                return Response.created(URI.create("/students/"+student.getIndex()+"/grades/"+grade.getId())).build();
            }
            else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    //[GET, PUT, DELETE] /students/{index}/grades/{id}

    @GET
    @Path("/{index}/grades/{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Grade grade = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get().findGradeById(id);
        if(grade != null){
            return grade;
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{index}/grades/{id}")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response updateGrade(@PathParam("index") int index, @PathParam("id") int id, Grade grade) {
        Student s = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get();
        try {
            s.updateGrade(id,grade);
            MongoDB.getDatastore().save(s);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{index}/grades/{id}")
    public Response deleteGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Student s = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get();
        if (s.deleteGrade(id)) {
            MongoDB.getDatastore().save(s);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}