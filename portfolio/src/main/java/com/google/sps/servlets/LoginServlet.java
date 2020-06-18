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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // If user is logged in, show comment form
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {    
      // Show form
      out.println("<p>Hi " + userService.getCurrentUser().getEmail() + "!</p>");
      out.println("<p>Type a message and hit sumbit:</p>");
      out.println("<form method=\"POST\" action=\"/data\">");
      out.println("<textarea name=\"text-input\">your text goes here</textarea>");
      out.println("<p>Spice it up:</p>");
      out.println("<input type=\"checkbox\" name=\"upper-case\" value=\"true\"> Upper case");
      out.println("<br/>");
      out.println("<br/>");

      // Logout option
      String logoutUrl = userService.createLogoutURL("/index.html");
      out.println("<button class=\"button\">Submit</button> <a href=\"" + logoutUrl + "\" class=\"button\">Logout</a>");
      out.println("</form>");
    } else {
      // If user is not logged in, show link to login
      String loginUrl = userService.createLoginURL("/index.html");
      out.println("<p>Login to submit a comment!</p>");
      out.println("<a href=\"" + loginUrl + "\" class=\"button\">Login</a>");
    }
  }
}
