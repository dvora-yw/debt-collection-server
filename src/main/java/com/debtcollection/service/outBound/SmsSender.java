package com.debtcollection.service.outBound;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.debtcollection.service.outBound.UnsafeOkHttpClient;

@Service
public class SmsSender implements ChannelSender {

    private final String baseUrl;
    private final String token;
    private final String from;
    private final String username;

    public SmsSender(
            @Value("${sms019.base-url}") String baseUrl,
            @Value("${sms019.token}") String token,
            @Value("${sms019.from}") String from,
            @Value("${sms019.username}") String username
    ) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.from = from;
        this.username = username;
    }

    @Override
    public boolean send(Message message, User user) {
        if (user.getPhone() == null) return false;

        String to = normalizePhone(user.getPhone());

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = UnsafeOkHttpClient.create();


        MediaType jsonType = MediaType.parse("application/json");

        String json = """
        {
          "sms": {
            "user": {
              "username": "%s"
            },
            "source": "%s",
            "destinations": {
              "phone": ["%s"]
            },
            "message": "%s"
          }
        }
        """.formatted(
                username,
                from,
                to,
                message.getContent()
        );

        Request request = new Request.Builder()
                .url(baseUrl)
                .post(RequestBody.create(json, jsonType))
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "";
            System.out.println("019 SMS response: " + body);
          //  boolean apiResult = body.s == 0; // רק 0 = הצלחה

            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String normalizePhone(String phone) {
        // 019 רוצה מספר ישראלי רגיל – בלי +972
        if (phone.startsWith("+972")) {
            return "0" + phone.substring(4);
        }
        return phone;
    }
}
