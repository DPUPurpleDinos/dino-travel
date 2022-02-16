package com.dinoTravel.complaints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles exceptions that get thrown by the FlightController
 */
@ControllerAdvice

class ComplaintNotFoundAdvice {

/**
 * Generates a 404 status if a requested ID is not found and
 * returns an error message as a String
 * @param ex ComplaintNotFoundException
 * @return Error message containing the Complaint id that caused the exception
 */

  @ResponseBody
  @ExceptionHandler(ComplaintNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String ComplaintNotFoundHandler(ComplaintNotFoundException ex){
    return ex.getMessage();
  }

  /**
   *  Handles HTTP requests for Complaint objects
   */
}

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

  @Autowired
  private ComplaintRepository complaintRepository;
  private final ComplaintModelAssembler complaintAssembler;

  /**
   * Constructor to create a RestController for Complaint objects
   * @param repository Repository to save the Complaint objects
   * @param assembler Assembler to create the JSON response
   */

  ComplaintController(ComplaintRepository repository, ComplaintModelAssembler assembler){
    this.complaintRepository = repository;
    this.complaintAssembler = assembler;
  }
  /**
   * Returns all complaints saved in the ComplaintRepository
   * @return A collection of Complaints and their bodies represented as an EntityModel
   */
  @GetMapping()
  CollectionModel<EntityModel<Complaint>> GetAllComplaints(){
    List<EntityModel<Complaint>> complaints = complaintRepository.findAll().stream()
        .map(complaintAssembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(complaints);
  }
  /**
   * Return the body for a single complaint
   * @param complaintId the ID for the complaint
   * @return The body of the complaint as an EntityModel
   */
  @GetMapping("/{id}")
  EntityModel<Complaint> getComplaintById(@PathVariable ("id") int complaintId) {
    Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new ComplaintNotFoundException(complaintId));

    return complaintAssembler.toModel(complaint);
  }
  /**
   * Update an existing flight already contained in the ComplaintRepository
   * Otherwise save it to the ComplaintRepository
   * @param complaint The body of the complaint
   * @param complaintID The id for the existing complaint
   * @return The body of the updated complaint as a ResponseEntity
   */

  @PutMapping("/{id}")
  ResponseEntity<?> updateComplaint(@RequestBody Complaint complaint, @PathVariable("id") int complaintID) {
    Complaint existingComplaint = complaintRepository.findById(complaintID)
        .map(newComplaint -> {
          newComplaint.setFullName(complaint.getFullName());
          newComplaint.setEmail(complaint.getEmail());
          newComplaint.setReservation_id(complaint.getReservation_id());
          newComplaint.setComplaint(complaint.getComplaint());
          return complaintRepository.save(newComplaint);
        }).orElseGet(() -> {
          complaint.setComplaint_id(complaintID);
          return complaintRepository.save(complaint);
        });
    EntityModel<Complaint> entityModel = complaintAssembler.toModel(existingComplaint);

    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  /**
   * Create a new complaint to be added to the ComplaintRepository
   * @param complaint The body of the complaint
   * @return The body of the created complaint as a ResponseEntity
   */
  @PostMapping
  ResponseEntity<?> createComplaint(@RequestBody Complaint complaint) {
    System.out.println(complaint.fullName);
    EntityModel<Complaint> entityModel = complaintAssembler.toModel(complaintRepository.save(complaint));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  /**
   * Delete a complaint from the ComplaintRepository
   * @param complaintId The id for a complaint to be deleted
   * @return An empty body as a ResponseEntity
   */
  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteComplaint(@PathVariable ("id") int complaintId) {
    complaintRepository.findById(complaintId).orElseThrow(() -> new ComplaintNotFoundException(complaintId));

    complaintRepository.deleteById(complaintId);

    return ResponseEntity.noContent().build();
  }

}
