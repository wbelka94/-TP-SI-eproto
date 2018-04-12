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
                            1,
                            "Jan",
                            "Kowalski",
                            new SimpleDateFormat("dd-MM-yyyy").parse("22-02-1994")
                    )
            );

            List.add(
                    new Student(
                            2,
                            "Anna",
                            "Nowak",
                            new SimpleDateFormat("dd-MM-yyyy").parse("12-05-1996")
                    )
            );

            List.add(
                    new Student(
                            3,
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
    public Response updateStudent(@PathParam("index") int index) {
        Student s = findStudentByIndex(index);
        if(List.remove(s)){
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //[GET, POST] /students/{index}/grades

    @GET
    @Path("/{index}/grades")
    public List<Grade> getGradesOfStudent(@PathParam("index") int index) {
        return findStudentByIndex(index).getGrades();
    }

    @POST
    @Path("/{index}/grades")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(@PathParam("index") int index, Grade grade){
        if(findStudentByIndex(index).addGrade(grade)){
            return Response.status(Response.Status.CREATED).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}