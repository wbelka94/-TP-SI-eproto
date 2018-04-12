package models;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public void addCourse(Course course){
        List.add(course);
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
    public void updateCourse(@PathParam("id") int id, Course course) {
        Course c = findCoursetById(id);
        List.set(List.indexOf(c), course);
    }

    @DELETE
    @Path("/{id}")
    public void deleteCourse(@PathParam("id") int id) {
        Course course = findCoursetById(id);
        List.remove(course);
    }
}
