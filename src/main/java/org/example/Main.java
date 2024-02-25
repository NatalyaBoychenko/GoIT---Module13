package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entity.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String URL = "https://jsonplaceholder.typicode.com/users";
    private static final String URI_TODO = "https://jsonplaceholder.typicode.com/users/";

    public static void main(String[] args) throws IOException, InterruptedException {
        UserService userService = new UserService();
        User user = defaultUser();


//        userService.getAllUsers(URL);

//        User userPost = userService.createUser(URL, user);
//        System.out.println(userPost);

//        User userById = userService.getUserById(URL, 6);
//        System.out.println(userById);

//        User userByUserName = userService.getUserByUsername(URL, "Maxime_Nienow");
//        System.out.println(userByUserName);

//        userService.deleteUser(URL, 3);

//        User updatedUser = userService.updateUser(URL, user, 1);
//        System.out.println(updatedUser);

//        userService.getPosts(URL, 6);

        userService.getOpenTasks(URI_TODO, 1);
    }

    private static User defaultUser() {
        Geo geo = new Geo("-54.3254", "45.1254");
        Address address = new Address("Meredian Str.", "App. 568",
                "Springfield", "457-526", geo);
        Company company = new Company("Springfield AG", "Main factory", "");

        User user = User.builder()
                .name("John Doe")
                .username("j-doe")
                .email("j-doe@mail.com")
                .company(company)
                .address(address)
                .build();
        return user;
    }
}