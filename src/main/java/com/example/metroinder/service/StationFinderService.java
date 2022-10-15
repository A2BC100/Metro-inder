package com.example.metroinder.service;

import com.example.metroinder.model.Station;

import java.util.List;

public class StationFinderService {
    private List<Station> allList;
    private List<Station> openList;
    private List<Station> closeList;

    // 역간의 위경도를 이용한 유클리드 거리를 계산하여 반환 (미터 단위)
    private double calcDistance(Station curStn, Station nextStn){
        double lat_start = curStn.lat  * Math.PI / 180.0;
        double lon_start = curStn.lon  * Math.PI / 180.0;
        double lat_end   = nextStn.lat * Math.PI / 180.0;
        double lon_end   = nextStn.lon * Math.PI / 180.0;

        return Math.round( 6378.137 * Math.acos( Math.cos( lat_start ) * Math.cos( lat_end ) * Math.cos( lon_end - lon_start ) + Math.sin( lat_start ) * Math.sin( lat_end )) * 1000.0 );
    }
    // 거리 비용이 적은 경로를 탐색하는건 알겠지만 traffic이 사용되지 않음, 미터 단위이기 때문에 double을 사용해서 한 건 알겠음, traffic은 왜 int로 했는지 궁금함, 그냥 traffic도 double로 해서
    // 거리 비용에 더해서 최종 비용 산정할 때 같이 계산하면 되지 않는지, 내가 잘 이해하고 있는지 모르겠음

    // 역이 업데이트나 추가때문이라면 AllList(모든 역?)에서 처음에는 null일텐데 AllList를 반복시킨다면 에러가 뜰텐데 처음에 allList값을 세팅해줄 때 DB에서 세팅해주고 그 후 업데이트도 DB에서 한다면 굳이 필요한 코드는 아니지 않은지
    // 전체 역 목록에 새로운 역을 추가하거나, 기존 역을 갱신
    public void updateStation(Station curStn){
        // 기존 역 리스트 정보에 해당 역이 존재 하는지 확인
        for(Station station : allList) {
            // 해당 역이 존재한다면? 해당 역 혼잡도(traffic)와 거리 정보등을 업데이트 하고 종료
            if( station.name.equals(curStn.name) ){
                station.name = curStn.name;
                station.traffic = curStn.traffic;
                station.next = curStn.next;
                return;
            }
        }
        // 기존 역 리스트 정보에 해당 이름을 가진 역이 존재하지 않는다면? 새로 추가
        Station new_station = new Station();
        new_station.name = curStn.name;
        new_station.traffic = curStn.traffic;
        new_station.next = curStn.next;
        allList.add(new_station);
    }

    // 지정된 역을 기준으로 가장 최적의 목적지 경로를 계산하기
    // 최종적으로 열린 목록(O)에서 F값이 가장 작은 경로가 최단 경로
    public void find(Station curStn, Station destStn){
        // 해당 출발지 역을 닫힌 목록(C)에 저장
        closeList.add(curStn);
        // 다음 역 리스트를 열린 목록(O)에 저장
        for(Station stn : curStn.next) {
            // 해당 역이 존재한다면? 해당 역 혼잡도(traffic)와 거리 정보등을 업데이트 하고 종료
            if(stn.name.equals(curStn.name)){
                stn.g = calcDistance(curStn, stn);
                stn.h = calcDistance(stn, destStn);
                stn.f = stn.g + stn.h;
                openList.add(stn);
            }
        }
        // 열린 목록(O)에서 F값이 가장 작은 노드를 열린 목록(O)에서 삭제하고 닫힌 목록(C)에 저장
        Station min_node = openList.get(0);
        for(Station stn : openList) {
            if(stn.f < min_node.f)
                min_node = stn;
        }
        closeList.add(min_node);
        openList.remove(min_node);

        // F값이 가장 작은 노드와 연결되어 있는 다음 노드들 중 닫힌 목록(C)에 없는 노드 가져오기
        for(Station stn : min_node.next) {
            if(closeList.contains(stn))
                continue;
            stn.g = calcDistance(curStn, stn);
            stn.h = calcDistance(stn, destStn);
            stn.f = stn.g + stn.h;
            openList.add(stn);
        }
    }
}
