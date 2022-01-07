# dashboard

example on how to use it (on JavaScript):

 //Starting the execution by sending "what_now" to the server.	
 ionViewDidEnter() {
    this.startExecution();
  }


 sendLogData(): void {
    this.http.get('http://' + this.localhostip + ':3000/' + 'logdata', {}, {})
      .then(data => {
        this.checkCorrectData(data.data);
      })
      .catch(error => {
        console.log('error while executing ' + 'logdata');
        console.log(error);
      });
  }
  checkCorrectData(dataParam: string) {
    if (dataParam === 'done') {
      this.http.get('http://' + this.localhostip + ':3000/' + 'done', {}, {})
        .then(data => { })
        .catch(error => {
          console.log('error while executing ' + 'done');
          console.log(error);
        });
    } else { console.log('not done?'); };
  }

  startExecution() {
    this.http.get('http://' + this.localhostip + ':3000/' + 'what_now', {}, {})
      .then(data => {
        this.foo(data.data);
      })
      .catch(error => {
        console.log('error while executing ' + 'what_now');
        console.log(error);
      });
  }
  
  //Starting the execution by sending "what_now" to the server.	
  foo(data: string){
  
  //do your work here
  
  //after you have finished, log the data
  this.sendLogData()
  
  
  }
  
