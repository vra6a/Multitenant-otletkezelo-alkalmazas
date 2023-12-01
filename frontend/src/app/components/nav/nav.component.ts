import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/dto/userDto';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss'],
})
export class NavComponent implements OnInit {
  constructor(private auth: AuthService, private router: Router) {}

  currentUser: User;
  showScoring: boolean = false;

  ngOnInit(): void {
    this.auth.login.subscribe((user) => {
      this.currentUser = user;
      console.log(this.currentUser);
      if (user.role == 'ADMIN' || user.role == 'JURY') {
        this.showScoring = true;
      } else {
        this.showScoring = false;
      }
    });
  }

  logout() {
    this.auth.logOut();
    this.router.navigate(['/login']);
  }
}
