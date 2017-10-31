import { Component, ViewChild, ElementRef } from '@angular/core';

import { NavController } from 'ionic-angular';
import { BackgroundGeolocation } from '@ionic-native/background-geolocation';
import { Geolocation } from '@ionic-native/geolocation';
import { Diagnostic } from '@ionic-native/diagnostic';


@Component({
  selector: 'page-page3',
  templateUrl: 'page3.html'
})
export class Page3 {

  @ViewChild('map') mapElement: ElementRef;
  map: any;
  public posicion: number = 0;

  spinner: any = false;

  constructor(public navCtrl: NavController, public geolocation: Geolocation
    , private backgroundGeolocation: BackgroundGeolocation, private diagnostic: Diagnostic) {

    //this.diagnosticLocation();

    this.loadMap();

    //this.startGPS();
    
  }

  ionloaded(){
  	this.spinner = false;
  }

  loadMap(){


    this.geolocation.getCurrentPosition().then((position)=>{

    let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

    let mapOptions = {
      center: latLng,
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    this.map = new google.maps.Map(this.mapElement.nativeElement, mapOptions);

    let marker = new google.maps.Marker({
    map: this.map,
    animation: google.maps.Animation.DROP,
    position: latLng
    });

    }, (err) => {
      console.log(err);
    });


  }

  startGPS(){

    console.log('Entre');

    let config = {
            desiredAccuracy: 10,
            stationaryRadius: 20,
            distanceFilter: 30,
            debug: true, //  enable this hear sounds for background-geolocation life-cycle.
            interval:100,
            stopOnTerminate: false, // enable this to clear background location settings when the app terminates
    };

  this.backgroundGeolocation.configure(config)
   .subscribe((location) => {

    console.log(location);
    this.posicion= location.longitude;

    // IMPORTANT:  You must execute the finish method here to inform the native plugin that you're finished,
    // and the background-task may be completed.  You must do this regardless if your HTTP request is successful or not.
    // IF YOU DON'T, ios will CRASH YOUR APP for spending too much time in the background.
    //this.backgroundGeolocation.finish(); // FOR IOS ONLY

  }, (err)=>{
    console.log(err);
    this.posicion= err;
  });

  // start recording location
  this.backgroundGeolocation.start();

  }

  stopGPS(){
    this.backgroundGeolocation.stop();

  }

  diagnosticLocation(){
   /*
    this.diagnostic.isLocationAvailable().then( isAvailable => {
               alert("Location available: " + isAvailable);
               Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               this.diagnostic.startActivityForResult((CordovaPlugin) this, intent,1);
        });*/
  }

  doRefresh(refresher) {
    console.log('Begin async operation', refresher);

    setTimeout(() => {
      console.log('Async operation has ended');
      this.loadMap();
      refresher.complete();
    }, 2000);
  }

}