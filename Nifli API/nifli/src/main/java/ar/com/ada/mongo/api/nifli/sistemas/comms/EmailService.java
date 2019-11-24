package ar.com.ada.mongo.api.nifli.sistemas.comms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * EmailService
 */
@Service
public class EmailService {

    enum TipoEnvio {
        SMTP, API
    }

    @Value("${emailSettings.apiKey}")
    private String apiKey;
    @Value("${emailSettings.apiBaseUri}")
    public String apiBaseUri;
    @Value("${emailSettings.apiBaseUri}")
    public String requestUri;
    @Value("${emailSettings.from}")
    public String from;
    @Value("${emailSettings.domain}")
    public String domain;
    @Value("${emailSettings.enabled}")
    public boolean enabled;


    // Basico
    public void SendEmail(String email, String subject, String message) throws UnirestException {

        if (!this.enabled)
            return;
            
        JsonNode r;
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + this.domain + "/messages")
                .basicAuth("api", this.apiKey)
                .field("from", this.from)
                .field("to", email)
                .field("subject", subject)
                .field("text", message).asJson();

        r = request.getBody();

    }
}