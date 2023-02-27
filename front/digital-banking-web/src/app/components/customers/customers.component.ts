import {Component, OnInit} from '@angular/core';
import {CustomerManagerService} from "../../services/customer/customer-manager.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../../models/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
    selector: 'app-customers',
    templateUrl: './customers.component.html',
    styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
    customers!: Observable<Array<Customer>>;
    errorMessage!: string;

    searchCustomerForm: FormGroup | undefined;

    constructor(private customerSrv: CustomerManagerService, private fb: FormBuilder, private router: Router) {
    }

    ngOnInit(): void {
        this.onGetCustomers();

        this.searchCustomerForm = this.fb.group({
            keyword: this.fb.control("")
        })
    }

    onGetCustomers() {
        this.customers = this.customerSrv.getAllCustomers()
            .pipe(catchError(err => {
                    this.errorMessage = err.message;
                    return throwError(err);
                })
            );
    }

    /**
     onGetCustomers() {
        this.customerSrv.getAllCustomers()
            .subscribe({
                next: (data) => {
                    this.customers = data;
                },
                error: (err) => {
                    this.errorMessage = err.message;
                    console.log(err)
                }
            })

    }
     **/
    handleSearchCustomers() {
        let kw = this.searchCustomerForm?.value.keyword
        this.customers = this.customerSrv.searchCustomer(kw)
            .pipe(catchError(err => {
                this.errorMessage = err.message;
                return throwError(err);
            }))

    }

    handleDeleteCustomer(customer: Customer) {
        let co = confirm("Are you sure?")
        if (!co) return;
        this.customerSrv.deleteCustomer(customer.id)
            .subscribe({
                next: resp => {
                    // this.onGetCustomers();
                    this.customers = this.customers.pipe(
                        map(data => {
                            let index = data.indexOf(customer);
                            data.slice(index, 1);
                            return data
                        })
                    )
                },
                error: err => {
                    console.log(err)
                }
            })
    }

    handleCustomerAccounts(customer: Customer) {
        this.router.navigateByUrl('/customer-account/' + customer.id, {state: customer})
    }
}
