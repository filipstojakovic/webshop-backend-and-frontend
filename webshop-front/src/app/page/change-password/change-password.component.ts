import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../model/User';
import {UserService} from '../../service/user.service';
import tokenService from '../../service/TokenService';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit{

  currentUser: User;

  constructor(private userService:UserService) {
  }

  ngOnInit(): void {
    const userId = tokenService.getIdFromToken();
  }


}
