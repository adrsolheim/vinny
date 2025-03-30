package no.vinny.nightfly.domain.batch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Keg {
    Long id;
    Double capacity;
    String brand;
    String serialNumber;
    PurchaseCondition purchaseCondition;
    String note;
}