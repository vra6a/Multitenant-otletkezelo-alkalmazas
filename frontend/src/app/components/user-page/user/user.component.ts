import { Component, Input, OnInit } from '@angular/core';
import { User } from 'src/app/models/dto/userDto';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  constructor() {}

  @Input() user: User = null;

  ngOnInit(): void {
    console.log(this.user);
    console.log('user init');
  }
}
