"use strict";

var apiSerwer = "http://localhost:8888/myapp/";

class Student {
    constructor(data){
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

    this.students = ko.observableArray([]);ko.mapping.fromJS(getStudents());
    this.courses = ko.observableArray([]);ko.mapping.fromJS(getCourses());
    self.grades = ko.observableArray([]);

    this.students.extend({ notify: 'always' });

    this.students.subscribe(function(){
        console.log("sub");
    });

    var mapping = {
            create: function(options) {
                return new Student(options.data);
            }
    };

    ko.mapping.fromJS(getStudents(),mapping,this.students);

    function getStudents() {
        return JSON.parse($.ajax({
            url: apiSerwer + 'students',
            async: false,
            headers: {
                Accept: "application/json",
            }
        }).responseText);
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