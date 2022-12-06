package com.example.metroinder.service;

import com.example.metroinder.dto.StationScheduleDto;
import com.example.metroinder.dto.TimeStationPersonnelDto;
import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.Line;
import com.example.metroinder.model.StationSchedule;
import com.example.metroinder.repository.CapitalareaStationRepository;
import com.example.metroinder.repository.LineRepository;
import com.example.metroinder.repository.StationLineRepository;
import com.example.metroinder.repository.StationScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class StationScheduleService {
    @Value("${generalKey}")
    private String generalKey;

    private final StationScheduleRepository stationScheduleRepository;
    private final CapitalareaStationRepository capitalareaStationRepository;
    private final StationLineRepository stationLineRepository;
    private final LineRepository lineRepository;

    /* 입력한 역이름으로 역코드목록을 json으로 받아옴 */
    public String getStationCode(String station) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openAPI.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("SearchInfoBySubwayNameService", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("999", "UTF-8"));

        /* 서비스별 추가 요청인자*/
        /* 전철역이름 */
        if(station.equals("서울")) {
            station = "서울역";
        }
        urlBuilder.append("/" + URLEncoder.encode(station, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode()); /* 연결에 대한 확인*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuilder.append(line);
        }
        rd.close();
        conn.disconnect();
        return stringBuilder.toString();
    }

    public String getStationSchduleAPI(String stationCode, String week, String inout) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openAPI.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("SearchSTNTimeTableByIDService", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("999", "UTF-8"));

        /* 서비스별 추가 요청인자*/
        /* 전철역코드 */
        urlBuilder.append("/" + URLEncoder.encode(stationCode, "UTF-8"));
        /* 요일 */
        urlBuilder.append("/" + URLEncoder.encode(week, "UTF-8"));
        /* 상/하행선 */
        urlBuilder.append("/" + URLEncoder.encode(inout, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuilder.append(line);
        }
        rd.close();
        conn.disconnect();
        return stringBuilder.toString();
    }

    /* JSON List에서 역코드를 뽑아와 열차 시간표 API 호출 */
    public void stationScheduleCall(String json, String week, String inout) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            JSONObject searchInfoBySubwayName = (JSONObject) jsonObject.get("SearchInfoBySubwayNameService");
            //log.info("json("+ i + ") : " + searchInfoBySubwayName);
            JSONArray jsonArr = (JSONArray) searchInfoBySubwayName.get("row");
            //JSONArray result = (JSONArray) searchInfoBySubwayName.get("RESULT");
            List<String> stationScheduleList = new ArrayList<>();
            for (int count = 0; count < jsonArr.size(); count++) {
                JSONObject row = (JSONObject) jsonArr.get(count);
                String stationCode = (String) row.get("STATION_CD");

                String lineNum = (String) row.get("LINE_NUM");
                if(lineNum.equals("01호선") || lineNum.equals("02호선") || lineNum.equals("03호선") || lineNum.equals("04호선") || lineNum.equals("05호선") || lineNum.equals("06호선") || lineNum.equals("07호선") || lineNum.equals("08호선")) {
                    String stationSchduleJson = getStationSchduleAPI(stationCode, week, inout);
                    // log.info("역 시간표 : " + stationSchduleJson);
                    setStationScheduleSave(stationSchduleJson, lineNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* 열차 시간표 저장 */
    public void setStationScheduleSave(String json, String line) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            //log.info("jsonObject : " + jsonObject);
            JSONObject result = (JSONObject) jsonObject.get("RESULT");
            if(result != null) {
                return;
            }
            JSONObject searchSTNTimeTableByIDService = (JSONObject) jsonObject.get("SearchSTNTimeTableByIDService");
            JSONArray jsonArr = (JSONArray) searchSTNTimeTableByIDService.get("row");
            List<StationScheduleDto> stationScheduleDtoList = new ArrayList<>();
            for (int count = 0; count < jsonArr.size(); count++) {
                JSONObject row = (JSONObject) jsonArr.get(count);
                String week = (String) row.get("WEEK_TAG");
                String upDown = (String) row.get("INOUT_TAG");
                String express = (String) row.get("EXPRESS_YN");
                if(week.equals("1")) {
                    week = "평일";
                }else if (week.equals("2")) {
                    week = "토요일";
                }else {
                    week = "휴일/일요일";
                }
                upDown = upDown.equals("1") ? "상행, 내선" : "하행, 외선";
                express = express.equals("G") ? "일반" : "급행";
                String station = (String) row.get("STATION_NM");
                String stnLine = line;
                switch(stnLine) {
                    case "01호선":
                        if(station.equals("소요산") || station.equals("동두천") || station.equals("보산") || station.equals("동두천중앙") || station.equals("지행") || station.equals("덕정") || station.equals("덕계") || station.equals("양주") || station.equals("녹양") || station.equals("가능") || station.equals("의정부") || station.equals("회룡") || station.equals("망월사") || station.equals("도봉산") || station.equals("도봉") || station.equals("방학") || station.equals("창동") || station.equals("녹천") || station.equals("월계") || station.equals("광운대") || station.equals("석계") || station.equals("신이문") || station.equals("외대앞") || station.equals("회기") ||
                                station.equals("청량리") || station.equals("제기동") || station.equals("신설동") || station.equals("동묘앞") || station.equals("동대문") || station.equals("종로5가") || station.equals("종로3가") || station.equals("종각") || station.equals("시청") || station.equals("서울역") || station.equals("남영") || station.equals("용산") || station.equals("노량진") || station.equals("대방") || station.equals("신길") || station.equals("영등포") || station.equals("신도림") || station.equals("구로") || station.equals("구일") || station.equals("개봉") || station.equals("오류동") || station.equals("온수") || station.equals("역곡") || station.equals("소사") ||
                                station.equals("부천") || station.equals("중동") || station.equals("송내") || station.equals("부개") || station.equals("부평") || station.equals("백운") || station.equals("동암") || station.equals("간석") || station.equals("주안") || station.equals("도화") || station.equals("제물포") || station.equals("도원") || station.equals("동인천") || station.equals("인천")) {
                            stnLine = "경인1호선";
                        }
                        if(station.equals("가산디지털단지") || station.equals("독산") || station.equals("금천구청") || station.equals("석수") || station.equals("관악") || station.equals("안양") || station.equals("명학") || station.equals("금정") || station.equals("군포") || station.equals("당정") || station.equals("의왕") || station.equals("성균관대") || station.equals("화서") || station.equals("수원") || station.equals("세류") || station.equals("병점") || station.equals("세마") || station.equals("오산대") || station.equals("오산") || station.equals("진위") || station.equals("송탄") || station.equals("서정리") || station.equals("평택지제") || station.equals("평택") || station.equals("성환") || station.equals("직산") || station.equals("두정") || station.equals("천안") || station.equals("봉명") || station.equals("쌍용") || station.equals("아산") || station.equals("탕정") || station.equals("배방") || station.equals("온양온천") || station.equals("신창")){
                            stnLine = "경부1호선";
                        }
                        if(station.equals("서동탄")) {
                            stnLine = "병점기지선";
                        }
                        break;
                    case "02호선":
                        stnLine = "서울2호선";
                        if(station.equals("용답") || station.equals("신답") || station.equals("용두") || station.equals("신설동")) {
                            stnLine ="성수지선";
                        }
                        if(station.equals("도림천") || station.equals("양천구청") || station.equals("신정네거리")) {
                            stnLine ="신정지선";
                        }
                        break;
                    case "03호선":
                        stnLine = "수도권3호선";
                        break;
                    case "04호선":
                        stnLine = "수도권4호선";
                        break;
                    case "05호선":
                        stnLine = "수도권5호선";

                        if(station.equals("둔촌동") || station.equals("올림픽공원") || station.equals("방이") || station.equals("오금") || station.equals("개롱") || station.equals("거여") || station.equals("마천")) {
                            stnLine = "마천지선";
                        }
                        break;
                    case "06호선":
                        stnLine = "서울6호선";
                        break;
                    case "07호선":
                        stnLine = "서울7호선";
                        break;
                    case "08호선":
                        stnLine = "서울8호선";
                        break;
                    default:
                        break;
                }

                StationScheduleDto stationScheduleDto = StationScheduleDto.builder()
                        .station(station)
                        .line(stnLine)
                        .arrivalTime((String) row.get("LEFTTIME"))
                        .departureTime((String) row.get("ARRIVETIME"))
                        .arrivalStation((String) row.get("SUBWAYSNAME"))
                        .departureStation((String) row.get("SUBWAYENAME"))
                        .week(week)
                        .upDown(upDown)
                        .express(express)
                        .build();
                stationScheduleDtoList.add(stationScheduleDto);
                if(station.equals("구로") || station.equals("금정") || station.equals("병점") || station.equals("성수") || station.equals("신도림") || station.equals("까치산")) {
                    if(station.equals("구로") && stnLine.equals("01호선")) {
                        stnLine = "경인1호선";
                    }else if(station.equals("금정") && stnLine.equals("01호선")) {
                        stnLine = "수도권4호선";
                    } else if(station.equals("병점") && stnLine.equals("01호선")) {
                        stnLine = "병점기지선";
                    } else if(station.equals("성수") && stnLine.equals("서울2호선")) {
                        stnLine = "성수지선";
                    } else if(station.equals("신도림") && stnLine.equals("서울2호선")) {
                        stnLine = "신정지선";
                    } else if(station.equals("까치산") && stnLine.equals("수도권5호선")) {
                        stnLine = "신정지선";
                    }
                    stationScheduleDto = StationScheduleDto.builder()
                            .station(station)
                            .line(stnLine)
                            .arrivalTime((String) row.get("LEFTTIME"))
                            .departureTime((String) row.get("ARRIVETIME"))
                            .arrivalStation((String) row.get("SUBWAYSNAME"))
                            .departureStation((String) row.get("SUBWAYENAME"))
                            .week(week)
                            .upDown(upDown)
                            .express(express)
                            .build();
                    stationScheduleDtoList.add(stationScheduleDto);
                }
            }
            StationScheduleDto stationScheduleDto = new StationScheduleDto();
            List<StationSchedule> stationScheduleList = stationScheduleDto.toEntityList(stationScheduleDtoList);
            for (StationSchedule stationSchedule : stationScheduleList) {
                stationScheduleRepository.save(stationSchedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //혼잡도데이터가 담겨있는 Entity에서 겹치지 않는(distinct) 역이름을 list에 담아오기 위한 메소드
    public List<String> stationDistinctList() {
        List<String> stationList = capitalareaStationRepository.findDistinctStation();
        return stationList;
    }

    public Map findStationSchedule(String stationName) {
        Map<String, List<Map<String, Object>>> json = new HashMap<>();
        List<StationSchedule> stationScheduleList = stationScheduleRepository.findByStation(stationName);
        List<Map<String, Object>> weekdayUp = new ArrayList<>();
        List<Map<String, Object>> weekdayDown = new ArrayList<>();
        List<Map<String, Object>> weekendUp  = new ArrayList<>();
        List<Map<String, Object>> weekendDown  = new ArrayList<>();

        //stationLineRepository.findByCapitalareaStationAndLine()

        for(StationSchedule stationSchedule : stationScheduleList) {
            String express = stationSchedule.getExpress(); // 급행여부 구분
            String upDown = stationSchedule.getUpDown(); // 상,하행 구분
            String departureStation = stationSchedule.getDepartureStation();//출발역
            String arrivalStation = stationSchedule.getArrivalStation(); // 도착역
            String arrivalTime = stationSchedule.getArrivalTime(); //출발시간
            CapitalareaStation capitalareaStation = capitalareaStationRepository.findByStation(stationName);
            String line = stationSchedule.getLine();
            Line findLine = lineRepository.findByLine("");


            if(arrivalTime.equals("00:00:00")) {
                continue;
            }
            String weekDivision = stationSchedule.getWeek(); //평일, 토요일, 휴일+일요일
            String timeDivision = arrivalTime.substring(0,2);
            String minuteDivision = arrivalTime.substring(3,5);
            log.info(timeDivision);
            log.info(minuteDivision);
            log.info(arrivalTime);
            if(weekDivision.equals("평일")) {
                if(upDown.equals("상행, 내선")) {
                    Map<String, Object> schedule = new HashMap<>();
                    if(express.equals("급행"))
                        schedule.put("급행", express);
                    schedule.put("출발역", departureStation);
                    schedule.put("도착역", arrivalStation);
                    schedule.put(timeDivision, minuteDivision);
                    weekdayUp.add(schedule);
                } else {
                    Map<String, Object> schedule = new HashMap<>();
                    if(express.equals("급행"))
                        schedule.put("급행", express);
                    schedule.put("출발역", departureStation);
                    schedule.put("도착역", arrivalStation);
                    schedule.put(timeDivision, minuteDivision);
                    weekdayDown.add(schedule);
                }
            }else if(weekDivision.equals("토요일")) {
                if(upDown.equals("상행, 내선")) {
                    Map<String, Object> schedule = new HashMap<>();
                    if(express.equals("급행"))
                        schedule.put("급행", express);
                    schedule.put("출발역", departureStation);
                    schedule.put("도착역", arrivalStation);
                    schedule.put(timeDivision, minuteDivision);
                    weekendUp.add(schedule);
                } else {
                    Map<String, Object> schedule = new HashMap<>();
                    if(express.equals("급행"))
                        schedule.put("급행", express);
                    schedule.put("출발역", departureStation);
                    schedule.put("도착역", arrivalStation);
                    schedule.put(timeDivision, minuteDivision);
                    weekendDown.add(schedule);
                }
            }
        }
        json.put("평일 상행", weekdayUp);
        json.put("평일 하행", weekdayDown);
        json.put("주말 상행", weekendUp);
        json.put("주말 하행", weekendDown);

        return json;
    }

}
