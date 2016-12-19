package ru.javaops.masterjava.service.mail;

import ru.javaops.web.WebStateException;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.Set;

/**
 * gkislin
 * 15.11.2016
 */
@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)
@HandlerChain(file = "mailWsHandlers.xml")
public class MailServiceImpl implements MailService {
    private final MailServiceExecutor mailServiceExecutor = new MailServiceExecutor();

    @Override
    public String sendBulkMail(Set<Addressee> to, Set<Addressee> cc, String subject, String body) throws WebStateException {
        return MailSender.sendMail(to, cc, subject, body);
    }

    @Override
    public GroupResult sendIndividualMails(Set<Addressee> to, String subject, String body) throws WebStateException {
        return mailServiceExecutor.sendToList(to, subject, body);
    }
}