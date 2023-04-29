import { Component, Input, OnInit } from '@angular/core';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';

@Component({
  selector: 'app-details-tab',
  templateUrl: './details-tab.component.html',
  styleUrls: ['./details-tab.component.scss'],
})
export class DetailsTabComponent implements OnInit {
  constructor() {}

  @Input() owner: UserSlimDto = null;
  @Input() status: string = '';
  @Input() creationDate: Date = null;

  ngOnInit(): void {}
}
