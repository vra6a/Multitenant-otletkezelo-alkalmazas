import { CommentSlimDto } from '../slimDto/commentSlimDto';
import { IdeaBoxSlimDto } from '../slimDto/ideaBoxSlimDto';
import { ScoreSlimDto } from '../slimDto/scoreSlimDto';
import { TagSlimDto } from '../slimDto/tagSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';

export interface IdeaDto {
  id?: number;
  title?: string;
  description?: string;
  score?: ScoreSlimDto[];
  owner?: UserSlimDto;
  status?: string;
  creationDate?: Date;
  tags?: TagSlimDto[];
  comments?: CommentSlimDto[];
  ideaBox?: IdeaBoxSlimDto;
  likes?: UserSlimDto[];
  requiredJuries?: UserSlimDto[];
}
