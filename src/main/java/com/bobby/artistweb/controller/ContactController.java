package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.ContactMe;
import com.bobby.artistweb.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ContactController extends BaseController{

    private final ContactService contactService;

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestPart ContactMe contactMe, HttpServletRequest request) {
        try {
            this.contactService.saveMessage(contactMe);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchMessages")
    @ResponseBody
    public ResponseEntity<List<ContactMe>> fetchMessages(HttpServletRequest request) {
        List<ContactMe> messages = this.contactService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
