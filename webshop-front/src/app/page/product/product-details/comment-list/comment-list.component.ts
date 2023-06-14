import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../../../../constants/backendUrl';
import {CommentResponse} from '../../../../model/CommentResponse';
import tokenService from '../../../../service/TokenService';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastService} from 'angular-toastify';
import formUtils from '../../../../utils/formUtils';
import {Product} from '../../../../model/Product';
import myUtils from '../../../../utils/myUtils';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css'],
})
export class CommentListComponent implements OnInit {
  @Input() product!: Product
  comments: CommentResponse[] = [];
  form!: FormGroup;

  url:string;

  constructor(private http: HttpClient, private fb: FormBuilder, private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.url = backendUrl.PRODUCTS + `/${this.product.id}/comments`;

    this.form = this.fb.group({
      message: ["", Validators.required],
    });

    this.getAllComments();
  }

  getAllComments() {
    this.http.get<CommentResponse[]>(this.url).subscribe({
          next: (res) => {
            this.comments = res;
          },
          error: (err) => {

          },
        },
    )
  }

  getClassBasedOnUser(comment: CommentResponse) {
    return {
      'gray-background': this.product.id === comment.userId,
      'white-background': this.product.id !== comment.userId,
    }
  }


  postComment() {
    if (!this.form.valid) {
      return;
    }
    this.http.post<CommentResponse>(this.url, this.form.value).subscribe({
          next: (res) => {
            this.comments.unshift(res);
            this.form.reset();
            formUtils.clearFormErrors(this.form);
          },
          error: (err) => {
            this.toastService.error("Error posting comment");
          },
        },
    )
  }

  isLoggedIn() {
    return tokenService.getTokenFromStorage();
  }

  protected readonly myUtils = myUtils;
}
