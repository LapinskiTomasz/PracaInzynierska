Vue.use(VueGoogleMaps, {
     load: {
       key: 'AIzaSyAVifT9vDLccjOtZlJC9maTz54X1kfaOsA'
     },
     // Demonstrating how we can customize the name of the components
  // installComponents: false,
   });

   document.addEventListener('DOMContentLoaded', function() {
     // Vue.component('gmap-map', VueGoogleMaps.Map);
     // Vue.component('gmap-marker', VueGoogleMaps.Marker);
     // Vue.component('gmap-marker', VueGoogleMaps.Info);


     new Vue({
       el: '#app',
       data: {
         positions:[],
         isLogged:false,
         userPosition:'',
         infoContent: '',
          infoWindowPos: {
            lat: 0,
            lng: 0
          },
          infoWinOpen: false,
          currentMidx: null,
          //optional: offset infowindow so it visually sits nicely on top of our marker
          infoOptions: {
            pixelOffset: {
              width: 0,
              height: -35
            }
          },
         center: {
           lat:'',
           lng: ''
         },
         markers: []
       },

       mounted() {
         this.setPositions();
         if(localStorage.getItem('token')!=null) this.isLogged=true;
       },
      methods: {
        setPositions(){
        this.positions = JSON.parse(sessionStorage.getItem('positions'));
        this.userPosition =JSON.parse(sessionStorage.getItem('userPosition'));
        for(var i = 0; i < this.positions.length; i++){
          this.markers.push({position:{
            lat : this.positions[i].latitude,
            lng :this.positions[i].longitude
          },
        infoText:this.positions[i].description})
          }

          this.markers.push({position:{
            lat : this.userPosition.latitude,
            lng :this.userPosition.longitude
          },
        infoText:this.userPosition.description})

            this.center.lat= this.userPosition.latitude,
            this.center.lng= this.userPosition.longitude

        },
        toggleInfoWindow: function(marker, idx) {

            this.infoWindowPos = marker.position;
            this.infoContent = marker.infoText;

            //check if its the same marker that was selected if yes toggle
            if (this.currentMidx == idx) {
              this.infoWinOpen = !this.infoWinOpen;
            }
            //if different marker set infowindow to open and reset current marker index
            else {
              this.infoWinOpen = true;
              this.currentMidx = idx;

            }
          },

          login(){
            window.location.href = 'login.html';
            console.log("LOGIN");
          },
          logout(){
            localStorage.removeItem('token');
            this.isLogged=false;
            window.location.href = 'index.html';
          },
          register(){
            window.location.href = 'register.html';
          },
      }
     });
   });
