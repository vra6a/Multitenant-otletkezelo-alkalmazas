import { WebData } from './webData';

export interface WebResponse {
  code?: number;
  message?: string;
  data?: WebData;
}
