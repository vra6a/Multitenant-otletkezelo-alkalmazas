import { IdeaSlimDto } from '../slimDto/ideaSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';

export interface CommentDto {
  id?: number;
  creationDate?: Date;
  text?: string;
  owner?: UserSlimDto;
  idea?: IdeaSlimDto;
  likes?: UserSlimDto[];
  isEdited?: boolean;
}
