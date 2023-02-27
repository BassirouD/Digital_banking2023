import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../../models/customer.model";
import {AccountDetail} from "../../models/account.model";

@Injectable({
    providedIn: 'root'
})
export class AccountService {

    host = 'http://localhost:8085'

    constructor(private httpClient: HttpClient) {
    }

    public searchAccount(accountId: string, page: number, size: number): Observable<AccountDetail> {
        return this.httpClient.get<AccountDetail>(this.host + '/accounts/' + accountId + '/pageOperations?page=' + page + '&size=' + size)
    }

    public debit(accountId: string, amount: number, description: string) {
        let data = {accountId: accountId, amount: amount, description: description}
        return this.httpClient.post(this.host + '/accounts/debit', data)
    }

    public credit(accountId: string, amount: number, description: string) {
        let data = {accountId: accountId, amount: amount, description: description}
        return this.httpClient.post(this.host + '/accounts/credit', data)
    }

    public transfer(accountIdSource: string, accountIDestination: string, amount: number, description: string) {
        let data = {accountIdSource, accountIDestination, amount, description}
        return this.httpClient.post(this.host + '/accounts/transfer', data)
    }
}
