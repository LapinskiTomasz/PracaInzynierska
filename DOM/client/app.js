
function buildPriceUrl (city,fuel,dist) {
  return "http://localhost:8080/api/compare/price" + "?city="+city+"&fuel="+fuel+"&distance="+dist
}
function buildRatingUrl (city,dist) {
  return "http://localhost:8080/api/compare/rating" + "?city="+city+"&distance="+dist
}

const vm = new Vue({
  el: '#app',
  data: {
    results: [],
    isLogged:false,
    byRating:false,
    emptyLoad:false,
    fuel:"PB95",
    city:"Wojnicz",
    distance:30,
    checkedStations:[],
    userPosition:'',
  },
  mounted () {
    if(localStorage.getItem('token')!=null) this.isLogged=true;
    sessionStorage.removeItem('stationID');
    this.emptyLoad = false;
    if(sessionStorage.getItem('city')!=null){
      this.getPosts(sessionStorage.getItem('city'),sessionStorage.getItem('fuel'));
      this.city=sessionStorage.getItem('city');
      this.fuel=sessionStorage.getItem('fuel');
      if(sessionStorage.getItem('byRating') === null) this.byRating=false;
      sessionStorage.removeItem('city');
      sessionStorage.removeItem('fuel');
      sessionStorage.removeItem('byRating');
      sessionStorage.removeItem('positions');
    }
  },
  methods: {
    getPosts(city,fuel) {

      let url
      if(fuel === "Ocena"){
         url = buildRatingUrl(city,this.distance);
         this.byRating = true;
        console.log("PO OCENIE");
      }
      else{
         url = buildPriceUrl(city,fuel,this.distance);
        console.log("PO CENIE");
      }

      axios.get(url).then((response) => {
        this.results = response.data.stationsDTO;
        this.userPosition = response.data.userPosition;
        if(Object.keys(this.results).length === 0){
          this.emptyLoad = true;
        }
      }).catch( error => { console.log(error); });
    },
    login(){
      window.location.href = 'login.html';
      console.log("LOGIN");
    },
    logout(){
      localStorage.removeItem('token');
      this.isLogged=false;
    },
    getComments(id){
      console.log(id);
      sessionStorage.setItem('fuel',this.fuel);
      sessionStorage.setItem('city',this.city);
      sessionStorage.setItem('byRating',this.byRating);
      sessionStorage.setItem('stationID', id);
      window.location.href = 'comments.html';
    },
    register(){
      window.location.href = 'register.html';
    },
    viewMap(){
      sessionStorage.setItem('positions',JSON.stringify(this.checkedStations));
      sessionStorage.setItem('userPosition',JSON.stringify(this.userPosition));
      sessionStorage.setItem('fuel',this.fuel);
      sessionStorage.setItem('city',this.city);
      sessionStorage.setItem('byRating',this.byRating);
      window.location.href = 'map.html';
    }
  }
});
