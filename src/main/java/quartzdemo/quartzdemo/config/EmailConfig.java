package quartzdemo.quartzdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    static String mailHost;

    static int mailPort;

    static String mailUsername;

    static String mailPassword;

    static String mailStmpAuth;

    static String mailStmpStartTlsEnable;

    static String mailProtocol;

    static String mailDebug;


    @Autowired
    public EmailConfig(@Value("${spring.mail.host}") String mailHost,
                       @Value("${spring.mail.port}") int mailPort,
                       @Value("${spring.mail.username}") String mailUsername,
                       @Value("${spring.mail.password}") String mailPassword,
                       @Value("${spring.mail.properties.mail.smtp.auth}") String mailStmpAuth,
                       @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String mailStmpStartTlsEnable,
                       @Value("${mail_protocol}") String mailProtocol,
                       @Value("${mail_debug}") String mailDebug
    ) {
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.mailUsername = mailUsername;
        this.mailPassword = mailPassword;
        this.mailStmpAuth = mailStmpAuth;
        this.mailStmpStartTlsEnable = mailStmpStartTlsEnable;
        this.mailProtocol=mailProtocol;
        this.mailDebug=mailDebug;

    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailProtocol);
        props.put("mail.smtp.auth", mailStmpAuth);
        props.put("mail.smtp.starttls.enable", mailStmpStartTlsEnable);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }
}
