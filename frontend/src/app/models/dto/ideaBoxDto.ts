import { IdeaSlimDto } from '../slimDto/ideaSlimDto';
import { UserSlimDto } from '../slimDto/userSlimDto';

export interface IdeaBoxDto {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Date;
  endDate?: Date;
  creator?: UserSlimDto;
  ideas?: IdeaSlimDto[];
}
