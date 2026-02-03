package com.debtcollection.service.outBound;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.io.IOException;

@Service
public class VoiceCallSender implements ChannelSender {

    private final String baseUrl;
    private final String token;
    private final String from;

    public VoiceCallSender(
            @Value("${sms019.base-url}") String baseUrl,
            @Value("${sms019.token}") String token,
            @Value("${sms019.from}") String from
    ) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.from = from;
    }

    @Override
    public boolean send(Message message, User user) {
        if (user.getPhone() == null) return false;

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = unsafeClient();
        MediaType mediaType = MediaType.parse("application/json");

        /*
         * דוגמה לשיחת TTS (הקראה קולית)
         * שימי לב: אם 019 דורשים שדה אחר – רק מחליפים JSON
         */
        String json = "{"
                + "\"from\": \"" + from + "\","
                + "\"to\": \"" + normalizePhone(user.getPhone()) + "\","
                + "\"text\": \"" + message.getContent() + "\","
                + "\"language\": \"he\""
                + "}";

        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url(baseUrl + "/voice/call")
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("VOICE 019 response: "
                    + response.code() + " " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String normalizePhone(String phone) {
        if (phone.startsWith("0")) {
            return "+972" + phone.substring(1);
        }
        return phone;
    }


    private OkHttpClient unsafeClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
