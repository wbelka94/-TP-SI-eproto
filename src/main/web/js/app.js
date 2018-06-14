"use strict";

var apiSerwer = "http://localhost:8888/myapp/";

class Student {
    constructor(data){
        this.firstname = new ko.observable(data.firstname);
        this.lastname = new ko.observable(data.lastname);
        this.birthday = new ko.observable(data.birthday);
        this.index = new ko.observable(data.index);
    }

    addSubscribe(){
        for(var x in this){

        }
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
        'children': {
            create: function(options) {
                var student = new Student(options.data);
                return student;
            }
        }
    }

    ko.mapping.fromJS(getStudents(),mapping,this.students);

    this.students.subscribe(function(){
        console.log("sub");
    });

    this.students.push(new Student({firstname: "s", lastname: "X", birthday: "2018-02-23", index: 258}))

    // ko.utils.arrayForEach(this.students(), function (i,student) {
    //     i.subscribe(function(){console.log("sub xxx")});
    // })

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