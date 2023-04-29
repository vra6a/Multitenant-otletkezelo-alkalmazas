import { IdeaSlimDto } from '../slimDto/ideaSlimDto';

export interface ScoreDto {
  id?: number;
  score?: number;
  tyoe?: string;
  idea?: IdeaSlimDto;
}
