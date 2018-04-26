package models;

import org.mongodb.morphia.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("/courses")
public class CourseService {
    private static java.util.List<Course> List = new ArrayList<>();;

    /*static {
        try {
            List.add(
                    new Course(
                            "TPSI",
                            "Kowalski"
                    )
            );

            List.add(
                    new Course(
                            "MiASI",
                            "Nowak"
                    )
            );

            List.add(
                    new Course(
                            "TPAL",
                            "Kowalczyk"
                    )
            );
        } catch (Exception ignored) {
        }
    }*/

   /* public static Course findCoursetById(int id){
        for(Course course : List){
            if(course.getId() == id){
                return course;
            }
        }
        return null;
    }*/

    //[GET, POST] /courses
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getAll() {
        try {
            Query<Course> query = MongoDB.getDatastore().createQuery(Course.class);
            return query.asList();
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response addCourse(Course course){
        MongoDB.getDatastore().save(course);
        //if(List.add(course)){
            return Response.created(URI.create("/myapp/courses/"+course.getId())).build();
        /*}
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }*/
    }

    //[GET, PUT, DELETE] /courses/{id}
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getCourse(@PathParam("id") int id) {
        Course course =  MongoDB.getDatastore().createQuery(Course.class).filter("uid",id).get();
        if(course == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return course;
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response updateCourse(@PathParam("id") int id, Course course) {
        Course c = MongoDB.getDatastore().createQuery(Course.class).filter("uid",id).get();
        c.setLecturer(course.getLecturer());
        c.setName(course.getName());
        MongoDB.getDatastore().save(c);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") int id) {
        Query<Course> query = MongoDB.getDatastore().createQuery(Course.class).filter("uid",id);
        Course course = MongoDB.getDatastore().findAndDelete(query);
        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}
