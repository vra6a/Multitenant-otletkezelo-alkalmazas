import { IdeaBox } from './ideaBox';
import { User } from './user';
import { WebData } from './webData';

export interface WebResponse<T> {
  code?: number;
  message?: string;
  data?: T;
}
