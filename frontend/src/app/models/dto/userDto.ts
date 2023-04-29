import { CommentSlimDto } from '../slimDto/commentSlimDto';
import { IdeaBoxSlimDto } from '../slimDto/ideaBoxSlimDto';
import { IdeaSlimDto } from '../slimDto/ideaSlimDto';

export interface User {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  role?: string;
  likedIdeas?: IdeaSlimDto[];
  likedComments?: CommentSlimDto[];
  ideas?: IdeaSlimDto[];
  ideaBoxes?: IdeaBoxSlimDto[];
  comments?: CommentSlimDto[];
}
