package tn.esprit.tpfoyer.service.impl;

import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.service.INotificationService;

@Service
public class EmailNotificationService implements INotificationService {

    @Override
    public void envoyer() {
        System.out.println("Notification par email");
    }
}