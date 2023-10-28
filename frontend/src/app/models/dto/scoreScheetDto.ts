import { IdeaSlimDto } from '../slimDto/ideaSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';
import { IdeaBoxDto } from './ideaBoxDto';
import { ScoreItemDto } from './scoreItemDto';

export interface ScoreSheetDto {
  id?: number;
  scores?: ScoreItemDto[];
  owner?: UserSlimDto;
  idea?: IdeaSlimDto;
  templateFor?: IdeaBoxDto;
}
