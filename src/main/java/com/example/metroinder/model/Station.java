package com.example.metroinder.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class Station {
    public String name;
    public String line;
    public Map<String, Object> traffic;
    public double lat;
    public double lon;
    public double f;
    public double g;
    public double h;
    public List<Station> parent;
    public List<Station> next;
}
