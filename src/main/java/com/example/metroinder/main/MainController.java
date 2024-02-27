package com.example.metroinder.main;

import com.example.metroinder.dataSet.service.StationInformationSetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final StationInformationSetService stationInformationSetService;

    @GetMapping("/")
    public String mainAccess() {
        return "index";
    }


    @GetMapping("/dataSet")
    public void dateSet() {
        //stationInformationSetService.peopleInformationBySeoulAtTimeSave("202312");
        //stationInformationSetService.excelCongetionDataSave("C:\\Users\\iway\\Downloads\\2015~2023.csv"); // 엑셀 파일 일반 저장
        // WMIC CPU Get NumberOfCores - windows cpu 확인 명령어, cpu 수 X 2 가 좋은 성능을 얻는 경우가 많음
        stationInformationSetService.excelCongetionDataParallelizationSave("C:\\Users\\iway\\Downloads\\2015~2023.csv"); // 엑셀 파일 병렬화 저장
        /*병렬화 저장 시에 코드 실행이 금방 끝나지만 JPA가 DBMS에 보낸 쿼리를 실행하는 시간은 다름.
          엄청난 속도로 쿼리를 DB에 전송한 상태에서 저장이 완료될 때까지 시간이 꽤 걸림.
         */
    }

    /*@GetMapping("/test")
    public void test() throws Exception{
        //String lastRegistrationDate = stationInformationSetService.getLastRegistrationDate();
        //String lastRegistrationDate = "201501";
        String lastRegistrationDate = null;
        log.info("마지막 등록일자 : " + lastRegistrationDate);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.of(2015, 1, 10);
        boolean flag = true;

        log.info("혼잡도 데이터 저장 시작...");

        if(lastRegistrationDate == null) {
            log.info("데이터 등록 기록 없음.");

            while (flag) {
                stationInformationSetService.peopleInformationBySeoulAtTimeSave(start.format(dateFormat));
                start = start.plusMonths(1);
                if(start.getYear() == now.getYear() && start.getMonth() == now.getMonth()) {
                    flag = false;
                }
            }
            log.info("혼잡도 데이터 저장완료!!!");

            *//*log.info("역 정보 데이터 저장중...");
            stationInformationSetService.setStationInformation();
            stationInformationSetService.setLetLon();
            log.info("역 정보 데이터 저장완료");

            stationScehduleSet();

            log.info("혼잡도 평균 저장 중...");
            stationInformationSetService.getStationDegreeOfCongestionAvg(count);
            log.info("혼잡도 평균 저장 완료");*//*

        }else {
            LocalDate registrationDate = LocalDate.of(Integer.parseInt(lastRegistrationDate.substring(0, 4)),
                    Integer.parseInt(lastRegistrationDate.substring(4, 6)), 10);
            if(registrationDate.getYear() == now.getYear() && registrationDate.getMonth() == now.getMonth()) {
                log.info("이미 데이터가 등록되어 있습니다. 데이터 등록 종료");
                return;
            }
            start = registrationDate;

            while (flag) {
                stationInformationSetService.peopleInformationBySeoulAtTimeSave(start.format(dateFormat));
                start = start.plusMonths(1);
                if(start.getYear() == now.getYear() && start.getMonth() == now.getMonth()) {
                    flag = false;
                }
            }
            log.info("혼잡도 데이터 저장완료!!!");
        }
    }*/
}

