import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerManagerService} from "../../services/customer/customer-manager.service";
import {Customer} from "../../models/customer.model";
import {Router} from "@angular/router";

@Component({
    selector: 'app-new-customer',
    templateUrl: './new-customer.component.html',
    styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
    newCustomerFormGroup!: FormGroup;

    constructor(private fb: FormBuilder, private customerSrv: CustomerManagerService, private router: Router) {
    }

    ngOnInit(): void {
        this.newCustomerFormGroup = this.fb.group({
            name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
            email: this.fb.control(null, [Validators.required, Validators.email])
        })
    }

    handlerSaveCustomer() {
        let customer: Customer = this.newCustomerFormGroup.value;
        this.customerSrv.saveCustomer(customer)
            .subscribe({
                next: data => {
                    alert('Saved');
                    this.newCustomerFormGroup.reset();
                    this.router.navigateByUrl('customers');
                },
                error: err => {
                    console.log(err)
                }
            })
    }
}
