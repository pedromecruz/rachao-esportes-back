import { Component } from '@angular/core';
import {RouterLink } from '@angular/router';
import { Header } from '../../compartilhado/header/header';
import { Footer } from '../../compartilhado/footer/footer';

@Component({
  selector: 'app-home',
  imports: [Header, Footer, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

}