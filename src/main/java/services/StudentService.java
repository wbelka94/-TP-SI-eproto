package services;

import components.MongoDB;
import jersey.repackaged.com.google.common.collect.Lists;
import models.Grade;
import org.mongodb.morphia.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Path("/students")
public class StudentService {

    //[GET, POST] /students

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response getAll(
            @QueryParam("firstname") String firstname,
            @QueryParam("lastname") String lastname,
            @QueryParam("beforeDate") String beforeDate,
            @QueryParam("date") String date,
            @QueryParam("afterDate") String afterDate

    ) {
        try {
            Query<Student> query = MongoDB.getDatastore().createQuery(Student.class);
            if (firstname != null) {
                query.field("firstname").containsIgnoreCase(firstname);
            }
            if (lastname != null) {
                query.field("lastname").containsIgnoreCase(lastname);
            }
            if(beforeDate != null){
                query.field("birthday").lessThan(dateFromString(beforeDate));
            }
            if(date != null){
                query.field("birthday").equal(dateFromString(date));
            }
            if(afterDate != null){
                query.field("birthday").greaterThan(dateFromString(afterDate));
            }
            List<Student> studentsList = query.asList();
            if(studentsList.size() > 0) {
                GenericEntity<List<Student>> entity = new GenericEntity<List<Student>>(Lists.newArrayList(studentsList)) {};
                return Response.status(Response.Status.OK).entity(entity).build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
    public Object getGradesOfStudent(
            @PathParam("index") int index,
            @QueryParam("course") Integer course,
            @QueryParam("greaterGrade") Double graeterGrade,
            @QueryParam("lessGrade") Double lessGrade
            ) {
        List<Grade> grades = MongoDB.getDatastore().createQuery(Student.class).filter("index",index).get().getGrades();

        Iterator<Grade> iterator = grades.iterator();
        while(iterator.hasNext()){
            Grade grade = iterator.next();
            if(course != null && grade.getCourse().getUid() != course){
                iterator.remove();
                continue;
            }
            if(graeterGrade != null && grade.getValue() <= graeterGrade){
                iterator.remove();
                continue;
            }
            if(lessGrade != null && grade.getValue() >= lessGrade){
                iterator.remove();
                continue;
            }
        }

        if(grades != null && grades.size() > 0){
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

    private Date dateFromString(String date){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

}