export class CommentResponse {
  userFirstName: string;
  userLastName: string;
  message: string;
  date: Date;

  constructor(userFirstName: string, userLastName: string, message: string, date: Date) {
    this.userFirstName = userFirstName;
    this.userLastName = userLastName;
    this.message = message;
    this.date = date;
  }
}
