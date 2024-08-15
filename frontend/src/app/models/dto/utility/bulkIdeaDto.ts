import { IdeaBoxSlimDto } from "../../slimDto/ideaBoxSlimDto";
import { IdeaSlimDto } from "../../slimDto/ideaSlimDto";

export interface BulkIdeaDto {
    ideaBox: IdeaBoxSlimDto;
    ideas: IdeaSlimDto[];
}