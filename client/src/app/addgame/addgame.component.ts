import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FlotsamService } from '../flotsam.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-addgame',
  templateUrl: './addgame.component.html',
  styleUrls: ['./addgame.component.css']
})
export class AddgameComponent {
  searchGameForm!: FormGroup
  fb = inject(FormBuilder)
  service = inject(FlotsamService)
  private router = inject(Router)
  gameCards: any[] = []
  mediaType: string = 'game'
  showProgressBar!: boolean;
  userid!: any
  ngOnInit(){
    this.userid = localStorage.getItem("userid")
    this.searchGameForm = this.fb.group({
      searchGame: this.fb.control<string>('')
    })
  }
  onSubmit() {

  }

  searchGame() {
    this.showProgressBar = true;
    const value = this.searchGameForm.value
    console.log(value['searchGame'])
    this.service.searchsteamgames(value['searchGame'])
      .then(resp =>{
        this.gameCards = resp
        this.showProgressBar = false
      })
      .catch(error =>{
        console.error(error)
        this.showProgressBar = false
      })
    console.log(this.mediaType)

  }

  gameDetails(id: number) {
    console.log(id);
    this.router.navigate(
      ['/gamedetails', id]
    )
  }

  addgame(id: number, $event:any) {
    $event.stopPropagation();
    this.service.addgame(this.userid, id)
    .then(resp =>{
      console.log(resp)
      this.gameCards = this.gameCards.filter((game) => game.appid !== id);
    })
    .catch(error =>{
      console.error(error)
    })
    console.log(id)
  }
}
