package development.team.ticketsystem.authservice.service;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class PemKeyLoader {

    private final ResourceLoader resourceLoader;

    public PemKeyLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @SneakyThrows
    public RSAPublicKey loadPublicKey(String location) {
        String pem = readResource(location);
        String content = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(content);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    @SneakyThrows
    public RSAPrivateKey loadPrivateKey(String location) {
        String pem = readResource(location);
        String content = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(content);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    @SneakyThrows
    private String readResource(String location) {
        Resource resource = resourceLoader.getResource(location);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}