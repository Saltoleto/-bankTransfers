package br.com.saltoleto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BankTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransferApplication.class, args);
	}

}
