package com.example.metroinder.realtime.service;

import com.example.metroinder.realtime.dto.request.RealTimeRequest;
import com.revinate.guava.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
@Service
public class RealtimeStationservice {
    @Value("${generalKey}")
    public String generalKey;
    @Value("${realTimeKey}")
    public String realTimeKey;
    public StringBuilder sb;

    private final String realTimeUrl = "http://swopenAPI.seoul.go.kr/api/subway";

    String station;

    private RateLimiter throttle = RateLimiter.create(0.05);

    public String  getStation(RealTimeRequest.RealTimeStationRequest realTimeStationRequest){
        return this.station = realTimeStationRequest.station;
    }

    public String realtimeStaion() throws IOException {
        throttle.acquire();
        StringBuilder urlBuilder = new StringBuilder("http://swopenAPI.seoul.go.kr");
        urlBuilder.append("/" + URLEncoder.encode("api","UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("subway","UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(realTimeKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("realtimeStationArrival", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("0", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("40", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(station, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type","application/json");
        System.out.println("실시간 지하철 도착 정보 API호출 - Response cod:"+conn.getResponseCode());
        BufferedReader Br;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            Br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            Br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        sb = new StringBuilder();
        String line;
        while ((line = Br.readLine()) != null) {
            sb.append(line);
        }
        Br.close();
        conn.disconnect();

        return sb.toString();
    }

}
