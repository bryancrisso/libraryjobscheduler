/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryjobscheduler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author b-abi-karam
 */
public class ApplicationSettings 
{
    private static ApplicationSettings settingsObj;
    private String filePath = "properties.properties";
    private Properties appProps;
    
    private int slotCount = 5;
    private int viewWindowSize = 5;
    private String email;
    
    public ApplicationSettings()
    {
        appProps = new Properties();
        try 
        {
            appProps.load(new FileInputStream(filePath));
        } 
        catch (FileNotFoundException ex) 
        {
            outputProperties();
            System.out.println("Creating properties.properties file");
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ApplicationSettings.class.getName()).log(Level.WARNING, null, ex);
        }
        email = appProps.getProperty("email");
        if (email == null)
        {
            System.out.println("Email not set.");
        }
        try
        {
            if (appProps.getProperty("slotCount") == null)
            {
                System.out.println("Slot Count not set.");
            }
            else
            {
                slotCount = Integer.parseInt(appProps.getProperty("slotCount"));
            }
        }
        catch (NumberFormatException ex)
        {
            System.out.println("Slot Count set in properties.properties file is invalid");
            Logger.getLogger(ApplicationSettings.class.getName()).log(Level.WARNING, null, ex);
        }
        
        try
        {
            if (appProps.getProperty("viewWindowSize") == null)
            {
                System.out.println("View Window Size not set.");
            }
            else
            {
                viewWindowSize = Integer.parseInt(appProps.getProperty("viewWindowSize"));
            }
        }
        catch (NumberFormatException ex)
        {
            System.out.println("View Window Size set in properties.properties file is invalid");
            Logger.getLogger(ApplicationSettings.class.getName()).log(Level.WARNING, null, ex);
        }
    }
    
    public static ApplicationSettings getInstance()
    {
        if (settingsObj == null)
        {
            settingsObj = new ApplicationSettings();
        }
        return settingsObj;
    }
    
    private void outputProperties()
    {
        try
        {
            appProps.store(new FileWriter(filePath), null);
        }
        catch (IOException ex1) 
        {
            Logger.getLogger(ApplicationSettings.class.getName()).log(Level.WARNING, null, ex1);
        }
    }
    
    public int getSlotCount() {
        return slotCount;
    }

    public int getViewWindowSize() {
        return viewWindowSize;
    }

    public String getEmail() {
        return email;
    }

    public void setSlotCount(int slotCount) 
    {
        appProps.setProperty("slotCount", String.valueOf(slotCount));
        outputProperties();
        this.slotCount = slotCount;
    }

    public void setViewWindowSize(int viewWindowSize) 
    {
        appProps.setProperty("viewWindowSize", String.valueOf(viewWindowSize));
        outputProperties();
        this.viewWindowSize = viewWindowSize;
    }

    public void setEmail(String email) 
    {
        appProps.setProperty("email", email);
        outputProperties();
        this.email = email;
    }
}
