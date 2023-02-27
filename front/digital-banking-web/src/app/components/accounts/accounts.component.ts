import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {CustomerManagerService} from "../../services/customer/customer-manager.service";
import {Router} from "@angular/router";
import {AccountService} from "../../services/account/account.service";
import {catchError, Observable, throwError} from "rxjs";
import {AccountDetail} from "../../models/account.model";

@Component({
    selector: 'app-accounts',
    templateUrl: './accounts.component.html',
    styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {

    accountFormGroup!: FormGroup;
    operationFormGroup!: FormGroup;
    currentPage = 0;
    pageSize = 5;
    account$!: Observable<AccountDetail>;
    errorMessage!: string;

    constructor(private fb: FormBuilder, private accountService: AccountService, private router: Router) {
    }

    ngOnInit(): void {
        this.accountFormGroup = this.fb.group({
            accountId: this.fb.control('')
        })
        this.operationFormGroup = this.fb.group({
            operationType: this.fb.control(null),
            amount: this.fb.control(0),
            description: this.fb.control(null),
            accountDestination: this.fb.control(null),
        })
    }

    handleSearchAccount() {
        let accountId: string = this.accountFormGroup.value.accountId;
        this.account$ = this.accountService.searchAccount(accountId, this.currentPage, this.pageSize)
            .pipe(catchError(err => {
                this.errorMessage = err.error.message;
                return throwError(err);
            }))
    }

    gotoPage(page: number) {
        this.currentPage = page;
        this.handleSearchAccount();
    }

    handleOperation() {
        let accountId: string = this.accountFormGroup.value['accountId']
        let operationType: string = this.operationFormGroup.value['operationType']
        let amount: number = this.operationFormGroup.value['amount']
        let description: string = this.operationFormGroup.value['description']
        let accountDestination: string = this.operationFormGroup.value['accountDestination']
        console.log(operationType)
        if (operationType == 'DEBIT') {
            this.accountService.debit(accountId, amount, description)
                .subscribe({
                    next: (data) => {
                        alert('SUCCESS DEBIT')
                        this.handleSearchAccount();
                        this.operationFormGroup.reset()

                    }, error: err => {
                        console.log(err)
                    }
                })
        } else if (operationType == 'CREDIT') {
            this.accountService.credit(accountId, amount, description)
                .subscribe({
                    next: (data) => {
                        alert('SUCCESS Credit')
                        this.handleSearchAccount();
                        this.operationFormGroup.reset()

                    }, error: err => {
                        console.log(err)
                    }
                })
        } else if (operationType == 'TRANSFER') {
            this.accountService.transfer(accountId, accountDestination, amount, description)
                .subscribe({
                    next: (data) => {
                        alert('SUCCESS Transfer')
                        this.handleSearchAccount();
                        this.operationFormGroup.reset()

                    }, error: err => {
                        console.log(err)
                    }
                })
        }
    }
}
