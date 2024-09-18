import { IdeaSlimDto } from "./ideaSlimDto";
import { UserSlimDto } from "./userSlimDto";

export interface ScoreSheetSlimDto {
    id?: number;
    idea?: IdeaSlimDto
    owner?: UserSlimDto
}