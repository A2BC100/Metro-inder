package com.example.metroinder.realtime.service;



import com.revinate.guava.util.concurrent.RateLimiter;
import com.example.metroinder.realtime.model.GpsTransfer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;


@Component
@Service
public class RealtimeStationservice {
    @Value("${generalKey}")
    public String generalKey;
    @Value("${realTimeKey}")
    public String realTimeKey;
    public StringBuilder sb;

    private final String realTimeUrl = "http://swopenAPI.seoul.go.kr/api/subway";


    private RateLimiter throttle = RateLimiter.create(0.05);

    private GpsTransfer gpsTransfer;

    private RestTemplate restTemplate = new RestTemplate();

    private LocalDate localDate = LocalDate.now();

    public String nx,ny;

    public JSONObject realtimeStaion(String station) throws IOException, ParseException {
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

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(sb.toString());
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj;
    }

    public JSONObject realtiemWeather(double lat,double lon,String date,String time)throws IOException,ParseException {
        GpsTransfer transfer = new GpsTransfer(lat,lon);
        gpsTransfer.transfer(transfer,1);
        System.out.println(gpsTransfer.toString());
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=서비스키"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
//        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(gpsTransfer.getStringnx(), "UTF-8")); /*예보지점의 X 좌표값*/
//        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(gpsTransfer.getStringny(), "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(sb.toString());
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj;
    }



}
