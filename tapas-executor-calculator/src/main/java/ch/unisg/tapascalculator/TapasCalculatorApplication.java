package ch.unisg.tapascalculator;

import ch.unisg.tapascalculator.services.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TapasCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TapasCalculatorApplication.class, args);
    }

}
