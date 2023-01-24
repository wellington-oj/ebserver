import { Injectable } from '@angular/core';

import { Filesystem, Directory, Encoding } from '@capacitor/filesystem';

import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor(private http: HttpClient) { }

  private 

  public async writeText(text: string) {
    const chunkSubstr = (str: String, size: number) => {
      const numChunks = Math.ceil(str.length / size);
      const chunks = new Array(numChunks);
      for (let i = 0, o = 0; i < numChunks; ++i, o += size) {
          chunks[i] = (i - 1 == numChunks) ? str.slice(o).toString() : str.slice(o, size).toString();
      }
      return chunks;
    }
    
    const list = chunkSubstr(text, 1000000);
    for (let index = 0; index < list.length; index++) {
      const text = list[index];
      await Filesystem.appendFile({
        path: 'output.txt',
        data: text,
        directory: Directory.Data,
        encoding: Encoding.UTF8,
      })
    }

  }

  public async readFile() {
    const content = await Filesystem.readFile({
      path: 'output.txt',
      directory: Directory.Data,
      encoding: Encoding.UTF8,
    })
    return content.data
  }

  public async deleteFile(){
    await Filesystem.deleteFile({
      path: "output.txt",
      directory: Directory.Data
    })
  }

  public async getFileFromAssets(filename){
    const filePath = `../../../assets/${filename}.txt`
    return await this.http.get(filePath, {responseType: 'text'}).toPromise()
  }

}
function chunkSubstr(str: any, string: any, size: any, number: any) {
  throw new Error('Function not implemented.');
}

