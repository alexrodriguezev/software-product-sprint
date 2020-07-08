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

/**
 * Adds a random activity to the page.
 */
function addRandomActivity() {
  console.log("inside addRA");
  const activities =
      ['Go for walks', 'Watch TV with my family', 'Pet my pets', 'Go on drives'];

  // Pick a random activity.
  const act = activities[Math.floor(Math.random() * activities.length)];

  // Add it to the page.
  const actContainer = document.getElementById('activity-container');
  actContainer.innerText = act;
}

/**
 * Fetches a message from the server and adds it to the DOM.
 */
function addCommentSection() {
  // The fetch() function returns a Promise because the request is asynchronous.
  const responsePromise = fetch('/data');

  // When the request is complete, pass the response into handleResponse().
  responsePromise.then(handleResponse);

  // Fetch the login status
  const responseLoginPromise = fetch('/login');

  // When the request is complete, pass into handleStatusResponse
  responseLoginPromise.then(handleStatusResponse);
}

/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
function handleResponse(response) {
    // Convert to text
    const textPromise = response.text()

    // When the response is converted to text, pass the result into the
    // addCommentToDom() function.
    textPromise.then(addCommentToDom)
}

/** Adds a comment to the DOM. */
function addCommentToDom(text) {
  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerHTML = text;
}

/** Handle status response. */
function handleStatusResponse(response) {
    // Convert to text
    const textPromise = response.text();

    // When the response is converted to text, pass the result into the
    // addLoginToDom() function.
    textPromise.then(addLoginToDom)
}

//** Add login or form to DOM. */
function addLoginToDom(text) {
  const statusContainer = document.getElementById('status-container');
  statusContainer.innerHTML = text;
}

//**  Log user out. */
function logUserOut() {
  const statusContainer = document.getElementById('status-container');
  statusContainer.innerHTML = text;
}