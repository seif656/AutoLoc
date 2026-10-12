package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceB {

    private final ServiceA serviceA;

    public void execute() {
        System.out.println("Service B");
    }
}