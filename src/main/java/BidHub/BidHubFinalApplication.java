package BidHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "BidHub")
@EnableJpaRepositories("BidHub.repository")
@EntityScan("Bidhub.entity")
public class BidHubFinalApplication {

	public static void main(String... args) {
		SpringApplication.run(BidHubFinalApplication.class, args);
	}
}