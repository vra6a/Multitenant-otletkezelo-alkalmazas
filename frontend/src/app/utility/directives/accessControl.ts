import { Directive, Input, OnInit, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Directive({
  selector: '[accessControl]',
})
export class AccessControlDirective implements OnInit {
  @Input('permission') permission: string;
  constructor(private elementRef: ElementRef, private auth: AuthService) {}

  ngOnInit() {
    this.elementRef.nativeElement.style.display = 'none';
    this.checkAccess();
  }
  checkAccess() {
    const role: string = this.auth.getRole();
    this.elementRef.nativeElement.style.display =
      this.permission === role ? 'block' : 'none';
  }
}
