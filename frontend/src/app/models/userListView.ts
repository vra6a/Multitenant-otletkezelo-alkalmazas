import { Role } from './Role';

export interface UserListView {
  id?: number;
  firstName?: string | null;
  lastName?: string;
  email?: string;
  role?: Role;
}
