package no.vinny.nightfly.components.taphouse.api;

public record ConnectBatchRequest (Long tapId,
                                   Long batchUnitId,
                                   boolean oldBatchEmpty) { }
