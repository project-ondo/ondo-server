package project.team.ondo.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailService {

    private final JavaMailSender javaMailSender;

    public void sendAuthCode(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("[ondo] 이메일 인증 코드");
        msg.setText("인증 코드: " + code);
        javaMailSender.send(msg);
    }
}
