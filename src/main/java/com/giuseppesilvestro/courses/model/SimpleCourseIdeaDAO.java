package com.giuseppesilvestro.courses.model;

import java.util.ArrayList;
import java.util.List;

/*
this is a simple way to do it, we should not work in this way when dealing with
real projects as this implementation will not survive a server reboot.
That means, we do not have persistence and we will have to add all the content anew.
 */
public class SimpleCourseIdeaDAO implements CourseIdeaDAO{

    //as we will not work with a database in this project, we are going to use a List
    private List<CourseIdea> ideas;

    public SimpleCourseIdeaDAO() {
        ideas = new ArrayList<>();
    }

    @Override
    public boolean add(CourseIdea idea) {
        return ideas.add(idea);
    }

    @Override
    public List<CourseIdea> findAll() {
        return new ArrayList<>(ideas);
    }
}
