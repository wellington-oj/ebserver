import { Injectable } from '@angular/core';
import { Device } from '@capacitor/device';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  // uses https://github.com/sneas/ionic-native-http-connection-backend to bypass CR

  constructor(private http: HttpClient) { }

  private basePath = 'http://192.168.1.68:3000/';
  //change the above to your local ebserver ip address and the port.
  private testType = "example";
  private app_id = "io.ionic.starter";
  private framework = "IONIC";

  public async what_now() {
    const path = this.basePath + 'what_now';
    const device_model = await (await Device.getInfo()).model
    try{
      const programString = await this.http.get(
        path, {
          responseType: 'text',
          headers: {
            'device': device_model,
            'test_type': this.testType,
            'application_id': this.app_id,
            'framework': this.framework,
            'activity': '.MainActivity'
          }
        }
      ).toPromise()
      console.log(programString) 
      return programString;
    } catch (error){
      console.log(error.message);
      console.log(error.code);
    }
    return null
  }

  public async log_data(){
    const path = this.basePath + 'logdata';
    const device_model = await (await Device.getInfo()).model;
    try{
      return await this.http.get(
        path, {
          responseType: 'text',
          headers: {
            'device': device_model,
            'test_type': this.testType,
            'application_id': this.app_id,
            'framework': this.framework,
            'activity': '.MainActivity'
          }
        }
      ).toPromise()
    } catch (error){
      console.log(error.message);
    }
    return null
  }

  public async done(){
    const path = this.basePath + 'done';
    const device_model = await (await Device.getInfo()).model;
    try{
      this.http.get(
        path, {
          responseType: 'text',
          headers: {
            'device': device_model,
            'test_type': this.testType,
            'application_id': this.app_id,
            'framework': this.framework,
            'activity': '.MainActivity'
          }
        }
      ).toPromise()
    } catch (error){
      console.log(error.message);
    }
    
  }
}
