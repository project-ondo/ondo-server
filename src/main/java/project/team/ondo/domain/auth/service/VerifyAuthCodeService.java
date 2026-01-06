package project.team.ondo.domain.auth.service;

public interface VerifyAuthCodeService {
    String execute(String email, String code);
}
