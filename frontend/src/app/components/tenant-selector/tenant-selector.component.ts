import { Component, OnInit } from '@angular/core';
import { Tenant, TenantService } from 'src/app/services/multitenancy/tenantService';

@Component({
  selector: 'app-tenant-selector',
  templateUrl: './tenant-selector.component.html',
  styleUrls: ['./tenant-selector.component.scss']
})
export class TenantSelectorComponent implements OnInit {

  constructor(
    private tenantService: TenantService
  ) { }

  ngOnInit(): void {
  }

  selectedTenant: string = null

  tenants: Tenant[] = [
    {value: 'tenant1', viewValue: 'First Tenant'},
    {value: 'tenant2', viewValue: 'Second Tenant'},
  ];

  changeTenant(event) {
    console.log(event.value)
    this.tenantService.clearTenantId()
    this.tenantService.setTenantId(event.value)
    console.log("tenant value set, value: " + event.value)
  }

}
