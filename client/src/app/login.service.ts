import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  http = inject(HttpClient)
  constructor() { }
  signup(username: string, password: string, elemRef: any): Promise<any> {
    const data = new FormData()
      data.set("username", username)
      data.set("password", password)
      data.set("image", elemRef.nativeElement.files[0])
      return firstValueFrom(
        this.http.post<any>('/signup', data)
      )

  }
  storeid(id: string) {
    localStorage.setItem("userid", id)
    console.log(localStorage.getItem("userid"))
    console.log("IT WORKS")
  }

  login(username: string, password: string) {
    const data = new FormData()
    data.set("username", username)
    data.set("password", password)
    return firstValueFrom(
      this.http.post<any>('/login', data)
    )
  }

  getUserDetails(id: any) {
    const params = new HttpParams()
    .set("userid", id)
    return firstValueFrom(
      this.http.get<any>('getuserdetails', { params: params})
    )
  }

  logout() {
    localStorage.clear()
    console.log(localStorage.getItem("userid"))
  }
}
