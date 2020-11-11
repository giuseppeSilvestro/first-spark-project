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
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (request, response) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", request.queryParams("username"));
            return new ModelAndView(model, "sign-in.hbs");
        }, new HandlebarsTemplateEngine());
    }
}