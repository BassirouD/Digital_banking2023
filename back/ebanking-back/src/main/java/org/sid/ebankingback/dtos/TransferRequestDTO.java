package org.sid.ebankingback.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String accountIdSource;
    private String accountIDestination;
    private double amount;
    private String description;
}
