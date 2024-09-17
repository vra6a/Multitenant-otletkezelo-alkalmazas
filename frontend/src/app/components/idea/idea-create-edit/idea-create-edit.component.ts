import { Component, Input, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Observable, filter, map, startWith } from 'rxjs';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { TagDto } from 'src/app/models/dto/tagDto';
import { Judgement } from 'src/app/models/judgement';
import { IdeaBoxSlimDto } from 'src/app/models/slimDto/ideaBoxSlimDto';
import { IdeaSlimDto } from 'src/app/models/slimDto/ideaSlimDto';
import { TagSlimDto } from 'src/app/models/slimDto/tagSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { WebResponse } from 'src/app/models/webResponse';
import { AuthService } from 'src/app/services/auth/auth.service';
import { IdeaService } from 'src/app/services/idea.service';
import { IdeaBoxService } from 'src/app/services/ideaBox.service';
import { SnackBarService } from 'src/app/services/snackBar.service';
import { TagService } from 'src/app/services/tag.service';
import { UserService } from 'src/app/services/user.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-create',
  templateUrl: './idea-create-edit.component.html',
  styleUrls: ['./idea-create-edit.component.scss'],
})
export class IdeaCreateEditComponent implements OnInit {
  constructor(
    private fb: UntypedFormBuilder,
    private auth: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private ideaBoxService: IdeaBoxService,
    private ideaService: IdeaService,
    private userService: UserService,
    private snackBar: SnackBarService,
    private tagService: TagService
  ) {}

  user: UserSlimDto = null;
  isEdit: boolean = false;

  ideaBox: IdeaBoxSlimDto = null;
  ideaBoxId: string = '';
  idea: IdeaDto = null;
  ideaId: string = '';

  tags: TagSlimDto[] = [];
  selectedTags: TagSlimDto[] = [];
  tagCtrl = new FormControl('');
  filteredTags: TagSlimDto[];
  addButtonVisible: boolean = false;
  createButtonVisible: boolean = false;

  juries: UserSlimDto[] = [];
  selectedJuries: UserSlimDto[] = [];
  juryCtrl = new FormControl('');
  filteredJuries: UserSlimDto[] = [];
  addJuryButtonVisible: boolean = true;

  IdeaForm = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    owner: this.user,
    tags: [[]],
  });

  ngOnInit(): void {
    this.user = this.auth.getCurrentUser();
    this.ideaBoxId = this.route.snapshot.paramMap.get('ideaBoxId');
    this.ideaId = this.route.snapshot.paramMap.get('id');
    this.getAllTags();
    this.getJuries();
    this.getDefaultJuries();

    if (this.ideaId) {
      this.isEdit = true;
      this.getIdea();
    } else {
      this.isEdit = false;
      this.getIdeaBox();
    }

    this.tagCtrl.valueChanges.subscribe((value) => {
      this.filteredTags = this._filterTags(value);
    });
    this.juryCtrl.valueChanges.subscribe((value) => {
      this.filteredJuries = this._filterJuries(value);
      console.log(this.selectedJuries);
    });
  }

  create() {
    let idea = this.IdeaForm.value;
    idea.owner = this.user;
    idea.status = 'SUBMITTED';
    idea.creationDate = '';
    idea.judgement = Judgement.NOT_JUDGED
    idea.ideaBox = this.ideaBox;
    idea.tags = this.selectedTags;
    idea.requiredJuries = this.selectedJuries;
    this.ideaService
      .createIdea$(idea)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaSlimDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea-boxes/' + this.ideaBoxId);
        } else {
          this.snackBar.error(res.message);
          this.router.navigateByUrl('/idea-boxes/' + this.ideaBoxId);
        }
      });
  }

  edit() {
    let idea = this.idea;
    idea.description = this.IdeaForm.value.description;
    idea.title = this.IdeaForm.value.title;
    idea.tags = this.selectedTags;
    idea.requiredJuries = this.selectedJuries;
    this.ideaService
      .editIdea$(this.idea.id.toString(), idea)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.router.navigateByUrl('/idea/' + this.ideaId);
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  getIdeaBox() {
    this.ideaBoxService
      .getIdeaBoxSlim$(this.ideaBoxId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaBoxSlimDto>) => {
        if (res.code == 200) {
          this.ideaBox = res.data;
          this._filterTags('');
          this._filterJuries('');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  getIdea() {
    this.ideaService
      .getIdea$(this.ideaId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<IdeaDto>) => {
        if (res.code == 200) {
          this.idea = res.data;
          this.selectedTags = res.data.tags;
          this.selectedJuries = res.data.requiredJuries;
          this.fillForm(res.data);
          this._filterTags('');
          this._filterJuries('');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  removeTag(tag: TagSlimDto) {
    const index = this.selectedTags.indexOf(tag);
    if (index >= 0) {
      this.selectedTags.splice(index, 1);
    }
  }

  removeJury(jury: UserSlimDto) {
    console.log(jury);
    const index = this.selectedJuries.indexOf(jury);
    console.log(index);
    if (index >= 0) {
      this.selectedJuries.splice(index, 1);
    }
  }

  addNewTag() {
    let tag = this.tags.find((t: TagSlimDto) => t.name == this.tagCtrl.value);
    if (!this.selectedTags.find((t: TagSlimDto) => t.name == tag.name)) {
      this.selectedTags.push(tag);
      this.tagCtrl.setValue('');
    } else {
      this.snackBar.error('this tag has already been added to the idea!');
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

  createNewTag() {
    let tag = {
      id: 0,
      name: this.tagCtrl.value,
      taggedIdeas: [],
    };
    this.tagService
      .createTag$(tag)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<TagDto>) => {
        if (res.code == 200) {
          this.snackBar.ok(res.message);
          this.getAllTags();
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private fillForm(i: IdeaDto) {
    this.IdeaForm.controls['description'].setValue(i.description);
    this.IdeaForm.controls['title'].setValue(i.title);
  }

  private _filterTags(value: string): TagSlimDto[] {
    const filterValue = value.toLowerCase();

    if (filterValue === '') {
      this.createButtonVisible = false;
      this.addButtonVisible = false;
      return this.tags;
    }
    let values = this.tags.filter((option) =>
      option.name.toLowerCase().includes(filterValue)
    );
    this.createButtonVisible = values.length == 0;
    this.addButtonVisible = values.length > 0;
    return values;
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

  private getAllTags() {
    this.tagService
      .getTags$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<TagSlimDto[]>) => {
        if (res.code == 200) {
          this.tags = res.data;
          this.filteredTags = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getJuries() {
    this.userService
      .getJuries$()
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto[]>) => {
        if (res.code == 200) {
          this.juries = res.data;
          this.filteredJuries = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  private getDefaultJuries() {
    this.ideaService
      .getDefaultJuries$(this.ideaBoxId)
      .pipe(untilDestroyed(this))
      .subscribe((res: WebResponse<UserSlimDto[]>) => {
        if (res.code == 200) {
          this.selectedJuries = res.data;
        } else {
          this.snackBar.error(res.message);
        }
      });
  }
}
