import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginMainComponent } from './components/login/login.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { AuthGuard } from './services/auth/auth.goard';
import { RegisterComponent } from './components/register/register.component';
import { IdeaBoxPageComponent } from './components/idea-box-page/idea-box-page.component';
import { IdeaBoxCreateComponent } from './components/idea-box-page/idea-box-create/idea-box-create.component';
import { IdeaBoxEditComponent } from './components/idea-box-page/idea-box-edit/idea-box-edit.component';
import { IdeaComponent } from './components/idea-box-page/idea/idea.component';
import { IdeaCreateComponent } from './components/idea-box-page/idea/idea-create/idea-create.component';
import { IdeaEditComponent } from './components/idea-box-page/idea/idea-edit/idea-edit.component';

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
    component: IdeaBoxCreateComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'create',
        component: IdeaBoxCreateComponent
      },
      {
        path: 'edit',
        component: IdeaBoxEditComponent
      }
    ],
  },
  {
    path: 'idea',
    component: IdeaComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'create',
        component: IdeaCreateComponent
      },
      {
        path: 'edit',
        component: IdeaEditComponent
      }
    ]
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
