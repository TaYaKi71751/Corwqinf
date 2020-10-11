package koreawqi.temperature;

import java.io.IOException;

public class Request {
    private String koreaWQIURL = "http://koreawqi.go.kr/wQSCHomeLayout_D.wq";
    private String firefoxUserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0";

    public String getTemperatureJSONString() throws IOException {
        org.jsoup.select.Elements table_01Elements = org.jsoup.Jsoup.connect(koreaWQIURL).userAgent(firefoxUserAgent)
                .get().getElementsByClass("table_01");
        org.jsoup.select.Elements timetableElements = org.jsoup.Jsoup.connect(koreaWQIURL).userAgent(firefoxUserAgent)
                .get().getElementsByClass("timetable");

        String timetableString = timetableElements.get(0).childNode(3).childNode(0).toString();
        while (timetableString.contains("<") | timetableString.contains(">")) {
            timetableString = timetableString.replace(
                    timetableString.substring(timetableString.indexOf("<"), timetableString.indexOf(">") + 1), "");
        }
        timetableString = timetableString.substring(timetableString.indexOf("="), timetableString.indexOf(";"));

        while (timetableString.contains(" ") || timetableString.contains("=") || timetableString.contains("\""))
            timetableString = timetableString.replace("=", "").replace("\"", "").replace(" ", "");

        String jsonDataString = "";
        jsonDataString += "{";
        jsonDataString += "\"" + "date" + "\"" + ":" + "\"" + timetableString.substring(0, timetableString.indexOf("+"))
                + "\",";
        jsonDataString += "\"" + "time" + "\"" + ":" + "\""
                + timetableString.substring(timetableString.indexOf("+") + 1, timetableString.length()) + "\",";
        for (int i = 0; i < table_01Elements.size(); i++) {
            jsonDataString += (i == 0 ? "\"Han\""
                    : i == 1 ? "\"Nakdong\"" : i == 2 ? "\"Geum\"" : i == 3 ? "\"Yeongsan\"" : "\"undefined\"") + ":[";
            for (int j = 0; j < table_01Elements.get(i).childNodeSize(); j++) {
                if (!table_01Elements.get(i).childNode(j).toString().contains("tbody"))
                    continue;
                for (int k = 0; k < table_01Elements.get(i).childNode(j).childNodeSize(); k++) {
                    if (!table_01Elements.get(i).childNode(j).childNode(k).toString().contains("tr"))
                        continue;
                    try {
                        jsonDataString += "{" + "\"name\":" + "\""
                                + table_01Elements.get(i).childNode(j).childNode(k).childNode(1).childNode(0).toString()
                                + "\"," + "" + "\"temp\":";
                        jsonDataString += "\"";
                        try {
                            jsonDataString += Float.toString(Float.parseFloat(table_01Elements.get(i).childNode(j).childNode(k).childNode(4).toString()));
                        } catch (Exception e) {
                            jsonDataString += table_01Elements.get(i).childNode(j).childNode(k).childNode(3)
                                    .childNode(0).toString();
                        }
                        jsonDataString += "\"";
                        jsonDataString += "" + "}";
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
            jsonDataString += (i == table_01Elements.size() - 1 ? "]" : "],");
        }
        jsonDataString += "}";

        while (jsonDataString.contains("}{"))
            jsonDataString = jsonDataString.replace("}{", "},{");
        return jsonDataString;
    }

    public static void main(String[] args) throws IOException {
        new Request().getTemperatureJSONString();
    }
}