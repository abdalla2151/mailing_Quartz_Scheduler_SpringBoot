package quartzdemo.quartzdemo.payload;


import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ScheduleEmailRequest {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

    @NotNull
    private Date dateTime;

//    @NotNull
//    private ZoneId timeZone;

    
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

//	public ZoneId getTimeZone() {
//		return timeZone;
//	}
//
//	public void setTimeZone(ZoneId timeZone) {
//		this.timeZone = timeZone;
//	}
    
    
	
}