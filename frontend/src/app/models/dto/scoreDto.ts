import { IdeaSlimDto } from '../slimDto/ideaSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';

export interface ScoreDto {
  id?: number;
  score?: number;
  type?: string;
  idea?: IdeaSlimDto;
  title?: string;
  owner?: UserSlimDto;
}
