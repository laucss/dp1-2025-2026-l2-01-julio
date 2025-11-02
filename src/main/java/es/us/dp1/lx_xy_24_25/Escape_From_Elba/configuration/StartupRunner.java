package es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@Component
public class StartupRunner implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public StartupRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.ensureAllUsersHavePlayer();
        System.out.println("✅ Todos los usuarios tienen ahora un Player asociado.");
    }
}
