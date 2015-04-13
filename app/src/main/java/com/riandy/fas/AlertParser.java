package com.riandy.fas;

import android.content.Context;
import android.util.Log;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**
 * Created by Riandy on 30/3/15.
 */
public class AlertParser {

    Context context;

    AlertParser(Context context){
        this.context = context;
    }


    public static List<AlertModel> parse(String filename){

        List<AlertModel> list = new ArrayList<AlertModel>();

        try {

            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("alert");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    AlertModel model = new AlertModel();
                    model.id = Long.parseLong(eElement.getAttribute("id"));
                    model.getAlertFeature().setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    model.getAlertFeature().setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                    model.setEnabled(eElement.getElementsByTagName("isEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setVibrationEnabled(eElement.getElementsByTagName("isVibrationEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setVoiceInstructionStatusEnabled(eElement.getElementsByTagName("isVoiceInstructionEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setSoundEnabled(eElement.getElementsByTagName("isSoundEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setLaunchAppEnabled(eElement.getElementsByTagName("isLaunchAppEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setNotificationEnabled(eElement.getElementsByTagName("isNotificationEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setTone(eElement.getElementsByTagName("tone").item(0).getTextContent());
                    model.getAlertFeature().setAppToLaunch(eElement.getElementsByTagName("appToLaunch").item(0).getTextContent());
                    model.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.values()[Integer.parseInt(eElement.getElementsByTagName("dayTypes").item(0).getTextContent())]);
                    LocalDate startDate = LocalDate.parse(eElement.getElementsByTagName("startDate").item(0).getTextContent(), DateTimeFormat.forPattern("d MMM yyyy"));
                    LocalDate endDate = LocalDate.parse(eElement.getElementsByTagName("endDate").item(0).getTextContent(),DateTimeFormat.forPattern("d MMM yyyy"));
                    model.getAlertSpecs().getDaySpecs().setDateRange(startDate,endDate);
                    model.getAlertSpecs().getDaySpecs().setDayOfWeek(AlertDBHelper.convertStringToBooleanArray(eElement.getElementsByTagName("dayOfWeek").item(0).getTextContent()));
                    model.getAlertSpecs().getDaySpecs().setRepeatWeekly(eElement.getElementsByTagName("repeatWeekly").item(0).getTextContent().equals("true"));
                    model.getAlertSpecs().getHourSpecs().setHourType(HourSpecs.HourTypes.values()[Integer.parseInt(eElement.getElementsByTagName("hourTypes").item(0).getTextContent())]);
                    LocalTime startTime = LocalTime.parse(eElement.getElementsByTagName("startTime").item(0).getTextContent());
                    LocalTime endTime = LocalTime.parse(eElement.getElementsByTagName("endTime").item(0).getTextContent());
                    int intervalHour = Integer.parseInt(eElement.getElementsByTagName("intervalHour").item(0).getTextContent());
                    model.getAlertSpecs().getHourSpecs().setTimeRange(startTime,endTime,intervalHour);
                    model.getAlertSpecs().getHourSpecs().setNumOfTimes(Integer.parseInt(eElement.getElementsByTagName("numberOfTimes").item(0).getTextContent()));
                    model.getAlertSpecs().getDaySpecs().setEveryNDays(Integer.parseInt(eElement.getElementsByTagName("everyNDays").item(0).getTextContent()));
                    model.getAlertSpecs().getHourSpecs().setLastAlertTime(LocalTime.parse(eElement.getElementsByTagName("lastAlertTime").item(0).getTextContent()));
                    model.getAlertSpecs().getHourSpecs().setCurrentCounter(Integer.parseInt(eElement.getElementsByTagName("currentCounter").item(0).getTextContent()));

                    list.add(model);
                    Log.d("Alert model",model.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    return list;
    }
}
