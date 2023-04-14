import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatPaginatorModule } from '@angular/material/paginator';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavComponent } from './components/nav/nav.component';
import { IdeaBoxPageComponent } from './components/idea-box-page/idea-box-page.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { LoginMainComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { IdeaBoxCreateComponent } from './components/idea-box-page/idea-box-create/idea-box-create.component';
import { IdeaBoxEditComponent } from './components/idea-box-page/idea-box-edit/idea-box-edit.component';
import { IdeaComponent } from './components/idea-box-page/idea/idea.component';
import { IdeaCreateComponent } from './components/idea-box-page/idea/idea-create/idea-create.component';
import { IdeaEditComponent } from './components/idea-box-page/idea/idea-edit/idea-edit.component';
import { IdeaListComponent } from './components/idea-box-page/idea/idea-list/idea-list.component';
import { IdeaListViewComponent } from './components/idea-box-page/idea/idea-list/idea-list-view/idea-list-view.component';
import { IdeaBoxListComponent } from './components/idea-box-page/idea-box-list/idea-box-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    IdeaBoxPageComponent,
    UserPageComponent,
    LoginMainComponent,
    RegisterComponent,
    IdeaBoxCreateComponent,
    IdeaBoxEditComponent,
    IdeaBoxListComponent,
    IdeaComponent,
    IdeaCreateComponent,
    IdeaEditComponent,
    IdeaListComponent,
    IdeaListViewComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatCardModule,
    HttpClientModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatInputModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatGridListModule,
    MatPaginatorModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}