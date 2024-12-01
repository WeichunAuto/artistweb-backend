package com.bobby.artistweb.service;

import com.bobby.artistweb.model.ContactMe;
import com.bobby.artistweb.repo.ContactMeRepo;
import com.bobby.artistweb.utils.GmailSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactMeRepo contactMeRepo;
    private final GmailSender gmailSender;

    public void saveMessage(ContactMe contactMe) throws MessagingException {
        contactMe.setDate(new Date());
        this.contactMeRepo.save(contactMe);

        // start a timer schedule to send an email after 5 seconds
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {

            @SneakyThrows
            @Override
            public void run() {
                gmailSender.setContactMe(contactMe);
                gmailSender.sendEmail();
                timer.cancel();
            }
        }, 5000);
    }

    public List<ContactMe> getAllMessages() {
        Sort sort = Sort.by("date").descending();
        return this.contactMeRepo.findAll(sort);
    }
}
