package quartzdemo.quartzdemo.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import quartzdemo.quartzdemo.job.EmailJob;
import quartzdemo.quartzdemo.payload.ScheduleEmailRequest;
import quartzdemo.quartzdemo.payload.ScheduleEmailResponse;

@RestController
public class EmailJobSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(EmailJobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/scheduleEmail")
    public ResponseEntity scheduleEmail(@Valid @RequestBody ScheduleEmailRequest scheduleEmailRequest) {
        try {
//            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), scheduleEmailRequest.getTimeZone());
//            if(dateTime.isBefore(ZonedDateTime.now())) {
//                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
//                        "dateTime must be after current time");
//                return ResponseEntity.badRequest().body(scheduleEmailResponse);
//            }
        	
        	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EET"));
        	cal.clear();
        	cal.setTime(scheduleEmailRequest.getDateTime());
        	Date dateTime = cal.getTime();
        	
//        	Date dateTime = scheduleEmailRequest.getDateTime();
        	System.out.println("trigger dateTime : "+dateTime);
        	System.out.println("Current dateTime : "+new java.util.Date());
             if(dateTime.before(new java.util.Date())) {
            	 
            	 scheduler.pauseAll();
                 ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                         "dateTime must be after current time");
                 return ResponseEntity.badRequest().body(scheduleEmailResponse);
             }

            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true, 
            		jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email", ex);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }

    private JobDetail buildJobDetail(ScheduleEmailRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Date startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(startAt)//.startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}