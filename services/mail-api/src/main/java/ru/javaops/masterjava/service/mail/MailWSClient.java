package ru.javaops.masterjava.service.mail;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.web.WebStateException;
import ru.javaops.web.WsClient;

import javax.xml.namespace.QName;
import java.util.Set;

/**
 * gkislin
 * 28.11.2016
 */
@Slf4j
public class MailWSClient {
    private static final WsClient<MailService> WS_CLIENT;

    static {
        WS_CLIENT = new WsClient<MailService>(Resources.getResource("wsdl/mailService.wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"),
                MailService.class);

        WS_CLIENT.init("mail", "/mail/mailService?wsdl");
    }


    public static String sendBulkMail(final Set<Addressee> to, final Set<Addressee> cc, final String subject, final String body) throws WebStateException {
        log.info("Send mail to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String status;
        try {
            status = WS_CLIENT.getPort().sendBulkMail(to, cc, subject, body);
            log.info("Sent with status: " + status);
        } catch (Exception e) {
            throw WsClient.getWebStateException(e);
        }
        return status;
    }

    public static GroupResult sendIndividualMails(final Set<Addressee> to, final String subject, final String body) throws WebStateException {
        log.info("Send mail to '" + to + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        GroupResult result;
        try {
            result = WS_CLIENT.getPort().sendIndividualMails(to, subject, body);
        } catch (WebStateException e) {
            throw WsClient.getWebStateException(e);
        }
        log.info("Sent with result: " + result);
        return result;
    }

    public static Set<Addressee> split(String addressees) {
        Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(addressees);
        return ImmutableSet.copyOf(Iterables.transform(split, Addressee::new));
    }
}
