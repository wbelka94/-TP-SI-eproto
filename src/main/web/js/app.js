"use strict";

var apiSerwer = "http://localhost:8888/myapp/";

class Student {
    constructor(data = {firstname: "", lastname: "", birthday: ""}){
        this.firstname = new ko.observable(data.firstname);
        this.lastname = new ko.observable(data.lastname);
        this.birthday = new ko.observable(data.birthday);
        this.index = new ko.observable(data.index);
        this.addSubscribe();
    }

    addSubscribe(){
        this.firstname.subscribe(this.PUT.bind(this));
        this.lastname.subscribe(this.PUT.bind(this));
        this.birthday.subscribe(this.PUT.bind(this));
        this.index.subscribe(this.PUT.bind(this));
    }

    getData(){
        return ko.toJSON({
            firstname: this.firstname,
            lastname: this.lastname,
            birthday: this.birthday
        });
    }

    PUT(){
        console.log(this.getData());
        $.ajax({
            url: "http://localhost:8888/myapp/students/" + ko.toJS(this.index),
            method: "PUT",
            data: this.getData(),
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    DELETE(){
        $.ajax({
            url: "http://localhost:8888/myapp/students/" + ko.toJS(this.index),
            method: "DELETE",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    POST(){
        $.ajax({
            url: "http://localhost:8888/myapp/students/",
            method: "POST",
            async: false,
            data: this.getData(),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }
}
class Course {
    constructor(data = {name: "", lecturer: "", uid: ""}){
        this.name = new ko.observable(data.name);
        this.lecturer = new ko.observable(data.lecturer);
        this.uid = new ko.observable(data.uid);
        this.addSubscribe();
    }

    addSubscribe(){
        this.name.subscribe(this.PUT.bind(this));
        this.lecturer.subscribe(this.PUT.bind(this));
        this.uid.subscribe(this.PUT.bind(this));;
    }

    getData(){
        return ko.toJSON({
            name: this.name,
            lecturer: this.lecturer
        });
    }

    PUT(){
        $.ajax({
            url: "http://localhost:8888/myapp/courses/" + ko.toJS(this.uid),
            method: "PUT",
            data: this.getData(),
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    DELETE(){
        $.ajax({
            url: "http://localhost:8888/myapp/courses/" + ko.toJS(this.uid),
            method: "DELETE",
            async: false,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }

    POST(){
        $.ajax({
            url: "http://localhost:8888/myapp/courses/",
            method: "POST",
            async: false,
            data: this.getData(),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    }
}
var gradesSystemModel = function(){
    var self = this;

    this.students = ko.observableArray([]);
    this.studentToAdd = new Student();
    this.courses = ko.observableArray([]);
    this.courseToAdd = new Course();
    this.grades = ko.observableArray([]);


    this.getStudents = function() {
        var mapping = {
            create: function(options) {
                return new Student(options.data);
            }
        };
        var studnets = JSON.parse($.ajax({
            url: apiSerwer + 'students',
            async: false,
            headers: {
                Accept: "application/json",
            }
        }).responseText);
        ko.mapping.fromJS(studnets,mapping,self.students);
    }
    this.getStudents();
    this.addStudent = function(){
        self.studentToAdd.POST();
        self.getStudents();
        self.studentToAdd = new Student();
    }
    this.deleteStudent = function(student){
        student.DELETE();
        self.getStudents();
    }

    this.getCourses = function() {
        var mapping = {
            create: function(options) {
                return new Course(options.data);
            }
        };
        var courses = JSON.parse($.ajax({
            url: apiSerwer + 'courses',
            async: false,
            headers: {
                Accept: "application/json",
            }
        }).responseText);
        ko.mapping.fromJS(courses,mapping,self.courses);
    }
    this.getCourses();
    this.addCourse= function(){
        self.courseToAdd.POST();
        self.getCourses();
        self.courseToAdd = new Course();
    }
    this.deleteCourse = function(course){
        course.DELETE();
        self.getCourses();
    }


    function getGradesForStudent(index){
        return JSON.parse($.ajax({
            url: apiSerwer + 'students/'+index+'/grades',
            async: false,
            headers: {
                Accept: "application/json"
            }
        }).responseText);
    }
    self.onClickGrades = function(data) {
        var dataJS = ko.toJS(data);
        console.log(dataJS.index);
        if(typeof dataJS.index !== 'undefined'){

            ko.mapping.fromJS(getGradesForStudent(dataJS.index),{},self.grades);
        }
        window.location = "#grades-list";
    };




}
var  viewModel = new gradesSystemModel();



ko.applyBindings(viewModel);