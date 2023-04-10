import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { AuthService } from 'src/app/services/auth/auth.service';

@UntilDestroy()
@Component({
  selector: 'app-idea-box-create',
  templateUrl: './idea-box-create.component.html',
  styleUrls: ['./idea-box-create.component.scss']
})
export class IdeaBoxCreateComponent implements OnInit {

  constructor(
    private fb: UntypedFormBuilder,
    private auth: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  IdeaBoxForm = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
    //creator: ['', Validators.required],
  })

  ngOnInit(): void {
    
  }

  create() {
    console.log(this.IdeaBoxForm.value)
  }

}
