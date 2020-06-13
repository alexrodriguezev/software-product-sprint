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
import java.util.List;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  Comment comment = new Comment();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String text = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();
    boolean upperCase = Boolean.parseBoolean(getParameter(request, "upper-case", "false"));
    boolean sort = Boolean.parseBoolean(getParameter(request, "sort", "false"));

    // Convert the text to upper case.
    if (upperCase) {
      text = text.toUpperCase();
    }

    // Break the text into individual words.
    String[] words = text.split("\\s*,\\s*");

    // Sort the words.
    if (sort) {
      Arrays.sort(words);
    }

    // Set the Comment object
    comment.setComment(words);

    // Make datastore and entity if comment is not null
    if (comment.getComment() != null) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity inputEntity = new Entity("Input");
        inputEntity.setProperty("timestamp", timestamp);
        inputEntity.setProperty("text", Arrays.toString(comment.getComment()));

        // Store the entity
        datastore.put(inputEntity);

        // Redirect to /index.html 
        response.setContentType("text/html;");
        response.sendRedirect("https://8080-dot-12573813-dot-devshell.appspot.com/index.html");
    }
   
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    // Get comments from datastore 
    Query query = new Query("Input").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> comments = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");

      comments.add(text);
    }

    // Print comments out
    response.getWriter().println(comments); 
  }

  /**
   * Converts an ArrayList<String> instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(HttpServletResponse response) {
    Gson gson = new Gson();
    String json = gson.toJson(response);
    return json;
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
