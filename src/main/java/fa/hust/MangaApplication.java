package fa.hust;

import fa.hust.entities.Account;
import fa.hust.enums.ERoles;
import fa.hust.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class MangaApplication implements CommandLineRunner {

	final private PasswordEncoder passwordEncoder;
	final private AccountRepository accountRepository;
	public static void main(String[] args) {
		SpringApplication.run(MangaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Account> accounts = List.of(
				Account.builder()
						.email("dung@gmail.com")
						.password(passwordEncoder.encode("123456"))
						.role(ERoles.ROLE_ADMIN)
						.enable(true)
						.build(),
				Account.builder()
						.email("lamem97@gmail.com")
						.password(passwordEncoder.encode("123456"))
						.role(ERoles.ROLE_USER)
						.enable(true)
						.build()
		);
		//accountRepository.saveAll(accounts);
	}
}
