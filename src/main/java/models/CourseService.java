package models;

import org.mongodb.morphia.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Iterator;

@Path("/courses")
public class CourseService {
    //[GET, POST] /courses
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Object getAll(
            @QueryParam("lecturer") String lecturer
    ) {
        try {
            Query<Course> query = MongoDB.getDatastore().createQuery(Course.class);
            if(lecturer != null){
                query.field("lecturer").containsIgnoreCase(lecturer);
            }
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
            return Response.created(URI.create("/myapp/courses/"+course.getUid())).build();
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
        Query<Student> query2 = MongoDB.getDatastore().createQuery(Student.class);
        Course course = query.get();

        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //deleting all grades for deleted course
        try {
            for(Student student : query2.asList()){
                Iterator<Grade> iterator = student.getGrades().iterator();
                while(iterator.hasNext()){
                    Grade grade = iterator.next();
                    if(grade.getCourse().getUid() == course.getUid()){
                        iterator.remove();
                    }
                }
                MongoDB.getDatastore().save(student);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        MongoDB.getDatastore().delete(query);


        return Response.status(Response.Status.OK).build();
    }
}
