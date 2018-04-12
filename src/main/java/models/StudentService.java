package models;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/students")
public class StudentService {
    private static List<Student> List = new ArrayList<>();;

    static {
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
    }

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
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAll() {
        return List;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(Student student){
        if(List.add(student)){
            return Response.status(Response.Status.CREATED).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    //[GET, PUT, DELETE] /students/{index}
    @GET
    @Path("/{index}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("index") int index) {
        return findStudentByIndex(index);
    }

    @PUT
    @Path("/{index}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("index") int index, Student student) {
        Student s = findStudentByIndex(index);
        List.set(List.indexOf(s), student);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{index}")
    public Response deleteStudent(@PathParam("index") int index) {
        Student s = findStudentByIndex(index);
        if(List.remove(s)){
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //[GET, POST] /students/{index}/grades

    @GET
    @Path("/{index}/grades")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Grade> getGradesOfStudent(@PathParam("index") int index) {
        return findStudentByIndex(index).getGrades();
    }

    @POST
    @Path("/{index}/grades")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGrade(@PathParam("index") int index, Grade grade){
        if(findStudentByIndex(index).addGrade(grade)){
            return Response.status(Response.Status.CREATED).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    //[GET, PUT, DELETE] /students/{index}/grades/{id}

    @GET
    @Path("/{index}/grades/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Grade getGrade(@PathParam("index") int index, @PathParam("id") int id) {
        return findStudentByIndex(index).findGradeById(id);
    }

    @PUT
    @Path("/{index}/grades/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGrade(@PathParam("index") int index, @PathParam("id") int id, Grade grade) {
        Student s = findStudentByIndex(index);
        try {
            s.updateGrade(id,grade);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{index}/grades/{id}")
    public Response deleteGrade(@PathParam("index") int index, @PathParam("id") int id) {
        Student s = findStudentByIndex(index);
        if(s.deleteGrade(id)){
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}