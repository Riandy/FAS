package com.riandy.fas;

/**
 * Created by Riandy on 19/4/15.
 */
//public class SchedulerTest {
//
//    //to run this class set your phone date and time to:
//    //Date : 20 April 2015
//    //Time : 2.00 pm
//
//    public void runTest(){
//        testOneOffAlert();
//        testOneOffAlertExpired();
//    }
//
//    public void testOneOffAlert(){
//        AlertModel alert = new AlertModel();
//        alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.DATEONLY);
//        alert.getAlertSpecs().getDaySpecs().setStartDate(new LocalDate());
//        alert.getAlertSpecs().getHourSpecs().setExactTime(new LocalTime(21, 30, 0));
//
//        AlertManagerHelper.counterReset = false;
//        LocalTime localTime = AlertManagerHelper.getValidTime(alert.getAlertSpecs().getHourSpecs());
//        LocalDate localDate = AlertManagerHelper.getValidDate(alert.getAlertSpecs().getDaySpecs(), alert.getAlertSpecs().getHourSpecs());
//
//        if(localDate == null || localTime == null) {
//            Log.d("test ", "testOneOffAlert alarm expired " + localDate + " " + localTime);
//            return;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(localDate.getYear(), localDate.getMonthOfYear() - 1, localDate.getDayOfMonth(),
//                localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute());
//
//        Calendar expected = Calendar.getInstance();
//        expected.set(2015, 3, 20, 21, 30, 0);
//
//        if(expected.equals(calendar)){
//            Log.d("test ", "testOneOffAlert PASS");
//        }else{
//            Log.d("test ", "testOneOffAlert FAIL " + "expected : " + expected.getTime().toString() + "actual : " + calendar.getTime().toString());
//        }
//    }
//
//    public void testOneOffAlertExpired(){
//        AlertModel alert = new AlertModel();
//        alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.DATEONLY);
//        alert.getAlertSpecs().getDaySpecs().setStartDate(new LocalDate());
//        alert.getAlertSpecs().getHourSpecs().setExactTime(new LocalTime(11, 30, 0));
//
//        AlertManagerHelper.counterReset = false;
//        LocalTime localTime = AlertManagerHelper.getValidTime(alert.getAlertSpecs().getHourSpecs());
//        LocalDate localDate = AlertManagerHelper.getValidDate(alert.getAlertSpecs().getDaySpecs(), alert.getAlertSpecs().getHourSpecs());
//
//        if(localDate == null || localTime == null) {
//            Log.d("test ", "testOneOffAlertExpired alarm expired " + localDate + " " + localTime);
//            return;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(localDate.getYear(), localDate.getMonthOfYear() - 1, localDate.getDayOfMonth(),
//                localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute());
//
//        Calendar expected = Calendar.getInstance();
//        expected.set(2015, 3, 20, 11, 30, 0);
//
//        if(expected.equals(calendar)){
//            Log.d("test ", "testOneOffAlertExpired PASS");
//        }else{
//            Log.d("test ", "testOneOffAlertExpired FAIL " + "expected : " + expected.getTime().toString() + "actual : " + calendar.getTime().toString());
//        }
//    }
//
//    public void testSpecificDayTimeRange(){
//        AlertModel alert = new AlertModel();
//        alert.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.DATERANGE);
//        alert.getAlertSpecs().getDaySpecs().setStartDate(new LocalDate());
//        alert.getAlertSpecs().getDaySpecs().setEndDate(new LocalDate().plusDays(2));
//        alert.getAlertSpecs().getHourSpecs().setExactTime(new LocalTime(21, 30, 0));
//
//        AlertManagerHelper.counterReset = false;
//        LocalTime localTime = AlertManagerHelper.getValidTime(alert.getAlertSpecs().getHourSpecs());
//        LocalDate localDate = AlertManagerHelper.getValidDate(alert.getAlertSpecs().getDaySpecs(), alert.getAlertSpecs().getHourSpecs());
//
//        if(localDate == null || localTime == null) {
//            Log.d("test ", "testOneOffAlert alarm expired " + localDate + " " + localTime);
//            return;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(localDate.getYear(), localDate.getMonthOfYear() - 1, localDate.getDayOfMonth(),
//                localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute());
//
//        Calendar expected = Calendar.getInstance();
//        expected.set(2015, 3, 20, 21, 30, 0);
//
//        if(expected.equals(calendar)){
//            Log.d("test ", "testOneOffAlert PASS");
//        }else{
//            Log.d("test ", "testOneOffAlert FAIL " + "expected : " + expected.getTime().toString() + "actual : " + calendar.getTime().toString());
//        }
//    }
//}
