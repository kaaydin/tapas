package ch.unisg.tapas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main TAPAS Auction House application.
 */
@SpringBootApplication
public class TapasAuctionHouseApplication {

    public static void main(String[] args) {
		new SpringApplication(TapasAuctionHouseApplication.class).run(args);
	}
}
