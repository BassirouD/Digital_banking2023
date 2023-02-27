import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../../models/customer.model";

@Injectable({
    providedIn: 'root'
})
export class CustomerManagerService {

    host = 'http://localhost:8085'

    constructor(private httpClient: HttpClient) {
    }

    getAllCustomers(): Observable<Array<Customer>> {
        return this.httpClient.get<Array<Customer>>(this.host + '/customers')
    }

    searchCustomer(keyword: string): Observable<Array<Customer>> {
        return this.httpClient.get<Array<Customer>>(this.host + '/customers/search?keyword=' + keyword)
    }

    saveCustomer(customer: Customer): Observable<Customer> {
        return this.httpClient.post<Customer>(this.host + '/customers', customer)
    }

    deleteCustomer(id: number) {
        return this.httpClient.delete(this.host + '/customers/' + id)
    }
}
