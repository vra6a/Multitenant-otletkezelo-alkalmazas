import { IdeaSlimDto } from '../slimDto/ideaSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';
import { ScoreSheetDto } from './scoreScheetDto';

export interface IdeaBoxDto {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Date;
  endDate?: Date;
  creator?: UserSlimDto;
  ideas?: IdeaSlimDto[];
  defaultRequiredJuries?: UserSlimDto[];
  scoreSheetTemplates?: ScoreSheetDto[];
}
