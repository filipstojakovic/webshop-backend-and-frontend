import {Component, OnInit} from '@angular/core';
import tokenService from '../../service/TokenService';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../service/auth.service';
import {paths} from '../../constants/paths';
import {LoginEmitterService} from '../../service/login-emitter.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  id: number | null = null;

  constructor(private http: HttpClient,
              private auth: AuthService,
              private loginEmiterService: LoginEmitterService,
  ) {
  }

  ngOnInit(): void {
    this.loginEmiterService.loggedInEvent.subscribe(() => {
      this.id = tokenService.getIdFromToken();
    })

  }

  isLoggedIn(): boolean {
    const token = tokenService.getTokenFromStorage();
    return token != null && token != "";
  }

  logoutClick() {
    this.auth.logout();
  }

  protected readonly paths = paths;
}
