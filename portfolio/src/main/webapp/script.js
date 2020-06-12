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
function addRandomGreeting() {
  const greetings =
      ['Go for walks', 'Watch TV with my family', 'Pet my pets', 'Go on drives'];

  // Pick a random activity.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Fetches a message from the server and adds it to the DOM.
 */
function addComment() {
  console.log('Fetching a comment.');

  // The fetch() function returns a Promise because the request is asynchronous.
  const responsePromise = fetch('/data');

  // When the request is complete, pass the response into handleResponse().
  responsePromise.then(handleResponse);
}

/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
function handleResponse(response) {
  console.log('Handling the response.');

  // Old comment: response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.

    // Convert to json
    const jsonPromise = response.json()

    // When the response is converted to json, pass the result into the
    // addQuoteToDom() function.
    jsonPromise.then(addToDom)
}

/** Adds a comment to the DOM. */
function addToDom(text) {
  console.log('Adding comment to dom: ' + text);

  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerText = comment;
}
