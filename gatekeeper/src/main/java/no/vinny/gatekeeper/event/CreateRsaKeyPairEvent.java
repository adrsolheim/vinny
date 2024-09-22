package no.vinny.gatekeeper.event;

import org.springframework.context.ApplicationEvent;

public class CreateRsaKeyPairEvent extends ApplicationEvent {
    public CreateRsaKeyPairEvent(Object source) {
        super(source);
    }
}
