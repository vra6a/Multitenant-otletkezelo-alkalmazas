import { Role } from './Role';

export interface User {
  id?: number;
  firstName?: string | null;
  lastName?: string;
  email?: string;
  role?: Role;
}
