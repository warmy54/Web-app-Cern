This is a skeleton of Spring Boot application which should be used as a start point to create a working one.
The goal of this task is to create simple web application which allows users to create TODOs via REST API.

Below you may find a proposition of the DB model:

![DB model](DBModel.png)

To complete the exercices please implement all missing classes and functonalites in order to be able to store and retrieve information about tasks and their categories.
Once you are ready, please send it to me (ie link to your git repository) before  our interview.


Here is the part of the README written by Frédéric:
As the project has grown into a moderatly big codebase I'd like to tell you what each class does so you don't get lost.

Aplication lauch the server.
Task_Category and Task are entity class that hold the fields and logic for their respective types.
CategoryDatabase and Task Database are the interfaces with the fututure DataBase
TaskMockDatabase and CategoryMockDatabase are mocks of the real Database to be be able to run smoothly.
Contoller is the class the defines the API interface.
Conflict Exception is a custome Exception the database may throw.