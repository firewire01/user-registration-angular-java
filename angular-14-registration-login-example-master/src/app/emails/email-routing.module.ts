import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ListEmailComponent } from './list-email.component';

const routes: Routes = [
    {
        path: '', component: ListEmailComponent,
        children: [
            { path: 'emails', component: ListEmailComponent }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EmailsRoutingModule { }