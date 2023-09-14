import { Component, ElementRef, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  fb = inject(FormBuilder)
  loginServe = inject(LoginService)
  router = inject(Router)
  @ViewChild('toUpload')
  toUpload!: ElementRef
  failedSignUp!: boolean
  signUpForm!: FormGroup

  ngOnInit(): void {
    this.signUpForm = this.fb.group({
      username: this.fb.control<string>('', Validators.required),
      password: this.fb.control<string>('', Validators.required)
    })
  }

  processForm() {
    const value = this.signUpForm.value
    this.loginServe.signup(value['username'], value['password'], this.toUpload)
      .then(resp => {
        console.log('>>>> resp VOODOO: ', resp)
        this.loginServe.storeid(resp.id)
        this.router.navigate(
          ['/profile']
        )
      })
      .catch(error => {
        console.error('error: ', error)
        this.failedSignUp = true
      })
  }
  navToHome(){
    this.router.navigate(
      ['/']
    )
  }
}
