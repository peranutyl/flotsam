import { FlotsamService } from './../flotsam.service';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-gamepage',
  templateUrl: './gamepage.component.html',
  styleUrls: ['./gamepage.component.css']
})
export class GamepageComponent {
  gameid: any
  gameDetails: any
  reviewSummary: any
  reviewType!: string
  listOfReviews: any[] = []
  userid!: any
  private activatedRoute = inject(ActivatedRoute)
  private serve = inject(FlotsamService)

  ngOnInit(): void {
    this.gameid = this.activatedRoute.snapshot.params['id']
    this.userid = localStorage.getItem("userid")
    this.serve.getgamedetails(this.gameid)
    .then(resp =>{
      this.gameDetails = resp
      console.log(resp)
    })
    .catch(error =>{
      console.error(error)
    })
    this.serve.getreviewsummary(this.gameid)
    .then(resp =>{
      this.reviewSummary = resp
      console.log(resp)
    })
    .catch(error =>{
      console.error(error)
    })
  }
  getreview(reviewType: string) {
    this.serve.getreviews(this.gameid, reviewType)
    .then(resp =>{
      this.listOfReviews = resp
      console.log(resp)
    })
    .catch(error =>{
      console.error(error)
    })
  }

  addgame() {
    this.serve.addgame(this.userid, this.gameid)
  }

}
