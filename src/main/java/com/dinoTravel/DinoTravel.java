package com.dinoTravel;

import java.security.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.dentrassi.crypto.pem.PemKeyStoreProvider;

@SpringBootApplication
public class DinoTravel {

	public static void main(String[] args){
		Security.addProvider(new PemKeyStoreProvider());
		SpringApplication.run(DinoTravel.class, args);
	}

}
