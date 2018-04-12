package models;

public class Course {
    private int id;
    private String name,lecturer;

    public Course(int id, String name, String lecturer) {
        this.id = id;
        this.name = name;
        this.lecturer = lecturer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
