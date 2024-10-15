package BidHub;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import BidHub.entity.*;
import BidHub.repository.*;

@Configuration
class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, BidItemRepository bidItemRepository,
			PaymentRepository paymentRepository) {

		return args -> {

			userRepository.save(new User("Nars", "Gumsuv", "admin@gmail.com", "416-736-2100", "4700 Keele St", null,
					"M3J 1P3", "Toronto", "Canada", "admin", "Admin!Pass24", Role.ROLE_ADMIN));

			userRepository.save(new User("Bilbo", "Baggins", "bilbo@gmail.com", "123-456-7890", "1 Bagshot Row", null,
					"H1H 0S0", "Hobbiton", "Shire", "bilbobaggins", "First@User01", Role.ROLE_USER));

			userRepository.save(new User("Frodo", "Baggins", "frodo@gmail.com", "123-456-7891", "2 Bagshot Row", null,
					"H1H 0S0", "Hobbiton", "Shire", "FrodoBaggins", "Second#User02", Role.ROLE_USER));

			userRepository.save(new User("Fillip", "Baggins", "fillip@gmail.com", "123-456-7892", "3 Bagshot Row", null,
					"H1H 0S0", "Hobbiton", "Shire", "fillipBaggins", "Third$User03", Role.ROLE_USER));

			userRepository.findAll().forEach(user -> log.info("Preloaded " + user));

			bidItemRepository.save(new BidItem(userRepository.findByUsername("bilbobaggins").get().getId(), "Iphone 15",
					"New Iphone 15, not opened from box", true, false, "2024-04-10T00:00:00Z", 1000, 500,
					500, 20, 10, 2, Status.IN_PROGRESS));

			bidItemRepository.save(new BidItem(userRepository.findByUsername("bilbobaggins").get().getId(), "MacBook",
					"New MacBook, not opened from box", false, true, "2024-04-10T00:00:00Z", 2500, 0,
					0, 20, 10, 2, Status.IN_PROGRESS));

			bidItemRepository.save(new BidItem(userRepository.findByUsername("bilbobaggins").get().getId(), "Xbox One",
					"New Xbox One, not opened from box", true, false, "2024-04-10T00:00:00Z", 300, 50,
					500, 20, 10, 2, Status.SOLD));

			bidItemRepository.save(new BidItem(userRepository.findByUsername("bilbobaggins").get().getId(),
					"MacBook Air", "New MacBook Air, not opened from box", false, true,
					"2024-04-10T00:00:00Z", 2000, 0, 0, 20, 10, 2, Status.SOLD));
			
			bidItemRepository.save(new BidItem(userRepository.findByUsername("bilbobaggins").get().getId(),
					"MacBook Air", "New MacBook Air, not opened from box", true, false,
					"2024-04-07T00:00:00Z", 2000, 50, 500, 20, 10, 2, Status.IN_PROGRESS));

			bidItemRepository.findAll().forEach(bidItem -> log.info("Preloaded " + bidItem));

			paymentRepository.findAll().forEach(payment -> log.info("Preloaded " + payment));
		};
	}

	@Autowired
	Environment env;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		return dataSource;
	}

}