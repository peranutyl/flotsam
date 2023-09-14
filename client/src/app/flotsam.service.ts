import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlotsamService {

  private http = inject(HttpClient)
  searchsteamgames(searchTerm: string): Promise<any> {
    const params = new HttpParams()
      .set("searchTerm", searchTerm)

      return firstValueFrom(this.http
        .get<any>('/searchsteamgames', { params: params }))
  }

  getgamedetails(id: any): Promise<any> {
    const params = new HttpParams()
    .set("id", id)
    return firstValueFrom(this.http
      .get<any>('/getgamedetails', { params: params }))

  }
  getreviews(id: any, reviewType: string): Promise<any> {
    const params = new HttpParams()
    .set("id", id)
    .set("reviewType", reviewType)
    return firstValueFrom(this.http
      .get<any>('/getreviews', { params: params }))
  }

  getreviewsummary(id: any): Promise<any> {
    const params = new HttpParams()
    .set("id", id)
    return firstValueFrom(this.http
      .get<any>('/getreviewsummary', {params: params}))
  }
  addgame(userid: any, gameid: any): Promise<any> {
    const params = new HttpParams()
    .set("userid", userid)
    .set("gameid", gameid)
    return firstValueFrom(this.http
      .get<any>('/addmedia',{ params: params}))
  }
  updategame(userid: any, mediaid:any, status: any): Promise<any> {
    const params = new HttpParams()
    .set("userid", userid)
    .set("mediaid", mediaid)
    .set("status", status)
    return firstValueFrom(this.http
      .get<any>('/updateusermedia',{ params: params}))
  }

  getgamelist(userid: any, status: string): Promise<any> {
    const params = new HttpParams()
    .set("userid", userid)
    .set("status", status)
    return firstValueFrom(this.http
      .get<any>('/listofgames',{ params: params}))
  }
  deletegame(userid:any, gameid:any): Promise<any> {
    const params = new HttpParams()
    .set("userid", userid)
    .set("gameid", gameid)
    return firstValueFrom(this.http
      .get<any>('/deletegame',{ params: params}))
  }
}


