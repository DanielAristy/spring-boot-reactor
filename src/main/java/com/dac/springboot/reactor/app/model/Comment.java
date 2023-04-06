package com.dac.springboot.reactor.app.model;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private List<String> comments;

    public Comment() {
        this.comments = new ArrayList<>();
    }

    public void addComments(String comment) {
        this.comments.add(comment);
    }

    @Override
    public String toString() {
        return "Comments=" + comments;
    }
}
