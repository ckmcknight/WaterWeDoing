package com.wwd.model.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * class to handle water reports at a certain location
 */
public class WaterSpot implements Serializable {

    private final Location location;
    private final LinkedList<WaterReport> waterReportList;
    private final LinkedList<PurityReport> purityReportList;

    /**
     * creates an empty list of water reports and attaches a location
     *
     * @param location the location of the report
     */
    public WaterSpot(Location location) {
        waterReportList = new LinkedList<>();
        purityReportList = new LinkedList<>();
        this.location = location;
    }

    /**
     * adds a water report to the list of water reports
     *
     * @param waterReport the report being added
     */
    public void addWaterReport(WaterReport waterReport) {
        waterReportList.add(waterReport);
    }

    /**
     * adds a purity report to the list of purity reports
     *
     * @param purityReport the report being added
     */
    public void addPurityReport(PurityReport purityReport) {
        purityReportList.add(purityReport);
    }

    /**
     * a getter of the location of the water spot
     *
     * @return the location of the report
     */
    public Location getLocation() {
        return location;
    }

    /**
     * returns a "copied" list of the water reports for the current location
     * @return a list of water reports
     */
    public List<WaterReport> getWaterReportList() {
        List<WaterReport> temp = new LinkedList<>();
        temp.addAll(waterReportList);
        return temp;
    }

    /**
     * returns a "copied" list of the purity reports for the current location
     * @return a list of purity reports
     */
    public List<PurityReport> getPurityReportList() {
        List<PurityReport> temp = new LinkedList<>();
        temp.addAll(purityReportList);
        return temp;
    }

    private String calculateMedian(int[] arr) {
        int median;
        if (arr.length != 0) {
            if (arr.length % 2 == 0) {
                median = (arr[arr.length / 2]
                        + arr[arr.length / 2 - 1]) / 2;
            } else {
                median = arr[arr.length / 2];
            }
            return Integer.toString(median);
        }
        return "---";
    }


    /**
     * Calculates the median value for virusPPM
     * @return the median value for virusPPM
     */
    public String calculateMedianVirusPPM() {
        int[] virusArray = new int[getPurityReportList().size()];
        int a = 0;
        for (PurityReport i : getPurityReportList()) {
            virusArray[a] = i.getVirusPPM(); // a = i
            a++;
        }
        Arrays.sort(virusArray);
        return calculateMedian(virusArray);
    }

    /**
     * Calculates the median value for contaminantPPM
     * @return the median value for contaminantPPM
     */
    public String calculateMedianContaminantPPM() {
        int[] contaminantArray = new int[getPurityReportList().size()];
        int b = 0;
        for (PurityReport j : getPurityReportList()) {
            contaminantArray[b] = j.getContaminantPPM(); // b = i
            b++;
        }
        Arrays.sort(contaminantArray);
        return calculateMedian(contaminantArray);
    }

    /**
     * Calculates the most occurring WaterType
     * @return the most occurring WaterType
     */
    public String calculateWaterTypeMode() {

        //Extract all WaterTypes from List
        ArrayList<WaterType> waterTypeArray
                = getWaterReportList().stream().map(WaterReport::getType)
                .collect(Collectors.toCollection(ArrayList::new));

        // Determine counts for each WaterType
        Map<WaterType, Integer> waterTypeCount = new HashMap<>();
        for (WaterType w: waterTypeArray) {
            Integer c = waterTypeCount.get(w);
            if (c == null) {
                c = 0;
            }
            c++;
            waterTypeCount.put(w, c);
        }

        // Determine which count is highest
        Map.Entry<WaterType, Integer> mostRepeated = null;
        for (Map.Entry<WaterType, Integer> e
                : waterTypeCount.entrySet()) {
            if (mostRepeated == null
                    || mostRepeated.getValue() < e.getValue()) {
                mostRepeated = e;
            }
        }
        if (mostRepeated != null) {
            return mostRepeated.getKey().toString();
        }
        return "---";
    }

    /**
     * Calculates the most occurring WaterCondition
     * @return the most occurring WaterCondition
     */
    public String calculateWaterConditionMode() {

        //Extract all WaterConditions from List
        ArrayList<WaterCondition> waterConditionArray
                = getWaterReportList().stream().map(WaterReport::getCondition)
                .collect(Collectors.toCollection(ArrayList::new));

        // Determine counts for each WaterCondition
        Map<WaterCondition, Integer> waterConditionCount = new HashMap<>();
        for (WaterCondition w: waterConditionArray) {
            Integer c = waterConditionCount.get(w);
            if (c == null) {
                c = 0;
            }
            c++;
            waterConditionCount.put(w, c);
        }

        // Determine which count is highest
        Map.Entry<WaterCondition, Integer> mostRepeated = null;
        for (Map.Entry<WaterCondition, Integer> e
                : waterConditionCount.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue()
                    < e.getValue()) {
                mostRepeated = e;
            }
        }
        if (mostRepeated != null) {
            return mostRepeated.getKey().toString();
        }
        return "---";
    }

    /**
     * Calculates the most occurring OverallCondition
     * @return the most occurring OverallCondition
     */
    public String calculateWaterSafetyMode() {

        //Extract all OverallConditions from List
        ArrayList<OverallCondition> overallConditionArray
                = getPurityReportList().stream().map(PurityReport::getCondition)
                .collect(Collectors.toCollection(ArrayList::new));

        // Determine counts for each OverallCondition
        Map<OverallCondition, Integer> overallConditionCount = new HashMap<>();
        for (OverallCondition w: overallConditionArray) {
            Integer c = overallConditionCount.get(w);
            if (c == null) {
                c = 0;
            }
            c++;
            overallConditionCount.put(w, c);
        }

        // Determine which count is highest
        Map.Entry<OverallCondition, Integer> mostRepeated = null;
        for (Map.Entry<OverallCondition, Integer> e
                : overallConditionCount.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue()
                    < e.getValue()) {
                mostRepeated = e;
            }
        }
        if (mostRepeated != null) {
            return mostRepeated.getKey().toString();
        }
        return "---";
    }

    @Override
    public String toString() {
        String out = location.toString();
        out += "Purity Reports:";
        for (PurityReport r : purityReportList) {
            out += r.toString();
        }

        out += "Source Reports:";
        for (WaterReport r: waterReportList) {
            out += r.toString();
        }
        return out;
    }
}
