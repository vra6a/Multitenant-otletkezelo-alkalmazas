import { Component, OnInit } from '@angular/core';
import { UserSlimDto } from 'src/app/models/slimDto/userSlimDto';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-scoring',
  templateUrl: './scoring.component.html',
  styleUrls: ['./scoring.component.scss']
})
export class ScoringComponent implements OnInit {

  constructor(
    private authService: AuthService,
  ) { }

  currentUser: UserSlimDto = null
  ngOnInit(): void {
    this.currentUser =  this.authService.getCurrentUser();
  }

}
