import { IdeaSlimDto } from "../../slimDto/ideaSlimDto";
import { ScoreSheetDto } from "../scoreScheetDto";

export interface IdeaScoreSheets {
    idea: IdeaSlimDto
    scoreSheet: ScoreSheetDto[]
}