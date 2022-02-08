package com.dinoTravel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

  @RequestMapping("/error")
  public String handleError(){
    return "Sorry but an error has occurred.";
  }
}
