import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginMainComponent } from './components/login/login.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { AuthGuard } from './services/auth/auth.goard';
import { RegisterComponent } from './components/register/register.component';
import { IdeaBoxPageComponent } from './components/idea-box-page/idea-box-page.component';
import { IdeaBoxCreateComponent } from './components/idea-box-page/idea-box-create/idea-box-create.component';
import { IdeaBoxEditComponent } from './components/idea-box-page/idea-box-edit/idea-box-edit.component';
import { IdeaComponent } from './components/idea/idea.component';
import { IdeaCreateComponent } from './components/idea/idea-create/idea-create.component';
import { IdeaEditComponent } from './components/idea/idea-edit/idea-edit.component';
import { IdeaBoxListComponent } from './components/idea-box-page/idea-box-list/idea-box-list.component';
import { IdeaBoxComponent } from './components/idea-box-page/idea-box/idea-box.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'user',
    component: UserPageComponent,
    canActivate: [AuthGuard],
    children: [],
  },
  {
    path: 'idea-boxes',
    component: IdeaBoxPageComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/list',
    component: IdeaBoxListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/create',
    component: IdeaBoxCreateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id',
    component: IdeaBoxComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id/edit',
    component: IdeaBoxEditComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id/create',
    component: IdeaCreateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea',
    component: IdeaComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea/:id',
    component: IdeaEditComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea/:id/edit',
    component: IdeaEditComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    component: LoginMainComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
