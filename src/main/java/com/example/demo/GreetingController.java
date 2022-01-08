package com.example.demo;

import com.example.demo.Greeting;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private class user{
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String dob;

    public user(int id, String first_name, String last_name, String email, String dob) {
      this.id = id;
      this.first_name = first_name;
      this.last_name = last_name;
      this.email = email;
      this.dob = dob;
    }
    @Override
    public String toString(){
      return "user [" + this.first_name + "," + this.last_name + "]";
    }
  }

  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    String sql = "Select * from flights";
    int result = jdbcTemplate.queryForObject("Select Count(*) From users", Integer.class);
    System.out.println(result);
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }
}