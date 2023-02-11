package com.example.btrack.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.model.Body;
import com.example.btrack.models.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AlertEmailService {

    private static final String FROM = "dev.btrack@gmail.com";
    private static final String TEMPLATE_DIRECTORY = "templates/";

    private final String accesskey;
    private final String secretkey;

    private final String sendInBlueAccessKey;

    @Autowired
    public AlertEmailService(@Value("${aws_access_key}")String accesskey, @Value("${aws_secret_key}") String secretkey,@Value("${sendInBlueAccessKey}") String sibkey) {
        this.accesskey = accesskey;
        this.secretkey = secretkey;
        this.sendInBlueAccessKey = sibkey;
    }


    public void sendCustomEmailUsingSIB (String destination, String templateName,List<Products> products)
    {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(sendInBlueAccessKey);

        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateName + ".html");
            String htmlBody = new String(inputStream.readAllBytes());
            List<String> htmlproducts = new ArrayList<>();
            String productDivStart = "<div style='margin: 0px auto; max-width: 600px'>"
                    + "<table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width: 100%'>"
                    + "<tbody><tr>"
                    + "<td style='direction: ltr; font-size: 0px; padding: 20px 0; padding-top: 0px; text-align: center;'>";

            String productDivEnd = "</td></tr></tbody></table></div>";
            String productData = "";
            for (Products product: products)
            {
                productData += "<div class='mj-column-per-50 mj-outlook-group-fix' style='font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%'>" +
                        "<table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top' width='100%'>" +
                        "<tbody>" +
                        "<tr>" +
                        "<td align='left' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<div style='font-family:\"Alex Brush\";font-size:1.75rem;font-style:italic;line-height:2.25rem;font-weight:bold;line-height:20px;text-align:left;color:#ef4444;'>" +
                        product.getCategory() +
                        "</div>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;' class='mj-full-width-mobile'>" +
                        "<tbody>" +
                        "<tr>" +
                        "<td style='width:250px' class='mj-full-width-mobile'>" +
                        "<img alt='image description' height='auto' src='"+product.getImage_url()+"' style='border-radius:0.25rem;display:block;outline:none;text-decoration:none;height:auto;width:100%;font-size:16px;' width='250'/>" +
                        "</td>" +
                        "</tr>" +
                        "</tbody>" +
                        "</table>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='left' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<div style='font-family:Nunito, Helvetica, Arial, sans-serif;font-size:1.125rem;line-height:1.75rem;font-weight:400;text-align:left;'>" +
                        product.getName() +
                        "</div>" +
                        "<div style='font-family:Nunito, Helvetica, Arial, sans-serif;font-size:16px;font-weight:400;line-height:20px;text-align:left;color:#54595f;font-size:0.875rem;line-height:1.25rem;'>" +
                        "<p style='margin: 0 0 5px 0'>"+product.getDescription()+"</p>"
                        + "<div class='health' title='Health: "+ product.getHealth() +"%' style='position: relative; width: 100%; height: 0.75rem; margin-top: 0.5rem; border-radius: 0.25rem; border: 1px solid #0369a1;'>"
                        + "<div class='healthanim' style='position: absolute; height: 100%; left: 0px; top: 0px; border-radius: 0.25rem; background: #ef4444; width: "+ product.getHealth() +"%; animation: fill-bar 2s ease-in-out;'></div>"
                        + "</div></td></tr></tbody></table></div>";
                htmlproducts.add(productData);
                productData = "";
            }

            StringBuilder finaldata = new StringBuilder();
            for (int i = 0; i < htmlproducts.size(); i = i + 2) {
                finaldata.append(productDivStart);
                finaldata.append(htmlproducts.get(i));
                if (i + 1 < htmlproducts.size()) {
                    finaldata.append(htmlproducts.get(i + 1));
                }
                finaldata.append(productDivEnd);
            }

            htmlBody = htmlBody.replace("{products}", finaldata);

            TransactionalEmailsApi api = new TransactionalEmailsApi();
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("dev.btrack@gmail.com");
            sender.setName("BTrack Developer");
            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(String.valueOf(destination));
            to.setName(String.valueOf(destination));
            toList.add(to);
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setHtmlContent(htmlBody);
            sendSmtpEmail.setSubject("BTrack - Low Health Items Alert");
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
        }
    }

    public void sendCustomEmail(String to, String templateName, List<Products> products) {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.AP_SOUTH_1)
                    .build();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateName + ".html");
            String htmlBody = new String(inputStream.readAllBytes());
            List<String> htmlproducts = new ArrayList<>();
            String productDivStart = "<div style='margin: 0px auto; max-width: 600px'>"
                    + "<table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width: 100%'>"
                    + "<tbody><tr>"
                    + "<td style='direction: ltr; font-size: 0px; padding: 20px 0; padding-top: 0px; text-align: center;'>";

            String productDivEnd = "</td></tr></tbody></table></div>";
            String productData = "";
            for (Products product: products)
            {
                productData += "<div class='mj-column-per-50 mj-outlook-group-fix' style='font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%'>" +
                        "<table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top' width='100%'>" +
                        "<tbody>" +
                        "<tr>" +
                        "<td align='left' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<div style='font-family:\"Alex Brush\";font-size:1.75rem;font-style:italic;line-height:2.25rem;font-weight:bold;line-height:20px;text-align:left;color:#ef4444;'>" +
                        product.getCategory() +
                        "</div>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;' class='mj-full-width-mobile'>" +
                        "<tbody>" +
                        "<tr>" +
                        "<td style='width:250px' class='mj-full-width-mobile'>" +
                        "<img alt='image description' height='auto' src='"+product.getImage_url()+"' style='border-radius:0.25rem;display:block;outline:none;text-decoration:none;height:auto;width:100%;font-size:16px;' width='250'/>" +
                        "</td>" +
                        "</tr>" +
                        "</tbody>" +
                        "</table>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align='left' style='font-size:0px;padding:10px 25px;word-break:break-word;'>" +
                        "<div style='font-family:Nunito, Helvetica, Arial, sans-serif;font-size:1.125rem;line-height:1.75rem;font-weight:400;text-align:left;'>" +
                        product.getName() +
                        "</div>" +
                        "<div style='font-family:Nunito, Helvetica, Arial, sans-serif;font-size:16px;font-weight:400;line-height:20px;text-align:left;color:#54595f;font-size:0.875rem;line-height:1.25rem;'>" +
                        "<p style='margin: 0 0 5px 0'>"+product.getDescription()+"</p>"
                        + "<div class='health' title='Health: "+ product.getHealth() +"%' style='position: relative; width: 100%; height: 0.75rem; margin-top: 0.5rem; border-radius: 0.25rem; border: 1px solid #0369a1;'>"
                        + "<div class='healthanim' style='position: absolute; height: 100%; left: 0px; top: 0px; border-radius: 0.25rem; background: #ef4444; width: "+ product.getHealth() +"%; animation: fill-bar 2s ease-in-out;'></div>"
                        + "</div></td></tr></tbody></table></div>";
                htmlproducts.add(productData);
                productData = "";
            }

            StringBuilder finaldata = new StringBuilder();
            for (int i = 0; i < htmlproducts.size(); i = i + 2) {
                finaldata.append(productDivStart);
                finaldata.append(htmlproducts.get(i));
                if (i + 1 < htmlproducts.size()) {
                    finaldata.append(htmlproducts.get(i + 1));
                }
                finaldata.append(productDivEnd);
            }

            htmlBody = htmlBody.replace("{products}", finaldata);

            String subject = "BTrack - Low Health Items Alert";
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                    .withSource(FROM);
            client.sendEmail(request);


        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

}
