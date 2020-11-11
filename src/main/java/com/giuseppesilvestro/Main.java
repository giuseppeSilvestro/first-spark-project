package com.giuseppesilvestro;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

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
    }
}