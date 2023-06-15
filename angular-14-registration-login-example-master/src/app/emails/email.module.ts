import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { EmailsRoutingModule } from './email-routing.module';
import { ListEmailComponent } from './list-email.component';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        EmailsRoutingModule
    ],
    declarations: [
        ListEmailComponent
    ]
})
export class EmailModule { }