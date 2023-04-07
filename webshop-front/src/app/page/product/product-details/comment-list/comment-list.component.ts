import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../../../../constants/backendUrl';
import {CommentResponse} from '../../../../model/CommentResponse';
import tokenService from '../../../../service/TokenService';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Category} from '../../../../model/Category';
import {ToastService} from 'angular-toastify';
import formUtils from '../../../../utils/formUtils';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css'],
})
export class CommentListComponent implements OnInit {
  @Input() productId!: number
  comments: CommentResponse[] = [];
  form!: FormGroup;

  constructor(private http: HttpClient, private fb: FormBuilder, private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      message: ["", Validators.required],
    });

    this.getAllComments();
  }

  getAllComments() {
    const url = backendUrl.PRODUCTS + `/${this.productId}/comments`;
    this.http.get<CommentResponse[]>(url).subscribe({
          next: (res) => {
            this.comments = res;
          },
          error: (err) => {

          },
        },
    )
  }


  postComment() {
    if (!this.form.valid) {
      return;
    }
    const url = backendUrl.PRODUCTS + `/${this.productId}/comments`;
    this.http.post<CommentResponse>(url, this.form.value).subscribe({
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

  protected readonly JSON = JSON;

}
