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
  userGamesSalvaged: any[] = []
  userGamesPending: any[] = []
  userGamesCompleted: any[] = []
  userDetails!: any
  id!: any
  game: any
  ngOnInit() {
    this.id = localStorage.getItem("userid")
    this.loginServe.getUserDetails(this.id)
    .then(resp => {
      this.userDetails = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
    this.flotsamServe.getgamelist(this.id, 'salvaged')
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.userGamesSalvaged = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
    this.flotsamServe.getgamelist(this.id, 'pending')
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.userGamesPending = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
    this.flotsamServe.getgamelist(this.id, 'completed')
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.userGamesCompleted = resp
    })
    .catch(error => {
      console.error('error: ', error)
    })
  }

  gamePending(gameid:number) {
    console.log(this.id)
    this.flotsamServe.updategame(this.id, gameid, "pending")
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.game = this.userGamesSalvaged.find((game) => game.appid === gameid);
      this.userGamesSalvaged = this.userGamesSalvaged.filter((game) => game.appid !== gameid);
      this.userGamesPending.push(this.game);
      console.log(this.userGamesSalvaged)
    })
    .catch(error => {
      console.error('error: ', error)
    })
  }

  gameCompleted(gameid:number) {
    this.flotsamServe.updategame(this.id, gameid, "completed")
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.game = this.userGamesPending.find((game) => game.appid === gameid);
      this.userGamesPending = this.userGamesPending.filter((game) => game.appid !== gameid);
      this.userGamesCompleted.push(this.game);
    })
    .catch(error => {
      console.error('error: ', error)
    })
  }
  gameDeleted(gameid:number) {
    this.flotsamServe.deletegame(this.id, gameid)
    .then(resp => {
      console.log('>>>> resp VOODOO: ', resp)
      this.userGamesCompleted = this.userGamesCompleted.filter((game) => game.appid !== gameid);
    })
    .catch(error => {
      console.error('error: ', error)
    })
  }
}


