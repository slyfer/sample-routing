# sample-routing
## Description
A sample web application that routes http request to an internal rest API usinfg spring integration.
The internal API adds paging function to an external API: https://restcountries.eu/rest/v2/all

The internal API is protected with basic auth using spring security. (user: spring/spring)

In real world we must do:

- cache external API responses
- encrypt passoword and use a DB o another service to retreive user passwords
- using rolling file appenders for logging
- add a better exception handler
- use external configuration for the application (for example external API url is hardcoded)

## Develepment tools
- maven 3.5.0
- jdk 1.8
- intellij idea 2017.3 (note: requires Lombok plugin)
