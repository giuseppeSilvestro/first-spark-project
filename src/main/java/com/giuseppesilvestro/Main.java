package com.giuseppesilvestro;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
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
    }
}