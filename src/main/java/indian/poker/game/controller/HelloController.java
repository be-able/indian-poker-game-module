
package indian.poker.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RequiredArgsConstructor
@RestController
public class HelloController {

  @GetMapping
  public String get() {
    return "Hello";
  }
}
