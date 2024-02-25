package org.example.entity;

import lombok.Data;

@Data
public class Task {
    private int id;
    private int userId;
    private String title;
    private boolean completed;
}
