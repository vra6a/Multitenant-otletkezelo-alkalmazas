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
  @Input() juries: UserSlimDto[] = [];

  ngOnInit(): void {}
}
