package session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Notreal
 */
//@SessionScoped
public class EmailService {

    @Inject
    private Logger log;

    @Resource(mappedName = "java:/mail/aStore")
    private Session mailSession;

    public void sendConfirmEmail(String to, String subject, String confirmationNumber) {
        String templateFileName = "templates/confirm/confirm.html";
        Document doc = parseTemplate(templateFileName, "UTF-8", "/");
        if (doc != null) {
            processDocument(doc, "#confirmationNumber", confirmationNumber);
            sendEmailFromTemplate(to, subject, doc, templateFileName);
        }

    }

    public void sendActivationEmail(String to, String subject, String link) {
        String templateFileName = "templates/activate/activate.html";
        Document doc = parseTemplate(templateFileName, "UTF-8", "/");
        if (doc != null) {
            processDocument(doc, "#activationLink", "href", link);
            sendEmailFromTemplate(to, subject, doc, templateFileName);
        }

    }

    public void sendEmailFromTemplate(String to, String subject, Document doc, String templateFileName) {
        Map<String, String> imageFileNameMap;
        imageFileNameMap = processImages(doc, templateFileName);
        sendEmail(to, subject, doc.toString(), imageFileNameMap);
    }

    private void sendEmail(String to, String subject, String body, Map<String, String> imageFileNameMap) {
        try {
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("webservice@anrusstrans.ru"));
            Address toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            MimeMultipart multipart = new MimeMultipart("related");
            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html; charset=utf-8");
            // add it
            multipart.addBodyPart(messageBodyPart);
            // second part (images)
            for (String imageFileName : imageFileNameMap.keySet()) {
                // if error on images - send email w/out images
                try {
                    Path imagePath = Paths.get(imageFileName);
                    String mimeType = Files.probeContentType(imagePath);
                    messageBodyPart = new MimeBodyPart();
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream(imageFileName);
                    DataSource ds = new ByteArrayDataSource(is, mimeType);
                    messageBodyPart.setDataHandler(new DataHandler(ds));
                    messageBodyPart.setHeader("Content-ID", "<" + imageFileNameMap.get(imageFileName) + ">");
                    multipart.addBodyPart(messageBodyPart);
                } catch (IOException | MessagingException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private Document parseTemplate(String templateFileName, String charsetName, String baseUri) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(templateFileName)) {
            Document doc = Jsoup.parse(is, charsetName, baseUri);
            return doc;
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private Map<String, String> processImages(Document doc, String templateFileName) {
        Map<String, String> imageFileNameMap = new HashMap<>();
        Path relativePath = Paths.get(templateFileName).getParent();
        Elements images = doc.select("img[src]");
        for (Element image : images) {
            String imageSrc = image.attr("src");
            Path imagePath = Paths.get(imageSrc);
            String imageName = imagePath.getFileName().toString();
            imageFileNameMap.put(relativePath.resolve(imageName).toString(), imageName);
            image.attr("src", "cid:" + imageName);
        }
        return imageFileNameMap;
    }

    private void processDocument(Document doc, String selector, String text) {
        Elements elements = doc.select(selector);
        for (Element element : elements) {
            element.text(text);
        }
    }

    private void processDocument(Document doc, String selector, String attrKey, String attrValue) {
        Elements elements = doc.select(selector);
        for (Element element : elements) {
            element.attr(attrKey, attrValue);
        }
    }

}
