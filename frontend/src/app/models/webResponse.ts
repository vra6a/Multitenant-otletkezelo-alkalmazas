export interface WebResponse<T> {
  code?: number;
  message?: string;
  data?: T;
}
