import { UserSlimDto } from './userSlimDto';

export interface CommentSlimDto {
  id?: number;
  creationDate?: Date;
  text?: string;
  owner?: UserSlimDto;
}
