import { Idea } from "./idea";
import { User } from "./user";

export interface IdeaBox {
    id?: number;
    name?: string;
    description?: string;
    startDate?: Date;
    endDate?: Date
    creator?: User
    ideas?: Idea[]
}