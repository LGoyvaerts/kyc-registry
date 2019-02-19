package ch.ti8m.codecamp.kycoftrust.kycregister;

import ch.ti8m.codecamp.kycoftrust.kycregister.service.UsernameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan("ch.ti8m.codecamp.kycoftrust.kycregister")
public class KycregisterApplication extends SpringBootServletInitializer {

    private final static Logger log = LoggerFactory.getLogger(KycregisterApplication.class);

    @Autowired
    private UsernameService usernameService;


    public static void main(String[] args) {
        SpringApplication.run(KycregisterApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        log.error("OHOHOHOHOOHHHH");
        return "Shaka Registry HomePage.";
    }

    @GetMapping("/wipe")
    public String deleteAllUsernames() {
        log.info("[/wipe] All Usernames deleted.");
        usernameService.deleteAll();
        return "All Usernames deleted. Registry is now reset.";
    }
}
