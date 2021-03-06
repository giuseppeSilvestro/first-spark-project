package com.giuseppesilvestro.courses;

import com.giuseppesilvestro.courses.model.CourseIdea;
import com.giuseppesilvestro.courses.model.CourseIdeaDAO;
import com.giuseppesilvestro.courses.model.NotFoundException;
import com.giuseppesilvestro.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {
        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        //save the username cookie as attribute
        before((request, response) -> {
            if (request.cookie("username") != null) {
                request.attribute("username", request.cookie("username"));
            }
        });

        //stop unregistered students to access /ideas page
        before("/ideas", (request, response) -> {
            if (request.attribute("username") == null) {
                setFlashMessage(request,"Whoops, please sign in first!");
                response.redirect("/");
                halt();
            }
        });

        get("/", (request, response) -> {
            //create a new model to check and use the user's input saved as cookie
            Map<String, String> model = new HashMap<>();
            model.put("username", request.attribute("username"));
            model.put("flashMessage", captureFlashMessage(request));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (request, response) -> {
            Map<String, String> model = new HashMap<>();
            //extract the variable from the lambda and save it as a String. we will use this variable to
            //set up a cookie so we can go around the stateless of the http protocol
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            response.redirect("/");
            return null;
        });

        get("/ideas", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            model.put("flashMessage", captureFlashMessage(request));
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (request, response) -> {
            //get the title from the text field form in /ideas
            String title = request.queryParams("title");
            //create a new courseIdea object from the title variable and getting the username from the attribute
            CourseIdea courseIdea = new CourseIdea(title, request.attribute("username"));
            //add the course idea to our List
            dao.add(courseIdea);
            //redirect the user to /ideas again, this time it displays also our List of courseIdea
            response.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("idea", dao.findBySlug(request.params("slug")));
            return new ModelAndView(model, "idea.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas/:slug/vote", (request, response) -> {
            CourseIdea idea = dao.findBySlug(request.params("slug"));
            boolean added = idea.addVoter(request.attribute("username"));
            if (added) {
                setFlashMessage(request, "Thanks for your vote");
            } else {
                setFlashMessage(request, "You already voted!");
            }
            response.redirect("/ideas");
            return null;
        });

        exception(NotFoundException.class, (exception, request, response) -> {
            response.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            response.body(html);
        });
    }

    private static void setFlashMessage(Request request, String message) {
        request.session().attribute(FLASH_MESSAGE_KEY, message);
    }
    private static String getFlashMessage(Request request) {
        if (request.session(false) == null) {
            return null;
        }
        if (!request.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) request.session().attribute(FLASH_MESSAGE_KEY);
    }
    public static String captureFlashMessage(Request request) {
        String message = getFlashMessage(request);
        if (message != null) {
            request.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }

}
