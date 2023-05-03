import { IdeaSlimDto } from '../slimDto/ideaSlimDto';

export interface TagDto {
  id?: number;
  name?: string;
  taggedIdeas?: IdeaSlimDto[];
}
