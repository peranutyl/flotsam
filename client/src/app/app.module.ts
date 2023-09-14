import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { MaterialModule } from './material/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ProfileComponent } from './profile/profile.component';
import { AddgameComponent } from './addgame/addgame.component';
import { GamepageComponent } from './gamepage/gamepage.component';
import { OAuthModule } from 'angular-oauth2-oidc';
import { SignupComponent } from './signup/signup.component';
import { NavigationComponent } from './navigation/navigation.component';



  const appRoutes: Routes = [
    { path: '', component: HomeComponent, title: 'HOEM' },
    { path: 'profile', component: ProfileComponent, title: 'PROEFILE' },
    { path: 'signup', component: SignupComponent, title: 'SIGNUP' },
    { path: 'addgame', component: AddgameComponent, title: 'ADDGAEM' },
    { path: 'gamedetails/:id', component: GamepageComponent, title: "GAEMDETAILS"},


  // default in switch, has to last
  { path: '**', redirectTo: '/', pathMatch: 'prefix' }
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProfileComponent,
    AddgameComponent,
    GamepageComponent,
    SignupComponent,
    NavigationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes),
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    OAuthModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
