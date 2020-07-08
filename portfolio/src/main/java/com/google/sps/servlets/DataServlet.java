// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.List;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get user (which should be logged in) information .
    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    // Get the input from the form.
    String text = getParameter(request, "text-input", "");
    // String email = getParameter(request, "email-input", "");
    long timestamp = System.currentTimeMillis();
    boolean upperCase = Boolean.parseBoolean(getParameter(request, "upper-case", "false"));

    // Convert the text to upper case.
    if (upperCase) {
      text = text.toUpperCase();
    }

    // Instantiate and set the Comment object
    Comment comment = new Comment(text);

    // Make datastore and entity 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity inputEntity = new Entity("Input");
    inputEntity.setProperty("timestamp", timestamp);
    if (comment.getComment() != null) {
        // if comment is not null
        inputEntity.setProperty("text", comment.getComment());
        inputEntity.setProperty("email", email);
    }
     // Store the entity
    datastore.put(inputEntity);

    // Redirect to /index.html 
    response.setContentType("text/html;");
    response.sendRedirect("/index.html");
   
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    // Get comments from datastore from most to least recent 
    Query query = new Query("Input").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Make array list of comments in order
    List<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String email = (String) entity.getProperty("email");
      String text = (String) entity.getProperty("text");
      comments.add(email + ": " + text);
    }
    // Print comments out
    for (String comm : comments) {
        response.getWriter().println("<html><li> " + comm + "</li></html>"); 
    }
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    return request.getParameter(name) == null ? defaultValue : request.getParameter(name);
  }
}
