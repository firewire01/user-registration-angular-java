import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { AccountService } from '@app/_services';
import { AlertComponent } from '@app/_components';
// import { Alert, AlertType } from '@app/_models';



@Component({ templateUrl: 'list.component.html' })
export class ListComponent implements OnInit {
    users?: any[];

    constructor(private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.getAll()
            .pipe(first())
            .subscribe(users => this.users = users);
    }

    deleteUser(id: string) {
        const user = this.users!.find(x => x.id === id);
        user.isDeleting = true;
        this.accountService.delete(id)
            .subscribe({
                next: data =>{
                    this.users = this.users!.filter(x => x.id !== id)
                },
                error: error => {
                    alert(error);
                    user.isDeleting = false;
                }
            });
    }
}