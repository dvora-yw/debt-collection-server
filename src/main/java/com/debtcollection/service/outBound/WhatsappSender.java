package com.debtcollection.service.outBound;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WhatsappSender implements ChannelSender {

    private final String phoneNumberId;
    private final String token;

    public WhatsappSender(
            @Value("${whatsapp.phone-number-id}") String phoneNumberId,
            @Value("${whatsapp.token}") String token
    ) {
        this.phoneNumberId = phoneNumberId;
        this.token = token;
    }

    @Override
    public boolean send(Message message, User user) {
        if (user.getPhone() == null) return false;

        String toPhone = user.getPhone();
        if (!toPhone.startsWith("+972")) {
            toPhone = toPhone.replaceFirst("^0", "+972");
        }

        try {
            //HttpClient client = HttpClient.newHttpClient();
            HttpClient client = UnsafeHttpClient.create();

            String jsonBody = "{"
                    + "\"messaging_product\": \"whatsapp\","
                    + "\"to\": \"" + toPhone + "\","
                    + "\"type\": \"text\","
                    + "\"text\": {\"body\": \"" + message.getContent() + "\"}"
                    + "}";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://graph.facebook.com/v17.0/" + phoneNumberId + "/messages"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("WhatsApp response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
