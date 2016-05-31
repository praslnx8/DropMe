package com.prasilabs.dropme.backend.utils;

import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by prasi on 23/3/16.
 */
public class TemplateEngine
{
    private static final String TAG = TemplateEngine.class.getSimpleName();

    public static String getEmailContentFromString(String text, Map<String, String> emailParams)
    {
        if(text.length() > 0)
        {
            for (Map.Entry<String, String> entry : emailParams.entrySet())
            {
                String value = entry.getValue();
                if(value == null)
                {
                    value = "NA";
                }
                text = text.replace(encrypt(entry.getKey()), value);
            }
        }
        else
        {
            ConsoleLog.s(TAG, "template is empty or not found : ( ");
        }

        return text;
    }

    public static String getEmailContentFromTemplate(String fileName, Map<String, String> emailParams)
    {
        String text = getTextFromStream(fileName);

        return getEmailContentFromString(text, emailParams);
    }

    private static String getTextFromStream(String fileName)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            InputStream inputStream = new FileInputStream("WEB-INF/resources/" + fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String content;
            while ((content = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private static String encrypt(String variable)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("${");
        stringBuilder.append(variable);
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private static String fileToString(File file)
    {
        String text = "";
        try {
            Scanner fileReader = new Scanner(file);
            fileReader.useDelimiter("\\Z"); // \Z means EOF.
            text = fileReader.next();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return text;
    }
}
