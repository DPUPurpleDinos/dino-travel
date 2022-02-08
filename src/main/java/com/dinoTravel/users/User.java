package com.dinoTravel.users;
import lombok.Data;
import javax.persistence.*;

/**
 * A representation of a user that allows values
 * to be mapped to keys in a relational database
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "subjectID")
    // the subject_id will need to be set since it is not auto generated
    public String subject_id;

    @Column(name = "first_name")
    public String first_name;

    @Column(name = "last_name")
    public String last_name;

    @Column(name = "email")
    public String email;

    @Column(name = "dob")
    public String dob;

    /**
     * The constructor to create User objects
     * @param first_name First name of the user
     * @param last_name Last name of the user
     * @param email The user's email address
     * @param dob The date (YYYY-MM-DD) the user was born
     */
    public User(String first_name, String last_name, String email, String dob) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setEmail(email);
        setDob(dob);
    }

    /**
     * Default constructor
     */
    public User() {}


    // Getters and setters

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
