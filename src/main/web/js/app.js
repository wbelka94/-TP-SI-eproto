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
        this.firstname.subscribe(this.update.bind(this));
        this.lastname.subscribe(this.update.bind(this));
        this.birthday.subscribe(this.update.bind(this));
        this.index.subscribe(this.update.bind(this));
    }

    getData(){
        return ko.toJSON({
            firstname: this.firstname,
            lastname: this.lastname,
            birthday: this.birthday
        });
    }

    update(){
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

    deleteStudent(){
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
}
var gradesSystemModel = function(){
    var self = this;

    this.students = ko.observableArray([]);
    this.studentToAdd = new Student();
    this.students.subscribe(function(change){
       console.log(change);
    });
    this.courses = ko.observableArray([]);
    this.grades = ko.observableArray([]);

    getStudents();

    function getStudents() {
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

    function addStudent(){
        self.students.push(self.studentToAdd);
        self.studentToAdd = new Student();
    }

    function getCourses() {
        return JSON.parse($.ajax({
            url: apiSerwer + 'courses',
            async: false,
            headers: {
                Accept: "application/json"
            }
        }).responseText);
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