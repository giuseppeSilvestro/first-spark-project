package com.giuseppesilvestro.courses;

import com.giuseppesilvestro.courses.model.CourseIdea;
import com.giuseppesilvestro.courses.model.CourseIdeaDAO;
import com.giuseppesilvestro.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        /*
        https://github.com/perwendel/spark-template-engines/tree/master/spark-template-handlebars
        this link will show how to use the handlebars template
         */
        get("/", (request, response) -> {
            //create a new model to check and use the user's input saved as cookie
            Map<String, String> model = new HashMap<>();
            model.put("username", request.cookie("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (request, response) -> {
            Map<String, String> model = new HashMap<>();
            //extract the variable from the lambda and save it as a String. we will use this variable to
            //set up a cookie so we can go around the stateless of the http protocol
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model, "sign-in.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (request, response) -> {
            //get the title from the text field form in /ideas
            String title = request.queryParams("title");
            //TODO: csd - This username is tied to the cookie implementation
            //create a new courseIdea object from the title variable and getting the username from the cookie
            CourseIdea courseIdea = new CourseIdea(title, request.cookie("username"));
            //add the course idea to our List
            dao.add(courseIdea);
            //redirect the user to /ideas again, this time it displays also our List of courseIdea
            response.redirect("/ideas");
            return null;
        });
    }
}