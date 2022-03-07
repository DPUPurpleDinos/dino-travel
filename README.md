# Purple Dino Travel

Welcome! This is the backend code for Purple Dino Travel, 
a project for CSC 394 the senior capstone course at DePaul University.

In this project we created a crud app called Purple Dino Travel.
This app makes it easy to search and book flights. 

Relevant links
- [Purple Dino Travel website](https://daniel-mccarthy.github.io/DinoTravelFrontend/)
- [Frontend Code](https://github.com/Daniel-McCarthy/DinoTravelFrontend)

## Code Explainer
This project uses the Java Spring Boot framework for our REST API.
For flight data we use the Amadeus Flight API for getting flight offer information, and airport locations data.
For user authentication we use Google's OAuth 2.0 API.

This project consists of 8 packages and a couple of other smaller classes.
To start I will detail how these packages work and why we made the decisions we made.

### Reservations
This is the package that handles reservations. 
To make a round trip reservation from LAX to JFK to reservations are made.
One from LAX to JFK another from JFK to LAX.
This solved the problem of storing oneway, round trip, and multiplicity flights in the same table.

Once a reservation is made a confirmation email is sent out.
This email contains some flight details as well as the name of the flight booked.

This package also handles updating, deleting, and getting reservation data.

Since this package deals with user data it always authenticates the user using the token verifier.
This token verifier will be explained more in the Token Verifier section.

### Flight Offers
This package handles the requests for searching any available flight offers.
For example a request of "What are the available one way flights from LAX to JFK leaving on June 11th?" is a question this package would answer.
This class also takes in the parameters as a dictionary allowing the frontend to add or omit any parameters they deem unnecessary.
The only parameter set as a default is the currency set to USD.

### Locations
This package handles the question "What airports start with 'lon'".
This is used for the flight search auto complete.
It also uses a dictionary like the flight offers class.
This class limits the requests to location type to any, page limit to 100, and the view to light.

### Flight
This package was one of the first packages we made, and currently we do not use any of its http methods.
What this package is mostly used for is the storing of flights.
Any reservation that gets made its appropriate flight is stored in the flight repository or updated.
If a flight exists its seat count is decreased by one, if it does not exist the fight gets created.

### Token Verifier
This is a static class that verifies any ID token string given to it.
It uses google's token verifier.
If the token is valid a TokenVerifierResponse is given, otherwise an error is thrown.
In reality IDTokens should not be used to authenticate users, authentication tokens should.
This is good enough for our purposes though and for future project we wont make this mistake again.

### Complaints and Reviews
These two packages are similar to each other.
They both exist to store any reviews or complaints we get from the website.
Only relevant method we need is the post method.
The get method is just there if any one want to look at the complaints or reviews we got.

### Users
This package is not used in the website. 
It was more used as a test for the authentication api.

## Resources
This folder stores all our important keys. These files are not shared on git hub.
Because of this you will not be able to run this project if you copy off of GitHub.

#### Thank you for taking a look at our project and have a good day!