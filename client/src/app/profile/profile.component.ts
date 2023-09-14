import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { FlotsamService } from '../flotsam.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{
  private loginServe = inject(LoginService)
  private flotsamServe = inject(FlotsamService)
  userGames: any[] = []
  userDetails!: any
  id!: any
  ngOnInit() {
    this.id = localStorage.getItem("userid")
    this.loginServe.getUserDetails(this.id)
    .then(resp => {
      this.userDetails = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
    this.flotsamServe.getgamelist(this.id)
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.userGames = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
  }

  gamePending(id:number) {

  }
}
