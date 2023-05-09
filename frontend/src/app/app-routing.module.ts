import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginMainComponent } from './components/login/login.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { AuthGuard } from './services/auth/auth.goard';
import { RegisterComponent } from './components/register/register.component';
import { IdeaBoxPageComponent } from './components/idea-box-page/idea-box-page.component';
import { IdeaBoxCreateEditComponent } from './components/idea-box-page/idea-box-create-edit/idea-box-create-edit.component';
import { IdeaComponent } from './components/idea/idea.component';
import { IdeaCreateComponent } from './components/idea/idea-create-edit/idea-create-edit.component';
import { IdeaEditComponent } from './components/idea/idea-edit/idea-edit.component';
import { IdeaBoxListComponent } from './components/idea-box-page/idea-box-list/idea-box-list.component';
import { IdeaBoxComponent } from './components/idea-box-page/idea-box/idea-box.component';
import { IdeaBoxesManageComponent } from './components/idea-box-page/idea-boxes-manage/idea-boxes-manage.component';

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
    component: IdeaBoxCreateEditComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/manage',
    component: IdeaBoxesManageComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id',
    component: IdeaBoxComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id/edit',
    component: IdeaBoxCreateEditComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea-boxes/:id/create',
    component: IdeaCreateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'idea/:id',
    component: IdeaComponent,
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
