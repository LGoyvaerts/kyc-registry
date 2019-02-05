package ch.ti8m.codecamp.kycoftrust.kycregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ch.ti8m.codecamp.kycoftrust.kycregister")
public class KycregisterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(KycregisterApplication.class, args);
    }
}
