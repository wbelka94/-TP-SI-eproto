package models;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
                            123456,
                            "Jan",
                            "Kowalski",
                            new SimpleDateFormat("dd-MM-yyyy").parse("22-02-1994")
                    )
            );

            List.add(
                    new Student(
                            654321,
                            "Anna",
                            "Nowak",
                            new SimpleDateFormat("dd-MM-yyyy").parse("12-05-1996")
                    )
            );

            List.add(
                    new Student(
                            741258,
                            "Piotr",
                            "Kowalczyk",
                            new SimpleDateFormat("dd-MM-yyyy").parse("28-07-1997")
                    )
            );
        } catch (Exception ignored) {
        }
    }

    //[GET, POST] /students

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addStudent(Student student){
        List.add(student);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        return new Gson().toJson(List);
    }

    //[GET, PUT, DELETE] /students/{index}


}