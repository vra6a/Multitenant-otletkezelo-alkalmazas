import { T } from '@angular/cdk/keycodes';
import { Component, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { IdeaBoxDto } from 'src/app/models/dto/ideaBoxDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { DeleteWarningComponent } from '../../popup/delete-warning/delete-warning.component';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-create',
  templateUrl: './idea-box-create-edit.component.html',
  styleUrls: ['./idea-box-create-edit.component.scss'],
})
export class IdeaBoxCreateEditComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private snackBar: SnackBarService,
    private auth: AuthService,
    private router: Router,
    private ideaBoxService: IdeaBoxService,
    private ideaService: IdeaService,
    private userService: UserService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  id: string = '';
  user: UserSlimDto = null;
  ideaBox: IdeaBoxDto = null;
  isEdit: boolean = false;

  displayedColumns: string[] = ['id', 'title', 'status', 'actions'];
  dataSource: IdeaSlimDto[] = [];

  defaultJuries: UserSlimDto[] = [];
  juries: UserSlimDto[] = [];
  selectedJuries: UserSlimDto[] = [];
  juryCtrl = new FormControl('');
  filteredJuries: UserSlimDto[] = [];
  addJuryButtonVisible: boolean = true;

  IdeaBoxForm = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
    creator: this.user,
  });

  ngOnInit(): void {
    this.user = this.auth.getCurrentUser();
    this.id = this.route.snapshot.paramMap.get('id');
    this.IdeaBoxForm.controls['creator'].setValue(this.user);

    this.getJuries();
    this.getDefaultJuries();

    if (this.id) {
      this.isEdit = true;
      this.getIdeaBox();
    } else {
      this.isEdit = false;
    }

    this.juryCtrl.valueChanges.subscribe((value) => {
      this.filteredJuries = this._filterJuries(value);
      console.log(this.selectedJuries);
    });
  }

  create() {
    let ideaBox = this.IdeaBoxForm.value;
    ideaBox.requiredJuries = this.selectedJuries;
    this.ideaBoxService
      .createIdeaBox$(ideaBox)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        console.log(res);
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  edit() {
    this.ideaBoxService
      .editIdeaBox$(this.ideaBox.id.toString(), this.IdeaBoxForm.value)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        console.log(res);
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  editIdea(id: number) {
    this.router.navigateByUrl('/idea/' + id + '/edit');
  }

  deleteIdea(id: number) {
    let dialogRef = this.dialog.open(DeleteWarningComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'Delete') {
        this.ideaService
          .deleteIdea$(id)
          .pipe(untilDestroyed(this))
          .subscribe((res: WebResponse<string>) => {
            if (res.code == 200) {
              this.snackBar.ok(res.message);
              this.getIdeaBox();
            } else {
              this.snackBar.error(res.message);
            }
          });
      }
    });
  }

  private getIdeaBox() {
    this.ideaBoxService
      .getIdeaBox$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxDto>) => {
        if (res.code == 200) {
          console.log(res.data);
          this.ideaBox = res.data;
          this.dataSource = res.data.ideas;
          this.fillForm(res.data);
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  removeJury(jury: UserSlimDto) {
    console.log(jury);
    const index = this.selectedJuries.indexOf(jury);
    console.log(index);
    if (index >= 0) {
      this.selectedJuries.splice(index, 1);
    }
  }

  addNewJury() {
    console.log(this.juryCtrl.value);
    let names = this.juryCtrl.value.split(' ');
    let jury = this.juries.find(
      (u: UserSlimDto) => u.firstName == names[0] && u.lastName == names[1]
    );
    if (!this.selectedJuries.find((u: UserSlimDto) => u.id == jury.id)) {
      this.selectedJuries.push(jury);
      this.juryCtrl.setValue('');
    } else {
      this.snackBar.error('this Jury has already been added to the idea!');
    }
  }

  private _filterJuries(value: string): UserSlimDto[] {
    const filterValue = value.toLowerCase();
    if (filterValue === '') {
      return this.juries;
    }
    let values = this.juries.filter((option) => {
      let name = option.firstName.toLowerCase() + option.lastName.toLowerCase();
      return name.includes(filterValue);
    });
    return values;
  }

  private getJuries() {
    this.userService
      .getJuries$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto[]>) => {
        if (res.code == 200) {
          this.juries = res.data;
          this.filteredJuries = res.data;
          console.log(this.filteredJuries);
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getDefaultJuries() {
    this.ideaService
      .getDefaultJuries$(this.id)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto[]>) => {
        if (res.code == 200) {
          this.selectedJuries = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private fillForm(ib: IdeaBoxDto) {
    this.IdeaBoxForm.controls['name'].setValue(ib.name);
    this.IdeaBoxForm.controls['description'].setValue(ib.description);
    this.IdeaBoxForm.controls['startDate'].setValue(ib.startDate);
    this.IdeaBoxForm.controls['endDate'].setValue(ib.endDate);
  }
}
