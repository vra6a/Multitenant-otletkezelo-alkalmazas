import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root',
  })
  export class TenantService {
    private tenantId: string | null = null;
  
    constructor() {}
    setTenantId(tenantId: string): void {
      this.tenantId = tenantId;
      console.log("tenant value set in TenantService, value: " + this.tenantId)
    }

    getTenantId(): string | null {
      console.log("tenant value get: " + this.tenantId)
      return this.tenantId;
    }
  
    clearTenantId(): void {
      console.log("tenant value cleared: " + this.tenantId)
      this.tenantId = null;
    }
  }

  export interface Tenant {
    value: string;
    viewValue: string;
  }