export interface AccountDetail {
    accountID:            string;
    balance:              number;
    currentPage:          number;
    totalPages:           number;
    pageSize:             number;
    accountOperationDTOS: AccountOperationDTO[];
}

export interface AccountOperationDTO {
    id:            number;
    operationDate: Date;
    amount:        number;
    type:          string;
    description:   string;
}
