/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryjobscheduler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author bryan
 */
public class EmailHandler 
{
    public static void sendTimetables(String date, String emailPwd)
    {
        EmployeeManager em = EmployeeManager.getInstance();
        ApplicationSettings as = ApplicationSettings.getInstance();
        Employee[] empArray = em.selectEmployees(em.employeeCount(), 0);
        //sender's email
        String from = as.getEmail();
        
        //send through gmail smtp server
        String host = "smtp.gmail.com";
        
        //get system properties
        Properties properties = System.getProperties();
        
        //setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        
        //get session object and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                //wofsunkodmbufvae
                return new PasswordAuthentication(from, emailPwd);
            }
        });
        
        //used to debug SMTP issues
        session.setDebug(false);
        try
        {
            //Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);
            
            //set from: header field of the header
            message.setFrom(new InternetAddress(from));
            
            //set to: header field of the header
            for (Employee emp: empArray)
            {
                if (em.isEmployeeWorking(date, emp.getID()))
                {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emp.getEmail()));
                }
            }
            //Set Subject: header field
            message.setSubject("Work timetable: " + date);
            
            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();             

            File f =new File("/timetables/"+date+".pdf");
            if (f.isFile())
            {
                attachmentPart.attachFile(f);
                textPart.setText("Please find the work timetable for " + date + " attached");
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);


                message.setContent(multipart);

                System.out.println("Sending email...");

                //send message
                Transport.send(message);

                System.out.println("Sent message successfully.");
            }
            else
            {
                System.out.println("Timetable PDF for given date not found");
            }
        }
        catch (IOException e)
        {
            System.out.println("Timetable PDF for given date not found");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        catch (AuthenticationFailedException ex)
        {
            System.out.println("Invalid credentials");
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        catch (MessagingException mex)
        {
            System.err.println(mex.getClass().getName() + ": " + mex.getMessage());
        }
        
        
    }
}
