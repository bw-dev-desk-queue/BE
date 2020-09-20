# BE
Unit 4


#### BaseURL:
https://dbidwell-dev-desk-queue.herokuapp.com

#### Documentation:
https://dbidwell-dev-desk-queue.herokuapp.com/swagger-ui.html

<details>

<summary>Issues endpoint</summary>

GET /issues/issues - get all issues

GET /issues/issue/{id} - get issue by issue ID

GET /issues/username/{somePartialNameHere} - Get issues by username related to the one provided

GET /issues/userid/{id} - get issues by a specific user ID

POST /issues/userid/{id} - create a new issue under a specific user ID

</details>

<details>

<summary>Users endpoint</summary>

GET /users/users - get all users

GET /users/user/{id} - get user by ID

</details>

<details>

<summary>Answers endpoint</summary>

GET /answers/answers - get all answers

GET /answers/issueid/{id} - get all answers for a specific issue ID

GET /answers/answer/{id} - get answer by ID

</details>
