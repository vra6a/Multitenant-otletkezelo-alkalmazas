import { Component, Input, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Observable, map, startWith } from 'rxjs';
import { IdeaDto } from 'src/app/models/dto/ideaDto';
import { TagDto } from 'src/app/models/dto/tagDto';
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

    if (this.ideaId) {
      this.isEdit = true;
      this.getIdea();
    } else {
      this.isEdit = false;
      this.getIdeaBox();
    }

    this.tagCtrl.valueChanges.subscribe((value) => {
      this.filteredTags = this._filter(value);
    });
  }

  create() {
    let idea = this.IdeaForm.value;
    idea.owner = this.user;
    idea.status = 'SUBMITTED';
    idea.creationDate = '';
    idea.ideaBox = this.ideaBox;
    idea.tags = this.selectedTags;
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
          this._filter('');
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
          this.fillForm(res.data);
          this._filter('');
        } else {
          this.snackBar.error(res.message);
        }
      });
  }

  remove(tag: TagSlimDto) {
    const index = this.selectedTags.indexOf(tag);
    if (index >= 0) {
      this.selectedTags.splice(index, 1);
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

  private _filter(value: string): TagSlimDto[] {
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
}
