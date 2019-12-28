package pl.herfor.server.data;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String NEW_REPORT_NOTIFICATION_TOPIC = "report-new";
    public static final String UPDATE_REPORT_NOTIFICATION_TOPIC = "report-update";
    public static final String REMOVE_REPORT_NOTIFICATION_TOPIC = "report-remove";

    public static Boolean DEV_SWITCH = false;
    public static int GRADE_IMPACT_DURATION = 30;
    public static int REGULAR_EXPIRY_DURATION = 10 * 60;
}
