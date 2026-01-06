package project.team.ondo.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import project.team.ondo.global.data.MailEnvironment;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailEnvironment mailEnvironment;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailEnvironment.host());
        mailSender.setPort(mailEnvironment.port());
        mailSender.setUsername(mailEnvironment.username());
        mailSender.setPassword(mailEnvironment.password());

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.timeout", "5000");
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
