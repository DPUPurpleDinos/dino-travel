package com.dinoTravel.complaints;

public class ComplaintNotFoundException extends RuntimeException {
      ComplaintNotFoundException(int complaint_id) {super("Could not find complaint with ID: " + complaint_id);}
}
