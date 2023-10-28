import { ScoreSheetDto } from './scoreScheetDto';

export interface ScoreItemDto {
  id?: number;
  type?: string;
  scoreSheet?: ScoreSheetDto;
  score?: number;
  title?: string;
  text?: string;
}
