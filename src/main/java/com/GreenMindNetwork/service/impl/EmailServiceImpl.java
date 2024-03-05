package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.EmailResponse;
import com.GreenMindNetwork.payloads.UserDto;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    private String setOtp=null;
    @Override
    public boolean sendEmailOTP(String to,String message) {
        boolean flag=false;
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.auth",true);
        String subject="Green Mind Network";
        String from="koushikj4297@gmail.com";
        String otp=""
                +"<div style='border:2px solid #78be20;border-radius:10px;height:200px;width:500px;padding:20px;text-align:center;'>"
                +"<h1 style='font-size:40px;'>"
                +"Verification Code is"
                +"</h1>"
                +"<br>"
                +"<h1 style='color:#78be20;'>"
                +message
                +"</h1>"
                +"</div>";
        String username="koushikj4297";
        String password="gjxr pvvr jrjr lajw";
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        MimeMessage m=new MimeMessage(session);
        try {
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            m.setContent(otp,"text/html");
            m.setSubject(subject);
            Transport.send(m);
            flag=true;
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return  flag;
    }

    @Override
    public String generateOtp() {
        Random random=new Random();
        int result = random.nextInt(999999);
        String otp = String.format("%06d", result);
        setOtp(otp);
        return  otp;
    }

    public void setOtp(String otp) {
        this.setOtp=otp;
    }

    public  String getOtp(){
        return setOtp;
    }
    @Override
    public Boolean verifyOtp(EmailResponse emailResponse) {
        System.out.println("otp"+getOtp());
        if(getOtp().equals(emailResponse.getOtp())){
            return true;
        }
        return false;
    }

}
