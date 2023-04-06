import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {backendUrl} from '../../../../constants/backendUrl';
import {CommentResponse} from '../../../../model/CommentResponse';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css'],
})
export class CommentListComponent implements OnInit {
  @Input() productId!: number
  comments: CommentResponse[] = [];


  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
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

  protected readonly JSON = JSON;
}
