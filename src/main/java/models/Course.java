package models;

public class Course {
    private int id;
    private String name,lecturer;
    private static int idCounter = 0;

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
