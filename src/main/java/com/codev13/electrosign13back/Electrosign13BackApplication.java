package com.codev13.electrosign13back;

import com.codev13.electrosign13back.utils.FIleManageUtil;
import com.codev13.electrosign13back.utils.KeyGeneratorUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class Electrosign13BackApplication {

    public static void main(String[] args) {
        SpringApplication.run(Electrosign13BackApplication.class, args);
//        String secretKey = UUID.randomUUID().toString().replace("-", "");
//        System.out.println("Secret Key: " + secretKey);
//        boolean result = FIleManageUtil.deleteFile("signer/0b324ab2-9f44-4b82-8fe2-abc2955ab971_mon cv.pdf");
//        System.out.println(result);
    }

}
