import { Component , OnInit } from '@angular/core';

import { HttpService } from '../services/remote/http.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit{

  constructor(private http: HttpService) {}

  async ngOnInit() {

    const response = await this.http.what_now();
    const programList = response.split("-");
    await this.runProgram(programList[0], parseInt(programList[1]));
    await this.http.log_data();
    this.http.done();

  }

  private async runProgram(program: String, parameter: Number){   
    console.log("Running " + program + " with " + parameter + " as input");
    return new Promise( (resolve) => setTimeout(resolve, 5000) );
  }


}
