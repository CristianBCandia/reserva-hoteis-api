package com.cristian.teste.reservas.hoteis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class TestMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configuração básica, pode ser ajustada conforme necessário
        mailSender.setHost("localhost");
        mailSender.setPort(25);
        return mailSender;
    }

}
