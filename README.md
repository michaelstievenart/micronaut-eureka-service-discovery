# micronaut-eureka-service-discovery
A repo to demonstrate authorization header issue when using eureka SD

See images for ease of usage with POSTMAN
Substitute {{hostname}} for localhost

##Steps
1. Start Eureka Server
2. Start UAA Application
3. Start A, B
4. make request to Application A TestController /api/testA 
(I did it from Postman and had a valid Bearer token as part of the request - see req.PNG)
5. Debug on TokenPropagationHttpClientFilter - verify token added to request from A
5. Debug on CustomFilter (located in Application B) - see that request headers missing Authorization Header
