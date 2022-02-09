package com.dinoTravel.users;

public class UserRequest {

  public String first_name;
  public String last_name;
  public String email;
  public String dob;

  //make sure that none of these params are null
  public UserRequest(String first_name, String last_name, String email, String dob) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.email = email;
    this.dob = dob;
  }

  public UserRequest(User user){
    this.first_name = user.first_name;
    this.last_name = user.last_name;
    this.email = user.email;
    this.dob = user.dob;
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
