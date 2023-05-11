import { CommentDto } from '../models/dto/commentDto';
import { UserSlimDto } from '../models/slimDto/userSlimDto';

export default class Utils {
  static createLikeText(likes: UserSlimDto[], id: number): string {
    let likeText: string = '';
    let likeCount: number = likes?.length;
    let lastLike: UserSlimDto = null;

    if (likeCount == 0) {
      lastLike = null;
    } else {
      lastLike = likes.at(likeCount - 1);
    }

    switch (likeCount) {
      case 0:
        likeText = 'Be the first one to like this!';
        break;
      case 1:
        if (lastLike?.id == id) {
          likeText = 'You liked this';
        } else {
          likeText =
            lastLike?.firstName + ' ' + lastLike?.lastName + ' liked this';
        }
        break;
      default:
        if (lastLike?.id == id) {
          likeText = 'You and ' + (likeCount - 1) + ' other liked this';
        } else {
          likeText =
            lastLike?.firstName +
            ' ' +
            lastLike?.lastName +
            ' and ' +
            (likeCount - 1) +
            ' other liked this';
        }
    }
    return likeText;
  }
}
