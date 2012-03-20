package com.business;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;

@Stateless
public class ScheduledBusinessProcess {

    /**
     * Default constructor. 
     */
    public ScheduledBusinessProcess() {
        // TODO Auto-generated constructor stub
    }
	
	@SuppressWarnings("unused")
	@Schedule(minute="*", hour="*", dayOfWeek="*", dayOfMonth="*", month="*", year="*", info="MyTimer")
    private void scheduledTimeout(final Timer t) {
        System.out.println("@Schedulex called at: " + new java.util.Date());
    }
}