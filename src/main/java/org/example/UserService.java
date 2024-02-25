package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entity.Post;
import org.example.entity.Task;
import org.example.entity.User;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson JSON = new Gson();
    private static final String URI_POST = "https://jsonplaceholder.typicode.com/posts/";

    public void getAllUsers(String url) throws IOException {
        String text = Jsoup
                .connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text();

        List<User> users = new Gson().fromJson(text, new TypeToken<List<User>>(){}.getType());
        users.forEach(System.out::println);
    }

    public User createUser(String url, User user) throws IOException, InterruptedException {
        final String requestBody = JSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return JSON.fromJson(response.body(), User.class);
    }

    public User getUserById(String url, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%d", url, id)))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response.statusCode() = " + response.statusCode());
        return JSON.fromJson(response.body(), User.class);
    }

    public User getUserByUsername(String url, String userName) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?username=%s", url, userName)))
                .GET()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        Type collectionType = new TypeToken<List<User>>(){}.getType();
        List<User> users = new Gson()
                .fromJson( response.body() , collectionType);
        return users.get(0);
    }

    public void deleteUser(String url, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(String.format("%s/%d", url, id)))
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
    }

    public User updateUser(String url, User user, int id) throws IOException, InterruptedException {
        String updatedUser = JSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%d", url, id)))
                .header("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(updatedUser))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return JSON.fromJson(response.body(), User.class);
    }

    public void getPosts(String url, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%d/posts", url, id)))
                .GET()
                .build();

        HttpResponse<String> response =  CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        Type collectionType = new TypeToken<List<Post>>(){}.getType();
        ArrayList<Post> posts = new Gson()
                .fromJson( response.body() , collectionType);
        int lastPost = posts.size();
        System.out.println("lastPost = " + lastPost);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%d/comments", URI_POST, lastPost)))
                .GET()
                .build();

        CLIENT.send(request2, HttpResponse.BodyHandlers.ofFile
                (Paths.get(String.format("src/main/java/org/example/user-%d-post-%d-comments.json",id, lastPost))));
    }

    public void getOpenTasks(String uri, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%d/todos", uri, id)))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Task>>(){}.getType();
        ArrayList<Task> posts = new Gson()
                .fromJson( response.body() , type);

        posts.stream()
                .filter(task -> !task.isCompleted())
                .forEach(System.out::println);
    }

}
