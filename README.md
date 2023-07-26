Basic information about application:
- Basic web application with all the foundational blocks one web app should have.
- Includes full authorization and authentication based on roles.
- Includes signup, login, account activation and forgot password routes with sending emails to users.
- Authentication and authorization is based on JWT token stateless model with refresh token strategy that generates new access JWT token
everytime the old one expires which in turn gives better security. 
- Database migrations are made with Liquibase.
- Backend is made with the use of Spring-boot and frontend is made with Angular. 
- Frontend also features checking user roles first and gives access to specific routes then.
- Complete handling of user authentication and permissions is firstly handled on frontend and then handled on backend.
- For additional information on API frontend has route which uses Swagger-ui to visualize information about backend API.
- Data layer is using SQL databases with Postgresql database as choice.
- Application is deployed on AWS as choice, backend-server application is containerized using Docker and pushed to AWS Elastic Beanstalk
and frontend-client application is served on AWS S3 bucket as static website.

Testing credentials for application:
- username: admin_root
- password: Admin123456

Application link: http://basic-web-app-frontend-client.s3-website.eu-central-1.amazonaws.com

Application is work in progress and many other changes may be made in the future.
The current state presents Minimum viable product (MVP) which contains all the basic blocks one web app needs to function.
