import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSliderModule } from '@angular/material/slider';
import { MatTreeModule } from '@angular/material/tree';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavComponent } from './components/nav/nav.component';
import { IdeaBoxPageComponent } from './components/idea-box-page/idea-box-page.component';
import { UserPageComponent } from './components/user-page/user-page.component';
import { LoginMainComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { IdeaBoxCreateEditComponent } from './components/idea-box-page/idea-box-create-edit/idea-box-create-edit.component';
import { IdeaComponent } from './components/idea/idea.component';
import { IdeaCreateEditComponent } from './components/idea/idea-create-edit/idea-create-edit.component';
import { IdeaListComponent } from './components/idea/idea-list/idea-list.component';
import { IdeaListViewComponent } from './components/idea/idea-list/idea-list-view/idea-list-view.component';
import { IdeaBoxListComponent } from './components/idea-box-page/idea-box-list/idea-box-list.component';
import { IdeaBoxListViewComponent } from './components/idea-box-page/idea-box-list/idea-box-list-view/idea-box-list-view.component';
import { IdeaBoxComponent } from './components/idea-box-page/idea-box/idea-box.component';
import { HeaderInterceptor } from './interceptors/headerHandler';
import { HttpErrorInterceptor } from './interceptors/errorHandler';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { IdeaTabComponent } from './components/idea/idea-tab/idea-tab.component';
import { DetailsTabComponent } from './components/idea/details-tab/details-tab.component';
import { CommentsTabComponent } from './components/idea/comments-tab/comments-tab.component';
import { CommentComponent } from './components/idea/comments-tab/comment/comment.component';
import { IdeaBoxesManageComponent } from './components/idea-box-page/idea-boxes-manage/idea-boxes-manage.component';
import { DeleteWarningComponent } from './components/popup/delete-warning/delete-warning.component';
import { TruncatePipe } from './utility/pipes/truncate';
import { UserComponent } from './components/user-page/user/user.component';
import { AdminComponent } from './components/user-page/admin/admin.component';
import { JuryComponent } from './components/user-page/jury/jury.component';
import { UserIdeaListComponent } from './components/user-page/components/user-idea-list/user-idea-list.component';
import { UserIdeaBoxListComponent } from './components/user-page/components/user-idea-box-list/user-idea-box-list.component';
import { UserPermissionComponent } from './components/user-page/admin/user-permission/user-permission.component';
import { EditRoleComponent } from './components/user-page/admin/user-permission/edit-role/edit-role.component';
import { IdeaApproveDenyPageComponent } from './components/user-page/admin/idea-approve-deny-page/idea-approve-deny-page.component';
import { ReviewedIdeasListComponent } from './components/user-page/components/reviewed-ideas-list/reviewed-ideas-list.component';
import { ScoringComponent } from './components/scoring/scoring.component';
import { CreateScoreSheetComponent } from './components/scoring/create-score-sheet/create-score-sheet.component';
import { ScoreItemFormComponent } from './components/scoring/create-score-sheet/score-item-form/score-item-form.component';
import { TextScoreItemComponent } from './components/scoring/create-score-sheet/score-item-form/text-score-item/text-score-item.component';
import { StarScoreItemComponent } from './components/scoring/create-score-sheet/score-item-form/star-score-item/star-score-item.component';
import { SliderScoreItemComponent } from './components/scoring/create-score-sheet/score-item-form/slider-score-item/slider-score-item.component';
import { ViewScoringTemplateComponent } from './components/idea-box-page/view-scoring-template/view-scoring-template.component';
import { ScoreIdeaComponentComponent } from './components/scoring/score-idea-component/score-idea-component.component';
import { IdeasToScoreComponent } from './components/scoring/ideas-to-score/ideas-to-score.component';
import { StarScoreComponent } from './components/scoring/score-idea-component/star-score/star-score.component';
import { SliderScoreComponent } from './components/scoring/score-idea-component/slider-score/slider-score.component';
import { OtherScoreComponent } from './components/scoring/score-idea-component/other-score/other-score.component';
import { ViewScoredIdeasComponent } from './components/scoring/view-scored-ideas/view-scored-ideas.component';
import { ViewScoresComponent } from './components/scoring/view-scores/view-scores.component';
import { NgxEchartsModule } from 'ngx-echarts';
import * as echarts from 'echarts';
import { ScoredIdeaMoreInfoComponent } from './components/scoring/scored-idea-more-info/scored-idea-more-info.component';
import { ScoredIdeaDetailsComponent } from './components/scoring/scored-idea-details/scored-idea-details.component';
import { ClosedIdeaBoxesComponent } from './components/scoring/closed-idea-boxes/closed-idea-boxes.component';
import { TenantSelectorComponent } from './components/tenant-selector/tenant-selector.component';



@NgModule({
  declarations: [
    TruncatePipe,
    AppComponent,
    NavComponent,
    IdeaBoxPageComponent,
    UserPageComponent,
    LoginMainComponent,
    RegisterComponent,
    IdeaBoxCreateEditComponent,
    IdeaBoxesManageComponent,
    IdeaBoxListComponent,
    IdeaComponent,
    IdeaCreateEditComponent,
    IdeaListComponent,
    IdeaListViewComponent,
    IdeaBoxListViewComponent,
    IdeaBoxComponent,
    IdeaTabComponent,
    DetailsTabComponent,
    CommentsTabComponent,
    CommentComponent,
    DeleteWarningComponent,
    UserComponent,
    AdminComponent,
    JuryComponent,
    UserIdeaListComponent,
    UserIdeaBoxListComponent,
    UserPermissionComponent,
    EditRoleComponent,
    IdeaApproveDenyPageComponent,
    ReviewedIdeasListComponent,
    ScoringComponent,
    CreateScoreSheetComponent,
    ScoreItemFormComponent,
    TextScoreItemComponent,
    StarScoreItemComponent,
    SliderScoreItemComponent,
    ViewScoringTemplateComponent,
    ScoreIdeaComponentComponent,
    IdeasToScoreComponent,
    StarScoreComponent,
    SliderScoreComponent,
    OtherScoreComponent,
    ViewScoredIdeasComponent,
    ViewScoresComponent,
    ScoredIdeaMoreInfoComponent,
    ScoredIdeaDetailsComponent,
    ClosedIdeaBoxesComponent,
    TenantSelectorComponent,
  ],
  imports: [
    FormsModule,
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
    MatExpansionModule,
    MatButtonModule,
    MatTabsModule,
    MatChipsModule,
    MatAutocompleteModule,
    MatTableModule,
    MatDialogModule,
    MatSliderModule,
    NgxEchartsModule.forRoot({ echarts }),
    MatTreeModule,
    CdkTreeModule,
    MatStepperModule,
    MatSlideToggleModule,
  ],
  schemas: [
    NO_ERRORS_SCHEMA
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HeaderInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
