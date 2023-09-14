import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  fb = inject(FormBuilder)
  router = inject(Router)
  loginServe = inject(LoginService)
  failedLogin!: boolean

  loginForm!: FormGroup

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: this.fb.control<string>('', Validators.required),
      password: this.fb.control<string>('', Validators.required)
    })
  }

  processForm() {
    const value = this.loginForm.value
    this.loginServe.login(value['username'], value['password'])
      .then(resp => {
        console.log('>>>> HI: ', resp)
        this.loginServe.storeid(resp.id)
        this.router.navigate(
          ['/profile']
        )
      })
      .catch(error => {
        console.error('error: ', error)
        this.failedLogin = true;
      })
  }

  navToProfile(){

  }

  navToSignUp(){
    this.router.navigate(
      ['/signup']
    )
  }
}
