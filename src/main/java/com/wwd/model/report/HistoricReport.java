package com.wwd.model.report;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.chart.XYChart;

/**
 * Class to handle instantiation of Historic Reports
 */
public class HistoricReport {

    public static XYChart.Series<String, Number> getSeries(float latitude,
                                                          float longitude,
                                                          int year,
                                                          ImpurityType ppm,
                                                          List<WaterSpot>
                                                                   waterSpots) {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        Location loc = new Location(latitude, longitude);

        ArrayList<Integer> januaryValues = new ArrayList<>();
        ArrayList<Integer> februaryValues = new ArrayList<>();
        ArrayList<Integer> marchValues = new ArrayList<>();
        ArrayList<Integer> aprilValues = new ArrayList<>();
        ArrayList<Integer> mayValues = new ArrayList<>();
        ArrayList<Integer> juneValues = new ArrayList<>();
        ArrayList<Integer> julyValues = new ArrayList<>();
        ArrayList<Integer> augustValues = new ArrayList<>();
        ArrayList<Integer> septemberValues = new ArrayList<>();
        ArrayList<Integer> octoberValues = new ArrayList<>();
        ArrayList<Integer> novemberValues = new ArrayList<>();
        ArrayList<Integer> decemberValues = new ArrayList<>();

        waterSpots.stream().filter(i -> loc.equals(i.getLocation()))
                .forEach(i -> i.getPurityReportList().stream()
                    .filter(j -> j.getDate().getYear() == year)
                    .forEach(j -> {
                            if (j.getDate().getMonth() == Month.JANUARY) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    januaryValues.add(j.getVirusPPM());
                                } else {
                                    januaryValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth()
                                    == Month.FEBRUARY) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    februaryValues.add(j.getVirusPPM());
                                } else {
                                    februaryValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.MARCH) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    marchValues.add(j.getVirusPPM());
                                } else {
                                    marchValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.APRIL) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    aprilValues.add(j.getVirusPPM());
                                } else {
                                    aprilValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.MAY) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    mayValues.add(j.getVirusPPM());
                                } else {
                                    mayValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.JUNE) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    juneValues.add(j.getVirusPPM());
                                } else {
                                    juneValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.JULY) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    julyValues.add(j.getVirusPPM());
                                } else {
                                    julyValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth() == Month.AUGUST) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    augustValues.add(j.getVirusPPM());
                                } else {
                                    augustValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth()
                                    == Month.SEPTEMBER) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    septemberValues.add(j.getVirusPPM());
                                } else {
                                    septemberValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth()
                                    == Month.OCTOBER) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    octoberValues.add(j.getVirusPPM());
                                } else {
                                    octoberValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth()
                                    == Month.NOVEMBER) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    novemberValues.add(j.getVirusPPM());
                                } else {
                                    novemberValues.add(j.getContaminantPPM());
                                }
                            } else if (j.getDate().getMonth()
                                    == Month.DECEMBER) {
                                if (ppm.equals(ImpurityType.VIRUS)) {
                                    decemberValues.add(j.getVirusPPM());
                                } else {
                                    decemberValues.add(j.getContaminantPPM());
                                }
                            }
                        }));

        float januaryMedian = calculateAverage(januaryValues);
        float februaryMedian = calculateAverage(februaryValues);
        float marchMedian = calculateAverage(marchValues);
        float aprilMedian = calculateAverage(aprilValues);
        float mayMedian = calculateAverage(mayValues);
        float juneMedian = calculateAverage(juneValues);
        float julyMedian = calculateAverage(julyValues);
        float augustMedian = calculateAverage(augustValues);
        float septemberMedian = calculateAverage(septemberValues);
        float octoberMedian = calculateAverage(octoberValues);
        float novemberMedian = calculateAverage(novemberValues);
        float decemberMedian = calculateAverage(decemberValues);

        series1.getData().add(new XYChart.Data<>("Jan", januaryMedian));
        series1.getData().add(new XYChart.Data<>("Feb", februaryMedian));
        series1.getData().add(new XYChart.Data<>("Mar", marchMedian));
        series1.getData().add(new XYChart.Data<>("Apr", aprilMedian));
        series1.getData().add(new XYChart.Data<>("May", mayMedian));
        series1.getData().add(new XYChart.Data<>("Jun", juneMedian));
        series1.getData().add(new XYChart.Data<>("Jul", julyMedian));
        series1.getData().add(new XYChart.Data<>("Aug", augustMedian));
        series1.getData().add(new XYChart.Data<>("Sep", septemberMedian));
        series1.getData().add(new XYChart.Data<>("Oct", octoberMedian));
        series1.getData().add(new XYChart.Data<>("Nov", novemberMedian));
        series1.getData().add(new XYChart.Data<>("Dec", decemberMedian));

        return series1;
    }

    public static float calculateAverage(List<Integer> arrList) {
        float avg = 0.0f;
        if (arrList != null && arrList.size() > 0) {
            float sum = 0.0f;
            for (Integer i : arrList) {
                sum += i;
            }
            avg = sum / arrList.size();
        }
        return avg;
    }
}
