package com.dinoTravel.users;
import com.dinoTravel.users.exceptions.UserVariableIsNotValidException;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.*;

/**
 * A representation of a user that allows values
 * to be mapped to keys in a relational database
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "subjectID")
    // the subject_id will need to be set since it is not auto generated
    private String subject_id;

    @Column(name = "first_name")
    public String first_name;

    @Column(name = "last_name")
    public String last_name;

    @Column(name = "email")
    public String email;

    @Column(name = "dob")
    public String dob;

    /**
     * default constructor
     */
    public User() {
    }

    /**
     * Constructor for a User, make a user form a userRequest
     * and assign it the appropriate subject ID
     * @param SubjectID the subject ID of the user
     * @param userRequest the info from the user request
     */
    public User(String SubjectID, UserRequest userRequest){
        this.subject_id = SubjectID;
        setFirst_name(userRequest.first_name);
        setLast_name(userRequest.last_name);
        setEmail(userRequest.email);
        setDob(userRequest.dob);
    }

    /**
     * A map of all changes we can make if a request is not part of it throw an error
     * @param changes the variable to change with it value
     * @throws UserVariableIsNotValidException Thrown if the user variable is not present
     */
    public void update(Map<String, String> changes) throws UserVariableIsNotValidException{
        Map<String, Consumer<String>> mutable = Map.of(
            "first_name", this::setFirst_name,
            "last_name", this::setLast_name,
            "email", this::setEmail,
            "dob", this::setDob
        );

        for(Map.Entry<String, String> pair : changes.entrySet()){
            if (mutable.containsKey(pair.getKey())) {
                mutable.get(pair.getKey()).accept(pair.getValue());
            }else{
                throw new UserVariableIsNotValidException(pair.getKey());
            }
        }
    }

    // Getters and setters

    public String getSubject_id() {
        return subject_id;
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
