package project.team.ondo.global.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.HexFormat;

@Slf4j
@Component
public class DiscordSignatureVerifier {

    // Ed25519 SubjectPublicKeyInfo prefix (OID 1.3.101.112)
    private static final byte[] ED25519_SPKI_PREFIX = {
            0x30, 0x2a, 0x30, 0x05, 0x06, 0x03, 0x2b, 0x65, 0x70, 0x03, 0x21, 0x00
    };

    @Value("${discord.public-key:}")
    private String publicKeyHex;

    public boolean verify(String signatureHex, String timestamp, byte[] bodyBytes) {
        if (publicKeyHex == null || publicKeyHex.isBlank()) {
            log.error("discord.public-key is not configured. Signature verification failed.");
            return false;
        }
        try {
            byte[] rawKey = HexFormat.of().parseHex(publicKeyHex);
            byte[] spkiKey = concat(ED25519_SPKI_PREFIX, rawKey);

            KeyFactory kf = KeyFactory.getInstance("Ed25519");
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(spkiKey));

            Signature sig = Signature.getInstance("Ed25519");
            sig.initVerify(publicKey);
            sig.update(timestamp.getBytes(StandardCharsets.UTF_8));
            sig.update(bodyBytes);

            return sig.verify(HexFormat.of().parseHex(signatureHex));
        } catch (Exception e) {
            log.warn("Discord signature verification failed: {}", e.getMessage());
            return false;
        }
    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
