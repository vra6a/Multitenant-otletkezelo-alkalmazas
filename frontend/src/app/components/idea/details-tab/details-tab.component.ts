import { Component, Input, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder, Validators } from '@angular/forms';
import { TagSlimDto } from 'src/app/models/slimDto/tagSlimDto';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { Observable } from 'rxjs/internal/Observable';
import { MatChipInputEvent } from '@angular/material/chips';
import { map, startWith } from 'rxjs';

@Component({
  selector: 'app-details-tab',
  templateUrl: './details-tab.component.html',
  styleUrls: ['./details-tab.component.scss'],
})
export class DetailsTabComponent implements OnInit {
  constructor(private fb: UntypedFormBuilder) {}

  @Input() owner: UserSlimDto = null;
  @Input() status: string = '';
  @Input() creationDate: Date = null;
  @Input() tags: TagSlimDto[] = [];

  selectedTags: TagSlimDto[] = [];
  tagCtrl = new FormControl('');
  filteredTags: Observable<TagSlimDto[]>;
  addButtonVisible: boolean = false;

  ngOnInit(): void {
    this.selectedTags.push({ id: 1, name: 'asd' });
    this.selectedTags.push({ id: 2, name: 'haha' });
    this.selectedTags.push({ id: 3, name: 'hehe' });

    this.tags.push({ id: 1, name: 'asd' });
    this.tags.push({ id: 2, name: 'haha' });
    this.tags.push({ id: 3, name: 'hehe' });

    this.filteredTags = this.tagCtrl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter(value || ''))
    );
  }

  private _filter(value: string): TagSlimDto[] {
    const filterValue = value.toLowerCase();

    let values = this.tags.filter((option) =>
      option.name.toLowerCase().includes(filterValue)
    );
    this.addButtonVisible = values.length == 0;
    return values;
  }

  remove(tag: TagSlimDto) {
    const index = this.selectedTags.indexOf(tag);
    console.log(this.selectedTags);

    if (index >= 0) {
      this.selectedTags.splice(index, 1);
    }

    console.log(this.selectedTags);
  }

  addNewTag() {
    console.log(this.tagCtrl.value);
    this.selectedTags.push({ id: 5, name: this.tagCtrl.value });
    this.tags.push({ id: 5, name: this.tagCtrl.value });
    this.tagCtrl.setValue('');
  }

  createNewTag() {}
}
