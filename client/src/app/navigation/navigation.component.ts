import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent {
  private router = inject(Router)
  private loginServe = inject(LoginService)
  navToProfile(){
    this.router.navigate(
      ['/profile']
    )
  }
  navToAddGame(){
    this.router.navigate(
      ['/addgame']
    )
  }
  logout(){
    this.loginServe.logout()
    this.router.navigate(
      ['/']
    )

  }

}
