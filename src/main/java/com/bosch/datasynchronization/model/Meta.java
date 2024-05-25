package com.bosch.datasynchronization.model;

public class Meta {
    private int count;
    private int start;
    private int limit;
    private String next_link;

    // Getters and setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext_link() {
        return next_link;
    }

    public void setNext_link(String next_link) {
        this.next_link = next_link;
    }
}
