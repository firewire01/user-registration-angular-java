import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { AccountService } from '@app/_services';

@Component({ templateUrl: 'list-email.component.html' })
export class ListEmailComponent implements OnInit {
    emails?: any[];

    constructor(private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.getAllEmails()
            .pipe(first())
            .subscribe(emails => this.emails = emails);
    }
}