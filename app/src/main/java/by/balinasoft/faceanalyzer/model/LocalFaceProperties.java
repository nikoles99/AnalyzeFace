package by.balinasoft.faceanalyzer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFaceProperties {

    private List<HumanQuality> qualityList = new ArrayList<>();

    private String typeQuality;

    public LocalFaceProperties(String typeQuality) {
        this.typeQuality = typeQuality;
    }

    public void addProperty(HumanQuality humanQuality) {
        qualityList.add(humanQuality);
    }

    public Map<String, List<HumanQuality>> getMapProperties() {
        Map<String, List<HumanQuality>> listMap = new HashMap<>();
        listMap.put(typeQuality, qualityList);
        return listMap;
    }
}
