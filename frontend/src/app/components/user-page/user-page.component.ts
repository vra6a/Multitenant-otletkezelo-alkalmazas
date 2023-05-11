import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/dto/userDto';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SnackBarService } from 'src/app/services/snackBar.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss'],
})
export class UserPageComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: SnackBarService
  ) {}

  currentUser: User = null;

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
  }
}
