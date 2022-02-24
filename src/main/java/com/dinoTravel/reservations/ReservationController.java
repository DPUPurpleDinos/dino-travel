package com.dinoTravel.reservations;

import com.dinoTravel.TokenVerifier;
import com.dinoTravel.TokenVerifierResponse;
import com.dinoTravel.flights.Flight;
import com.dinoTravel.flights.FlightRepository;
import com.dinoTravel.reservations.enums.tripType;
import com.dinoTravel.reservations.exceptions.FlightIsFull;
import com.dinoTravel.reservations.exceptions.InvalidBagAmountException;
import com.dinoTravel.reservations.exceptions.InvalidCredentials;
import com.dinoTravel.reservations.exceptions.ReservationNotFoundException;
import com.dinoTravel.reservations.exceptions.TooManyReservationsException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles ReservationNotFoundExceptions thrown by the controller
 */
@ControllerAdvice
class ReservationExceptionHandler {
    /**
     * Generate a 404 status is a requested ID is not found
     * and return an error message as a String
     * @param ex ReservationNotFoundException
     * @return Error message containing the reservation ID that caused the exception
     */
    @ResponseBody
    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reservationNotFoundHandler(ReservationNotFoundException ex) { return ex.getMessage(); }

    /**
     * Generate a 422 status if too many reservations are requested to be added
     * @param ex TooManyReservationsException
     * @return Error message notifying user about the reservation limit
     */
    @ResponseBody
    @ExceptionHandler(TooManyReservationsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String tooManyReservationsHandler(TooManyReservationsException ex) { return ex.getMessage(); }

    /**
     * Generate a 401 error if the user is unauthorized to perform an action
     * @param ex InvalidCredentials
     * @return String explaining what the user can't do
     */
    @ResponseBody
    @ExceptionHandler(InvalidCredentials.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String invalidCredentials(InvalidCredentials ex) {return ex.getMessage();}

    /**
     * Generates a 422 error if a selected flight has no seats left
     * @param ex flightIsFull
     * @return Error message notifying the user which flight is full
     */
    @ResponseBody
    @ExceptionHandler(FlightIsFull.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String flightIsFull(FlightIsFull ex) {return ex.getMessage();}

    /**
     * Generate a 400 error if the bag amount requested is too much
     * @param ex InvalidBagAmountException
     * @return String saying that the bag amount is invalid
     */
    @ResponseBody
    @ExceptionHandler(InvalidBagAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidBagAmount(InvalidBagAmountException ex) {return ex.getMessage();}
}

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final Session session;
    private final ExampleMatcher matcher;
    private final Random rand;

    /**
     * Constructor to create a RestController for Reservation objects
     * @param repository Repository to save Reservation objects
     * @param flightRepository Repository to save flights
     */
    ReservationController(ReservationRepository repository,
        FlightRepository flightRepository) {
        this.reservationRepository = repository;
        this.flightRepository = flightRepository;
        //make matcher to match any flights that are exactly the same
        //we use a matcher because it is better than making a long sql
        //query with 5 params
        //and rand
        this.matcher = ExampleMatcher.matchingAll().withIgnorePaths("flight_id", "seats_available");
        this.rand = new Random();

        //set the session for sending emails
        String host = "smtp.gmail.com";//or IP address

        //Get the session object
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        this.session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                Properties properties = new Properties();
                //open the application file from the classpath
                try(InputStream is = getClass().getResourceAsStream("/application.properties")){
                    properties.load(is);
                }catch (IOException e){
                    e.printStackTrace();
                }
                //set the email password
                String emailPassword = properties.getProperty("emailPassword");
                return new PasswordAuthentication("purpledinoair@gmail.com", emailPassword);
            }
        });
    }

    /**
     * get all the reservation ids associated with a user
     * @param auth auth string
     * @return list of all the reservations associated with a user
     */
    @GetMapping("/user")
    List<Reservation> getReservationsByUser(@RequestHeader("Authorization") String auth){
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        return reservationRepository.findBySubjectId(response.getSubject());
    }

    /**
     * get a reservation from a specified reservation id
     * @param auth auth string
     * @param reservationId the specified reservation id
     * @return return the id with the specified reservation
     */
    @GetMapping("/{id}")
    Reservation getReservationById(@RequestHeader("Authorization") String auth, @PathVariable ("id") int reservationId) {
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));
        if (!reservation.getSubject_id().equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to view the requested reservation.");
        }else {
            return reservation;
        }
    }

    //get a formatted return request
    //for now I am not going to make it

    /**
     * Given a list of users and associated flights it will make new
     * reservations and new flights is not already present also takes away
     * seat availability from booked flights
     * @param auth auth string
     * @param requestedReservations the array of requested reservations
     * @return created status if everything got made
     */
    @PostMapping
    ResponseEntity<?> createReservation(@RequestHeader("Authorization") String auth, @RequestBody ReservationRequest [] requestedReservations) {
        //verify token
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        //get bookingID off system time
        long bookingID = System.currentTimeMillis();
        Set<String> nameSet = new HashSet<>();
        //for every given reservation book it
        for (ReservationRequest request : requestedReservations){
            //for every given flight check if
            for (Flight requestedFlight: request.getFlight_request_info()){
                nameSet.add(requestedFlight.getArrival_airport());
                //get any matched flights, using the matcher. This is easier than
                //writing a query with a lot of input
                List<Flight> flightMatches = flightRepository.findAll(Example.of(requestedFlight, matcher));
                int reservationFlightID;
                if (flightMatches.isEmpty()){
                    //create the new flight
                    requestedFlight.setSeats_available(rand.nextInt(10, 50));
                    flightRepository.save(requestedFlight);
                    reservationFlightID = requestedFlight.getFlight_id();
                }else{
                    //else remove a passenger from the flight
                    Flight matchedFlight = flightMatches.get(0);
                    if (matchedFlight.getSeats_available() > 0) {
                        matchedFlight.addSeats_available(-1);
                        flightRepository.save(flightMatches.get(0));
                        reservationFlightID = matchedFlight.getFlight_id();
                    }else{
                        //throw an error if the flight is full
                        throw new FlightIsFull(matchedFlight.getDeparture_airport(), matchedFlight.getArrival_airport());
                    }
                }
                //for every flight make the appropriate reservation for the customer
                Reservation newReservation = new Reservation(request, bookingID, reservationFlightID, response.getSubject());
                reservationRepository.save(newReservation);
            }
        }
        //send the confirmation email
        String flightName = makeFlightString(nameSet);
        sendEmail(response.getPayload().getEmail(), (String) response.getPayload().get("name"), bookingID, requestedReservations[0].trip_type, flightName);

        //return created status all good!
        Map<String, Long> ret = new HashMap<>();
        ret.put("booking_id", bookingID);
        return ResponseEntity.created(
            URI.create("https://www.purpledinoapi.link:8080/api/reservations/booking/" + bookingID))
            .body(ret);
    }

    /**
     * Make a flight name from a given set of flight strings
     * @param names the set of all the names in the flight
     * @return a string that says the flight name
     */
    private String makeFlightString(Set<String> names){
        StringBuilder ret = new StringBuilder();
        for (String flight : names){
            ret.append(flight).append(" to ");
        }
        ret.setLength(ret.length() - 4);
        return ret.toString();
    }

    /**
     * Method for sending confirmation emails
     * @param toEmail the email address to send the message to
     * @param Name the name to use is the email
     * @param BookingID the bookingID to use in the subject
     */
    private void sendEmail(String toEmail, String Name, long BookingID, tripType type, String tripName){
        String from = "purpledinoair@gmail.com";
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            // Set Subject: header field
            message.setSubject("Purple Dino Travel Confirmation - " + BookingID);

            //get the html file
            //I use jsoup because it is easy to parse html files.
            //I tried using the default java formatter and a message
            //formatter, but they did not work. Jsoup was the best option to
            //make good customizable content.
            Document doc = Jsoup.parse(getClass().getResourceAsStream("/templates/EmailMessage.html"), "UTF-8", "https://daniel-mccarthy.github.io");
            //set the name values
            Element tag = doc.select("p.nameTag").first();
            tag.replaceWith(doc.createElement("p")
                .appendText("Hello " + Name + "! Your " + type + " from " + tripName + " has been booked."));
            //set the booking id values
            tag = doc.select("p.idTag").first();
            tag.replaceWith(doc.createElement("p")
                .appendText("Your booking ID is: " + BookingID));

            // Now set the actual message
            message.setContent(doc.toString(), "text/html; charset=utf-8");
            // Send message
            Transport.send(message);
        } catch (MessagingException | IOException mex) {
            mex.printStackTrace();
        }
    }


    /**
     * Given a dict of changes, change the associated reservation
     * @param auth auth string
     * @param changes the fields to change
     * @param reservationId the id of the reservation to change
     * @return the newly changed reservation
     */
    @PutMapping("/{id}")
    Reservation changeReservation(@RequestHeader("Authorization") String auth,@RequestBody Map<String, String> changes, @PathVariable("id") int reservationId){
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);

        Reservation existingReservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));

        existingReservation.update(changes);

        reservationRepository.save(existingReservation);

        return existingReservation;
    }

    //put mod bookings
    //I have not done this yet may not be needed

    /**
     * Delete a requested booking
     * @param auth auth string
     * @param bookingID the booking id to deleted
     * @return ok if every thing went well
     */
    @DeleteMapping("/booking/{id}")
    HttpStatus deleteBooking(@RequestHeader("Authorization") String auth, @PathVariable("id") long bookingID) {
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        String bookingOwner = reservationRepository.findBookingOwner(bookingID);
        if (!bookingOwner.equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to delete the requested booking.");
        }
        List<Reservation> reservationsInBooking = reservationRepository.findByBookingId(bookingID);
        for (Reservation r : reservationsInBooking){
            reservationRepository.deleteById(r.getReservation_id());
        }

        return HttpStatus.OK;
    }

    /**
     * delete a requested reservation, this is singular
     * use booking for multiple
     * @param auth auth string
     * @param reservationId the id of the reservations to be deleted
     * @return ok if everything went well
     */
    @DeleteMapping("/{id}")
    HttpStatus deleteReservation(@RequestHeader("Authorization") String auth, @PathVariable("id") int reservationId){
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));
        //Check if the user owns the requested reservation to be deleted
        if (!reservation.getSubject_id().equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to delete the requested reservation.");
        }
        reservationRepository.deleteById(reservationId);

        return HttpStatus.OK;
    }
}
