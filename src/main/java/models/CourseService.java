package models;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/courses")
public class CourseService {
    private static java.util.List<Course> List = new ArrayList<>();;

    static {
        try {
            List.add(
                    new Course(
                            1,
                            "TPSI",
                            "Kowalski"
                    )
            );

            List.add(
                    new Course(
                            2,
                            "MiASI",
                            "Nowak"
                    )
            );

            List.add(
                    new Course(
                            3,
                            "TPAL",
                            "Kowalczyk"
                    )
            );
        } catch (Exception ignored) {
        }
    }

    Course findCoursetById(int id){
        for(Course course : List){
            if(course.getId() == id){
                return course;
            }
        }
        return null;
    }

    //[GET, POST] /courses
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getAll() {
        return List;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(Course course){
        if(List.add(course)){
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //[GET, PUT, DELETE] /courses/{id}
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourse(@PathParam("id") int id) {
        return findCoursetById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("id") int id, Course course) {
        Course c = findCoursetById(id);
        List.set(List.indexOf(c), course);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") int id) {
        Course course = findCoursetById(id);
        if(List.remove(course)){
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }
}
