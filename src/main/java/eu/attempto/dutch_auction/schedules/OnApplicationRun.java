package eu.attempto.dutch_auction.schedules;

import eu.attempto.dutch_auction.auth.AuthService;
import eu.attempto.dutch_auction.auth.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnApplicationRun implements ApplicationRunner {
  private final SchedulesService schedulesService;
  private final AuthService authService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    try {
      authService.registration(
          RegistrationDto.builder()
              .email("test@mail.com")
              .name("Vova")
              .password("Administrator")
              .build(),
          true);
      System.out.println("Created default admin");
    } catch (Exception e) {
      System.out.println("Default admin already exists");
    }

    schedulesService.onApplicationStart();
  }
}
