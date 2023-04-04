import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from '../../service/GenericCrudService';
import {User} from '../../model/user';
import {AuthService} from '../../service/auth.service';
import {backendUrl} from '../../constants/backendUrl';
import tokenService from '../../service/TokenService';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent  {


}
